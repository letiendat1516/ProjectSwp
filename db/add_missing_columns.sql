-- Migration script to add missing columns to product_info table
-- Run this script to add support for supplier, expiration date, additional notes, min stock threshold, and created_by fields

-- Add supplier_id column (foreign key to supplier table)
ALTER TABLE product_info 
ADD COLUMN supplier_id INT NULL 
COMMENT 'Foreign key reference to supplier table';

-- Add expiration_date column
ALTER TABLE product_info 
ADD COLUMN expiration_date DATE NULL 
COMMENT 'Product expiration date';

-- Add additional_notes column
ALTER TABLE product_info 
ADD COLUMN additional_notes TEXT NULL 
COMMENT 'Additional notes or remarks about the product';

-- Add min_stock_threshold column
ALTER TABLE product_info 
ADD COLUMN min_stock_threshold DECIMAL(10,2) DEFAULT NULL 
COMMENT 'Minimum stock level to trigger low stock warning';

-- Add created_by column
ALTER TABLE product_info 
ADD COLUMN created_by INT NULL 
COMMENT 'User ID who created this product record';

-- Add created_date and updated_date columns for better tracking
ALTER TABLE product_info 
ADD COLUMN created_date DATETIME DEFAULT CURRENT_TIMESTAMP 
COMMENT 'Date and time when product was created';

ALTER TABLE product_info 
ADD COLUMN updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 
COMMENT 'Date and time when product was last updated';

-- Add foreign key constraints
ALTER TABLE product_info 
ADD CONSTRAINT fk_product_supplier 
FOREIGN KEY (supplier_id) REFERENCES supplier(id) 
ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE product_info 
ADD CONSTRAINT fk_product_created_by 
FOREIGN KEY (created_by) REFERENCES users(id) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- Add indexes for better performance
CREATE INDEX idx_product_info_supplier_id ON product_info(supplier_id);
CREATE INDEX idx_product_info_expiration_date ON product_info(expiration_date);
CREATE INDEX idx_product_info_min_stock_threshold ON product_info(min_stock_threshold);
CREATE INDEX idx_product_info_created_by ON product_info(created_by);
CREATE INDEX idx_product_info_created_date ON product_info(created_date);

-- Show the updated table structure
DESCRIBE product_info;
