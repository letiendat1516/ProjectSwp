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
            String action = request.getParameter("action");

            // Xử lý hành động xóa lọc
            if ("clear".equals(action)) {
                startDateStr = null;
                endDateStr = null;
                statusFilter = null;
                requestIdFilter = null;
                request.setAttribute("startDate", null);
                request.setAttribute("endDate", null);
                request.setAttribute("statusFilter", null);
                request.setAttribute("requestIdFilter", null);
            }

            // Tính tổng số yêu cầu và số trang
            int countPage = dao.getTotalFilteredRequests(startDateStr, endDateStr, statusFilter, requestIdFilter);
            int endPage = countPage / 10;
            if (countPage % 10 != 0) {
                endPage++;
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

        if ("delete".equals(action)) {
            String requestId = request.getParameter("requestId");
            if (requestId != null && !requestId.isEmpty()) {
                boolean isDeleted = dao.deleteRequest(requestId);
                if (isDeleted) {
                    response.sendRedirect("approvepurchaserequest");
                    return;
                }
            }
        } else if ("approve".equals(action)) {
            String requestId = request.getParameter("requestId");
            if (requestId != null && !requestId.isEmpty()) {
                boolean isApproved = dao.updateApprovedStatus(requestId);
                if (isApproved) {
                    response.sendRedirect("approvepurchaserequest");
                    return;
                }
            }
        } else if ("reject".equals(action)) {
            String requestId = request.getParameter("requestId");
            if (requestId != null && !requestId.isEmpty()) {
                boolean isRejected = dao.updateRejectedStatus(requestId);
                if (isRejected) {
                    response.sendRedirect("approvepurchaserequest");
                    return;
                }
            }
        } else if ("approve-all".equals(action)) {
            String requestId = request.getParameter("requestId");
            if (requestId != null && !requestId.isEmpty()) {
                boolean isApproved = dao.updateApprovedStatus(requestId);
                if (isApproved) {
                    response.sendRedirect("approvepurchaserequest");
                    return;
                }
            }
        } else if ("reject-all".equals(action)) {
            String requestId = request.getParameter("requestId");
            if (requestId != null && !requestId.isEmpty()) {
                boolean isRejected = dao.updateRejectedStatus(requestId);
                if (isRejected) {
                    response.sendRedirect("approvepurchaserequest");
                    return;
                }
            }
        } else {
            processRequest(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý phê duyệt, từ chối, xóa yêu cầu mua hàng và lọc danh sách";
    }
}