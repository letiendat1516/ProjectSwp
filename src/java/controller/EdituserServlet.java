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

            request.setAttribute("editUser", user);
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("admin");
        }
    }


@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        int activeFlag = Integer.parseInt(request.getParameter("activeFlag"));
        int roleId = Integer.parseInt(request.getParameter("role"));

        Users user = new Users();
        user.setId(id);
        user.setUsername(username);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);

        // Xử lý ngày sinh
        java.sql.Date dob = null;
        try {
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                dob = java.sql.Date.valueOf(dobStr);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Date of Birth is invalid!");
            
            UserDAO userDAO = new UserDAO();
            user = userDAO.getUserById(id);
            request.setAttribute("editUser", user);
            request.getRequestDispatcher("EditUser.jsp").forward(request, response);
            return;
        }
        user.setDob(dob);

        user.setActiveFlag(activeFlag);

        UserDAO userDAO = new UserDAO();
        userDAO.updateUser(user, roleId);

        HttpSession session = request.getSession();
        session.setAttribute("message", "User updated successfully!");
        response.sendRedirect("admin");
    } catch (Exception e) {
        request.setAttribute("error", "Error updating user: " + e.getMessage());
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            UserDAO userDAO = new UserDAO();
            Users user = userDAO.getUserById(id);
            request.setAttribute("editUser", user);
        } catch (Exception ex) {
        }
        request.getRequestDispatcher("EditUser.jsp").forward(request, response);
    }
}


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}