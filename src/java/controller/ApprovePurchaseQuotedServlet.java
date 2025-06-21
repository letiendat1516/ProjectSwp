package controller;

import dao.PurchaseQuotedDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.PurchaseOrderInfo;

/**
 * Servlet xử lý phê duyệt, từ chối, xóa Purchase Order và lọc danh sách.
 */
public class ApprovePurchaseQuotedServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            PurchaseQuotedDAO dao = new PurchaseQuotedDAO();

            // Lấy tham số lọc từ form
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String statusFilter = request.getParameter("statusFilter");
            String orderIdFilter = request.getParameter("orderIdFilter");

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
            if (orderIdFilter != null && orderIdFilter.trim().isEmpty()) {
                orderIdFilter = null;
            }

            // Tính tổng số Purchase Order và số trang
            int countPage = dao.getTotalFilteredPurchaseQuoted(startDateStr, endDateStr, statusFilter, orderIdFilter);
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

            // Lấy danh sách Purchase Order đã lọc
            List<PurchaseOrderInfo> purchaseOrders = dao.getAllPurchaseQuoted(index, startDateStr, endDateStr, statusFilter, orderIdFilter);
            request.setAttribute("purchaseOrders", purchaseOrders);

            // Debug logging
            System.out.println("=== ApprovePurchaseOrderServlet DEBUG ===");
            System.out.println("Total Purchase Orders: " + countPage);
            System.out.println("Current page: " + index);
            System.out.println("End page: " + endPage);
            System.out.println("Orders loaded: " + (purchaseOrders != null ? purchaseOrders.size() : 0));

            // Chuyển tiếp đến JSP
            request.getRequestDispatcher("ApprovePurchaseQuoted.jsp").forward(request, response);

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
        PurchaseQuotedDAO dao = new PurchaseQuotedDAO();

        try {
            if ("delete".equals(action)) {
                String orderId = request.getParameter("orderId");
                if (orderId != null && !orderId.isEmpty()) {
                    boolean isDeleted = dao.deletePurchaseOrder(orderId);
                    if (isDeleted) {
                        System.out.println("✅ Purchase Order deleted successfully: " + orderId);
                        // Redirect về trang hiện tại với các tham số lọc
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    } else {
                        System.err.println("❌ Failed to delete Purchase Order: " + orderId);
                    }
                }
            } else if ("approve".equals(action)) {
                String orderId = request.getParameter("orderId");
                if (orderId != null && !orderId.isEmpty()) {
                    // Sử dụng updateStatus thay vì updatePurchaseOrderStatus
                    boolean isApproved = dao.updateStatus(orderId, "approved");
                    if (isApproved) {
                        System.out.println("✅ Purchase Order approved and Request completed: " + orderId);
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    } else {
                        System.err.println("❌ Failed to approve Purchase Order: " + orderId);
                    }
                }
            } else if ("reject".equals(action)) {
                String orderId = request.getParameter("orderId");
                if (orderId != null && !orderId.isEmpty()) {
                    // Sử dụng updateStatus thay vì updatePurchaseOrderStatus
                    boolean isRejected = dao.updateStatus(orderId, "rejected");
                    if (isRejected) {
                        System.out.println("✅ Purchase Order rejected and Request set to pending re-quote: " + orderId);
                        String redirectUrl = buildRedirectUrl(request);
                        response.sendRedirect(redirectUrl);
                        return;
                    } else {
                        System.err.println("❌ Failed to reject Purchase Order: " + orderId);
                    }
                }
            }

            // Nếu không có action đặc biệt hoặc thất bại, xử lý như request thông thường
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
        StringBuilder url = new StringBuilder("approvepurchasequoted");
        boolean hasParams = false;

        // Lấy các tham số lọc từ request (không phải từ action buttons)
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String statusFilter = request.getParameter("statusFilter");
        String orderIdFilter = request.getParameter("orderIdFilter");
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

        if (orderIdFilter != null && !orderIdFilter.trim().isEmpty()) {
            url.append(hasParams ? "&" : "?").append("orderIdFilter=").append(orderIdFilter);
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
        return "Servlet xử lý phê duyệt, từ chối, xóa Purchase Order và lọc danh sách";
    }
}
