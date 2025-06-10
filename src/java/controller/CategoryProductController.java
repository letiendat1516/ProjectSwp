package controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.CategoryProductDAO;
import model.CategoryProduct;
import model.CategoryProductParent;

@WebServlet(name = "CategoryController", urlPatterns = {"/category/*"})
public class CategoryProductController extends HttpServlet {
    private CategoryProductDAO categoryDAO;
    
    @Override
    public void init() {
        categoryDAO = new CategoryProductDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            listCategories(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            deleteCategory(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/category/list");
        } else if (pathInfo.equals("/create")) {
            createCategory(request, response);
        } else if (pathInfo.equals("/edit")) {
            updateCategory(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xử lý phân trang
        int page = 1;
        int pageSize = 10;
        
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // Giữ nguyên page = 1
            }
        }
        
        // Xử lý tìm kiếm
        String searchKeyword = request.getParameter("search");
        
        // Xử lý sắp xếp
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");
        
        // Giá trị mặc định cho sắp xếp
        if (sortField == null || sortField.trim().isEmpty()) {
            sortField = "id"; // Mặc định sắp xếp theo ID
        }
        
        if (sortDir == null || sortDir.trim().isEmpty()) {
            sortDir = "asc"; // Mặc định sắp xếp tăng dần
        }
        
        // ✅ SỬA: Sử dụng method có JOIN để lấy thông tin parent
        List<CategoryProduct> categories = categoryDAO.getAllCategoriesWithParent(page, pageSize, searchKeyword, sortField, sortDir);
        
        // Đếm tổng số danh mục để tính số trang
        int totalCategories = categoryDAO.countCategories(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
        
        // Tính toán thông tin phân trang
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);
        
        // Đảo ngược hướng sắp xếp cho lần click tiếp theo
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        request.setAttribute("reverseSortDir", reverseSortDir);
        
        request.getRequestDispatcher("/category_product/list.jsp").forward(request, response);
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ SỬA: Lấy danh sách category parent thay vì category
        List<CategoryProductParent> parentCategories = categoryDAO.getCategoryParentsForDropdown();
        request.setAttribute("parentCategories", parentCategories);
        
        request.getRequestDispatcher("/category_product/create.jsp").forward(request, response);
    }
    
    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String parentIdStr = request.getParameter("parentId");
        String activeFlagStr = request.getParameter("activeFlag");
        
        // Validate dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên danh mục không được để trống");
            showCreateForm(request, response);
            return;
        }
        
        // Tạo đối tượng CategoryProduct
        CategoryProduct category = new CategoryProduct();
        category.setName(name.trim());
        
        // Xử lý parentId
        if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("0")) {
            try {
                int parentId = Integer.parseInt(parentIdStr);
                category.setParentId(parentId);
            } catch (NumberFormatException e) {
                category.setParentId(null);
            }
        } else {
            category.setParentId(null);
        }
        
        // Xử lý activeFlag
        boolean activeFlag = activeFlagStr != null && activeFlagStr.equals("1");
        category.setActiveFlag(activeFlag);
        
        // Lưu vào database
        boolean success = categoryDAO.addCategory(category);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/category/list?message=create_success");
        } else {
            request.setAttribute("error", "Không thể thêm danh mục. Vui lòng thử lại.");
            showCreateForm(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy id từ request
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // ✅ SỬA: Sử dụng method có JOIN để lấy thông tin parent
            CategoryProduct category = categoryDAO.getCategoryWithParentById(id);
            
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/category/list?error=category_not_found");
                return;
            }
            
            // ✅ SỬA: Lấy danh sách category parent thay vì category
            List<CategoryProductParent> parentCategories = categoryDAO.getCategoryParentsForDropdown();
            request.setAttribute("parentCategories", parentCategories);
            request.setAttribute("category", category);
            
            request.getRequestDispatcher("/category_product/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
        }
    }
    
    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String parentIdStr = request.getParameter("parentId");
        String activeFlagStr = request.getParameter("activeFlag");
        
        // Validate dữ liệu
        if (idStr == null || idStr.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_data");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Tạo đối tượng CategoryProduct
            CategoryProduct category = new CategoryProduct();
            category.setId(id);
            category.setName(name.trim());
            
            // Xử lý parentId
            if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("0")) {
                try {
                    int parentId = Integer.parseInt(parentIdStr);
                    // ✅ SỬA: Kiểm tra parent_id hợp lệ thay vì kiểm tra tự tham chiếu
                    // (vì parent_id tham chiếu đến bảng category_parent, không phải category)
                    if (!categoryDAO.isValidParentId(parentId)) {
                        request.setAttribute("error", "Danh mục cha không hợp lệ");
                        showEditForm(request, response);
                        return;
                    }
                    category.setParentId(parentId);
                } catch (NumberFormatException e) {
                    category.setParentId(null);
                }
            } else {
                category.setParentId(null);
            }
            
            // Xử lý activeFlag
            boolean activeFlag = activeFlagStr != null && activeFlagStr.equals("1");
            category.setActiveFlag(activeFlag);
            
            // Cập nhật vào database
            boolean success = categoryDAO.updateCategory(category);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category/list?message=update_success");
            } else {
                request.setAttribute("error", "Không thể cập nhật danh mục. Vui lòng thử lại.");
                showEditForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
        }
    }
    
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy id từ request
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Xóa danh mục
            boolean success = categoryDAO.deleteCategory(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category/list?message=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/category/list?error=delete_failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
        }
    }
}
