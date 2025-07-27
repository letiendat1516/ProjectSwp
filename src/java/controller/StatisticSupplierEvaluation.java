/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.StatisticExportDAO;
import dao.StatisticPurchaseDAO;
import dao.SupplierEvaluationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import model.StatisticExport;
import model.StatisticPurchase;
import model.Supplier;
import model.SupplierEvaluation;
import model.StatisticExport;

/**
 *
 * @author Fpt06
 */
@WebServlet(name = "StatisticSupplierEvaluation", urlPatterns = {"/StatisticSupplierEvaluation"})
public class StatisticSupplierEvaluation extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StatisticSupplierEvaluation</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StatisticSupplierEvaluation at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        String categoruStatistic = request.getParameter("luaChonValue");

        if (categoruStatistic.equals("third")) {
            String sort = request.getParameter("sort");
            String status = request.getParameter("status");
            SupplierEvaluationDAO sed = new SupplierEvaluationDAO();
            request.setAttribute("sta", status);
            String index_raw = request.getParameter("index");

            int index = 1;
            if (index_raw != null) {
                index = Integer.parseInt(index_raw);
            }

            List<Supplier> list = sed.staticRated(sort);
            if (status.equalsIgnoreCase("active")) {
                list = list.stream().filter((c) -> c.getActiveFlag() == 1).collect(Collectors.toList());
            } else if (status.equalsIgnoreCase("inactive")) {
                list = list.stream().filter((c) -> c.getActiveFlag() == 0).collect(Collectors.toList());
            }
            //phan trang
            int totalPage = (int) Math.ceil((double) list.size() / 5);
            if (index < totalPage) {
                list = list.subList((index - 1) * 5, index * 5);
            }
            if (index == totalPage) {
                list = list.subList((index - 1) * 5, list.size());
            }
            request.setAttribute("totalPage", totalPage);
            //
            List<Float> listStar = new ArrayList<>();
            List<Integer> listComment = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                List<SupplierEvaluation> lse = sed.getSupplierEvaluationByID(list.get(i).getSupplierID());
                float star = 0;
                for (int j = 0; j < lse.size(); j++) {
                    star = star + lse.get(j).getAvgRate();
                }
                listComment.add(lse.size());
                star = star / lse.size();
                star = Math.round(star * 10) / 10.0f;
                listStar.add(star);
            }

            // giao hàng nhanh
            List<Supplier> list11 = sed.staticExpectedDelivery("desc");
            List<Float> listStar11 = new ArrayList<>();
            if (list11.size() > 5) {
                list11 = list11.subList(0, 5);
            }
            for (int i = 0; i < list.size(); i++) {
                List<SupplierEvaluation> lse = sed.getSupplierEvaluationByID(list.get(i).getSupplierID());
                float star = 0;
                for (int j = 0; j < lse.size(); j++) {
                    star = star + lse.get(j).getExpectedDeliveryTime();
                }
                star = star / (lse.size() * 3);
                star = Math.round(star * 10) / 10.0f;
                listStar11.add(star);
            }
            request.setAttribute("listStar11", listStar11);
            request.setAttribute("list11", list11);
            // Được đánh giá có giá rẻ hơn
            List<Supplier> list22 = sed.staticMarketPrice("desc");
            List<Float> listStar22 = new ArrayList<>();
            if (list22.size() > 5) {
                list22 = list22.subList(0, 5);
            }
            for (int i = 0; i < list.size(); i++) {
                List<SupplierEvaluation> lse = sed.getSupplierEvaluationByID(list.get(i).getSupplierID());
                float star = 0;
                for (int j = 0; j < lse.size(); j++) {
                    star = star + lse.get(j).getExpectedDeliveryTime();
                }
                star = star / (lse.size() * 3);
                star = Math.round(star * 10) / 10.0f;
                listStar22.add(star);
            }
            request.setAttribute("listStar22", listStar22);
            request.setAttribute("list22", list22);
            request.setAttribute("choice", categoruStatistic);
            request.setAttribute("listComment", listComment);
            request.setAttribute("listStar", listStar);
            request.setAttribute("list", list);
            request.setAttribute("st", sort);
            request.setAttribute("mess", "Top-rated Supplier");
            request.getRequestDispatcher("StatisticSupplierEvaluation.jsp").forward(request, response);
        }
        if (categoruStatistic.equals("first")) {
            String fromDay_raw = request.getParameter("fromDay");
            String toDay_raw = request.getParameter("toDay");
            String index_raw = request.getParameter("index1");

            int index = 1;
            if (index_raw != null) {
                index = Integer.parseInt(index_raw);
            }

            if (fromDay_raw == null || toDay_raw == null || fromDay_raw.trim().isEmpty() || toDay_raw.trim().isEmpty()) {
                request.setAttribute("choice", "first");
                request.getRequestDispatcher("StatisticSupplierEvaluation.jsp").forward(request, response);
                return;
            }

            try {

                Date fromDay = Date.valueOf(fromDay_raw);
                Date toDay = Date.valueOf(toDay_raw);

                StatisticPurchaseDAO spd = new StatisticPurchaseDAO();
                List<StatisticPurchase> list2 = spd.getStatistic(fromDay, toDay);

                int x = (int) list2.stream().map(StatisticPurchase::getProductName).distinct().count();
                int totalPurchase = list2.size();
                double totalPrice = list2.stream().mapToDouble(sp -> sp.getPricePerUnit() * sp.getQuantity()).sum();
                int totalPage = (int) Math.ceil((double) list2.size() / 5);
                if (index < totalPage) {
                    list2 = list2.subList((index - 1) * 5, index * 5);
                }
                if (index == totalPage) {
                    list2 = list2.subList((index - 1) * 5, list2.size());
                }
                
                StatisticExportDAO sed = new StatisticExportDAO();
                List<StatisticExport> list21 = sed.getStatic(fromDay, toDay);
                int totalExport = list21.size();
                request.setAttribute("list21", list21);
                request.setAttribute("totalPage1", totalPage);
                request.setAttribute("list2", list2);
                request.setAttribute("fromdate", fromDay);
                request.setAttribute("todate", toDay);
                request.setAttribute("totalProduct", x);
                request.setAttribute("totalPurchase", totalPurchase);
                request.setAttribute("totalExport", totalExport);
                request.setAttribute("totalPrice", totalPrice);
                request.setAttribute("choice", categoruStatistic);
                
                List<StatisticExport> topExport = sed.topExport(fromDay, toDay);
                topExport.sort(Comparator.comparing(StatisticExport::getQuantity).reversed());
                List<StatisticExport> topImport = sed.topImport(fromDay, toDay);
                topImport.sort(Comparator.comparing(StatisticExport::getQuantity).reversed());
                request.setAttribute("topExport", topExport);
                request.setAttribute("topImport", topImport);
                request.getRequestDispatcher("StatisticSupplierEvaluation.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
                request.setAttribute("choice", "first");
                request.getRequestDispatcher("StatisticSupplierEvaluation.jsp").forward(request, response);
            }
        }

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
        return "Short description";
    }// </editor-fold>

}
