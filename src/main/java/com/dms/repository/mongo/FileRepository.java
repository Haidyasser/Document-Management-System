package com.dms.repository.mongo;

import com.dms.entity.mongo.File;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {

    // Already existing
    List<File> findByWorkspaceIdAndDeletedFalse(String id);

    List<File> findByNidAndDeletedTrue(String nid,Sort sort);

    List<File> findByNidAndDeletedFalse(String nid);
}
