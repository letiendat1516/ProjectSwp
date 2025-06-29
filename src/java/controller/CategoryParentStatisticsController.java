package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.CategoryParentStatisticsDAO;
import model.CategoryProductParent;
import model.Users;
import com.google.gson.Gson;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CategoryParentStatisticsController extends HttpServlet {
    
    private CategoryParentStatisticsDAO statisticsDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        System.out.println("=== CategoryParentStatisticsController INIT ===");
        statisticsDAO = new CategoryParentStatisticsDAO();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("=== CategoryParentStatisticsController doGet called ===");
        System.out.println("Request method: " + request.getMethod());
        System.out.println("Action parameter: " + request.getParameter("action"));
        
        // Check authentication
        Users user = (Users) request.getSession().getAttribute("user");
        if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
            System.out.println("User not authenticated, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Set encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            System.out.println("Loading statistics data...");
            
            // Check if this is an AJAX request for filtered data
            String action = request.getParameter("action");
            
            if ("filter".equals(action)) {
                System.out.println("Processing filter request...");
                handleFilterRequest(request, response);
                return;
            }
            
            // Load all statistics data for initial page load
            loadStatisticsData(request);
            
            // Forward to JSP at root level
            System.out.println("Forwarding to: /category-parent-statistics.jsp");
            request.getRequestDispatcher("/category-parent-statistics.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("ERROR in doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải dữ liệu thống kê: " + e.getMessage());
            request.getRequestDispatcher("/category-parent-statistics.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("=== CategoryParentStatisticsController doPost called ===");
        
        // Check if this is an AJAX filter request
        String action = request.getParameter("action");
        if ("filter".equals(action)) {
            // Check authentication first
            Users user = (Users) request.getSession().getAttribute("user");
            if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "Phiên làm việc đã hết hạn");
                response.getWriter().write(gson.toJson(result));
                return;
            }
            
            handleFilterRequest(request, response);
            return;
        }
        
        // For other POST requests, delegate to doGet
        doGet(request, response);
    }
    
    /**
     * Handle AJAX filter requests
     */
    private void handleFilterRequest(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Set response type and encoding FIRST
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String status = request.getParameter("status");
            
            System.out.println("Filter request - Start: " + startDate + ", End: " + endDate + ", Status: " + status);
            
            // Validate dates
            if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                
                if (start.isAfter(end)) {
                    result.put("success", false);
                    result.put("message", "Ngày bắt đầu phải trước ngày kết thúc");
                } else {
                    // Get filtered statistics using new DAO method
                    Map<String, Object> filteredStats = statisticsDAO.getFilteredStatistics(status, startDate, endDate);
                    
                    if (filteredStats == null) {
                        filteredStats = new HashMap<>();
                        filteredStats.put("totalParents", 0);
                        filteredStats.put("activeParents", 0);
                        filteredStats.put("inactiveParents", 0);
                    }
                    
                    result.put("success", true);
                    result.put("data", filteredStats);
                    result.put("message", "Đã lọc dữ liệu thành công. Tìm thấy " + 
                              filteredStats.getOrDefault("totalParents", 0) + " danh mục trong khoảng thời gian này.");
                    
                    System.out.println("Filter result: " + filteredStats);
                }
            } else {
                result.put("success", false);
                result.put("message", "Vui lòng chọn khoảng thời gian");
            }
            
        } catch (Exception e) {
            System.out.println("Error in handleFilterRequest: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
        
        // Write response
        String jsonResponse = gson.toJson(result);
        System.out.println("Sending JSON response: " + jsonResponse);
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
    
    /**
     * Load all statistics data
     */
    private void loadStatisticsData(HttpServletRequest request) {
        try {
            System.out.println("Getting statistics from DAO...");
            
            // 1. Get overall statistics using new method
            Map<String, Object> overallStats = statisticsDAO.getOverallStatistics();
            
            // Extract individual values
            int totalParents = (int) overallStats.getOrDefault("totalParents", 0);
            int activeParents = (int) overallStats.getOrDefault("activeParents", 0);
            int inactiveParents = (int) overallStats.getOrDefault("inactiveParents", 0);
            double activePercentage = (double) overallStats.getOrDefault("activePercentage", 0.0);
            double inactivePercentage = (double) overallStats.getOrDefault("inactivePercentage", 0.0);
            int monthlyDifference = (int) overallStats.getOrDefault("monthlyDifference", 0);
            CategoryProductParent topParent = (CategoryProductParent) overallStats.get("topParent");
            
            // Create stats map for JSP compatibility
            Map<String, Integer> stats = new HashMap<>();
            stats.put("totalParents", totalParents);
            stats.put("activeParents", activeParents);
            stats.put("inactiveParents", inactiveParents);
            
            request.setAttribute("stats", stats);
            request.setAttribute("topParent", topParent);
            request.setAttribute("activePercentage", String.format("%.1f", activePercentage));
            request.setAttribute("inactivePercentage", String.format("%.1f", inactivePercentage));
            request.setAttribute("monthlyDifference", Math.abs(monthlyDifference));
            request.setAttribute("monthlyDifferenceSign", monthlyDifference >= 0 ? "+" : "");
            
            // 2. Get statistics by parent (for table)
            List<Map<String, Object>> parentStats = statisticsDAO.getStatisticsByParent();
            if (parentStats == null) parentStats = new ArrayList<>();
            request.setAttribute("parentStats", parentStats);
            
            // Calculate totals for summary row
            int totalCategories = 0;
            int totalActiveCategories = 0;
            int totalInactiveCategories = 0;
            int totalProducts = 0;
            
            for (Map<String, Object> stat : parentStats) {
                totalCategories += (Integer) stat.getOrDefault("totalCategories", 0);
                totalActiveCategories += (Integer) stat.getOrDefault("activeCategories", 0);
                totalInactiveCategories += (Integer) stat.getOrDefault("inactiveCategories", 0);
                totalProducts += (Integer) stat.getOrDefault("totalProducts", 0);
            }
            
            request.setAttribute("totalCategories", totalCategories);
            request.setAttribute("totalActiveCategories", totalActiveCategories);
            request.setAttribute("totalInactiveCategories", totalInactiveCategories);
            request.setAttribute("totalProducts", totalProducts);
            
            double totalActivePercentage = totalCategories > 0 ? (totalActiveCategories * 100.0 / totalCategories) : 0;
            request.setAttribute("totalActivePercentage", String.format("%.1f", totalActivePercentage));
            
            // 3. Get distribution data (for pie chart)
            Map<String, Integer> distribution = statisticsDAO.getCategoryDistribution();
            if (distribution == null) distribution = new HashMap<>();
            String distributionJson = gson.toJson(distribution);
            request.setAttribute("distributionJson", distributionJson);
            
            // 4. Get top parents by product count (for bar chart)
            List<Map<String, Object>> topParents = statisticsDAO.getTopParentsByProductCount(10);
            if (topParents == null) topParents = new ArrayList<>();
            String topParentsJson = gson.toJson(topParents);
            request.setAttribute("topParentsJson", topParentsJson);
            
            // 5. Get monthly statistics (for line chart)
            List<Map<String, Object>> monthlyStats = statisticsDAO.getMonthlyStatistics();
            if (monthlyStats == null) monthlyStats = new ArrayList<>();
            String monthlyStatsJson = gson.toJson(monthlyStats);
            request.setAttribute("monthlyStatsJson", monthlyStatsJson);
            
            // 6. Get recently added parents
            List<CategoryProductParent> recentParents = statisticsDAO.getRecentlyAddedParents(5);
            if (recentParents == null) recentParents = new ArrayList<>();
            request.setAttribute("recentParents", recentParents);
            
            // 7. Get time-based statistics
            Map<String, Object> timeStats = statisticsDAO.getTimeBasedStatistics();
            if (timeStats == null) {
                timeStats = new HashMap<>();
                timeStats.put("thisMonth", 0);
                timeStats.put("thisQuarter", 0);
                timeStats.put("lastSixMonths", 0);
                timeStats.put("thisYear", 0);
                timeStats.put("currentYear", LocalDate.now().getYear());
                timeStats.put("thisMonthPeriod", "Tháng " + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear());
                timeStats.put("thisQuarterPeriod", "Quý " + ((LocalDate.now().getMonthValue() - 1) / 3 + 1) + "/" + LocalDate.now().getYear());
                timeStats.put("lastSixMonthsPeriod", 
                    LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("MM/yyyy")) + " - " + 
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")));
            }
            request.setAttribute("timeStats", timeStats);
            
            // 8. Add current date info for display
            LocalDate currentDate = LocalDate.now();
            request.setAttribute("currentMonth", currentDate.getMonth().toString());
            request.setAttribute("currentYear", currentDate.getYear());
            
            System.out.println("Statistics data loaded successfully");
            System.out.println("Total parents: " + totalParents);
            System.out.println("Active parents: " + activeParents);
            System.out.println("Inactive parents: " + inactiveParents);
            System.out.println("Top parent: " + (topParent != null ? topParent.getName() : "N/A"));
            System.out.println("Time stats loaded: " + timeStats);
            
        } catch (Exception e) {
            System.out.println("ERROR in loadStatisticsData: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values to prevent JSP errors
            Map<String, Integer> emptyStats = new HashMap<>();
            emptyStats.put("totalParents", 0);
            emptyStats.put("activeParents", 0);
            emptyStats.put("inactiveParents", 0);
            request.setAttribute("stats", emptyStats);
            
            CategoryProductParent emptyTopParent = new CategoryProductParent();
            emptyTopParent.setName("N/A");
            emptyTopParent.setChildCount(0);
            request.setAttribute("topParent", emptyTopParent);
            
            request.setAttribute("activePercentage", "0.0");
            request.setAttribute("inactivePercentage", "0.0");
            request.setAttribute("monthlyDifference", 0);
            request.setAttribute("monthlyDifferenceSign", "");
            request.setAttribute("parentStats", new ArrayList<>());
            request.setAttribute("distributionJson", "{}");
            request.setAttribute("topParentsJson", "[]");
            request.setAttribute("monthlyStatsJson", "[]");
            request.setAttribute("recentParents", new ArrayList<>());
            request.setAttribute("totalCategories", 0);
            request.setAttribute("totalActiveCategories", 0);
            request.setAttribute("totalInactiveCategories", 0);
            request.setAttribute("totalProducts", 0);
            request.setAttribute("totalActivePercentage", "0.0");
            
            // Default time stats
            Map<String, Object> emptyTimeStats = new HashMap<>();
            emptyTimeStats.put("thisMonth", 0);
            emptyTimeStats.put("thisQuarter", 0);
            emptyTimeStats.put("lastSixMonths", 0);
            emptyTimeStats.put("thisYear", 0);
            emptyTimeStats.put("currentYear", LocalDate.now().getYear());
            emptyTimeStats.put("thisMonthPeriod", "Tháng " + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear());
            emptyTimeStats.put("thisQuarterPeriod", "Quý " + ((LocalDate.now().getMonthValue() - 1) / 3 + 1) + "/" + LocalDate.now().getYear());
            emptyTimeStats.put("lastSixMonthsPeriod", 
                LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("MM/yyyy")) + " - " + 
                LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")));
            request.setAttribute("timeStats", emptyTimeStats);
        }
    }
}