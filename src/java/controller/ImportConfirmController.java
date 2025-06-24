package controller;

import dao.ListRequestImportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.sql.SQLException;
import model.Request;
import model.RequestItem;

public class ImportConfirmController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestId = request.getParameter("id");
        if (requestId == null || requestId.trim().isEmpty()) {
            System.err.println("Invalid requestId: null or empty");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã đơn nhập.");
            return;
        }

        ListRequestImportDAO dao = new ListRequestImportDAO();
        Request req = dao.getRequestById(requestId);
        List<RequestItem> itemList = dao.getRequestItemsByRequestId(requestId);

        if (req == null) {
            System.err.println("Request not found for requestId: " + requestId);
            response.sendRedirect("import?error=request_not_found");
            return;
        }

        // Set current date for default import date
        request.setAttribute("currentDate", java.time.LocalDate.now().toString());
        request.setAttribute("p", req);
        request.setAttribute("itemList", itemList);

        request.getRequestDispatcher("WarehouseManagement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestId = request.getParameter("id");
        String action = request.getParameter("action");
        String importDate = request.getParameter("importDate");
        String warehouse = request.getParameter("warehouse");
        String receiver = request.getParameter("receiver");
        String additionalNote = request.getParameter("additionalNote");

        // Validate parameters
        if (requestId == null || requestId.trim().isEmpty()) {
            System.err.println("Invalid requestId: null or empty");
            response.sendRedirect("import?error=invalid_data&message=Thiếu mã đơn nhập");
            return;
        }
        
        if (action == null || action.trim().isEmpty()) {
            System.err.println("Invalid action: null or empty for requestId: " + requestId);
            response.sendRedirect("import-confirm?id=" + requestId + "&error=invalid_action");
            return;
        }

        ListRequestImportDAO dao = new ListRequestImportDAO();
        List<RequestItem> itemList = dao.getRequestItemsByRequestId(requestId);

        try {
            if ("confirm".equalsIgnoreCase(action)) {
                // Validate required fields for confirmation
                if (importDate == null || importDate.trim().isEmpty() ||
                    warehouse == null || warehouse.trim().isEmpty() ||
                    receiver == null || receiver.trim().isEmpty()) {
                    System.err.println("Missing required fields for confirmation. RequestId: " + requestId);
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=missing_required_fields");
                    return;
                }

                // Validate at least one item quantity
                boolean hasValidQuantity = false;
                for (RequestItem item : itemList) {
                    String code = item.getProductCode();
                    String qtyParam = request.getParameter("importQty_" + code);
                    
                    if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                        int qty = Integer.parseInt(qtyParam);
                        if (qty > 0) {
                            hasValidQuantity = true;
                            break;
                        }
                    }
                }

                if (!hasValidQuantity) {
                    System.err.println("No valid quantities provided for requestId: " + requestId);
                    response.sendRedirect("import-confirm?id=" + requestId + "&error=no_quantities");
                    return;
                }

                // Process import confirmation
                for (RequestItem item : itemList) {
                    String code = item.getProductCode();
                    String qtyParam = request.getParameter("importQty_" + code);
                    String note = request.getParameter("note_" + code);

                    int qty = (qtyParam != null && !qtyParam.trim().isEmpty()) ? Integer.parseInt(qtyParam) : 0;
                    
                    if (qty > 0) {
                        // Update import item with additional info
                        dao.updateImportItemWithDetails(requestId, code, qty, note, warehouse, receiver);
                    }
                }

                // Update request status to completed
                dao.updateRequestStatusWithDetails(requestId, "completed", importDate, warehouse, receiver, additionalNote);
                
                System.out.println("Import confirmed successfully for requestId: " + requestId);
                response.sendRedirect("import?message=approve_success&details=Đã xác nhận nhập kho thành công");

            } else if ("reject".equalsIgnoreCase(action)) {
                // Process rejection - Lấy lý do từ chối từ form hoặc sử dụng lý do mặc định
                String rejectReason = additionalNote;
                if (rejectReason == null || rejectReason.trim().isEmpty()) {
                    rejectReason = "Từ chối nhập kho - Không đáp ứng yêu cầu chất lượng";
                }

                // Update request status to rejected với thông tin chi tiết
                dao.updateRequestStatusToRejected(requestId, "rejected", rejectReason, 
                    java.time.LocalDateTime.now().toString());
                
                // Log rejection
                System.out.println("Import rejected for requestId: " + requestId + ". Reason: " + rejectReason);
                response.sendRedirect("import?message=reject_success&details=Đã từ chối yêu cầu nhập kho: " + rejectReason);

            } else {
                System.err.println("Unknown action: " + action + " for requestId: " + requestId);
                response.sendRedirect("import-confirm?id=" + requestId + "&error=invalid_action");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException in doPost for requestId: " + requestId + ", Action: " + action + ", Error: " + e.getMessage());
            response.sendRedirect("import-confirm?id=" + requestId + "&error=database_error&details=" + e.getMessage());
            
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.err.println("NumberFormatException in doPost for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect("import-confirm?id=" + requestId + "&error=invalid_quantity&details=Số lượng không hợp lệ");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected error in doPost for requestId: " + requestId + ", Error: " + e.getMessage());
            response.sendRedirect("import-confirm?id=" + requestId + "&error=unexpected_error&details=" + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "ImportConfirmController - xử lý xác nhận hoặc từ chối yêu cầu nhập kho với đầy đủ thông tin chi tiết";
    }
}
