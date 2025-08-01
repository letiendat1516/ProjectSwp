package controller;

import dao.GetStatusOfPurchaseRequestInformationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Request;

public class HistoryPurchaseRequestServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            GetStatusOfPurchaseRequestInformationDAO dao = new GetStatusOfPurchaseRequestInformationDAO();

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

            // Tính tổng số yêu cầu và số trang
            int countPage = dao.getTotalFilteredRequests(startDateStr, endDateStr, statusFilter, requestIdFilter);
            int endPage = countPage / 10;
            if (countPage % 10 != 0) {
                endPage++;
            }
            if (endPage == 0) {
                endPage = 1;
            }
            request.setAttribute("endPage", endPage);
            request.setAttribute("totalRequests", countPage);

            // Lấy trang hiện tại
            String indexPage = request.getParameter("index");
            int index = 1;
            if (indexPage != null && indexPage.matches("\\d+")) {
                index = Integer.parseInt(indexPage);
            }

            // Lấy danh sách yêu cầu đã lọc (chỉ xem, không có chức năng phê duyệt)
            List<Request> requests = dao.getAllPurchaseRequests(index, startDateStr, endDateStr, statusFilter, requestIdFilter);
            request.setAttribute("purchaseRequests", requests);

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("HistoryPurchaseRequest.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("HistoryPurchaseRequest.jsp").forward(request, response);
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
        // POST request cũng chỉ để lọc dữ liệu, không có action phê duyệt/từ chối
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet hiển thị lịch sử yêu cầu mua hàng (chỉ xem)";
    }
}