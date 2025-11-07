package com.dms.repository.mongo;

import com.dms.entity.mongo.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, String> {

}
