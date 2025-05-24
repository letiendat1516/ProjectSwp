package controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.CategoryDAO;
import model.Category;

@WebServlet(name = "CategoryController", urlPatterns = {"/category/*"})
public class CategoryController extends HttpServlet {
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() {
        categoryDAO = new CategoryDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            listCategories(request, response);
        } else if (pathInfo.equals("create")) {
            showCreateForm(request, response);
        } else if (pathInfo.equals("edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("delete")) {
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
            response.sendRedirect(request.getContextPath() + "list");
        } else if (pathInfo.equals("create")) {
            createCategory(request, response);
        } else if (pathInfo.equals("edit")) {
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
        
        // Lấy danh sách danh mục theo phân trang và tìm kiếm
        List<Category> categories = categoryDAO.getAllCategories(page, pageSize, searchKeyword);
        
        // Đếm tổng số danh mục để tính số trang
        int totalCategories = categoryDAO.countCategories(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
        
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", searchKeyword);
        
        request.getRequestDispatcher("JSP/Category/list.jsp").forward(request, response);
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy danh sách danh mục để hiển thị dropdown danh mục cha
        List<Category> parentCategories = categoryDAO.getAllCategoriesForDropdown();
        request.setAttribute("parentCategories", parentCategories);
        
        request.getRequestDispatcher("JSP/Category/create.jsp").forward(request, response);
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
        
        // Tạo đối tượng Category
        Category category = new Category();
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
            Category category = categoryDAO.getCategoryById(id);
            
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/category/list?error=category_not_found");
                return;
            }
            
            // Lấy danh sách danh mục để hiển thị dropdown danh mục cha
            List<Category> parentCategories = categoryDAO.getAllCategoriesForDropdown();
            request.setAttribute("parentCategories", parentCategories);
            request.setAttribute("category", category);
            
            request.getRequestDispatcher("JSP/Category/edit.jsp").forward(request, response);
            
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
            
            // Tạo đối tượng Category
            Category category = new Category();
            category.setId(id);
            category.setName(name.trim());
            
            // Xử lý parentId
            if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("0")) {
                try {
                    int parentId = Integer.parseInt(parentIdStr);
                    // Kiểm tra không cho phép chọn chính nó làm danh mục cha
                    if (parentId == id) {
                        request.setAttribute("error", "Không thể chọn chính danh mục này làm danh mục cha");
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