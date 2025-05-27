/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RequestInformationDAO;
import dao.RequestItemsDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class LoadingRequestServlet extends HttpServlet {

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
        RequestInformationDAO dao = new RequestInformationDAO();
        String nextID = dao.getNextRequestId();
        request.setAttribute("nextID", nextID);
        request.getRequestDispatcher("ItemsSupplyRequestForm.jsp").forward(request, response);
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
        //Lay du lieu tu form
        String dayRequestStr = request.getParameter("day_request");
        String reason = request.getParameter("reason");
        String supplier = request.getParameter("supplier");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        Date dayRequest = null;
        try {
            java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dayRequestStr);
            dayRequest = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
        }

        // Các trường thông tin chi tiết mặt hàng (nhiều dòng → dùng getParameterValues)
        String[] sttArr = request.getParameterValues("stt");
        String[] productNameArr = request.getParameterValues("product_name");
        String[] productCodeArr = request.getParameterValues("product_code");
        String[] unitArr = request.getParameterValues("unit");
        String[] quantityArr = request.getParameterValues("quantity");
        String[] noteArr = request.getParameterValues("note");
        String reasonDetail = request.getParameter("reason_detail");
        int[] quantityIntArr = new int[quantityArr.length];
        for (int i = 0; i < quantityArr.length; i++) {
            try {
                quantityIntArr[i] = Integer.parseInt(quantityArr[i]);
            } catch (NumberFormatException e) {
            }
        }
        
        RequestItemsDAO requestitemsDAO = new RequestItemsDAO();
        RequestInformationDAO requestInformationDAO = new RequestInformationDAO();

        String request_id = requestInformationDAO.addRequestInformationIntoDB(12, dayRequest, "pending", reason, supplier, address, phone, email);
        requestitemsDAO.addItemsIntoDB(request_id, productNameArr, productCodeArr, unitArr, quantityIntArr, noteArr, reasonDetail);
        response.sendRedirect("RequestSuccessNotification.jsp");
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
