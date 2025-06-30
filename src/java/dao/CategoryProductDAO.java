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

    // ===================== PHƯƠNG THỨC CŨ GIỮ NGUYÊN =====================

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

    // ===================== CÁC PHƯƠNG THỨC MỚI =====================

    /**
     * Tìm kiếm danh mục với bộ lọc nâng cao và phân trang
     */
    public List<CategoryProduct> searchCategoriesWithFilters(String searchKeyword, Integer status, Integer parentId,
                                                           String sortField, String sortDir, int offset, int limit) {
        List<CategoryProduct> categories = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.id, c.name, c.parent_id, c.active_flag, c.create_date, c.update_date, cp.name as parent_name ");
        sql.append("FROM category c LEFT JOIN category_parent cp ON c.parent_id = cp.id WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // Thêm điều kiện tìm kiếm
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND LOWER(c.name) LIKE LOWER(?) ");
            params.add("%" + searchKeyword.trim() + "%");
        }

        // Thêm điều kiện lọc theo trạng thái
        if (status != null) {
            sql.append("AND c.active_flag = ? ");
            params.add(status == 1);
        }

        // Thêm điều kiện lọc theo parent
        if (parentId != null) {
            sql.append("AND c.parent_id = ? ");
            params.add(parentId);
        }

        // Thêm ORDER BY
        sql.append("ORDER BY ");
        switch (sortField != null ? sortField : "id") {
            case "name":
                sql.append("c.name ");
                break;
            case "parent_name":
                sql.append("cp.name ");
                break;
            case "active_flag":
                sql.append("c.active_flag ");
                break;
            case "create_date":
                sql.append("c.create_date ");
                break;
            case "update_date":
                sql.append("c.update_date ");
                break;
            default:
                sql.append("c.id ");
                break;
        }

        // Thêm direction
        sql.append("desc".equalsIgnoreCase(sortDir) ? "DESC " : "ASC ");

        // Thêm LIMIT và OFFSET
        sql.append("LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

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

    /**
     * Đếm tổng số danh mục với bộ lọc
     */
    public int countCategoriesWithFilters(String searchKeyword, Integer status, Integer parentId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM category c WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        // Thêm điều kiện tìm kiếm
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND LOWER(c.name) LIKE LOWER(?) ");
            params.add("%" + searchKeyword.trim() + "%");
        }

        // Thêm điều kiện lọc theo trạng thái
        if (status != null) {
            sql.append("AND c.active_flag = ? ");
            params.add(status == 1);
        }

        // Thêm điều kiện lọc theo parent
        if (parentId != null) {
            sql.append("AND c.parent_id = ? ");
            params.add(parentId);
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

    /**
     * Toggle trạng thái active/inactive của danh mục
     */
    public boolean toggleCategoryStatus(int categoryId) {
        String sql = "UPDATE category SET active_flag = NOT active_flag, update_date = NOW() WHERE id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy tất cả danh mục cha đang active để làm filter
     */
    public List<CategoryProductParent> getAllActiveParentCategories() {
        return parentDAO.getAllCategoryParents(); // Sử dụng method có sẵn
    }

    /**
     * Lấy tất cả danh mục đang active
     */
    public List<CategoryProduct> getAllActiveCategories() {
        List<CategoryProduct> categories = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.parent_id, c.active_flag, c.create_date, c.update_date, cp.name as parent_name " +
                    "FROM category c LEFT JOIN category_parent cp ON c.parent_id = cp.id " +
                    "WHERE c.active_flag = 1 ORDER BY c.name ASC";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs, true));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    /**
     * Lấy danh mục theo parent ID với parent name
     */
    public List<CategoryProduct> getCategoriesByParentIdWithParentName(Integer parentId) {
        List<CategoryProduct> categories = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.parent_id, c.active_flag, c.create_date, c.update_date, cp.name as parent_name " +
                    "FROM category c LEFT JOIN category_parent cp ON c.parent_id = cp.id " +
                    "WHERE " + (parentId == null ? "c.parent_id IS NULL" : "c.parent_id = ?") +
                    " ORDER BY c.name ASC";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parentId != null) {
                ps.setInt(1, parentId);
            }

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

    /**
     * Lấy tổng số danh mục
     */
    public int getTotalCategoriesCount() {
        String sql = "SELECT COUNT(*) FROM category";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Lấy thống kê danh mục theo trạng thái
     */
    public int getCategoriesCountByStatus(boolean activeFlag) {
        String sql = "SELECT COUNT(*) FROM category WHERE active_flag = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, activeFlag);
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

    /**
     * Lấy thống kê danh mục theo parent ID
     */
    public int getCategoriesCountByParentId(Integer parentId) {
        String sql = "SELECT COUNT(*) FROM category WHERE " + 
                    (parentId == null ? "parent_id IS NULL" : "parent_id = ?");

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parentId != null) {
                ps.setInt(1, parentId);
            }

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

    /**
     * Kiểm tra danh mục có sản phẩm hay không (để quyết định có thể xóa không)
     */
    public boolean hasCategoryProducts(int categoryId) {
        String sql = "SELECT COUNT(*) FROM product WHERE category_id = ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy danh mục theo tên (for search suggestion)
     */
    public List<CategoryProduct> getCategoriesByName(String name, int limit) {
        List<CategoryProduct> categories = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.parent_id, c.active_flag, c.create_date, c.update_date, cp.name as parent_name " +
                    "FROM category c LEFT JOIN category_parent cp ON c.parent_id = cp.id " +
                    "WHERE LOWER(c.name) LIKE LOWER(?) AND c.active_flag = 1 " +
                    "ORDER BY c.name ASC LIMIT ?";

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name.trim() + "%");
            ps.setInt(2, limit);

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

    /**
     * Cập nhật trạng thái hàng loạt
     */
    public boolean updateCategoriesStatus(List<Integer> categoryIds, boolean activeFlag) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return false;
        }

        StringBuilder sql = new StringBuilder("UPDATE category SET active_flag = ?, update_date = NOW() WHERE id IN (");
        for (int i = 0; i < categoryIds.size(); i++) {
            if (i > 0) sql.append(",");
            sql.append("?");
        }
        sql.append(")");

        try (Connection conn = new Context().getJDBCConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setBoolean(1, activeFlag);
            for (int i = 0; i < categoryIds.size(); i++) {
                ps.setInt(i + 2, categoryIds.get(i));
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ===================== HELPER METHODS =====================

    // Helper methods (giữ nguyên)
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

    /**
     * Validate sort field để tránh SQL injection
     */
    private boolean isValidSortField(String sortField) {
        if (sortField == null) return false;

        String[] allowedFields = {"id", "name", "parent_name", "active_flag", "create_date", "update_date"};
        for (String field : allowedFields) {
            if (field.equals(sortField)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate sort direction để tránh SQL injection
     */
    private boolean isValidSortDirection(String sortDir) {
        return "asc".equalsIgnoreCase(sortDir) || "desc".equalsIgnoreCase(sortDir);
    }
}