/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.GetStatusOfPurchaseRequestInformationDAO;
import dao.PurchaseOrderDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
import model.Users;

/**
 *
 * @author Admin
 */
public class SubmitPurchaseOrderServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SubmitPurchaseOrderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubmitPurchaseOrderServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            // Lấy thông tin cơ bản từ form
            String originalRequestId = request.getParameter("originalRequestId");
            String quoteDate = request.getParameter("quote_date");
            String supplier = request.getParameter("supplier");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String quoteSummary = request.getParameter("quote_summary");

            // Lấy thông tin từ session
            String fullname = (String) session.getAttribute("currentUser");
            Date dob = (Date) session.getAttribute("DoB");

            // Chuyển đổi ngày tạo báo giá
            Date purchaseDate = null;
            if (quoteDate != null && !quoteDate.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                purchaseDate = sdf.parse(quoteDate);
            }

            // Tạo đối tượng PurchaseOrderInfo
            PurchaseOrderInfo purchaseOrder = new PurchaseOrderInfo();
            purchaseOrder.setId(originalRequestId);
            purchaseOrder.setFullname(fullname);
            purchaseOrder.setDoB(dob);
            purchaseOrder.setDayPurchase(purchaseDate);
            purchaseOrder.setStatus("pending"); // Mặc định là pending

            // Lấy reason từ form trước đó (có thể cần lưu trong session hoặc hidden field)
            String reason = request.getParameter("quote_note");
            purchaseOrder.setReason(reason);

            purchaseOrder.setSupplier(supplier);
            purchaseOrder.setAddress(address);
            purchaseOrder.setPhone(phone);
            purchaseOrder.setEmail(email);
            purchaseOrder.setSummary(quoteSummary);

            // Lấy danh sách items từ form
            ArrayList<PurchaseOrderItems> items = new ArrayList<>();

            // Lấy tất cả các parameter name để tìm items
            String[] productNames = request.getParameterValues("product_name");
            String[] productCodes = request.getParameterValues("product_code");
            String[] units = request.getParameterValues("unit");
            String[] quantities = request.getParameterValues("quantity");
            String[] pricesPerUnit = request.getParameterValues("pricePerUnit");
            String[] totalPrices = request.getParameterValues("totalPrice");
            String[] itemNotes = request.getParameterValues("quote_item_note");

            if (productNames != null && productNames.length > 0) {
                for (int i = 0; i < productNames.length; i++) {
                    if (productNames[i] != null && !productNames[i].trim().isEmpty()) {
                        PurchaseOrderItems item = new PurchaseOrderItems();
                        item.setPurchaseId(originalRequestId);
                        item.setProductName(productNames[i]);
                        item.setProductCode(productCodes != null && i < productCodes.length ? productCodes[i] : "");
                        item.setUnit(units != null && i < units.length ? units[i] : "");

                        // Xử lý quantity
                        if (quantities != null && i < quantities.length && quantities[i] != null) {
                            try {
                                String quantityStr = quantities[i].replace(",", ".");
                                item.setQuantity(new BigDecimal(quantityStr));
                            } catch (NumberFormatException e) {
                                item.setQuantity(BigDecimal.ZERO);
                            }
                        } else {
                            item.setQuantity(BigDecimal.ZERO);
                        }

                        // Xử lý price per unit
                        if (pricesPerUnit != null && i < pricesPerUnit.length && pricesPerUnit[i] != null) {
                            try {
                                String priceStr = pricesPerUnit[i].replaceAll("[^0-9,.]", "").replace(",", ".");
                                item.setPricePerUnit(new BigDecimal(priceStr));
                            } catch (NumberFormatException e) {
                                item.setPricePerUnit(BigDecimal.ZERO);
                            }
                        } else {
                            item.setPricePerUnit(BigDecimal.ZERO);
                        }

                        // Xử lý total price
                        if (totalPrices != null && i < totalPrices.length && totalPrices[i] != null) {
                            try {
                                String totalStr = totalPrices[i].replaceAll("[^0-9,.]", "").replace(",", ".");
                                item.setTotalPrice(new BigDecimal(totalStr));
                            } catch (NumberFormatException e) {
                                // Tính lại total price nếu parse lỗi
                                item.setTotalPrice(item.getQuantity().multiply(item.getPricePerUnit()));
                            }
                        } else {
                            // Tính total price nếu không có
                            item.setTotalPrice(item.getQuantity().multiply(item.getPricePerUnit()));
                        }

                        // Xử lý note
                        item.setNote(itemNotes != null && i < itemNotes.length ? itemNotes[i] : "");

                        items.add(item);
                    }
                }
            }

            purchaseOrder.setPurchaseItems(items);

            // Insert vào database
            PurchaseOrderDAO dao = new PurchaseOrderDAO();
            boolean success = dao.insertPurchaseOrder(purchaseOrder);

            if (success) {
                // Cập nhật status của Purchase Request gốc
                GetStatusOfPurchaseRequestInformationDAO requestDAO = new GetStatusOfPurchaseRequestInformationDAO();
                boolean updateSuccess = requestDAO.updateQuotedStatus(originalRequestId);

                if (updateSuccess) {
                    // ✅ Cả 2 thao tác đều thành công
                    session.setAttribute("successMessage", "Đơn báo giá đã được tạo thành công với ID: " + originalRequestId);
                    response.sendRedirect("QuoteSuccessNotification.jsp");
                } else {
                    // ✅ Tạo báo giá thành công nhưng không cập nhật được status
                    request.setAttribute("errorMessage", "Tạo báo giá thành công nhưng không thể cập nhật trạng thái request.");
                    request.getRequestDispatcher("PurchaseOrderForm.jsp").forward(request, response);
                }
            } else {
                // ✅ Tạo báo giá thất bại
                request.setAttribute("errorMessage", "Không thể tạo đơn báo giá. Vui lòng thử lại.");
                request.getRequestDispatcher("PurchaseOrderForm.jsp").forward(request, response);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ. Vui lòng kiểm tra lại.");
            request.getRequestDispatcher("PurchaseOrderForm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Định dạng số không hợp lệ. Vui lòng kiểm tra lại giá và số lượng.");
            request.getRequestDispatcher("PurchaseOrderForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi không mong muốn xảy ra: " + e.getMessage());
            request.getRequestDispatcher("PurchaseOrderForm.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý submit đơn báo giá";
    }
}
