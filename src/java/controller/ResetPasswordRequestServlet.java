/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author phucn
 */
public class ResetPasswordRequestServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet ResetPasswordRequestServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResetPasswordRequestServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ForgotPasswordDAO dao = new ForgotPasswordDAO();
        List<ForgotPasswordRequest> requests = dao.getAllRequests();
        req.setAttribute("requestList", requests);
        req.getRequestDispatcher("AdminResetPasswordRequest.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int requestId = Integer.parseInt(req.getParameter("requestId"));

        ForgotPasswordDAO dao = new ForgotPasswordDAO();
        if ("approve".equals(action)) {
            // 1. Reset mật khẩu cho user (code mẫu, tuỳ logic)
            // 2. Gửi email nếu muốn (bổ sung sau)
            dao.updateStatus(requestId, "Đã xử lý"); // Đổi trạng thái sang đã xử lý
        } else if ("deny".equals(action)) {
            dao.updateStatus(requestId, "Từ chối");
        }
        resp.sendRedirect("resetPasswordRequests"); // refresh lại trang admin
    }

}
