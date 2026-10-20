CREATE DATABASE IF NOT EXISTS `seckill-v2` CHARACTER SET utf8mb4;
USE `seckill-v2`;

-- 用户表
CREATE TABLE `t_user` (
                          `user_id` INT AUTO_INCREMENT PRIMARY KEY,
                          `username` VARCHAR(50) NOT NULL UNIQUE,
                          `password` VARCHAR(100) NOT NULL,
                          `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 商品表
CREATE TABLE `t_product` (
                             `product_id` INT AUTO_INCREMENT PRIMARY KEY,
                             `name` VARCHAR(100) NOT NULL,
                             `price` DECIMAL(10,2) NOT NULL,
                             `stock` INT NOT NULL,
                             `seckill_price` DECIMAL(10,2) NOT NULL,
                             `start_time` DATETIME NOT NULL,
                             `end_time` DATETIME NOT NULL,
                             `status` INT DEFAULT 1
);

-- 订单表
CREATE TABLE `t_order` (
                           `order_id` INT AUTO_INCREMENT PRIMARY KEY,
                           `user_id` INT NOT NULL,
                           `product_id` INT NOT NULL,
                           `quantity` INT NOT NULL,
                           `total_price` DECIMAL(10,2) NOT NULL,
                           `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `status` INT DEFAULT 0
);

-- 测试数据
INSERT INTO `t_product` (`name`, `price`, `stock`, `seckill_price`, `start_time`, `end_time`, `status`)
VALUES ('iPhone 15', 5999.00, 100, 4999.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1);