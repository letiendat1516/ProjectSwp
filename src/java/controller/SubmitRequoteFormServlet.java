/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PurchaseQuotedDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.PurchaseOrderInfo;
import model.PurchaseOrderItems;

/**
 * Servlet xử lý việc báo giá lại cho đơn đã tồn tại
 * @author Admin
 */
public class SubmitRequoteFormServlet extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
   * methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      
      // Kiểm tra session
      HttpSession session = request.getSession(false);
      if (session == null || session.getAttribute("user") == null) {
          response.sendRedirect("login.jsp");
          return;
      }

      try {
          // Lấy thông tin cơ bản từ form
          String originalRequestId = request.getParameter("originalRequestId");
          String quoteDate = request.getParameter("quote_date");
          String supplier = request.getParameter("supplier_name");
          String address = request.getParameter("supplier_address");
          String phone = request.getParameter("supplier_phone");
          String email = request.getParameter("supplier_email");
          String quoteSummary = request.getParameter("quote_summary");

          // Validate required fields
          if (originalRequestId == null || originalRequestId.trim().isEmpty()) {
              request.setAttribute("errorMessage", "ID đơn báo giá không hợp lệ.");
              request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
              return;
          }

          if (supplier == null || supplier.trim().isEmpty()) {
              request.setAttribute("errorMessage", "Vui lòng chọn nhà cung cấp.");
              request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
              return;
          }

          if (quoteSummary == null || quoteSummary.trim().isEmpty()) {
              request.setAttribute("errorMessage", "Vui lòng nhập tổng kết báo giá.");
              request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
              return;
          }

          // Lấy thông tin từ session
          String fullname = (String) session.getAttribute("currentUser");
          Date dob = (Date) session.getAttribute("DoB");

          // Chuyển đổi ngày tạo báo giá
          Date purchaseDate = null;
          if (quoteDate != null && !quoteDate.isEmpty()) {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
              purchaseDate = sdf.parse(quoteDate);
          } else {
              // Nếu không có ngày, sử dụng ngày hiện tại
              purchaseDate = new Date();
          }

          // Tạo đối tượng PurchaseOrderInfo để cập nhật
          PurchaseOrderInfo purchaseOrder = new PurchaseOrderInfo();
          purchaseOrder.setId(originalRequestId);
          purchaseOrder.setFullname(fullname);
  //        purchaseOrder.setDoB(dob);
          purchaseOrder.setDayPurchase(purchaseDate);
          // Status sẽ được set thành "pending" trong DAO

          // Lấy reason từ form
          String reason = request.getParameter("quote_note");
          purchaseOrder.setReason(reason);

          purchaseOrder.setSupplier(supplier);
          purchaseOrder.setAddress(address);
          purchaseOrder.setPhone(phone);
          purchaseOrder.setEmail(email);
          purchaseOrder.setSummary(quoteSummary);

          // Lấy danh sách items từ form
          ArrayList<PurchaseOrderItems> items = new ArrayList<>();

          // Lấy tất cả các parameter arrays
          String[] productNames = request.getParameterValues("product_name");
          String[] productCodes = request.getParameterValues("product_code");
          String[] units = request.getParameterValues("unit");
          String[] quantities = request.getParameterValues("quantity");
          String[] pricesPerUnit = request.getParameterValues("pricePerUnit");
          String[] totalPrices = request.getParameterValues("totalPrice");
          String[] itemNotes = request.getParameterValues("quote_item_note");

          // Validate items data
          if (productNames == null || productNames.length == 0) {
              request.setAttribute("errorMessage", "Không có sản phẩm nào để báo giá.");
              request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
              return;
          }

          // Kiểm tra xem có ít nhất một item có giá hợp lệ không
          boolean hasValidPrice = false;

          // Xử lý từng item
          for (int i = 0; i < productNames.length; i++) {
              if (productNames[i] != null && !productNames[i].trim().isEmpty()) {
                  PurchaseOrderItems item = new PurchaseOrderItems();
                  item.setPurchaseId(originalRequestId);
                  item.setProductName(productNames[i].trim());
                  item.setProductCode(productCodes != null && i < productCodes.length ? 
                                     productCodes[i].trim() : "");
                  item.setUnit(units != null && i < units.length ? units[i].trim() : "");

                  // Xử lý quantity
                  BigDecimal quantity = BigDecimal.ZERO;
                  if (quantities != null && i < quantities.length && 
                      quantities[i] != null && !quantities[i].trim().isEmpty()) {
                      try {
                          String quantityStr = quantities[i].replace(",", ".").trim();
                          quantity = new BigDecimal(quantityStr);
                          if (quantity.compareTo(BigDecimal.ZERO) < 0) {
                              quantity = BigDecimal.ZERO;
                          }
                      } catch (NumberFormatException e) {
                          System.err.println("Invalid quantity format for item " + i + ": " + quantities[i]);
                          quantity = BigDecimal.ZERO;
                      }
                  }
                  item.setQuantity(quantity);

                  // Xử lý price per unit
                  BigDecimal pricePerUnit = BigDecimal.ZERO;
                  if (pricesPerUnit != null && i < pricesPerUnit.length && 
                      pricesPerUnit[i] != null && !pricesPerUnit[i].trim().isEmpty()) {
                      try {
                          String priceStr = pricesPerUnit[i]
                              .replaceAll("[^0-9,.]", "")
                              .replace(",", ".")
                              .trim();
                          if (!priceStr.isEmpty()) {
                              pricePerUnit = new BigDecimal(priceStr);
                              if (pricePerUnit.compareTo(BigDecimal.ZERO) > 0) {
                                  hasValidPrice = true;
                              }
                              if (pricePerUnit.compareTo(BigDecimal.ZERO) < 0) {
                                  pricePerUnit = BigDecimal.ZERO;
                              }
                          }
                      } catch (NumberFormatException e) {
                          System.err.println("Invalid price format for item " + i + ": " + pricesPerUnit[i]);
                          pricePerUnit = BigDecimal.ZERO;
                      }
                  }
                  item.setPricePerUnit(pricePerUnit);

                  // Tính toán total price
                  BigDecimal totalPrice = quantity.multiply(pricePerUnit);
                  
                  // Nếu có total price từ form, so sánh và sử dụng giá trị chính xác hơn
                  if (totalPrices != null && i < totalPrices.length && 
                      totalPrices[i] != null && !totalPrices[i].trim().isEmpty()) {
                      try {
                          String totalStr = totalPrices[i]
                              .replaceAll("[^0-9,.]", "")
                              .replace(",", ".")
                              .trim();
                          if (!totalStr.isEmpty()) {
                              BigDecimal formTotalPrice = new BigDecimal(totalStr);
                              // Sử dụng giá trị từ form nếu hợp lý
                              if (formTotalPrice.compareTo(BigDecimal.ZERO) >= 0) {
                                  totalPrice = formTotalPrice;
                              }
                          }
                      } catch (NumberFormatException e) {
                          System.err.println("Invalid total price format for item " + i + ": " + totalPrices[i]);
                          // Sử dụng giá trị đã tính toán
                      }
                  }
                  item.setTotalPrice(totalPrice);

                  // Xử lý note
                  String note = "";
                  if (itemNotes != null && i < itemNotes.length && itemNotes[i] != null) {
                      note = itemNotes[i].trim();
                  }
                  item.setNote(note);

                  items.add(item);
              }
          }

          // Validate rằng có ít nhất một item có giá hợp lệ
          if (!hasValidPrice) {
              request.setAttribute("errorMessage", "Vui lòng nhập giá cho ít nhất một sản phẩm.");
              request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
              return;
          }

          purchaseOrder.setPurchaseItems(items);

          // Cập nhật Purchase Order trong database
          PurchaseQuotedDAO dao = new PurchaseQuotedDAO();
          
          // Cập nhật thông tin Purchase Order
          boolean updateSuccess = dao.updatePurchaseQuoted(purchaseOrder);

          if (updateSuccess) {
              // Cập nhật status thành "pending" và đồng bộ với Request
              boolean statusUpdateSuccess = dao.updateQuoteExistStatus(originalRequestId);

              if (statusUpdateSuccess) {
                  // ✅ Cả 2 thao tác đều thành công
                  session.setAttribute("successMessage", 
                      "Đơn báo giá đã được cập nhật thành công với ID: " + originalRequestId);
                  System.out.println("✅ Requote completed successfully for: " + originalRequestId);
                  response.sendRedirect("QuoteSuccessNotification.jsp");
              } else {
                  // ⚠️ Cập nhật thông tin thành công nhưng không cập nhật được status
                  System.err.println("⚠️ Updated purchase order but failed to update status for: " + originalRequestId);
                  request.setAttribute("errorMessage", 
                      "Cập nhật thông tin báo giá thành công nhưng không thể cập nhật trạng thái. Vui lòng liên hệ quản trị viên.");
                  request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
              }
          } else {
              // ❌ Cập nhật thất bại
              System.err.println("❌ Failed to update purchase order: " + originalRequestId);
              request.setAttribute("errorMessage", 
                  "Không thể cập nhật đơn báo giá. Vui lòng kiểm tra lại thông tin và thử lại.");
              request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
          }

      } catch (ParseException e) {
          System.err.println("Date parsing error: " + e.getMessage());
          e.printStackTrace();
          request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng kiểm tra lại.");
          request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
      } catch (NumberFormatException e) {
          System.err.println("Number format error: " + e.getMessage());
          e.printStackTrace();
          request.setAttribute("errorMessage", "Định dạng số không hợp lệ. Vui lòng kiểm tra lại giá và số lượng.");
          request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
      } catch (Exception e) {
          System.err.println("Unexpected error in SubmitRequoteFormServlet: " + e.getMessage());
          e.printStackTrace();
          request.setAttribute("errorMessage", "Có lỗi không mong muốn xảy ra: " + e.getMessage());
          request.getRequestDispatcher("RequoteForm.jsp").forward(request, response);
      }
  }

  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
      // Redirect GET requests to POST
      response.sendRedirect("listpurchaseorder");
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
      processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
      return "Servlet xử lý việc báo giá lại cho đơn đã tồn tại";
  }
}