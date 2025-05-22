package controller;

import dao.CategoryDAO;
import model.Category;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CategoryController", urlPatterns = {"/category"})
public class CategoryController extends HttpServlet {
    
    private CategoryDAO categoryDAO = new CategoryDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if (action == null) {
            // Mặc định hiển thị danh sách
            listCategories(request, response);
        } else {
            switch (action) {
                case "delete":
                    deleteCategory(request, response);
                    break;
                case "toggle":
                    toggleStatus(request, response);
                    break;
                default:
                    listCategories(request, response);
                    break;
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/category");
        } else {
            switch (action) {
                case "add":
                    addCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/category");
                    break;
            }
        }
    }
    
    // Hiển thị danh sách danh mục có phân trang và tìm kiếm
    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy tham số tìm kiếm và phân trang
        String searchKeyword = request.getParameter("search");
        if (searchKeyword == null) {
            searchKeyword = "";
        }
        
        int page = 1;
        int pageSize = 10;
        
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            if (request.getParameter("size") != null) {
                pageSize = Integer.parseInt(request.getParameter("size"));
            }
        } catch (NumberFormatException e) {
            // Sử dụng giá trị mặc định nếu có lỗi
        }
        
        // Lấy danh sách danh mục
        List<Category> categories = categoryDAO.getAllCategories(page, pageSize, searchKeyword);
        
        // Tính tổng số trang
        int totalCategories = categoryDAO.countCategories(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
        
        // Lấy danh sách danh mục cha cho dropdown
        List<Category> parentCategories = categoryDAO.getParentCategories();
        
        // Đặt thuộc tính để hiển thị trong JSP
        request.setAttribute("categories", categories);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("searchKeyword", searchKeyword);
        
        // Forward đến trang JSP
        request.getRequestDispatcher("/category.jsp").forward(request, response);
    }
    
    // Xử lý thêm mới danh mục
    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String parentIdStr = request.getParameter("parentId");
        String activeFlagStr = request.getParameter("activeFlag");
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên danh mục không được để trống");
            request.setAttribute("showAddForm", true);
            listCategories(request, response);
            return;
        }
        
        // Tạo đối tượng Category
        Category category = new Category();
        category.setName(name);
        
        // Xử lý parentId
        if (parentIdStr != null && !parentIdStr.trim().isEmpty()) {
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
        
        // Thêm danh mục vào database
        boolean success = categoryDAO.addCategory(category);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            request.setAttribute("message", "Thêm danh mục thành công");
            listCategories(request, response);
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("error", "Không thể thêm danh mục");
            request.setAttribute("category", category);
            request.setAttribute("showAddForm", true);
            listCategories(request, response);
        }
    }
    
    // Xử lý cập nhật danh mục
    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String parentIdStr = request.getParameter("parentId");
        String activeFlagStr = request.getParameter("activeFlag");
        
        // Kiểm tra dữ liệu
        if (idStr == null || idStr.trim().isEmpty() || name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            listCategories(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Tạo đối tượng Category
            Category category = new Category();
            category.setId(id);
            category.setName(name);
            
            // Xử lý parentId
            if (parentIdStr != null && !parentIdStr.trim().isEmpty()) {
                try {
                    int parentId = Integer.parseInt(parentIdStr);
                    
                    // Kiểm tra xem parentId có phải là id của chính danh mục này không
                    if (parentId == id) {
                        request.setAttribute("error", "Danh mục không thể là cha của chính nó");
                        request.setAttribute("category", category);
                        request.setAttribute("showEditForm", true);
                        listCategories(request, response);
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
            
            // Cập nhật danh mục vào database
            boolean success = categoryDAO.updateCategory(category);
            
            if (success) {
                // Chuyển hướng về trang danh sách với thông báo thành công
                request.setAttribute("message", "Cập nhật danh mục thành công");
                listCategories(request, response);
            } else {
                // Hiển thị lại form với thông báo lỗi
                request.setAttribute("error", "Không thể cập nhật danh mục");
                request.setAttribute("category", category);
                request.setAttribute("showEditForm", true);
                listCategories(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
            listCategories(request, response);
        }
    }
    
    // Xử lý xóa danh mục
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy ID từ request
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("error", "ID không hợp lệ");
            listCategories(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            // Kiểm tra xem danh mục có danh mục con không
            if (categoryDAO.hasChildCategories(id)) {
                request.setAttribute("error", "Không thể xóa danh mục này vì có danh mục con.");
                listCategories(request, response);
                return;
            }
            
            // Kiểm tra xem danh mục có đang được sử dụng không
            if (categoryDAO.isInUse(id)) {
                request.setAttribute("error", "Không thể xóa danh mục này vì đang được sử dụng.");
                listCategories(request, response);
                return;
            }
            
            // Xóa danh mục
            boolean success = categoryDAO.deleteCategory(id);
            
            if (success) {
                // Chuyển hướng về trang danh sách với thông báo thành công
                request.setAttribute("message", "Xóa danh mục thành công");
            } else {
                // Chuyển hướng về trang danh sách với thông báo lỗi
                request.setAttribute("error", "Không thể xóa danh mục. Vui lòng thử lại sau.");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
        }
        
        listCategories(request, response);
    }
    
    // Xử lý thay đổi trạng thái active/deactive
    private void toggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy ID và trạng thái từ request
        String idStr = request.getParameter("id");
        String statusStr = request.getParameter("status");
        
        if (idStr == null || idStr.trim().isEmpty() || statusStr == null) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            listCategories(request, response);
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            boolean status = statusStr.equals("1");
            
            // Thay đổi trạng thái
            boolean success = categoryDAO.toggleCategoryStatus(id, status);
            
            if (success) {
                // Chuyển hướng về trang danh sách với thông báo thành công
                request.setAttribute("message", status ? "Đã kích hoạt danh mục" : "Đã vô hiệu hóa danh mục");
            } else {
                // Chuyển hướng về trang danh sách với thông báo lỗi
                request.setAttribute("error", "Không thể thay đổi trạng thái danh mục");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
        }
        
        listCategories(request, response);
    }
}
