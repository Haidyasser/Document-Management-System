package com.dms.repository.mongo;

import com.dms.entity.mongo.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {
    List<Workspace> findByNidAndParentIdIsNullAndDeletedFalse(String nid);
    List<Workspace> findByParentIdAndDeletedFalse(String parentId);
    List<Workspace> findByParentId(String parentId);

}
