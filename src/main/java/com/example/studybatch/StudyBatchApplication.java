package com.example.studybatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class StudyBatchApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StudyBatchApplication.class, args);
        int exitCode = SpringApplication.exit(context);
        log.info("EXIT CODE : " + exitCode);
        System.exit(exitCode);
    }

}
