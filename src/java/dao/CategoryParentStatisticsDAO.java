package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CategoryProductParent;
import DBContext.Context;

public class CategoryParentStatisticsDAO {
    
    /**
     * Get total count of parent categories
     */
    public int getTotalParents() {
        String sql = "SELECT COUNT(*) FROM category_parent";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting total parents: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get count of active parent categories
     */
    public int getActiveParentsCount() {
        String sql = "SELECT COUNT(*) FROM category_parent WHERE active_flag = 1";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting active parents count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get count of inactive parent categories
     */
    public int getInactiveParentsCount() {
        String sql = "SELECT COUNT(*) FROM category_parent WHERE active_flag = 0";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting inactive parents count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get parent category with most child categories
     */
    public CategoryProductParent getParentWithMostCategories() {
        String sql = "SELECT cp.*, COUNT(c.id) as child_count " +
                    "FROM category_parent cp " +
                    "LEFT JOIN category c ON cp.id = c.parent_id " +
                    "GROUP BY cp.id " +
                    "ORDER BY child_count DESC " +
                    "LIMIT 1";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                CategoryProductParent parent = createCategoryFromResultSet(rs);
                parent.setChildCount(rs.getInt("child_count"));
                return parent;
            }
        } catch (SQLException e) {
            System.out.println("Error getting parent with most categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return empty object if no data
        CategoryProductParent emptyParent = new CategoryProductParent();
        emptyParent.setName("Chưa có dữ liệu");
        emptyParent.setChildCount(0);
        return emptyParent;
    }
    
    /**
     * Get count of parents created last month
     */
    public int getParentsCountLastMonth() {
        String sql = "SELECT COUNT(*) FROM category_parent " +
                    "WHERE MONTH(create_date) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH)) " +
                    "AND YEAR(create_date) = YEAR(DATE_SUB(NOW(), INTERVAL 1 MONTH))";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting last month count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get statistics for each parent category (for detailed table)
     */
    public List<Map<String, Object>> getStatisticsByParent() {
        List<Map<String, Object>> statsList = new ArrayList<>();
        
        String sql = "SELECT " +
                    "cp.id, " +
                    "cp.name, " +
                    "cp.active_flag as parent_active, " +
                    "COUNT(DISTINCT c.id) as total_categories, " +
                    "SUM(CASE WHEN c.active_flag = 1 THEN 1 ELSE 0 END) as active_categories, " +
                    "SUM(CASE WHEN c.active_flag = 0 THEN 1 ELSE 0 END) as inactive_categories, " +
                    "COUNT(DISTINCT p.id) as total_products " +
                    "FROM category_parent cp " +
                    "LEFT JOIN category c ON cp.id = c.parent_id " +
                    "LEFT JOIN product_info p ON c.id = p.category_id " +
                    "GROUP BY cp.id, cp.name, cp.active_flag " +
                    "ORDER BY total_categories DESC";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("parentID", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("parentActive", rs.getBoolean("parent_active"));
                row.put("totalCategories", rs.getInt("total_categories"));
                row.put("activeCategories", rs.getInt("active_categories"));
                row.put("inactiveCategories", rs.getInt("inactive_categories"));
                row.put("totalProducts", rs.getInt("total_products"));
                
                // Calculate active percentage
                int total = rs.getInt("total_categories");
                int active = rs.getInt("active_categories");
                double percentage = total > 0 ? (active * 100.0 / total) : 0;
                row.put("activePercentage", percentage);
                
                statsList.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error getting statistics by parent: " + e.getMessage());
            e.printStackTrace();
        }
        
        return statsList;
    }
    
    /**
     * Get monthly statistics for the last 6 months
     */
    public List<Map<String, Object>> getMonthlyStatistics() {
        List<Map<String, Object>> monthlyStats = new ArrayList<>();
        
        String sql = "SELECT " +
                    "DATE_FORMAT(create_date, '%Y-%m') as month, " +
                    "COUNT(*) as new_parents " +
                    "FROM category_parent " +
                    "WHERE create_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH) " +
                    "GROUP BY DATE_FORMAT(create_date, '%Y-%m') " +
                    "ORDER BY month";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("month", rs.getString("month"));
                row.put("newParents", rs.getInt("new_parents"));
                monthlyStats.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error getting monthly statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return monthlyStats;
    }
    
    /**
     * Get category distribution for pie chart
     */
    public Map<String, Integer> getCategoryDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        
        String sql = "SELECT " +
                    "cp.name, " +
                    "COUNT(c.id) as category_count " +
                    "FROM category_parent cp " +
                    "LEFT JOIN category c ON cp.id = c.parent_id " +
                    "WHERE cp.active_flag = 1 " +
                    "GROUP BY cp.id, cp.name " +
                    "HAVING COUNT(c.id) > 0 " +
                    "ORDER BY category_count DESC";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                distribution.put(rs.getString("name"), rs.getInt("category_count"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting category distribution: " + e.getMessage());
            e.printStackTrace();
        }
        
        return distribution;
    }
    
    /**
     * Get top parent categories by product count
     */
    public List<Map<String, Object>> getTopParentsByProductCount(int limit) {
        List<Map<String, Object>> topParents = new ArrayList<>();
        
        String sql = "SELECT " +
                    "cp.id, " +
                    "cp.name, " +
                    "COUNT(DISTINCT p.id) as product_count " +
                    "FROM category_parent cp " +
                    "INNER JOIN category c ON cp.id = c.parent_id " +
                    "INNER JOIN product_info p ON c.id = p.category_id " +
                    "WHERE cp.active_flag = 1 AND p.active_flag = 1 " +
                    "GROUP BY cp.id, cp.name " +
                    "ORDER BY product_count DESC " +
                    "LIMIT ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id", rs.getInt("id"));
                    row.put("name", rs.getString("name"));
                    row.put("productCount", rs.getInt("product_count"));
                    topParents.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting top parents by product count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return topParents;
    }
    
    /**
     * Get recently added parent categories
     */
    public List<CategoryProductParent> getRecentlyAddedParents(int limit) {
        List<CategoryProductParent> recentParents = new ArrayList<>();
        CategoryParentDAO parentDAO = new CategoryParentDAO(); // For getting child count
        
        String sql = "SELECT * FROM category_parent " +
                    "ORDER BY create_date DESC " +
                    "LIMIT ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoryProductParent parent = createCategoryFromResultSet(rs);
                    // Get child count
                    parent.setChildCount(parentDAO.getTotalChildCategoryCount(parent.getId()));
                    recentParents.add(parent);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recently added parents: " + e.getMessage());
            e.printStackTrace();
        }
        
        return recentParents;
    }
    
    /**
     * Get statistics filtered by date range and status
     */
    public Map<String, Object> getFilteredStatistics(String status, String startDate, String endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // Build WHERE clause
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1 ");
        
        if (status != null && !status.isEmpty()) {
            whereClause.append(" AND cp.active_flag = ? ");
        }
        
        if (startDate != null && endDate != null) {
            whereClause.append(" AND cp.create_date BETWEEN ? AND DATE_ADD(?, INTERVAL 1 DAY) ");
        }
        
        // Get filtered parent stats
        String sql = "SELECT " +
                    "COUNT(DISTINCT cp.id) as total_parents, " +
                    "SUM(CASE WHEN cp.active_flag = 1 THEN 1 ELSE 0 END) as active_parents, " +
                    "SUM(CASE WHEN cp.active_flag = 0 THEN 1 ELSE 0 END) as inactive_parents " +
                    "FROM category_parent cp " + whereClause.toString();
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int paramIndex = 1;
            
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            if (startDate != null && endDate != null) {
                ps.setString(paramIndex++, startDate);
                ps.setString(paramIndex++, endDate);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalParents", rs.getInt("total_parents"));
                    stats.put("activeParents", rs.getInt("active_parents"));
                    stats.put("inactiveParents", rs.getInt("inactive_parents"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting filtered statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }
    
    /**
     * Get overall statistics summary
     */
    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalParents", getTotalParents());
        stats.put("activeParents", getActiveParentsCount());
        stats.put("inactiveParents", getInactiveParentsCount());
        
        // Get parent with most categories
        CategoryProductParent topParent = getParentWithMostCategories();
        stats.put("topParent", topParent);
        
        // Calculate percentages
        int total = getTotalParents();
        if (total > 0) {
            stats.put("activePercentage", (getActiveParentsCount() * 100.0 / total));
            stats.put("inactivePercentage", (getInactiveParentsCount() * 100.0 / total));
        } else {
            stats.put("activePercentage", 0.0);
            stats.put("inactivePercentage", 0.0);
        }
        
        // Get comparison with last month
        int lastMonthCount = getParentsCountLastMonth();
        int currentTotal = getTotalParents();
        stats.put("monthlyDifference", currentTotal - lastMonthCount);
        
        return stats;
    }
    
    /**
     * Get time-based statistics (month, quarter, 6 months, year)
     */
    public Map<String, Object> getTimeBasedStatistics() {
        Map<String, Object> timeStats = new HashMap<>();
        
        try (Connection conn = new Context().getJDBCConnection()) {
            // Get current date dynamically
            LocalDate now = LocalDate.now();
            
            // This month
            String thisMonthSql = "SELECT COUNT(*) FROM category_parent " +
                                 "WHERE YEAR(create_date) = ? AND MONTH(create_date) = ?";
            try (PreparedStatement ps = conn.prepareStatement(thisMonthSql)) {
                ps.setInt(1, now.getYear());
                ps.setInt(2, now.getMonthValue());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    timeStats.put("thisMonth", rs.getInt(1));
                }
            }
            timeStats.put("thisMonthPeriod", "Tháng " + now.getMonthValue() + "/" + now.getYear());
            
            // This quarter
            int quarter = (now.getMonthValue() - 1) / 3 + 1;
            int quarterStartMonth = (quarter - 1) * 3 + 1;
            int quarterEndMonth = quarter * 3;
            
            String thisQuarterSql = "SELECT COUNT(*) FROM category_parent " +
                                   "WHERE YEAR(create_date) = ? AND MONTH(create_date) BETWEEN ? AND ?";
            try (PreparedStatement ps = conn.prepareStatement(thisQuarterSql)) {
                ps.setInt(1, now.getYear());
                ps.setInt(2, quarterStartMonth);
                ps.setInt(3, quarterEndMonth);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    timeStats.put("thisQuarter", rs.getInt(1));
                }
            }
            timeStats.put("thisQuarterPeriod", "Quý " + quarter + "/" + now.getYear());
            
            // Last 6 months - using SQL date arithmetic
            String lastSixMonthsSql = "SELECT COUNT(*) FROM category_parent " +
                                     "WHERE create_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)";
            try (PreparedStatement ps = conn.prepareStatement(lastSixMonthsSql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    timeStats.put("lastSixMonths", rs.getInt(1));
                }
            }
            
            LocalDate sixMonthsAgo = now.minusMonths(6);
            timeStats.put("lastSixMonthsPeriod", 
                sixMonthsAgo.format(DateTimeFormatter.ofPattern("MM/yyyy")) + " - " + 
                now.format(DateTimeFormatter.ofPattern("MM/yyyy")));
            
            // This year
            String thisYearSql = "SELECT COUNT(*) FROM category_parent " +
                                "WHERE YEAR(create_date) = ?";
            try (PreparedStatement ps = conn.prepareStatement(thisYearSql)) {
                ps.setInt(1, now.getYear());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    timeStats.put("thisYear", rs.getInt(1));
                }
            }
            timeStats.put("currentYear", now.getYear());
            
            // Debug: Check actual data in database (comment out in production)
            System.out.println("=== Time-based Statistics Debug ===");
            System.out.println("Current date: " + now);
            System.out.println("This month: " + timeStats.get("thisMonth") + " records");
            System.out.println("This quarter (Q" + quarter + "): " + timeStats.get("thisQuarter") + " records");
            System.out.println("Last 6 months: " + timeStats.get("lastSixMonths") + " records");
            System.out.println("This year: " + timeStats.get("thisYear") + " records");
            
        } catch (SQLException e) {
            System.out.println("Error getting time-based statistics: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values with current date info
            LocalDate now = LocalDate.now();
            int quarter = (now.getMonthValue() - 1) / 3 + 1;
            
            timeStats.put("thisMonth", 0);
            timeStats.put("thisQuarter", 0);
            timeStats.put("lastSixMonths", 0);
            timeStats.put("thisYear", 0);
            timeStats.put("currentYear", now.getYear());
            timeStats.put("thisMonthPeriod", "Tháng " + now.getMonthValue() + "/" + now.getYear());
            timeStats.put("thisQuarterPeriod", "Quý " + quarter + "/" + now.getYear());
            timeStats.put("lastSixMonthsPeriod", 
                now.minusMonths(6).format(DateTimeFormatter.ofPattern("MM/yyyy")) + " - " + 
                now.format(DateTimeFormatter.ofPattern("MM/yyyy")));
        }
        
        return timeStats;
    }
    
    /**
     * Helper method to create CategoryProductParent from ResultSet
     */
    private CategoryProductParent createCategoryFromResultSet(ResultSet rs) throws SQLException {
        CategoryProductParent category = new CategoryProductParent();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setActiveFlag(rs.getBoolean("active_flag"));
        if (rs.getTimestamp("create_date") != null) {
            category.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
        }
        if (rs.getTimestamp("update_date") != null) {
            category.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
        }
        return category;
    }
}