package controller;

import dao.ProductInStockDAO;
import dao.ProductInfoDAO;
import model.ProductInStock;
import model.ProductInfo;
import model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Controller for Product Stock Management
 * Handles all stock-related operations separately from product information
 */
@WebServlet(name = "ProductStockController", urlPatterns = {"/product-stock/*"})
public class ProductStockController extends HttpServlet {
    
    private final transient ProductInStockDAO stockDAO = new ProductInStockDAO();
    private final transient ProductInfoDAO productDAO = new ProductInfoDAO();
    
    /**
     * Helper method to create redirect URL with properly encoded Vietnamese text
     */
    private void redirectWithMessage(HttpServletResponse response, HttpServletRequest request, 
                                   String path, String paramName, String message) throws IOException {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + path + "?" + paramName + "=" + encodedMessage);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check user authentication and authorization
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            showStockList(request, response);
        } else if (pathInfo.equals("/add")) {
            showAddStockForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditStockForm(request, response);
        } else if (pathInfo.equals("/detail")) {
            showStockDetail(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/product-stock/list");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check user authentication and authorization
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.equals("/add")) {
            addStock(request, response);
        } else if (pathInfo != null && pathInfo.equals("/edit")) {
            updateStock(request, response);
        } else if (pathInfo != null && pathInfo.equals("/delete")) {
            deleteStock(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/product-stock/list");
        }
    }
    
    /**
     * Show stock list page
     */
    private void showStockList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get search parameters
            String search = request.getParameter("search");
            String pageParam = request.getParameter("page");
            String pageSizeParam = request.getParameter("pageSize");
            
            // Parse pagination parameters
            int page = 0;
            int pageSize = 10;
            
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Math.max(0, Integer.parseInt(pageParam));
                } catch (NumberFormatException e) {
                    page = 0;
                }
            }
            
            if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                try {
                    pageSize = Math.max(5, Math.min(100, Integer.parseInt(pageSizeParam)));
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }
            
            // Get all products with their stock information
            List<ProductInfo> products = productDAO.getProductsWithStockInfo(page, pageSize, search);
            int totalProducts = productDAO.getTotalProductCountForStock(search);
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
            
            // Create a map of category IDs to category names for JSP
            Map<Integer, String> categoryMap = new HashMap<>();
            for (ProductInfo product : products) {
                if (product.getCate_id() > 0 && !categoryMap.containsKey(product.getCate_id())) {
                    String categoryName = productDAO.getCategoryNameById(product.getCate_id());
                    categoryMap.put(product.getCate_id(), categoryName != null ? categoryName : "Chưa phân loại");
                }
            }
            
            // Set attributes
            request.setAttribute("products", products);
            request.setAttribute("categoryMap", categoryMap);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("search", search != null ? search : "");
            
            // Handle messages
            String successMessage = request.getParameter("success");
            String errorMessage = request.getParameter("error");
            
            if (successMessage != null && !successMessage.trim().isEmpty()) {
                request.setAttribute("successMessage", successMessage);
            }
            
            if (errorMessage != null && !errorMessage.trim().isEmpty()) {
                request.setAttribute("errorMessage", errorMessage);
            }
            
            request.getRequestDispatcher("/product-stock-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithMessage(response, request, "/product-stock/list", "error", "Có lỗi xảy ra khi tải danh sách tồn kho");
        }
    }
    
    /**
     * Show add stock form
     */
    private void showAddStockForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get all products for dropdown (those without stock records)
            List<ProductInfo> availableProducts = productDAO.getProductsWithoutStock();
            
            request.setAttribute("availableProducts", availableProducts);
            
            request.getRequestDispatcher("/product-stock-add.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithMessage(response, request, "/product-stock/list", "error", "Có lỗi xảy ra khi tải form thêm tồn kho");
        }
    }
    
 
/**
 * Show edit stock form for a specific product
 */
private void showEditStockForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    try {
        int productId = Integer.parseInt(request.getParameter("id"));
        System.out.println("DEBUG: showEditStockForm called with productId: " + productId);
        
        // Get product information first
        ProductInfo product = productDAO.getProductById(productId);
        
        if (product == null) {
            System.out.println("DEBUG: Product not found with ID: " + productId);
            redirectWithMessage(response, request, "/product-stock/list", 
                              "error", "Không tìm thấy sản phẩm!");
            return;
        }
        
        System.out.println("DEBUG: Product found: " + product.getName());
        
        // Try to get existing stock record
        ProductInStock existingStock = stockDAO.getStockByProductId(productId);
        
        if (existingStock == null) {
            // ✅ Tạo stock record mới nếu chưa có
            System.out.println("DEBUG: No existing stock found, creating new stock record");
            existingStock = new ProductInStock();
            existingStock.setProductId(productId);
            existingStock.setQty(BigDecimal.ZERO); // Mặc định là 0
            existingStock.setStatus("active");
            
            // Insert vào database để tạo record mới
            boolean created = stockDAO.addProductStock(existingStock);
            if (created) {
                System.out.println("DEBUG: New stock record created successfully");
                // Lấy lại stock record vừa tạo để có ID
                existingStock = stockDAO.getStockByProductId(productId);
            } else {
                System.out.println("DEBUG: Failed to create new stock record");
                redirectWithMessage(response, request, "/product-stock/list", 
                                  "error", "Không thể tạo record tồn kho mới!");
                return;
            }
        }
        
        System.out.println("DEBUG: Stock record found/created with quantity: " + existingStock.getQty());
        
        // Set attributes for JSP
        request.setAttribute("product", product);
        request.setAttribute("stock", existingStock);
        
        // Forward to edit form
        request.getRequestDispatcher("/WEB-INF/views/product-stock-edit.jsp")
               .forward(request, response);
        
    } catch (NumberFormatException e) {
        System.out.println("DEBUG: Invalid product ID format");
        redirectWithMessage(response, request, "/product-stock/list", 
                          "error", "ID sản phẩm không hợp lệ!");
    } catch (Exception e) {
        System.out.println("DEBUG: Exception in showEditStockForm: " + e.getMessage());
        e.printStackTrace();
        redirectWithMessage(response, request, "/product-stock/list", 
                          "error", "Có lỗi xảy ra khi tải form chỉnh sửa!");
    }
}

    
    /**
     * Show stock detail
     */
    private void showStockDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/product-stock/list?error=ID sản phẩm không được để trống");
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            
            // Get product information
            ProductInfo product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/product-stock/list?error=Sản phẩm không tồn tại");
                return;
            }
            
            // Get stock information
            ProductInStock stock = stockDAO.getStockByProductId(productId);
            
            request.setAttribute("product", product);
            request.setAttribute("stock", stock);
            
            request.getRequestDispatcher("/product-stock-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/product-stock/list?error=ID sản phẩm không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/product-stock/list?error=Có lỗi xảy ra khi tải chi tiết tồn kho");
        }
    }
    
    /**
     * Add new stock record
     */
    private void addStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            String qtyParam = request.getParameter("qty");
            String minThresholdParam = request.getParameter("minThreshold");
            
            // Validation
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn sản phẩm");
                showAddStockForm(request, response);
                return;
            }
            
            if (qtyParam == null || qtyParam.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập số lượng");
                showAddStockForm(request, response);
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            BigDecimal qty = new BigDecimal(qtyParam);
            
            // Validate quantity
            if (qty.compareTo(BigDecimal.ZERO) < 0) {
                request.setAttribute("error", "Số lượng không được âm");
                showAddStockForm(request, response);
                return;
            }
            
            // Check if product exists and is active
            ProductInfo product = productDAO.getProductById(productId);
            if (product == null) {
                request.setAttribute("error", "Sản phẩm không tồn tại");
                showAddStockForm(request, response);
                return;
            }
            
            if (!"active".equals(product.getStatus())) {
                request.setAttribute("error", "Không thể thêm tồn kho cho sản phẩm không hoạt động");
                showAddStockForm(request, response);
                return;
            }
            
            // Check if stock already exists
            if (stockDAO.stockExistsForProduct(productId)) {
                request.setAttribute("error", "Sản phẩm đã có thông tin tồn kho");
                showAddStockForm(request, response);
                return;
            }
            
            // Create stock record with active status by default
            ProductInStock stock = new ProductInStock(productId, qty, "active");
            boolean success = stockDAO.addProductStock(stock);
            
            if (success) {
                // Update minimum threshold if provided
                if (minThresholdParam != null && !minThresholdParam.trim().isEmpty()) {
                    try {
                        BigDecimal minThreshold = new BigDecimal(minThresholdParam);
                        productDAO.updateMinStockThreshold(productId, minThreshold);
                    } catch (NumberFormatException e) {
                        // Ignore threshold error, stock was added successfully
                    }
                }
                
                redirectWithMessage(response, request, "/product-stock/list", "success", "Thêm thông tin tồn kho thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi thêm thông tin tồn kho");
                showAddStockForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập không hợp lệ");
            showAddStockForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi hệ thống xảy ra");
            showAddStockForm(request, response);
        }
    }
    
    /**
     * Update existing stock record
     */
    private void updateStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            String qtyParam = request.getParameter("qty");
            String minThresholdParam = request.getParameter("minThreshold");
            
            // Validation
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/product-stock/list?error=ID sản phẩm không được để trống");
                return;
            }
            
            if (qtyParam == null || qtyParam.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập số lượng");
                showEditStockForm(request, response);
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            BigDecimal qty = new BigDecimal(qtyParam);
            
            // Validate quantity
            if (qty.compareTo(BigDecimal.ZERO) < 0) {
                request.setAttribute("error", "Số lượng không được âm");
                showEditStockForm(request, response);
                return;
            }
            
            // Check if product exists and is active
            ProductInfo product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/product-stock/list?error=Sản phẩm không tồn tại");
                return;
            }
            
            if (!"active".equals(product.getStatus())) {
                request.setAttribute("error", "Không thể cập nhật tồn kho cho sản phẩm không hoạt động");
                showEditStockForm(request, response);
                return;
            }
            
            // Update stock quantity only (status remains active)
            boolean qtySuccess = stockDAO.updateStockQuantity(productId, qty);
            
            // Update minimum threshold if provided
            boolean thresholdSuccess = true;
            if (minThresholdParam != null && !minThresholdParam.trim().isEmpty()) {
                try {
                    BigDecimal minThreshold = new BigDecimal(minThresholdParam);
                    thresholdSuccess = productDAO.updateMinStockThreshold(productId, minThreshold);
                } catch (NumberFormatException e) {
                    thresholdSuccess = false;
                }
            }
            
            if (qtySuccess) {
                String message = "Cập nhật thông tin tồn kho thành công";
                if (!thresholdSuccess) {
                    message += " (có lỗi khi cập nhật ngưỡng cảnh báo)";
                }
                redirectWithMessage(response, request, "/product-stock/list", "success", message);
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin tồn kho");
                showEditStockForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập không hợp lệ");
            showEditStockForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi hệ thống xảy ra");
            showEditStockForm(request, response);
        }
    }
    
    /**
     * Delete stock record
     */
    private void deleteStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productIdParam = request.getParameter("productId");
            
            if (productIdParam == null || productIdParam.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/product-stock/list?error=ID sản phẩm không được để trống");
                return;
            }
            
            int productId = Integer.parseInt(productIdParam);
            
            // Check if product exists
            ProductInfo product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/product-stock/list?error=Sản phẩm không tồn tại");
                return;
            }
            
            // Delete stock record
            boolean success = stockDAO.deleteStockByProductId(productId);
            
            if (success) {
                redirectWithMessage(response, request, "/product-stock/list", "success", "Xóa thông tin tồn kho thành công");
            } else {
                redirectWithMessage(response, request, "/product-stock/list", "error", "Có lỗi xảy ra khi xóa thông tin tồn kho");
            }
            
        } catch (NumberFormatException e) {
            redirectWithMessage(response, request, "/product-stock/list", "error", "ID sản phẩm không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            redirectWithMessage(response, request, "/product-stock/list", "error", "Có lỗi hệ thống xảy ra");
        }
    }
}
