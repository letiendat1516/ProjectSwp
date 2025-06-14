package controller;

import dao.GetStatusOfPurchaseRequestInformationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Request;

/**
 * Servlet xử lý phê duyệt, từ chối, xóa yêu cầu mua hàng và lọc danh sách.
 */
public class ApprovePurchaseRequestServlet extends HttpServlet {

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
                endPage = 1; // Đảm bảo có ít nhất 1 trang
            }
            request.setAttribute("endPage", endPage);

            // Lấy trang hiện tại
            String indexPage = request.getParameter("index");
            int index = 1; // Mặc định là trang 1
            if (indexPage != null && indexPage.matches("\\d+")) {
                index = Integer.parseInt(indexPage);
            }

            // Lấy danh sách yêu cầu đã lọc
            List<Request> pendingRequests = dao.getAllPurchaseRequests(index, startDateStr, endDateStr, statusFilter, requestIdFilter);
            request.setAttribute("pendingPurchaseRequests", pendingRequests);

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("ApprovePurchaseRequest.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu: " + e.getMessage());
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
        String action = request.getParameter("action");
        GetStatusOfPurchaseRequestInformationDAO dao = new GetStatusOfPurchaseRequestInformationDAO();

        try {
            if ("delete".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.isEmpty()) {
                    boolean isDeleted = dao.deleteRequest(requestId);
                    if (isDeleted) {
                        // Redirect về trang hiện tại với các tham số lọc
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    }
                }
            } else if ("approve".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.isEmpty()) {
                    boolean isApproved = dao.updateApprovedStatus(requestId);
                    if (isApproved) {
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    }
                }
            } else if ("reject".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.isEmpty()) {
                    boolean isRejected = dao.updateRejectedStatus(requestId);
                    if (isRejected) {
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    }
                }
            } else if ("approve-all".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.isEmpty()) {
                    boolean isApproved = dao.updateApprovedStatus(requestId);
                    if (isApproved) {
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    }
                }
            } else if ("reject-all".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.isEmpty()) {
                    boolean isRejected = dao.updateRejectedStatus(requestId);
                    if (isRejected) {
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    }
                }
            }
            
            // Nếu không có action đặc biệt, xử lý như request thông thường
            processRequest(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu: " + e.getMessage());
        }
    }

    /**
     * Xây dựng URL redirect với các tham số lọc hiện tại
     */
    private String buildRedirectUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder("approvepurchaserequest");
        boolean hasParams = false;

        // Lấy các tham số lọc từ request (không phải từ action buttons)
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String statusFilter = request.getParameter("statusFilter");
        String requestIdFilter = request.getParameter("requestIdFilter");
        String currentIndex = request.getParameter("index");

        if (startDate != null && !startDate.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("startDate=").append(startDate);
            hasParams = true;
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("endDate=").append(endDate);
            hasParams = true;
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("statusFilter=").append(statusFilter);
            hasParams = true;
        }

        if (requestIdFilter != null && !requestIdFilter.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("requestIdFilter=").append(requestIdFilter);
            hasParams = true;
        }

        if (currentIndex != null && !currentIndex.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("index=").append(currentIndex);
            hasParams = true;
        }

        return url.toString();
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý phê duyệt, từ chối, xóa yêu cầu mua hàng và lọc danh sách";
    }
}