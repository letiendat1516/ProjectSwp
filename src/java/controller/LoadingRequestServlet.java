/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.MaterialUnitDAO;
import dao.ProductInfoDAO;
import dao.RequestInformationDAO;
import dao.RequestItemsDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.MaterialUnit;
import model.ProductInfo;
import model.Users;

/**
 * Servlet xử lý yêu cầu mua hàng: hiển thị form và xử lý submit
 * @author Admin
 */
public class LoadingRequestServlet extends HttpServlet {

    /**
     * Xử lý request chung (không sử dụng)
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
     * GET: Hiển thị form tạo yêu cầu mua hàng với dữ liệu cần thiết
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy thông tin user từ session
        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");

        // Khởi tạo các DAO
        UserDAO dao = new UserDAO();
        String fullname = dao.getFullName(currentUser.getId());
        Date DoB = dao.getDoB(currentUser.getId());
        RequestInformationDAO requestInfo = new RequestInformationDAO();
        ProductInfoDAO product = new ProductInfoDAO();
        
        // Lấy ID yêu cầu tiếp theo
        String nextID = requestInfo.getNextRequestId();
        
        if (currentUser != null) {
            Date dob = DoB;

            // Tính tuổi từ ngày sinh
            if (dob != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(dob);
                int yearOfBirth = cal.get(java.util.Calendar.YEAR);
                int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                int age = currentYear - yearOfBirth;

                request.setAttribute("age", age);
            }
        }
        
        // Lấy danh sách sản phẩm và đơn vị
        List<ProductInfo> products_list = product.getAllProducts();
        MaterialUnitDAO unitDAO = new MaterialUnitDAO();
        List<MaterialUnit> unit_list = unitDAO.getAllMaterialUnits();
        
        // Set attributes để hiển thị trên form
        request.setAttribute("nextID", nextID);
        request.setAttribute("products_list", products_list);
        request.setAttribute("unit_list", unit_list);
        session.setAttribute("currentUser", fullname);
        session.setAttribute("DoB", DoB);
        
        // Chuyển đến trang form
        request.getRequestDispatcher("ItemsSupplyRequestForm.jsp").forward(request, response);
    }

    /**
     * POST: Xử lý submit form tạo yêu cầu mua hàng
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Lấy thông tin cơ bản từ form
        String role = request.getParameter("role");
        String dayRequestStr = request.getParameter("day_request");
        String reason = request.getParameter("reason");
        String supplier = request.getParameter("supplier");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        
        // Chuyển đổi string thành Date
        Date dayRequest = null;
        try {
            java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dayRequestStr);
            dayRequest = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
        }

        // Lấy thông tin chi tiết các mặt hàng (mảng từ multiple rows)
        String[] productNameArr = request.getParameterValues("product_name");
        String[] productCodeArr = request.getParameterValues("product_code");
        String[] unitArr = request.getParameterValues("unit");
        String[] quantityArr = request.getParameterValues("quantity");
        String[] noteArr = request.getParameterValues("note");
        String reasonDetail = request.getParameter("reason_detail");
        
        // Chuyển đổi quantity từ string sang int
        int[] quantityIntArr = new int[quantityArr.length];
        for (int i = 0; i < quantityArr.length; i++) {
            try {
                quantityIntArr[i] = Integer.parseInt(quantityArr[i]);
            } catch (NumberFormatException e) {
            }
        }
        
        // Lấy user ID từ session
        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");
        int user_id = currentUser.getId();
        
        // Khởi tạo DAO để lưu vào database
        RequestItemsDAO requestitemsDAO = new RequestItemsDAO();
        RequestInformationDAO requestInformationDAO = new RequestInformationDAO();

        // Lưu thông tin yêu cầu chính và lấy request_id
        String request_id = requestInformationDAO.addRequestInformationIntoDB(user_id, role, dayRequest, "pending", reason, supplier, address, phone, email);
        
        // Lưu chi tiết các items của yêu cầu
        requestitemsDAO.addItemsIntoDB(request_id, productNameArr, productCodeArr, unitArr, quantityIntArr, noteArr, reasonDetail);
        
        // Redirect đến trang thông báo thành công
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