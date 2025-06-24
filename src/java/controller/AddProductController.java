package controller;

// Controller for adding a new product
import dao.ProductInfoDAO;
import model.ProductInfo;
import model.CategoryProduct;
import model.Unit;
import model.Supplier;
import model.Users;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AddProductController extends HttpServlet {
    
    private final ProductInfoDAO productDAO = new ProductInfoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and has proper role
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Load dropdown data
        loadDropdownData(request);
        
        // Forward to add product form
        request.getRequestDispatcher("/add-product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Get form parameters
            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String categoryIdStr = request.getParameter("categoryId");
            String unitIdStr = request.getParameter("unitId");
            String priceStr = request.getParameter("price");
            String status = request.getParameter("status");
            String description = request.getParameter("description");
            String supplierIdStr = request.getParameter("supplierId");
            String expirationDateStr = request.getParameter("expirationDate");
            String storageLocation = request.getParameter("storageLocation");
            String additionalNotes = request.getParameter("additionalNotes");
            
            // Validate required fields
            String validationError = validateInput(name, code, categoryIdStr, unitIdStr, priceStr, status);
            if (validationError != null) {
                loadDropdownData(request);
                request.setAttribute("error", validationError);
                request.setAttribute("formData", request.getParameterMap());
                request.getRequestDispatcher("/add-product.jsp").forward(request, response);
                return;
            }
            
            // Check for duplicate product code
            if (productDAO.isProductCodeExists(code.trim())) {
                loadDropdownData(request);
                request.setAttribute("error", "Mã sản phẩm đã tồn tại. Vui lòng chọn mã khác.");
                request.setAttribute("formData", request.getParameterMap());
                request.getRequestDispatcher("/add-product.jsp").forward(request, response);
                return;
            }
            
            // Create ProductInfo object
            ProductInfo product = new ProductInfo();
            product.setName(name.trim());
            product.setCode(code.trim().toUpperCase());
            product.setCate_id(Integer.parseInt(categoryIdStr));
            product.setUnit_id(Integer.parseInt(unitIdStr));
            product.setPrice(new BigDecimal(priceStr));
            product.setStatus(status);
            product.setDescription(description != null ? description.trim() : "");
            
            // Set optional fields
            if (supplierIdStr != null && !supplierIdStr.isEmpty()) {
                product.setSupplierId(Integer.parseInt(supplierIdStr));
            }
            
            if (expirationDateStr != null && !expirationDateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdf.parse(expirationDateStr);
                product.setExpirationDate(new Date(parsedDate.getTime()));
            }
            
            if (storageLocation != null && !storageLocation.isEmpty()) {
                product.setStorageLocation(storageLocation.trim());
            }           
            
            if (additionalNotes != null && !additionalNotes.isEmpty()) {
                product.setAdditionalNotes(additionalNotes.trim());
            }              // Add product to database
            boolean success = productDAO.addProduct(product, user.getId());
              if (success) {                // Set success message and forward to a success page
                request.setAttribute("successMessage", "Sản phẩm đã được thêm thành công!");
                request.setAttribute("productName", product.getName());
                request.setAttribute("productCode", product.getCode());
                request.getRequestDispatcher("/ProductSuccessNotification.jsp").forward(request, response);
            } else {
                loadDropdownData(request);
                request.setAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm. Vui lòng thử lại.");
                request.setAttribute("formData", request.getParameterMap());
                request.getRequestDispatcher("/add-product.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            loadDropdownData(request);
            request.setAttribute("error", "Dữ liệu số không hợp lệ. Vui lòng kiểm tra lại.");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/add-product.jsp").forward(request, response);
        } catch (ParseException e) {
            loadDropdownData(request);
            request.setAttribute("error", "Định dạng ngày không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd.");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/add-product.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            loadDropdownData(request);
            request.setAttribute("error", "Có lỗi hệ thống xảy ra. Vui lòng thử lại sau.");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/add-product.jsp").forward(request, response);
        }
    }
    
    private void loadDropdownData(HttpServletRequest request) {
        try {
            List<CategoryProduct> categories = productDAO.getAllActiveCategories();
            List<Unit> units = productDAO.getAllActiveUnits();
            List<Supplier> suppliers = productDAO.getAllActiveSuppliers();
            List<String> storageLocations = productDAO.getAllStorageLocations();
            
            request.setAttribute("categories", categories);
            request.setAttribute("units", units);
            request.setAttribute("suppliers", suppliers);
            request.setAttribute("storageLocations", storageLocations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String validateInput(String name, String code, String categoryId, String unitId, 
                                String price, String status) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên sản phẩm không được để trống.";
        }
        if (name.trim().length() > 100) {
            return "Tên sản phẩm không được vượt quá 100 ký tự.";
        }
        
        if (code == null || code.trim().isEmpty()) {
            return "Mã sản phẩm không được để trống.";
        }
        if (code.trim().length() > 50) {
            return "Mã sản phẩm không được vượt quá 50 ký tự.";
        }
        
        if (categoryId == null || categoryId.isEmpty()) {
            return "Vui lòng chọn danh mục sản phẩm.";
        }
        
        if (unitId == null || unitId.isEmpty()) {
            return "Vui lòng chọn đơn vị tính.";
        }
        
        if (price == null || price.trim().isEmpty()) {
            return "Giá sản phẩm không được để trống.";
        }
        
        try {
            BigDecimal priceValue = new BigDecimal(price);
            if (priceValue.compareTo(BigDecimal.ZERO) < 0) {
                return "Giá sản phẩm phải lớn hơn hoặc bằng 0.";
            }
        } catch (NumberFormatException e) {
            return "Giá sản phẩm không hợp lệ.";
        }
        
        if (status == null || status.isEmpty()) {
            return "Vui lòng chọn trạng thái sản phẩm.";
        }
        
        return null; // No validation errors
    }
}
