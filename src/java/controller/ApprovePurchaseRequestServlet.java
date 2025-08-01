package controller;

import dao.GetStatusOfPurchaseRequestInformationDAO;
import dao.PurchaseOrderDAO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Request;

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
        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();

        try {
            if ("approve".equals(action)) {
                String requestId = request.getParameter("requestId");
                if (requestId != null && !requestId.isEmpty()) {
                    boolean isApproved = dao.updateApprovedStatus(requestId);
                    
                    if (isApproved) {
                        boolean isAddedToPurchaseOrder = purchaseOrderDAO.addApprovedRequestToPurchaseOrder(requestId);
                        
                        if (isAddedToPurchaseOrder) {
                            System.out.println("✅ Đã phê duyệt và add vào purchase order: " + requestId);
                        } else {
                            System.out.println("⚠️ Phê duyệt thành công nhưng không thể add vào purchase order: " + requestId);
                        }
                        
                        String redirectUrl = buildRedirectUrl(request);
                        String separator = redirectUrl.contains("?") ? "&" : "?";
                        String encodedMessage = java.net.URLEncoder.encode("Đã phê duyệt yêu cầu thành công!", "UTF-8");
                        response.sendRedirect(redirectUrl + separator + "message=" + encodedMessage);
                        return;
                    }
                }
            } else if ("reject".equals(action)) {
                String requestId = request.getParameter("requestId");
                String rejectReason = request.getParameter("rejectReason");
                
                if (requestId != null && !requestId.isEmpty() && rejectReason != null && !rejectReason.trim().isEmpty()) {
                    boolean isRejected = dao.updateRejectedStatusWithReason(requestId, rejectReason.trim());
                    if (isRejected) {
                        System.out.println("✅ Đã từ chối request: " + requestId + " với lý do: " + rejectReason);
                        String redirectUrl = buildRedirectUrl(request);
                        String separator = redirectUrl.contains("?") ? "&" : "?";
                        String encodedMessage = java.net.URLEncoder.encode("Đã từ chối yêu cầu thành công!", "UTF-8");
                        response.sendRedirect(redirectUrl + separator + "message=" + encodedMessage);
                        return;
                    } else {
                        request.setAttribute("errorMessage", "Không thể từ chối yêu cầu. Vui lòng thử lại!");
                    }
                } else {
                    request.setAttribute("errorMessage", "Thiếu thông tin yêu cầu hoặc lý do từ chối!");
                }
            }

            // Nếu không có action đặc biệt hoặc có lỗi, xử lý như request thông thường
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

        // Lấy các tham số lọc từ request
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String statusFilter = request.getParameter("statusFilter");
        String requestIdFilter = request.getParameter("requestIdFilter");
        String currentIndex = request.getParameter("index");

        if (startDate != null && !startDate.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("startDate=");
            try {
                url.append(java.net.URLEncoder.encode(startDate, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                url.append(startDate);
            }
            hasParams = true;
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("endDate=");
            try {
                url.append(java.net.URLEncoder.encode(endDate, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                url.append(endDate);
            }
            hasParams = true;
        }

        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("statusFilter=");
            try {
                url.append(java.net.URLEncoder.encode(statusFilter, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                url.append(statusFilter);
            }
            hasParams = true;
        }

        if (requestIdFilter != null && !requestIdFilter.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("requestIdFilter=");
            try {
                url.append(java.net.URLEncoder.encode(requestIdFilter, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                url.append(requestIdFilter);
            }
            hasParams = true;
        }

        // ✅ SỬA LỖI: Đổi từ isEmpty() thành !isEmpty()
        if (currentIndex != null && !currentIndex.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("index=");
            try {
                url.append(java.net.URLEncoder.encode(currentIndex, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                url.append(currentIndex);
            }
            hasParams = true;
        }

        return url.toString();
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý phê duyệt, từ chối, xóa yêu cầu mua hàng và lọc danh sách";
    }
}
