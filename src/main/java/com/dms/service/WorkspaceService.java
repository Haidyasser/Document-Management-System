package com.dms.service;

import com.dms.entity.mongo.Workspace;

import java.util.List;

public interface WorkspaceService {
    Workspace createWorkspace(Workspace workspace, String nid);
    List<Workspace> getUserWorkspaces(String nid);
}
