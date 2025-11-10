package com.dms.service.Impl;

import com.dms.dto.WorkspaceTreeDTO;
import com.dms.entity.mongo.File;
import com.dms.entity.mongo.Workspace;
import com.dms.repository.mongo.FileRepository;
import com.dms.repository.mongo.WorkspaceRepository;
import com.dms.service.WorkspaceService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final FileRepository fileRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, FileRepository fileRepository) {
        this.workspaceRepository = workspaceRepository;
        this.fileRepository = fileRepository;
    }

    /** ✅ Create workspace or subfolder */
    @Override
    public Workspace createWorkspace(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }

    /** ✅ Get workspace by ID */
    @Override
    public Optional<Workspace> getWorkspaceById(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return Optional.of(workspace);
    }

    public WorkspaceTreeDTO getWorkspaceTree(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        return buildWorkspaceTree(workspace);
    }

    private WorkspaceTreeDTO buildWorkspaceTree(Workspace workspace) {
        WorkspaceTreeDTO dto = new WorkspaceTreeDTO();
        dto.setId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setDescription(workspace.getDescription());
        dto.setType(workspace.getType());
        dto.setAccess(workspace.getAccess());
        dto.setParentId(workspace.getParentId());

        // ✅ find subfolders
        List<Workspace> subFolders = workspaceRepository.findByParentIdAndDeletedFalse(workspace.getId());
        dto.setFolders(subFolders.stream().map(this::buildWorkspaceTree).toList());

        // ✅ find files
        List<File> files = fileRepository.findByWorkspaceIdAndDeletedFalse(workspace.getId());
        dto.setFiles(files);

        return dto;
    }



    /** ✅ Get user’s root workspaces (no parentId) */
    @Override
    public List<Workspace> getRootWorkspaces(String nid) {
        return workspaceRepository.findByNidAndParentIdIsNullAndDeletedFalse(nid);
    }

    /** ✅ Get subfolders of a workspace */
    @Override
    public List<Workspace> getSubfolders(String parentId) {
        return workspaceRepository.findByParentIdAndDeletedFalse(parentId);
    }

    /** ✅ Upload file and link to workspace */
    @Override
    public File addFile(File file) {
        return fileRepository.save(file);
    }

    /** ✅ Get file by ID */
    @Override
    public Optional<File> getFileById(String fileId) {
        return fileRepository.findById(fileId);
    }

    /** ✅ Soft delete workspace */
    @Override
    public void softDeleteWorkspace(String workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        workspace.setDeleted(true);
        System.out.println("workspace deleted: " + workspace.getId());
        workspaceRepository.save(workspace);

        // Optionally soft delete all child workspaces & files
        List<Workspace> children = workspaceRepository.findByParentId(workspaceId);
        for (Workspace child : children) {
            softDeleteWorkspace(child.getId());
        }

        List<File> files = fileRepository.findByWorkspaceIdAndDeletedFalse(workspaceId);
        for (File f : files) {
            f.setDeleted(true);
            f.setDeletedAt(new Date());
            fileRepository.save(f);
        }
    }

    /** ✅ Soft delete file */
    @Override
    public void softDeleteFile(String fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
        file.setDeleted(true);
        file.setDeletedAt(new Date());
        fileRepository.save(file);
    }
}
