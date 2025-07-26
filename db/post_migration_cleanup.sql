-- Post-Migration Cleanup Script
-- Run this script ONLY after successful migration verification and testing

-- WARNING: This script will permanently remove the product_in_stock table
-- Make sure you have verified the migration is successful before running this

-- Step 1: Final verification before cleanup
SELECT 
    'PRE-CLEANUP VERIFICATION' as stage,
    COUNT(*) as product_count,
    SUM(qty) as total_stock,
    COUNT(CASE WHEN qty > 0 THEN 1 END) as products_with_stock
FROM product_info;

-- Step 2: Check if backup tables exist
SELECT 
    TABLE_NAME,
    CREATE_TIME,
    TABLE_ROWS
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME IN ('product_info_backup', 'product_in_stock_backup');

-- Step 3: Drop the original product_in_stock table
-- UNCOMMENT THE NEXT LINE ONLY AFTER VERIFICATION
-- DROP TABLE product_in_stock;

-- Step 4: Optionally keep backup tables for a period, then remove them
-- UNCOMMENT THE NEXT LINES ONLY AFTER EXTENDED TESTING PERIOD (e.g., 1 month)
-- DROP TABLE product_info_backup;
-- DROP TABLE product_in_stock_backup;

-- Step 5: Verify cleanup
SELECT 
    'Tables remaining after cleanup:' as description,
    GROUP_CONCAT(TABLE_NAME) as tables
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME LIKE '%product%';

-- Step 6: Final table structure verification
DESCRIBE product_info;

-- Success message
SELECT 
    'CLEANUP INSTRUCTIONS:' as instruction,
    'Uncomment the DROP statements above only after thorough testing' as action_required
UNION ALL
SELECT 
    'RECOMMENDED TIMELINE:' as instruction,
    'Keep backup tables for at least 1 month after migration' as action_required
UNION ALL
SELECT 
    'FINAL STEP:' as instruction,
    'Update application documentation to reflect unified table structure' as action_required;
