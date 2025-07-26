/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DepartmentDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Department;
import model.Users;

/**
 *
 * @author phucn
 */
public class UserManager extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserManager</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserManager at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login.jsp");
            return;
        }

        String roleParam = request.getParameter("role");
        String status = request.getParameter("status");
        String fullname = request.getParameter("keyword");
        String departmentIdStr = request.getParameter("departmentId");
        String isSearch = request.getParameter("search");
        boolean isFiltering = "1".equals(isSearch);

        boolean includeAdmin = false;
        Integer roleId = null;
        if (roleParam != null) {
            if ("all".equalsIgnoreCase(roleParam)) {
                includeAdmin = false;
            } else {
                try {
                    roleId = Integer.parseInt(roleParam);
                } catch (NumberFormatException e) {
                    roleId = null;
                }
            }
        }

        // Xử lý department parameter
        Integer departmentId = null;
        if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
            try {
                departmentId = Integer.parseInt(departmentIdStr);
            } catch (NumberFormatException e) {
                departmentId = null;
            }
        }

        DepartmentDAO deptDAO = new DepartmentDAO();
        List<Department> departments = deptDAO.getAllDepartments();
        request.setAttribute("departments", departments);

        UserDAO userDAO = new UserDAO();
        List<Users> userList;
        int totalPages = 1;
        int totalUsers = 0;

        int pageIndex = 1;
        int pageSize = 10;

        if (!isFiltering) {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.equals("usermanager")) {
                try {
                    pageIndex = Integer.parseInt(pageParam);
                    if (pageIndex < 1) {
                        pageIndex = 1;
                    }
                } catch (NumberFormatException e) {
                    pageIndex = 1;
                }
            }
        }

        //filter/search
        if (isFiltering) {
            userList = userDAO.filterUsers(roleId, status, fullname, departmentId, includeAdmin);
            totalUsers = userList.size();
            totalPages = 1;
            request.setAttribute("currentPage", 1);
        } else {
            totalUsers = userDAO.countFilteredUsers(roleId, status, fullname, departmentId, includeAdmin);
            totalPages = (int) Math.ceil((double) totalUsers / pageSize);

            if (pageIndex > totalPages && totalPages > 0) {
                pageIndex = totalPages;
            }
            if (pageIndex < 1) {
                pageIndex = 1;
            }

            userList = userDAO.filterUsersWithPaging(roleId, status, fullname, departmentId, includeAdmin, pageIndex, pageSize);
            request.setAttribute("currentPage", pageIndex);
        }

        request.setAttribute("userList", userList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalUsers", totalUsers);

        request.setAttribute("roleSelected", roleParam);
        request.setAttribute("statusSelected", status);
        request.setAttribute("keyword", fullname);
        request.setAttribute("departmentIdSelected", departmentIdStr);

        request.getRequestDispatcher("UserManager.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
