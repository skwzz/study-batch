package com.example.studybatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class StudyBatchApplication {

    public static void main(String[] args) {
        int exitCode = 0;
        try{
            SpringApplication.run(StudyBatchApplication.class, args);
        }catch(Exception e){
            exitCode = 5;
        }finally {
            log.info("EXIT CODE : "+exitCode);
        }
    }

}
