/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.WarehouseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ProductInfo;
import model.Request;
import model.RequestItem;
import model.Supplier;

public class WarehouseController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet WarehouseController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet WarehouseController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    private WarehouseDAO warehouseDAO;

    @Override
    public void init() throws ServletException {
        warehouseDAO = new WarehouseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get request_id from parameter
            String requestIdStr = request.getParameter("request_id");
            int requestId = requestIdStr != null ? Integer.parseInt(requestIdStr) : 1; // Default for testing

            // Fetch request details
            Request req = warehouseDAO.getRequestById(requestId);
            if (req == null) {
                request.setAttribute("error", "Request not found!");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Fetch request items and corresponding product info
            List<RequestItem> items = warehouseDAO.getRequestItems(requestId);
            Map<RequestItem, ProductInfo> itemProductMap = new HashMap<>();
            for (RequestItem item : items) {
                ProductInfo product = warehouseDAO.getProductInfo(item.getProductId());
                if (product != null) {
                    itemProductMap.put(item, product);
                }
            }

            // Fetch supplier (assuming supplier_id is linked to request; hardcoding for now)
            Supplier supplier = warehouseDAO.getSupplier(1); // Replace with actual logic

            // Set attributes
            request.setAttribute("request", req);
            request.setAttribute("itemProductMap", itemProductMap);
            request.setAttribute("supplier", supplier);

            // Forward to JSP
            request.getRequestDispatcher("/WarehouseManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
