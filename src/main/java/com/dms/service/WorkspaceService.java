package com.dms.service;

import com.dms.dto.WorkspaceTreeDTO;
import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceService {
    Workspace createWorkspace(Workspace workspace);
    Optional<Workspace> getWorkspaceById(String workspaceId);

    WorkspaceTreeDTO getWorkspaceTree(String workspaceId);

    List<Workspace> getRootWorkspaces(String nid);
    List<Workspace> getSubfolders(String parentId);
    File addFile(File file);
    Optional<File> getFileById(String fileId);
    void softDeleteWorkspace(String workspaceId);
    void softDeleteFile(String fileId);
}
