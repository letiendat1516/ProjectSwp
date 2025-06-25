package controller;

// Controller for deleting a product

import dao.ProductInfoDAO;
import model.ProductInfo;
import model.Users;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DeleteProductController extends HttpServlet {
    
    private final ProductInfoDAO productDAO = new ProductInfoDAO();
      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Debug logging
        System.out.println("DeleteProductController: doGet method called");
        
        // Check user authentication and authorization
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
            System.out.println("DeleteProductController: User not authenticated or not Admin");
            response.sendRedirect("login.jsp");
            return;
        }
          String productIdStr = request.getParameter("id");
        System.out.println("DeleteProductController: Product ID parameter = " + productIdStr);
        
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            System.out.println("DeleteProductController: Product ID is null or empty");
            String errorMsg = URLEncoder.encode("Không tìm thấy sản phẩm!", StandardCharsets.UTF_8);
            response.sendRedirect("product-list?error=" + errorMsg);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            System.out.println("DeleteProductController: Parsed product ID = " + productId);
              // Get product details before deletion
            ProductInfo product = productDAO.getProductById(productId);
            if (product == null) {
                System.out.println("DeleteProductController: Product not found in database");
                String errorMsg = URLEncoder.encode("Sản phẩm không tồn tại!", StandardCharsets.UTF_8);
                response.sendRedirect("product-list?error=" + errorMsg);
                return;
            }
            
            System.out.println("DeleteProductController: Found product = " + product.getName());
            
            // Check if product can be safely deleted
            boolean canDelete = productDAO.canDeleteProduct(productId);
            System.out.println("DeleteProductController: Can delete product = " + canDelete);
              if (!canDelete) {
                System.out.println("DeleteProductController: Product has dependencies, cannot delete");
                String errorMsg = URLEncoder.encode("Không thể xóa sản phẩm này vì nó đang được sử dụng trong hệ thống (có trong kho, yêu cầu nhập/xuất)!", StandardCharsets.UTF_8);
                response.sendRedirect("update-product?id=" + productId + "&error=" + errorMsg);
                return;
            }
            
            // Perform deletion
            System.out.println("DeleteProductController: Attempting to delete product");
            boolean success = productDAO.deleteProduct(productId);
            System.out.println("DeleteProductController: Delete operation result = " + success);
              if (success) {
                // Log the deletion activity
                System.out.println("Product deleted successfully: " + product.getName() + " (ID: " + productId + ") by user: " + user.getUsername());
                
                // Redirect to product list with success message
                String successMsg = URLEncoder.encode("Đã xóa sản phẩm '" + product.getName() + "' thành công!", StandardCharsets.UTF_8);
                response.sendRedirect("product-list?success=" + successMsg);
            } else {
                System.out.println("DeleteProductController: Delete operation failed");
                String errorMsg = URLEncoder.encode("Có lỗi xảy ra khi xóa sản phẩm. Vui lòng thử lại!", StandardCharsets.UTF_8);
                response.sendRedirect("update-product?id=" + productId + "&error=" + errorMsg);
            }
              } catch (NumberFormatException e) {
            System.out.println("DeleteProductController: Invalid product ID format - " + e.getMessage());
            String errorMsg = URLEncoder.encode("ID sản phẩm không hợp lệ!", StandardCharsets.UTF_8);
            response.sendRedirect("product-list?error=" + errorMsg);
        } catch (Exception e) {
            System.out.println("DeleteProductController: Exception occurred - " + e.getMessage());
            e.printStackTrace();
            String errorMsg = URLEncoder.encode("Có lỗi hệ thống xảy ra!", StandardCharsets.UTF_8);
            response.sendRedirect("product-list?error=" + errorMsg);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect POST requests to GET
        doGet(request, response);
    }
}
