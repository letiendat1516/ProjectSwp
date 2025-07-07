/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author phucn
 */
public class LoginServlet extends HttpServlet {

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
            out.println("<title>Servlet loginservlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet loginservlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        Users user = userDAO.findByUsername(username);
        if (user != null) {
            String pwFromDB = user.getPassword();
            // Nếu đã là hash BCrypt
            if (pwFromDB != null && pwFromDB.startsWith("$2a$")) {
                if (BCrypt.checkpw(password, pwFromDB)) {
                    // Đăng nhập thành công
                } else {
                    // Sai mật khẩu
                }
            } else {
                // So sánh trực tiếp mật khẩu plaintext
                if (password.equals(pwFromDB)) {
                    // Đăng nhập thành công và nâng cấp mật khẩu
                    String newHash = BCrypt.hashpw(password, BCrypt.gensalt());
                    userDAO.updatePasswordHash(user.getId(), newHash);
                } else {
                    // Sai mật khẩu
                }
            }
        }

        if (user != null) {
            String hashedPasswordFromDB = user.getPassword(); // password đã mã hóa trong DB

            // So sánh password người dùng nhập với hash đã lưu
            if (org.mindrot.jbcrypt.BCrypt.checkpw(password, hashedPasswordFromDB)) {

                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                int userId = user.getId();
                List<String> userPermissions = userDAO.getUserPermissions(userId);
                session.setAttribute("userPermissions", userPermissions);

                switch (user.getRoleName()) {
                    case "Admin":
                        response.sendRedirect("Admin.jsp");
                        break;
                    case "Nhân viên kho":
                        response.sendRedirect("categoriesforward.jsp");
                        break;
                    case "Nhân viên công ty":
                        response.sendRedirect("RequestForward.jsp");
                        break;
                    case "Giám đốc":
                        response.sendRedirect("ApproveListForward.jsp");
                        break;
                    default:
                        response.sendRedirect("homepage.jsp");
                }
                return; // Đăng nhập thành công!
            }
        }
        // Nếu không đúng, trả lại trang login + báo lỗi
        request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
