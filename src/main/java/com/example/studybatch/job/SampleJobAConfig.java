package com.example.studybatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SampleJobAConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobA(){
        return jobBuilderFactory.get("jobA")
                .incrementer(new RunIdIncrementer())
                .start(stepA())
                .build();
    }

    @Bean
    public Step stepA(){
        return stepBuilderFactory.get("stepA")
                .tasklet((contribution, chunkContext) -> {
                    log.info("[jobA] - STEPNAME : stepA");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
