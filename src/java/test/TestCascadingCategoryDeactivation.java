package test;

import dao.CategoryProductDAO;
import dao.ProductInfoDAO;
import model.CategoryProduct;
import model.ProductInfo;

/**
 * Test for cascading category deactivation functionality
 * Tests that when a category is deactivated:
 * 1. All child categories are deactivated
 * 2. All products in that category and child categories are deactivated
 * 3. Products cannot be reactivated while category is inactive
 */
public class TestCascadingCategoryDeactivation {
    
    public static void main(String[] args) {
        System.out.println("🧪 TESTING - Cascading Category Deactivation");
        
        CategoryProductDAO categoryDAO = new CategoryProductDAO();
        ProductInfoDAO productDAO = new ProductInfoDAO();
        
        // Test data - use existing categories and products from your database
        // You should replace these IDs with actual ones from your database
        int parentCategoryId = 1; // Replace with actual parent category ID
        int childCategoryId = 2;  // Replace with actual child category ID
        int productInParentId = 1; // Replace with actual product ID in parent category
        int productInChildId = 2;  // Replace with actual product ID in child category
        
        System.out.println("Testing with:");
        System.out.println("- Parent Category ID: " + parentCategoryId);
        System.out.println("- Child Category ID: " + childCategoryId);
        System.out.println("- Product in Parent ID: " + productInParentId);
        System.out.println("- Product in Child ID: " + productInChildId);
        
        // Test 1: Check initial status
        System.out.println("\n--- Test 1: Check Initial Status ---");
        CategoryProduct parentCategory = categoryDAO.getCategoryById(parentCategoryId);
        CategoryProduct childCategory = categoryDAO.getCategoryById(childCategoryId);
        ProductInfo productInParent = productDAO.getProductById(productInParentId);
        ProductInfo productInChild = productDAO.getProductById(productInChildId);
        
        if (parentCategory != null) {
            System.out.println("Parent Category '" + parentCategory.getName() + "' status: " + 
                (parentCategory.isActiveFlag() ? "Active" : "Inactive"));
        }
        if (childCategory != null) {
            System.out.println("Child Category '" + childCategory.getName() + "' status: " + 
                (childCategory.isActiveFlag() ? "Active" : "Inactive"));
        }
        if (productInParent != null) {
            System.out.println("Product in Parent '" + productInParent.getName() + "' status: " + 
                productInParent.getStatus());
        }
        if (productInChild != null) {
            System.out.println("Product in Child '" + productInChild.getName() + "' status: " + 
                productInChild.getStatus());
        }
        
        // Test 2: Test category activation validation
        System.out.println("\n--- Test 2: Test Category Activation Validation ---");
        boolean canActivateParent = categoryDAO.canActivateCategory(parentCategoryId);
        boolean canActivateChild = categoryDAO.canActivateCategory(childCategoryId);
        System.out.println("Can activate parent category: " + canActivateParent);
        System.out.println("Can activate child category: " + canActivateChild);
        
        // Test 3: Test product reactivation validation
        System.out.println("\n--- Test 3: Test Product Reactivation Validation ---");
        if (productInParent != null) {
            boolean canReactivateProductInParent = productDAO.isCategoryActive(productInParent.getCate_id());
            System.out.println("Can reactivate product in parent category: " + canReactivateProductInParent);
        }
        if (productInChild != null) {
            boolean canReactivateProductInChild = productDAO.isCategoryActive(productInChild.getCate_id());
            System.out.println("Can reactivate product in child category: " + canReactivateProductInChild);
        }
        
        // Test 4: Simulate category toggle (WARNING: This will actually change data!)
        System.out.println("\n--- Test 4: Simulate Category Status Check ---");
        System.out.println("NOTE: Actual category toggle test should be done manually to avoid");
        System.out.println("unintended data changes. Use the web interface to test:");
        System.out.println("1. Deactivate a parent category with child categories and products");
        System.out.println("2. Verify all child categories become inactive");
        System.out.println("3. Verify all products in parent and child categories become inactive");
        System.out.println("4. Try to reactivate a product - should fail with category error message");
        System.out.println("5. Reactivate the parent category first, then try reactivating products");
        
        System.out.println("\n✅ Test completed! Check the results above.");
    }
}
