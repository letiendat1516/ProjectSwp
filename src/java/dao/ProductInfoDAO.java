package dao;

import DBContext.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ProductInfo;

public class ProductInfoDAO {

    /**
     * Lấy danh sách tất cả sản phẩm từ database
     */
    public List<ProductInfo> getAllProducts() {
        List<ProductInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM product_info";

        // Sử dụng try-with-resources để tự động đóng connection
        try (
                Connection con = Context.getJDBCConnection(); 
                PreparedStatement stmt = con.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery();) {
            
            // Duyệt qua từng record và mapping vào ProductInfo object
            while (rs.next()) {
                ProductInfo p = new ProductInfo();
                p.setId(rs.getInt("id"));                           // ID sản phẩm
                p.setName(rs.getString("name"));                    // Tên sản phẩm
                p.setCode(rs.getString("code"));                    // Mã sản phẩm
                p.setCate_id(rs.getInt("cate_id"));                // ID danh mục
                p.setUnit_id(rs.getInt("unit_id"));                // ID đơn vị tính
                p.setPrice(rs.getBigDecimal("price"));             // Giá sản phẩm
                p.setStatus(rs.getString("status"));               // Trạng thái (active/inactive)
                p.setDescription(rs.getString("description"));     // Mô tả sản phẩm
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Lấy tên sản phẩm theo ID
     */
    public String getProductNameById(int productId) {
        String sql = "SELECT name FROM product_info WHERE id = ?";
        
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productId);  // Set parameter để tránh SQL injection
            ResultSet rs = stmt.executeQuery();

            // Nếu tìm thấy sản phẩm, trả về tên
            if (rs.next()) {
                return rs.getString("name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "";  // Trả về chuỗi rỗng nếu không tìm thấy hoặc có lỗi
    }

}