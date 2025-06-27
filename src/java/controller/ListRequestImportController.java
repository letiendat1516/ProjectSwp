package controller;

import dao.ListRequestImportDAO;
import dao.SupplierDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import model.ApprovedRequestItem;
import model.Supplier;

@WebServlet("/request/list")
public class ListRequestImportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Tạo instance của DAO
        ListRequestImportDAO dao = new ListRequestImportDAO();

        // Debug: Test connection
        dao.testConnection();

        // Lấy tham số tìm kiếm từ request
        String searchType = request.getParameter("searchType");
        String searchValue = request.getParameter("searchValue");

        System.out.println("Search parameters - Type: " + searchType + ", Value: " + searchValue);

        // Lấy danh sách ApprovedRequestItem với tham số tìm kiếm
        List<ApprovedRequestItem> approvedItems = dao.getApprovedRequestItems(searchType, searchValue);
        List<ApprovedRequestItem> completedItems = dao.getCompletedRequestItems(searchType, searchValue);

        // Debug logs
        System.out.println("Approved items size: " + (approvedItems != null ? approvedItems.size() : "null"));
        System.out.println("Completed items size: " + (completedItems != null ? completedItems.size() : "null"));

        // Rest of your code...
        SupplierDAO sd = new SupplierDAO();
        List<Supplier> listSupplier = new ArrayList<>();
        for (int i = 0; i < completedItems.size(); i++) {
            if (completedItems.get(i).getSupplier() != null) {
                Supplier s = sd.getSupplierByName(completedItems.get(i).getSupplier());
                if (s != null) {
                    listSupplier.add(s);
                }
            }
        }

        // Gửi danh sách và tham số tìm kiếm sang JSP
        request.setAttribute("items", approvedItems);
        request.setAttribute("historyItems", completedItems);
        request.setAttribute("searchType", searchType);
        request.setAttribute("searchValue", searchValue);
        request.setAttribute("supplier", listSupplier);

        request.getRequestDispatcher("/ListRequestImport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ListRequestImportDAO dao = new ListRequestImportDAO();
        String requestId = request.getParameter("id");
        String note = request.getParameter("note");
        String approveDate = request.getParameter("approveDate");

        // Validate parameters
        if (requestId == null || requestId.trim().isEmpty()
                || approveDate == null || approveDate.trim().isEmpty()) {

            System.err.println("Invalid parameters: requestId=" + requestId + ", approveDate=" + approveDate);
            response.sendRedirect(request.getContextPath() + "/request/list?error=invalid_data");
            return;
        }

        try {
            // Cập nhật trạng thái yêu cầu thành 'completed'
            dao.updateRequestStatus(requestId, "completed", approveDate);

            // Chuyển hướng về danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/request/list?message=approve_success");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException occurred while updating request status for requestId: "
                    + requestId + ", Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/request/list?error=approve_failed");
        }
    }
}
