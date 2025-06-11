// ExportRequestServlet.java
package controller;

import dao.ExportRequestDAO;
import dao.ExportRequestItemsDAO;
import dao.ProductInfoDAO;
import dao.UserDAO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ProductInfo;
import model.Users;

public class ExportRequestController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");

        UserDAO dao = new UserDAO();
        String fullname = dao.getFullName(currentUser.getId());
        Date DoB = dao.getDoB(currentUser.getId());
        ExportRequestDAO exportRequestDAO = new ExportRequestDAO();
        ProductInfoDAO product = new ProductInfoDAO();
        String nextExportID = exportRequestDAO.getNextExportRequestId();
        
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
        
        List<ProductInfo> products_list = product.getAllProducts();
        request.setAttribute("nextExportID", nextExportID);
        request.setAttribute("products_list", products_list);
        session.setAttribute("currentUser", fullname);
        session.setAttribute("DoB", DoB);
        request.getRequestDispatcher("ExportRequest.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Lấy dữ liệu từ form
        String role = request.getParameter("role");
        String dayRequestStr = request.getParameter("day_request");
        String reason = request.getParameter("reason");
        String department = request.getParameter("department");
        String recipientName = request.getParameter("recipient_name");
        String recipientPhone = request.getParameter("recipient_phone");
        String recipientEmail = request.getParameter("recipient_email");
        
        Date dayRequest = null;
        try {
            java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dayRequestStr);
            dayRequest = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
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
                quantityIntArr[i] = 0;
            }
        }
        
        HttpSession session = request.getSession(false);
        Users currentUser = (Users) session.getAttribute("user");
        int user_id = currentUser.getId();
        
        ExportRequestItemsDAO exportRequestItemsDAO = new ExportRequestItemsDAO();
        ExportRequestDAO exportRequestDAO = new ExportRequestDAO();

        String export_request_id = exportRequestDAO.addExportRequestIntoDB(user_id, role, dayRequest, "pending", reason, department, recipientName, recipientPhone, recipientEmail);
        exportRequestItemsDAO.addExportItemsIntoDB(export_request_id, productNameArr, productCodeArr, unitArr, quantityIntArr, noteArr, reasonDetail);
        
        response.sendRedirect("ExportRequestSuccessNotification.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Export Request Servlet";
    }
}
