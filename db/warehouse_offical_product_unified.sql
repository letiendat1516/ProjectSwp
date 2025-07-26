-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: warehouse_offical
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Migration script to merge product_info and product_in_stock tables
-- This script safely adds stock columns to existing product_info table
--

-- Step 1: Create backup tables for safety
CREATE TABLE product_info_backup AS SELECT * FROM product_info;
CREATE TABLE product_in_stock_backup AS SELECT * FROM product_in_stock;

-- Step 2: Check if columns already exist (to avoid errors on re-run)
SET @col_exists_qty = 0;
SET @col_exists_stock_status = 0;

SELECT COUNT(*) INTO @col_exists_qty 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'product_info' 
  AND COLUMN_NAME = 'qty';

SELECT COUNT(*) INTO @col_exists_stock_status 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'product_info' 
  AND COLUMN_NAME = 'stock_status';

-- Step 3: Add stock columns to product_info table (only if they don't exist)
SET @sql_qty = IF(@col_exists_qty = 0, 
    'ALTER TABLE product_info ADD COLUMN qty decimal(10,2) DEFAULT 0.00 COMMENT ''Stock quantity''', 
    'SELECT ''Column qty already exists'' as message');
PREPARE stmt FROM @sql_qty;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql_stock_status = IF(@col_exists_stock_status = 0, 
    'ALTER TABLE product_info ADD COLUMN stock_status varchar(20) DEFAULT ''active'' COMMENT ''Stock status''', 
    'SELECT ''Column stock_status already exists'' as message');
PREPARE stmt FROM @sql_stock_status;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 4: Migrate data from product_in_stock to product_info
UPDATE product_info p 
LEFT JOIN product_in_stock s ON p.id = s.product_id 
SET p.qty = COALESCE(s.qty, 0.00),
    p.stock_status = COALESCE(s.status, 'active');

-- Step 5: Add indexes for performance (only if they don't exist)
SET @index_exists_qty = 0;
SET @index_exists_stock_status = 0;

SELECT COUNT(*) INTO @index_exists_qty 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'product_info' 
  AND INDEX_NAME = 'idx_product_qty';

SELECT COUNT(*) INTO @index_exists_stock_status 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'product_info' 
  AND INDEX_NAME = 'idx_product_stock_status';

SET @sql_idx_qty = IF(@index_exists_qty = 0, 
    'ALTER TABLE product_info ADD INDEX idx_product_qty (qty)', 
    'SELECT ''Index idx_product_qty already exists'' as message');
PREPARE stmt FROM @sql_idx_qty;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql_idx_stock_status = IF(@index_exists_stock_status = 0, 
    'ALTER TABLE product_info ADD INDEX idx_product_stock_status (stock_status)', 
    'SELECT ''Index idx_product_stock_status already exists'' as message');
PREPARE stmt FROM @sql_idx_stock_status;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 6: Verify data migration
SELECT 
    'Data Migration Summary' as description,
    COUNT(*) as total_products,
    COUNT(CASE WHEN qty > 0 THEN 1 END) as products_with_stock,
    SUM(qty) as total_stock_quantity
FROM product_info;

-- Step 7: Drop the product_in_stock table (ONLY after verification)
-- IMPORTANT: Uncomment and run this ONLY after verifying data migration is successful
-- DROP TABLE product_in_stock;

-- Step 8: Display completion message
SELECT 'Migration completed successfully! Please verify the data before dropping product_in_stock table.' as status;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Migration completed
-- Remember to:
-- 1. Test the migration thoroughly
-- 2. Update all Java DAO classes to remove product_in_stock references
-- 3. Update all JSP files and controllers
-- 4. Remove the product_in_stock table only after everything is working
