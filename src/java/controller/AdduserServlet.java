/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DepartmentDAO;
import dao.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
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

        UserDAO userDAO = new UserDAO();
        List<String> errors = new ArrayList<>();

        // Kiểm tra
        try {
            if (userDAO.isUsernameExists(username)) {
                errors.add("Username '" + username + "' đã tồn tại!");
            }

            if (userDAO.isEmailExists(email)) {
                errors.add("Email '" + email + "' đã được sử dụng bởi người dùng khác!");
            }

            if (userDAO.isPhoneExists(phone)) {
                errors.add("Số điện thoại '" + phone + "' đã được sử dụng bởi người dùng khác!");
            }

            // Nếu có lỗi, trả về form với thông báo chi tiết
            if (!errors.isEmpty()) {
                String errorMessage = String.join("<br>", errors);
                request.setAttribute("error", errorMessage);

                request.setAttribute("username", username);
                request.setAttribute("fullname", fullname);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.setAttribute("departmentIdSelected", departmentIdStr);
                request.setAttribute("roleSelected", String.valueOf(roleId));
                request.setAttribute("activeFlagSelected", String.valueOf(activeFlag));

                request.getRequestDispatcher("AddUser.jsp").forward(request, response);
                return;
            }

            // Nếu không có lỗi, thêm user
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            Users user = new Users();
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDepartmentId(departmentId);
            user.setActiveFlag(activeFlag);

            userDAO.addUser(user, roleId);
            HttpSession session = request.getSession();
            session.setAttribute("message", "Thêm người dùng '" + fullname + "' thành công!");
            response.sendRedirect("usermanager");

        } catch (SQLException e) {
            System.err.println("Database error in AddUser: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("AddUser.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Unexpected error in AddUser: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi thêm người dùng: " + e.getMessage());
            request.getRequestDispatcher("AddUser.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}