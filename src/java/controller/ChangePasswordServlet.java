/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ForgotPasswordRequest;
import model.Users;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author phucn
 */
//@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private UserDAO userDao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users currentUser = (Users) (session != null ? session.getAttribute("user") : null);

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        ForgotPasswordRequest req = userDao.getLatestRequestByUserId(currentUser.getId());

        request.setAttribute("resetRequest", req);
        request.setAttribute("targetUser", currentUser);
        request.getRequestDispatcher("change_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users currentUser = (Users) (session != null ? session.getAttribute("user") : null);

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        String msg = null;

        ForgotPasswordRequest req = null;

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            req = userDao.getLatestRequestByUserId(currentUser.getId());

            if (req == null || !"approved".equalsIgnoreCase(req.getStatus())) {
                msg = "Bạn không có yêu cầu đổi mật khẩu hợp lệ!";
            } else if (newPassword == null || !newPassword.equals(confirmPassword)) {
                msg = "Mật khẩu xác nhận không khớp!";
            } else if (newPassword.length() < 6) {
                msg = "Mật khẩu phải từ 6 ký tự trở lên!";
            } else {
                String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
                boolean updated = userDao.updateUserPassword(currentUser.getId(), hashedPassword);
                boolean marked = userDao.markRequestUsed(req.getId());

                if (updated && marked) {
                    msg = "Đổi mật khẩu thành công!";
                    request.setAttribute("success", true);
                } else {
                    msg = "Không thể cập nhật mật khẩu.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "Đã xảy ra lỗi: " + e.getMessage();
        }

        request.setAttribute("msg", msg);
        request.setAttribute("resetRequest", req);
        request.setAttribute("targetUser", currentUser);
        request.getRequestDispatcher("change_password.jsp").forward(request, response);
    }
}
