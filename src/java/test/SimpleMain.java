package test;

import dao.PurchaseQuotedDAO;
import model.PurchaseOrderInfo;
import java.util.List;

public class SimpleMain {
  public static void main(String[] args) {
      System.out.println("=== SIMPLE TEST ===");
      
      try {
          PurchaseQuotedDAO dao = new PurchaseQuotedDAO();
          
          // Test 1: Count
          System.out.println("1. Testing count...");
          int count = dao.getTotalFilteredPurchaseQuoted(null, null, null, null);
          System.out.println("Total count: " + count);
          
          // Test 2: Get list
          System.out.println("\n2. Testing get list...");
          List<PurchaseOrderInfo> orders = dao.getAllPurchaseQuoted(1, null, null, null, null);
          
          if (orders == null) {
              System.out.println("❌ Orders is NULL");
          } else if (orders.isEmpty()) {
              System.out.println("⚠️ Orders is EMPTY (size = 0)");
          } else {
              System.out.println("✅ Found " + orders.size() + " orders");
              
              // Show first order
              PurchaseOrderInfo first = orders.get(0);
              System.out.println("First order:");
              System.out.println("- ID: " + first.getId());
              System.out.println("- Name: " + first.getFullname());
              System.out.println("- Status: " + first.getStatus());
              System.out.println("- Items: " + (first.getPurchaseItems() != null ? first.getPurchaseItems().size() : "null"));
          }
          
      } catch (Exception e) {
          System.out.println("❌ ERROR: " + e.getMessage());
          e.printStackTrace();
      }
  }
}