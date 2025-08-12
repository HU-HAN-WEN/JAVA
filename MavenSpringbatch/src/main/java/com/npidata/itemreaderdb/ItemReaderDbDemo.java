package com.npidata.itemreaderdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

@EnableBatchProcessing
@Configuration
public class ItemReaderDbDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("dbJdbcWriter")
	private ItemWriter<? super User> dbJdbcWriter;
	
	@Bean
	public Job itemReaderDbJob()
	{
		return jobBuilderFactory.get("itemReaderDbJob")
				.start(itemReaderDbStep())
				.build();
	}

	@Bean
	public Step itemReaderDbStep() {
		
		return stepBuilderFactory.get("itemReaderDbStep")
				.<User, User>chunk(2)
				.reader(dbJdbcReader())
				.writer(dbJdbcWriter) //創建成成員
				.build();
	}

	@Bean
	@StepScope //指限在範圍之內
//	用JdbcPaging類型來實現從數據庫讀取
	public JdbcPagingItemReader<User> dbJdbcReader() {
		JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<User>(); 
		
//		指定數據源
		reader.setDataSource(dataSource);
		
//		一次取多少條紀錄
		reader.setFetchSize(2);
		
//		把讀取到的一條條紀錄轉換成User對象
		reader.setRowMapper(new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				// 把一條紀錄進行映射成User對象
				User user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setAge(rs.getInt(4));
				return user;
			}
		});
		
//		指定SQL語句
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("id, username, user_password, age"); //指名查詢哪些字段
		provider.setFromClause("from users"); //指定自哪個表中做查詢
		
//		指定根據哪個字段進行排序'
		Map<String, Order> sort = new HashMap<>(1);
		sort.put("id", Order.ASCENDING);
		provider.setSortKeys(sort);
		
		reader.setQueryProvider(provider);
		return reader;
	}

}



