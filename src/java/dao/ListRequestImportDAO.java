package dao;

import DBContext.Context;
import model.RequestItem;
import model.ApprovedRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Request;

public class ListRequestImportDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

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

    public List<RequestItem> getAllRequestItems() {
        List<RequestItem> list = new ArrayList<>();
        String sql = "SELECT poi.id AS request_id, poi.product_name, poi.product_code, "
                + "poi.unit, poi.quantity, poi.note "
                + "FROM purchase_order_items poi "
                + "JOIN purchase_order_info po ON poi.purchase_id = po.id";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                RequestItem item = new RequestItem();
                item.setId(rs.getInt("id"));
                item.setRequestId(rs.getString("request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setImportedQty(0); // Not in schema
                item.setNote(rs.getString("note"));
                item.setReasonDetail(null); // Not in schema
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<ApprovedRequestItem> getApprovedRequestItems(String searchType, String searchValue) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        String sql = "SELECT po.id AS request_id, po.day_purchase AS day_request, po.status, po.supplier, po.address, po.phone, po.email, "
                + "poi.product_name, poi.product_code, poi.product_name AS product_full_name, poi.price_per_unit AS price, poi.unit, poi.quantity, "
                + "poi.note "
                + "FROM purchase_order_info po "
                + "LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id "
                + "WHERE po.status = 'approved' "
                + (searchValue != null && !searchValue.trim().isEmpty() 
                    ? "AND (" 
                    + (searchType != null && searchType.equals("requestId") ? "po.id LIKE ?" : "FALSE")
                    + " OR " 
                    + (searchType != null && searchType.equals("productName") ? "poi.product_name LIKE ?" : "FALSE")
                    + " OR " 
                    + (searchType != null && searchType.equals("productCode") ? "poi.product_code LIKE ?" : "FALSE")
                    + ")" 
                    : "")
                + " ORDER BY po.day_purchase DESC";

        try {
            System.out.println(sql);
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
                String searchPattern = "%" + searchValue.trim() + "%";
                if (searchType.equals("requestId") || searchType.equals("productName") || searchType.equals("productCode")) {
                    ps.setString(1, searchPattern);
                }
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = new ApprovedRequestItem();
                item.setRequestId(rs.getString("request_id"));
                item.setDayRequest(rs.getString("day_request"));
                item.setStatus(rs.getString("status"));
                item.setSupplier(rs.getString("supplier"));
                item.setAddress(rs.getString("address"));
                item.setPhone(rs.getString("phone"));
                item.setEmail(rs.getString("email"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setProductFullName(rs.getString("product_full_name"));
                item.setPrice(rs.getDouble("price"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setNote(rs.getString("note"));
                item.setReasonDetail(null); // Not in schema
                list.add(item);
            }
            System.out.println("Fetched approved items count: " + list.size() + ", Items: " + list);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<ApprovedRequestItem> getCompletedRequestItems(String searchType, String searchValue) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        String sql = "SELECT po.id AS request_id, po.day_purchase AS day_request, po.status, po.supplier, po.address, po.phone, po.email, "
                + "poi.product_name, poi.product_code, poi.product_name AS product_full_name, poi.price_per_unit AS price, poi.unit, poi.quantity, "
                + "poi.note "
                + "FROM purchase_order_info po "
                + "LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id "
                + "WHERE po.status = 'completed' "
                + (searchValue != null && !searchValue.trim().isEmpty() 
                    ? "AND (" 
                    + (searchType != null && searchType.equals("requestId") ? "po.id LIKE ?" : "FALSE")
                    + " OR " 
                    + (searchType != null && searchType.equals("productName") ? "poi.product_name LIKE ?" : "FALSE")
                    + " OR " 
                    + (searchType != null && searchType.equals("productCode") ? "poi.product_code LIKE ?" : "FALSE")
                    + ")" 
                    : "")
                + " ORDER BY po.day_purchase DESC";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
                String searchPattern = "%" + searchValue.trim() + "%";
                if (searchType.equals("requestId") || searchType.equals("productName") || searchType.equals("productCode")) {
                    ps.setString(1, searchPattern);
                }
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = new ApprovedRequestItem();
                item.setRequestId(rs.getString("request_id"));
                item.setDayRequest(rs.getString("day_request"));
                item.setStatus(rs.getString("status"));
                item.setSupplier(rs.getString("supplier"));
                item.setAddress(rs.getString("address"));
                item.setPhone(rs.getString("phone"));
                item.setEmail(rs.getString("email"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setProductFullName(rs.getString("product_full_name"));
                item.setPrice(rs.getDouble("price"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setNote(rs.getString("note"));
                item.setReasonDetail(null); // Not in schema
                list.add(item);
            }
            System.out.println("Fetched completed items count: " + list.size() + ", Items: " + list);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public Request getRequestById(String id) {
        Request req = null;
        String sql = "SELECT * FROM purchase_order_info WHERE id = ?";
        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                req = new Request();
                req.setId(rs.getString("id"));
                req.setUser_id(0); // Not in schema
                req.setDay_request(rs.getDate("day_purchase"));
                req.setStatus(rs.getString("status"));
                req.setReason(rs.getString("reason"));
                req.setSupplier(rs.getString("supplier"));
                req.setAddress(rs.getString("address"));
                req.setPhone(rs.getString("phone"));
                req.setEmail(rs.getString("email"));
                req.setApprove_by(null); // Not in schema
                req.setWarehouse(null); // Not in schema
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return req;
    }

    public List<RequestItem> getRequestItemsByRequestId(String requestId) {
        List<RequestItem> list = new ArrayList<>();
        String sql = "SELECT * FROM purchase_order_items WHERE purchase_id = ?";
        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();
            while (rs.next()) {
                RequestItem item = new RequestItem();
                item.setId(rs.getInt("id"));
                item.setRequestId(rs.getString("purchase_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setImportedQty(0); // Not in schema
                item.setNote(rs.getString("note"));
                item.setReasonDetail(null); // Not in schema
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public void updateImportItem(String requestId, String productCode, int importQty, String note) throws SQLException {
        String sql = "UPDATE purchase_order_items SET quantity = ?, note = ? WHERE purchase_id = ? AND product_code = ?";
        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, importQty);
            ps.setString(2, note);
            ps.setString(3, requestId);
            ps.setString(4, productCode);
            ps.executeUpdate();
        }
    }

    public void updateRequestStatus(String requestId, String status, String importDate) throws SQLException {
        String sql = "UPDATE purchase_order_info SET status = ?, day_purchase = ? WHERE id = ?";
        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, importDate);
            ps.setString(3, requestId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No rows updated for requestId: " + requestId);
            }
        }
    }
    
    public static void main(String[] args) {
        ListRequestImportDAO leid = new ListRequestImportDAO();
        List<ApprovedRequestItem> l = leid.getApprovedRequestItems(null, null);
    }
}