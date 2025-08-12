package com.npidata.itemprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdFilterProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		if(item.getId()%2==0)
			return item;
		else
			return null; //相當於把該對象過濾掉
	}
	
	
	
}
