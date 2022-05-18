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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class PayJobCConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final int chunkSize = 1;

    @Bean
    public Job payJobC(Step payStepC) throws Exception{
        return jobBuilderFactory.get("payJobC")
                .incrementer(new RunIdIncrementer())
                .start(payStepC)
                .build();
    }

    @Bean
    public Step payStepC(JdbcPagingItemReader payJdbcPagingItemReaderC) throws Exception{
        return stepBuilderFactory.get("payStepC")
                .<Pay, Pay>chunk(chunkSize)
                .reader(payJdbcPagingItemReaderC)
                .writer(payJdbcPagingItemWriterC())
                .build();
    }

    @Bean
    public JdbcPagingItemReader payJdbcPagingItemReaderC(PagingQueryProvider createQueryProvider) throws Exception {
        Map<String ,Object> parameterValues = new HashMap<>();
        parameterValues.put("amount", 2000);

        return new JdbcPagingItemReaderBuilder<Pay>()
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Pay.class))
                .queryProvider(createQueryProvider)
                .parameterValues(parameterValues)
                .name("payJdbcPagingItemReader")
                .build();
    }

    private ItemWriter<Pay> payJdbcPagingItemWriterC(){
        return list -> {
            for (Pay pay: list) {
                log.info("Current Pay={}", pay);
            }
        };
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception{
        SqlPagingQueryProviderFactoryBean queryProvier = new SqlPagingQueryProviderFactoryBean();
        queryProvier.setDataSource(dataSource);
        queryProvier.setSelectClause("id, amount, tx_name, tx_date_time");
        queryProvier.setFromClause("from pay");
        queryProvier.setWhereClause("where amount >= :amount");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.ASCENDING);

        queryProvier.setSortKeys(sortKeys);
        return queryProvier.getObject();
    }
}


