package com.dms.service.Impl;

import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Folder;
import com.dms.entity.mongo.Workspace;
import com.dms.repository.mongo.FileRepository;
import com.dms.repository.mongo.FolderRepository;
import com.dms.repository.mongo.WorkspaceRepository;
import com.dms.service.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Observable;
import java.util.Optional;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, FileRepository fileRepository, FolderRepository folderRepository) {
        this.workspaceRepository = workspaceRepository;
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public Workspace createWorkspace(Workspace workspace, String nid) {

        workspace.setNid(nid); // attach NID from token
        return workspaceRepository.save(workspace);
    }

    @Override
    public List<Workspace> getUserWorkspaces(String nid) {
        return workspaceRepository.findByNid(nid);
    }

    @Override
    public Workspace addFolder(String workspaceId, Folder folder) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        Folder folder1 = folderRepository.save(folder);
        workspace.getFolders().add(folder1);
        return workspaceRepository.save(workspace);
    }

    @Override
    public Workspace addFile(String workspaceId, File file) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        File file1 = fileRepository.save(file);
        workspace.getFiles().add(file);
        return workspaceRepository.save(workspace);
    }

    @Override
    public Workspace addFileToFolder(String workspaceId, String folderId, File file) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        boolean added = addFileRecursive(workspace.getFolders(), folderId, file);

        if (!added)
            throw new RuntimeException("Folder not found");
        return workspaceRepository.save(workspace);
    }

    private boolean addFileRecursive(List<Folder> folders, String folderId, File file) {
        for (Folder folder : folders) {
            if (folder.getId().equals(folderId)) {
                folder.getFiles().add(file);
                return true;
            }
            if (addFileRecursive(folder.getFolders(), folderId, file)) return true;
        }
        return false;
    }

    @Override
    public Workspace addSubFolder(String workspaceId, String parentFolderId, Folder subFolder) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        boolean added = addSubFolderRecursive(workspace.getFolders(), parentFolderId, subFolder);

        if (!added)
            throw new RuntimeException("Parent folder not found");
        return workspaceRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> getWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return Optional.ofNullable(workspace);
    }

    @Override
    public Workspace deleteFile(String workspaceId, String fileId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(
                () -> new RuntimeException("Workspace not found")
        );
        workspace.getFiles().removeIf(file -> file.getId().equals(fileId));
        return workspaceRepository.save(workspace);
    }

    private boolean addSubFolderRecursive(List<Folder> folders, String parentFolderId, Folder subFolder) {
        for (Folder folder : folders) {
            if (folder.getId().equals(parentFolderId)) {
                folder.getFolders().add(subFolder);
                return true;
            }
            if (addSubFolderRecursive(folder.getFolders(), parentFolderId, subFolder)) return true;
        }
        return false;
    }

    @Override
    public Workspace deleteFolder(String workspaceId, String folderId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        boolean deleted = deleteFolderRecursive(workspace.getFolders(), folderId);

        if (!deleted)
            throw new RuntimeException("Folder not found");

        return workspaceRepository.save(workspace);
    }

    private boolean deleteFolderRecursive(List<Folder> folders, String folderId) {
        if (folders == null) return false;

        // Try to remove folder at this level
        boolean removed = folders.removeIf(folder -> folder.getId().equals(folderId));
        if (removed) return true;

        // Otherwise, go deeper
        for (Folder folder : folders) {
            if (deleteFolderRecursive(folder.getFolders(), folderId)) return true;
        }

        return false;
    }



}
