package com.dms.controller;

import com.dms.dto.WorkspaceTreeDTO;
import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Workspace;
import com.dms.security.JwtUtil;
import com.dms.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final JwtUtil jwtUtil;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService, JwtUtil jwtUtil) {
        this.workspaceService = workspaceService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Create workspace or subfolder
    @PostMapping
    public ResponseEntity<Workspace> createWorkspace(
            @RequestBody Workspace workspace,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);
        System.out.println("token: " + token);
        workspace.setNid(nid);
        Workspace saved = workspaceService.createWorkspace(workspace);
        System.out.println("saved: " + saved);
        return ResponseEntity.ok(saved);
    }

    // ✅ Get all user's root workspaces (no parent)
    @GetMapping("/root")
    public ResponseEntity<List<Workspace>> getRootWorkspaces(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);

        List<Workspace> found = workspaceService.getRootWorkspaces(nid);
        return ResponseEntity.ok(found);
    }

    // ✅ Get subfolders of a workspace (children)
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Workspace>> getSubfolders(@PathVariable String parentId) {
        List<Workspace> children = workspaceService.getSubfolders(parentId);
        return ResponseEntity.ok(children);
    }

    // ✅ Get a single workspace by ID
    @GetMapping("/{workspaceId}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable String workspaceId) {
        Workspace found = workspaceService.getWorkspaceById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return ResponseEntity.ok(found);
    }

    // ✅ Upload file to workspace or subfolder
    @PostMapping("/{workspaceId}/files")
    public ResponseEntity<?> uploadFile(
            @PathVariable String workspaceId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        // Create upload directory
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        java.io.File uploadDirFile = new java.io.File(uploadDir);
        if (!uploadDirFile.exists()) uploadDirFile.mkdirs();

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        java.io.File destinationFile = new java.io.File(uploadDir + filename);
        file.transferTo(destinationFile);

        // Build file entity
        File newFile = new File();
        newFile.setUrl("/uploads/" + filename);
        newFile.setStoredName(filename);
        newFile.setDisplayName(file.getOriginalFilename());
        newFile.setType(file.getContentType());
        newFile.setSize((int) file.getSize());
        newFile.setWorkspaceId(workspaceId);
        //extract nid from token
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);
        newFile.setNid(nid);

        workspaceService.addFile(newFile);
        Map<String, String> response = new HashMap<>();
        response.put("message", "File uploaded successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ Download file
    @GetMapping("/{workspaceId}/files/{fileId}/download")
    public ResponseEntity<?> downloadFile(
            @PathVariable String workspaceId,
            @PathVariable String fileId,
            @RequestHeader("Authorization") String authHeader
    ) throws FileNotFoundException {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);

        // Check workspace access
        Workspace workspace = workspaceService.getWorkspaceById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!workspace.getNid().equals(nid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        File targetFile = workspaceService.getFileById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        java.io.File file = new java.io.File(System.getProperty("user.dir") + targetFile.getUrl());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        System.out.println("file: " + file.getAbsolutePath());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + targetFile.getStoredName())
                .contentType(MediaType.parseMediaType(targetFile.getType()))
                .contentLength(file.length())
                .body(resource);
    }

    // Soft delete workspace (folder)
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<?> softDeleteWorkspace(@PathVariable String workspaceId) {
        workspaceService.softDeleteWorkspace(workspaceId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Workspace marked as deleted");
        return ResponseEntity.ok(response);
    }

    // Soft delete file
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<?> softDeleteFile(@PathVariable String fileId) {
        workspaceService.softDeleteFile(fileId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "File marked as deleted");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{workspaceId}/tree")
    public ResponseEntity<WorkspaceTreeDTO> getWorkspaceTree(@PathVariable String workspaceId) {
        WorkspaceTreeDTO tree = workspaceService.getWorkspaceTree(workspaceId);
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/{workspaceId}/files/{fileId}/preview")
    public ResponseEntity<?> previewFile(
            @PathVariable String workspaceId,
            @PathVariable String fileId,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);

        Workspace workspace = workspaceService.getWorkspaceById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!workspace.getNid().equals(nid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        File fileEntity = workspaceService.getFileById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // FIXED PATH HANDLING
        Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", fileEntity.getStoredName());

        if (!Files.exists(filePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found on server");
        }

        byte[] fileBytes = Files.readAllBytes(filePath);
        String base64Data = Base64.getEncoder().encodeToString(fileBytes);

        return ResponseEntity.ok(new HashMap<>() {{
            put("fileName", fileEntity.getDisplayName());
            put("type", fileEntity.getType());
            put("base64", base64Data);
        }});
    }



    @PutMapping("/{workspaceId}")
    public ResponseEntity<?> updateWorkspace(
            @PathVariable String workspaceId,
            @RequestBody Workspace workspaceUpdates,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);

        Workspace existing = workspaceService.getWorkspaceById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!existing.getNid().equals(nid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        // apply allowed updates (example: name, parentId, description)
        if (workspaceUpdates.getName() != null) existing.setName(workspaceUpdates.getName());
        if (workspaceUpdates.getParentId() != null) existing.setParentId(workspaceUpdates.getParentId());
        if (workspaceUpdates.getDescription() != null) existing.setDescription(workspaceUpdates.getDescription());

        Workspace saved = workspaceService.updateWorkspace(existing);
        return ResponseEntity.ok(saved);
    }


}
