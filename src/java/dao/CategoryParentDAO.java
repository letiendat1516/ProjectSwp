package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.CategoryProductParent;
import DBContext.Context;

public class CategoryParentDAO {
    
    /**
     * Get all parent categories (for dropdown)
     */
    public List<CategoryProductParent> getAllCategoryParents() {
        List<CategoryProductParent> list = new ArrayList<>();
        String sql = "SELECT * FROM category_parent WHERE active_flag = 1 ORDER BY name";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CategoryProductParent category = createCategoryFromResultSet(rs);
                list.add(category);
            }
            
        } catch (SQLException e) {
            System.out.println("Error when getting category parent list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get list with pagination and search - UPDATED
     */
    public List<CategoryProductParent> getAllCategoryParents(int page, int pageSize, 
                                                           String searchKeyword, 
                                                           String sortField, 
                                                           String sortDir) {
        List<CategoryProductParent> list = new ArrayList<>();
        
        // 1. Build SQL query
        StringBuilder sql = new StringBuilder("SELECT * FROM category_parent WHERE 1=1");
        
        // 2. Add search condition if exists
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
        }
        
        // 3. Add sorting
        sql.append(" ORDER BY ");
        if ("name".equals(sortField)) {
            sql.append("name");
        } else if ("description".equals(sortField)) {
            sql.append("description");
        } else if ("active_flag".equals(sortField)) {
            sql.append("active_flag");
        } else {
            sql.append("id");
        }
        
        if ("desc".equalsIgnoreCase(sortDir)) {
            sql.append(" DESC");
        } else {
            sql.append(" ASC");
        }
        
        // 4. Add pagination - UPDATED
        sql.append(" LIMIT ? OFFSET ?");
        
        // === DEBUG ===
        System.out.println("=== DAO DEBUG ===");
        System.out.println("SQL: " + sql.toString());
        System.out.println("Page: " + page + ", PageSize: " + pageSize);
        System.out.println("SearchKeyword: " + searchKeyword);
        System.out.println("SortField: " + sortField + ", SortDir: " + sortDir);
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // 5. Set parameters
            int paramIndex = 1;
            
            // Set search parameters
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String searchPattern = "%" + searchKeyword + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                System.out.println("Search pattern: " + searchPattern);
            }
            
            // Set pagination parameters - UPDATED
            int offset = (page - 1) * pageSize;
            ps.setInt(paramIndex++, pageSize);  // LIMIT
            ps.setInt(paramIndex, offset);      // OFFSET
            
            System.out.println("LIMIT: " + pageSize + ", OFFSET: " + offset);
            
            // 6. Execute and get results
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoryProductParent category = createCategoryFromResultSet(rs);
                    list.add(category);
                }
            }
            
            System.out.println("Results found: " + list.size());
            
        } catch (SQLException e) {
            System.out.println("Error when getting paginated list: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get list with pagination and search - WITH CHILD COUNT
     */
    public List<CategoryProductParent> getAllCategoryParentsWithChildCount(int page, int pageSize, 
                                                                          String searchKeyword, 
                                                                          String sortField, 
                                                                          String sortDir) {
        List<CategoryProductParent> list = getAllCategoryParents(page, pageSize, searchKeyword, sortField, sortDir);
        
        // Add child count for each parent
        for (CategoryProductParent parent : list) {
            int childCount = getTotalChildCategoryCount(parent.getId());
            parent.setChildCount(childCount);
        }
        
        return list;
    }
    
    /**
     * Count total parent categories (for pagination) - UPDATED
     */
    public int countCategoryParents(String searchKeyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM category_parent WHERE 1=1");
        
        // Add search condition if exists
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
        }
        
        // === DEBUG ===
        System.out.println("=== COUNT DEBUG ===");
        System.out.println("Count SQL: " + sql.toString());
        System.out.println("Search keyword: " + searchKeyword);
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // Set search parameters
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String searchPattern = "%" + searchKeyword + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                System.out.println("Count search pattern: " + searchPattern);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("Total count result: " + count);
                    return count;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when counting category parent: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Count returning 0");
        return 0;
    }
    
    /**
     * Get parent category by ID
     */
    public CategoryProductParent getCategoryParentById(int id) {
        String sql = "SELECT * FROM category_parent WHERE id = ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createCategoryFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when getting category parent by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // ==================== CREATE NEW ====================
    
    /**
     * Add new parent category
     */
    public boolean addCategoryParent(CategoryProductParent category) {
        // 1. Check if name already exists
        if (isNameExists(category.getName(), null)) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }
        
        // 2. Execute insert
        String sql = "INSERT INTO category_parent (name, description, active_flag, create_date, update_date) VALUES (?, ?, ?, NOW(), NOW())";
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActiveFlag());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error when adding category parent: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Update parent category
     */
    public boolean updateCategoryParent(CategoryProductParent category) {
        // 1. Check if name already exists (excluding itself)
        if (isNameExists(category.getName(), category.getId())) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }
        
        // 2. Execute update
        String sql = "UPDATE category_parent SET name = ?, description = ?, active_flag = ?, update_date = NOW() WHERE id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActiveFlag());
            ps.setInt(4, category.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error when updating category parent: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== TOGGLE STATUS - NEW ====================
    
    /**
     * Toggle active status of parent category
     * Also cascade to child categories if parent becomes inactive
     */
    public boolean toggleCategoryParentStatus(int id, boolean newStatus) {
        Connection conn = null;
        
        try {
            conn = new Context().getJDBCConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Update parent category status
            String updateParentSql = "UPDATE category_parent SET active_flag = ?, update_date = NOW() WHERE id = ?";
            try (PreparedStatement ps1 = conn.prepareStatement(updateParentSql)) {
                ps1.setBoolean(1, newStatus);
                ps1.setInt(2, id);
                
                int rowsAffected = ps1.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // 2. If parent becomes inactive, also deactivate all child categories
            if (!newStatus) {
                String updateChildrenSql = "UPDATE category SET active_flag = 0 WHERE parent_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(updateChildrenSql)) {
                    ps2.setInt(1, id);
                    int childrenUpdated = ps2.executeUpdate();
                    
                    System.out.println("Deactivated " + childrenUpdated + " child categories for parent ID: " + id);
                }
            }
            
            conn.commit(); // Commit transaction
            System.out.println("Successfully toggled parent category status. ID: " + id + ", New Status: " + newStatus);
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error when toggling category parent status: " + e.getMessage());
            e.printStackTrace();
            
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.out.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            return false;
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.out.println("Error when closing connection: " + closeEx.getMessage());
                }
            }
        }
    }
    
    /**
     * Get current status of parent category
     */
    public Boolean getCategoryParentStatus(int id) {
        String sql = "SELECT active_flag FROM category_parent WHERE id = ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("active_flag");
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when getting category parent status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Check how many child categories will be affected by parent status change
     */
    public int getActiveChildCategoryCount(int parentId) {
        String sql = "SELECT COUNT(*) FROM category WHERE parent_id = ? AND active_flag = 1";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, parentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when counting active child categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    // ==================== DELETE ====================
    
    /**
     * Delete parent category (check constraints first)
     */
    public boolean deleteCategoryParent(int id) {
        // 1. Check if any child categories are using this parent
        if (hasChildCategories(id)) {
            System.out.println("Cannot delete: parent category is being used by child categories");
            return false;
        }
        
        // 2. Execute delete
        String sql = "DELETE FROM category_parent WHERE id = ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error when deleting category parent: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Get child category count for a parent (active only)
     */
    public int getChildCategoryCount(int parentId) {
        String sql = "SELECT COUNT(*) FROM category WHERE parent_id = ? AND active_flag = 1";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, parentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when counting child categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total child category count for a parent (both active and inactive)
     */
    public int getTotalChildCategoryCount(int parentId) {
        String sql = "SELECT COUNT(*) FROM category WHERE parent_id = ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, parentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when counting total child categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Check if category name already exists
     */
    private boolean isNameExists(String name, Integer excludeId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM category_parent WHERE name = ?");
        
        // If updating, exclude the current ID
        if (excludeId != null) {
            sql.append(" AND id != ?");
        }
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            ps.setString(1, name);
            if (excludeId != null) {
                ps.setInt(2, excludeId);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when checking name existence: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if parent is being used by child categories
     */
    private boolean hasChildCategories(int parentId) {
        String sql = "SELECT COUNT(*) FROM category WHERE parent_id = ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, parentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when checking child categories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Create CategoryProductParent object from ResultSet
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
    
    /**
     * Test method to check data in DB - NEWLY ADDED
     */
    public void testData() {
        System.out.println("=== TESTING DATABASE ===");
        
        // 1. Check total records
        String sql1 = "SELECT COUNT(*) FROM category_parent";
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql1);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                System.out.println("Total records in DB: " + rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 2. Check first 5 records
        String sql2 = "SELECT * FROM category_parent LIMIT 5";
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql2);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.println("First 5 records:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                 ", Name: " + rs.getString("name") + 
                                 ", Active: " + rs.getBoolean("active_flag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 3. Check table structure
        String sql3 = "DESCRIBE category_parent";
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql3);
             ResultSet rs = ps.executeQuery()) {
            
            System.out.println("Table structure:");
            while (rs.next()) {
                System.out.println("Column: " + rs.getString("Field") + 
                                 ", Type: " + rs.getString("Type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Update basic info (name, description) without affecting status
     */
    public boolean updateCategoryParentBasicInfo(CategoryProductParent category) {
        // 1. Check if name already exists (excluding itself)
        if (isNameExists(category.getName(), category.getId())) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }
        
        // 2. Execute update (only name and description, keep current status)
        String sql = "UPDATE category_parent SET name = ?, description = ?, update_date = NOW() WHERE id = ?";
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error when updating category parent basic info: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== STATISTICS METHODS - NEW ====================
    
    /**
     * Get total statistics for category parents
     */
    public Map<String, Integer> getCategoryParentStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        // 1. Count total, active and inactive parent categories
        String parentSql = "SELECT " +
                          "COUNT(*) as total, " +
                          "SUM(CASE WHEN active_flag = 1 THEN 1 ELSE 0 END) as active, " +
                          "SUM(CASE WHEN active_flag = 0 THEN 1 ELSE 0 END) as inactive " +
                          "FROM category_parent";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(parentSql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                stats.put("totalParents", rs.getInt("total"));
                stats.put("activeParents", rs.getInt("active"));
                stats.put("inactiveParents", rs.getInt("inactive"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting parent statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 2. Count total child categories
        String childSql = "SELECT COUNT(*) as total FROM category";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(childSql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                stats.put("totalChildCategories", rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting child category count: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 3. Count total products (if product table exists)
        String productSql = "SELECT COUNT(*) as total FROM product_info WHERE active_flag = 1";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(productSql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                stats.put("totalProducts", rs.getInt("total"));
            }
        } catch (SQLException e) {
            // Product table might not exist yet
            stats.put("totalProducts", 0);
        }
        
        return stats;
    }
    
    /**
     * Get parent category with most child categories
     */
    public CategoryProductParent getParentWithMostChildren() {
        String sql = "SELECT cp.*, COUNT(c.id) as child_count " +
                    "FROM category_parent cp " +
                    "LEFT JOIN category c ON cp.id = c.parent_id " +
                    "WHERE cp.active_flag = 1 " +
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
            System.out.println("Error getting parent with most children: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get statistics by parent category (for table)
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
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("parentActive", rs.getBoolean("parent_active"));
                row.put("totalCategories", rs.getInt("total_categories"));
                row.put("activeCategories", rs.getInt("active_categories"));
                row.put("inactiveCategories", rs.getInt("inactive_categories"));
                row.put("totalProducts", rs.getInt("total_products"));
                
                // Calculate percentage
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
     * Get monthly statistics for chart (last 6 months)
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
     * Get distribution data for pie chart
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
     * Get recently added parent categories
     */
    public List<CategoryProductParent> getRecentlyAddedParents(int limit) {
        List<CategoryProductParent> recentParents = new ArrayList<>();
        
        String sql = "SELECT * FROM category_parent " +
                    "ORDER BY create_date DESC " +
                    "LIMIT ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CategoryProductParent parent = createCategoryFromResultSet(rs);
                    // Also get child count
                    parent.setChildCount(getTotalChildCategoryCount(parent.getId()));
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
     * Get statistics filtered by date range
     */
    public Map<String, Integer> getStatisticsByDateRange(String startDate, String endDate) {
        Map<String, Integer> stats = new HashMap<>();
        
        String sql = "SELECT " +
                    "COUNT(*) as new_parents, " +
                    "SUM(CASE WHEN active_flag = 1 THEN 1 ELSE 0 END) as active_new " +
                    "FROM category_parent " +
                    "WHERE create_date BETWEEN ? AND ?";
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, startDate);
            ps.setString(2, endDate);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("newParents", rs.getInt("new_parents"));
                    stats.put("activeNew", rs.getInt("active_new"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting statistics by date range: " + e.getMessage());
            e.printStackTrace();
        }
        
        return stats;
    }
}