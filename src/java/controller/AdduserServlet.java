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
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;
import model.Department;
import model.Users;

/**
 *
 * @author phucn
 */
public class AdduserServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdduserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdduserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DepartmentDAO deptDAO = new DepartmentDAO();
        List<Department> departments = deptDAO.getAllDepartments();
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("AddUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DepartmentDAO deptDAO = new DepartmentDAO();
        List<Department> departments = deptDAO.getAllDepartments();
        request.setAttribute("departments", departments);

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        int activeFlag = Integer.parseInt(request.getParameter("activeFlag"));
        int roleId = Integer.parseInt(request.getParameter("role"));
        String departmentIdStr = request.getParameter("departmentId");
        Integer departmentId = null;
        if (departmentIdStr != null && !departmentIdStr.isEmpty()) {
            try {
                departmentId = Integer.parseInt(departmentIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Phòng ban không hợp lệ!");
                request.getRequestDispatcher("AddUser.jsp").forward(request, response);
                return;
            }
        }

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setDepartmentId(departmentId);
        user.setActiveFlag(activeFlag);

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.addUser(user, roleId);
            HttpSession session = request.getSession();
            session.setAttribute("message", "Thêm người dùng thành công!");
            response.sendRedirect("adduser");
        } catch (Exception e) {
            request.setAttribute("error", "Không thể thêm người dùng");
            request.getRequestDispatcher("AddUser.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}