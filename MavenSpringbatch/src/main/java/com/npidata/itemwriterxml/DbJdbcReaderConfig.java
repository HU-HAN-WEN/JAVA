package com.npidata.itemwriterxml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
//將此改為implemt spring batch reader介面


@Configuration
public class DbJdbcReaderConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
//	用JdbcPaging類型來實現從數據庫讀取
	public JdbcPagingItemReader<Customer> dbJdbcReader() {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<Customer>(); 
		
//		指定數據源
		reader.setDataSource(dataSource);
		
//		一次取多少條紀錄
		reader.setFetchSize(10);
		
//		把讀取到的一條條紀錄轉換成Customer對象
		reader.setRowMapper(new RowMapper<Customer>() {
			
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// 把一條紀錄進行映射成Customer對象
				Customer customer = new Customer();
				customer.setId(rs.getLong(1));
				customer.setFirstName(rs.getString(2));
				customer.setLastName(rs.getString(3));
				customer.setBirthday(rs.getString(4));
				return customer;
			}
		});
		
//		指定SQL語句
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("id, firstName, lastName, birthday"); //指名查詢哪些字段
		provider.setFromClause("from customer"); //指定自哪個表中做查詢
		
//		指定根據哪個字段進行排序'
		Map<String, Order> sort = new HashMap<>(1);
		sort.put("id", Order.ASCENDING);
		provider.setSortKeys(sort);
		
		reader.setQueryProvider(provider);
		return reader;
	}
}
