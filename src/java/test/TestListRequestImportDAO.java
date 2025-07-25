package test;

import dao.ListRequestImportDAO;
import model.ApprovedRequestItem;
import java.util.List;
import java.util.Map;

public class TestListRequestImportDAO {
    
    public static void main(String[] args) {
        System.out.println("🔍 TESTING ListRequestImportDAO");
        System.out.println("=".repeat(60));
        
        ListRequestImportDAO dao = new ListRequestImportDAO();
        
        // Test 1: Kiểm tra kết nối
        testConnection(dao);
        
        // Test 2: Lấy danh sách approved requests
        testGetApprovedRequests(dao);
        
        // Test 3: Lấy danh sách completed requests
        testGetCompletedRequests(dao);
        
        // Test 4: Lấy thống kê
        testGetStatistics(dao);
        
        // Test 5: Lấy danh sách suppliers
        testGetSuppliers(dao);
        
        // Test 6: Lấy danh sách product codes
        testGetProductCodes(dao);
        
        // Test 7: Test search functionality
        testSearchRequests(dao);
        
        System.out.println("\n🎉 TESTING COMPLETED!");
    }
    
    /**
     * Test 1: Kiểm tra kết nối database
     */
    private static void testConnection(ListRequestImportDAO dao) {
        System.out.println("\n📡 TEST 1: Connection Test");
        System.out.println("-".repeat(40));
        
        boolean connected = dao.testConnection();
        if (connected) {
            System.out.println("✅ Database connection: SUCCESS");
        } else {
            System.out.println("❌ Database connection: FAILED");
            System.out.println("⚠️ Stopping tests due to connection failure");
            return;
        }
    }
    
    /**
     * Test 2: Lấy danh sách approved requests
     */
    private static void testGetApprovedRequests(ListRequestImportDAO dao) {
        System.out.println("\n📋 TEST 2: Get Approved Requests");
        System.out.println("-".repeat(40));
        
        try {
            // Lấy 10 records đầu tiên
            List<ApprovedRequestItem> approvedList = dao.getApprovedRequestItems(null, null, 1, 10);
            
            System.out.println("📊 Total approved items found: " + approvedList.size());
            
            if (!approvedList.isEmpty()) {
                System.out.println("\n📝 Sample approved requests:");
                System.out.printf("%-12s | %-20s | %-15s | %-10s | %-8s | %-8s%n", 
                                "Request ID", "Product Name", "Status", "Ordered", "Imported", "Pending");
                System.out.println("-".repeat(85));
                
                int count = 0;
                for (ApprovedRequestItem item : approvedList) {
                    if (count >= 5) break; // Chỉ hiển thị 5 records đầu
                    
                    System.out.printf("%-12s | %-20s | %-15s | %-10.1f | %-8.1f | %-8.1f%n",
                                    truncate(item.getRequestId(), 12),
                                    truncate(item.getProductName(), 20),
                                    item.getStatus(),
                                    item.getQuantityOrdered(),
                                    item.getQuantityImported(),
                                    item.getQuantityPending());
                    count++;
                }
                
                if (approvedList.size() > 5) {
                    System.out.println("... and " + (approvedList.size() - 5) + " more items");
                }
            } else {
                System.out.println("ℹ️ No approved requests found");
            }
            
            // Test count
            int totalCount = dao.countApprovedRequestItems(null, null);
            System.out.println("🔢 Total count from countApprovedRequestItems(): " + totalCount);
            
        } catch (Exception e) {
            System.out.println("❌ Error testing approved requests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 3: Lấy danh sách completed requests
     */
    private static void testGetCompletedRequests(ListRequestImportDAO dao) {
        System.out.println("\n✅ TEST 3: Get Completed Requests");
        System.out.println("-".repeat(40));
        
        try {
            List<ApprovedRequestItem> completedList = dao.getCompletedRequestItems(null, null, 1, 10);
            
            System.out.println("📊 Total completed items found: " + completedList.size());
            
            if (!completedList.isEmpty()) {
                System.out.println("\n📝 Sample completed requests:");
                System.out.printf("%-12s | %-20s | %-15s | %-10s | %-15s%n", 
                                "Request ID", "Product Name", "Status", "Quantity", "Supplier");
                System.out.println("-".repeat(75));
                
                int count = 0;
                for (ApprovedRequestItem item : completedList) {
                    if (count >= 5) break;
                    
                    System.out.printf("%-12s | %-20s | %-15s | %-10.1f | %-15s%n",
                                    truncate(item.getRequestId(), 12),
                                    truncate(item.getProductName(), 20),
                                    item.getStatus(),
                                    item.getQuantity(),
                                    truncate(item.getSupplier(), 15));
                    count++;
                }
                
                if (completedList.size() > 5) {
                    System.out.println("... and " + (completedList.size() - 5) + " more items");
                }
            } else {
                System.out.println("ℹ️ No completed requests found");
            }
            
            // Test count
            int totalCount = dao.countCompletedRequestItems(null, null);
            System.out.println("🔢 Total count from countCompletedRequestItems(): " + totalCount);
            
        } catch (Exception e) {
            System.out.println("❌ Error testing completed requests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 4: Lấy thống kê
     */
    private static void testGetStatistics(ListRequestImportDAO dao) {
        System.out.println("\n📈 TEST 4: Get Statistics");
        System.out.println("-".repeat(40));
        
        try {
            Map<String, Integer> stats = dao.getStatistics();
            
            if (!stats.isEmpty()) {
                System.out.println("📊 Statistics by status:");
                for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                    System.out.printf("  %-20s: %d%n", entry.getKey(), entry.getValue());
                }
            } else {
                System.out.println("ℹ️ No statistics found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error getting statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 5: Lấy danh sách suppliers
     */
    private static void testGetSuppliers(ListRequestImportDAO dao) {
        System.out.println("\n🏢 TEST 5: Get All Suppliers");
        System.out.println("-".repeat(40));
        
        try {
            List<String> suppliers = dao.getAllSuppliers();
            
            System.out.println("📊 Total suppliers found: " + suppliers.size());
            
            if (!suppliers.isEmpty()) {
                System.out.println("📝 Suppliers list:");
                int count = 0;
                for (String supplier : suppliers) {
                    if (count >= 10) {
                        System.out.println("... and " + (suppliers.size() - 10) + " more suppliers");
                        break;
                    }
                    System.out.println("  " + (count + 1) + ". " + supplier);
                    count++;
                }
            } else {
                System.out.println("ℹ️ No suppliers found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error getting suppliers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 6: Lấy danh sách product codes
     */
    private static void testGetProductCodes(ListRequestImportDAO dao) {
        System.out.println("\n🏷️ TEST 6: Get All Product Codes");
        System.out.println("-".repeat(40));
        
        try {
            List<String> productCodes = dao.getAllProductCodes();
            
            System.out.println("📊 Total product codes found: " + productCodes.size());
            
            if (!productCodes.isEmpty()) {
                System.out.println("📝 Product codes list:");
                int count = 0;
                for (String code : productCodes) {
                    if (count >= 10) {
                        System.out.println("... and " + (productCodes.size() - 10) + " more product codes");
                        break;
                    }
                    System.out.println("  " + (count + 1) + ". " + code);
                    count++;
                }
            } else {
                System.out.println("ℹ️ No product codes found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error getting product codes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 7: Test search functionality
     */
    private static void testSearchRequests(ListRequestImportDAO dao) {
        System.out.println("\n🔍 TEST 7: Search Requests");
        System.out.println("-".repeat(40));
        
        try {
            // Test search by request ID (lấy ID đầu tiên từ approved list)
            List<ApprovedRequestItem> approvedList = dao.getApprovedRequestItems(null, null, 1, 1);
            
            if (!approvedList.isEmpty()) {
                String testRequestId = approvedList.get(0).getRequestId();
                System.out.println("🔍 Testing search with Request ID: " + testRequestId);
                
                List<ApprovedRequestItem> searchResults = dao.searchRequests(testRequestId, null, null, null, 1, 5);
                System.out.println("📊 Search results found: " + searchResults.size());
                
                if (!searchResults.isEmpty()) {
                    System.out.println("📝 Search results:");
                    for (ApprovedRequestItem item : searchResults) {
                        System.out.printf("  - %s | %s | %s%n", 
                                        item.getRequestId(), 
                                        truncate(item.getProductName(), 20), 
                                        item.getStatus());
                    }
                }
            } else {
                System.out.println("ℹ️ No data available for search testing");
            }
            
            // Test search by status
            System.out.println("\n🔍 Testing search by status 'approved':");
            List<ApprovedRequestItem> statusSearchResults = dao.searchRequests(null, "approved", null, null, 1, 3);
            System.out.println("📊 Status search results: " + statusSearchResults.size());
            
        } catch (Exception e) {
            System.out.println("❌ Error testing search: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method để cắt ngắn string cho display
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Test method riêng để test một request ID cụ thể
     */
    public static void testSpecificRequestId(String requestId) {
        System.out.println("\n🎯 TESTING SPECIFIC REQUEST ID: " + requestId);
        System.out.println("=".repeat(60));
        
        ListRequestImportDAO dao = new ListRequestImportDAO();
        
        try {
            // Test get products by request ID
            List<ApprovedRequestItem> products = dao.getProductsByRequestId(requestId);
            System.out.println("📊 Products found for request " + requestId + ": " + products.size());
            
            if (!products.isEmpty()) {
                System.out.println("\n📝 Products details:");
                System.out.printf("%-20s | %-10s | %-8s | %-8s | %-8s%n", 
                                "Product Name", "Code", "Ordered", "Imported", "Pending");
                System.out.println("-".repeat(65));
                
                for (ApprovedRequestItem item : products) {
                    System.out.printf("%-20s | %-10s | %-8.1f | %-8.1f | %-8.1f%n",
                                    truncate(item.getProductName(), 20),
                                    truncate(item.getProductCode(), 10),
                                    item.getQuantityOrdered(),
                                    item.getQuantityImported(),
                                    item.getQuantityPending());
                }
            }
            
            // Test get request overview
            ApprovedRequestItem overview = dao.getRequestOverview(requestId);
            if (overview != null) {
                System.out.println("\n📋 Request Overview:");
                System.out.println("  Request ID: " + overview.getRequestId());
                System.out.println("  Status: " + overview.getStatus());
                System.out.println("  Supplier: " + overview.getSupplier());
                System.out.println("  Total Ordered: " + overview.getQuantityOrdered());
                System.out.println("  Total Imported: " + overview.getQuantityImported());
                System.out.println("  Total Pending: " + overview.getQuantityPending());
            }
            
            // Test get import history
            List<ApprovedRequestItem> history = dao.getImportHistory(requestId);
            System.out.println("\n📜 Import History: " + history.size() + " records");
            
            if (!history.isEmpty()) {
                System.out.printf("%-20s | %-10s | %-15s%n", "Product Name", "Quantity", "Import Date");
                System.out.println("-".repeat(50));
                for (ApprovedRequestItem item : history) {
                    System.out.printf("%-20s | %-10.1f | %-15s%n",
                                    truncate(item.getProductName(), 20),
                                    item.getQuantity(),
                                    item.getDayRequest());
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error testing specific request ID: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
