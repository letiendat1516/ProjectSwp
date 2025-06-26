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

/**
 * Simplified ProductInfoDAO that works with basic product_info table structure
 * Use this if you don't want to modify your database schema
 */
public class ProductInfoDAOSimple {
    
    // Tham số cho các cột 
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_CODE = "code";
    private static final String COL_CATE_ID = "cate_id";
    private static final String COL_UNIT_ID = "unit_id";
    private static final String COL_PRICE = "price";
    private static final String COL_STATUS = "status";
    private static final String COL_DESCRIPTION = "description";

    /**
     * Add a new product with basic information only
     * This version only uses fields that exist in the basic product_info table
     */
    public boolean addProduct(ProductInfo product, int createdBy) {
        String sql = "INSERT INTO product_info (name, code, cate_id, unit_id, price, status, description, expiration_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        System.out.println("DEBUG: Starting addProduct method (simplified version)");
        System.out.println("DEBUG: Product data - Name: " + product.getName() + ", Code: " + product.getCode());
        System.out.println("DEBUG: Category ID: " + product.getCate_id() + ", Unit ID: " + product.getUnit_id());
        System.out.println("DEBUG: Price: " + product.getPrice() + ", Status: " + product.getStatus());
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            if (con == null) {
                System.err.println("ERROR: Database connection is null!");
                return false;
            }
            
            System.out.println("DEBUG: Database connection successful");
            
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCode());
            stmt.setInt(3, product.getCate_id());
            stmt.setInt(4, product.getUnit_id());
            stmt.setBigDecimal(5, product.getPrice());
            stmt.setString(6, product.getStatus());
            stmt.setString(7, product.getDescription());
            stmt.setDate(8, product.getExpirationDate()); // This should exist in basic schema
            
            System.out.println("DEBUG: All parameters set, executing query");
            int rowsAffected = stmt.executeUpdate();
            System.out.println("DEBUG: Query executed, rows affected: " + rowsAffected);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("ERROR: SQL Exception in addProduct: " + e.getMessage());
            System.err.println("ERROR: SQL State: " + e.getSQLState());
            System.err.println("ERROR: Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("ERROR: General Exception in addProduct: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get a single product by ID for editing (simplified version)
     */
    public ProductInfo getProductById(int productId) {
        String sql = "SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.price, p.status, p.description, " +
                     "p.expiration_date " +
                     "FROM product_info p WHERE p.id = ? AND p.active_flag = 1";
        
        System.out.println("DEBUG: getProductById called with ID: " + productId);
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            if (con == null) {
                System.err.println("ERROR: Database connection is null in getProductById!");
                return null;
            }
            
            System.out.println("DEBUG: Database connection successful in getProductById");
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
                    product.setExpirationDate(rs.getDate("expiration_date"));
                    
                    // Set default values for extended fields
                    product.setSupplierId(0);
                    product.setAdditionalNotes("");
                    product.setCreatedBy(0);
                    product.setUpdatedBy(0);
                    
                    // Debug log
                    System.out.println("DEBUG: Product loaded successfully from DB: " + product.getName());
                    return product;
                } else {
                    System.out.println("DEBUG: No product found with ID: " + productId + " in database");
                }
            }
        } catch (SQLException e) {
            System.err.println("ERROR: SQL Exception in getProductById: " + e.getMessage());
            System.err.println("ERROR: SQL State: " + e.getSQLState());
            System.err.println("ERROR: Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: General Exception in getProductById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Update product information (simplified version)
     */
    public boolean updateProduct(ProductInfo product) {
        String sql = "UPDATE product_info SET " +
                     "name = ?, code = ?, cate_id = ?, unit_id = ?, price = ?, " +
                     "status = ?, description = ?, expiration_date = ? " +
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
            stmt.setDate(8, product.getExpirationDate());
            stmt.setInt(9, product.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("ERROR: SQL Exception in updateProduct: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a product code already exists
     */
    public boolean isProductCodeExists(String code) {
        String sql = "SELECT COUNT(*) FROM product_info WHERE code = ?";
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, code);
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
    
    /**
     * Get all active categories for dropdown
     */
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
    }
    
    /**
     * Get all active units for dropdown
     */
    public List<Unit> getAllActiveUnits() {
        List<Unit> units = new ArrayList<>();
        String sql = "SELECT id, name FROM unit WHERE active_flag = 1 ORDER BY name";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setId(rs.getInt("id"));
                unit.setName(rs.getString("name"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }
    
    /**
     * Get all active suppliers for dropdown
     */
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
    
    /**
     * Get all storage locations for dropdown (simplified - returns empty list)
     */
    public List<String> getAllStorageLocations() {
        // Return empty list since storage_location table might not exist
        return new ArrayList<>();
    }
}
