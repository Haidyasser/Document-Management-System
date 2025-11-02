package com.dms.controller;

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
}
