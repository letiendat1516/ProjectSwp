package controller;

import dao.GetExportRequestWithItemsDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.ExportRequest;

/**
 * Servlet xử lý lịch sử yêu cầu xuất kho - chỉ xem và lọc danh sách
 */
@WebServlet(name = "HistoryExportRequestServlet", urlPatterns = {"/historyexportrequest"})
public class HistoryExportRequestServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // ✅ Set encoding cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            GetExportRequestWithItemsDAO dao = new GetExportRequestWithItemsDAO();

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

            // Parse dates
            java.sql.Date startDate = parseDate(startDateStr);
            java.sql.Date endDate = parseDate(endDateStr);

            // ✅ THAY ĐỔI: Thêm requestIdFilter vào method call
            // Tính tổng số yêu cầu và số trang
            int countPage = dao.getTotalFilteredExportRequests(startDate, endDate, statusFilter, null, requestIdFilter);
            int endPage = countPage / 10;
            if (countPage % 10 != 0) {
                endPage++;
            }
            if (endPage == 0) {
                endPage = 1; // Đảm bảo có ít nhất 1 trang
            }
            request.setAttribute("endPage", endPage);
            request.setAttribute("totalRequests", countPage);

            // Lấy trang hiện tại
            String indexPage = request.getParameter("index");
            int index = 1; // Mặc định là trang 1
            if (indexPage != null && indexPage.matches("\\d+")) {
                index = Integer.parseInt(indexPage);
            }

            // ✅ THAY ĐỔI: Thêm requestIdFilter vào method call
            // Lấy danh sách yêu cầu đã lọc
            List<ExportRequest> exportRequests = dao.getFilteredExportRequests(
                startDate, endDate, statusFilter, null, requestIdFilter, index, 10
            );
            request.setAttribute("exportRequests", exportRequests);
            
            // Debug info
            System.out.println("=== HISTORY EXPORT REQUEST SERVLET DEBUG ===");
            System.out.println("Total requests: " + countPage);
            System.out.println("Current page: " + index);
            System.out.println("Total pages: " + endPage);
            System.out.println("Found requests: " + exportRequests.size());
            System.out.println("Start date: " + startDate);
            System.out.println("End date: " + endDate);
            System.out.println("Status filter: " + statusFilter);
            System.out.println("Request ID filter: " + requestIdFilter); // ✅ THÊM debug cho ID filter

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("HistoryExportRequest.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải lịch sử yêu cầu xuất kho: " + e.getMessage());
            request.getRequestDispatcher("HistoryExportRequest.jsp").forward(request, response);
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
        // ✅ THAY ĐỔI: Xử lý POST request để hỗ trợ form filter
        // Thay vì redirect, xử lý như GET request để giữ nguyên parameters
        processRequest(request, response);
    }

    /**
     * Helper method to parse date string to SQL Date
     */
    private java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(dateStr.trim());
            return new java.sql.Date(utilDate.getTime());
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateStr + " - " + e.getMessage());
            return null;
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý lịch sử yêu cầu xuất kho - chỉ xem và lọc danh sách với hỗ trợ filter theo ID";
    }
}
