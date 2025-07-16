-- Updated product_info table structure with minimum stock threshold
-- This is the updated schema for new installations

DROP TABLE IF EXISTS `product_info`;
CREATE TABLE `product_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `code` varchar(50) NOT NULL,
  `cate_id` int DEFAULT NULL,
  `unit_id` int DEFAULT NULL,
  `price` decimal(15,2) DEFAULT NULL,
  `active_flag` tinyint(1) DEFAULT '1',
  `status` varchar(20) DEFAULT NULL,
  `description` text,
  `expiration_date` date DEFAULT NULL,
  `min_stock_threshold` decimal(10,2) DEFAULT NULL COMMENT 'Minimum stock level to trigger low stock warning',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `cate_id` (`cate_id`),
  KEY `unit_id` (`unit_id`),
  KEY `idx_product_info_min_stock_threshold` (`min_stock_threshold`),
  CONSTRAINT `product_info_ibfk_1` FOREIGN KEY (`cate_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert sample data with minimum stock thresholds
INSERT INTO `product_info` (`name`, `code`, `cate_id`, `unit_id`, `price`, `status`, `description`, `min_stock_threshold`) VALUES
('Laptop Dell Inspiron 15', 'LAPTOP_DELL_001', 1, 1, 15000000.00, 'active', 'Laptop văn phòng Dell Inspiron 15 inch', 5.00),
('Chuột không dây Logitech', 'MOUSE_LOGI_001', 2, 1, 250000.00, 'active', 'Chuột không dây Logitech MX Master 3', 20.00),
('Bàn phím cơ Keychron K2', 'KEYBOARD_KEY_001', 2, 1, 2500000.00, 'active', 'Bàn phím cơ Keychron K2 RGB', 10.00);

-- Create the stock monitoring view
CREATE OR REPLACE VIEW `v_product_stock_status` AS
SELECT 
    pi.id,
    pi.name,
    pi.code,
    pi.min_stock_threshold,
    COALESCE(SUM(pis.qty), 0) as current_stock,
    CASE 
        WHEN pi.min_stock_threshold IS NOT NULL AND COALESCE(SUM(pis.qty), 0) <= pi.min_stock_threshold 
        THEN 'LOW_STOCK'
        ELSE 'NORMAL'
    END as stock_status,
    CASE 
        WHEN pi.min_stock_threshold IS NOT NULL AND COALESCE(SUM(pis.qty), 0) <= pi.min_stock_threshold 
        THEN 1
        ELSE 0
    END as is_low_stock
FROM product_info pi
LEFT JOIN product_in_stock pis ON pi.id = pis.product_id AND pis.status = 'active'
WHERE pi.active_flag = 1
GROUP BY pi.id, pi.name, pi.code, pi.min_stock_threshold;
