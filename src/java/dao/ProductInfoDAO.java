package dao;

import DBContext.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ProductInfo;

public class ProductInfoDAO {

    public List<ProductInfo> getAllProducts() {
        List<ProductInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM product_info";

        try (
            Connection con = Context.getJDBCConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                ProductInfo p = new ProductInfo();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setCode(rs.getString("code"));
                p.setCate_id(rs.getInt("cate_id"));
                p.setUnit_id(rs.getInt("unit_id"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStatus(rs.getString("status"));
                p.setDescription(rs.getString("description"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
