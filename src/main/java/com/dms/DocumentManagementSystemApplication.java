package com.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dms.repository.mysql")
@EnableMongoRepositories(basePackages = "com.dms.repository.mongo")
public class DocumentManagementSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(DocumentManagementSystemApplication.class, args);
    }

}
