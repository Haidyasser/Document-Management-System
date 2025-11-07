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
    public Optional<Workspace> getWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // Filter out deleted items
        workspace.setFiles(workspace.getFiles().stream()
                .filter(file -> !file.isDeleted())
                .toList());

        workspace.setFolders(workspace.getFolders().stream()
                .filter(folder -> !folder.isDeleted())
                .peek(this::filterDeletedItems)
                .toList());

        return Optional.of(workspace);
    }

    private void filterDeletedItems(Folder folder) {
        folder.setFiles(folder.getFiles().stream()
                .filter(file -> !file.isDeleted())
                .toList());
        folder.setFolders(folder.getFolders().stream()
                .filter(sub -> !sub.isDeleted())
                .peek(this::filterDeletedItems)
                .toList());
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
    public Workspace deleteFile(String workspaceId, String fileId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // Try deleting from root
        for (File file : workspace.getFiles()) {
            if (file.getId().equals(fileId)) {
                file.setDeleted(true);
                return workspaceRepository.save(workspace);
            }
        }

        // Try deleting inside folders recursively
        boolean deleted = markFileAsDeleted(workspace.getFolders(), fileId);
        if (!deleted) {
            throw new RuntimeException("File not found");
        }

        return workspaceRepository.save(workspace);
    }

    private boolean markFileAsDeleted(List<Folder> folders, String fileId) {
        if (folders == null) return false;
        for (Folder folder : folders) {
            // Check files inside this folder
            for (File file : folder.getFiles()) {
                if (file.getId().equals(fileId)) {
                    file.setDeleted(true);
                    return true;
                }
            }
            // Go deeper
            if (markFileAsDeleted(folder.getFolders(), fileId)) return true;
        }
        return false;
    }

    @Override
    public Workspace deleteFolder(String workspaceId, String folderId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        boolean deleted = markFolderAsDeleted(workspace.getFolders(), folderId);
        if (!deleted) {
            throw new RuntimeException("Folder not found");
        }

        return workspaceRepository.save(workspace);
    }

    private boolean markFolderAsDeleted(List<Folder> folders, String folderId) {
        if (folders == null) return false;

        for (Folder folder : folders) {
            if (folder.getId().equals(folderId)) {
                folder.setDeleted(true);
                return true;
            }
            if (markFolderAsDeleted(folder.getFolders(), folderId)) return true;
        }

        return false;
    }


    @Override
    public void deleteWorkspace(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // Delete all files and folders from their respective collections
        deleteFilesRecursive(workspace.getFiles());
        deleteFoldersRecursive(workspace.getFolders());

        // Finally, delete the workspace itself
        workspaceRepository.deleteById(workspaceId);
    }

    private void deleteFilesRecursive(List<File> files) {
        if (files == null || files.isEmpty()) return;
        for (File file : files) {
            fileRepository.deleteById(file.getId());
        }
    }

    private void deleteFoldersRecursive(List<Folder> folders) {
        if (folders == null || folders.isEmpty()) return;
        for (Folder folder : folders) {
            // Delete all files inside this folder
            deleteFilesRecursive(folder.getFiles());
            // Recursively delete subfolders
            deleteFoldersRecursive(folder.getFolders());
            // Delete the folder itself
            folderRepository.deleteById(folder.getId());
        }
    }


}
