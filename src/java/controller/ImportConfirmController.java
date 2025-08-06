package controller;

import dao.ImportDAO;
import dao.PurchaseOrderDAO; // Thêm import này
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import model.PurchaseOrderInfo;
import model.PurchaseOrderItems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import model.Users;

public class ImportConfirmController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ImportDAO dao = new ImportDAO();

        String requestId = request.getParameter("id");
        if (requestId == null || requestId.trim().isEmpty()) {
            response.sendRedirect("request/list?error=missing_id");
            return;
        }

        // Kiểm tra đơn hàng có thể xử lý không
        if (!dao.isOrderProcessable(requestId)) {
            response.sendRedirect("request/list?error=order_not_processable");
            return;
        }

        // Lấy thông tin đơn hàng
        PurchaseOrderInfo purchaseOrder = dao.getPurchaseOrderById(requestId);
        if (purchaseOrder == null) {
            response.sendRedirect("request/list?error=order_not_found");
            return;
        }

        // Lấy danh sách items với thông tin nhập kho
        List<PurchaseOrderItems> itemList = dao.getPurchaseOrderItemsByOrderId(requestId);

        // Lấy lịch sử nhập kho (nếu có)
        List<Object[]> importHistory = dao.getImportHistory(requestId);

        // Set attributes
        request.setAttribute("p", purchaseOrder);
        request.setAttribute("itemList", itemList);
        request.setAttribute("importHistory", importHistory);
        request.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // Forward to JSP
        request.getRequestDispatcher("WarehouseManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ImportDAO dao = new ImportDAO();
        PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO(); // Thêm instance của PurchaseOrderDAO
        HttpSession session = request.getSession();

        String requestId = request.getParameter("id");
        String action = request.getParameter("action");

        // Validate basic parameters
        if (requestId == null || requestId.trim().isEmpty() || action == null) {
            response.sendRedirect("request/list?error=invalid_data");
            return;
        }

        // Kiểm tra đơn hàng có thể xử lý không
        if (!dao.isOrderProcessable(requestId)) {
            response.sendRedirect("request/list?error=order_not_processable");
            return;
        }

        try {
            if ("confirm".equalsIgnoreCase(action)) {
                // Xử lý xác nhận nhập kho từng phần (bỏ warehouse)
                String importDate = request.getParameter("importDate");
                String additionalNote = request.getParameter("additionalNote");

                // Validate required fields (chỉ cần importDate)
                if (importDate == null || importDate.trim().isEmpty()) {
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=missing_required_fields");
                    return;
                }

                // Lấy thông tin người xử lý từ session
                String processor = "Unknown";
                Users user = (Users) session.getAttribute("user");
                if (user != null) {
                    processor = user.getFullname() != null ? user.getFullname() : user.getUsername();
                }

                // Lấy danh sách items để nhập kho
                List<PurchaseOrderItems> importItems = new ArrayList<>();

                // Lấy danh sách items gốc
                List<PurchaseOrderItems> originalItems = dao.getPurchaseOrderItemsByOrderId(requestId);

                for (PurchaseOrderItems originalItem : originalItems) {
                    String quantityParam = request.getParameter("import_quantity_" + originalItem.getId());

                    if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                        try {
                            // Chỉ chấp nhận số nguyên
                            int importQuantityInt = Integer.parseInt(quantityParam.trim());

                            // Kiểm tra số nguyên dương
                            if (importQuantityInt <= 0) {
                                System.err.println("Invalid quantity (not positive integer) for item " + originalItem.getId());
                                continue;
                            }

                            BigDecimal importQuantity = new BigDecimal(importQuantityInt);

                            // Kiểm tra số lượng hợp lệ (phải là số nguyên dương)
                            if (importQuantity.compareTo(BigDecimal.ZERO) > 0
                                    && importQuantity.compareTo(originalItem.getQuantityPending()) <= 0) {

                                // Tạo item để nhập kho
                                PurchaseOrderItems importItem = new PurchaseOrderItems();
                                importItem.setId(originalItem.getId());
                                importItem.setPurchaseId(originalItem.getPurchaseId());
                                importItem.setProductName(originalItem.getProductName());
                                importItem.setProductCode(originalItem.getProductCode());
                                importItem.setUnit(originalItem.getUnit());
                                importItem.setQuantity(importQuantity); // Số lượng nhập lần này (số nguyên)
                                importItem.setQuantityOrdered(originalItem.getQuantityOrdered());
                                importItem.setPricePerUnit(originalItem.getPricePerUnit());
                                importItem.setNote(originalItem.getNote());

                                importItems.add(importItem);
                            } else {
                                System.err.println("Quantity exceeds pending amount for item " + originalItem.getId());
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid integer quantity format for item " + originalItem.getId() + ": " + quantityParam);
                            // Có thể thêm thông báo lỗi cho user
                        }
                    }
                }

                // Kiểm tra có item nào để nhập không
                if (importItems.isEmpty()) {
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=no_valid_items_to_import");
                    return;
                }

                // Xử lý nhập kho từng phần (bỏ warehouse parameter)
                boolean importSuccess = dao.processPartialImport(requestId, importDate.trim(), processor,
                        additionalNote, importItems);

                // Trong doPost method, thay thế phần xử lý sau khi import thành công:
                if (importSuccess) {
                    System.out.println("✅ Import và cập nhật stock thành công cho đơn: " + requestId);

                    // Kiểm tra xem đơn đã hoàn thành chưa để cập nhật status
                    boolean isFullyImported = dao.isOrderFullyImported(requestId);

                    if (isFullyImported) {
                        System.out.println("📦 Đơn " + requestId + " đã nhập đủ, cập nhật status thành 'completed'");

                        // Chỉ cập nhật status thành 'completed', không cần cập nhật stock nữa
                        boolean statusUpdated = purchaseOrderDAO.updatePurchaseOrderStatus(requestId, "completed");

                        if (statusUpdated) {
                            System.out.println("✅ Đã cập nhật status thành 'completed' cho đơn: " + requestId);
                            response.sendRedirect("request/list?message=import_completed_success");
                        } else {
                            System.err.println("❌ Lỗi cập nhật status cho đơn: " + requestId);
                            response.sendRedirect("request/list?message=import_success_but_status_failed");
                        }
                    } else {
                        System.out.println("ℹ️ Đơn " + requestId + " chưa nhập đủ, tiếp tục với status 'partial_imported'");
                        response.sendRedirect("request/list?message=partial_import_success");
                    }
                } else {
                    System.err.println("❌ Import thất bại cho đơn: " + requestId);
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=import_failed");
                }

            } else if ("reject".equalsIgnoreCase(action)) {
                // Xử lý từ chối
                String rejectReason = request.getParameter("rejectReason");

                if (rejectReason == null || rejectReason.trim().isEmpty()) {
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=reject_reason_required");
                    return;
                }

                boolean updated = dao.updateRequestStatusToRejected(requestId, rejectReason.trim());

                if (updated) {
                    response.sendRedirect("request/list?message=reject_success");
                } else {
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=reject_failed");
                }

            } else {
                response.sendRedirect("import-confirm?id=" + requestId + "&error=invalid_action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing import for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect("import-confirm?id=" + requestId + "&error=processing_failed");
        }
    }
}
