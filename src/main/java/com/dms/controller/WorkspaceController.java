package com.dms.controller;

import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Folder;
import com.dms.entity.mongo.Workspace;
import com.dms.security.JwtUtil;
import com.dms.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Workspace> addFile(
            @PathVariable String workspaceId,
            @RequestBody File file
    ) {
        Workspace updated = workspaceService.addFile(workspaceId, file);
        return ResponseEntity.ok(updated);
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

}
