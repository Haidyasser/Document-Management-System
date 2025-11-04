package com.dms.repository.mongo;

import com.dms.entity.mongo.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FolderRepository extends MongoRepository <Folder, String>{

}
