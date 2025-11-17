package com.dms.entity.mongo;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "files")
public class File {
    @Id
    private String id;
    private String storedName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public File(String id, String storedName, String displayName, String type, String url, String workspaceId, String nid, int size, Date deletedAt) {
        this.id = id;
        this.storedName = storedName;
        this.displayName = displayName;
        this.type = type;
        this.url = url;
        this.workspaceId = workspaceId;
        this.nid = nid;
        this.deleted = false;
        this.createdAt = new Date();
        this.size = size;
        this.deletedAt = deletedAt;
    }

    private String displayName;
    private String type;
    private String url;           // File path or cloud URL
    private String workspaceId;   // Parent workspace or folder ID
    private String nid;           // Owner (user ID)
    private boolean deleted = false;
    private Date createdAt;
    private int size;
    private Date deletedAt;

    public File(String storedName, String type, String url, String workspaceId, String nid) {
        this.storedName = storedName;
        this.type = type;
        this.url = url;
        this.workspaceId = workspaceId;
        this.nid = nid;
        this.deleted = false;
        this.createdAt = new Date();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public File() {
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}