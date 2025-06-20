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

import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Users;

/**
 *
 * @author Admin
 */
public class PurchaseOrderFormServlet extends HttpServlet {

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
            out.println("<title>Servlet LoadingRequestServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoadingRequestServlet at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // Nhận thông tin cơ bản từ form trước đó
        String requestId = request.getParameter("requestId");
        String reason = request.getParameter("reason");
        // Nhận thông tin items
        List<Map<String, String>> items = new ArrayList<>();
        int i = 0;
        while (request.getParameter("items[" + i + "].productName") != null) {
            Map<String, String> item = new HashMap<>();
            item.put("productName", request.getParameter("items[" + i + "].productName"));
            item.put("productCode", request.getParameter("items[" + i + "].productCode"));
            item.put("unit", request.getParameter("items[" + i + "].unit"));
            item.put("quantity", request.getParameter("items[" + i + "].quantity"));
            item.put("note", request.getParameter("items[" + i + "].note"));
            items.add(item);
            i++;
        }

        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");

        UserDAO dao = new UserDAO();
        String fullname = dao.getFullName(currentUser.getId());
        Date DoB = dao.getDoB(currentUser.getId());
        if (currentUser != null) {
            Date dob = DoB;

            if (dob != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(dob);
                int yearOfBirth = cal.get(java.util.Calendar.YEAR);
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                int age = currentYear - yearOfBirth;

                request.setAttribute("age", age);
            } else {
                System.out.println("DOB is null for user: " + currentUser.getId());
            }
        } else {
            System.out.println("currentUser is null in session");
        }

        // Set các attribute để hiển thị trong form
        request.setAttribute("requestId", requestId);
        request.setAttribute("reason", reason);
        request.setAttribute("items", items);
        session.setAttribute("currentUser", fullname);
        session.setAttribute("DoB", DoB);

        // Forward đến JSP
        request.getRequestDispatcher("PurchaseOrderForm.jsp").forward(request, response);
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
