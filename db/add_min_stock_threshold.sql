-- Migration script to add min_stock_threshold column to product_info table
-- This script should be run manually after backing up the database

-- Check if the column already exists and add it if it doesn't
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'product_info' 
     AND COLUMN_NAME = 'min_stock_threshold') = 0,
    'ALTER TABLE product_info ADD COLUMN min_stock_threshold DECIMAL(10,2) DEFAULT NULL COMMENT ''Minimum stock level to trigger low stock warning''',
    'SELECT ''Column min_stock_threshold already exists'' as message'
));

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add index for better performance
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'product_info' 
     AND INDEX_NAME = 'idx_product_info_min_stock_threshold') = 0,
    'ALTER TABLE product_info ADD INDEX idx_product_info_min_stock_threshold (min_stock_threshold)',
    'SELECT ''Index idx_product_info_min_stock_threshold already exists'' as message'
));

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Verify the change
DESCRIBE product_info; 