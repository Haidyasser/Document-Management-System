package com.dms.repository.mongo;

import com.dms.entity.mongo.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    List<Workspace> findByNid(String nid);
    Optional<Workspace> findById(String id);
}


