package com.dms.controller;

import com.dms.entity.mongo.File;
import com.dms.repository.mongo.FileRepository;
import com.dms.security.JwtUtil;
import com.dms.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileRepository fileRepository;
    private final FileService fileService;
    private final JwtUtil jwtUtil;

    public FileController(FileRepository fileRepository, FileService fileService, JwtUtil jwtUtil) {
        this.fileRepository = fileRepository;
        this.fileService = fileService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user/{nid}")
    public ResponseEntity<List<File>> getFilesByUser(@PathVariable String nid) {
        List<File> files = fileService.getFilesByNid(nid);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<File>> getDeletedFiles(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String nid = jwtUtil.extractNid(token);
        List<File> deletedFiles = fileService.getRecentlyDeletedFiles(nid);
        return ResponseEntity.ok(deletedFiles);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Map<String, Object>> restoreFile(@PathVariable String id) {
        File file = fileService.restoreFile(id);
        return ResponseEntity.ok(Map.of("message", "File restored successfully", "file", file));
    }

    @DeleteMapping("/permanent/{id}")
    public ResponseEntity<Map<String, String>> permanentlyDeleteFile(@PathVariable String id) {
        fileService.permanentlyDeleteFile(id);
        return ResponseEntity.ok(Map.of("message", "File permanently deleted"));
    }

}
