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
        String sql = "SELECT r.id AS request_id, p.name AS product_name, ri.product_code AS product_code, "
                + "ri.unit AS unit, ri.quantity, ri.imported_qty, ri.id, ri.note, ri.reason_detail "
                + "FROM request_items ri "
                + "JOIN request r ON ri.request_id = r.id "
                + "JOIN product_info p ON ri.product_code = p.code";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                RequestItem item = new RequestItem();
                item.setId(rs.getString("id"));
                item.setRequestId(rs.getString("request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setImportedQty(rs.getDouble("imported_qty"));
                item.setNote(rs.getString("note"));
                item.setReasonDetail(rs.getString("reason_detail"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<ApprovedRequestItem> getApprovedRequestItems(String search) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        String sql = "SELECT r.id AS request_id, r.day_request, r.status, r.supplier, r.address, r.phone, r.email, "
                + "ri.product_name, ri.product_code, pi.name AS product_full_name, pi.price, ri.unit, ri.quantity, "
                + "ri.note, ri.reason_detail "
                + "FROM request r "
                + "JOIN request_items ri ON r.id = ri.request_id "
                + "JOIN product_info pi ON ri.product_code = pi.code "
                + "WHERE r.status = 'approved' "
                + (search != null && !search.trim().isEmpty() 
                    ? "AND (ri.product_code LIKE ? OR ri.product_name LIKE ?)" 
                    : "")
                + " ORDER BY r.id, ri.product_name";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.trim() + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
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
                item.setReasonDetail(rs.getString("reason_detail"));
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public Request getRequestById(String id) {
        Request req = null;
        String sql = "SELECT * FROM request WHERE id = ?";
        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                req = new Request();
                req.setId(rs.getString("id"));
                req.setUser_id(rs.getInt("user_id"));
                req.setDay_request(rs.getDate("day_request"));
                req.setStatus(rs.getString("status"));
                req.setReason(rs.getString("reason"));
                req.setSupplier(rs.getString("supplier"));
                req.setAddress(rs.getString("address"));
                req.setPhone(rs.getString("phone"));
                req.setEmail(rs.getString("email"));
                req.setApprove_by(rs.getString("approve_by"));
                req.setWarehouse(rs.getString("warehouse"));
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
        String sql = "SELECT * FROM request_items WHERE request_id = ?";
        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();
            while (rs.next()) {
                RequestItem item = new RequestItem();
                item.setId(rs.getString("id"));
                item.setRequestId(rs.getString("request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setImportedQty(rs.getDouble("imported_qty"));
                item.setNote(rs.getString("note"));
                item.setReasonDetail(rs.getString("reason_detail"));
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
        String sql = "UPDATE request_items SET imported_qty = ?, note = ? WHERE request_id = ? AND product_code = ?";
        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, importQty);
            ps.setString(2, note);
            ps.setString(3, requestId);
            ps.setString(4, productCode);
            ps.executeUpdate();
        }
    }

    public void updateRequestStatus(String requestId, String status, String importDate, String receiver, String warehouse) throws SQLException {
        String sql = "UPDATE request SET status = ?, day_request = ?, approve_by = ?, warehouse = ? WHERE id = ?";
        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, importDate != null ? importDate : null);
            ps.setString(3, receiver != null ? receiver : null);
            ps.setString(4, warehouse != null ? warehouse : null);
            ps.setString(5, requestId);
            ps.executeUpdate();
        }
    }
}