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
    HttpSession session = request.getSession();

    String requestId = request.getParameter("id");
    String action = request.getParameter("action");

    if (requestId == null || requestId.trim().isEmpty() || action == null) {
        response.sendRedirect("request/list?error=invalid_data");
        return;
    }

    // Kiểm tra đơn có thể xử lý
    if (!dao.isOrderProcessable(requestId)) {
        response.sendRedirect("request/list?error=order_not_processable");
        return;
    }

    try {
        if ("confirm".equalsIgnoreCase(action)) {
            String importDate = request.getParameter("importDate");
            String additionalNote = request.getParameter("additionalNote");

            if (importDate == null || importDate.trim().isEmpty()) {
                response.sendRedirect("import-confirm?id=" + requestId + "&error=missing_required_fields");
                return;
            }

            // Người xử lý từ session
            String processor = "Unknown";
            Users user = (Users) session.getAttribute("user");
            if (user != null) {
                processor = user.getFullname() != null ? user.getFullname() : user.getUsername();
            }

            // Lấy danh sách items
            List<PurchaseOrderItems> importItems = new ArrayList<>();
            List<PurchaseOrderItems> originalItems = dao.getPurchaseOrderItemsByOrderId(requestId);

            for (PurchaseOrderItems originalItem : originalItems) {
                String quantityParam = request.getParameter("import_quantity_" + originalItem.getId());

                if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                    try {
                        int importQuantityInt = Integer.parseInt(quantityParam.trim());

                        if (importQuantityInt <= 0) {
                            continue;
                        }

                        BigDecimal importQuantity = new BigDecimal(importQuantityInt);

                        if (importQuantity.compareTo(BigDecimal.ZERO) > 0
                                && importQuantity.compareTo(originalItem.getQuantityPending()) <= 0) {

                            PurchaseOrderItems importItem = new PurchaseOrderItems();
                            importItem.setId(originalItem.getId());
                            importItem.setPurchaseId(originalItem.getPurchaseId());
                            importItem.setProductName(originalItem.getProductName());
                            importItem.setProductCode(originalItem.getProductCode());
                            importItem.setUnit(originalItem.getUnit());
                            importItem.setQuantity(importQuantity);
                            importItem.setQuantityOrdered(originalItem.getQuantityOrdered());
                            importItem.setPricePerUnit(originalItem.getPricePerUnit());
                            importItem.setNote(originalItem.getNote());

                            importItems.add(importItem);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid quantity format for item " + originalItem.getId() + ": " + quantityParam);
                    }
                }
            }

            if (importItems.isEmpty()) {
                response.sendRedirect("import-confirm?id=" + requestId + "&error=no_valid_items_to_import");
                return;
            }

            // Thực hiện nhập kho
            boolean importSuccess = dao.processPartialImport(requestId, importDate.trim(), processor,
                    additionalNote, importItems);

            if (importSuccess) {
                System.out.println("✅ Đã nhập kho thành công đơn: " + requestId);

                // Cộng tồn kho NGAY bằng bản ghi mới nhất
                boolean stockUpdated = dao.updateStockFromLatestHistory(requestId);

                if (stockUpdated) {
                    System.out.println("✅ Đã cập nhật tồn kho từ lịch sử mới nhất");
                    response.sendRedirect("request/list?message=import_and_stock_success");
                } else {
                    System.err.println("❌ Cập nhật tồn kho thất bại sau khi import");
                    response.sendRedirect("request/list?message=import_success_but_stock_failed");
                }

            } else {
                System.err.println("❌ Import thất bại cho đơn: " + requestId);
                response.sendRedirect("import-confirm?id=" + requestId + "&error=import_failed");
            }

        } else if ("reject".equalsIgnoreCase(action)) {
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