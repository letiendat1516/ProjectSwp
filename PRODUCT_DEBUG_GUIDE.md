# Product Feature Debugging Guide

## Issues Identified and Solutions

### Issue 1: Add Product Error ("Có lỗi xảy ra khi thêm sản phẩm. Vui lòng thử lại.")

**Root Causes:**
1. **Database Connection Issues** - MySQL connection might be failing
2. **Missing Database Schema** - Required tables or columns might not exist
3. **Foreign Key Constraint Violations** - Referenced categories, units, or users might not exist
4. **Data Type Mismatches** - Field types in database vs. Java object

**Debugging Steps:**

1. **Check Database Connection:**
   ```bash
   # Run the database connection test
   cd /path/to/your/project
   javac -cp "lib/*:." src/java/test/DatabaseConnectionTest.java
   java -cp "lib/*:src/java" test.DatabaseConnectionTest
   ```

2. **Verify Database Schema:**
   ```sql
   -- Check if all required tables exist
   SHOW TABLES LIKE '%product%';
   SHOW TABLES LIKE '%category%';
   SHOW TABLES LIKE '%unit%';
   SHOW TABLES LIKE '%supplier%';
   SHOW TABLES LIKE '%users%';
   
   -- Check product_info table structure
   DESCRIBE product_info;
   
   -- Check if required columns exist
   SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
   FROM INFORMATION_SCHEMA.COLUMNS 
   WHERE TABLE_NAME = 'product_info' AND TABLE_SCHEMA = 'warehouse_offical';
   ```

3. **Check Foreign Key Data:**
   ```sql
   -- Verify categories exist
   SELECT COUNT(*) as category_count FROM category WHERE active_flag = 1;
   
   -- Verify units exist  
   SELECT COUNT(*) as unit_count FROM unit WHERE active_flag = 1;
   
   -- Verify users exist
   SELECT COUNT(*) as user_count FROM users;
   
   -- Check for specific IDs being used
   SELECT id, name FROM category WHERE active_flag = 1 LIMIT 5;
   SELECT id, name FROM unit WHERE active_flag = 1 LIMIT 5;
   ```

4. **Monitor Console Output:**
   - Check your application server console (Tomcat/NetBeans)
   - Look for detailed DEBUG messages added to the code
   - Check for SQL exceptions with specific error codes

### Issue 2: Update Product Blank Page

**Root Cause:** 
The `product` object is not being set in the request attributes, causing the JSP to show the debug error message.

**Debugging Steps:**

1. **Check URL Parameters:**
   - Ensure the URL contains `?id=X` where X is a valid product ID
   - Example: `http://localhost:8080/your-app/update-product?id=1`

2. **Verify Product Exists:**
   ```sql
   -- Check if product with specific ID exists
   SELECT * FROM product_info WHERE id = 1 AND active_flag = 1;
   ```

3. **Check Console Output:**
   - Look for DEBUG messages in UpdateProductController
   - Verify the product is loaded from database
   - Check if loadDropdownData is working properly

## Quick Fixes to Try:

### 1. Database Schema Fix
If the database is missing required columns, run this SQL:

```sql
-- Add missing columns to product_info if they don't exist
ALTER TABLE product_info 
ADD COLUMN IF NOT EXISTS supplier_id INT,
ADD COLUMN IF NOT EXISTS expiration_date DATE,
ADD COLUMN IF NOT EXISTS storage_location VARCHAR(100),
ADD COLUMN IF NOT EXISTS additional_notes TEXT,
ADD COLUMN IF NOT EXISTS created_by INT,
ADD COLUMN IF NOT EXISTS created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_by INT,
ADD COLUMN IF NOT EXISTS updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Create storage_location table if it doesn't exist
CREATE TABLE IF NOT EXISTS storage_location (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active_flag TINYINT(1) DEFAULT 1
);

-- Insert sample storage locations
INSERT IGNORE INTO storage_location (name, description) VALUES 
('Kho A - Tầng 1', 'Khu vực lưu trữ chính tầng 1'),
('Kho A - Tầng 2', 'Khu vực lưu trữ chính tầng 2'),
('Kho B - Lạnh', 'Khu vực bảo quản lạnh');
```

### 2. Test with Minimal Data
Try adding a product with only required fields:
- Name: "Test Product"
- Code: "TEST001"
- Category: (select any existing)
- Unit: (select any existing)
- Price: 100.00
- Status: "active"

### 3. Check Database Connection Settings
Verify in `DBContext/Context.java`:
- URL: `jdbc:mysql://localhost:3306/warehouse_offical`
- Username: `root`
- Password: `letiendat`
- Database exists and is accessible

## How to Run Debugging:

1. **Enable Debug Mode:**
   - The enhanced code now includes detailed console logging
   - Check your application server console for DEBUG and ERROR messages

2. **Test Database Connection:**
   - Compile and run the DatabaseConnectionTest class
   - This will verify basic connectivity and data availability

3. **Test Add Product:**
   - Try adding a simple product through the web interface
   - Monitor the console for detailed error messages
   - Check database to see if partial data was inserted

4. **Test Update Product:**
   - Access a product update URL directly: `/update-product?id=1`
   - Check console for product loading messages
   - Verify the product ID exists in the database

## Expected Console Output:

When working correctly, you should see:
```
DEBUG: Starting addProduct method
DEBUG: Product data - Name: Test Product, Code: TEST001
DEBUG: Database connection successful
DEBUG: All parameters set, executing query
DEBUG: Query executed, rows affected: 1
```

For update product:
```
DEBUG: UpdateProductController doGet method called
DEBUG: Product ID parameter: 1
DEBUG: Product from DAO: Test Product
DEBUG: Product set in request: Test Product (ID: 1)
```

If you see errors, they will be detailed with SQL error codes and states for easier troubleshooting.

## SOLUTION FOUND: Missing Database Columns

**Error Details:**
```
ERROR: SQL Exception in addProduct: Unknown column 'supplier_id' in 'field list'
ERROR: SQL State: 42S22
ERROR: Error Code: 1054
```

**Root Cause:** The `product_info` table is missing the extended columns that were added in the enhanced schema.

### IMMEDIATE FIX - Run This SQL Script:

```sql
-- Connect to your MySQL database and run these commands:

USE warehouse_offical;

-- Check current table structure
DESCRIBE product_info;

-- Add missing columns to product_info table
ALTER TABLE product_info 
ADD COLUMN supplier_id INT DEFAULT NULL,
ADD COLUMN storage_location VARCHAR(100) DEFAULT NULL,
ADD COLUMN additional_notes TEXT DEFAULT NULL,
ADD COLUMN created_by INT DEFAULT NULL,
ADD COLUMN created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_by INT DEFAULT NULL,
ADD COLUMN updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Create storage_location table
CREATE TABLE IF NOT EXISTS storage_location (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active_flag TINYINT(1) DEFAULT 1
);

-- Insert sample storage locations
INSERT IGNORE INTO storage_location (name, description) VALUES 
('Kho A - Tầng 1', 'Khu vực lưu trữ chính tầng 1'),
('Kho A - Tầng 2', 'Khu vực lưu trữ chính tầng 2'),
('Kho B - Lạnh', 'Khu vực bảo quản lạnh'),
('Kho C - Khô', 'Khu vực bảo quản khô'),
('Kho D - Nguyên liệu', 'Khu vực lưu trữ nguyên liệu');

-- Add foreign key constraints (optional - run if you want referential integrity)
-- ALTER TABLE product_info 
-- ADD CONSTRAINT fk_product_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id),
-- ADD CONSTRAINT fk_product_created_by FOREIGN KEY (created_by) REFERENCES users(id),
-- ADD CONSTRAINT fk_product_updated_by FOREIGN KEY (updated_by) REFERENCES users(id);

-- Verify the changes
DESCRIBE product_info;
SELECT * FROM storage_location;
```

### Option 2: Use Simplified DAO (No Database Changes Required)

If you prefer not to modify your database, you can use the simplified DAO:

1. **Replace the import in your controllers:**
   ```java
   // In AddProductController.java and UpdateProductController.java
   // Change this line:
   import dao.ProductInfoDAO;
   // To this:
   import dao.ProductInfoDAOSimple;
   
   // And change the declaration:
   // From:
   private final ProductInfoDAO productDAO = new ProductInfoDAO();
   // To:
   private final ProductInfoDAOSimple productDAO = new ProductInfoDAOSimple();
   ```

2. **The simplified DAO only uses basic fields:**
   - id, name, code, cate_id, unit_id, price, status, description, expiration_date
   - Skips: supplier_id, storage_location, additional_notes, created_by, updated_by

3. **Test the simplified version:**
   - Try adding a product with basic information
   - The advanced fields will be ignored or set to default values

### Which Option Should You Choose?

**Choose Option 1 (Database Schema Update) if:**
- You want full functionality with suppliers, storage locations, and audit fields
- You have control over the database and can run SQL commands
- You want the complete feature set

**Choose Option 2 (Simplified DAO) if:**
- You cannot modify the database schema
- You want a quick fix without database changes
- You only need basic product information

---
