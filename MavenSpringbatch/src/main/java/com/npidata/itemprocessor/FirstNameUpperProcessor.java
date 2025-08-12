package com.npidata.itemprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirstNameUpperProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		Customer cus = new Customer();
		cus.setId(item.getId());
		cus.setFirstName(item.getFirstName().toUpperCase());
		cus.setLastName(item.getLastName());
		cus.setBirthday(item.getBirthday());
		return cus;
	}
	
}
