package controller;

import dao.GetStatusOfPurchaseRequestInformationDAO;
import dao.PurchaseOrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Request;

/**
 * Servlet xử lý Purchase Order - hiển thị các đơn đã được duyệt
 */
public class ListPurchaseOrderServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            PurchaseOrderDAO dao = new PurchaseOrderDAO();

            // Lấy tham số lọc từ form
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String statusFilter = request.getParameter("statusFilter");
            String requestIdFilter = request.getParameter("requestIdFilter");

            // Kiểm tra và làm sạch các tham số rỗng
            if (startDateStr != null && startDateStr.trim().isEmpty()) {
                startDateStr = null;
            }
            if (endDateStr != null && endDateStr.trim().isEmpty()) {
                endDateStr = null;
            }
            if (statusFilter != null && statusFilter.trim().isEmpty()) {
                statusFilter = null;
            }
            if (requestIdFilter != null && requestIdFilter.trim().isEmpty()) {
                requestIdFilter = null;
            }

            // Tính tổng số purchase order và số trang
            int countPage = dao.getTotalFilteredPurchaseOrders(startDateStr, endDateStr, statusFilter, requestIdFilter);
            int endPage = countPage / 11; // 11 items per page
            if (countPage % 11 != 0) {
                endPage++;
            }
            if (endPage == 0) {
                endPage = 1; // Đảm bảo có ít nhất 1 trang
            }
            request.setAttribute("endPage", endPage);

            // Lấy trang hiện tại
            String indexPage = request.getParameter("index");
            int index = 1; // Mặc định là trang 1
            if (indexPage != null && indexPage.matches("\\d+")) {
                index = Integer.parseInt(indexPage);
            }

            // Lấy danh sách purchase order đã lọc
            List<Request> purchaseOrders = dao.getAllPurchaseOrder(index, startDateStr, endDateStr, statusFilter, requestIdFilter);
            request.setAttribute("purchaseOrders", purchaseOrders);

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("ListPurchaseOrder.jsp").forward(request, response);

        } catch (ServletException | IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chỉ xử lý filter, không có action xóa nữa
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý Purchase Order - hiển thị các đơn đã được duyệt";
    }
}
