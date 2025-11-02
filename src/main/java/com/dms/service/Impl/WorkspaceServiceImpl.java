package com.dms.service.Impl;

import com.dms.entity.mongo.Workspace;
import com.dms.repository.mongo.WorkspaceRepository;
import com.dms.service.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
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
}
