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

    // Initialize DAO when servlet is created
    @Override
    public void init() {
        dao = new CategoryParentDAO();
    }

    // Handle GET requests (display pages)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set UTF-8 encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Get path to determine what user wants to do
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
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Handle POST requests (process forms)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set UTF-8 encoding
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

    // Display category parent list
    private void showCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get current page parameter
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

        // 2. Number of items per page
        int pageSize = 10;

        // 3. Get search keyword
        String searchKeyword = request.getParameter("search");
        if (searchKeyword != null) {
            searchKeyword = searchKeyword.trim();
            if (searchKeyword.isEmpty()) {
                searchKeyword = null;
            }
        }

        // 4. Get sorting information
        String sortField = request.getParameter("sortField");
        String sortDir = request.getParameter("sortDir");

        if (sortField == null) {
            sortField = "id";
        }
        if (sortDir == null) {
            sortDir = "asc";
        }

        // 5. Get data from database
        List<CategoryProductParent> categories = dao.getAllCategoryParents(
                page, pageSize, searchKeyword, sortField, sortDir);

        // 6. Calculate pagination
        int totalCategories = dao.countCategoryParents(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCategories / pageSize);

        // 7. Get child category count for each parent
        for (CategoryProductParent category : categories) {
            int childCount = dao.getChildCategoryCount(category.getId());
            category.setChildCount(childCount);
        }
        int startRecord = 0;
        int endRecord = 0;

        if (totalCategories > 0 && !categories.isEmpty()) {
            startRecord = (page - 1) * pageSize + 1;
            endRecord = Math.min(page * pageSize, totalCategories);

            // Ensure page doesn't exceed totalPages
            if (page > totalPages) {
                page = totalPages;
                startRecord = (page - 1) * pageSize + 1;
                endRecord = totalCategories;
            }
        }
        // Calculate pagination:
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);
        String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("reverseSortDir", reverseSortDir);

        // 8. Send data to JSP
        request.setAttribute("categoryParents", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        request.setAttribute("totalCategoryParents", totalCategories);

        // 9. Display message if any
        showMessage(request);

        // 10. Forward to JSP page
        request.getRequestDispatcher("/category_parent/list.jsp").forward(request, response);
    }

    // Display create form
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/category_parent/create.jsp").forward(request, response);
    }

    // Handle category parent creation
    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get data from form
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String activeFlagStr = request.getParameter("activeFlag");

        // 2. Check that name is not empty
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Category name cannot be empty");
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            request.setAttribute("activeFlag", activeFlagStr);
            showCreateForm(request, response);
            return;
        }

        name = name.trim();

        // 3. Create CategoryProductParent object
        CategoryProductParent category = new CategoryProductParent();
        category.setName(name);
        category.setDescription(description != null ? description.trim() : "");
        category.setActiveFlag("1".equals(activeFlagStr));

        // 4. Save to database
        boolean success = dao.addCategoryParent(category);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?message=create_success");
        } else {
            request.setAttribute("error", "Cannot add category. Name may already exist");
            request.setAttribute("name", name);
            request.setAttribute("description", description);
            request.setAttribute("activeFlag", activeFlagStr);
            showCreateForm(request, response);
        }
    }

    // Display edit form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get ID from parameter
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            // 2. Get category parent information
            CategoryProductParent category = dao.getCategoryParentById(id);
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }

            // 3. Get child category count
            int childCount = dao.getChildCategoryCount(id);

            // 4. Send data to JSP
            request.setAttribute("categoryParent", category);
            request.setAttribute("childCount", childCount);

            request.getRequestDispatcher("/category_parent/edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    // Handle category parent update
    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get data from form
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

            // 2. Create CategoryProductParent object
            CategoryProductParent category = new CategoryProductParent();
            category.setId(id);
            category.setName(name);
            category.setDescription(description != null ? description.trim() : "");
            category.setActiveFlag("1".equals(activeFlagStr));

            // 3. Update database
            boolean success = dao.updateCategoryParent(category);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?message=update_success");
            } else {
                request.setAttribute("error", "Cannot update. Name may already exist");
                request.setAttribute("id", idStr);
                showEditForm(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    // Display view details page
    private void showViewPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get ID from parameter
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            // 2. Get category parent information
            CategoryProductParent category = dao.getCategoryParentById(id);
            if (category == null) {
                response.sendRedirect(request.getContextPath() + "/category-parent/list?error=not_found");
                return;
            }

            // 3. Get child category count
            int childCount = dao.getChildCategoryCount(id);

            // 4. Send data to JSP
            request.setAttribute("categoryParent", category);
            request.setAttribute("childCount", childCount);

            request.getRequestDispatcher("/category_parent/view.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
        }
    }

    // Handle category parent deletion
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get ID from parameter
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect(request.getContextPath() + "/category-parent/list?error=invalid_id");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            // 2. Delete category parent
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

    // Method to display messages
    private void showMessage(HttpServletRequest request) {
        String message = request.getParameter("message");
        String error = request.getParameter("error");

        if (message != null) {
            switch (message) {
                case "create_success":
                    request.setAttribute("successMessage", " category added successfully!");
                    break;
                case "update_success":
                    request.setAttribute("successMessage", " category updated successfully!");
                    break;
                case "delete_success":
                    request.setAttribute("successMessage", " category deleted successfully!");
                    break;
            }
        }

        if (error != null) {
            switch (error) {
                case "invalid_id":
                    request.setAttribute("errorMessage", "Invalid ID");
                    break;
                case "not_found":
                    request.setAttribute("errorMessage", "category not found");
                    break;
                case "invalid_data":
                    request.setAttribute("errorMessage", "Invalid data");
                    break;
                case "delete_failed":
                    request.setAttribute("errorMessage", "Cannot delete parent category");
                    break;
                default:
                    request.setAttribute("errorMessage", "An error occurred");
            }
        }
    }
}
