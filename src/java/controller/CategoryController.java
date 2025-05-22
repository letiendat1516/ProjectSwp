package controller;

import dao.CategoryDAO;
import dao.UnitDAO;
import model.Category;
import model.Unit;
import model.ParentCategoryDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet(name = "CategoryController", urlPatterns = {"/category"})
public class CategoryController extends HttpServlet {
    
    private CategoryDAO categoryDAO;
    private UnitDAO unitDAO;
    
    @Override
    public void init() {
        categoryDAO = new CategoryDAO();
        unitDAO = new UnitDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String tab = request.getParameter("tab");
        
        if (action == null) {
            // Mặc định hiển thị danh sách
            listAll(request, response);
        } else {
            switch (action) {
                case "delete":
                    deleteCategory(request, response);
                    break;
                case "toggle":
                    toggleStatus(request, response);
                    break;
                default:
                    listAll(request, response);
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
                // Xử lý danh mục
                case "add":
                    addCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                case "getCategory":
                    getCategoryById(request, response);
                    break;
                
                // Xử lý danh mục cha
                case "add_parent":
                    addParentCategory(request, response);
                    break;
                case "update_parent":
                    updateParentCategory(request, response);
                    break;
                
                // Xử lý đơn vị tính
                case "add_unit":
                    addUnit(request, response);
                    break;
                case "update_unit":
                    updateUnit(request, response);
                    break;
                case "delete_unit":
                    deleteUnit(request, response);
                    break;
                case "getUnit":
                    getUnitById(request, response);
                    break;
                    
                default:
                    response.sendRedirect(request.getContextPath() + "/category");
                    break;
            }
        }
    }
    
    // Hiển thị tất cả danh mục và đơn vị tính
    private void listAll(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy tham số tìm kiếm và phân trang cho danh mục
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
        
        // Tính tổng số trang cho danh mục
        int totalCategories = categoryDAO.countCategories(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
        
        // Lấy danh sách danh mục cha cho dropdown
        List<Category> parentCategories = categoryDAO.getParentCategories();
        
        // Lấy danh sách danh mục cha kèm số lượng danh mục con
        List<ParentCategoryDTO> parentCategoriesWithCount = categoryDAO.getParentCategoriesWithChildCount();
        
        // Lấy danh sách đơn vị tính
        List<Unit> units = unitDAO.getAllUnits();
        
        // Đặt thuộc tính để hiển thị trong JSP
        request.setAttribute("categories", categories);
        request.setAttribute("parentCategories", parentCategories);
        request.setAttribute("parentCategoriesWithCount", parentCategoriesWithCount);
        request.setAttribute("units", units);
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
        String name = request.getParameter("categoryName");
        int parentId = Integer.parseInt(request.getParameter("parentCategory"));
        int status = Integer.parseInt(request.getParameter("status"));
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên danh mục không được để trống");
            listAll(request, response);
            return;
        }
        
        // Tạo đối tượng Category
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setActiveFlag(status == 1);
        
        // Thêm danh mục vào database
        boolean success = categoryDAO.addCategory(category);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/category?success=add_category&tab=category");
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể thêm danh mục");
            listAll(request, response);
        }
    }
    
    // Xử lý thêm mới danh mục cha
    private void addParentCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        String name = request.getParameter("categoryName");
        int status = Integer.parseInt(request.getParameter("status"));
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên danh mục cha không được để trống");
            listAll(request, response);
            return;
        }
        
        // Tạo đối tượng Category
        Category category = new Category();
        category.setName(name);
        category.setParentId(0); // Danh mục cha có parent_id = 0
        category.setActiveFlag(status == 1);
        
        // Thêm danh mục vào database
        boolean success = categoryDAO.addCategory(category);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/category?success=add_category&tab=parent");
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể thêm danh mục cha");
            listAll(request, response);
        }
    }
    
    // Xử lý cập nhật danh mục
    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        int id = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("categoryName");
        int parentId = Integer.parseInt(request.getParameter("parentCategory"));
        int status = Integer.parseInt(request.getParameter("status"));
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên danh mục không được để trống");
            listAll(request, response);
            return;
        }
        
        // Kiểm tra xem parentId có phải là id của chính danh mục này không
        if (parentId == id) {
            request.setAttribute("errorMessage", "Danh mục không thể là cha của chính nó");
            listAll(request, response);
            return;
        }
        
        // Tạo đối tượng Category
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setParentId(parentId);
        category.setActiveFlag(status == 1);
        
        // Cập nhật danh mục vào database
        boolean success = categoryDAO.updateCategory(category);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/category?success=update_category&tab=category");
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể cập nhật danh mục");
            listAll(request, response);
        }
    }
    
    // Xử lý cập nhật danh mục cha
    private void updateParentCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        int id = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("categoryName");
        int status = Integer.parseInt(request.getParameter("status"));
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên danh mục cha không được để trống");
            listAll(request, response);
            return;
        }
        
        // Tạo đối tượng Category
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setParentId(0); // Đảm bảo vẫn là danh mục cha
        category.setActiveFlag(status == 1);
        
        // Cập nhật danh mục vào database
        boolean success = categoryDAO.updateCategory(category);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/category?success=update_category&tab=parent");
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể cập nhật danh mục cha");
            listAll(request, response);
        }
    }
    
    // Xử lý xóa danh mục
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra xem là AJAX request hay không
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        
        try {
            // Lấy ID từ request
            int id = Integer.parseInt(request.getParameter("categoryId"));
            
            // Kiểm tra xem danh mục có danh mục con không
            if (categoryDAO.hasChildCategories(id)) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Không thể xóa danh mục này vì có danh mục con.");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa danh mục này vì có danh mục con.");
                    listAll(request, response);
                }
                return;
            }
            
            // Kiểm tra xem danh mục có đang được sử dụng không
            if (categoryDAO.isInUse(id)) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Không thể xóa danh mục này vì đang được sử dụng.");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa danh mục này vì đang được sử dụng.");
                    listAll(request, response);
                }
                return;
            }
            
            // Xóa danh mục
            boolean success = categoryDAO.deleteCategory(id);
            
            if (isAjax) {
                sendJsonResponse(response, success, success ? "Xóa danh mục thành công" : "Không thể xóa danh mục");
            } else {
                if (success) {
                    request.setAttribute("message", "Xóa danh mục thành công");
                } else {
                    request.setAttribute("errorMessage", "Không thể xóa danh mục. Vui lòng thử lại sau.");
                }
                listAll(request, response);
            }
            
        } catch (NumberFormatException e) {
            if (isAjax) {
                sendJsonResponse(response, false, "ID không hợp lệ");
            } else {
                request.setAttribute("errorMessage", "ID không hợp lệ");
                listAll(request, response);
            }
        }
    }
    
    // Xử lý thay đổi trạng thái active/deactive
    private void toggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy ID và trạng thái từ request
        String idStr = request.getParameter("id");
        String statusStr = request.getParameter("status");
        
        if (idStr == null || idStr.trim().isEmpty() || statusStr == null) {
            request.setAttribute("errorMessage", "Dữ liệu không hợp lệ");
            listAll(request, response);
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
                request.setAttribute("errorMessage", "Không thể thay đổi trạng thái danh mục");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID không hợp lệ");
        }
        
        listAll(request, response);
    }
    
    // Lấy thông tin danh mục theo ID và trả về dạng JSON
    private void getCategoryById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("categoryId"));
            Category category = categoryDAO.getCategoryById(id);
            
            if (category != null) {
                Gson gson = new Gson();
                String json = gson.toJson(category);
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Không tìm thấy danh mục\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"ID không hợp lệ\"}");
        }
    }
    
    // Xử lý thêm mới đơn vị tính
    private void addUnit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        String name = request.getParameter("unitName");
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên đơn vị tính không được để trống");
            listAll(request, response);
            return;
        }
        
        // Tạo đối tượng Unit
        Unit unit = new Unit();
        unit.setName(name);
        
        // Thêm đơn vị tính vào database
        boolean success = unitDAO.addUnit(unit);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/category?success=add_unit&tab=unit");
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể thêm đơn vị tính");
            listAll(request, response);
        }
    }
    
    // Xử lý cập nhật đơn vị tính
    private void updateUnit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy dữ liệu từ form
        int id = Integer.parseInt(request.getParameter("unitId"));
        String name = request.getParameter("unitName");
        
        // Kiểm tra dữ liệu
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tên đơn vị tính không được để trống");
            listAll(request, response);
            return;
        }
        
        // Tạo đối tượng Unit
        Unit unit = new Unit();
        unit.setId(id);
        unit.setName(name);
        
        // Cập nhật đơn vị tính vào database
        boolean success = unitDAO.updateUnit(unit);
        
        if (success) {
            // Chuyển hướng về trang danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/category?success=update_unit&tab=unit");
        } else {
            // Hiển thị lại form với thông báo lỗi
            request.setAttribute("errorMessage", "Không thể cập nhật đơn vị tính");
            listAll(request, response);
        }
    }
    
    // Xử lý xóa đơn vị tính
    private void deleteUnit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Lấy ID từ request
            int id = Integer.parseInt(request.getParameter("unitId"));
            
            // Kiểm tra xem đơn vị tính có đang được sử dụng không
            if (unitDAO.isUnitInUse(id)) {
                sendJsonResponse(response, false, "Không thể xóa đơn vị tính này vì đang được sử dụng.");
                return;
            }
            
            // Xóa đơn vị tính
            boolean success = unitDAO.deleteUnit(id);
            
            sendJsonResponse(response, success, success ? "Xóa đơn vị tính thành công" : "Không thể xóa đơn vị tính");
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "ID không hợp lệ");
        }
    }
    
    // Lấy thông tin đơn vị tính theo ID và trả về dạng JSON
    private void getUnitById(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("unitId"));
            Unit unit = unitDAO.getUnitById(id);
            
            if (unit != null) {
                Gson gson = new Gson();
                String json = gson.toJson(unit);
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } else {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"Không tìm thấy đơn vị tính\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"ID không hợp lệ\"}");
        }
    }
    
    // Phương thức gửi phản hồi JSON
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("{\"success\": " + success + ", \"message\": \"" + message + "\"}");
        out.flush();
    }
}
