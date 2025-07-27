package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBContext.Context;
import model.ProductInStock;

/**
 * DAO class for ProductInStock operations
 * Handles all database operations for the product_in_stock table
 */
public class ProductInStockDAO {
    
    // Column constants
    private static final String COL_ID = "id";
    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_QTY = "qty";
    private static final String COL_STATUS = "status";
    
    /**
     * Add new stock record for a product
     * @param productInStock ProductInStock object with stock details
     * @return true if addition was successful, false otherwise
     */
    public boolean addProductStock(ProductInStock productInStock) {
        String sql = "INSERT INTO product_in_stock (product_id, qty, status) VALUES (?, ?, ?)";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, productInStock.getProductId());
            stmt.setBigDecimal(2, productInStock.getQty());
            stmt.setString(3, productInStock.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update stock quantity for a product
     * @param productId Product ID
     * @param newQuantity New quantity
     * @return true if update was successful, false otherwise
     */
    public boolean updateStockQuantity(int productId, BigDecimal newQuantity) {
        String sql = "UPDATE product_in_stock SET qty = ? WHERE product_id = ?";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newQuantity);
            stmt.setInt(2, productId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update stock status for a product
     * @param productId Product ID
     * @param newStatus New status
     * @return true if update was successful, false otherwise
     */
    public boolean updateStockStatus(int productId, String newStatus) {
        String sql = "UPDATE product_in_stock SET status = ? WHERE product_id = ?";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, productId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get stock information for a specific product
     * @param productId Product ID
     * @return ProductInStock object or null if not found
     */
    public ProductInStock getStockByProductId(int productId) {
        String sql = "SELECT id, product_id, qty, status FROM product_in_stock WHERE product_id = ?";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                ProductInStock stock = new ProductInStock();
                stock.setId(rs.getInt(COL_ID));
                stock.setProductId(rs.getInt(COL_PRODUCT_ID));
                stock.setQty(rs.getBigDecimal(COL_QTY));
                stock.setStatus(rs.getString(COL_STATUS));
                return stock;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all stock records
     * @return List of ProductInStock objects
     */
    public List<ProductInStock> getAllStocks() {
        List<ProductInStock> list = new ArrayList<>();
        String sql = "SELECT id, product_id, qty, status FROM product_in_stock";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ProductInStock stock = new ProductInStock();
                stock.setId(rs.getInt(COL_ID));
                stock.setProductId(rs.getInt(COL_PRODUCT_ID));
                stock.setQty(rs.getBigDecimal(COL_QTY));
                stock.setStatus(rs.getString(COL_STATUS));
                list.add(stock);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Delete stock record for a product
     * @param productId Product ID
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteStockByProductId(int productId) {
        String sql = "DELETE FROM product_in_stock WHERE product_id = ?";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if stock record exists for a product
     * @param productId Product ID
     * @return true if stock record exists, false otherwise
     */
    public boolean stockExistsForProduct(int productId) {
        String sql = "SELECT COUNT(*) FROM product_in_stock WHERE product_id = ?";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Add or update stock for a product (upsert operation)
     * @param productId Product ID
     * @param quantity Quantity
     * @param status Status
     * @return true if operation was successful, false otherwise
     */
    public boolean upsertStock(int productId, BigDecimal quantity, String status) {
        if (stockExistsForProduct(productId)) {
            // Update existing record
            return updateStockQuantity(productId, quantity) && updateStockStatus(productId, status);
        } else {
            // Insert new record
            ProductInStock newStock = new ProductInStock(productId, quantity, status);
            return addProductStock(newStock);
        }
    }
    
}
