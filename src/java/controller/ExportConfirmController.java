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

        ExportDAO dao = new ExportDAO();

        String requestId = request.getParameter("id");
        if (requestId == null || requestId.trim().isEmpty()) {
            response.sendRedirect("exportList?error=missing_id");
            return;
        }

        // Kiểm tra đơn xuất có thể xử lý không
        if (!dao.isExportRequestProcessable(requestId)) {
            response.sendRedirect("exportList?error=request_not_processable");
            return;
        }

        // Lấy thông tin đơn xuất kho
        ExportRequest exportRequest = dao.getExportRequestById(requestId);
        if (exportRequest == null) {
            response.sendRedirect("exportList?error=request_not_found");
            return;
        }

        // Lấy danh sách items với thông tin xuất kho
        List<ExportRequestItem> itemList = dao.getExportRequestItemsByRequestId(requestId);

        // Lấy lịch sử xuất kho (nếu có)
        List<Object[]> exportHistory = dao.getExportHistory(requestId);

        // Set attributes
        request.setAttribute("exportRequest", exportRequest);
        request.setAttribute("itemList", itemList);
        request.setAttribute("exportHistory", exportHistory);
        request.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // Forward to JSP
        request.getRequestDispatcher("ExportManagement.jsp").forward(request, response);
    }

// Sửa lại method doPost trong ExportConfirmController
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

        System.out.println("=== DEBUG POST REQUEST ===");
        System.out.println("Request ID: " + requestId);
        System.out.println("Action: " + action);

        // Validate basic parameters
        if (requestId == null || requestId.trim().isEmpty() || action == null) {
            System.out.println("ERROR: Missing basic parameters");
            response.sendRedirect("exportList?error=invalid_data");
            return;
        }

        // Kiểm tra đơn xuất có thể xử lý không
        if (!dao.isExportRequestProcessable(requestId)) {
            System.out.println("ERROR: Request not processable");
            response.sendRedirect("exportList?error=request_not_processable");
            return;
        }

        try {
            if ("confirm".equalsIgnoreCase(action)) {
                System.out.println("Processing CONFIRM action");

                // Xử lý xác nhận xuất kho từng phần
                String exportDate = request.getParameter("exportDate");
                String additionalNote = request.getParameter("additionalNote");

                System.out.println("Export Date: " + exportDate);
                System.out.println("Additional Note: " + additionalNote);

                // Validate required fields
                if (exportDate == null || exportDate.trim().isEmpty()) {
                    System.out.println("ERROR: Missing export date");
                    response.sendRedirect("export?id=" + requestId + "&error=missing_required_fields");
                    return;
                }

                // FIX: Validate date format
                try {
                    java.sql.Date.valueOf(exportDate.trim());
                } catch (IllegalArgumentException e) {
                    System.out.println("ERROR: Invalid date format: " + exportDate);
                    response.sendRedirect("export?id=" + requestId + "&error=invalid_date_format");
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
                List<ExportRequestItem> originalItems = dao.getExportRequestItemsByRequestId(requestId);
                System.out.println("Original items count: " + originalItems.size());

                boolean hasValidItems = false;
                List<String> validationErrors = new ArrayList<>();

                // Debug: In ra tất cả parameters
                System.out.println("=== ALL PARAMETERS ===");
                request.getParameterMap().forEach((key, values) -> {
                    System.out.println(key + " = " + String.join(", ", values));
                });

                for (ExportRequestItem originalItem : originalItems) {
                    String quantityParam = request.getParameter("export_quantity_" + originalItem.getId());
                    System.out.println("Processing item ID: " + originalItem.getId()
                            + ", Product: " + originalItem.getProductName()
                            + ", Quantity param: " + quantityParam
                            + ", Available: " + originalItem.getQuantityPending());

                    if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                        try {
                            String trimmedValue = quantityParam.trim();

                            // Kiểm tra chặt chẽ chỉ chứa số nguyên dương
                            if (!trimmedValue.matches("^\\d+$")) {
                                validationErrors.add("Số lượng xuất cho " + originalItem.getProductName() + " phải là số nguyên dương (1, 2, 3...)");
                                continue;
                            }

                            // Parse thành integer
                            int exportQuantityInt = Integer.parseInt(trimmedValue);

                            // Kiểm tra số nguyên dương (phải lớn hơn 0)
                            if (exportQuantityInt <= 0) {
                                validationErrors.add("Số lượng xuất cho " + originalItem.getProductName() + " phải lớn hơn 0");
                                continue;
                            }

                            double exportQuantity = exportQuantityInt;

                            // Kiểm tra số lượng không vượt quá số lượng còn lại
                            if (exportQuantity > originalItem.getQuantityPending()) {
                                validationErrors.add("Số lượng xuất cho " + originalItem.getProductName()
                                        + " (" + (int) exportQuantity + ") vượt quá số lượng còn lại (" + (int) originalItem.getQuantityPending() + ")");
                                continue;
                            }

                            // FIX: Tạo item để xuất kho với đầy đủ thông tin
                            ExportRequestItem exportItem = new ExportRequestItem();
                            exportItem.setId(originalItem.getId());
                            exportItem.setExportRequestId(originalItem.getExportRequestId());
                            exportItem.setProductName(originalItem.getProductName());
                            exportItem.setProductCode(originalItem.getProductCode());
                            exportItem.setUnit(originalItem.getUnit());
                            exportItem.setQuantity(exportQuantity); // Số lượng xuất lần này
                            exportItem.setQuantityRequested(originalItem.getQuantityRequested());
                            exportItem.setNote(originalItem.getNote());
                            exportItem.setProductId(originalItem.getProductId());
                            exportItem.setUnitId(originalItem.getUnitId());

                            exportItems.add(exportItem);
                            hasValidItems = true;

                            System.out.println("Added export item: " + originalItem.getProductName()
                                    + ", Quantity: " + exportQuantity
                                    + ", Product Code: " + originalItem.getProductCode());

                        } catch (NumberFormatException e) {
                            validationErrors.add("Số lượng xuất cho " + originalItem.getProductName() + " không hợp lệ (chỉ nhập số nguyên dương)");
                            System.out.println("NumberFormatException for item: " + originalItem.getProductName() + ", value: " + quantityParam);
                        }
                    }
                }

                System.out.println("Valid items count: " + exportItems.size());
                System.out.println("Has valid items: " + hasValidItems);
                System.out.println("Validation errors: " + validationErrors.size());

                // Kiểm tra validation errors
                if (!validationErrors.isEmpty()) {
                    System.out.println("Validation errors found, forwarding back to form");
                    for (String error : validationErrors) {
                        System.out.println("Validation error: " + error);
                    }
                    request.setAttribute("validationErrors", validationErrors);
                    request.setAttribute("errorMessage", "Có lỗi trong dữ liệu nhập:");
                    doGet(request, response);
                    return;
                }

                // Kiểm tra có item nào để xuất không
                if (!hasValidItems) {
                    System.out.println("No valid items to export");
                    response.sendRedirect("export?id=" + requestId + "&error=no_valid_items_to_export");
                    return;
                }

                // FIX: Kiểm tra lại dữ liệu trước khi gọi DAO
                System.out.println("=== FINAL VALIDATION BEFORE DAO ===");
                for (ExportRequestItem item : exportItems) {
                    System.out.println("Final item check - Product: " + item.getProductName()
                            + ", Code: " + item.getProductCode()
                            + ", Quantity: " + item.getQuantity()
                            + ", Request ID: " + item.getExportRequestId());

                    if (item.getProductCode() == null || item.getProductCode().trim().isEmpty()) {
                        System.out.println("ERROR: Product code is null or empty for " + item.getProductName());
                        response.sendRedirect("export?id=" + requestId + "&error=invalid_product_data");
                        return;
                    }
                }

                // Xử lý xuất kho từng phần
                System.out.println("Calling processPartialExport...");
                boolean success = dao.processPartialExport(requestId, exportDate.trim(), processor,
                        additionalNote != null ? additionalNote.trim() : null, exportItems);

                System.out.println("Export result: " + success);

                if (success) {
                    // Kiểm tra trạng thái sau khi xuất kho
                    ExportRequest updatedRequest = dao.getExportRequestByIdAnyStatus(requestId);
                    if (updatedRequest != null) {
                        System.out.println("Updated status: " + updatedRequest.getStatus());
                        if ("completed".equals(updatedRequest.getStatus())) {
                            // Đã hoàn thành -> chuyển về tab lịch sử
                            response.sendRedirect("exportList?tab=history&message=export_completed");
                        } else if ("partial_exported".equals(updatedRequest.getStatus())) {
                            // Xuất từng phần -> vẫn ở tab đã duyệt
                            response.sendRedirect("exportList?tab=approved&message=partial_export_success");
                        } else {
                            // Trường hợp khác
                            response.sendRedirect("exportList?message=export_success");
                        }
                    } else {
                        response.sendRedirect("exportList?message=export_success");
                    }
                } else {
                    System.out.println("Export failed");
                    response.sendRedirect("export?id=" + requestId + "&error=export_failed");
                }

            } else if ("reject".equalsIgnoreCase(action)) {
                System.out.println("Processing REJECT action");

                // Xử lý từ chối
                String rejectReason = request.getParameter("rejectReason");

                if (rejectReason == null || rejectReason.trim().isEmpty()) {
                    System.out.println("ERROR: Missing reject reason");
                    response.sendRedirect("export?id=" + requestId + "&error=reject_reason_required");
                    return;
                }

                System.out.println("Reject reason: " + rejectReason);

                boolean updated = dao.updateExportRequestStatusToRejected(requestId, rejectReason.trim());

                if (updated) {
                    System.out.println("Reject successful");
                    // Từ chối -> chuyển về tab lịch sử
                    response.sendRedirect("exportList?tab=history&message=reject_success");
                } else {
                    System.out.println("Reject failed");
                    response.sendRedirect("export?id=" + requestId + "&error=reject_failed");
                }

            } else {
                System.out.println("Invalid action: " + action);
                response.sendRedirect("export?id=" + requestId + "&error=invalid_action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing export for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect("export?id=" + requestId + "&error=processing_failed");
        }
    }
}
