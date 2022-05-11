package com.example.studybatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class StudyBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyBatchApplication.class, args);
    }

}
