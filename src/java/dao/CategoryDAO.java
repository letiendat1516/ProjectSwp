package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import DBContext.Context;

public class CategoryDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    // Lấy danh sách danh mục có phân trang, tìm kiếm và sắp xếp
    public List<Category> getAllCategories(int page, int pageSize, String searchKeyword, String sortField, String sortDir) {
        List<Category> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM category WHERE 1=1");
        
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query.append(" AND name LIKE ?");
        }
        
        // Thêm phần sắp xếp
        if (sortField != null && !sortField.trim().isEmpty()) {
            // Đảm bảo sortField hợp lệ để tránh SQL Injection
            String validSortField = "id"; // Mặc định là id
            if ("name".equalsIgnoreCase(sortField)) {
                validSortField = "name";
            }
            
            // Đảm bảo sortDir hợp lệ
            String validSortDir = "asc"; // Mặc định là asc
            if ("desc".equalsIgnoreCase(sortDir)) {
                validSortDir = "desc";
            }
            
            query.append(" ORDER BY ").append(validSortField).append(" ").append(validSortDir);
        } else {
            // Sắp xếp mặc định nếu không có trường sắp xếp
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
                Category category = new Category();
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
    public List<Category> getAllCategories(int page, int pageSize, String searchKeyword) {
        // Gọi phương thức mới với sắp xếp mặc định
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
    public Category getCategoryById(int id) {
        String query = "SELECT * FROM category WHERE id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Category category = new Category();
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
    
    // Thêm danh mục mới
    public boolean addCategory(Category category) {
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
    
    // Cập nhật danh mục
    public boolean updateCategory(Category category) {
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
            return false; // Không thể xóa nếu đang được sử dụng làm danh mục cha
        }
        // Kiểm tra xem danh mục có được sử dụng trong bảng product_info không
        if (isCategoryUsedInProduct(id)) {
            return false; // Không thể xóa nếu đang được sử dụng trong sản phẩm
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
    
    // Lấy tất cả danh mục (không phân trang) - dùng để hiển thị dropdown danh mục cha
    public List<Category> getAllCategoriesForDropdown() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM category ORDER BY name";
        
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
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
