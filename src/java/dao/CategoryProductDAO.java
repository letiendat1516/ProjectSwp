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

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private CategoryParentDAO categoryParentDAO = new CategoryParentDAO();

    // Lấy danh sách danh mục có phân trang, tìm kiếm và sắp xếp (với thông tin parent)
    public List<CategoryProduct> getAllCategoriesWithParent(int page, int pageSize, String searchKeyword, String sortField, String sortDir) {
        List<CategoryProduct> list = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT c.*, cp.name as parent_name "
                + "FROM category c "
                + "LEFT JOIN category_parent cp ON c.parent_id = cp.id "
                + "WHERE 1=1"
        );

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query.append(" AND c.name LIKE ?");
        }

        // Thêm phần sắp xếp
        if (sortField != null && !sortField.trim().isEmpty()) {
            String validSortField = "c.id";
            if ("name".equalsIgnoreCase(sortField)) {
                validSortField = "c.name";
            } else if ("parent_name".equalsIgnoreCase(sortField)) {
                validSortField = "cp.name";
            }

            String validSortDir = "desc".equalsIgnoreCase(sortDir) ? "desc" : "asc";
            query.append(" ORDER BY ").append(validSortField).append(" ").append(validSortDir);
        } else {
            query.append(" ORDER BY c.id ASC");
        }

        query.append(" LIMIT ?, ?");

        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query.toString());

            int paramIndex = 1;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchKeyword + "%");
            }

            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                // ✅ ĐÃ SỬA: Set parentName từ JOIN
                category.setParentName(rs.getString("parent_name"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy danh sách danh mục có phân trang, tìm kiếm và sắp xếp (không có parent info)
    public List<CategoryProduct> getAllCategories(int page, int pageSize, String searchKeyword, String sortField, String sortDir) {
        List<CategoryProduct> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM category WHERE 1=1");

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query.append(" AND name LIKE ?");
        }

        // Thêm phần sắp xếp
        if (sortField != null && !sortField.trim().isEmpty()) {
            String validSortField = "id";
            if ("name".equalsIgnoreCase(sortField)) {
                validSortField = "name";
            }

            String validSortDir = "desc".equalsIgnoreCase(sortDir) ? "desc" : "asc";
            query.append(" ORDER BY ").append(validSortField).append(" ").append(validSortDir);
        } else {
            query.append(" ORDER BY id ASC");
        }

        query.append(" LIMIT ?, ?");

        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query.toString());

            int paramIndex = 1;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchKeyword + "%");
            }

            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // Phương thức overload để tương thích ngược
    public List<CategoryProduct> getAllCategories(int page, int pageSize, String searchKeyword) {
        return getAllCategories(page, pageSize, searchKeyword, "id", "asc");
    }

    // Đếm tổng số danh mục (dùng cho phân trang)
    public int countCategories(String searchKeyword) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM category WHERE 1=1";

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query += " AND name LIKE ?";
        }

        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                ps.setString(1, "%" + searchKeyword + "%");
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    // Lấy danh mục theo ID
    public CategoryProduct getCategoryById(int id) {
        String query = "SELECT * FROM category WHERE id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    // ✅ THÊM METHOD MỚI: Lấy danh mục theo ID với thông tin parent
    public CategoryProduct getCategoryWithParentById(int id) {
        String query = "SELECT c.*, cp.name as parent_name " +
                       "FROM category c " +
                       "LEFT JOIN category_parent cp ON c.parent_id = cp.id " +
                       "WHERE c.id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                category.setParentName(rs.getString("parent_name"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    // Thêm danh mục mới (với validation parent_id)
    public boolean addCategory(CategoryProduct category) {
        // Validate parent_id trước khi thêm
        if (!isValidParentId(category.getParentId())) {
            System.out.println("Invalid parent_id: " + category.getParentId());
            return false;
        }

        String query = "INSERT INTO category (name, parent_id, active_flag) VALUES (?, ?, ?)";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category.getName());
            if (category.getParentId() != null) {
                ps.setInt(2, category.getParentId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setBoolean(3, category.isActiveFlag());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Cập nhật danh mục (với validation parent_id)
    public boolean updateCategory(CategoryProduct category) {
        // Validate parent_id trước khi update
        if (!isValidParentId(category.getParentId())) {
            System.out.println("Invalid parent_id: " + category.getParentId());
            return false;
        }

        String query = "UPDATE category SET name = ?, parent_id = ?, active_flag = ? WHERE id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category.getName());
            if (category.getParentId() != null) {
                ps.setInt(2, category.getParentId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setBoolean(3, category.isActiveFlag());
            ps.setInt(4, category.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Xóa danh mục
    public boolean deleteCategory(int id) {
        // Kiểm tra xem danh mục có được sử dụng làm parent_id không
        if (isCategoryUsedAsParent(id)) {
            System.out.println("Cannot delete category: it's being used as parent");
            return false;
        }
        // Kiểm tra xem danh mục có được sử dụng trong bảng product_info không
        if (isCategoryUsedInProduct(id)) {
            System.out.println("Cannot delete category: it's being used in products");
            return false;
        }

        String query = "DELETE FROM category WHERE id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Kiểm tra xem danh mục có được sử dụng làm parent_id không
    private boolean isCategoryUsedAsParent(int id) {
        String query = "SELECT COUNT(*) FROM category WHERE parent_id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // Kiểm tra xem danh mục có được sử dụng trong bảng product_info không
    private boolean isCategoryUsedInProduct(int id) {
        String query = "SELECT COUNT(*) FROM product_info WHERE cate_id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // Lấy tất cả danh mục (không phân trang) - dùng để hiển thị dropdown
    public List<CategoryProduct> getAllCategoriesForDropdown() {
        List<CategoryProduct> list = new ArrayList<>();
        String query = "SELECT * FROM category WHERE active_flag = 1 ORDER BY name";

        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy danh sách danh mục cha cho dropdown
    public List<CategoryProductParent> getCategoryParentsForDropdown() {
        return categoryParentDAO.getAllCategoryParents();
    }

    // Kiểm tra parent_id có hợp lệ không
    public boolean isValidParentId(Integer parentId) {
        if (parentId == null) {
            return true; // NULL là hợp lệ
        }
        return categoryParentDAO.getCategoryParentById(parentId) != null;
    }

    // Lấy danh mục theo parent_id
    public List<CategoryProduct> getCategoriesByParentId(Integer parentId) {
        List<CategoryProduct> list = new ArrayList<>();
        String query = "SELECT * FROM category WHERE parent_id ";
        
        if (parentId == null) {
            query += "IS NULL";
        } else {
            query += "= ?";
        }
        query += " AND active_flag = 1 ORDER BY name";

        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            
            if (parentId != null) {
                ps.setInt(1, parentId);
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryProduct category = new CategoryProduct();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // Đóng các tài nguyên
    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
