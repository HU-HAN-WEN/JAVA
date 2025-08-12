-- 刪除已存在的資料表，確保每次執行都是乾淨的環境
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

-- 建立客戶資料表
CREATE TABLE customers (
    id BIGINT NOT NULL PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    birthday DATETIME
);

-- 建立訂單資料表
CREATE TABLE orders (
    orderId BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customerId BIGINT,
    amount DECIMAL(10, 2),
    orderDate DATETIME,
    FOREIGN KEY (customerId) REFERENCES customers(id)
);
