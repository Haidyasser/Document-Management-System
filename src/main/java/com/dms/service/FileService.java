package com.dms.service;

import com.dms.entity.mongo.File;

import java.util.List;

public interface FileService {

    List<File> getRecentlyDeletedFiles(String nid);

    File restoreFile(String id);

    void permanentlyDeleteFile(String id);

    List<File> getFilesByNid(String nid);
}
