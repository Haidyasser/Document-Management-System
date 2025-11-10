package com.dms.service.Impl;

import com.dms.entity.mongo.File;
import com.dms.repository.mongo.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
        fileRepository.deleteById(id);
    }

    @Override
    public List<File> getFilesByNid(String nid){
        return fileRepository.findByNidAndDeletedFalse(nid);
    }

}



