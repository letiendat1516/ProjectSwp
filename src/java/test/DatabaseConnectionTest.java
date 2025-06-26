package test;

import DBContext.Context;
import dao.ProductInfoDAO;
import model.ProductInfo;
import java.sql.Connection;
import java.math.BigDecimal;

public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===");
        
        // Test 1: Basic connection
        System.out.println("\n1. Testing basic database connection...");
        Connection conn = Context.getJDBCConnection();
        if (conn != null) {
            System.out.println("✅ Database connection successful!");
            try {
                System.out.println("Database URL: " + conn.getMetaData().getURL());
                System.out.println("Database Product: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database Version: " + conn.getMetaData().getDatabaseProductVersion());
                conn.close();
            } catch (Exception e) {
                System.err.println("Error getting connection metadata: " + e.getMessage());
            }
        } else {
            System.err.println("❌ Database connection failed!");
            return;
        }
        
        // Test 2: Test ProductInfoDAO methods
        System.out.println("\n2. Testing ProductInfoDAO methods...");
        ProductInfoDAO dao = new ProductInfoDAO();
        
        try {
            // Test getting categories
            System.out.println("Testing getAllActiveCategories...");
            var categories = dao.getAllActiveCategories();
            System.out.println("Found " + categories.size() + " categories");
            
            // Test getting units
            System.out.println("Testing getAllActiveUnits...");
            var units = dao.getAllActiveUnits();
            System.out.println("Found " + units.size() + " units");
            
            // Test getting suppliers
            System.out.println("Testing getAllActiveSuppliers...");
            var suppliers = dao.getAllActiveSuppliers();
            System.out.println("Found " + suppliers.size() + " suppliers");
            
            // Test creating a simple product
            System.out.println("\n3. Testing product creation...");
            if (categories.size() > 0 && units.size() > 0) {
                ProductInfo testProduct = new ProductInfo();
                testProduct.setName("Test Product - " + System.currentTimeMillis());
                testProduct.setCode("TEST" + System.currentTimeMillis());
                testProduct.setCate_id(categories.get(0).getId());
                testProduct.setUnit_id(units.get(0).getId());
                testProduct.setPrice(new BigDecimal("100.00"));
                testProduct.setStatus("active");
                testProduct.setDescription("Test product for debugging");
                
                boolean result = dao.addProduct(testProduct, 1); // assuming user ID 1 exists
                if (result) {
                    System.out.println("✅ Test product created successfully!");
                } else {
                    System.err.println("❌ Failed to create test product!");
                }
            } else {
                System.err.println("❌ No categories or units found - cannot test product creation");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error testing DAO methods: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}
