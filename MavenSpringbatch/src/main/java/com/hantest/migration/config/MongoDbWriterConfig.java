package com.hantest.migration.config;

import com.hantest.migration.CustomerProfile;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDbWriterConfig {

    @Bean
    public MongoItemWriter<CustomerProfile> mongoItemWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<CustomerProfile>()
                .template(mongoTemplate)
                .collection("customer_profiles") // 再次確認 Collection 名稱
                .build();
    }
}
