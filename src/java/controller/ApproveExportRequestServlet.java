package controller;

import dao.GetExportRequestWithItemsDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.util.List;
import model.ExportRequest;
import model.Users;

public class ApproveExportRequestServlet extends HttpServlet {

    private GetExportRequestWithItemsDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new GetExportRequestWithItemsDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String requestId = request.getParameter("requestId");

        // Nếu có hành động approve hoặc reject
        if (action != null && requestId != null) {
            try {
                HttpSession session = request.getSession();
                Users currentUser = (Users) session.getAttribute("user");

                if (currentUser == null) {
                    response.sendRedirect("approveexportrequest?error=" + java.net.URLEncoder.encode("Bạn cần đăng nhập để thực hiện hành động!", "UTF-8"));
                    return;
                }

                boolean success = false;
                String message = "";

                if ("approve".equals(action)) {
                    success = dao.updateExportRequestStatus(requestId, "approved", currentUser.getId());
                    message = success
                            ? "Đã phê duyệt yêu cầu xuất kho " + requestId + " thành công!"
                            : "Không thể phê duyệt yêu cầu xuất kho " + requestId;
                } else if ("reject".equals(action)) {
                    success = dao.updateExportRequestStatus(requestId, "rejected", currentUser.getId());
                    message = success
                            ? "Đã từ chối yêu cầu xuất kho " + requestId
                            : "Không thể từ chối yêu cầu xuất kho " + requestId;
                }

                // Tạo redirect URL với các tham số lọc giữ nguyên
                StringBuilder redirectUrl = new StringBuilder("approveexportrequest?");
                redirectUrl.append(success ? "message=" : "error=").append(java.net.URLEncoder.encode(message, "UTF-8"));

                String[] params = {"startDate", "endDate", "statusFilter", "requestIdFilter", "index"};
                for (String param : params) {
                    String val = request.getParameter(param);
                    if (val != null && !val.trim().isEmpty()) {
                        redirectUrl.append("&").append(param).append("=").append(java.net.URLEncoder.encode(val, "UTF-8"));
                    }
                }

                response.sendRedirect(redirectUrl.toString());
                return;

            } catch (Exception e) {
                e.printStackTrace();
                String err = "Lỗi khi xử lý yêu cầu: " + e.getMessage();
                response.sendRedirect("approveexportrequest?error=" + java.net.URLEncoder.encode(err, "UTF-8"));
                return;
            }
        }

        // Nếu không có action, tiếp tục load dữ liệu như bình thường
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String statusFilter = request.getParameter("statusFilter");
            String requestIdFilter = request.getParameter("requestIdFilter");

            if (startDateStr != null && startDateStr.trim().isEmpty()) startDateStr = null;
            if (endDateStr != null && endDateStr.trim().isEmpty()) endDateStr = null;
            if (statusFilter != null && statusFilter.trim().isEmpty()) statusFilter = null;
            if (requestIdFilter != null && requestIdFilter.trim().isEmpty()) requestIdFilter = null;

            java.sql.Date startDate = parseDate(startDateStr);
            java.sql.Date endDate = parseDate(endDateStr);

            int countPage = dao.getTotalFilteredExportRequests(startDate, endDate, statusFilter, null,requestIdFilter);
            int endPage = (countPage + 9) / 10;
            if (endPage == 0) endPage = 1;

            String indexPage = request.getParameter("index");
            int index = 1;
            if (indexPage != null && indexPage.matches("\\d+")) {
                index = Integer.parseInt(indexPage);
            }

            List<ExportRequest> exportRequests = dao.getFilteredExportRequests(
                    startDate, endDate, statusFilter, null, index, 10,requestIdFilter
            );

            request.setAttribute("endPage", endPage);
            request.setAttribute("totalRequests", countPage);
            request.setAttribute("pendingExportRequests", exportRequests);

            // Thông báo message hoặc lỗi
            String message = request.getParameter("message");
            if (message != null) request.setAttribute("message", message);
            String error = request.getParameter("error");
            if (error != null) request.setAttribute("errorMessage", error);

            request.getRequestDispatcher("ApproveExportRequest.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("ApproveExportRequest.jsp").forward(request, response);
        }
    }

    private java.sql.Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return new java.sql.Date(sdf.parse(dateStr.trim()).getTime());
        } catch (Exception e) {
            System.err.println("Lỗi parse ngày: " + e.getMessage());
            return null;
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
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý phê duyệt/từ chối yêu cầu xuất kho";
    }
}
