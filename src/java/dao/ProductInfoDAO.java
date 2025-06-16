package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBContext.Context;
import model.CategoryProduct;
import model.ProductInfo;
import model.ProductStock;
import model.Supplier;
import model.Unit;

public class ProductInfoDAO {
    
    // Constants for column names
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_CODE = "code";
    private static final String COL_CATE_ID = "cate_id";
    private static final String COL_UNIT_ID = "unit_id";
    private static final String COL_PRICE = "price";
    private static final String COL_STATUS = "status";
    private static final String COL_DESCRIPTION = "description";

    public List<ProductInfo> getAllProducts() {
        List<ProductInfo> list = new ArrayList<>();
        String sql = "SELECT id, name, code, cate_id, unit_id, price, status, description FROM product_info";

        try (
                Connection con = Context.getJDBCConnection(); 
                PreparedStatement stmt = con.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery();) {
            while (rs.next()) {
                ProductInfo p = new ProductInfo();
                p.setId(rs.getInt(COL_ID));
                p.setName(rs.getString(COL_NAME));
                p.setCode(rs.getString(COL_CODE));
                p.setCate_id(rs.getInt(COL_CATE_ID));
                p.setUnit_id(rs.getInt(COL_UNIT_ID));
                p.setPrice(rs.getBigDecimal(COL_PRICE));
                p.setStatus(rs.getString(COL_STATUS));
                p.setDescription(rs.getString(COL_DESCRIPTION));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Thêm method này vào ProductInfoDAO
    public String getProductNameById(int productId) {
        String sql = "SELECT name FROM product_info WHERE id = ?";
        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Get products with stock information, pagination and search
    public List<ProductStock> getProductsWithStock(int page, int pageSize, String search, String sortBy, String sortOrder) {
        List<ProductStock> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.price, p.status, p.description, ");
        sql.append("COALESCE(s.qty, 0) as stock_qty, s.status as stock_status, ");
        sql.append("c.name as category_name, u.name as unit_name, u.symbol as unit_symbol ");
        sql.append("FROM product_info p ");
        sql.append("LEFT JOIN product_in_stock s ON p.id = s.product_id ");
        sql.append("LEFT JOIN category c ON p.cate_id = c.id ");
        sql.append("LEFT JOIN unit u ON p.unit_id = u.id ");
        sql.append("WHERE p.active_flag = 1 ");
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.code LIKE ? OR c.name LIKE ?) ");
        }
          if (sortBy != null && !sortBy.isEmpty()) {
            sql.append("ORDER BY ");
            switch (sortBy) {
                case COL_NAME -> sql.append("p.name ");
                case COL_CODE -> sql.append("p.code ");
                case COL_PRICE -> sql.append("p.price ");
                case "stock" -> sql.append("stock_qty ");
                case "category" -> sql.append("c.name ");
                default -> sql.append("p.id ");
            }
            sql.append(sortOrder != null && sortOrder.equals("desc") ? "DESC " : "ASC ");
        } else {
            sql.append("ORDER BY p.id ASC ");
        }
        
        sql.append("LIMIT ? OFFSET ?");

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            
            stmt.setInt(paramIndex++, pageSize);
            stmt.setInt(paramIndex, page * pageSize);
            
            ResultSet rs = stmt.executeQuery();            while (rs.next()) {
                ProductStock product = new ProductStock();
                product.setId(rs.getInt(COL_ID));
                product.setName(rs.getString(COL_NAME));
                product.setCode(rs.getString(COL_CODE));
                product.setCateId(rs.getInt(COL_CATE_ID));
                product.setUnitId(rs.getInt(COL_UNIT_ID));
                product.setPrice(rs.getBigDecimal(COL_PRICE));
                product.setStatus(rs.getString(COL_STATUS));
                product.setDescription(rs.getString(COL_DESCRIPTION));
                product.setStockQuantity(rs.getBigDecimal("stock_qty"));
                product.setStockStatus(rs.getString("stock_status"));
                product.setCategoryName(rs.getString("category_name"));
                product.setUnitName(rs.getString("unit_name"));
                product.setUnitSymbol(rs.getString("unit_symbol"));
                
                // Check for low stock and near expiration
                product.checkLowStock();
                // For now, we'll set expiration to null since it's not in the database
                product.setExpirationDate(null);
                product.checkNearExpiration();
                
                list.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Get total count for pagination
    public int getTotalProductCount(String search) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM product_info p ");
        sql.append("LEFT JOIN category c ON p.cate_id = c.id ");
        sql.append("WHERE p.active_flag = 1 ");
        
        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.code LIKE ? OR c.name LIKE ?) ");
        }

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Add new product with comprehensive information
    public boolean addProduct(ProductInfo product, int createdBy) {
        String sql = "INSERT INTO product_info (name, code, cate_id, unit_id, price, status, description, " +
                    "supplier_id, expiration_date, storage_location, image_url, additional_notes, created_by) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
              stmt.setString(1, product.getName());
            stmt.setString(2, product.getCode());
            stmt.setInt(3, product.getCate_id());
            stmt.setInt(4, product.getUnit_id());
            stmt.setBigDecimal(5, product.getPrice());
            stmt.setString(6, product.getStatus());
            stmt.setString(7, product.getDescription());
            
            // Handle supplier_id - set to null if 0 or not provided
            if (product.getSupplierId() > 0) {
                stmt.setInt(8, product.getSupplierId());
            } else {
                stmt.setNull(8, java.sql.Types.INTEGER);
            }
            
            stmt.setDate(9, product.getExpirationDate());
            stmt.setString(10, product.getStorageLocation());
            stmt.setString(11, product.getImageUrl());
            stmt.setString(12, product.getAdditionalNotes());
            stmt.setInt(13, createdBy);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if product code already exists
    public boolean isProductCodeExists(String code) {
        String sql = "SELECT COUNT(*) FROM product_info WHERE code = ?";
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get all active categories for dropdown
    public List<CategoryProduct> getAllActiveCategories() {
        List<CategoryProduct> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM category WHERE active_flag = 1 ORDER BY name";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }    // Get all active units for dropdown
    public List<Unit> getAllActiveUnits() {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT id, name, symbol FROM unit ORDER BY name";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setId(rs.getInt("id"));
                unit.setName(rs.getString("name"));
                unit.setSymbol(rs.getString("symbol"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }
      // Get all active suppliers for dropdown
    public List<Supplier> getAllActiveSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT id, name FROM supplier WHERE active_flag = 1 ORDER BY name";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
    
    // Get all storage locations for dropdown
    public List<String> getAllStorageLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT name FROM storage_location WHERE active_flag = 1 ORDER BY name";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                locations.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }
    
    /**
     * Get a single product by ID for editing
     */
public ProductInfo getProductById(int productId) {
    String sql = "SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.price, p.status, p.description, " +
                 "p.supplier_id, p.expiration_date, p.storage_location, p.image_url, p.additional_notes, " +
                 "p.created_by, p.created_date, p.updated_by, p.updated_date " +
                 "FROM product_info p WHERE p.id = ? AND p.active_flag = 1";
    
    try (Connection con = Context.getJDBCConnection(); 
         PreparedStatement stmt = con.prepareStatement(sql)) {
        
        stmt.setInt(1, productId);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                ProductInfo product = new ProductInfo();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCode(rs.getString("code"));
                product.setCate_id(rs.getInt("cate_id"));
                product.setUnit_id(rs.getInt("unit_id"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStatus(rs.getString("status"));
                product.setDescription(rs.getString("description"));
                product.setSupplierId(rs.getInt("supplier_id"));
                product.setExpirationDate(rs.getDate("expiration_date"));
                product.setStorageLocation(rs.getString("storage_location"));
                product.setImageUrl(rs.getString("image_url"));
                product.setAdditionalNotes(rs.getString("additional_notes"));
                product.setCreatedBy(rs.getInt("created_by"));
                
                // Convert Timestamp to Date for compatibility
                java.sql.Timestamp createdTimestamp = rs.getTimestamp("created_date");
                if (createdTimestamp != null) {
                    product.setCreatedDate(new java.sql.Date(createdTimestamp.getTime()));
                }
                
                product.setUpdatedBy(rs.getInt("updated_by"));
                
                java.sql.Timestamp updatedTimestamp = rs.getTimestamp("updated_date");
                if (updatedTimestamp != null) {
                    product.setUpdatedDate(new java.sql.Date(updatedTimestamp.getTime()));
                }
                
                // Debug log
                System.out.println("DAO - Product loaded successfully: " + product.getName());
                return product;
            } else {
                System.out.println("DAO - No product found with ID: " + productId);
            }
        }
    } catch (SQLException e) {
        System.err.println("DAO - Error loading product: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

    
    /**
     * Update product information
     */
    public boolean updateProduct(ProductInfo product) {
        String sql = "UPDATE product_info SET " +
                     "name = ?, code = ?, cate_id = ?, unit_id = ?, price = ?, " +
                     "status = ?, description = ?, supplier_id = ?, expiration_date = ?, " +
                     "storage_location = ?, image_url = ?, additional_notes = ?, " +
                     "updated_by = ?, updated_date = CURRENT_TIMESTAMP " +
                     "WHERE id = ?";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
              stmt.setString(1, product.getName());
            stmt.setString(2, product.getCode());
            stmt.setInt(3, product.getCate_id());
            stmt.setInt(4, product.getUnit_id());
            stmt.setBigDecimal(5, product.getPrice());
            stmt.setString(6, product.getStatus());
            stmt.setString(7, product.getDescription());
            
            // Handle supplier_id - set to null if 0 or not provided
            if (product.getSupplierId() > 0) {
                stmt.setInt(8, product.getSupplierId());
            } else {
                stmt.setNull(8, java.sql.Types.INTEGER);
            }
            
            stmt.setDate(9, product.getExpirationDate());
            stmt.setString(10, product.getStorageLocation());
            stmt.setString(11, product.getImageUrl());
            stmt.setString(12, product.getAdditionalNotes());
            stmt.setInt(13, product.getUpdatedBy());
            stmt.setInt(14, product.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update product stock quantity
     */
    public boolean updateProductStock(int productId, double newQuantity) {
        String sql = "UPDATE product_in_stock SET qty = ? WHERE product_id = ?";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDouble(1, newQuantity);
            stmt.setInt(2, productId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if product code exists for other products (for duplicate validation during update)
     */
    public boolean isProductCodeExistsForOtherProduct(String code, int excludeProductId) {
        String sql = "SELECT COUNT(*) FROM product_info WHERE code = ? AND id != ?";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, code);
            stmt.setInt(2, excludeProductId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
