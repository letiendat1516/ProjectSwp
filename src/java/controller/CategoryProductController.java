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
  
  // Constants for sorting
  private static final String DEFAULT_SORT_FIELD = "id";
  private static final String DEFAULT_SORT_DIR = "asc";
  private static final String SORT_ASC = "asc";
  private static final String SORT_DESC = "desc";
  private static final String[] ALLOWED_SORT_FIELDS = {"id", "name", "parent_name", "active_flag"};

  // Initialize DAO when servlet is created
  @Override
  public void init() {
      categoryDAO = new CategoryProductDAO();
  }

  // Handle GET requests (display pages)
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
      
      // Set UTF-8 encoding for Vietnamese display
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      
      // Get path to determine what user wants to do
      String pathInfo = request.getPathInfo();

      // Check path and call corresponding method
      if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
          showCategoryList(request, response);
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
          response.sendRedirect(request.getContextPath() + "/category/list");
      }
  }

  // Validate sortField
  private boolean isValidSortField(String sortField) {
      if (sortField == null) return false;
      for (String field : ALLOWED_SORT_FIELDS) {
          if (field.equalsIgnoreCase(sortField)) {
              return true;
          }
      }
      return false;
  }

  // Display category list
  private void showCategoryList(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      // 1. Get current page parameter
      int page = 1;
      String pageParam = request.getParameter("page");
      if (pageParam != null) {
          try {
              page = Integer.parseInt(pageParam);
              if (page < 1) page = 1;
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

      // 4. Get and validate sorting information
      String sortField = request.getParameter("sortField");
      String sortDir = request.getParameter("sortDir");
      
      // Validate sortField
      if (!isValidSortField(sortField)) {
          sortField = DEFAULT_SORT_FIELD;
      }
      
      // Validate sortDir - only accept "asc" or "desc"
      if (sortDir == null || 
          (!SORT_ASC.equalsIgnoreCase(sortDir) && !SORT_DESC.equalsIgnoreCase(sortDir))) {
          sortDir = DEFAULT_SORT_DIR;
      }

      // 5. Get data from database
      List<CategoryProduct> categories = categoryDAO.getAllCategoriesWithParent(
          page, pageSize, searchKeyword, sortField, sortDir);
      
      // 6. Calculate pagination
      int totalCategories = categoryDAO.countCategories(searchKeyword);
      int totalPages = (int) Math.ceil((double) totalCategories / pageSize);

      // 7. Send data to JSP
      request.setAttribute("categories", categories);
      request.setAttribute("currentPage", page);
      request.setAttribute("totalPages", totalPages);
      request.setAttribute("searchKeyword", searchKeyword);
      request.setAttribute("sortField", sortField);
      request.setAttribute("sortDir", sortDir);
      request.setAttribute("totalCategories", totalCategories);

      // 8. Display message if any
      showMessage(request);

      // 9. Forward to JSP page
      request.getRequestDispatcher("/category_product/list.jsp").forward(request, response);
  }

  // Display create form
  private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      // Get list of parent categories to display in dropdown
      List<CategoryProductParent> parentCategories = categoryDAO.getCategoryParentsForDropdown();
      request.setAttribute("parentCategories", parentCategories);

      request.getRequestDispatcher("/category_product/create.jsp").forward(request, response);
  }

  // Handle category creation
  private void createCategory(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      // 1. Get data from form
      String name = request.getParameter("name");
      String parentIdStr = request.getParameter("parentId");
      String activeFlagStr = request.getParameter("activeFlag");

      // 2. Check that name is not empty
      if (name == null || name.trim().isEmpty()) {
          request.setAttribute("error", "Category name cannot be empty");
          showCreateForm(request, response);
          return;
      }

      name = name.trim();

      // 3. Check if name already exists
      if (categoryDAO.isCategoryNameExists(name)) {
          request.setAttribute("error", "Category name already exists");
          showCreateForm(request, response);
          return;
      }

      // 4. Create CategoryProduct object
      CategoryProduct category = new CategoryProduct();
      category.setName(name);

      // 5. Handle parent ID
      if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("0")) {
          try {
              category.setParentId(Integer.parseInt(parentIdStr));
          } catch (NumberFormatException e) {
              category.setParentId(null);
          }
      } else {
          category.setParentId(null);
      }

      // 6. Handle active status
      category.setActiveFlag("1".equals(activeFlagStr));

      // 7. Save to database
      boolean success = categoryDAO.addCategory(category);

      if (success) {
          response.sendRedirect(request.getContextPath() + "/category/list?message=create_success");
      } else {
          request.setAttribute("error", "Cannot add category");
          showCreateForm(request, response);
      }
  }

  // Display edit form
  private void showEditForm(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      // 1. Get ID from parameter
      String idStr = request.getParameter("id");
      if (idStr == null) {
          response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
          return;
      }

      try {
          int id = Integer.parseInt(idStr);

          // 2. Get category information
          CategoryProduct category = categoryDAO.getCategoryWithParentById(id);
          if (category == null) {
              response.sendRedirect(request.getContextPath() + "/category/list?error=not_found");
              return;
          }

          // 3. Get list of parent categories
          List<CategoryProductParent> parentCategories = categoryDAO.getCategoryParentsForDropdown();
          
          // 4. Send data to JSP
          request.setAttribute("category", category);
          request.setAttribute("parentCategories", parentCategories);

          request.getRequestDispatcher("/category_product/edit.jsp").forward(request, response);

      } catch (NumberFormatException e) {
          response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
      }
  }

  // Handle category update
  private void updateCategory(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      // 1. Get data from form
      String idStr = request.getParameter("id");
      String name = request.getParameter("name");
      String parentIdStr = request.getParameter("parentId");
      String activeFlagStr = request.getParameter("activeFlag");

      if (idStr == null || name == null || name.trim().isEmpty()) {
          response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_data");
          return;
      }

      try {
          int id = Integer.parseInt(idStr);
          name = name.trim();

          // 2. Check if name already exists (excluding current one)
          if (categoryDAO.isCategoryNameExists(name, id)) {
              request.setAttribute("error", "Category name already exists");
              request.setAttribute("id", idStr);
              showEditForm(request, response);
              return;
          }

          // 3. Create CategoryProduct object
          CategoryProduct category = new CategoryProduct();
          category.setId(id);
          category.setName(name);

          // 4. Handle parent ID
          if (parentIdStr != null && !parentIdStr.trim().isEmpty() && !parentIdStr.equals("0")) {
              try {
                  category.setParentId(Integer.parseInt(parentIdStr));
              } catch (NumberFormatException e) {
                  category.setParentId(null);
              }
          } else {
              category.setParentId(null);
          }

          // 5. Handle active status
          category.setActiveFlag("1".equals(activeFlagStr));

          // 6. Update database
          boolean success = categoryDAO.updateCategory(category);

          if (success) {
              response.sendRedirect(request.getContextPath() + "/category/list?message=update_success");
          } else {
              request.setAttribute("error", "Cannot update category");
              request.setAttribute("id", idStr);
              showEditForm(request, response);
          }

      } catch (NumberFormatException e) {
          response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
      }
  }

  // Handle category deletion
  private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

      // 1. Get ID from parameter
      String idStr = request.getParameter("id");
      if (idStr == null) {
          response.sendRedirect(request.getContextPath() + "/category/list?error=invalid_id");
          return;
      }

      try {
          int id = Integer.parseInt(idStr);

          // 2. Check if category exists
          CategoryProduct category = categoryDAO.getCategoryById(id);
          if (category == null) {
              response.sendRedirect(request.getContextPath() + "/category/list?error=not_found");
              return;
          }

          // 3. Delete category
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

  // Method to display messages
  private void showMessage(HttpServletRequest request) {
      String message = request.getParameter("message");
      String error = request.getParameter("error");

      if (message != null) {
          switch (message) {
              case "create_success":
                  request.setAttribute("successMessage", "Category added successfully!");
                  break;
              case "update_success":
                  request.setAttribute("successMessage", "Category updated successfully!");
                  break;
              case "delete_success":
                  request.setAttribute("successMessage", "Category deleted successfully!");
                  break;
          }
      }

      if (error != null) {
          switch (error) {
              case "invalid_id":
                  request.setAttribute("errorMessage", "Invalid ID");
                  break;
              case "not_found":
                  request.setAttribute("errorMessage", "Category not found");
                  break;
              case "delete_failed":
                  request.setAttribute("errorMessage", "Cannot delete category");
                  break;
              case "invalid_data":
                  request.setAttribute("errorMessage", "Invalid data");
                  break;
              default:
                  request.setAttribute("errorMessage", "An error occurred");
          }
      }
  }
}
