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
          response.sendRedirect("exportRequest?action=list&error=missing_id");
          return;
      }

      // Kiểm tra đơn hàng có thể xử lý không
      if (!dao.isOrderProcessable(requestId)) {
          response.sendRedirect("exportRequest?action=list&error=order_not_processable");
          return;
      }

      // Khởi tạo pending items nếu chưa có
      System.out.println("Initializing pending items for request: " + requestId);
      boolean initialized = dao.initializeExportPendingItems(requestId);
      System.out.println("Pending items initialization result: " + initialized);

      // Lấy thông tin đơn hàng
      ExportRequest exportRequest = dao.getExportRequestById(requestId);
      if (exportRequest == null) {
          response.sendRedirect("exportRequest?action=list&error=order_not_found");
          return;
      }

      // Lấy danh sách items với thông tin xuất kho
      List<ExportRequestItem> itemList = dao.getExportRequestItemsByOrderId(requestId);
      System.out.println("Loaded items count: " + itemList.size());

      // Debug: In ra thông tin các items
      for (ExportRequestItem item : itemList) {
          System.out.println("Item: " + item.getProductName() + 
                           " - Requested: " + item.getQuantityRequested() + 
                           " - Exported: " + item.getQuantityExported() + 
                           " - Pending: " + item.getQuantityPending());
      }

      // Lấy lịch sử xuất kho (nếu có)
      List<Object[]> exportHistory = dao.getExportHistory(requestId);
      
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

      ExportDAO dao = new ExportDAO();
      HttpSession session = request.getSession();

      String requestId = request.getParameter("id");
      String action = request.getParameter("action");

      // Validate basic parameters
      if (requestId == null || requestId.trim().isEmpty() || action == null) {
          response.sendRedirect("exportRequest?action=list&error=invalid_data");
          return;
      }

      // Kiểm tra đơn hàng có thể xử lý không
      if (!dao.isOrderProcessable(requestId)) {
          response.sendRedirect("exportRequest?action=list&error=order_not_processable");
          return;
      }

      try {
          if ("confirm".equalsIgnoreCase(action)) {
              // Xử lý xác nhận xuất kho từng phần
              String exportDate = request.getParameter("exportDate");
              String additionalNote = request.getParameter("additionalNote");

              // Validate required fields
              if (exportDate == null || exportDate.trim().isEmpty()) {
                  response.sendRedirect("export?id=" + requestId + "&error=missing_required_fields");
                  return;
              }

              // Lấy thông tin người xử lý từ session
              String processor = "Unknown";
              Users user = (Users) session.getAttribute("user");
              if (user != null) {
                  processor = user.getFullname() != null ? user.getFullname() : user.getUsername();
              }

              // Lấy danh sách items để xuất kho
              List<ExportRequestItem> exportItems = new ArrayList<>();

              // Lấy danh sách items gốc
              List<ExportRequestItem> originalItems = dao.getExportRequestItemsByOrderId(requestId);

              for (ExportRequestItem originalItem : originalItems) {
                  String quantityParam = request.getParameter("export_quantity_" + originalItem.getId());

                  if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                      try {
                          // Chỉ chấp nhận số nguyên
                          int exportQuantityInt = Integer.parseInt(quantityParam.trim());

                          // Kiểm tra số nguyên dương
                          if (exportQuantityInt <= 0) {
                              System.err.println("Invalid quantity (not positive integer) for item " + originalItem.getId());
                              continue;
                          }

                          double exportQuantity = exportQuantityInt;

                          // Kiểm tra số lượng hợp lệ
                          if (exportQuantity > 0 && exportQuantity <= originalItem.getQuantityPending()) {

                              // Kiểm tra tồn kho
                              if (!dao.checkInventoryAvailability(originalItem.getProductCode(), exportQuantity)) {
                                  response.sendRedirect("export?id=" + requestId + "&error=insufficient_inventory&product=" + originalItem.getProductCode());
                                  return;
                              }

                              // Tạo item để xuất kho với exportQuantity
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
                              
                              // QUAN TRỌNG: Set exportQuantity thay vì quantity
                              exportItem.setExportQuantity(exportQuantity); // Số lượng xuất lần này
                              exportItem.setQuantity(originalItem.getQuantityRequested()); // Giữ nguyên số lượng yêu cầu

                              exportItems.add(exportItem);
                              
                              System.out.println("Added export item: " + exportItem.getProductName() + 
                                               " - Export quantity: " + exportItem.getExportQuantity());
                          } else {
                              System.err.println("Quantity exceeds pending amount for item " + originalItem.getId() + 
                                               " - Export: " + exportQuantity + ", Pending: " + originalItem.getQuantityPending());
                          }
                      } catch (NumberFormatException e) {
                          System.err.println("Invalid integer quantity format for item " + originalItem.getId() + ": " + quantityParam);
                      }
                  }
              }

              // Kiểm tra có item nào để xuất không
              if (exportItems.isEmpty()) {
                  response.sendRedirect("export?id=" + requestId + "&error=no_valid_items_to_export");
                  return;
              }

              System.out.println("Total export items to process: " + exportItems.size());

              // Xử lý xuất kho từng phần
              boolean success = dao.processPartialExport(requestId, exportDate.trim(), processor,
                      additionalNote, exportItems);

              if (success) {
                  response.sendRedirect("exportRequest?action=list&message=export_success");
              } else {
                  response.sendRedirect("export?id=" + requestId + "&error=export_failed");
              }

          } else if ("reject".equalsIgnoreCase(action)) {
              // Xử lý từ chối
              String rejectReason = request.getParameter("rejectReason");

              if (rejectReason == null || rejectReason.trim().isEmpty()) {
                  response.sendRedirect("export?id=" + requestId + "&error=reject_reason_required");
                  return;
              }

              boolean updated = dao.updateRequestStatusToRejected(requestId, rejectReason.trim());

              if (updated) {
                  response.sendRedirect("exportRequest?action=list&message=reject_success");
              } else {
                  response.sendRedirect("export?id=" + requestId + "&error=reject_failed");
              }

          } else {
              response.sendRedirect("export?id=" + requestId + "&error=invalid_action");
          }

      } catch (Exception e) {
          e.printStackTrace();
          System.err.println("Error processing export for requestId: " + requestId + ", Error: " + e.getMessage());
          response.sendRedirect("export?id=" + requestId + "&error=processing_failed");
      }
  }
}