package com.npidata.itemreaderdb;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;


@Component("dbJdbcWriter")
public class DbJdbcWriter implements ItemWriter<User>{

	@Override
	public void write(List<? extends User> items) throws Exception {
		// 增強的for循環來輸出
		for(User user:items) 
		{
			System.out.println(user);
		}
		
	}

}
