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
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    // ==================== READ OPERATIONS ====================
    
    // Lấy tất cả danh mục cha (cho dropdown)
    public List<CategoryProductParent> getAllCategoryParents() {
        List<CategoryProductParent> list = new ArrayList<>();
        String query = "SELECT * FROM category_parent WHERE active_flag = 1 ORDER BY name";
        
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryProductParent categoryParent = new CategoryProductParent();
                categoryParent.setId(rs.getInt("id"));
                categoryParent.setName(rs.getString("name"));
                categoryParent.setDescription(rs.getString("description"));
                categoryParent.setActiveFlag(rs.getBoolean("active_flag"));
                list.add(categoryParent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }
    
    // Lấy danh sách có phân trang, tìm kiếm và sắp xếp
    public List<CategoryProductParent> getAllCategoryParents(int page, int pageSize, String searchKeyword, String sortField, String sortDir) {
        List<CategoryProductParent> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM category_parent WHERE 1=1");
        
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query.append(" AND (name LIKE ? OR description LIKE ?)");
        }
        
        // Thêm phần sắp xếp
        if (sortField != null && !sortField.trim().isEmpty()) {
            String validSortField = "id";
            if ("name".equalsIgnoreCase(sortField)) {
                validSortField = "name";
            } else if ("description".equalsIgnoreCase(sortField)) {
                validSortField = "description";
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
                String searchPattern = "%" + searchKeyword + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                CategoryProductParent categoryParent = new CategoryProductParent();
                categoryParent.setId(rs.getInt("id"));
                categoryParent.setName(rs.getString("name"));
                categoryParent.setDescription(rs.getString("description"));
                categoryParent.setActiveFlag(rs.getBoolean("active_flag"));
                list.add(categoryParent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }
    
    // Phương thức overload để tương thích ngược
    public List<CategoryProductParent> getAllCategoryParents(int page, int pageSize, String searchKeyword) {
        return getAllCategoryParents(page, pageSize, searchKeyword, "id", "asc");
    }
    
    // Đếm tổng số danh mục cha (dùng cho phân trang)
    public int countCategoryParents(String searchKeyword) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM category_parent WHERE 1=1");
        
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query.append(" AND (name LIKE ? OR description LIKE ?)");
        }
        
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query.toString());
            
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String searchPattern = "%" + searchKeyword + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
            }
            
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }
    
    // Lấy danh mục cha theo ID
    public CategoryProductParent getCategoryParentById(int id) {
        String query = "SELECT * FROM category_parent WHERE id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                CategoryProductParent categoryParent = new CategoryProductParent();
                categoryParent.setId(rs.getInt("id"));
                categoryParent.setName(rs.getString("name"));
                categoryParent.setDescription(rs.getString("description"));
                categoryParent.setActiveFlag(rs.getBoolean("active_flag"));
                return categoryParent;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }
    
    // ==================== CREATE OPERATION ====================
    
    // Thêm danh mục cha mới
    public boolean addCategoryParent(CategoryProductParent categoryParent) {
        // Kiểm tra tên đã tồn tại chưa
        if (isCategoryParentNameExists(categoryParent.getName(), null)) {
            System.out.println("Category parent name already exists: " + categoryParent.getName());
            return false;
        }
        
        String query = "INSERT INTO category_parent (name, description, active_flag) VALUES (?, ?, ?)";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, categoryParent.getName());
            ps.setString(2, categoryParent.getDescription());
            ps.setBoolean(3, categoryParent.isActiveFlag());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }
    
    // ==================== UPDATE OPERATION ====================
    
    // Cập nhật danh mục cha
    public boolean updateCategoryParent(CategoryProductParent categoryParent) {
        // Kiểm tra tên đã tồn tại chưa (trừ chính nó)
        if (isCategoryParentNameExists(categoryParent.getName(), categoryParent.getId())) {
            System.out.println("Category parent name already exists: " + categoryParent.getName());
            return false;
        }
        
        String query = "UPDATE category_parent SET name = ?, description = ?, active_flag = ? WHERE id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, categoryParent.getName());
            ps.setString(2, categoryParent.getDescription());
            ps.setBoolean(3, categoryParent.isActiveFlag());
            ps.setInt(4, categoryParent.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }
    
    // ==================== DELETE OPERATION ====================
    
    // Xóa danh mục cha (kiểm tra ràng buộc)
    public boolean deleteCategoryParent(int id) {
        // Kiểm tra xem có danh mục con nào đang sử dụng không
        if (isParentUsedInCategory(id)) {
            System.out.println("Cannot delete parent category: it's being used by child categories");
            return false;
        }
        
        String query = "DELETE FROM category_parent WHERE id = ?";
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
    
    // Soft delete - chỉ set active_flag = false
    public boolean softDeleteCategoryParent(int id) {
        String query = "UPDATE category_parent SET active_flag = false WHERE id = ?";
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
    
    // ==================== VALIDATION METHODS ====================
    
    // Kiểm tra parent có đang được sử dụng không
    private boolean isParentUsedInCategory(int parentId) {
        String query = "SELECT COUNT(*) FROM category WHERE parent_id = ?";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, parentId);
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
    
    // Kiểm tra tên danh mục cha đã tồn tại chưa
    private boolean isCategoryParentNameExists(String name, Integer excludeId) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM category_parent WHERE name = ?");
        if (excludeId != null) {
            query.append(" AND id != ?");
        }
        
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query.toString());
            ps.setString(1, name);
            if (excludeId != null) {
                ps.setInt(2, excludeId);
            }
            
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
    
    // ==================== UTILITY METHODS ====================
    
    // Lấy số lượng danh mục con của một parent
    public int getChildCategoryCount(int parentId) {
        String query = "SELECT COUNT(*) FROM category WHERE parent_id = ? AND active_flag = 1";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, parentId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }
    
    // Toggle active flag
    public boolean toggleActiveFlag(int id) {
        String query = "UPDATE category_parent SET active_flag = NOT active_flag WHERE id = ?";
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
    
    // Đóng các tài nguyên
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
