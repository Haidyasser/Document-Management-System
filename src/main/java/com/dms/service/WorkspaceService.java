package com.dms.service;

import com.dms.entity.mongo.Folder;
import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceService {
    Workspace createWorkspace(Workspace workspace, String nid);
    List<Workspace> getUserWorkspaces(String nid);

    Workspace addFolder(String workspaceId, Folder folder);

    Workspace addFile(String workspaceId, File file);

    Workspace addFileToFolder(String workspaceId, String folderId, File file);

    Workspace addSubFolder(String workspaceId, String parentFolderId, Folder subFolder);

    Optional<Workspace> getWorkspaceById(String workspaceId);

    Workspace deleteFile(String workspaceId, String fileId);

    Workspace deleteFolder(String workspaceId, String folderId);

    void deleteWorkspace(String workspaceId);
}
