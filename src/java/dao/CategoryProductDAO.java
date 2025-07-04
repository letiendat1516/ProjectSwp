package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CategoryProduct;
import model.CategoryProductParent;
import DBContext.Context;

public class CategoryProductDAO {
    private CategoryParentDAO parentDAO;

    public CategoryProductDAO() {
        this.parentDAO = new CategoryParentDAO();
    }

    // Get categories with parent info (paginated, searchable, sortable)
    public List<CategoryProduct> getAllCategoriesWithParent(int page, int pageSize, 
            String searchKeyword, String sortField, String sortDir) {

        List<CategoryProduct> categories = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT c.id, c.name, c.parent_id, c.active_flag, c.create_date, c.update_date, cp.name as parent_name " +
            "FROM category c LEFT JOIN category_parent cp ON c.parent_id = cp.id WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        // Add search condition - simple lowercase comparison
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND LOWER(c.name) LIKE LOWER(?)");
            params.add("%" + searchKeyword.trim() + "%");
        }

        // Add sorting
        sql.append(" ORDER BY ").append(getSortField(sortField)).append(" ")
           .append(getSortDirection(sortDir));

        // Add pagination
        sql.append(" LIMIT ?, ?");
        params.add((page - 1) * pageSize);
        params.add(pageSize);

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs, true));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Get categories without parent info (for backward compatibility)
    public List<CategoryProduct> getAllCategories(int page, int pageSize, 
            String searchKeyword, String sortField, String sortDir) {

        List<CategoryProduct> categories = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, name, parent_id, active_flag, create_date, update_date FROM category WHERE 1=1");

        List<Object> params = new ArrayList<>();

        // Add search condition - simple lowercase comparison
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + searchKeyword.trim() + "%");
        }

        sql.append(" ORDER BY ").append(getSortFieldSimple(sortField)).append(" ")
           .append(getSortDirection(sortDir))
           .append(" LIMIT ?, ?");

        params.add((page - 1) * pageSize);
        params.add(pageSize);

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs, false));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Overloaded method for backward compatibility
    public List<CategoryProduct> getAllCategories(int page, int pageSize, String searchKeyword) {
        return getAllCategories(page, pageSize, searchKeyword, "id", "asc");
    }

    // Count categories for pagination
    public int countCategories(String searchKeyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM category WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Add search condition - simple lowercase comparison
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + searchKeyword.trim() + "%");
        }

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Get category by ID
    public CategoryProduct getCategoryById(int id) {
        String sql = "SELECT id, name, parent_id, active_flag, create_date, update_date FROM category WHERE id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs, false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get category with parent info by ID
    public CategoryProduct getCategoryWithParentById(int id) {
        String sql = "SELECT c.id, c.name, c.parent_id, c.active_flag, c.create_date, c.update_date, cp.name as parent_name " +
            "FROM category c LEFT JOIN category_parent cp ON c.parent_id = cp.id " +
            "WHERE c.id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs, true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Check if category name exists (for duplicate prevention)
    public boolean isCategoryNameExists(String name, Integer excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM category WHERE LOWER(name) = LOWER(?)");

        if (excludeId != null) {
            sql.append(" AND id != ?");
        }

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, name.trim());
            if (excludeId != null) {
                ps.setInt(2, excludeId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Overloaded method for backward compatibility
    public boolean isCategoryNameExists(String name) {
        return isCategoryNameExists(name, null);
    }

    // Add category
    public boolean addCategory(CategoryProduct category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }

        // Check for duplicate name
        if (isCategoryNameExists(category.getName())) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }

        String sql = "INSERT INTO category (name, parent_id, active_flag, create_date, update_date) VALUES (?, ?, ?, NOW(), NOW())";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName().trim());
            if (category.getParentId() != null) {
                ps.setInt(2, category.getParentId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setBoolean(3, category.isActiveFlag());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update category
    public boolean updateCategory(CategoryProduct category) {
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }

        // Check for duplicate name (excluding current category)
        if (isCategoryNameExists(category.getName(), category.getId())) {
            System.out.println("Category name already exists: " + category.getName());
            return false;
        }

        String sql = "UPDATE category SET name = ?, parent_id = ?, active_flag = ?, update_date = NOW() WHERE id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName().trim());
            if (category.getParentId() != null) {
                ps.setInt(2, category.getParentId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setBoolean(3, category.isActiveFlag());
            ps.setInt(4, category.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete category
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM category WHERE id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get active categories for dropdown
    public List<CategoryProduct> getAllCategoriesForDropdown() {
        List<CategoryProduct> categories = new ArrayList<>();
        String sql = "SELECT id, name, parent_id, active_flag, create_date, update_date FROM category WHERE active_flag = 1 ORDER BY name";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs, false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Get parent categories for dropdown
    public List<CategoryProductParent> getCategoryParentsForDropdown() {
        return parentDAO.getAllCategoryParents();
    }

    // Get categories by parent ID
    public List<CategoryProduct> getCategoriesByParentId(Integer parentId) {
        List<CategoryProduct> categories = new ArrayList<>();
        String sql = "SELECT id, name, parent_id, active_flag, create_date, update_date FROM category WHERE " + 
            (parentId == null ? "parent_id IS NULL" : "parent_id = ?") +
            " AND active_flag = 1 ORDER BY name";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parentId != null) {
                ps.setInt(1, parentId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs, false));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    // Helper methods
    private CategoryProduct mapResultSetToCategory(ResultSet rs, boolean includeParentName) 
            throws SQLException {
        CategoryProduct category = new CategoryProduct();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setParentId(rs.getObject("parent_id", Integer.class));
        category.setActiveFlag(rs.getBoolean("active_flag"));
        
        // Xử lý date fields với try-catch để tránh lỗi
        try {
            if (rs.getTimestamp("create_date") != null) {
                category.setCreatedAt(rs.getTimestamp("create_date").toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại hoặc null, bỏ qua
        }
        
        try {
            if (rs.getTimestamp("update_date") != null) {
                category.setUpdatedAt(rs.getTimestamp("update_date").toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại hoặc null, bỏ qua
        }

        if (includeParentName) {
            category.setParentName(rs.getString("parent_name"));
        }

        return category;
    }

    private String getSortField(String sortField) {
        if (sortField == null) return "c.id";

        switch (sortField.toLowerCase()) {
            case "name": return "c.name";
            case "parent_name": return "cp.name";
            case "active_flag": return "c.active_flag";
            case "create_date": return "c.create_date";
            case "update_date": return "c.update_date";
            default: return "c.id";
        }
    }

    private String getSortFieldSimple(String sortField) {
        if (sortField == null) return "id";

        switch (sortField.toLowerCase()) {
            case "name": return "name";
            case "active_flag": return "active_flag";
            case "create_date": return "create_date";
            case "update_date": return "update_date";
            default: return "id";
        }
    }

    private String getSortDirection(String sortDir) {
        return "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
    }

    private void setParameters(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }
}