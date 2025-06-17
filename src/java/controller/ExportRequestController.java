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
        request.setCharacterEncoding("UTF-8");

        try {
            // Lấy dữ liệu từ form
            String role = request.getParameter("role");
            String dayRequestStr = request.getParameter("day_request");
            String reason = request.getParameter("reason");
            String department = request.getParameter("department");
            String recipientName = request.getParameter("recipient_name");
            String recipientPhone = request.getParameter("recipient_phone");
            String recipientEmail = request.getParameter("recipient_email");
            
            // Debug log
            System.out.println("=== DEBUG EXPORT REQUEST ===");
            System.out.println("Role: " + role);
            System.out.println("Day request: " + dayRequestStr);
            System.out.println("Department: " + department);
            System.out.println("Recipient: " + recipientName);
            
            Date dayRequest = null;
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dayRequestStr);
                dayRequest = new java.sql.Date(utilDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                request.setAttribute("error", "Ngày không hợp lệ");
                request.getRequestDispatcher("ExportRequest.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin items
            String[] sttArr = request.getParameterValues("stt");
            String[] productNameArr = request.getParameterValues("product_name");
            String[] productCodeArr = request.getParameterValues("product_code");
            String[] unitArr = request.getParameterValues("unit");
            String[] quantityArr = request.getParameterValues("quantity");
            String[] noteArr = request.getParameterValues("note");
            String reasonDetail = request.getParameter("reason_detail");
            
            // Debug items
            System.out.println("Product names: " + java.util.Arrays.toString(productNameArr));
            System.out.println("Product codes: " + java.util.Arrays.toString(productCodeArr));
            System.out.println("Quantities: " + java.util.Arrays.toString(quantityArr));
            
            // Kiểm tra dữ liệu
            if (productNameArr == null || productCodeArr == null || quantityArr == null) {
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin sản phẩm");
                request.getRequestDispatcher("ExportRequest.jsp").forward(request, response);
                return;
            }
            
            // Chuyển đổi quantity sang int
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
            
            // Lấy tên sản phẩm thực tế từ database
            ProductInfoDAO productDAO = new ProductInfoDAO();
            String[] actualProductNames = new String[productNameArr.length];
            for (int i = 0; i < productNameArr.length; i++) {
                if (productNameArr[i] != null && !productNameArr[i].trim().isEmpty()) {
                    try {
                        int productId = Integer.parseInt(productNameArr[i]);
                        actualProductNames[i] = productDAO.getProductNameById(productId);
                    } catch (NumberFormatException e) {
                        actualProductNames[i] = productNameArr[i]; // Nếu không phải ID thì giữ nguyên
                    }
                } else {
                    actualProductNames[i] = "";
                }
            }
            
            ExportRequestDAO exportRequestDAO = new ExportRequestDAO();
            ExportRequestItemsDAO exportRequestItemsDAO = new ExportRequestItemsDAO();

            // Thêm export request
            String export_request_id = exportRequestDAO.addExportRequestIntoDB(
                user_id, role, dayRequest, "pending", reason, department, 
                recipientName, recipientPhone, recipientEmail);
            
            if (export_request_id != null) {
                // Thêm items
                exportRequestItemsDAO.addExportItemsIntoDB(
                    export_request_id, actualProductNames, productCodeArr, 
                    unitArr, quantityIntArr, noteArr, reasonDetail);
                
                // Set thông tin cho success page
                request.setAttribute("exportRequestId", export_request_id);
                request.setAttribute("requestDate", new SimpleDateFormat("dd/MM/yyyy").format(dayRequest));
                request.setAttribute("department", department);
                request.setAttribute("recipientName", recipientName);
                
                // Forward đến success page
                request.getRequestDispatcher("ExportRequestSuccessNotification.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không thể tạo đơn xuất kho. Vui lòng thử lại.");
                request.getRequestDispatcher("ExportRequest.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("ExportRequest.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Export Request Servlet";
    }
}
