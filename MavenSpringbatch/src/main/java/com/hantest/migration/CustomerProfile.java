package com.hantest.migration;

import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//--- 目標 MongoDB Document ---
@Document(collection = "customer_profiles") // 指定要寫入的 Collection 名稱
public class CustomerProfile {
 @Id
 private Long customerId; // 使用原來的 Customer ID 作為 MongoDB 的 _id
 private String fullName;
 private Date birthDate;
 private List<Order> orders; // **核心：嵌入訂單陣列**
 private int totalOrders;
 // Getters and Setters...
 public Long getCustomerId() { return customerId; }
 public void setCustomerId(Long customerId) { this.customerId = customerId; }
 public String getFullName() { return fullName; }
 public void setFullName(String fullName) { this.fullName = fullName; }
 public Date getBirthDate() { return birthDate; }
 public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
 public List<Order> getOrders() { return orders; }
 public void setOrders(List<Order> orders) { this.orders = orders; }
 public int getTotalOrders() { return totalOrders; }
 public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
}
