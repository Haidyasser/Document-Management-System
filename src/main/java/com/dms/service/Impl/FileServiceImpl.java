package com.dms.service.Impl;

import com.dms.entity.mongo.File;
import com.dms.repository.mongo.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileServiceImpl implements com.dms.service.FileService {
    private final FileRepository fileRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    public FileServiceImpl(FileRepository fileRepository, MongoTemplate mongoTemplate) {
        this.fileRepository = fileRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<File> getRecentlyDeletedFiles(String nid) {
        return fileRepository.findByNidAndDeletedTrue(nid, Sort.by(Sort.Direction.DESC, "deletedAt"));
    }


    @Override
    public File restoreFile(String id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        file.setDeleted(false);
        file.setDeletedAt(null);
        return fileRepository.save(file);
    }

    @Override
    public void permanentlyDeleteFile(String id) {
        // 1. Fetch record from DB
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // 2. Build full physical file path
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path filePath = Paths.get(uploadDir, file.getStoredName());

        // 3. Delete file from disk
        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (!deleted) {
                System.out.println("⚠ File not found in folder: " + filePath);
            } else {
                System.out.println("✔ File deleted from folder: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from uploads folder", e);
        }

        // 4. Delete MongoDB record
        fileRepository.deleteById(id);
        System.out.println("✔ File record deleted from DB");
    }


    @Override
    public List<File> getFilesByNid(String nid){
        return fileRepository.findByNidAndDeletedFalse(nid);
    }

}



