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
    private String parentId;
    private boolean deleted; // optional for soft delete
    private Date createdAt;

    public Workspace(String name, String description, String type, String access, String nid, String parentId) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.access = access;
        this.nid = nid;
        this.parentId = parentId;
        this.deleted = false;
        this.createdAt = new Date();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public void setNid(String nid) {
        this.nid = nid;
    }
}
