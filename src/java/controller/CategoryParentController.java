package controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.CategoryParentDAO;
import model.CategoryProductParent;

@WebServlet(name = "CategoryParentController", urlPatterns = {"/category-parent/*"})
public class CategoryParentController extends HttpServlet {
    private CategoryParentDAO categoryParentDAO;
    
    @Override
    public void init() {
        categoryParentDAO = new CategoryParentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            listCategoryParents(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            deleteCategoryParent(request, response);
        } else if (pathInfo.equals("/soft-delete")) {
            softDeleteCategoryParent(request, response);
        } else if (pathInfo.equals("/toggle-status")) {
            toggleActiveFlag(request, response);
        } else if (pathInfo.equals("/view")) {
            viewCategoryParent(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list");
        } else if (pathInfo.equals("/create")) {
            createCategoryParent(request, response);
        } else if (pathInfo.equals("/edit")) {
            updateCategoryParent(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    // ==================== LIST & SEARCH ====================
    
    private void listCategoryParents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Xử lý phân trang
            int page = 1;
            int pageSize = 10;
            
            String pageParam = request.getParameter("page");
            String pageSizeParam = request.getParameter("pageSize");
            
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            
            if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                try {
                    pageSize = Integer.parseInt(pageSizeParam);
                    if (pageSize < 5) pageSize = 5;
                    if (pageSize > 100) pageSize = 100;
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }
            
            // Xử lý tìm kiếm
            String searchKeyword = request.getParameter("search");
            if (searchKeyword != null) {
                searchKeyword = searchKeyword.trim();
                if (searchKeyword.isEmpty()) {
                    searchKeyword = null;
                }
            }
            
            // Xử lý sắp xếp
            String sortField = request.getParameter("sortField");
            String sortDir = request.getParameter("sortDir");
            
            // Validate sortField
            if (sortField == null || (!sortField.equals("id") && !sortField.equals("name") && !sortField.equals("description"))) {
                sortField = "id";
            }
            
            if (sortDir == null || (!sortDir.equals("asc") && !sortDir.equals("desc"))) {
                sortDir = "asc";
            }
            
            // Lấy danh sách danh mục 
            List<CategoryProductParent> categoryParents = categoryParentDAO.getAllCategoryParents(page, pageSize, searchKeyword, sortField, sortDir);
            
            // Đếm tổng số danh mục 
            int totalCategoryParents = categoryParentDAO.countCategoryParents(searchKeyword);
            int totalPages = (int) Math.ceil((double) totalCategoryParents / pageSize);
            
            // Tính toán thông tin phân trang
            int startPage = Math.max(1, page - 2);
            int endPage = Math.min(totalPages, page + 2);
            
            // Đảo ngược hướng sắp xếp cho lần click tiếp theo
            String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
            
            // Lấy số lượng danh mục con cho mỗi parent
            for (CategoryProductParent parent : categoryParents) {
                int childCount = categoryParentDAO.getChildCategoryCount(parent.getId());
                parent.setChildCount(childCount); // Cần thêm field này vào model
            }
            
            // Set attributes
            request.setAttribute("categoryParents", categoryParents);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCategoryParents", totalCategoryParents);
            request.setAttribute("startPage", startPage);
            request.setAttribute("endPage", endPage);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("sortField", sortField);
            request.setAttribute("sortDir", sortDir);
            request.setAttribute("reverseSortDir", reverseSortDir);
            
            // Thêm thông tin về message/error
            String message = request.getParameter("message");
            String error = request.getParameter("error");
            if (message != null) request.setAttribute("message", getMessageText(message));
            if (error != null) request.setAttribute("error", getErrorText(error));
            
            // ✅ ĐÃ SỬA: Đường dẫn vào folder category_parent
            request.getRequestDispatcher("/category_parent/list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách danh mục ");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    // ==================== CREATE ====================
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/category_parent/create.jsp").forward(request, response);
    }
    
    private void createCategoryParent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String activeFlagStr = request.getParameter("activeFlag");
            
            // Validate dữ liệu
            if (!validateCategoryParentData(name, description)) {
                request.setAttribute("error", "Dữ liệu không hợp lệ. Tên danh mục không được để trống và không quá 255 ký tự.");
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.setAttribute("activeFlag", activeFlagStr);
                showCreateForm(request, response);
                return;
            }
            
            // Tạo đối tượng CategoryProductParent
            CategoryProductParent categoryParent = new CategoryProductParent();
            categoryParent.setName(name.trim());
            categoryParent.setDescription(description != null ? description.trim() : "");
            
            // Xử lý activeFlag
            boolean activeFlag = activeFlagStr != null && activeFlagStr.equals("1");
            categoryParent.setActiveFlag(activeFlag);
            
            // Lưu vào database
            boolean success = categoryParentDAO.addCategoryParent(categoryParent);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?message=create_success");
            } else {
                request.setAttribute("error", "Không thể thêm danh mục . Tên danh mục có thể đã tồn tại.");
                request.setAttribute("name", name);
                request.setAttribute("description", description);
                request.setAttribute("activeFlag", activeFlagStr);
                showCreateForm(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi thêm danh mục ");
            showCreateForm(request, response);
        }
    }
    
    // ==================== EDIT ====================
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            CategoryProductParent categoryParent = categoryParentDAO.getCategoryParentById(id);
            
            if (categoryParent == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }
            
            // Lấy số lượng danh mục con
            int childCount = categoryParentDAO.getChildCategoryCount(id);
            
            request.setAttribute("categoryParent", categoryParent);
            request.setAttribute("childCount", childCount);
            
            // ✅ ĐÃ SỬA: Đường dẫn vào folder category_parent
            request.getRequestDispatcher("/category_parent/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }
    
    private void updateCategoryParent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form
            String idStr = request.getParameter("id");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String activeFlagStr = request.getParameter("activeFlag");
            
            // Validate dữ liệu cơ bản
            if (idStr == null || idStr.trim().isEmpty() || !validateCategoryParentData(name, description)) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_data");
                return;
            }
            
            try {
                int id = Integer.parseInt(idStr);
                
                // Tạo đối tượng CategoryProductParent
                CategoryProductParent categoryParent = new CategoryProductParent();
                categoryParent.setId(id);
                categoryParent.setName(name.trim());
                categoryParent.setDescription(description != null ? description.trim() : "");
                
                // Xử lý activeFlag
                boolean activeFlag = activeFlagStr != null && activeFlagStr.equals("1");
                categoryParent.setActiveFlag(activeFlag);
                
                // Cập nhật vào database
                boolean success = categoryParentDAO.updateCategoryParent(categoryParent);
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/category-parent/list?message=update_success");
                } else {
                    request.setAttribute("error", "Không thể cập nhật danh mục . Tên danh mục có thể đã tồn tại.");
                    request.setAttribute("categoryParent", categoryParent);
                    showEditForm(request, response);
                }
                
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=update_failed");
        }
    }
    
    // ==================== VIEW ====================
    
    private void viewCategoryParent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            CategoryProductParent categoryParent = categoryParentDAO.getCategoryParentById(id);
            
            if (categoryParent == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }
            
            // Lấy số lượng danh mục con
            int childCount = categoryParentDAO.getChildCategoryCount(id);
            
            request.setAttribute("categoryParent", categoryParent);
            request.setAttribute("childCount", childCount);
            
            // ✅ ĐÃ SỬA: Đường dẫn vào folder category_parent
            request.getRequestDispatcher("/category_parent/view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }
    
    // ==================== DELETE ====================
    
    private void deleteCategoryParent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Xóa danh mục  (hard delete)
            boolean success = categoryParentDAO.deleteCategoryParent(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?message=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=delete_failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }
    
    private void softDeleteCategoryParent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Soft delete danh mục 
            boolean success = categoryParentDAO.softDeleteCategoryParent(id);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?message=soft_delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=soft_delete_failed");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }
    
    // ==================== AJAX OPERATIONS ====================
    
    private void toggleActiveFlag(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean success = categoryParentDAO.toggleActiveFlag(id);
            
            if (success) {
                // Lấy trạng thái mới
                CategoryProductParent categoryParent = categoryParentDAO.getCategoryParentById(id);
                String statusText = categoryParent != null && categoryParent.isActiveFlag() ? "Hoạt động" : "Không hoạt động";
                String statusClass = categoryParent != null && categoryParent.isActiveFlag() ? "badge-success" : "badge-secondary";
                
                response.getWriter().write("{\"success\": true, \"message\": \"Cập nhật trạng thái thành công\", \"newStatus\": \"" + statusText + "\", \"statusClass\": \"" + statusClass + "\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể cập nhật trạng thái\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra\"}");
        }
    }
    
    // ==================== VALIDATION ====================
    
    private boolean validateCategoryParentData(String name, String description) {
        // Kiểm tra tên không được rỗng
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra độ dài tên
        if (name.trim().length() > 255) {
            return false;
        }
        
        // Kiểm tra độ dài mô tả (nếu có)
        if (description != null && description.trim().length() > 1000) {
            return false;
        }
        
        return true;
    }
    
    // ==================== UTILITY METHODS ====================
    
    private String getMessageText(String messageKey) {
        switch (messageKey) {
            case "create_success":
                return "Thêm danh mục thành công!";
            case "update_success":
                return "Cập nhật danh mục thành công!";
            case "delete_success":
                return "Xóa danh mục thành công!";
            case "soft_delete_success":
                return "Vô hiệu hóa danh mục thành công!";
            default:
                return "Thao tác thành công!";
        }
    }
    
    private String getErrorText(String errorKey) {
        switch (errorKey) {
            case "invalid_id":
                return "ID không hợp lệ!";
            case "not_found":
                return "Không tìm thấy danh mục !";
            case "invalid_data":
                return "Dữ liệu không hợp lệ!";
            case "delete_failed":
                return "Không thể xóa danh mục . Có thể đang được sử dụng bởi danh mục con.";
            case "soft_delete_failed":
                return "Không thể vô hiệu hóa danh mục !";
            case "update_failed":
                return "Không thể cập nhật danh mục !";
            default:
                return "Có lỗi xảy ra!";
        }
    }
}
