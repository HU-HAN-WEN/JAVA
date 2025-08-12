-- 插入一些客戶資料
INSERT INTO customers (id, firstName, lastName, birthday) VALUES (1, 'Han-Wen', 'Chen', '1999-08-15 10:30:00');
INSERT INTO customers (id, firstName, lastName, birthday) VALUES (2, 'David', 'Beckham', '1975-05-02 12:00:00');
INSERT INTO customers (id, firstName, lastName, birthday) VALUES (3, 'Taylor', 'Swift', '1989-12-13 18:45:00');
INSERT INTO customers (id, firstName,lastName, birthday) VALUES (4, 'Elon', 'Musk', '1971-06-28 08:00:00');
INSERT INTO customers (id, firstName,lastName, birthday) VALUES (5, 'Mickey', 'Mouse', '1928-11-18 00:00:00');

-- 為客戶1插入訂單
INSERT INTO orders (customerId, amount, orderDate) VALUES (1, 150.75, '2024-01-10 14:20:00');
INSERT INTO orders (customerId, amount, orderDate) VALUES (1, 88.50, '2024-03-22 19:05:00');

-- 為客戶2插入訂單
INSERT INTO orders (customerId, amount, orderDate) VALUES (2, 2500.00, '2025-02-14 11:11:11');
INSERT INTO orders (customerId, amount, orderDate) VALUES (2, 300.20, '2025-05-01 16:30:00');
INSERT INTO orders (customerId, amount, orderDate) VALUES (2, 450.00, '2025-07-30 21:00:00');

-- 為客戶3插入訂單
INSERT INTO orders (customerId, amount, orderDate) VALUES (3, 1989.12, '2023-12-25 20:00:00');

-- 為客戶4插入訂單 (沒有訂單的客戶)

-- 為客戶5插入訂單
INSERT INTO orders (customerId, amount, orderDate) VALUES (5, 10.00, '1930-01-01 10:00:00');
