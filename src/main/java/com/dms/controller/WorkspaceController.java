package com.dms.controller;

import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Folder;
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
import java.util.List;

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

    @PostMapping
    public ResponseEntity<Workspace> addWorkspace(@RequestBody Workspace workspace, @RequestHeader("Authorization") String authHeader) {
        // Extract JWT
        String token = authHeader.substring(7); // remove "Bearer "
        String nid = jwtUtil.extractNid(token); // custom method in your JwtUtil

        Workspace saved = workspaceService.createWorkspace(workspace, nid);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Workspace>> getAllWorkspaces(@RequestHeader("Authorization")  String authHeader) {
        // Extract JWT
        String token = authHeader.substring(7); // remove "Bearer "
        String nid = jwtUtil.extractNid(token); // custom method in your JwtUtil

        List<Workspace> found = workspaceService.getUserWorkspaces(nid);
        return ResponseEntity.ok().body(found);
    }

    @PostMapping("/{workspaceId}/folders")
    public ResponseEntity<Workspace> addFolder(
            @PathVariable String workspaceId,
            @RequestBody Folder folder
    ) {
        Workspace updated = workspaceService.addFolder(workspaceId, folder);
        return ResponseEntity.ok(updated);
    }

    // ✅ Add subfolder inside another folder
    @PostMapping("/{workspaceId}/folders/{parentFolderId}")
    public ResponseEntity<Workspace> addSubFolder(
            @PathVariable String workspaceId,
            @PathVariable String parentFolderId,
            @RequestBody Folder subFolder
    ) {
        Workspace updated = workspaceService.addSubFolder(workspaceId, parentFolderId, subFolder);
        return ResponseEntity.ok(updated);
    }

    // ✅ Add file directly to workspace
    @PostMapping("/{workspaceId}/files")
    public ResponseEntity<?> addFile(
            @PathVariable String workspaceId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // ✅ Create absolute uploads directory (if not exists)
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            java.io.File uploadDirFile = new java.io.File(uploadDir);
            if (!uploadDirFile.exists()) {
                boolean created = uploadDirFile.mkdirs();
                System.out.println("Created uploads directory: " + created);
            }

            // ✅ Unique file name
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.io.File destinationFile = new java.io.File(uploadDir + filename);

            // ✅ Save file
            file.transferTo(destinationFile);

            // ✅ Build file entity
            File newFile = new File();
            newFile.setName(file.getOriginalFilename());
            newFile.setPath("/uploads/" + filename); // relative path for frontend
            newFile.setType(file.getContentType());
            newFile.setSize(file.getSize());

            Workspace updated = workspaceService.addFile(workspaceId, newFile);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }


    // ✅ Add file inside a specific folder
    @PostMapping("/{workspaceId}/folders/{folderId}/files")
    public ResponseEntity<Workspace> addFileToFolder(
            @PathVariable String workspaceId,
            @PathVariable String folderId,
            @RequestBody File file
    ) {
        Workspace updated = workspaceService.addFileToFolder(workspaceId, folderId, file);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable String workspaceId) {
        Workspace found = workspaceService.getWorkspaceById(workspaceId).orElseThrow();
        return ResponseEntity.ok(found);
    }

    // ✅ Delete folder
    @DeleteMapping("/{workspaceId}/folders/{folderId}")
    public ResponseEntity<Workspace> deleteFolder(
            @PathVariable String workspaceId,
            @PathVariable String folderId
    ) {
        Workspace updated = workspaceService.deleteFolder(workspaceId, folderId);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete file
    @DeleteMapping("/{workspaceId}/files/{fileId}")
    public ResponseEntity<Workspace> deleteFile(
            @PathVariable String workspaceId,
            @PathVariable String fileId
    ) {
        Workspace updated = workspaceService.deleteFile(workspaceId, fileId);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/{workspaceId}/files/{fileId}/download")
    public ResponseEntity<?> downloadFile(
            @PathVariable String workspaceId,
            @PathVariable String fileId,
            @RequestHeader("Authorization") String authHeader
    ) throws FileNotFoundException {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);

        Workspace workspace = workspaceService.getWorkspaceById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (!workspace.getNid().equals(nid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

            // Find file by ID
            com.dms.entity.mongo.File targetFile = workspace.getFiles().stream()
                    .filter(f -> f.getId().equals(fileId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("File not found"));

            // Build absolute path
            java.io.File file = new java.io.File(System.getProperty("user.dir") + targetFile.getPath());
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Prepare file stream
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + targetFile.getName())
                    .contentType(MediaType.parseMediaType(targetFile.getType()))
                    .contentLength(file.length())
                    .body(resource);
    }


}
