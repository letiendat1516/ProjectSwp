# Database Migration Guide: Unifying product_info and product_in_stock Tables

## Overview
This migration consolidates the `product_info` and `product_in_stock` tables into a single unified `product_info` table to simplify the database structure and improve performance.

## Before Migration
- `product_info`: Contains product metadata (name, code, category, unit, etc.)
- `product_in_stock`: Contains stock quantity information (product_id, qty, status)
- Relationship: product_info.id = product_in_stock.product_id (1:1 or 1:0)

## After Migration
- `product_info`: Contains both product metadata AND stock information
- `product_in_stock`: REMOVED
- New columns in product_info: `qty`, `stock_status`

## Migration Steps

### 1. Database Schema Changes
Execute the migration SQL script: `warehouse_offical_product_unified.sql`

```sql
-- Add stock columns to product_info
ALTER TABLE product_info 
ADD COLUMN qty decimal(10,2) DEFAULT 0.00 COMMENT 'Stock quantity',
ADD COLUMN stock_status varchar(20) DEFAULT 'active' COMMENT 'Stock status';

-- Migrate data from product_in_stock to product_info
UPDATE product_info p 
LEFT JOIN product_in_stock s ON p.id = s.product_id 
SET p.qty = COALESCE(s.qty, 0.00),
    p.stock_status = COALESCE(s.status, 'active');

-- Add indexes for performance
ALTER TABLE product_info 
ADD INDEX idx_product_qty (qty),
ADD INDEX idx_product_stock_status (stock_status);
```

### 2. Java Code Changes Made

#### Model Classes
- ✅ **ProductInfo.java**: Added `stockStatus` field with getter/setter methods

#### DAO Classes
- ✅ **ProductInfoDAO.java**: 
  - Updated `addProduct()` to insert stock data directly into product_info
  - Updated `getProductsWithStock()` to remove JOIN with product_in_stock
  - Updated `updateProductStock()` to update qty column in product_info
  - Updated `getProductById()` to select qty from product_info
  - Removed product_in_stock from dependency check in delete validation

- ✅ **CategoryStatisticsDAO.java**: 
  - Updated stock calculation to use product_info.qty instead of product_in_stock.qty

#### Controller Classes
- ✅ **InventoryStatisticsController.java**: 
  - Updated debugging queries to check product_info.qty instead of product_in_stock

### 3. Testing Checklist

#### Database Operations
- [ ] Test product creation with initial stock quantity
- [ ] Test product stock updates
- [ ] Test product listing with stock information
- [ ] Test stock-based product filtering and sorting
- [ ] Test low stock threshold calculations

#### Application Features
- [ ] Add new product with stock quantity
- [ ] Edit existing product stock
- [ ] View product list with stock quantities
- [ ] Inventory statistics display
- [ ] Category statistics with stock totals
- [ ] Product deletion (verify no orphaned stock records)

### 4. Rollback Plan (if needed)

If issues are found, you can rollback using the backup tables:

```sql
-- Restore original tables from backups
DROP TABLE product_info;
CREATE TABLE product_info AS SELECT 
    id, name, code, cate_id, unit_id, active_flag, status, description,
    min_stock_threshold, supplier_id, expiration_date, additional_notes,
    created_by, created_date, updated_date
FROM product_info_backup;

CREATE TABLE product_in_stock AS SELECT * FROM product_in_stock_backup;

-- Restore foreign keys and indexes
ALTER TABLE product_info ADD PRIMARY KEY (id);
-- ... (add other constraints)
```

### 5. Performance Benefits

#### Before Migration
- Every stock query required JOIN between product_info and product_in_stock
- Potential for orphaned stock records
- More complex queries for product listings

#### After Migration
- Single table queries for product + stock information
- Simplified queries and improved performance
- Reduced storage overhead
- Easier maintenance and backups

### 6. Database Cleanup (after successful testing)

Once everything is verified to work correctly:

```sql
-- Remove backup tables (keep for a reasonable period first)
-- DROP TABLE product_info_backup;
-- DROP TABLE product_in_stock_backup;

-- Remove the old product_in_stock table
-- DROP TABLE product_in_stock;
```

### 7. Future Considerations

- Update any reports or analytics tools that reference product_in_stock
- Update database documentation
- Consider adding database triggers if business logic requires stock change tracking
- Update backup and recovery procedures

## Migration Status
- ✅ Database schema design completed
- ✅ Java model updates completed  
- ✅ DAO layer updates completed
- ✅ Controller updates completed
- ⏳ Database migration execution (pending)
- ⏳ Testing and validation (pending)
- ⏳ Production deployment (pending)

## Notes
- All database operations should be performed during maintenance windows
- Ensure database backups are taken before migration
- Test thoroughly in development environment before production deployment
- Monitor application performance after migration
