-- Migration script to remove price column from product_info table
-- This script should be run manually after backing up the database

-- Remove price column from product_info table
ALTER TABLE product_info DROP COLUMN price;

-- Verify the change
DESCRIBE product_info; 