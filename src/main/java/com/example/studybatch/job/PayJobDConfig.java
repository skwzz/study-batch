package com.example.studybatch.job;

import com.example.studybatch.entity.Pay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Component
public class PayJobDConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job payJpaPagingItemReaderJobD(Step payJpaPagingItemReaderStepD){
        return jobBuilderFactory.get("payJpaPagingItemReaderJobD")
                .incrementer(new RunIdIncrementer())
                .start(payJpaPagingItemReaderStepD)
                .build();
    }

    @Bean
    public Step payJpaPagingItemReaderStepD(JpaPagingItemReader<Pay> payJpaPagingItemReader, ItemWriter<Pay> payJpaPagingItemWriter){
        return stepBuilderFactory.get("payJpaPagingItemReaderStepD")
                .<Pay, Pay>chunk(chunkSize)
                .reader(payJpaPagingItemReader)
                .writer(payJpaPagingItemWriter)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Pay> payJpaPagingItemReader(){
        return new JpaPagingItemReaderBuilder<Pay>()
                .name("payJpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM Pay p WHERE amount >= 2000")
                .build();
    }

    @Bean
    public ItemWriter<Pay> payJpaPagingItemWriter(){
        return list -> {
            for(Pay pay:list){
                log.info("Current Pay={}", pay);
            }
        };
    }

}
