package com.hantest.migration;

import java.math.BigDecimal;
import java.util.Date;


//--- 來源 Table 2: Order ---
public class Order {
 private Long orderId;
 private Long customerId;
 private BigDecimal amount;
 private Date orderDate;
 // Getters and Setters...
 public Long getOrderId() { return orderId; }
 public void setOrderId(Long orderId) { this.orderId = orderId; }
 public Long getCustomerId() { return customerId; }
 public void setCustomerId(Long customerId) { this.customerId = customerId; }
 public BigDecimal getAmount() { return amount; }
 public void setAmount(BigDecimal amount) { this.amount = amount; }
 public Date getOrderDate() { return orderDate; }
 public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
