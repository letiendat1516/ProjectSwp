package dao;

import model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import DBContext.Context;
import model.ParentCategoryDTO;

public class CategoryDAO {

    // Lấy danh sách danh mục có phân trang và tìm kiếm
    public List<Category> getAllCategories(int page, int pageSize, String searchKeyword) {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "SELECT c.*, p.name as parent_name FROM categories c "
                    + "LEFT JOIN categories p ON c.parent_id = p.id "
                    + "WHERE c.name LIKE ? "
                    + "ORDER BY c.id "
                    + "LIMIT ? OFFSET ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + searchKeyword + "%");
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);

            rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setParentName(rs.getString("parent_name"));
                category.setActiveFlag(rs.getBoolean("active_flag"));

                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return categories;
    }

    // Đếm tổng số danh mục (dùng cho phân trang)
    public int countCategories(String searchKeyword) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "SELECT COUNT(*) FROM categories WHERE name LIKE ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + searchKeyword + "%");

            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return count;
    }

    // Lấy danh sách danh mục cha (dùng cho dropdown)
    public List<Category> getParentCategories() {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new Context().getJDBCConnection();

            // Chỉ lấy các danh mục đang hoạt động và không phải danh mục con
            String sql = "SELECT * FROM categories WHERE active_flag = 1 ORDER BY name";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));

                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return categories;
    }

    // Thêm danh mục mới
    public boolean addCategory(Category category) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "INSERT INTO categories (name, parent_id, active_flag) VALUES (?, ?, ?)";

            ps = conn.prepareStatement(sql);
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
            closeResources(conn, ps, null);
        }
    }

    // Cập nhật danh mục
    public boolean updateCategory(Category category) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "UPDATE categories SET name = ?, parent_id = ?, active_flag = ? WHERE id = ?";

            ps = conn.prepareStatement(sql);
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
            closeResources(conn, ps, null);
        }
    }

    // Xóa danh mục
    public boolean deleteCategory(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "DELETE FROM categories WHERE id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    // Thay đổi trạng thái danh mục
    public boolean toggleCategoryStatus(int id, boolean status) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "UPDATE categories SET active_flag = ? WHERE id = ?";

            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, status);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    // Kiểm tra xem danh mục có danh mục con không
    public boolean hasChildCategories(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "SELECT COUNT(*) FROM categories WHERE parent_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return false;
    }

    // Kiểm tra xem danh mục có đang được sử dụng không
    // Ví dụ: Kiểm tra trong bảng sản phẩm
    public boolean isInUse(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new Context().getJDBCConnection();

            // Giả sử có bảng products có trường category_id
            String sql = "SELECT COUNT(*) FROM products WHERE category_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return false;
    }

    // Đóng tài nguyên
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
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
    // Lấy danh sách danh mục cha kèm số lượng danh mục con

    public List<ParentCategoryDTO> getParentCategoriesWithChildCount() {
        List<ParentCategoryDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "SELECT c.*, "
                    + "(SELECT COUNT(*) FROM categories WHERE parent_id = c.id) AS child_count "
                    + "FROM categories c "
                    + "WHERE c.parent_id IS NULL OR c.parent_id = 0 "
                    + "ORDER BY c.name ASC";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ParentCategoryDTO category = new ParentCategoryDTO();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setActiveFlag(rs.getBoolean("active_flag"));
                category.setChildCount(rs.getInt("child_count"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }
// Lấy danh mục theo ID
    public Category getCategoryById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Category category = null;

        try {
            conn = new Context().getJDBCConnection();

            String sql = "SELECT c.*, p.name as parent_name FROM categories c "
                    + "LEFT JOIN categories p ON c.parent_id = p.id "
                    + "WHERE c.id = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                category.setParentName(rs.getString("parent_name"));
                category.setActiveFlag(rs.getBoolean("active_flag"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return category;
    }

}
