package controller;

import dao.ProductInfoDAO;
import model.ProductInfo;
import model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for changing product status (active/inactive)
 * Product status in product_info controls both product and stock visibility
 */
@WebServlet("/change-product-status")
public class ChangeProductStatusController extends HttpServlet {
    
    private ProductInfoDAO productDAO = new ProductInfoDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check user authentication and authorization
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Get parameters
            String productIdStr = request.getParameter("id");
            String newStatus = request.getParameter("status");
            
            // Validate parameters
            if (productIdStr == null || newStatus == null) {
                response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("Thiếu thông tin cần thiết", "UTF-8"));
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            
            // Validate status values
            if (!newStatus.equals("active") && !newStatus.equals("inactive")) {
                response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("Trạng thái không hợp lệ", "UTF-8"));
                return;
            }
            
            // Get current product
            ProductInfo product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("Không tìm thấy sản phẩm", "UTF-8"));
                return;
            }
            
            // Check if trying to reactivate a product whose category is inactive
            if (newStatus.equals("active")) {
                if (!productDAO.isCategoryActive(product.getCate_id())) {
                    response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("Không thể kích hoạt sản phẩm vì danh mục đang ngừng hoạt động. Vui lòng kích hoạt danh mục trước.", "UTF-8"));
                    return;
                }
            }
            
            // Update product status - this controls both product and stock visibility
            String vietnameseStatus = newStatus.equals("active") ? "Hoạt động" : "Ngưng hoạt động";
            product.setStatus(vietnameseStatus);
            
            boolean productUpdateSuccess = productDAO.updateProduct(product);
            
            if (productUpdateSuccess) {
                String successMessage = newStatus.equals("active") ? 
                    "Đã kích hoạt sản phẩm thành công" : 
                    "Đã ngưng hoạt động sản phẩm thành công";
                response.sendRedirect("product-list?success=" + java.net.URLEncoder.encode(successMessage, "UTF-8"));
            } else {
                response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("Có lỗi xảy ra khi cập nhật trạng thái", "UTF-8"));
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("ID sản phẩm không hợp lệ", "UTF-8"));
        } catch (Exception e) {
            System.err.println("Error in ChangeProductStatusController: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("product-list?error=" + java.net.URLEncoder.encode("Có lỗi hệ thống xảy ra", "UTF-8"));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
