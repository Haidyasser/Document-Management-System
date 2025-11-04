package com.dms.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "workspaces")
public class Workspace {
    @Id
    private String id;
    private String name;
    private String description;
    private String type;
    private String access;
    private String nid;
    private List<Folder> folders = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private Date createdAt;

    public Workspace(String name, String description, String type, String access, String nid, List<Folder> folders, List<File> files) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.access = access;
        this.nid = nid;
        this.folders = folders;
        this.files = files;
        this.createdAt = new Date();
    }
    public Workspace() {
        this.createdAt = new Date();
    }

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

    public String getNid() {
        return nid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }
}
