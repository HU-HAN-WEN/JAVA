package com.hantest.migration.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class DataSourceConfig {

    // 使用 H2 記憶體資料庫來模擬來源 MySQL
    // Spring Boot 會自動偵測並設定
    
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        // schema.sql 會建立 customers 和 orders 資料表
        resourceDatabasePopulator.addScript(new ClassPathResource("schema.sql"));
        // data.sql 會插入 10 萬筆客戶和相關的訂單數據
        resourceDatabasePopulator.addScript(new ClassPathResource("data.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}
