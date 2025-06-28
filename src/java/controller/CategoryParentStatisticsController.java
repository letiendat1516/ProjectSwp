package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import dao.CategoryParentDAO;
import model.CategoryProductParent;
import model.Users;
import com.google.gson.Gson;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/category-parent/statistics")
public class CategoryParentStatisticsController extends HttpServlet {
    
    private CategoryParentDAO dao;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        System.out.println("=== CategoryParentStatisticsController INIT ===");
        dao = new CategoryParentDAO();
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
                return; // Important: return here to avoid forwarding to JSP
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
                    // Get filtered statistics
                    Map<String, Integer> filteredStats = dao.getStatisticsByDateRange(startDate, endDate);
                    
                    if (filteredStats == null) {
                        filteredStats = new HashMap<>();
                        filteredStats.put("newParents", 0);
                        filteredStats.put("activeNew", 0);
                    }
                    
                    result.put("success", true);
                    result.put("data", filteredStats);
                    result.put("message", "Đã lọc dữ liệu thành công. Tìm thấy " + 
                              filteredStats.getOrDefault("newParents", 0) + " danh mục mới trong khoảng thời gian này.");
                    
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
            
            // 1. Get general statistics
            Map<String, Integer> stats = dao.getCategoryParentStatistics();
            if (stats == null) {
                stats = new HashMap<>();
                stats.put("totalParents", 0);
                stats.put("activeParents", 0);
                stats.put("inactiveParents", 0);
                stats.put("totalChildCategories", 0);
                stats.put("totalProducts", 0);
            }
            request.setAttribute("stats", stats);
            
            // Calculate percentages
            int totalParents = stats.getOrDefault("totalParents", 0);
            int activeParents = stats.getOrDefault("activeParents", 0);
            int inactiveParents = stats.getOrDefault("inactiveParents", 0);
            
            double activePercentage = totalParents > 0 ? (activeParents * 100.0 / totalParents) : 0;
            double inactivePercentage = totalParents > 0 ? (inactiveParents * 100.0 / totalParents) : 0;
            
            request.setAttribute("activePercentage", String.format("%.1f", activePercentage));
            request.setAttribute("inactivePercentage", String.format("%.1f", inactivePercentage));
            
            // 2. Get parent with most children
            CategoryProductParent topParent = dao.getParentWithMostChildren();
            if (topParent == null) {
                topParent = new CategoryProductParent();
                topParent.setName("N/A");
                topParent.setChildCount(0);
            }
            request.setAttribute("topParent", topParent);
            
            // 3. Get statistics by parent (for table)
            List<Map<String, Object>> parentStats = dao.getStatisticsByParent();
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
            
            // 4. Get distribution data (for pie chart)
            Map<String, Integer> distribution = dao.getCategoryDistribution();
            if (distribution == null) distribution = new HashMap<>();
            String distributionJson = gson.toJson(distribution);
            request.setAttribute("distributionJson", distributionJson);
            
            // 5. Get top parents by product count (for bar chart)
            List<Map<String, Object>> topParents = dao.getTopParentsByProductCount(10);
            if (topParents == null) topParents = new ArrayList<>();
            String topParentsJson = gson.toJson(topParents);
            request.setAttribute("topParentsJson", topParentsJson);
            
            // 6. Get monthly statistics (for line chart)
            List<Map<String, Object>> monthlyStats = dao.getMonthlyStatistics();
            if (monthlyStats == null) monthlyStats = new ArrayList<>();
            String monthlyStatsJson = gson.toJson(monthlyStats);
            request.setAttribute("monthlyStatsJson", monthlyStatsJson);
            
            // 7. Get recently added parents
            List<CategoryProductParent> recentParents = dao.getRecentlyAddedParents(5);
            if (recentParents == null) recentParents = new ArrayList<>();
            request.setAttribute("recentParents", recentParents);
            
            // 8. Get comparison with last month - Using current date
            LocalDate currentDate = LocalDate.now(); // Real-time date
            LocalDate firstDayThisMonth = currentDate.withDayOfMonth(1);
            LocalDate lastDayThisMonth = currentDate;
            LocalDate firstDayLastMonth = firstDayThisMonth.minusMonths(1);
            LocalDate lastDayLastMonth = firstDayLastMonth.withDayOfMonth(firstDayLastMonth.lengthOfMonth());
            
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            
            Map<String, Integer> lastMonthStats = dao.getStatisticsByDateRange(
                firstDayLastMonth.format(formatter), 
                lastDayLastMonth.format(formatter)
            );
            Map<String, Integer> thisMonthStats = dao.getStatisticsByDateRange(
                firstDayThisMonth.format(formatter), 
                lastDayThisMonth.format(formatter)
            );
            
            int lastMonthNew = lastMonthStats != null ? lastMonthStats.getOrDefault("newParents", 0) : 0;
            int thisMonthNew = thisMonthStats != null ? thisMonthStats.getOrDefault("newParents", 0) : 0;
            int difference = thisMonthNew - lastMonthNew;
            
            request.setAttribute("monthlyDifference", difference);
            request.setAttribute("monthlyDifferenceSign", difference >= 0 ? "+" : "");
            
            // Add current month info for display
            request.setAttribute("currentMonth", currentDate.getMonth().toString());
            request.setAttribute("currentYear", currentDate.getYear());
            
            System.out.println("Statistics data loaded successfully");
            System.out.println("Date range - Last month: " + firstDayLastMonth + " to " + lastDayLastMonth);
            System.out.println("Date range - This month: " + firstDayThisMonth + " to " + lastDayThisMonth);
            
        } catch (Exception e) {
            System.out.println("ERROR in loadStatisticsData: " + e.getMessage());
            e.printStackTrace();
        }
    }
}