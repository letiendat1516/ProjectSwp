package controller;

import dao.ListRequestImportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.SQLException;
import model.ApprovedRequestItem;

public class ListRequestImportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tạo instance của DAO
        ListRequestImportDAO dao = new ListRequestImportDAO();

        // Lấy tham số tìm kiếm từ request
        String search = request.getParameter("search");

        // Lấy danh sách ApprovedRequestItem với tham số tìm kiếm
        List<ApprovedRequestItem> approvedItems = dao.getApprovedRequestItems(search);
        List<ApprovedRequestItem> completedItems = dao.getCompletedRequestItems(search);

        if (approvedItems == null || approvedItems.isEmpty()) {
            System.out.println("Không có yêu cầu nào đã được duyệt.");
        } else {
            System.out.println("Số lượng yêu cầu đã duyệt: " + approvedItems.size() + ", Items: " + approvedItems);
        }

        if (completedItems == null || completedItems.isEmpty()) {
            System.out.println("Không có yêu cầu nào đã hoàn thành.");
        } else {
            System.out.println("Số lượng yêu cầu đã hoàn thành: " + completedItems.size() + ", Items: " + completedItems);
        }

        // Gửi danh sách và tham số tìm kiếm sang JSP
        request.setAttribute("items", approvedItems);
        request.setAttribute("historyItems", completedItems);
        request.setAttribute("search", search);
        request.getRequestDispatcher("ListRequestImport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ListRequestImportDAO dao = new ListRequestImportDAO();
        String requestId = request.getParameter("id");
        String note = request.getParameter("note");
        String approveDate = request.getParameter("approveDate");

        // Validate parameters
        if (requestId == null || requestId.trim().isEmpty() || approveDate == null || approveDate.trim().isEmpty()) {
            System.err.println("Invalid parameters: requestId=" + requestId + ", approveDate=" + approveDate);
            response.sendRedirect(request.getContextPath() + "/request/list?type=purchase&error=invalid_data");
            return;
        }

        try {
            // Cập nhật trạng thái yêu cầu thành 'completed'
            dao.updateRequestStatus(requestId, "completed", approveDate);

            // Chuyển hướng về danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/request/list?type=purchase&message=approve_success");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException occurred while updating request status for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/request/list?type=purchase&error=approve_failed");
        }
    }
}