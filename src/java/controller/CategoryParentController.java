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

    private CategoryParentDAO dao;

    @Override
    public void init() {
        dao = new CategoryParentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            showCategoryList(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("/view")) {
            showViewPage(request, response);
        } else if (pathInfo.equals("/delete")) {
            deleteCategory(request, response);
        } else if (pathInfo.equals("/toggle-status")) {
            toggleCategoryStatus(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo.equals("/create")) {
            createCategory(request, response);
        } else if (pathInfo.equals("/edit")) {
            updateCategory(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/category-parent/list");
        }
    }

    private void toggleCategoryStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
            return;
        }
        
        try {
            int id = Integer.parseInt(idStr);
            
            CategoryProductParent category = dao.getCategoryParentById(id);
            if (category == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy danh mục\"}");
                return;
            }
            
            boolean newStatus = !category.isActiveFlag();
            
            int affectedChildren = 0;
            if (!newStatus) {
                affectedChildren = dao.getActiveChildCategoryCount(id);
            }
            
            boolean success = dao.toggleCategoryParentStatus(id, newStatus);
            
            if (success) {
                String message = newStatus ? "Đã kích hoạt danh mục" : "Đã vô hiệu hóa danh mục";
                if (affectedChildren > 0) {
                    message += " và " + affectedChildren + " danh mục con";
                }
                
                response.getWriter().write(String.format(
                    "{\"success\": true, \"newStatus\": \"%s\", \"statusClass\": \"%s\", \"message\": \"%s\", \"buttonClass\": \"%s\", \"buttonText\": \"%s\"}",
                    newStatus ? "Hoạt động" : "Không hoạt động",
                    newStatus ? "badge-success" : "badge-secondary", 
                    message,
                    newStatus ? "btn-danger" : "btn-success",
                    newStatus ? "❌" : "✅"
                ));
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Không thể cập nhật trạng thái\"}");
            }
            
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"success\": false, \"message\": \"ID không hợp lệ\"}");
        }
    }

    private void showCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int pageSize = 10;

        // Get filter parameters
        String searchKeyword = request.getParameter("search");
        if (searchKeyword != null) {
            searchKeyword = searchKeyword.trim();
            if (searchKeyword.isEmpty()) {
                searchKeyword = null;
            }
        }

        String status = request.getParameter("status");
        String childCountFilter = request.getParameter("childCountFilter");
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");

        if (sortField == null) {
            sortField = "id";
        }
        if (sortDir == null) {
            sortDir = "asc";
        }

        // Use filtered method from DAO
        List<CategoryProductParent> categories = dao.getFilteredParentCategories(
                searchKeyword, status, childCountFilter, sortField, sortDir, page, pageSize);

        int totalCategories = dao.countFilteredParentCategories(searchKeyword, status, childCountFilter);
        int totalPages = (int) Math.ceil((double) totalCategories / pageSize);
        
        int startRecord = 0;
        int endRecord = 0;

        if (totalCategories > 0 && !categories.isEmpty()) {
            startRecord = (page - 1) * pageSize + 1;
            endRecord = Math.min(page * pageSize, totalCategories);

            if (page > totalPages) {
                page = totalPages;
                startRecord = (page - 1) * pageSize + 1;
                endRecord = totalCategories;
            }
        }
        
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);
        String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("reverseSortDir", reverseSortDir);
        request.setAttribute("categoryParents", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        request.setAttribute("totalCategoryParents", totalCategories);
        
        // Set filter parameters for JSP
        request.setAttribute("status", status);
        request.setAttribute("childCountFilter", childCountFilter);

        showMessage(request);

        request.getRequestDispatcher("/category_parent/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/category_parent/create.jsp").forward(request, response);
    }

    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String activeFlagStr = request.getParameter("activeFlag");

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên danh mục không được để trống");
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            request.setAttribute("activeFlag", activeFlagStr);
            showCreateForm(request, response);
            return;
        }

        name = name.trim();
        if (name.length() > 255) {
            request.setAttribute("error", "Tên danh mục không được vượt quá 255 ký tự");
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            request.setAttribute("activeFlag", activeFlagStr);
            showCreateForm(request, response);
            return;
        }

        CategoryProductParent category = new CategoryProductParent();
        category.setName(name);
        category.setDescription(description != null ? description.trim() : "");
        category.setActiveFlag("1".equals(activeFlagStr));

        boolean success = dao.addCategoryParent(category);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?message=create_success");
        } else {
            request.setAttribute("error", "Không thể thêm danh mục. Tên có thể đã tồn tại");
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            request.setAttribute("activeFlag", activeFlagStr);
            showCreateForm(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            CategoryProductParent category = dao.getCategoryParentById(id);
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }

            int childCount = dao.getChildCategoryCount(id);

            request.setAttribute("categoryParent", category);
            request.setAttribute("childCount", childCount);

            request.getRequestDispatcher("/category_parent/edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String activeFlagStr = request.getParameter("activeFlag");

        if (idStr == null || name == null || name.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_data");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            name = name.trim();
            boolean newActiveFlag = "1".equals(activeFlagStr);

            CategoryProductParent currentCategory = dao.getCategoryParentById(id);
            if (currentCategory == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }

            CategoryProductParent category = new CategoryProductParent();
            category.setId(id);
            category.setName(name);
            category.setDescription(description != null ? description.trim() : "");
            category.setActiveFlag(newActiveFlag);

            boolean success = false;
            boolean statusChanged = currentCategory.isActiveFlag() != newActiveFlag;
            
            if (statusChanged) {
                success = dao.toggleCategoryParentStatus(id, newActiveFlag);
                
                if (success) {
                    success = dao.updateCategoryParentBasicInfo(category);
                    
                    if (success) {
                        if (!newActiveFlag) {
                            int affectedChildren = dao.getActiveChildCategoryCount(id);
                            if (affectedChildren > 0) {
                                response.sendRedirect(request.getContextPath() + 
                                    "/category-parent/list?message=update_success_cascade&affected=" + affectedChildren);
                                return;
                            }
                        }
                    }
                }
            } else {
                success = dao.updateCategoryParent(category);
            }

            if (success) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?message=update_success");
            } else {
                request.setAttribute("error", "Không thể cập nhật. Tên có thể đã tồn tại");
                request.setAttribute("id", idStr);
                showEditForm(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    private void showViewPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            CategoryProductParent category = dao.getCategoryParentById(id);
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }

            int childCount = dao.getChildCategoryCount(id);

            request.setAttribute("categoryParent", category);
            request.setAttribute("childCount", childCount);

            request.getRequestDispatcher("/category_parent/view.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            boolean success = dao.deleteCategoryParent(id);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?message=delete_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=delete_failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    private void showMessage(HttpServletRequest request) {
        String message = request.getParameter("message");
        String error = request.getParameter("error");

        if (message != null) {
            switch (message) {
                case "create_success":
                    request.setAttribute("successMessage", "Thêm danh mục thành công!");
                    break;
                case "update_success":
                    request.setAttribute("successMessage", "Cập nhật danh mục thành công!");
                    break;
                case "update_success_cascade":
                    String affected = request.getParameter("affected");
                    if (affected != null) {
                        request.setAttribute("successMessage", 
                            "Cập nhật danh mục thành công! " + affected + " danh mục con cũng đã bị vô hiệu hóa.");
                    } else {
                        request.setAttribute("successMessage", "Cập nhật danh mục thành công!");
                    }
                    break;
                case "delete_success":
                    request.setAttribute("successMessage", "Xóa danh mục thành công!");
                    break;
                case "toggle_success":
                    request.setAttribute("successMessage", "Cập nhật trạng thái thành công!");
                    break;
            }
        }

        if (error != null) {
            switch (error) {
                case "invalid_id":
                    request.setAttribute("errorMessage", "ID không hợp lệ");
                    break;
                case "not_found":
                    request.setAttribute("errorMessage", "Không tìm thấy danh mục");
                    break;
                case "invalid_data":
                    request.setAttribute("errorMessage", "Dữ liệu không hợp lệ");
                    break;
                case "delete_failed":
                    request.setAttribute("errorMessage", "Không thể xóa danh mục cha");
                    break;
                default:
                    request.setAttribute("errorMessage", "Đã xảy ra lỗi");
            }
        }
    }
}