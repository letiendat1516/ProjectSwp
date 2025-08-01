package controller;

import dao.ExportDAO;
import dao.ExportRequestDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import model.ExportRequest;
import model.ExportRequestItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Users;

public class ExportConfirmController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ExportDAO dao = new ExportDAO();

        String requestId = request.getParameter("id");
        if (requestId == null || requestId.trim().isEmpty()) {
            response.sendRedirect("exportList?error=missing_id");
            return;
        }

        // Kiểm tra đơn hàng có thể xử lý không (chỉ approved)
        if (!dao.isOrderProcessable(requestId)) {
            response.sendRedirect("exportList?error=order_not_processable");
            return;
        }

        // Lấy thông tin đơn hàng (đã bao gồm thông tin người yêu cầu)
        ExportRequest exportRequest = dao.getExportRequestById(requestId);
        if (exportRequest == null) {
            response.sendRedirect("exportList?error=order_not_found");
            return;
        }

        // Lấy danh sách items
        List<ExportRequestItem> itemList = dao.getExportRequestItems(requestId);
        if (itemList == null || itemList.isEmpty()) {
            response.sendRedirect("exportList?error=no_items_found");
            return;
        }

        // Set attributes
        request.setAttribute("exportRequest", exportRequest);
        request.setAttribute("itemList", itemList);
        request.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // Debug log
        System.out.println("🔍 DEBUG - ExportConfirmController GET:");
        System.out.println("   Request ID: " + requestId);
        System.out.println("   Requester Display Name: " + exportRequest.getRequesterDisplayName());
        System.out.println("   Items count: " + itemList.size());

        // Forward to JSP
        request.getRequestDispatcher("ExportManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set encoding để xử lý tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        ExportDAO dao = new ExportDAO();
        HttpSession session = request.getSession();

        String requestId = request.getParameter("id");
        String action = request.getParameter("action");

        System.out.println("🔍 DEBUG - doPost received:");
        System.out.println("   Request ID: " + requestId);
        System.out.println("   Action: " + action);

        // Validate basic parameters
        if (requestId == null || requestId.trim().isEmpty() || action == null) {
            System.err.println("❌ Invalid data - requestId: " + requestId + ", action: " + action);
            response.sendRedirect("exportList?error=invalid_data");
            return;
        }

        // Kiểm tra đơn hàng có thể xử lý không
        if (!dao.isOrderProcessable(requestId)) {
            System.err.println("❌ Order not processable: " + requestId);
            response.sendRedirect("exportList?error=order_not_processable");
            return;
        }

        try {
            if ("confirm".equalsIgnoreCase(action)) {
                System.out.println("🧪 Testing database connection first...");
                dao.testDatabaseConnection();
                System.out.println("🔄 Processing CONFIRM action for request: " + requestId);

                // Xử lý xác nhận xuất kho
                String exportDate = request.getParameter("exportDate");
                String recipient = request.getParameter("recipient");
                String additionalNote = request.getParameter("additionalNote");

                System.out.println("   Export Date: " + exportDate);
                System.out.println("   Recipient: " + recipient);
                System.out.println("   Additional Note: " + additionalNote);

                // Validate required fields
                if (exportDate == null || exportDate.trim().isEmpty()) {
                    System.err.println("❌ Missing export date");
                    response.sendRedirect("export?id=" + requestId + "&error=missing_required_fields");
                    return;
                }

                if (recipient == null || recipient.trim().isEmpty()) {
                    System.err.println("❌ Missing recipient");
                    response.sendRedirect("export?id=" + requestId + "&error=recipient_required");
                    return;
                }

                // Lấy thông tin người xử lý từ session
                String processor = "System Admin";
                Users user = (Users) session.getAttribute("user");
                if (user != null) {
                    processor = user.getFullname() != null && !user.getFullname().trim().isEmpty()
                            ? user.getFullname() : user.getUsername();
                }

                System.out.println("   Processor: " + processor);

                // Lấy danh sách items để xuất kho
                List<ExportRequestItem> originalItems = dao.getExportRequestItems(requestId);

                if (originalItems == null || originalItems.isEmpty()) {
                    System.err.println("❌ No items found for export");
                    response.sendRedirect("export?id=" + requestId + "&error=no_items_to_export");
                    return;
                }

                System.out.println("   Found " + originalItems.size() + " items to export");

                // Xử lý xuất kho hoàn toàn
                boolean exportSuccess = dao.processCompleteExport(requestId, exportDate.trim(), 
                        recipient.trim(), processor, additionalNote, originalItems);

                if (exportSuccess) {
                    System.out.println("✅ Export completed successfully for request: " + requestId);
                    response.sendRedirect("exportList?message=export_completed");
                } else {
                    System.err.println("❌ Export failed for request: " + requestId);
                    response.sendRedirect("export?id=" + requestId + "&error=export_failed");
                }

            } else if ("reject".equalsIgnoreCase(action)) {
                System.out.println("🔄 Processing REJECT action for request: " + requestId);

                // Xử lý từ chối
                String rejectReason = request.getParameter("rejectReason");

                if (rejectReason == null || rejectReason.trim().isEmpty()) {
                    System.err.println("❌ Missing reject reason");
                    response.sendRedirect("export?id=" + requestId + "&error=reject_reason_required");
                    return;
                }

                System.out.println("🚫 Rejecting export request: " + requestId);
                System.out.println("   Reason: " + rejectReason);

                boolean updated = dao.updateRequestStatusToRejected(requestId, rejectReason.trim());

                if (updated) {
                    System.out.println("✅ Export request rejected successfully: " + requestId);
                    response.sendRedirect("exportList?message=reject_success");
                } else {
                    System.err.println("❌ Failed to reject export request: " + requestId);
                    response.sendRedirect("export?id=" + requestId + "&error=reject_failed");
                }

            } else {
                System.err.println("❌ Invalid action: " + action);
                response.sendRedirect("export?id=" + requestId + "&error=invalid_action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("💥 Error processing export for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect("export?id=" + requestId + "&error=processing_failed");
        }
    }
}