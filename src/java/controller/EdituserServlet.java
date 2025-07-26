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
import java.util.List;
import model.Department;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author phucn
 */
public class EdituserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EdituserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EdituserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("id");
        int userId = 0;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (Exception e) {
            response.sendRedirect("usermanager");
            return;
        }
        UserDAO userDAO = new UserDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();
        Users user = userDAO.getUserById(userId);
        List<Department> departments = deptDAO.getAllDepartments();

        request.setAttribute("editUser", user);
        request.setAttribute("departments", departments);
        request.getRequestDispatcher("EditUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int activeFlag = Integer.parseInt(request.getParameter("activeFlag"));
            int roleId = Integer.parseInt(request.getParameter("role"));
            String departmentIdStr = request.getParameter("departmentId");
            Integer departmentId = (departmentIdStr != null && !departmentIdStr.isEmpty())
                    ? Integer.parseInt(departmentIdStr) : null;

            UserDAO userDAO = new UserDAO();

            //(trừ chính user đang edit)
            // Kiểm tra username
            if (userDAO.isUsernameExistsForEdit(username, id)) {
                returnEditWithError(request, response, id, "Tên đăng nhập đã tồn tại!");
                return;
            }

            // Kiểm tra email
            if (userDAO.isEmailExistsForEdit(email, id)) {
                returnEditWithError(request, response, id, "Email đã tồn tại!");
                return;
            }

            // Kiểm tra số điện thoại
            if (userDAO.isPhoneExistsForEdit(phone, id)) {
                returnEditWithError(request, response, id, "Số điện thoại đã tồn tại!");
                return;
            }

            // Xử lý mật khẩu: nếu không đổi thì lấy lại mật khẩu cũ
            String hashedPassword;
            if (password != null && !password.trim().isEmpty()) {
                hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            } else {
                hashedPassword = userDAO.getUserById(id).getPassword();
            }


            Users user = new Users();
            user.setId(id);
            user.setUsername(username);
            user.setPassword(hashedPassword);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDepartmentId(departmentId);
            user.setActiveFlag(activeFlag);
            userDAO.updateUser(user, roleId);


            HttpSession session = request.getSession();
            session.setAttribute("message", "Cập nhật người dùng thành công!");
            response.sendRedirect("usermanager");

        } catch (SQLException e) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                // Xử lý lỗi SQL cụ thể
                String errorMessage = "Lỗi cơ sở dữ liệu: " + e.getMessage();
                if (e.getMessage().contains("username")) {
                    errorMessage = "Tên đăng nhập đã tồn tại!";
                } else if (e.getMessage().contains("email")) {
                    errorMessage = "Email đã tồn tại!";
                } else if (e.getMessage().contains("phone")) {
                    errorMessage = "Số điện thoại đã tồn tại!";
                }
                returnEditWithError(request, response, id, errorMessage);
            } catch (Exception ex) {
                response.sendRedirect("usermanager");
            }
        } catch (Exception e) {
            // Gặp lỗi thì lấy lại dữ liệu, forward về form và báo lỗi
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                returnEditWithError(request, response, id, "Không thể cập nhật người dùng: " + e.getMessage());
            } catch (Exception ex) {
                response.sendRedirect("usermanager");
            }
        }
    }

// Hàm forward về EditUser.jsp với dữ liệu và lỗi
    private void returnEditWithError(HttpServletRequest request, HttpServletResponse response, int userId, String errorMsg)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        DepartmentDAO deptDAO = new DepartmentDAO();
        Users user = userDAO.getUserById(userId);
        List<Department> departments = deptDAO.getAllDepartments();

        // Giữ lại dữ liệu người dùng đã nhập
        user.setUsername(request.getParameter("username"));
        user.setFullname(request.getParameter("fullname"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));

        request.setAttribute("editUser", user);
        request.setAttribute("departments", departments);
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("EditUser.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}