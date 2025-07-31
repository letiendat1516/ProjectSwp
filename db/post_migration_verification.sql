-- Post-Migration Verification Script
-- Run this script AFTER the migration to verify everything worked correctly

-- Check if new columns were added successfully
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'product_info' 
  AND COLUMN_NAME IN ('qty', 'stock_status')
ORDER BY ORDINAL_POSITION;

-- Check if indexes were created successfully
SELECT 
    INDEX_NAME,
    COLUMN_NAME,
    SEQ_IN_INDEX
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'product_info' 
  AND INDEX_NAME IN ('idx_product_qty', 'idx_product_stock_status')
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- Verify data migration by comparing record counts
SELECT 
    'Original product_info records' as description,
    COUNT(*) as count
FROM product_info_backup
UNION ALL
SELECT 
    'Original product_in_stock records' as description,
    COUNT(*) as count
FROM product_in_stock_backup
UNION ALL
SELECT 
    'Current product_info records' as description,
    COUNT(*) as count
FROM product_info;

-- Compare stock data before and after migration
SELECT 
    'Before migration - Total stock from product_in_stock' as description,
    SUM(qty) as total_stock
FROM product_in_stock_backup
UNION ALL
SELECT 
    'After migration - Total stock from product_info' as description,
    SUM(qty) as total_stock
FROM product_info;

-- Check for any data inconsistencies
SELECT 
    'Products with stock mismatch' as description,
    COUNT(*) as count
FROM product_info p
LEFT JOIN product_in_stock_backup s ON p.id = s.product_id
WHERE COALESCE(p.qty, 0) != COALESCE(s.qty, 0);

-- Sample comparison of migrated data (first 10 records)
SELECT 
    p.id,
    p.name,
    pb.status as orig_status,
    p.status as current_status,
    COALESCE(sb.qty, 0) as orig_qty,
    COALESCE(p.qty, 0) as current_qty,
    sb.status as orig_stock_status,
    p.stock_status as current_stock_status
FROM product_info p
LEFT JOIN product_info_backup pb ON p.id = pb.id
LEFT JOIN product_in_stock_backup sb ON p.id = sb.product_id
LIMIT 10;

-- Check for products with stock > 0
SELECT 
    'Products with stock > 0' as description,
    COUNT(*) as count,
    MIN(qty) as min_stock,
    MAX(qty) as max_stock,
    AVG(qty) as avg_stock
FROM product_info 
WHERE qty > 0;

-- Verify no orphaned stock records
SELECT 
    'Orphaned stock records (should be 0)' as description,
    COUNT(*) as count
FROM product_in_stock_backup s
LEFT JOIN product_info_backup p ON s.product_id = p.id
WHERE p.id IS NULL;

-- Final verification summary
SELECT 
    CASE 
        WHEN migration_check.total_issues = 0 THEN 'MIGRATION SUCCESSFUL - No issues found'
        ELSE CONCAT('MIGRATION ISSUES FOUND: ', migration_check.total_issues, ' problems detected')
    END as migration_status
FROM (
    SELECT 
        (SELECT COUNT(*) FROM product_info_backup) - (SELECT COUNT(*) FROM product_info) +
        (SELECT COUNT(*) FROM product_info p LEFT JOIN product_in_stock_backup s ON p.id = s.product_id 
         WHERE COALESCE(p.qty, 0) != COALESCE(s.qty, 0)) as total_issues
) as migration_check;
