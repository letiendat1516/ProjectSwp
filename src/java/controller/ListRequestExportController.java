package controller;

import dao.ListRequestExportDAO;
import dao.ExportDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import model.ExportRequestItem;
import model.Users;

public class ListRequestExportController extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            ListRequestExportDAO dao = new ListRequestExportDAO();

            // Lấy tham số tab
            String tab = request.getParameter("tab");
            if (tab == null || tab.isEmpty()) {
                tab = "approved";
            }

            // Lấy tham số page
            int approvedPage = 1;
            int historyPage = 1;

            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    if ("approved".equals(tab)) {
                        approvedPage = Integer.parseInt(pageParam);
                        if (approvedPage < 1) approvedPage = 1;
                    } else if ("history".equals(tab)) {
                        historyPage = Integer.parseInt(pageParam);
                        if (historyPage < 1) historyPage = 1;
                    }
                }
            } catch (NumberFormatException e) {
                // Giữ nguyên giá trị mặc định
            }

            // Khởi tạo danh sách
            List<ExportRequestItem> approvedItems = new ArrayList<>();
            List<ExportRequestItem> historyItems = new ArrayList<>();
            int approvedPages = 0;
            int historyPages = 0;

            // Lấy tổng số để hiển thị thống kê
            int totalApproved = dao.countApprovedExportItems(null, null);
            int totalHistory = dao.countCompletedExportItems(null, null);

            if ("approved".equals(tab)) {
                // Load dữ liệu cho tab approved
                String searchType = request.getParameter("searchType");
                String searchValue = request.getParameter("searchValue");

                approvedItems = dao.getApprovedExportItems(searchType, searchValue, approvedPage, PAGE_SIZE);
                int approvedTotal = dao.countApprovedExportItems(searchType, searchValue);
                approvedPages = (int) Math.ceil((double) approvedTotal / PAGE_SIZE);

                // Set search parameters
                request.setAttribute("searchType", searchType);
                request.setAttribute("searchValue", searchValue);
                request.setAttribute("approvedTotal", approvedTotal);

            } else if ("history".equals(tab)) {
                // Load dữ liệu cho tab history
                String historySearchType = request.getParameter("historySearchType");
                String historySearchValue = request.getParameter("historySearchValue");

                historyItems = dao.getCompletedExportItems(historySearchType, historySearchValue, historyPage, PAGE_SIZE);
                int historyTotal = dao.countCompletedExportItems(historySearchType, historySearchValue);
                historyPages = (int) Math.ceil((double) historyTotal / PAGE_SIZE);

                // Set search parameters
                request.setAttribute("historySearchType", historySearchType);
                request.setAttribute("historySearchValue", historySearchValue);
                request.setAttribute("historyTotal", historyTotal);
            }

            // Set attributes
            request.setAttribute("approvedItems", approvedItems);
            request.setAttribute("historyItems", historyItems);
            request.setAttribute("currentTab", tab);
            request.setAttribute("approvedPages", approvedPages);
            request.setAttribute("historyPages", historyPages);
            request.setAttribute("approvedPage", approvedPage);
            request.setAttribute("historyPage", historyPage);

            // Thống kê tổng
            request.setAttribute("approvedCount", totalApproved);
            request.setAttribute("historyCount", totalHistory);
            request.setAttribute("totalCount", totalApproved + totalHistory);

            request.setAttribute("pageSize", PAGE_SIZE);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("approvedItems", new ArrayList<>());
            request.setAttribute("historyItems", new ArrayList<>());
            request.setAttribute("error", "Có lỗi xảy ra khi tải dữ liệu");
        }

        request.getRequestDispatcher("/ListRequestExport.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Redirect tất cả POST requests về GET để tránh xung đột
        // Các thao tác export sẽ được xử lý bởi ExportConfirmController
        doGet(request, response);
    }
}