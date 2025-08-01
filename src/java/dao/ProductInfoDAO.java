package dao;

import java.math.BigDecimal;
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
  
  // Tham số cho các cột 
  private static final String COL_ID = "id";
  private static final String COL_NAME = "name";
  private static final String COL_CODE = "code";
  private static final String COL_CATE_ID = "cate_id";
  private static final String COL_UNIT_ID = "unit_id";
  private static final String COL_STATUS = "status";
  private static final String COL_DESCRIPTION = "description";

  /**
   * Get all active products with basic information
   * @return List of ProductInfo objects
   */
  //Lấy tất cả product 
  public List<ProductInfo> getAllProducts() {
      List<ProductInfo> list = new ArrayList<>();
      String sql = "SELECT id, name, code, cate_id, unit_id, status, description FROM product_info WHERE status != 'deleted'";

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
              p.setStatus(rs.getString(COL_STATUS));
              p.setDescription(rs.getString(COL_DESCRIPTION));
              // Note: min_stock_threshold is now in product_in_stock table
              list.add(p);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

      return list;
  }

  /**
   * Get only active products (excludes inactive and deleted products)
   * @return List of active ProductInfo objects
   */
  public List<ProductInfo> getActiveProducts() {
      List<ProductInfo> list = new ArrayList<>();
      String sql = "SELECT id, name, code, cate_id, unit_id, status, description FROM product_info WHERE status = 'active'";

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
              p.setStatus(rs.getString(COL_STATUS));
              p.setDescription(rs.getString(COL_DESCRIPTION));
              // Note: min_stock_threshold is now in product_in_stock table
              list.add(p);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

      return list;
  }

  /**
   * Get product name by ID
   * @param productId The ID of the product
   * @return Product name as String
   */
  //Lấy product dựa trên id
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

  /**
   * Get products with stock information, pagination and search
   * @param page Page number
   * @param pageSize Number of products per page
   * @param search Search term
   * @param sortBy Column to sort by
   * @param sortOrder Sort order (ASC/DESC)
   * @return List of ProductStock objects
   */
  //Lấy product dựa trên số lượng trong kho
  public List<ProductStock> getProductsWithStock(int page, int pageSize, String search, String sortBy, String sortOrder) {
      List<ProductStock> list = new ArrayList<>();
      StringBuilder sql = new StringBuilder();
      sql.append("SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description, ");
      sql.append("COALESCE(s.min_stock_threshold, 0) as min_stock_threshold, ");
      sql.append("COALESCE(s.qty, 0) as stock_qty, ");
      sql.append("c.name as category_name, u.name as unit_name, u.symbol as unit_symbol ");
      sql.append("FROM product_info p ");
      sql.append("LEFT JOIN product_in_stock s ON p.id = s.product_id ");
      sql.append("LEFT JOIN category c ON p.cate_id = c.id ");
      sql.append("LEFT JOIN unit u ON p.unit_id = u.id ");
      sql.append("WHERE p.status IN ('active', 'Hoạt động') AND p.status != 'deleted' ");
      
      if (search != null && !search.trim().isEmpty()) {
          sql.append("AND (p.name LIKE ? OR p.code LIKE ? OR c.name LIKE ?) ");
      }
        if (sortBy != null && !sortBy.isEmpty()) {
          sql.append("ORDER BY ");
          switch (sortBy) {
              case COL_NAME -> sql.append("p.name ");
              case COL_CODE -> sql.append("p.code ");
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
          
          ResultSet rs = stmt.executeQuery();            
          while (rs.next()) {
              ProductStock product = new ProductStock();
              product.setId(rs.getInt(COL_ID));
              product.setName(rs.getString(COL_NAME));
              product.setCode(rs.getString(COL_CODE));
              product.setCateId(rs.getInt(COL_CATE_ID));
              product.setUnitId(rs.getInt(COL_UNIT_ID));
              product.setStatus(rs.getString(COL_STATUS));
              product.setDescription(rs.getString(COL_DESCRIPTION));
              product.setStockQuantity(rs.getBigDecimal("stock_qty"));
              // Stock status is controlled by product status
              product.setStockStatus(rs.getString(COL_STATUS));
              product.setCategoryName(rs.getString("category_name"));
              product.setUnitName(rs.getString("unit_name"));
              product.setUnitSymbol(rs.getString("unit_symbol"));
              // Set min_stock_threshold from product_in_stock table
              product.setMinStockThreshold(rs.getBigDecimal("min_stock_threshold"));
              
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

  /**
   * Get total count of active products for pagination
   * @param search Search term
   * @return Total count of products as int
   */
  //Lấy tổng số lượng product
  public int getTotalProductCount(String search) {
      StringBuilder sql = new StringBuilder();
      sql.append("SELECT COUNT(*) FROM product_info p ");
      sql.append("LEFT JOIN product_in_stock s ON p.id = s.product_id ");
      sql.append("LEFT JOIN category c ON p.cate_id = c.id ");
      sql.append("WHERE p.status IN ('active', 'Hoạt động') AND p.status != 'deleted' ");
      
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

  /**
   * Add a new product with comprehensive information
   * @param product ProductInfo object with product details
   * @param createdBy User ID of the creator
   * @return true if addition was successful, false otherwise
   */
  //Thêm product mới
  public boolean addProduct(ProductInfo product, int createdBy) {
      String productSql = "INSERT INTO product_info (name, code, cate_id, unit_id, status, description, " +
                         "supplier_id, expiration_date, additional_notes, created_by) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      
      System.out.println("DEBUG: Starting addProduct method");
      System.out.println("DEBUG: Product data - Name: " + product.getName() + ", Code: " + product.getCode());
      System.out.println("DEBUG: Category ID: " + product.getCate_id() + ", Unit ID: " + product.getUnit_id());
      System.out.println("DEBUG: Status: " + product.getStatus());
      System.out.println("DEBUG: Initial Stock Quantity: " + (product.getStockQuantity() != null ? product.getStockQuantity() : "0"));
      
      Connection con = null;
      try {
          con = Context.getJDBCConnection();
          con.setAutoCommit(false); // Start transaction
          
          if (con == null) {
              System.err.println("ERROR: Database connection is null!");
              return false;
          }
          
          System.out.println("DEBUG: Database connection successful");
          
          int productId = 0;
          
          // Insert into product_info table first
          try (PreparedStatement productStmt = con.prepareStatement(productSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
              productStmt.setString(1, product.getName());
              productStmt.setString(2, product.getCode());
              productStmt.setInt(3, product.getCate_id());
              productStmt.setInt(4, product.getUnit_id());
              productStmt.setString(5, product.getStatus());
              productStmt.setString(6, product.getDescription());
              
              // Handle supplier_id - set to null if 0 or not provided
              if (product.getSupplierId() > 0) {
                  productStmt.setInt(7, product.getSupplierId());
              } else {
                  productStmt.setNull(7, java.sql.Types.INTEGER);
              }
              
              productStmt.setDate(8, product.getExpirationDate());
              productStmt.setString(9, product.getAdditionalNotes());
              productStmt.setInt(10, createdBy);
              
              System.out.println("DEBUG: Executing product insert query");
              int rowsAffected = productStmt.executeUpdate();
              
              if (rowsAffected > 0) {
                  // Get generated product ID
                  try (ResultSet generatedKeys = productStmt.getGeneratedKeys()) {
                      if (generatedKeys.next()) {
                          productId = generatedKeys.getInt(1);
                          System.out.println("DEBUG: Product inserted successfully with ID: " + productId);
                      } else {
                          throw new SQLException("Failed to get generated product ID");
                      }
                  }
              } else {
                  throw new SQLException("Product insert failed, no rows affected");
              }
          }
          
          // Insert into product_in_stock table using separate method
          BigDecimal initialStock = product.getStockQuantity() != null ? product.getStockQuantity() : BigDecimal.ZERO;
          
          // Create stock record in the same transaction
          String stockSql = "INSERT INTO product_in_stock (product_id, qty, min_stock_threshold) VALUES (?, ?, ?)";
          try (PreparedStatement stockStmt = con.prepareStatement(stockSql)) {
              stockStmt.setInt(1, productId);
              stockStmt.setBigDecimal(2, initialStock);
              stockStmt.setBigDecimal(3, BigDecimal.ZERO); // Default min_stock_threshold
              
              System.out.println("DEBUG: Executing stock insert query");
              int stockRows = stockStmt.executeUpdate();
              
              if (stockRows > 0) {
                  con.commit(); // Commit both inserts
                  System.out.println("DEBUG: Product and stock inserted successfully");
                  return true;
              } else {
                  throw new SQLException("Stock insert failed");
              }
          }
          
      } catch (SQLException e) {
          System.err.println("ERROR: SQL Exception in addProduct: " + e.getMessage());
          System.err.println("ERROR: SQL State: " + e.getSQLState());
          System.err.println("ERROR: Error Code: " + e.getErrorCode());
          e.printStackTrace();
          return false;
      } catch (Exception e) {
          System.err.println("ERROR: General Exception in addProduct: " + e.getMessage());
          e.printStackTrace();
          
          // Rollback transaction on error
          if (con != null) {
              try {
                  con.rollback();
                  System.out.println("DEBUG: Transaction rolled back due to error");
              } catch (SQLException rollbackEx) {
                  System.err.println("ERROR: Failed to rollback transaction: " + rollbackEx.getMessage());
              }
          }
          return false;
      } finally {
          // Close connection
          if (con != null) {
              try {
                  con.close();
              } catch (SQLException e) {
                  System.err.println("ERROR: Failed to close connection: " + e.getMessage());
              }
          }
      }
  }
  
  /**
   * Check if a product code already exists
   * @param code Product code to check
   * @return true if code exists, false otherwise
   */
  //Kiểm tra product code có tồn tại hay không
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
  
  /**
   * Get all active categories for dropdown
   * @return List of CategoryProduct objects
   */
  //Lấy dữ liệu từ categories 
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
   * Get category name by ID
   * @param categoryId The ID of the category
   * @return Category name as String
   */
  public String getCategoryNameById(int categoryId) {
      String sql = "SELECT name FROM category WHERE id = ? AND active_flag = 1";
      try (Connection con = Context.getJDBCConnection(); 
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setInt(1, categoryId);
          ResultSet rs = stmt.executeQuery();
          
          if (rs.next()) {
              return rs.getString("name");
          }
          
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return null;
  }
  
  /**
   * Get all active units for dropdown
   * @return List of Unit objects
   */
  //Lấy dữ liệu từ Unit 
  public List<Unit> getAllActiveUnits() {
      List<Unit> units = new ArrayList<>();
      String sql = "SELECT id, name, symbol, status FROM unit WHERE status = 1 ORDER BY name";
      
      try (Connection con = Context.getJDBCConnection(); 
           PreparedStatement stmt = con.prepareStatement(sql); 
           ResultSet rs = stmt.executeQuery()) {
          
          while (rs.next()) {
              Unit unit = new Unit();
              unit.setId(rs.getInt("id"));
              unit.setName(rs.getString("name"));
              unit.setSymbol(rs.getString("symbol"));
              unit.setStatus(rs.getInt("status"));
              units.add(unit);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return units;
  }
  
  /**
   * Get all active units plus a specific unit (for editing existing products)
   * This ensures that existing products can still see their current unit even if it's inactive
   * @param includeUnitId The specific unit ID to include even if inactive
   * @return List of Unit objects
   */
  public List<Unit> getUnitsForProductEdit(int includeUnitId) {
      List<Unit> units = new ArrayList<>();
      String sql = "SELECT id, name, symbol, status FROM unit WHERE status = 1 OR id = ? ORDER BY name";
      
      try (Connection con = Context.getJDBCConnection(); 
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setInt(1, includeUnitId);
          ResultSet rs = stmt.executeQuery();
          
          while (rs.next()) {
              Unit unit = new Unit();
              unit.setId(rs.getInt("id"));
              unit.setName(rs.getString("name"));
              unit.setSymbol(rs.getString("symbol"));
              unit.setStatus(rs.getInt("status"));
              units.add(unit);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return units;
  }
  
  /**
   * Check if a unit is active
   * @param unitId The unit ID to check
   * @return true if unit is active, false if inactive or doesn't exist
   */
  public boolean isUnitActive(int unitId) {
      String sql = "SELECT status FROM unit WHERE id = ?";
      
      try (Connection con = Context.getJDBCConnection(); 
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setInt(1, unitId);
          ResultSet rs = stmt.executeQuery();
          
          if (rs.next()) {
              return rs.getInt("status") == 1;
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
  }
  
  /**
   * Check if a category is active
   * @param categoryId The category ID to check
   * @return true if category is active, false if inactive or doesn't exist
   */
  public boolean isCategoryActive(int categoryId) {
      String sql = "SELECT active_flag FROM category WHERE id = ?";
      
      try (Connection con = Context.getJDBCConnection(); 
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setInt(1, categoryId);
          ResultSet rs = stmt.executeQuery();
          
          if (rs.next()) {
              return rs.getBoolean("active_flag");
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
  }
  
  /**
   * Check if products in a category can be reactivated (category must be active)
   * @param categoryId The category ID to check
   * @return true if category is active, false otherwise
   */
  public boolean canReactivateProductsInCategory(int categoryId) {
      return isCategoryActive(categoryId);
  }
  
  /**
   * Get all active suppliers for dropdown
   * @return List of Supplier objects
   */
  //Lấy dữ liệu từ Suppliers
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
   * Get a single product by ID for editing
   */
  //Lấy dữ liệu từ product dựa trên id 
  public ProductInfo getProductById(int productId) {
      String sql = "SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description " +
                   "FROM product_info p " +
                   "WHERE p.id = ?";
      
      System.out.println("DEBUG: getProductById called with ID: " + productId);
      System.out.println("DEBUG: Using SQL: " + sql);
      
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
                  product.setStatus(rs.getString("status"));
                  product.setDescription(rs.getString("description"));
                  
                  // Set default values for fields that might not exist
                  product.setSupplierId(0);
                  product.setAdditionalNotes("");
                  product.setCreatedBy(0);
                  product.setUpdatedBy(0);
                  
                  // Set expiration date to null since it's not in the database
                  product.setExpirationDate(null);
                  
                  // Stock quantity is now handled in product_in_stock table
                  // Set default stock quantity to 0 for ProductInfo
                  product.setStockQuantity(0.0);
                  
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
   * Check if a column exists in a table
   */
  private boolean checkColumnExists(String tableName, String columnName) {
      String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                   "WHERE TABLE_SCHEMA = DATABASE() " +
                   "AND TABLE_NAME = ? AND COLUMN_NAME = ?";
      
      try (Connection con = Context.getJDBCConnection();
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setString(1, tableName);
          stmt.setString(2, columnName);
          
          try (ResultSet rs = stmt.executeQuery()) {
              if (rs.next()) {
                  return rs.getInt(1) > 0;
              }
          }
      } catch (SQLException e) {
          System.err.println("ERROR checking column existence: " + e.getMessage());
      }
      return false;
  }

  /**
   * Update product information
   */
  //Update dữ liệu của product
  public boolean updateProduct(ProductInfo product) {
      String sql = "UPDATE product_info SET " +
                "name = ?, code = ?, cate_id = ?, unit_id = ?, " +
                "status = ?, description = ?, expiration_date = ? " +
                "WHERE id = ?";
      
      try (Connection con = Context.getJDBCConnection(); 
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setString(1, product.getName());
          stmt.setString(2, product.getCode());
          stmt.setInt(3, product.getCate_id());
          stmt.setInt(4, product.getUnit_id());
          stmt.setString(5, product.getStatus());
          stmt.setString(6, product.getDescription());
          stmt.setDate(7, product.getExpirationDate());
          stmt.setInt(8, product.getId());
          
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
  //Update số lượng tồn kho của product 
  public boolean updateProductStock(int productId, double newQuantity) {
      // Use ProductInStockDAO for stock operations
      ProductInStockDAO stockDAO = new ProductInStockDAO();
      return stockDAO.upsertStock(productId, BigDecimal.valueOf(newQuantity), BigDecimal.ZERO);
  }
  
  /**
   * Check if product code exists for other products (for duplicate validation during update)
   */
  //Kiểm tra trùng lặp product dựa trên code 
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
   * Soft delete a product by ID
   * @param productId The ID of the product to delete
   * @return true if soft deletion was successful, false otherwise
   */   
  //Soft delete product 
  public boolean deleteProduct(int productId) {
      String sql = "UPDATE product_info SET status = 'deleted' WHERE id = ?";
      
      System.out.println("ProductInfoDAO: Attempting to soft delete product with ID: " + productId);
      
      try (Connection con = Context.getJDBCConnection();
           PreparedStatement stmt = con.prepareStatement(sql)) {
          
          stmt.setInt(1, productId);
          int rowsAffected = stmt.executeUpdate();
          
          System.out.println("ProductInfoDAO: Soft delete operation affected " + rowsAffected + " rows");
          return rowsAffected > 0;
          
      } catch (SQLException e) {
          System.out.println("ProductInfoDAO: Error soft deleting product - " + e.getMessage());
          e.printStackTrace();
          return false;
      }
  }
  
  /**
   * Check if a product can be safely deleted (no dependencies)
   * @param productId The ID of the product to check
   * @return true if product can be deleted, false if it has dependencies
   */    
  //Kiểm tra xem có xóa product được không
  public boolean canDeleteProduct(int productId) {
      // Check if product is referenced in other tables
      String[] dependencyTables = {
          "request_items"
      };
      
      for (String table : dependencyTables) {
          String sql = "SELECT COUNT(*) FROM " + table + " WHERE product_id = ?";
          try (Connection con = Context.getJDBCConnection();
               PreparedStatement stmt = con.prepareStatement(sql)) {
              
              stmt.setInt(1, productId);
              try (ResultSet rs = stmt.executeQuery()) {
                  if (rs.next() && rs.getInt(1) > 0) {
                      System.out.println("ProductInfoDAO: Found " + rs.getInt(1) + " dependencies in table " + table);
                      return false; // Found dependencies
                  }
              }
          } catch (SQLException e) {
              System.out.println("ProductInfoDAO: Error checking dependencies in table " + table + " - " + e.getMessage());
              e.printStackTrace();
              return false; // Error occurred, safer to not allow deletion
          }
      }
      System.out.println("ProductInfoDAO: No dependencies found for product " + productId);
      return true; // No dependencies found
  }

  /**
   * Recover a soft-deleted product by setting status to 'active'
   */
  public boolean recoverProduct(int productId) {
      String sql = "UPDATE product_info SET status = 'active' WHERE id = ?";
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
   * Get all soft-deleted products (status = 'deleted')
   */
  public List<ProductInfo> getDeletedProducts() {
      List<ProductInfo> list = new ArrayList<>();
      String sql = "SELECT id, name, code, cate_id, unit_id, status, description FROM product_info WHERE status = 'deleted'";
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
              p.setStatus(rs.getString(COL_STATUS));
              p.setDescription(rs.getString(COL_DESCRIPTION));
              // Note: min_stock_threshold is now in product_in_stock table
              list.add(p);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return list;
  }
  /**
 * Get product information with unit symbol by product ID
 * @param productId The ID of the product
 * @return ProductInfo object with unit symbol
 */
public ProductInfo getProductWithUnitSymbol(int productId) {
    String sql = "SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description, " +
                "u.symbol as unit_symbol " +
                "FROM product_info p " +
                "LEFT JOIN unit u ON p.unit_id = u.id " +
                "WHERE p.id = ? AND p.status != 'deleted'";
    
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
                product.setStatus(rs.getString("status"));
                product.setDescription(rs.getString("description"));
                product.setUnitSymbol(rs.getString("unit_symbol"));
                
                return product;
            }
        }
    } catch (SQLException e) {
        System.err.println("ERROR: SQL Exception in getProductWithUnitSymbol: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

/**
 * Get all products with their unit symbols
 * @return List of ProductInfo objects with unit symbols
 */
public List<ProductInfo> getAllProductsWithUnitSymbols() {
    List<ProductInfo> products = new ArrayList<>();
    String sql = "SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description, " +
                "u.symbol as unit_symbol " +
                "FROM product_info p " +
                "LEFT JOIN unit u ON p.unit_id = u.id " +
                "WHERE p.status != 'deleted' " +
                "ORDER BY p.name";
    
    try (Connection con = Context.getJDBCConnection(); 
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            ProductInfo product = new ProductInfo();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCode(rs.getString("code"));
            product.setCate_id(rs.getInt("cate_id"));
            product.setUnit_id(rs.getInt("unit_id"));
            product.setStatus(rs.getString("status"));
            product.setDescription(rs.getString("description"));
            product.setUnitSymbol(rs.getString("unit_symbol"));
            
            products.add(product);
        }
    } catch (SQLException e) {
        System.err.println("ERROR: SQL Exception in getAllProductsWithUnitSymbols: " + e.getMessage());
        e.printStackTrace();
    }
    return products;
}

/**
 * Get products with their stock information for stock management
 * @param page Page number (0-based)
 * @param pageSize Number of items per page
 * @param search Search keyword
 * @return List of ProductInfo objects with stock data
 */
public List<ProductInfo> getProductsWithStockInfo(int page, int pageSize, String search) {
    List<ProductInfo> products = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description, ")
       .append("p.supplier_id, p.expiration_date, p.additional_notes, ")
       .append("c.name as category_name, u.name as unit_name, u.symbol as unit_symbol, ")
       .append("s.name as supplier_name, ")
       .append("ps.qty as stock_quantity ")
       .append("FROM product_info p ")
       .append("LEFT JOIN category c ON p.cate_id = c.id ")
       .append("LEFT JOIN unit u ON p.unit_id = u.id ")
       .append("LEFT JOIN supplier s ON p.supplier_id = s.id ")
       .append("LEFT JOIN product_in_stock ps ON p.id = ps.product_id ")
       .append("WHERE p.status = 'active' ");
    
    if (search != null && !search.trim().isEmpty()) {
        sql.append("AND (p.name LIKE ? OR p.code LIKE ? OR c.name LIKE ?) ");
    }
    
    sql.append("ORDER BY p.name ASC ");
    sql.append("LIMIT ? OFFSET ?");
    
    try (Connection con = Context.getJDBCConnection();
         PreparedStatement stmt = con.prepareStatement(sql.toString())) {
        
        int paramIndex = 1;
        if (search != null && !search.trim().isEmpty()) {
            String searchParam = "%" + search.trim() + "%";
            stmt.setString(paramIndex++, searchParam);
            stmt.setString(paramIndex++, searchParam);
            stmt.setString(paramIndex++, searchParam);
        }
        
        stmt.setInt(paramIndex++, pageSize);
        stmt.setInt(paramIndex, page * pageSize);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ProductInfo product = new ProductInfo();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setCode(rs.getString("code"));
                product.setCate_id(rs.getInt("cate_id"));
                product.setUnit_id(rs.getInt("unit_id"));
                product.setStatus(rs.getString("status"));
                product.setDescription(rs.getString("description"));
                product.setSupplierId(rs.getInt("supplier_id"));
                
                // Handle dates and thresholds
                if (rs.getDate("expiration_date") != null) {
                    product.setExpirationDate(rs.getDate("expiration_date"));
                }
                
                product.setAdditionalNotes(rs.getString("additional_notes"));
                
                // Set stock information
                if (rs.getBigDecimal("stock_quantity") != null) {
                    product.setStockQuantity(rs.getBigDecimal("stock_quantity"));
                }
                
                // Set additional fields for display
                product.setUnitSymbol(rs.getString("unit_symbol"));
                
                products.add(product);
            }
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return products;
}

/**
 * Get total count of products for stock management pagination
 * @param search Search keyword
 * @return Total count
 */
public int getTotalProductCountForStock(String search) {
    StringBuilder sql = new StringBuilder();
    sql.append("SELECT COUNT(*) FROM product_info p ")
       .append("LEFT JOIN category c ON p.cate_id = c.id ")
       .append("WHERE p.status = 'active' ");
    
    if (search != null && !search.trim().isEmpty()) {
        sql.append("AND (p.name LIKE ? OR p.code LIKE ? OR c.name LIKE ?) ");
    }
    
    try (Connection con = Context.getJDBCConnection();
         PreparedStatement stmt = con.prepareStatement(sql.toString())) {
        
        if (search != null && !search.trim().isEmpty()) {
            String searchParam = "%" + search.trim() + "%";
            stmt.setString(1, searchParam);
            stmt.setString(2, searchParam);
            stmt.setString(3, searchParam);
        }
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return 0;
}

/**
 * Get products that don't have stock records yet
 * @return List of ProductInfo objects
 */
public List<ProductInfo> getProductsWithoutStock() {
    List<ProductInfo> products = new ArrayList<>();
    String sql = "SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description, " +
                "c.name as category_name, u.name as unit_name, u.symbol as unit_symbol " +
                "FROM product_info p " +
                "LEFT JOIN category c ON p.cate_id = c.id " +
                "LEFT JOIN unit u ON p.unit_id = u.id " +
                "LEFT JOIN product_in_stock ps ON p.id = ps.product_id " +
                "WHERE p.status = 'active' AND ps.product_id IS NULL " +
                "ORDER BY p.name ASC";
    
    try (Connection con = Context.getJDBCConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            ProductInfo product = new ProductInfo();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCode(rs.getString("code"));
            product.setCate_id(rs.getInt("cate_id"));
            product.setUnit_id(rs.getInt("unit_id"));
            product.setStatus(rs.getString("status"));
            product.setDescription(rs.getString("description"));
            product.setUnitSymbol(rs.getString("unit_symbol"));
            products.add(product);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return products;
}

/**
 * Note: updateMinStockThreshold has been moved to ProductInStockDAO
 * since min_stock_threshold is now stored in the product_in_stock table
 */

/**
 * Get products with pagination (no stock information)
 * @param page Page number (0-based)
 * @param pageSize Number of products per page
 * @param search Search term for product name or code
 * @param sortBy Sort field (name, code, status, category)
 * @param sortOrder Sort order (asc or desc)
 * @return List of ProductInfo objects
 */
public List<ProductInfo> getProductsPaginated(int page, int pageSize, String search, String sortBy, String sortOrder) {
    List<ProductInfo> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    
    sql.append("SELECT p.id, p.name, p.code, p.cate_id, p.unit_id, p.status, p.description, ");
    sql.append("c.name as category_name, u.name as unit_name, u.symbol as unit_symbol ");
    sql.append("FROM product_info p ");
    sql.append("LEFT JOIN category c ON p.cate_id = c.id ");
    sql.append("LEFT JOIN unit u ON p.unit_id = u.id ");
    sql.append("WHERE p.status != 'deleted' ");
    
    if (search != null && !search.trim().isEmpty()) {
        sql.append("AND (p.name LIKE ? OR p.code LIKE ?) ");
    }
    
    // Add sorting
    if (sortBy != null && !sortBy.trim().isEmpty()) {
        switch (sortBy.toLowerCase()) {
            case "name":
                sql.append("ORDER BY p.name ");
                break;
            case "code":
                sql.append("ORDER BY p.code ");
                break;
            case "status":
                sql.append("ORDER BY p.status ");
                break;
            case "category":
                sql.append("ORDER BY c.name ");
                break;
            default:
                sql.append("ORDER BY p.id ");
                break;
        }
        
        if ("desc".equalsIgnoreCase(sortOrder)) {
            sql.append("DESC ");
        } else {
            sql.append("ASC ");
        }
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
        }
        
        stmt.setInt(paramIndex++, pageSize);
        stmt.setInt(paramIndex, page * pageSize);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            ProductInfo product = new ProductInfo();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCode(rs.getString("code"));
            product.setCate_id(rs.getInt("cate_id"));
            product.setUnit_id(rs.getInt("unit_id"));
            product.setStatus(rs.getString("status"));
            product.setDescription(rs.getString("description"));
            // Note: min_stock_threshold is now in product_in_stock table
            product.setUnitSymbol(rs.getString("unit_symbol"));
            
            // Store category name in additionalNotes temporarily
            String categoryName = rs.getString("category_name");
            product.setAdditionalNotes(categoryName != null ? categoryName : "Chưa phân loại");
            
            list.add(product);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return list;
}

}
