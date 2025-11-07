package com.dms.entity.mongo;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "files")
public class File {
    @Id
    private String id;
    private String name;
    private String type; // e.g. pdf, docx, png
    private String path; // file storage path or URL
    private long size;
    private String ownerId;
    private boolean deleted = false;
    private Date createdAt;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public File(String name, String type, String path, int size, String ownerId) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.size = size;
        this.ownerId = ownerId;
        this.deleted = false;
        this.createdAt = new Date();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }
}