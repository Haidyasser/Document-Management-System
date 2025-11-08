package com.dms.dto;

import com.dms.entity.mongo.File;

import java.util.List;

public class WorkspaceTreeDTO {
    private String id;
    private String name;
    private String description;
    private String type;
    private String access;
    private String parentId;
    private List<WorkspaceTreeDTO> folders;
    private List<File> files;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<WorkspaceTreeDTO> getFolders() {
        return folders;
    }

    public void setFolders(List<WorkspaceTreeDTO> folders) {
        this.folders = folders;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
