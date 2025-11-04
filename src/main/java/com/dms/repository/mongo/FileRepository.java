package com.dms.repository.mongo;

import com.dms.entity.mongo.File;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {

}
