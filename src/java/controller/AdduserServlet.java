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
public class AdduserServlet extends HttpServlet {

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
            out.println("<title>Servlet AdduserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdduserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        int activeFlag = Integer.parseInt(request.getParameter("activeFlag"));
        int roleId = Integer.parseInt(request.getParameter("role"));

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhone(phone);

        // Xử lý ngày sinh
        java.sql.Date dob = null;
        try {
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                dob = java.sql.Date.valueOf(dobStr); // Format: yyyy-MM-dd
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Date of Birth is invalid!");
            request.getRequestDispatcher("AddUser.jsp").forward(request, response);
            return;
        }
        user.setDob(dob); // Đặt giá trị ngày sinh
        if (dob != null) {
        java.time.LocalDate birthDate = dob.toLocalDate();
        java.time.LocalDate today = java.time.LocalDate.now();
        int age = java.time.Period.between(birthDate, today).getYears();

        if (age < 18 || age > 60) {
            request.setAttribute("error", "Tuổi người dùng phải từ 18 đến 60!");
            request.getRequestDispatcher("AddUser.jsp").forward(request, response);
            return;
        }
    }
        user.setActiveFlag(activeFlag);

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.addUser(user, roleId);

            HttpSession session = request.getSession();
            session.setAttribute("message", "User added successfully!");
            response.sendRedirect("AddUser.jsp");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to add user: " + e.getMessage());
            request.getRequestDispatcher("AddUser.jsp").forward(request, response);
        }
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
