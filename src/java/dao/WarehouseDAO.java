/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ProductInfo;
import model.Request;
import model.RequestItem;
import model.Supplier;

public class WarehouseDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/mydb";
    private String jdbcUsername = "root";
    private String jdbcPassword = "your_password";

    private static final String SELECT_REQUEST_BY_ID
            = "SELECT r.id, r.created_at, u.username, r.status, r.reason "
            + "FROM request r "
            + "JOIN user u ON r.user_id = u.id "
            + "WHERE r.id = ?";

    private static final String SELECT_REQUEST_ITEMS
            = "SELECT ri.id, ri.request_id, ri.product_id, ri.quantity AS requested_qty, "
            + "pi.code, pi.name, pi.unit_id, pis.qty AS stock_qty "
            + "FROM request_item ri "
            + "JOIN product_info pi ON ri.product_id = pi.id "
            + "LEFT JOIN product_in_stock pis ON pi.id = pis.product_id "
            + "WHERE ri.request_id = ?";

    private static final String SELECT_SUPPLIER
            = "SELECT id, name, address, phone, email FROM supplier WHERE id = ?"; // Assuming supplier table

    protected Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Request getRequestById(int requestId) throws SQLException {
        Request request = null;
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_REQUEST_BY_ID)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("username");
                String status = rs.getString("status");
                String reason = rs.getString("reason");
                Date createdAt = rs.getDate("created_at");
                request = new Request(id, userName, "import", status, reason, createdAt);
            }
        }
        return request;
    }

    public List<RequestItem> getRequestItems(int requestId) throws SQLException {
        List<RequestItem> items = new ArrayList<>();
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_REQUEST_ITEMS)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int productId = rs.getInt("product_id");
                double quantity = rs.getDouble("requested_qty");
                RequestItem item = new RequestItem(id, requestId, productId, quantity);
                items.add(item);
            }
        }
        return items;
    }

    public ProductInfo getProductInfo(int productId) throws SQLException {
        ProductInfo product = null;
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(
                "SELECT pi.id, pi.code, pi.name, pi.unit_id, pis.qty AS stock_qty "
                + "FROM product_info pi "
                + "LEFT JOIN product_in_stock pis ON pi.id = pis.product_id "
                + "WHERE pi.id = ?")) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String unit = "Unit"; // Replace with actual unit lookup if needed
                double stockQty = rs.getDouble("stock_qty");
                product = new ProductInfo(id, code, name, unit, stockQty);
            }
        }
        return product;
    }

    public Supplier getSupplier(int supplierId) throws SQLException {
        Supplier supplier = null;
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_SUPPLIER)) {
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                supplier = new Supplier(id, name, address, phone, email);
            }
        }
        return supplier;
    }
}
