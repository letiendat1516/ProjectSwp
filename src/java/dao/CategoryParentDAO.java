package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        String sql = "INSERT INTO category_parent (name, description, active_flag) VALUES (?, ?, ?)";
        
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
        String sql = "UPDATE category_parent SET name = ?, description = ?, active_flag = ? WHERE id = ?";
        
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
     * Get child category count for a parent
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
}
