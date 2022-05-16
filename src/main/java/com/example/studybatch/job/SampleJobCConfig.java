package com.example.studybatch.job;


import com.example.studybatch.exception.MyException;
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
public class SampleJobCConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MyException myException;

    @Bean
    public Job jobC(){
        return jobBuilderFactory.get("jobC")
                .incrementer(new RunIdIncrementer())
                .start(stepC_1())
                .next(stepC_2())
                .build();
    }

    @Bean
    public Step stepC_1() {
        return stepBuilderFactory.get("stepC_1")
                .tasklet((contribution, chunkContext) -> {
                    myException.callMyException();
                    return RepeatStatus.FINISHED; // 실행안됨
                })
                .build();
    }

    @Bean
    public Step stepC_2() {
        return stepBuilderFactory.get("stepC_2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("### [stepC_2] ###");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
