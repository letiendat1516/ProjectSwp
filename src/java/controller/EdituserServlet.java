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
import model.Users;

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
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("admin");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            UserDAO userDAO = new UserDAO();
            Users user = userDAO.getUserById(id);

            if (user == null) {
                response.sendRedirect("admin");
                return;
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("admin");
        }
    }

    // POST: Nhận dữ liệu form, cập nhật user, redirect về danh sách
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String fullname = request.getParameter("fullname");
            String email = request.getParameter("email");
            int activeFlag = Integer.parseInt(request.getParameter("activeFlag"));
            int roleId = Integer.parseInt(request.getParameter("role"));

            Users user = new Users();
            user.setId(id);
            user.setUsername(username);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setActiveFlag(activeFlag);

            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(user, roleId);

            response.sendRedirect("admin");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Update user failed: " + e.getMessage());
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);
        }
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
