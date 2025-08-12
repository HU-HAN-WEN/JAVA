package com.hantest.migration.config;

import com.hantest.migration.Customer;
import com.hantest.migration.CustomerProfile;
import com.hantest.migration.processing.DataEnrichmentProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class MigrationJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MongoItemWriter<CustomerProfile> mongoItemWriter;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- 1. 設定執行緒池 ---
    @Bean
    public TaskExecutor taskExecutor() {
        // SimpleAsyncTaskExecutor 會為每個任務建立一個新的執行緒
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
        taskExecutor.setConcurrencyLimit(10); // 設定最大併發執行緒數量
        return taskExecutor;
    }

    // --- 2. 設定分區規則 (Partitioner) ---
    @Bean
    public Partitioner partitioner() {
        // 這是一個簡單的分區器，它會將 customer ID 範圍進行切分
        ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
        partitioner.setDataSource(dataSource);
        partitioner.setTable("customers");
        partitioner.setColumn("id");
        return partitioner;
    }
    
    // --- 3. 設定 Worker Step (工作人員步驟) ---
    // 這個 Step 會被多個執行緒並發執行，每個執行緒處理一部分數據
    @Bean
    public Step workerStep() {
        return stepBuilderFactory.get("workerStep")
                .<Customer, CustomerProfile>chunk(1000) // 每個執行緒內，每處理1000筆資料提交一次
                .reader(pagingItemReader(null, null)) // reader 是 Step-scoped，在執行時才建立
                .processor(itemProcessor())
                .writer(mongoItemWriter)
                .build();
    }
    
    // --- 4. 設定 Manager Step (管理者步驟) ---
    // 這個步驟負責切分任務，並將任務分派給 workerStep
    @Bean
    public Step managerStep() {
        return stepBuilderFactory.get("managerStep")
                .partitioner("workerStep", partitioner()) // 指定要管理的 workerStep 和分區器
                .gridSize(4) // 分成 4 個區塊
                .taskExecutor(taskExecutor()) // 使用我們定義的執行緒池
                .build();
    }

    // --- 5. 定義最終的 Job ---
    @Bean
    public Job migrationJob() {
        return jobBuilderFactory.get("customerMigrationJob")
                .start(managerStep())
                .build();
    }

    // --- Step-scoped Beans (執行時才建立的 Bean) ---
    @Bean
    @StepScope // **非常重要**：確保每個執行緒都能拿到自己專屬的 Reader
    public JdbcPagingItemReader<Customer> pagingItemReader(
            @Value("#{stepExecutionContext['minValue']}") Long minValue,
            @Value("#{stepExecutionContext['maxValue']}") Long maxValue) {
        
        System.out.println("讀取數據範圍: " + minValue + " to " + maxValue);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, firstName, lastName, birthday");
        queryProvider.setFromClause("from customers");
        queryProvider.setWhereClause("where id >= " + minValue + " and id <= " + maxValue);

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("pagingItemReader")
                .dataSource(dataSource)
                .fetchSize(1000)
                .rowMapper((rs, rowNum) -> {
                    Customer customer = new Customer();
                    customer.setId(rs.getLong("id"));
                    customer.setFirstName(rs.getString("firstName"));
                    customer.setLastName(rs.getString("lastName"));
                    customer.setBirthday(rs.getTimestamp("birthday"));
                    return customer;
                })
                .queryProvider(queryProvider)
                .build();
    }
    
    @Bean
    @StepScope
    public ItemProcessor<Customer, CustomerProfile> itemProcessor() {
        return new DataEnrichmentProcessor(jdbcTemplate);
    }
}
