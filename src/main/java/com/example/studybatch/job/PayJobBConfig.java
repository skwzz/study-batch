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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PayJobBConfig {
    /**
     * CursorItemReader 사용
     */
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int chunkSize = 10;

    @Bean
    public Job payJobB(){
        return jobBuilderFactory.get("payJobB")
                .start(payStepB())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step payStepB(){
        return stepBuilderFactory.get("payStepB")
                .<Pay, Pay>chunk(chunkSize)
                .reader(payItemReaderB())
                // Processor is not required
                .writer(payItemWriterB())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Pay> payItemReaderB(){
        return new JdbcCursorItemReaderBuilder<Pay>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
                .name("payItemReaderB")
                .build();
    }

    private ItemWriter<Pay> payItemWriterB (){
        return list -> {
            for(Pay pay:list){
                log.info("Current Pay = {}", pay);
            }
        };
    }
}
