package com.hantest.migration.processing;

import com.hantest.migration.Customer;
import com.hantest.migration.CustomerProfile;
import com.hantest.migration.Order;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// 注意：這個類別不是一個 @Component，因為它會被 Worker Step 多次實例化
public class DataEnrichmentProcessor implements ItemProcessor<Customer, CustomerProfile> {

    private final JdbcTemplate jdbcTemplate;

    public DataEnrichmentProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CustomerProfile process(Customer customer) throws Exception {
        // 核心邏輯：根據傳入的 customer ID，查詢他所有的訂單
        List<Order> orders = findOrdersForCustomer(customer.getId());

        // 組裝成目標 MongoDB Document
        CustomerProfile profile = new CustomerProfile();
        profile.setCustomerId(customer.getId());
        profile.setFullName(customer.getFirstName() + " " + customer.getLastName());
        profile.setBirthDate(customer.getBirthday());
        profile.setOrders(orders);
        profile.setTotalOrders(orders.size());

        // 模擬一些耗時的業務計算
        Thread.sleep(10); 

        return profile;
    }

    private List<Order> findOrdersForCustomer(Long customerId) {
        String sql = "SELECT orderId, customerId, amount, orderDate FROM orders WHERE customerId = ?";
        return jdbcTemplate.query(sql, new Object[]{customerId}, new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
                Order order = new Order();
                order.setOrderId(rs.getLong("orderId"));
                order.setCustomerId(rs.getLong("customerId"));
                order.setAmount(rs.getBigDecimal("amount"));
                order.setOrderDate(rs.getTimestamp("orderDate"));
                return order;
            }
        });
    }
}
