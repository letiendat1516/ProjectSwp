package test;

import dao.ProductInfoDAO;
import model.ProductStock;
import model.ProductInfo;
import java.util.List;

/**
 * Test class để kiểm tra ProductInfoDAO
 */
public class ProductTestMain {
  
  public static void main(String[] args) {
      System.out.println("=== BẮT ĐẦU TEST PRODUCT DAO ===");
      
      ProductInfoDAO dao = new ProductInfoDAO();
      
      // Test 1: Kiểm tra kết nối database và lấy tất cả sản phẩm
      testGetAllProducts(dao);
      
      // Test 2: Kiểm tra lấy sản phẩm với stock
      testGetProductsWithStock(dao);
      
      // Test 3: Kiểm tra tổng số sản phẩm
      testGetTotalProductCount(dao);
      
      // Test 4: Kiểm tra lấy categories và units
      testGetCategoriesAndUnits(dao);
      
      System.out.println("=== KẾT THÚC TEST ===");
  }
  
  /**
   * Test lấy tất cả sản phẩm cơ bản
   */
  private static void testGetAllProducts(ProductInfoDAO dao) {
      System.out.println("\n--- TEST 1: GET ALL PRODUCTS ---");
      try {
          List<ProductInfo> products = dao.getAllProducts();
          
          if (products == null) {
              System.out.println("❌ FAILED: products list is null");
              return;
          }
          
          System.out.println("✅ SUCCESS: Retrieved " + products.size() + " products");
          
          if (products.size() > 0) {
              System.out.println("📋 First 3 products:");
              for (int i = 0; i < Math.min(3, products.size()); i++) {
                  ProductInfo p = products.get(i);
                  System.out.println("  " + (i+1) + ". ID: " + p.getId() + 
                                   ", Name: " + p.getName() + 
                                   ", Code: " + p.getCode() + 
                                   ", Status: " + p.getStatus());
              }
          } else {
              System.out.println("⚠️  WARNING: No products found in database");
          }
          
      } catch (Exception e) {
          System.out.println("❌ ERROR in getAllProducts: " + e.getMessage());
          e.printStackTrace();
      }
  }
  
  /**
   * Test lấy sản phẩm với thông tin stock (method chính gây vấn đề)
   */
  private static void testGetProductsWithStock(ProductInfoDAO dao) {
      System.out.println("\n--- TEST 2: GET PRODUCTS WITH STOCK ---");
      try {
          // Test với tham số mặc định
          List<ProductStock> products = dao.getProductsWithStock(0, 10, null, null, "asc");
          
          if (products == null) {
              System.out.println("❌ FAILED: products list is null");
              return;
          }
          
          System.out.println("✅ SUCCESS: Retrieved " + products.size() + " products with stock info");
          
          if (products.size() > 0) {
              System.out.println("📋 First 3 products with stock:");
              for (int i = 0; i < Math.min(3, products.size()); i++) {
                  ProductStock p = products.get(i);
                  System.out.println("  " + (i+1) + ". ID: " + p.getId() + 
                                   ", Name: " + p.getName() + 
                                   ", Code: " + p.getCode() + 
                                   ", Status: " + p.getStatus() +
                                   ", Stock: " + p.getStockQuantity() +
                                   ", Category: " + p.getCategoryName() +
                                   ", Unit: " + p.getUnitName());
              }
          } else {
              System.out.println("⚠️  WARNING: No products found with stock info");
              
              // Test với query đơn giản hơn
              System.out.println("🔍 Testing with simpler query...");
              testSimpleProductQuery(dao);
          }
          
      } catch (Exception e) {
          System.out.println("❌ ERROR in getProductsWithStock: " + e.getMessage());
          e.printStackTrace();
          
          // Test với query đơn giản khi có lỗi
          System.out.println("🔍 Testing with simpler query due to error...");
          testSimpleProductQuery(dao);
      }
  }
  
  /**
   * Test query đơn giản để debug
   */
  private static void testSimpleProductQuery(ProductInfoDAO dao) {
      System.out.println("\n--- SIMPLE QUERY TEST ---");
      try {
          // Tạo một method test đơn giản trong DAO hoặc test trực tiếp ở đây
          java.sql.Connection con = DBContext.Context.getJDBCConnection();
          if (con == null) {
              System.out.println("❌ Database connection is null!");
              return;
          }
          
          System.out.println("✅ Database connection successful");
          
          // Test query đơn giản nhất
          String sql = "SELECT COUNT(*) as total FROM product_info";
          java.sql.PreparedStatement stmt = con.prepareStatement(sql);
          java.sql.ResultSet rs = stmt.executeQuery();
          
          if (rs.next()) {
              int total = rs.getInt("total");
              System.out.println("📊 Total products in database: " + total);
          }
          
          // Test query với điều kiện
          sql = "SELECT COUNT(*) as filtered FROM product_info WHERE status != 'deleted'";
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery();
          
          if (rs.next()) {
              int filtered = rs.getInt("filtered");
              System.out.println("📊 Non-deleted products: " + filtered);
          }
          
          // Test lấy 3 bản ghi đầu
          sql = "SELECT id, name, code, status FROM product_info WHERE status != 'deleted' LIMIT 3";
          stmt = con.prepareStatement(sql);
          rs = stmt.executeQuery();
          
          System.out.println("📋 Sample products:");
          int count = 0;
          while (rs.next()) {
              count++;
              System.out.println("  " + count + ". ID: " + rs.getInt("id") + 
                               ", Name: " + rs.getString("name") + 
                               ", Code: " + rs.getString("code") + 
                               ", Status: " + rs.getString("status"));
          }
          
          con.close();
          
      } catch (Exception e) {
          System.out.println("❌ ERROR in simple query test: " + e.getMessage());
          e.printStackTrace();
      }
  }
  
  /**
   * Test đếm tổng số sản phẩm
   */
  private static void testGetTotalProductCount(ProductInfoDAO dao) {
      System.out.println("\n--- TEST 3: GET TOTAL PRODUCT COUNT ---");
      try {
          int total = dao.getTotalProductCount(null);
          System.out.println("✅ Total product count: " + total);
          
          // Test với search term
          int searchTotal = dao.getTotalProductCount("test");
          System.out.println("✅ Total with search 'test': " + searchTotal);
          
      } catch (Exception e) {
          System.out.println("❌ ERROR in getTotalProductCount: " + e.getMessage());
          e.printStackTrace();
      }
  }
  
  /**
   * Test lấy categories và units
   */
  private static void testGetCategoriesAndUnits(ProductInfoDAO dao) {
      System.out.println("\n--- TEST 4: GET CATEGORIES AND UNITS ---");
      try {
          // Test categories
          var categories = dao.getAllActiveCategories();
          System.out.println("✅ Retrieved " + (categories != null ? categories.size() : 0) + " categories");
          
          if (categories != null && categories.size() > 0) {
              System.out.println("📋 Categories:");
              for (int i = 0; i < Math.min(3, categories.size()); i++) {
                  var cat = categories.get(i);
                  System.out.println("  " + (i+1) + ". ID: " + cat.getId() + ", Name: " + cat.getName());
              }
          }
          
          // Test units
          var units = dao.getAllActiveUnits();
          System.out.println("✅ Retrieved " + (units != null ? units.size() : 0) + " units");
          
          if (units != null && units.size() > 0) {
              System.out.println("📋 Units:");
              for (int i = 0; i < Math.min(3, units.size()); i++) {
                  var unit = units.get(i);
                  System.out.println("  " + (i+1) + ". ID: " + unit.getId() + 
                                   ", Name: " + unit.getName() + 
                                   ", Symbol: " + unit.getSymbol());
              }
          }
          
      } catch (Exception e) {
          System.out.println("❌ ERROR in categories/units test: " + e.getMessage());
          e.printStackTrace();
      }
  }
}