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
public class AdminServlet extends HttpServlet {

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
            out.println("<title>Servlet AdminServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private static final int PAGE_SIZE = 10; // số bản ghi mỗi trang

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    UserDAO userDAO = new UserDAO();

    String pageParam = request.getParameter("page");
    int pageIndex = 1;
    int pageSize = 10;

    if (pageParam != null) {
        try {
            pageIndex = Integer.parseInt(pageParam);
            if (pageIndex < 1) pageIndex = 1;
        } catch (NumberFormatException e) {
            pageIndex = 1;
        }
    }

    List<Users> userList = userDAO.getUsersByPage(pageIndex, pageSize);
    int totalUsers = userDAO.getTotalUserCount();
    int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

    request.setAttribute("userList", userList);
    request.setAttribute("totalPages", totalPages);
    request.setAttribute("currentPage", pageIndex);

    request.getRequestDispatcher("JSP/Admin/admin.jsp").forward(request, response);
}


    // Bạn có thể bổ sung doPost nếu cần xử lý POST request
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
