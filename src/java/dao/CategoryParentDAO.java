package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * Get filtered parent categories with all filter options
     */
    public List<CategoryProductParent> getFilteredParentCategories(String searchName, String status, 
            String childCountFilter, String sortBy, String sortOrder, int page, int pageSize) {
        
        List<CategoryProductParent> list = new ArrayList<>();
        
        // Build SQL with filters
        StringBuilder sql = new StringBuilder(
            "SELECT cp.*, COUNT(c.id) as child_count " +
            "FROM category_parent cp " +
            "LEFT JOIN category c ON cp.id = c.parent_id " +
            "WHERE 1=1 ");
        
        // Add search condition
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append("AND (cp.name LIKE ? OR cp.description LIKE ?) ");
        }
        
        // Add status filter
        if (status != null && !status.isEmpty()) {
            sql.append("AND cp.active_flag = ? ");
        }
        
        // Group by for child count
        sql.append("GROUP BY cp.id, cp.name, cp.description, cp.active_flag, cp.create_date, cp.update_date ");
        
        // Add child count filter
        if (childCountFilter != null && !childCountFilter.isEmpty()) {
            switch (childCountFilter) {
                case "0":
                    sql.append("HAVING COUNT(c.id) = 0 ");
                    break;
                case "1-5":
                    sql.append("HAVING COUNT(c.id) BETWEEN 1 AND 5 ");
                    break;
                case "6-10":
                    sql.append("HAVING COUNT(c.id) BETWEEN 6 AND 10 ");
                    break;
                case "10+":
                    sql.append("HAVING COUNT(c.id) > 10 ");
                    break;
            }
        }
        
        // Add sorting
        String[] allowedSortColumns = {"id", "name", "create_date", "update_date"};
        if (sortBy != null && Arrays.asList(allowedSortColumns).contains(sortBy)) {
            sql.append("ORDER BY cp.").append(sortBy);
            if ("desc".equalsIgnoreCase(sortOrder)) {
                sql.append(" DESC ");
            } else {
                sql.append(" ASC ");
            }
        } else {
            sql.append("ORDER BY cp.id ASC ");
        }
        
        // Add pagination
        sql.append("LIMIT ? OFFSET ?");
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            // Set search parameters
            if (searchName != null && !searchName.trim().isEmpty()) {
                String searchPattern = "%" + searchName.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            // Set status parameter
            if (status != null && !status.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(status));
            }
            
            // Set pagination parameters
            int offset = (page - 1) * pageSize;
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, offset);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CategoryProductParent parent = new CategoryProductParent();
                parent.setId(rs.getInt("id"));
                parent.setName(rs.getString("name"));
                parent.setDescription(rs.getString("description"));
                parent.setActiveFlag(rs.getBoolean("active_flag"));
                parent.setChildCount(rs.getInt("child_count"));
                
                if (rs.getTimestamp("create_date") != null) {
                    parent.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
                }
                if (rs.getTimestamp("update_date") != null) {
                    parent.setUpdateDate(rs.getTimestamp("update_date").toLocalDateTime());
                }
                
                list.add(parent);
            }
            
        } catch (SQLException e) {
            System.out.println("Error in getFilteredParentCategories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Count filtered parent categories for pagination
     */
    public int countFilteredParentCategories(String searchName, String status, String childCountFilter) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(DISTINCT cp.id) as total_count " +
            "FROM category_parent cp " +
            "LEFT JOIN category c ON cp.id = c.parent_id " +
            "WHERE 1=1 ");
        
        // Add search condition
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append("AND (cp.name LIKE ? OR cp.description LIKE ?) ");
        }
        
        // Add status filter
        if (status != null && !status.isEmpty()) {
            sql.append("AND cp.active_flag = ? ");
        }
        
        // For child count filter, we need a subquery
        if (childCountFilter != null && !childCountFilter.isEmpty()) {
            sql = new StringBuilder(
                "SELECT COUNT(*) FROM (" +
                "SELECT cp.id " +
                "FROM category_parent cp " +
                "LEFT JOIN category c ON cp.id = c.parent_id " +
                "WHERE 1=1 ");
            
            if (searchName != null && !searchName.trim().isEmpty()) {
                sql.append("AND (cp.name LIKE ? OR cp.description LIKE ?) ");
            }
            
            if (status != null && !status.isEmpty()) {
                sql.append("AND cp.active_flag = ? ");
            }
            
            sql.append("GROUP BY cp.id ");
            
            switch (childCountFilter) {
                case "0":
                    sql.append("HAVING COUNT(c.id) = 0 ");
                    break;
                case "1-5":
                    sql.append("HAVING COUNT(c.id) BETWEEN 1 AND 5 ");
                    break;
                case "6-10":
                    sql.append("HAVING COUNT(c.id) BETWEEN 6 AND 10 ");
                    break;
                case "10+":
                    sql.append("HAVING COUNT(c.id) > 10 ");
                    break;
            }
            
            sql.append(") as filtered_parents");
        }
        
        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            
            // Set search parameters
            if (searchName != null && !searchName.trim().isEmpty()) {
                String searchPattern = "%" + searchName.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            // Set status parameter
            if (status != null && !status.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(status));
            }
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.out.println("Error in countFilteredParentCategories: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get list with pagination and search - WITH CHILD COUNT
     */
    public List<CategoryProductParent> getAllCategoryParentsWithChildCount(int page, int pageSize, 
                                                                          String searchKeyword, 
                                                                          String sortField, 
                                                                          String sortDir) {
        // Use the new filtered method with null status and childCountFilter
        return getFilteredParentCategories(searchKeyword, null, null, sortField, sortDir, page, pageSize);
    }
    
    /**
     * Count total parent categories (for pagination)
     */
    public int countCategoryParents(String searchKeyword) {
        // Use the new count method with null filters
        return countFilteredParentCategories(searchKeyword, null, null);
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
                    CategoryProductParent category = createCategoryFromResultSet(rs);
                    // Also get child count
                    category.setChildCount(getTotalChildCategoryCount(id));
                    return category;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error when getting category parent by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Add new parent category
     */
    public boolean addCategoryParent(CategoryProductParent category) {
        // Check if name already exists
        if (isNameExists(category.getName(), null)) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }
        
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
    
    /**
     * Update parent category
     */
    public boolean updateCategoryParent(CategoryProductParent category) {
        // Check if name already exists (excluding itself)
        if (isNameExists(category.getName(), category.getId())) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }
        
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
    
    /**
     * Update basic info (name, description) without affecting status
     */
    public boolean updateCategoryParentBasicInfo(CategoryProductParent category) {
        // Check if name already exists (excluding itself)
        if (isNameExists(category.getName(), category.getId())) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }
        
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
    
    /**
     * Toggle active status of parent category
     * Also cascade to child categories if parent becomes inactive
     */
    public boolean toggleCategoryParentStatus(int id, boolean newStatus) {
        Connection conn = null;
        
        try {
            conn = new Context().getJDBCConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Update parent category status
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
            
            // If parent becomes inactive, also deactivate all child categories
            if (!newStatus) {
                String updateChildrenSql = "UPDATE category SET active_flag = 0 WHERE parent_id = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(updateChildrenSql)) {
                    ps2.setInt(1, id);
                    int childrenUpdated = ps2.executeUpdate();
                    System.out.println("Deactivated " + childrenUpdated + " child categories for parent ID: " + id);
                }
            }
            
            conn.commit(); // Commit transaction
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
     * Delete parent category (check constraints first)
     */
    public boolean deleteCategoryParent(int id) {
        // Check if any child categories are using this parent
        if (hasChildCategories(id)) {
            System.out.println("Cannot delete: parent category is being used by child categories");
            return false;
        }
        
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
     * Check how many active child categories will be affected
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
    
    /**
     * Check if category name already exists
     */
    private boolean isNameExists(String name, Integer excludeId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM category_parent WHERE name = ?");
        
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
}