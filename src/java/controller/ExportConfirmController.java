package controller;

import dao.ExportDAO;
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

        System.out.println("=== ExportConfirmController doGet called ===");

        ExportDAO dao = new ExportDAO();

        String requestId = request.getParameter("id");
        System.out.println("Request ID: " + requestId);

        if (requestId == null || requestId.trim().isEmpty()) {
            System.err.println("Missing request ID");
            response.sendRedirect("exportList?error=missing_id");
            return;
        }

        // Kiểm tra đơn hàng có thể xử lý không
        if (!dao.isOrderProcessable(requestId)) {
            System.err.println("Order not processable: " + requestId);
            response.sendRedirect("exportList?error=order_not_processable");
            return;
        }

        // Khởi tạo pending items nếu chưa có
        dao.initializeExportPendingItems(requestId);

        // Lấy thông tin đơn hàng
        ExportRequest exportRequest = dao.getExportRequestById(requestId);
        if (exportRequest == null) {
            System.err.println("Export request not found: " + requestId);
            response.sendRedirect("exportList?error=order_not_found");
            return;
        }

        System.out.println("Export request status: " + exportRequest.getStatus());

        // Lấy danh sách items với thông tin xuất kho
        List<ExportRequestItem> itemList = dao.getExportRequestItemsByOrderId(requestId);
        System.out.println("Items count: " + itemList.size());

        // Lấy lịch sử xuất kho (nếu có)
        List<Object[]> exportHistory = dao.getExportHistory(requestId);
        System.out.println("History count: " + exportHistory.size());

        // Set attributes
        request.setAttribute("exportRequest", exportRequest);
        request.setAttribute("itemList", itemList);
        request.setAttribute("exportHistory", exportHistory);
        request.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // Forward to JSP
        request.getRequestDispatcher("/ExportManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Debug logging
        System.out.println("=== ExportConfirmController doPost called ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request parameters:");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println("  " + key + " = " + String.join(", ", values));
        });

        ExportDAO dao = new ExportDAO();
        HttpSession session = request.getSession();

        String requestId = request.getParameter("id");
        String action = request.getParameter("action");

        System.out.println("Request ID: " + requestId);
        System.out.println("Action: " + action);

        // Validate basic parameters
        if (requestId == null || requestId.trim().isEmpty() || action == null) {
            System.err.println("Missing required parameters - requestId: " + requestId + ", action: " + action);
            response.sendRedirect("exportList?error=invalid_data");
            return;
        }

        // Kiểm tra đơn hàng có thể xử lý không
        if (!dao.isOrderProcessable(requestId)) {
            System.err.println("Order not processable: " + requestId);
            response.sendRedirect("exportList?error=order_not_processable");
            return;
        }

        try {
            if ("confirm".equalsIgnoreCase(action)) {
                System.out.println("Processing confirm action");
                handleConfirmExport(request, response, dao, session, requestId);
            } else if ("reject".equalsIgnoreCase(action)) {
                System.out.println("Processing reject action");
                handleRejectExport(request, response, dao, requestId);
            } else {
                System.err.println("Invalid action: " + action);
                response.sendRedirect("export?id=" + requestId + "&error=invalid_action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing export for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect("export?id=" + requestId + "&error=processing_failed");
        }
    }

    private void handleConfirmExport(HttpServletRequest request, HttpServletResponse response,
            ExportDAO dao, HttpSession session, String requestId) throws IOException {

        System.out.println("=== handleConfirmExport called ===");

        // Xử lý xác nhận xuất kho từng phần
        String exportDate = request.getParameter("exportDate");
        String additionalNote = request.getParameter("additionalNote");

        System.out.println("Export date: " + exportDate);
        System.out.println("Additional note: " + additionalNote);

        // Validate required fields
        if (exportDate == null || exportDate.trim().isEmpty()) {
            System.err.println("Export date is empty");
            response.sendRedirect("export?id=" + requestId + "&error=missing_required_fields");
            return;
        }

        // Lấy thông tin người xử lý từ session
        String processor = "Unknown";
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            processor = user.getFullname() != null ? user.getFullname() : user.getUsername();
        }
        System.out.println("Processor: " + processor);

        // Lấy danh sách items để xuất kho
        List<ExportRequestItem> exportItems = new ArrayList<>();

        // Lấy danh sách items gốc
        List<ExportRequestItem> originalItems = dao.getExportRequestItemsByOrderId(requestId);
        System.out.println("Original items count: " + originalItems.size());

        for (ExportRequestItem originalItem : originalItems) {
            String quantityParam = request.getParameter("export_quantity_" + originalItem.getId());
            System.out.println("Item " + originalItem.getId() + " quantity param: " + quantityParam);

            if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                try {
                    // Kiểm tra format số nguyên
                    double exportQuantityDouble = Double.parseDouble(quantityParam.trim());

                    // Kiểm tra xem có phải số nguyên không
                    if (exportQuantityDouble != Math.floor(exportQuantityDouble) || exportQuantityDouble <= 0) {
                        System.out.println("Invalid quantity format for item " + originalItem.getId());
                        continue;
                    }

                    double exportQuantity = exportQuantityDouble;

                    // Kiểm tra số lượng hợp lệ
                    if (exportQuantity > 0 && exportQuantity <= originalItem.getQuantityPending()) {

                        // Kiểm tra tồn kho
                        if (!dao.checkInventoryAvailability(originalItem.getProductCode(), exportQuantity)) {
                            System.err.println("Insufficient inventory for: " + originalItem.getProductCode());
                            response.sendRedirect("export?id=" + requestId + "&error=insufficient_inventory&product=" + originalItem.getProductCode());
                            return;
                        }

                        // Tạo item để xuất kho
                        ExportRequestItem exportItem = new ExportRequestItem();
                        exportItem.setId(originalItem.getId());
                        exportItem.setExportRequestId(originalItem.getExportRequestId());
                        exportItem.setProductName(originalItem.getProductName());
                        exportItem.setProductCode(originalItem.getProductCode());
                        exportItem.setUnit(originalItem.getUnit());
                        exportItem.setQuantityRequested(originalItem.getQuantityRequested());
                        exportItem.setNote(originalItem.getNote());
                        exportItem.setProductId(originalItem.getProductId());
                        exportItem.setUnitId(originalItem.getUnitId());

                        // Set exportQuantity
                        exportItem.setExportQuantity(exportQuantity);
                        exportItem.setQuantity(originalItem.getQuantityRequested());

                        exportItems.add(exportItem);
                        System.out.println("Added export item: " + originalItem.getProductCode() + " quantity: " + exportQuantity);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for item " + originalItem.getId() + ": " + quantityParam);
                }
            }
        }

        // Kiểm tra có item nào để xuất không
        if (exportItems.isEmpty()) {
            System.err.println("No valid items to export");
            response.sendRedirect("export?id=" + requestId + "&error=no_valid_items_to_export");
            return;
        }

        System.out.println("Total export items: " + exportItems.size());

        // Xử lý xuất kho từng phần
        boolean success = dao.processPartialExport(requestId, exportDate.trim(), processor,
                additionalNote, exportItems);

        System.out.println("Export result: " + success);

        if (success) {
            response.sendRedirect("exportList?message=export_success");
        } else {
            response.sendRedirect("export?id=" + requestId + "&error=export_failed");
        }
    }

    private void handleRejectExport(HttpServletRequest request, HttpServletResponse response,
            ExportDAO dao, String requestId) throws IOException {

        System.out.println("=== handleRejectExport called ===");

        // Xử lý từ chối
        String rejectReason = request.getParameter("rejectReason");
        System.out.println("Reject reason: " + rejectReason);

        if (rejectReason == null || rejectReason.trim().isEmpty()) {
            System.err.println("Reject reason is empty");
            response.sendRedirect("export?id=" + requestId + "&error=reject_reason_required");
            return;
        }

        System.out.println("Calling updateRequestStatusToRejected...");
        boolean updated = dao.updateRequestStatusToRejected(requestId, rejectReason.trim());
        System.out.println("Update result: " + updated);

        if (updated) {
            System.out.println("Redirect to success");
            response.sendRedirect("exportList?message=reject_success");
        } else {
            System.out.println("Redirect to error");
            response.sendRedirect("export?id=" + requestId + "&error=reject_failed");
        }
    }
}
