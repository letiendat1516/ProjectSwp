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
import java.util.List;
import model.Users;

/**
 *
 * @author phucn
 */
public class UserFilterServlet extends HttpServlet {
   

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserFilterServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserFilterServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String roleStr = request.getParameter("role");
        String status = request.getParameter("status");
        String keyword = request.getParameter("keyword");

        Integer roleId = null;
        if (roleStr != null && !roleStr.isEmpty()) {
            try {
                roleId = Integer.parseInt(roleStr);
            } catch (NumberFormatException e) {
                roleId = null;
            }
        }

        UserDAO userDAO = new UserDAO();
        List<Users> filteredUsers = userDAO.filterUsers(roleId, status, keyword);

        request.setAttribute("userList", filteredUsers);
        request.getRequestDispatcher("UserManager.jsp").forward(request, response);
    }

}
