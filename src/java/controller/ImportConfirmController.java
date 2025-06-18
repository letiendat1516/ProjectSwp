/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ListRequestImportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Request;
import model.RequestItem;

public class ImportConfirmController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestId = request.getParameter("id");
        if (requestId == null || requestId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã đơn nhập.");
            return;
        }

        ListRequestImportDAO dao = new ListRequestImportDAO();
        Request req = dao.getRequestById(requestId);
        List<RequestItem> itemList = dao.getRequestItemsByRequestId(requestId);

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
        String receiver = request.getParameter("receiver");
        String warehouse = request.getParameter("warehouse");

        ListRequestImportDAO dao = new ListRequestImportDAO();
        List<RequestItem> itemList = dao.getRequestItemsByRequestId(requestId);

        try {
            if ("confirm".equalsIgnoreCase(action)) {
                for (RequestItem item : itemList) {
                    String code = item.getProductCode();
                    String qtyParam = request.getParameter("importQty_" + code);
                    String note = request.getParameter("note_" + code);

                    int qty = (qtyParam != null && !qtyParam.isEmpty()) ? Integer.parseInt(qtyParam) : 0;
                    dao.updateImportItem(requestId, code, qty, note);
                }

                dao.updateRequestStatus(requestId, "completed", importDate, receiver, warehouse);
                response.sendRedirect("import");

            } else if ("reject".equalsIgnoreCase(action)) {
                dao.updateRequestStatus(requestId, "rejected", null, null, null);
                response.sendRedirect("import");
            } else {
                response.sendRedirect("import-confirm?id=" + requestId + "&error=invalid_action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("import-confirm?id=" + requestId + "&error=processing_failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "ImportConfirmController - xử lý xác nhận hoặc từ chối yêu cầu nhập kho";
    }
}
