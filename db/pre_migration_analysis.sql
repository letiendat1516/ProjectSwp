-- Pre-Migration Analysis Script
-- Run this script BEFORE the migration to understand foreign key dependencies

-- Check all foreign key constraints that reference product_info table
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE REFERENCED_TABLE_NAME = 'product_info'
  AND TABLE_SCHEMA = DATABASE();

-- Check all foreign key constraints that reference product_in_stock table  
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE REFERENCED_TABLE_NAME = 'product_in_stock'
  AND TABLE_SCHEMA = DATABASE();

-- Check current product_info table structure
DESCRIBE product_info;

-- Check current product_in_stock table structure
DESCRIBE product_in_stock;

-- Count records in both tables
SELECT 
    'product_info' as table_name,
    COUNT(*) as record_count
FROM product_info
UNION ALL
SELECT 
    'product_in_stock' as table_name,
    COUNT(*) as record_count
FROM product_in_stock;

-- Check for products with and without stock records
SELECT 
    'Products with stock records' as description,
    COUNT(*) as count
FROM product_info p
INNER JOIN product_in_stock s ON p.id = s.product_id
UNION ALL
SELECT 
    'Products without stock records' as description,
    COUNT(*) as count
FROM product_info p
LEFT JOIN product_in_stock s ON p.id = s.product_id
WHERE s.product_id IS NULL;

-- Sample data from both tables (first 5 records)
SELECT 
    p.id,
    p.name,
    p.code,
    p.status as product_status,
    COALESCE(s.qty, 0) as stock_quantity,
    s.status as stock_status
FROM product_info p
LEFT JOIN product_in_stock s ON p.id = s.product_id
LIMIT 5;
