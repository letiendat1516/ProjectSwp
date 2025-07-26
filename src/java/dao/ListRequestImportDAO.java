<<<<<<< HEAD
//package dao;
//
//import DBContext.Context;
//import model.RequestItem;
//import model.ApprovedRequestItem;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import model.Request;
//
//public class ListRequestImportDAO {
//
//    private Connection conn = null;
//    private PreparedStatement ps = null;
//    private ResultSet rs = null;
//
//    private void closeResources() {
//        try {
//            if (rs != null) {
//                rs.close();
//            }
//            if (ps != null) {
//                ps.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<ApprovedRequestItem> getApprovedRequestItems(String searchType, String searchValue) {
//        List<ApprovedRequestItem> list = new ArrayList<>();
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("SELECT po.id AS request_id, po.day_purchase AS day_request, po.status, po.supplier, ")
//                .append("po.address, po.phone, po.email, poi.product_name, poi.product_code, ")
//                .append("poi.product_name AS product_full_name, poi.price_per_unit AS price, poi.unit, ")
//                .append("poi.quantity, poi.note ")
//                .append("FROM purchase_order_info po ")
//                .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
//                .append("WHERE po.status = 'approved' ");
//
//        // Thêm điều kiện tìm kiếm
//        if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
//            switch (searchType) {
//                case "requestId":
//                    sql.append("AND po.id LIKE ? ");
//                    break;
//                case "productName":
//                    sql.append("AND poi.product_name LIKE ? ");
//                    break;
//                case "productCode":
//                    sql.append("AND poi.product_code LIKE ? ");
//                    break;
//            }
//        }
//
//        sql.append("ORDER BY po.day_purchase DESC");
//
//        try {
//            conn = Context.getJDBCConnection();
//            ps = conn.prepareStatement(sql.toString());
//
//            // Set parameter nếu có tìm kiếm
//            int paramIndex = 1;
//            if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
//                String searchPattern = "%" + searchValue.trim() + "%";
//                ps.setString(paramIndex, searchPattern);
//            }
//
//            System.out.println("Executing SQL: " + sql.toString()); // Debug log
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                ApprovedRequestItem item = new ApprovedRequestItem();
//                item.setRequestId(rs.getString("request_id"));
//                item.setDayRequest(rs.getString("day_request"));
//                item.setStatus(rs.getString("status"));
//                item.setSupplier(rs.getString("supplier"));
//                item.setAddress(rs.getString("address"));
//                item.setPhone(rs.getString("phone"));
//                item.setEmail(rs.getString("email"));
//                item.setProductName(rs.getString("product_name"));
//                item.setProductCode(rs.getString("product_code"));
//                item.setProductFullName(rs.getString("product_full_name"));
//
//                // Xử lý giá trị null cho price
//                Double price = rs.getDouble("price");
//                item.setPrice(rs.wasNull() ? 0.0 : price);
//
//                item.setUnit(rs.getString("unit"));
//
//                // Xử lý giá trị null cho quantity
//                Double quantity = rs.getDouble("quantity");
//                item.setQuantity(rs.wasNull() ? 0.0 : quantity);
//
//                item.setNote(rs.getString("note"));
//                item.setReasonDetail(null);
//                list.add(item);
//            }
//
//            System.out.println("Fetched approved items count: " + list.size());
//
//        } catch (SQLException e) {
//            System.err.println("Error in getApprovedRequestItems: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//        return list;
//    }
//
//    public List<ApprovedRequestItem> getCompletedRequestItems(String searchType, String searchValue) {
//        List<ApprovedRequestItem> list = new ArrayList<>();
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("SELECT po.id AS request_id, po.day_purchase AS day_request, po.status, po.supplier, ")
//                .append("po.address, po.phone, po.email, po.reason, poi.product_name, poi.product_code, ")
//                .append("poi.product_name AS product_full_name, poi.price_per_unit AS price, poi.unit, ")
//                .append("poi.quantity, poi.note ")
//                .append("FROM purchase_order_info po ")
//                .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
//                .append("WHERE po.status IN ('completed', 'rejected') ");
//
//        // Thêm điều kiện tìm kiếm - BỔ SUNG THÊM SUPPLIER
//        if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
//            switch (searchType) {
//                case "requestId":
//                    sql.append("AND po.id LIKE ? ");
//                    break;
//                case "productName":
//                    sql.append("AND poi.product_name LIKE ? ");
//                    break;
//                case "productCode":
//                    sql.append("AND poi.product_code LIKE ? ");
//                    break;
//                case "supplier":  // THÊM CASE MỚI
//                    sql.append("AND po.supplier LIKE ? ");
//                    break;
//            }
//        }
//
//        sql.append("ORDER BY po.day_purchase DESC");
//
//        try {
//            conn = Context.getJDBCConnection();
//            ps = conn.prepareStatement(sql.toString());
//
//            // Set parameter nếu có tìm kiếm
//            if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
//                String searchPattern = "%" + searchValue.trim() + "%";
//                ps.setString(1, searchPattern);
//            }
//
//            System.out.println("Executing completed items SQL: " + sql.toString()); // Debug log
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                ApprovedRequestItem item = new ApprovedRequestItem();
//                item.setRequestId(rs.getString("request_id"));
//                item.setDayRequest(rs.getString("day_request"));
//                item.setStatus(rs.getString("status"));
//                item.setSupplier(rs.getString("supplier"));
//                item.setAddress(rs.getString("address"));
//                item.setPhone(rs.getString("phone"));
//                item.setEmail(rs.getString("email"));
//                item.setProductName(rs.getString("product_name"));
//                item.setProductCode(rs.getString("product_code"));
//                item.setProductFullName(rs.getString("product_full_name"));
//
//                // Xử lý giá trị null cho price
//                Double price = rs.getDouble("price");
//                item.setPrice(rs.wasNull() ? 0.0 : price);
//
//                item.setUnit(rs.getString("unit"));
//
//                // Xử lý giá trị null cho quantity
//                Double quantity = rs.getDouble("quantity");
//                item.setQuantity(rs.wasNull() ? 0.0 : quantity);
//
//                item.setNote(rs.getString("note"));
//                item.setReasonDetail(rs.getString("reason"));
//                list.add(item);
//            }
//
//            System.out.println("Fetched completed/rejected items count: " + list.size());
//
//        } catch (SQLException e) {
//            System.err.println("Error in getCompletedRequestItems: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//        return list;
//    }
//
//    public List<RequestItem> getAllRequestItems() {
//        List<RequestItem> list = new ArrayList<>();
//        String sql = "SELECT poi.id AS request_id, poi.product_name, poi.product_code, "
//                + "poi.unit, poi.quantity, poi.note "
//                + "FROM purchase_order_items poi "
//                + "JOIN purchase_order_info po ON poi.purchase_id = po.id";
//
//        try {
//            conn = Context.getJDBCConnection();
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                RequestItem item = new RequestItem();
//                item.setId(rs.getInt("request_id"));
//                item.setRequestId(rs.getString("request_id"));
//                item.setProductName(rs.getString("product_name"));
//                item.setProductCode(rs.getString("product_code"));
//                item.setUnit(rs.getString("unit"));
//                item.setQuantity(rs.getDouble("quantity"));
//                item.setImportedQty(0);
//                item.setNote(rs.getString("note"));
//                item.setReasonDetail(null);
//                list.add(item);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//        return list;
//    }
//
//
//    public List<RequestItem> getRequestItemsByRequestId(String requestId) {
//        List<RequestItem> list = new ArrayList<>();
//        String sql = "SELECT * FROM purchase_order_items WHERE purchase_id = ?";
//        try {
//            conn = Context.getJDBCConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, requestId);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                RequestItem item = new RequestItem();
//                item.setId(rs.getInt("id"));
//                item.setRequestId(rs.getString("purchase_id"));
//                item.setProductName(rs.getString("product_name"));
//                item.setProductCode(rs.getString("product_code"));
//                item.setUnit(rs.getString("unit"));
//                item.setQuantity(rs.getDouble("quantity"));
//                item.setImportedQty(0);
//                item.setNote(rs.getString("note"));
//                item.setReasonDetail(null);
//                list.add(item);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//        return list;
//    }
//
//    public void updateImportItem(String requestId, String productCode, int importQty, String note) throws SQLException {
//        String sql = "UPDATE purchase_order_items SET quantity = ?, note = ? WHERE purchase_id = ? AND product_code = ?";
//        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, importQty);
//            ps.setString(2, note);
//            ps.setString(3, requestId);
//            ps.setString(4, productCode);
//            ps.executeUpdate();
//        }
//    }
//
//    public void updateRequestStatus(String requestId, String status, String importDate) throws SQLException {
//        String sql = "UPDATE purchase_order_info SET status = ?, day_purchase = ? WHERE id = ?";
//        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, status);
//            ps.setString(2, importDate);
//            ps.setString(3, requestId);
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected == 0) {
//                throw new SQLException("No rows updated for requestId: " + requestId);
//            }
//        }
//    }
//
//    public void updateRequestStatusWithDetails(String requestId, String status, String importDate,
//            String warehouse, String receiver, String additionalNote) throws SQLException {
//        String sql = "UPDATE request SET status = ?, import_date = ?, warehouse = ?, receiver = ?, additional_note = ?, updated_at = NOW() WHERE id = ?";
//
//        try {
//            conn = new Context().getJDBCConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, status);
//            ps.setString(2, importDate);
//            ps.setString(3, warehouse);
//            ps.setString(4, receiver);
//            ps.setString(5, additionalNote);
//            ps.setString(6, requestId);
//
//            int result = ps.executeUpdate();
//            if (result == 0) {
//                throw new SQLException("Failed to update request status. No rows affected.");
//            }
//        } finally {
//            closeResources();
//        }
//    }
//
//    public void updateRequestStatusWithReason(String requestId, String status, String rejectReason) throws SQLException {
//        String sql = "UPDATE purchase_order_info SET status = ?, reason = ? WHERE id = ?";
//
//        try (Connection conn = Context.getJDBCConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, status);
//            ps.setString(2, rejectReason);
//            ps.setString(3, requestId);
//
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected == 0) {
//                throw new SQLException("No rows updated for requestId: " + requestId);
//            }
//
//            System.out.println("Successfully updated request " + requestId + " to " + status + " with reason: " + rejectReason);
//        }
//    }
//
//    public void updateImportItemWithDetails(String requestId, String productCode, int quantity,
//            String note, String warehouse, String receiver) throws SQLException {
//        String sql = "UPDATE request_items SET imported_quantity = ?, import_note = ?, warehouse = ?, receiver = ?, updated_at = NOW() WHERE request_id = ? AND product_code = ?";
//
//        try {
//            conn = new Context().getJDBCConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1, quantity);
//            ps.setString(2, note);
//            ps.setString(3, warehouse);
//            ps.setString(4, receiver);
//            ps.setString(5, requestId);
//            ps.setString(6, productCode);
//
//            ps.executeUpdate();
//        } finally {
//            closeResources();
//        }
//    }
//
//    // Method to get history with status including rejected
//    public List<Request> getImportHistory(int page, int pageSize, String searchKeyword) {
//        List<Request> requests = new ArrayList<>();
//        StringBuilder sql = new StringBuilder(
//                "SELECT r.*, ri.product_name, ri.product_code, ri.quantity, ri.imported_quantity, ri.import_note "
//                + "FROM request r "
//                + "LEFT JOIN request_items ri ON r.id = ri.request_id "
//                + "WHERE r.status IN ('completed', 'rejected')"
//        );
//
//        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
//            sql.append(" AND (r.supplier LIKE ? OR ri.product_name LIKE ? OR r.id LIKE ?)");
//        }
//
//        sql.append(" ORDER BY r.updated_at DESC LIMIT ?, ?");
//
//        try {
//            conn = new Context().getJDBCConnection();
//            ps = conn.prepareStatement(sql.toString());
//
//            int paramIndex = 1;
//            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
//                String keyword = "%" + searchKeyword + "%";
//                ps.setString(paramIndex++, keyword);
//                ps.setString(paramIndex++, keyword);
//                ps.setString(paramIndex++, keyword);
//            }
//
//            ps.setInt(paramIndex++, (page - 1) * pageSize);
//            ps.setInt(paramIndex, pageSize);
//
//            rs = ps.executeQuery();
//
//            Map<String, Request> requestMap = new HashMap<>();
//
//            while (rs.next()) {
//                String requestId = rs.getString("id");
//                Request req = requestMap.get(requestId);
//
//                if (req == null) {
//                    req = new Request();
//                    req.setId(requestId);
//                    req.setUser_id(rs.getInt("user_id"));
//                    req.setDay_request(rs.getDate("day_request"));
//                    req.setStatus(rs.getString("status"));
//                    req.setSupplier(rs.getString("supplier"));
//                    req.setItems(new ArrayList<>());
//
//                    requestMap.put(requestId, req);
//                    requests.add(req);
//                }
//
//                // Add item if exists
//                String productCode = rs.getString("product_code");
//                if (productCode != null) {
//                    RequestItem item = new RequestItem();
//                    item.setProductName(rs.getString("product_name"));
//                    item.setProductCode(productCode);
//                    item.setQuantity(rs.getInt("quantity"));
//                    item.setImportedQty(rs.getInt("imported_quantity"));
//                    item.setNote(rs.getString("import_note"));
//                    req.getItems().add(item);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//
//        return requests;
//    }
//
//    // Thêm phương thức mới vào ListRequestImportDAO
//    public void updateRequestStatusToRejected(String requestId, String status, String rejectReason, String rejectedAt) throws SQLException {
//        String sql = "UPDATE purchase_order_info SET status = ?, reject_reason = ?, rejected_at = ?, updated_at = NOW() WHERE id = ?";
//
//        try {
//            conn = Context.getJDBCConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, status);
//            ps.setString(2, rejectReason);
//            ps.setString(3, rejectedAt);
//            ps.setString(4, requestId);
//
//            int result = ps.executeUpdate();
//            if (result == 0) {
//                throw new SQLException("Failed to update request status to rejected. No rows affected.");
//            }
//
//            System.out.println("Successfully updated request " + requestId + " to rejected status");
//        } finally {
//            closeResources();
//        }
//    }
//
//// Cập nhật phương thức getCompletedRequestItems để bao gồm cả rejected
//    public List<ApprovedRequestItem> getImportHistoryItems(String searchType, String searchValue) {
//        List<ApprovedRequestItem> list = new ArrayList<>();
//        StringBuilder sql = new StringBuilder();
//
//        sql.append("SELECT po.id AS request_id, po.day_purchase AS day_request, po.status, po.supplier, ")
//                .append("po.address, po.phone, po.email, po.reject_reason, po.rejected_at, po.updated_at, ")
//                .append("poi.product_name, poi.product_code, poi.product_name AS product_full_name, ")
//                .append("poi.price_per_unit AS price, poi.unit, poi.quantity, poi.note ")
//                .append("FROM purchase_order_info po ")
//                .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
//                .append("WHERE po.status IN ('completed', 'rejected') ");
//
//        // Thêm điều kiện tìm kiếm
//        if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
//            String searchPattern = "%" + searchValue.trim() + "%";
//            switch (searchType) {
//                case "requestId":
//                    sql.append("AND po.id LIKE ? ");
//                    break;
//                case "productName":
//                    sql.append("AND poi.product_name LIKE ? ");
//                    break;
//                case "productCode":
//                    sql.append("AND poi.product_code LIKE ? ");
//                    break;
//                case "status":
//                    sql.append("AND po.status LIKE ? ");
//                    break;
//            }
//        }
//
//        sql.append("ORDER BY po.updated_at DESC");
//
//        try {
//            conn = Context.getJDBCConnection();
//            ps = conn.prepareStatement(sql.toString());
//
//            // Set parameter nếu có tìm kiếm
//            if (searchValue != null && !searchValue.trim().isEmpty() && searchType != null) {
//                String searchPattern = "%" + searchValue.trim() + "%";
//                ps.setString(1, searchPattern);
//            }
//
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                ApprovedRequestItem item = new ApprovedRequestItem();
//                item.setRequestId(rs.getString("request_id"));
//                item.setDayRequest(rs.getString("day_request"));
//                item.setStatus(rs.getString("status"));
//                item.setSupplier(rs.getString("supplier"));
//                item.setAddress(rs.getString("address"));
//                item.setPhone(rs.getString("phone"));
//                item.setEmail(rs.getString("email"));
//                item.setProductName(rs.getString("product_name"));
//                item.setProductCode(rs.getString("product_code"));
//                item.setProductFullName(rs.getString("product_full_name"));
//                item.setPrice(rs.getDouble("price"));
//                item.setUnit(rs.getString("unit"));
//                item.setQuantity(rs.getDouble("quantity"));
//                item.setNote(rs.getString("note"));
//
//                // Thêm thông tin từ chối nếu có
//                if ("rejected".equals(rs.getString("status"))) {
//                    item.setReasonDetail(rs.getString("reject_reason"));
//                }
//
//                list.add(item);
//            }
//
//            System.out.println("Fetched import history items count: " + list.size());
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//        return list;
//    }
//
//    public void testConnection() {
//        try {
//            conn = Context.getJDBCConnection();
//            if (conn != null) {
//                System.out.println("Database connection successful!");
//
//                // Test query
//                String testSql = "SELECT COUNT(*) as total FROM purchase_order_info";
//                ps = conn.prepareStatement(testSql);
//                rs = ps.executeQuery();
//
//                if (rs.next()) {
//                    System.out.println("Total records in purchase_order_info: " + rs.getInt("total"));
//                }
//
//                // Test approved records
//                String approvedSql = "SELECT COUNT(*) as approved_count FROM purchase_order_info WHERE status = 'approved'";
//                ps = conn.prepareStatement(approvedSql);
//                rs = ps.executeQuery();
//
//                if (rs.next()) {
//                    System.out.println("Approved records count: " + rs.getInt("approved_count"));
//                }
//
//            } else {
//                System.out.println("Database connection failed!");
//            }
//        } catch (SQLException e) {
//            System.err.println("Database connection error: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//    }
//
//}
=======

package dao;

import DBContext.Context;
import model.ApprovedRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ListRequestImportDAO {

    /**
     * Lấy danh sách yêu cầu đã duyệt và nhập từng phần với phân trang và tìm
     * kiếm Bao gồm cả approved và partial_imported
     */
    public List<ApprovedRequestItem> getApprovedRequestItems(String searchType, String searchValue, int page, int pageSize) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Query chính để lấy dữ liệu approved và partial_imported
            sql.append("SELECT DISTINCT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, ")
                    .append("COALESCE(wpi.product_name, poi.product_name) as product_name, ")
                    .append("COALESCE(wpi.product_code, poi.product_code) as product_code, ")
                    .append("COALESCE(wpi.unit, poi.unit) as unit, ")
                    .append("COALESCE(wpi.price_per_unit, poi.price_per_unit) as price_per_unit, ")
                    .append("COALESCE(wpi.quantity_ordered, poi.quantity) as quantity_ordered, ")
                    .append("COALESCE(wpi.quantity_imported, 0) as quantity_imported, ")
                    .append("COALESCE(wpi.quantity_pending, poi.quantity) as quantity_pending, ")
                    .append("COALESCE(wpi.note, poi.note) as note, ")
                    .append("po.reason, po.reject_reason ")
                    .append("FROM purchase_order_info po ")
                    .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
                    .append("LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND poi.product_name = wpi.product_name ")
                    .append("WHERE po.status IN ('approved', 'partial_imported') ")
                    .append("AND (wpi.quantity_pending > 0 OR (wpi.quantity_pending IS NULL AND poi.quantity > 0))");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append(" AND po.id LIKE ?");
                        break;
                    case "productName":
                        sql.append(" AND (wpi.product_name LIKE ? OR poi.product_name LIKE ?)");
                        break;
                    case "productCode":
                        sql.append(" AND (wpi.product_code LIKE ? OR poi.product_code LIKE ?)");
                        break;
                    case "supplier":
                        sql.append(" AND po.supplier LIKE ?");
                        break;
                }
            }

            // Sắp xếp và phân trang
            sql.append(" ORDER BY po.status DESC, po.day_purchase DESC, po.id DESC, ")
                    .append("COALESCE(wpi.product_name, poi.product_name) ASC");
            sql.append(" LIMIT ?, ?");

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String searchPattern = "%" + searchValue.trim() + "%";
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                    case "supplier":
                        ps.setString(paramIndex++, searchPattern);
                        break;
                    case "productName":
                    case "productCode":
                        ps.setString(paramIndex++, searchPattern);
                        ps.setString(paramIndex++, searchPattern);
                        break;
                }
            }

            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = mapResultSetToApprovedRequestItemWithPending(rs);
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error getting approved requests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    /**
     * Lấy danh sách lịch sử nhập kho hoàn thành Bao gồm cả completed và
     * rejected
     */
    public List<ApprovedRequestItem> getCompletedRequestItems(String searchType, String searchValue, int page, int pageSize) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            // UNION để lấy cả dữ liệu từ warehouse_import_history và purchase_order_items
            sql.append("SELECT * FROM (")
                    // Trường hợp 1: Có trong warehouse_import_history (đã nhập kho thực tế)
                    .append("(SELECT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, ")
                    .append("wih.product_name, wih.product_code, wih.quantity_imported as quantity, ")
                    .append("COALESCE(wpi.price_per_unit, poi.price_per_unit) as price_per_unit, ")
                    .append("COALESCE(wpi.unit, poi.unit) as unit, ")
                    .append("wih.note, po.reason, po.reject_reason, wih.import_date as actual_date ")
                    .append("FROM purchase_order_info po ")
                    .append("INNER JOIN warehouse_import_history wih ON po.id = wih.purchase_id ")
                    .append("LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND wih.product_name = wpi.product_name ")
                    .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id AND wih.product_name = poi.product_name ")
                    .append("WHERE po.status IN ('completed', 'rejected')) ")
                    .append("UNION ")
                    // Trường hợp 2: Không có trong warehouse_import_history nhưng status là completed/rejected
                    .append("(SELECT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, ")
                    .append("poi.product_name, poi.product_code, poi.quantity, ")
                    .append("poi.price_per_unit, poi.unit, poi.note, po.reason, po.reject_reason, po.day_purchase as actual_date ")
                    .append("FROM purchase_order_info po ")
                    .append("INNER JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
                    .append("WHERE po.status IN ('completed', 'rejected') ")
                    .append("AND NOT EXISTS (")
                    .append("  SELECT 1 FROM warehouse_import_history wih ")
                    .append("  WHERE wih.purchase_id = po.id AND wih.product_name = poi.product_name")
                    .append("))");

            // Thêm điều kiện tìm kiếm cho toàn bộ UNION
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                sql.append(") AS combined_results WHERE 1=1");

                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append(" AND id LIKE ?");
                        break;
                    case "productName":
                        sql.append(" AND product_name LIKE ?");
                        break;
                    case "productCode":
                        sql.append(" AND product_code LIKE ?");
                        break;
                    case "supplier":
                        sql.append(" AND supplier LIKE ?");
                        break;
                }
            } else {
                sql.append(") AS combined_results");
            }

            sql.append(" ORDER BY actual_date DESC, id DESC, product_name ASC");
            sql.append(" LIMIT ?, ?");

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + searchValue.trim() + "%");
            }

            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = mapResultSetToCompletedItem(rs);
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error getting completed requests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    /**
     * Đếm số lượng approved và partial_imported request items
     */
    public int countApprovedRequestItems(String searchType, String searchValue) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT COUNT(DISTINCT CONCAT(po.id, '-', COALESCE(wpi.product_name, poi.product_name))) "
                    + "FROM purchase_order_info po "
                    + "LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id "
                    + "LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND poi.product_name = wpi.product_name "
                    + "WHERE po.status IN ('approved', 'partial_imported') "
                    + "AND (wpi.quantity_pending > 0 OR (wpi.quantity_pending IS NULL AND poi.quantity > 0))"
            );

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append(" AND po.id LIKE ?");
                        break;
                    case "productName":
                        sql.append(" AND (wpi.product_name LIKE ? OR poi.product_name LIKE ?)");
                        break;
                    case "productCode":
                        sql.append(" AND (wpi.product_code LIKE ? OR poi.product_code LIKE ?)");
                        break;
                    case "supplier":
                        sql.append(" AND po.supplier LIKE ?");
                        break;
                }
            }

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String searchPattern = "%" + searchValue.trim() + "%";
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                    case "supplier":
                        ps.setString(paramIndex, searchPattern);
                        break;
                    case "productName":
                    case "productCode":
                        ps.setString(paramIndex++, searchPattern);
                        ps.setString(paramIndex, searchPattern);
                        break;
                }
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            System.err.println("Error counting approved request items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Đếm số lượng completed request items
     */
    public int countCompletedRequestItems(String searchType, String searchValue) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            // Đếm cả hai trường hợp: có trong warehouse_import_history và không có
            sql.append("SELECT COUNT(*) FROM (")
                    .append("(SELECT po.id, poi.product_name FROM purchase_order_info po ")
                    .append("INNER JOIN warehouse_import_history wih ON po.id = wih.purchase_id ")
                    .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id AND wih.product_name = poi.product_name ")
                    .append("WHERE po.status IN ('completed', 'rejected')) ")
                    .append("UNION ")
                    .append("(SELECT po.id, poi.product_name FROM purchase_order_info po ")
                    .append("INNER JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
                    .append("WHERE po.status IN ('completed', 'rejected') ")
                    .append("AND NOT EXISTS (SELECT 1 FROM warehouse_import_history wih ")
                    .append("WHERE wih.purchase_id = po.id AND wih.product_name = poi.product_name))");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                sql.append(") AS combined_count WHERE 1=1");

                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append(" AND id LIKE ?");
                        break;
                    case "productName":
                        sql.append(" AND product_name LIKE ?");
                        break;
                    case "productCode":
                        sql.append(" AND product_code LIKE ?");
                        break;
                    case "supplier":
                        sql.append(" AND supplier LIKE ?");
                        break;
                }
            } else {
                sql.append(") AS combined_count");
            }

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                ps.setString(paramIndex, "%" + searchValue.trim() + "%");
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            System.err.println("Error counting completed request items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Đếm số lượng partial imported items
     */
    public int countPartialImportedItems() {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(DISTINCT CONCAT(po.id, '-', wpi.product_name)) "
                    + "FROM purchase_order_info po "
                    + "INNER JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id "
                    + "WHERE po.status = 'partial_imported' AND wpi.quantity_pending > 0";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            System.err.println("Error counting partial imported items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Lấy thông tin chi tiết một yêu cầu theo ID
     */
    public ApprovedRequestItem getRequestItemById(String requestId, String productName) {
        ApprovedRequestItem item = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, "
                    + "COALESCE(wpi.product_name, poi.product_name) as product_name, "
                    + "COALESCE(wpi.product_code, poi.product_code) as product_code, "
                    + "COALESCE(wpi.unit, poi.unit) as unit, "
                    + "COALESCE(wpi.price_per_unit, poi.price_per_unit) as price_per_unit, "
                    + "COALESCE(wpi.quantity_ordered, poi.quantity) as quantity_ordered, "
                    + "COALESCE(wpi.quantity_imported, 0) as quantity_imported, "
                    + "COALESCE(wpi.quantity_pending, poi.quantity) as quantity_pending, "
                    + "COALESCE(wpi.note, poi.note) as note, "
                    + "po.reason, po.reject_reason "
                    + "FROM purchase_order_info po "
                    + "LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id "
                    + "LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND poi.product_name = wpi.product_name "
                    + "WHERE po.id = ? AND COALESCE(wpi.product_name, poi.product_name) = ?";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            ps.setString(2, productName);

            rs = ps.executeQuery();
            if (rs.next()) {
                item = mapResultSetToApprovedRequestItemWithPending(rs);
            }

        } catch (Exception e) {
            System.err.println("Error getting request item by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return item;
    }

    /**
     * Cập nhật trạng thái partial import
     */
    public boolean updatePartialImportStatus(String requestId, String productName, double quantityImported, String note) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            // 1. Kiểm tra và lấy thông tin hiện tại
            String checkSql = "SELECT quantity_ordered, quantity_imported, quantity_pending FROM warehouse_pending_items "
                    + "WHERE purchase_id = ? AND product_name = ?";
            ps1 = conn.prepareStatement(checkSql);
            ps1.setString(1, requestId);
            ps1.setString(2, productName);
            rs = ps1.executeQuery();

            if (rs.next()) {
                double currentImported = rs.getDouble("quantity_imported");
                double currentPending = rs.getDouble("quantity_pending");
                double newImported = currentImported + quantityImported;
                double newPending = Math.max(0, currentPending - quantityImported);

                // 2. Cập nhật warehouse_pending_items
                String updateSql = "UPDATE warehouse_pending_items SET quantity_imported = ?, quantity_pending = ?, note = ? "
                        + "WHERE purchase_id = ? AND product_name = ?";
                ps2 = conn.prepareStatement(updateSql);
                ps2.setDouble(1, newImported);
                ps2.setDouble(2, newPending);
                ps2.setString(3, note);
                ps2.setString(4, requestId);
                ps2.setString(5, productName);
                ps2.executeUpdate();

                // 3. Cập nhật trạng thái purchase_order_info nếu cần
                if (newPending <= 0) {
                    // Kiểm tra xem còn pending items nào khác không
                    String checkPendingSql = "SELECT COUNT(*) FROM warehouse_pending_items WHERE purchase_id = ? AND quantity_pending > 0";
                    ps3 = conn.prepareStatement(checkPendingSql);
                    ps3.setString(1, requestId);
                    ResultSet rs2 = ps3.executeQuery();

                    if (rs2.next() && rs2.getInt(1) == 0) {
                        // Không còn pending items nào, cập nhật status thành completed
                        String updateStatusSql = "UPDATE purchase_order_info SET status = 'completed' WHERE id = ?";
                        PreparedStatement ps4 = conn.prepareStatement(updateStatusSql);
                        ps4.setString(1, requestId);
                        ps4.executeUpdate();
                        ps4.close();
                    }
                    rs2.close();
                }

                conn.commit();
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error updating partial import status: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto commit: " + e.getMessage());
            }
            closeResources(conn, ps1, rs);
            if (ps2 != null) try {
                ps2.close();
            } catch (SQLException e) {
            }
            if (ps3 != null) try {
                ps3.close();
            } catch (SQLException e) {
            }
        }

        return false;
    }

    /**
     * Thêm record vào warehouse_import_history
     */
    public boolean addImportHistory(String requestId, String productName, double quantity, String note) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO warehouse_import_history (purchase_id, product_name, product_code, quantity_imported, import_date, note) "
                    + "SELECT ?, product_name, product_code, ?, NOW(), ? "
                    + "FROM purchase_order_items WHERE purchase_id = ? AND product_name = ?";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            ps.setDouble(2, quantity);
            ps.setString(3, note);
            ps.setString(4, requestId);
            ps.setString(5, productName);

            int result = ps.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            System.err.println("Error adding import history: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }

        return false;
    }

    /**
     * Helper method để map ResultSet cho completed items
     */
    private ApprovedRequestItem mapResultSetToCompletedItem(ResultSet rs) throws SQLException {
        ApprovedRequestItem item = new ApprovedRequestItem();

        try {
            item.setRequestId(rs.getString("id"));
            item.setDayRequest(rs.getString("day_purchase"));
            item.setStatus(rs.getString("status"));
            item.setSupplier(rs.getString("supplier"));
            item.setAddress(rs.getString("address"));
            item.setPhone(rs.getString("phone"));
            item.setEmail(rs.getString("email"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal price = rs.getBigDecimal("price_per_unit");
            item.setPrice(price != null ? price.doubleValue() : 0.0);

            BigDecimal quantity = rs.getBigDecimal("quantity");
            item.setQuantity(quantity != null ? quantity.doubleValue() : 0.0);

            item.setNote(rs.getString("note"));
            item.setReasonDetail(rs.getString("reason"));
            item.setRejectReason(rs.getString("reject_reason"));

        } catch (SQLException e) {
            System.err.println("Error mapping completed item result set: " + e.getMessage());
            // Set default values để tránh null
            if (item.getRequestId() == null) {
                item.setRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("unknown");
            }
        }

        return item;
    }

    /**
     * Helper method để map ResultSet với thông tin pending
     */
    private ApprovedRequestItem mapResultSetToApprovedRequestItemWithPending(ResultSet rs) throws SQLException {
        ApprovedRequestItem item = new ApprovedRequestItem();

        try {
            item.setRequestId(rs.getString("id"));
            item.setDayRequest(rs.getString("day_purchase"));
            item.setStatus(rs.getString("status"));
            item.setSupplier(rs.getString("supplier"));
            item.setAddress(rs.getString("address"));
            item.setPhone(rs.getString("phone"));
            item.setEmail(rs.getString("email"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal price = rs.getBigDecimal("price_per_unit");
            item.setPrice(price != null ? price.doubleValue() : 0.0);

            // Set thông tin về số lượng
            BigDecimal quantityOrdered = rs.getBigDecimal("quantity_ordered");
            BigDecimal quantityImported = rs.getBigDecimal("quantity_imported");
            BigDecimal quantityPending = rs.getBigDecimal("quantity_pending");

            item.setQuantityOrdered(quantityOrdered != null ? quantityOrdered.doubleValue() : 0.0);
            item.setQuantityImported(quantityImported != null ? quantityImported.doubleValue() : 0.0);
            item.setQuantityPending(quantityPending != null ? quantityPending.doubleValue() : 0.0);

            // Set quantity cho compatibility với code cũ
            item.setQuantity(item.getQuantityPending());

            item.setNote(rs.getString("note"));
            item.setReasonDetail(rs.getString("reason"));
            item.setRejectReason(rs.getString("reject_reason"));

        } catch (SQLException e) {
            System.err.println("Error mapping result set with pending info: " + e.getMessage());
            // Set default values để tránh null
            if (item.getRequestId() == null) {
                item.setRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("unknown");
            }
        }

        return item;
    }

    /**
     * Lấy danh sách sản phẩm theo request ID
     */
    public List<ApprovedRequestItem> getProductsByRequestId(String requestId) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, "
                    + "COALESCE(wpi.product_name, poi.product_name) as product_name, "
                    + "COALESCE(wpi.product_code, poi.product_code) as product_code, "
                    + "COALESCE(wpi.unit, poi.unit) as unit, "
                    + "COALESCE(wpi.price_per_unit, poi.price_per_unit) as price_per_unit, "
                    + "COALESCE(wpi.quantity_ordered, poi.quantity) as quantity_ordered, "
                    + "COALESCE(wpi.quantity_imported, 0) as quantity_imported, "
                    + "COALESCE(wpi.quantity_pending, poi.quantity) as quantity_pending, "
                    + "COALESCE(wpi.note, poi.note) as note, "
                    + "po.reason, po.reject_reason "
                    + "FROM purchase_order_info po "
                    + "LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id "
                    + "LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND poi.product_name = wpi.product_name "
                    + "WHERE po.id = ? AND po.status IN ('approved', 'partial_imported') "
                    + "AND (wpi.quantity_pending > 0 OR (wpi.quantity_pending IS NULL AND poi.quantity > 0)) "
                    + "ORDER BY COALESCE(wpi.product_name, poi.product_name)";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);

            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = mapResultSetToApprovedRequestItemWithPending(rs);
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error getting products by request ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    /**
     * Kiểm tra xem request có tồn tại và có thể import không
     */
    public boolean isRequestValidForImport(String requestId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM purchase_order_info po "
                    + "WHERE po.id = ? AND po.status IN ('approved', 'partial_imported')";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            System.err.println("Error checking request validity: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return false;
    }

    /**
     * Lấy thông tin tổng quan của một request
     */
    public ApprovedRequestItem getRequestOverview(String requestId) {
        ApprovedRequestItem item = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, "
                    + "po.reason, po.reject_reason, "
                    + "COUNT(DISTINCT COALESCE(wpi.product_name, poi.product_name)) as total_products, "
                    + "SUM(COALESCE(wpi.quantity_ordered, poi.quantity)) as total_ordered, "
                    + "SUM(COALESCE(wpi.quantity_imported, 0)) as total_imported, "
                    + "SUM(COALESCE(wpi.quantity_pending, poi.quantity)) as total_pending "
                    + "FROM purchase_order_info po "
                    + "LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id "
                    + "LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND poi.product_name = wpi.product_name "
                    + "WHERE po.id = ? "
                    + "GROUP BY po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, po.reason, po.reject_reason";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);

            rs = ps.executeQuery();
            if (rs.next()) {
                item = new ApprovedRequestItem();
                item.setRequestId(rs.getString("id"));
                item.setDayRequest(rs.getString("day_purchase"));
                item.setStatus(rs.getString("status"));
                item.setSupplier(rs.getString("supplier"));
                item.setAddress(rs.getString("address"));
                item.setPhone(rs.getString("phone"));
                item.setEmail(rs.getString("email"));
                item.setReasonDetail(rs.getString("reason"));
                item.setRejectReason(rs.getString("reject_reason"));

                // Set thông tin tổng quan
                BigDecimal totalOrdered = rs.getBigDecimal("total_ordered");
                BigDecimal totalImported = rs.getBigDecimal("total_imported");
                BigDecimal totalPending = rs.getBigDecimal("total_pending");

                item.setQuantityOrdered(totalOrdered != null ? totalOrdered.doubleValue() : 0.0);
                item.setQuantityImported(totalImported != null ? totalImported.doubleValue() : 0.0);
                item.setQuantityPending(totalPending != null ? totalPending.doubleValue() : 0.0);
            }

        } catch (Exception e) {
            System.err.println("Error getting request overview: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return item;
    }

    /**
     * Cập nhật trạng thái request thành completed
     */
    public boolean markRequestAsCompleted(String requestId, String completionNote) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE purchase_order_info SET status = 'completed', reason = ? WHERE id = ?";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, completionNote);
            ps.setString(2, requestId);

            int result = ps.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            System.err.println("Error marking request as completed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }

        return false;
    }

    /**
     * Cập nhật trạng thái request thành rejected
     */
    public boolean markRequestAsRejected(String requestId, String rejectReason) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE purchase_order_info SET status = 'rejected', reject_reason = ? WHERE id = ?";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rejectReason);
            ps.setString(2, requestId);

            int result = ps.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            System.err.println("Error marking request as rejected: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }

        return false;
    }

    /**
     * Lấy lịch sử import của một request
     */
    public List<ApprovedRequestItem> getImportHistory(String requestId) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT wih.*, po.supplier, po.day_purchase "
                    + "FROM warehouse_import_history wih "
                    + "INNER JOIN purchase_order_info po ON wih.purchase_id = po.id "
                    + "WHERE wih.purchase_id = ? "
                    + "ORDER BY wih.import_date DESC, wih.product_name ASC";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);

            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = new ApprovedRequestItem();
                item.setRequestId(rs.getString("purchase_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setQuantity(rs.getDouble("quantity_imported"));
                item.setNote(rs.getString("note"));
                item.setDayRequest(rs.getString("import_date"));
                item.setSupplier(rs.getString("supplier"));
                item.setStatus("imported");
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error getting import history: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    /**
     * Tìm kiếm requests theo nhiều tiêu chí
     */
    public List<ApprovedRequestItem> searchRequests(String keyword, String status, String dateFrom, String dateTo, int page, int pageSize) {
        List<ApprovedRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT po.id, po.day_purchase, po.status, po.supplier, po.address, po.phone, po.email, ")
                    .append("COALESCE(wpi.product_name, poi.product_name) as product_name, ")
                    .append("COALESCE(wpi.product_code, poi.product_code) as product_code, ")
                    .append("COALESCE(wpi.unit, poi.unit) as unit, ")
                    .append("COALESCE(wpi.price_per_unit, poi.price_per_unit) as price_per_unit, ")
                    .append("COALESCE(wpi.quantity_ordered, poi.quantity) as quantity_ordered, ")
                    .append("COALESCE(wpi.quantity_imported, 0) as quantity_imported, ")
                    .append("COALESCE(wpi.quantity_pending, poi.quantity) as quantity_pending, ")
                    .append("COALESCE(wpi.note, poi.note) as note, ")
                    .append("po.reason, po.reject_reason ")
                    .append("FROM purchase_order_info po ")
                    .append("LEFT JOIN purchase_order_items poi ON po.id = poi.purchase_id ")
                    .append("LEFT JOIN warehouse_pending_items wpi ON po.id = wpi.purchase_id AND poi.product_name = wpi.product_name ")
                    .append("WHERE 1=1 ");

            List<Object> params = new ArrayList<>();

            // Thêm điều kiện tìm kiếm
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append("AND (po.id LIKE ? OR po.supplier LIKE ? OR ")
                        .append("COALESCE(wpi.product_name, poi.product_name) LIKE ? OR ")
                        .append("COALESCE(wpi.product_code, poi.product_code) LIKE ?) ");
                String searchPattern = "%" + keyword.trim() + "%";
                params.add(searchPattern);
                params.add(searchPattern);
                params.add(searchPattern);
                params.add(searchPattern);
            }

            if (status != null && !status.trim().isEmpty()) {
                sql.append("AND po.status = ? ");
                params.add(status);
            }

            if (dateFrom != null && !dateFrom.trim().isEmpty()) {
                sql.append("AND po.day_purchase >= ? ");
                params.add(dateFrom);
            }

            if (dateTo != null && !dateTo.trim().isEmpty()) {
                sql.append("AND po.day_purchase <= ? ");
                params.add(dateTo);
            }

            sql.append("ORDER BY po.day_purchase DESC, po.id DESC ");
            sql.append("LIMIT ?, ?");
            params.add((page - 1) * pageSize);
            params.add(pageSize);

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                ApprovedRequestItem item = mapResultSetToApprovedRequestItemWithPending(rs);
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error searching requests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    /**
     * Lấy thống kê tổng quan
     */
    public java.util.Map<String, Integer> getStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT status, COUNT(*) as count FROM purchase_order_info GROUP BY status";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }

            // Thêm thống kê về pending items
            String pendingSql = "SELECT COUNT(DISTINCT CONCAT(purchase_id, '-', product_name)) as pending_count "
                    + "FROM warehouse_pending_items WHERE quantity_pending > 0";
            PreparedStatement ps2 = conn.prepareStatement(pendingSql);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                stats.put("pending_items", rs2.getInt("pending_count"));
            }
            rs2.close();
            ps2.close();

        } catch (Exception e) {
            System.err.println("Error getting statistics: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return stats;
    }

    /**
     * Batch update cho nhiều items cùng lúc
     */
    public boolean batchUpdateImportStatus(List<String> requestIds, List<String> productNames,
            List<Double> quantities, List<String> notes) {
        if (requestIds.size() != productNames.size()
                || productNames.size() != quantities.size()
                || quantities.size() != notes.size()) {
            System.err.println("Batch update: Arrays size mismatch");
            return false;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            String sql = "UPDATE warehouse_pending_items SET "
                    + "quantity_imported = quantity_imported + ?, "
                    + "quantity_pending = GREATEST(0, quantity_pending - ?), "
                    + "note = ? "
                    + "WHERE purchase_id = ? AND product_name = ?";

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < requestIds.size(); i++) {
                ps.setDouble(1, quantities.get(i));
                ps.setDouble(2, quantities.get(i));
                ps.setString(3, notes.get(i));
                ps.setString(4, requestIds.get(i));
                ps.setString(5, productNames.get(i));
                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            conn.commit();

            // Kiểm tra kết quả
            for (int result : results) {
                if (result <= 0) {
                    System.err.println("Batch update: Some updates failed");
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error in batch update: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back batch update: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto commit: " + e.getMessage());
            }
            closeResources(conn, ps, null);
        }

        return false;
    }

    /**
     * Lấy danh sách suppliers từ các requests
     */
    public List<String> getAllSuppliers() {
        List<String> suppliers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT DISTINCT supplier FROM purchase_order_info WHERE supplier IS NOT NULL ORDER BY supplier";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                suppliers.add(rs.getString("supplier"));
            }

        } catch (Exception e) {
            System.err.println("Error getting suppliers: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return suppliers;
    }

    /**
     * Lấy danh sách product codes từ các requests
     */
    public List<String> getAllProductCodes() {
        List<String> productCodes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT DISTINCT product_code FROM purchase_order_items WHERE product_code IS NOT NULL "
                    + "UNION SELECT DISTINCT product_code FROM warehouse_pending_items WHERE product_code IS NOT NULL "
                    + "ORDER BY product_code";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                productCodes.add(rs.getString("product_code"));
            }

        } catch (Exception e) {
            System.err.println("Error getting product codes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return productCodes;
    }

    /**
     * Kiểm tra và tạo warehouse_pending_items nếu chưa có
     */
    public boolean initializePendingItems(String requestId) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            // Kiểm tra xem đã có pending items chưa
            String checkSql = "SELECT COUNT(*) FROM warehouse_pending_items WHERE purchase_id = ?";
            ps1 = conn.prepareStatement(checkSql);
            ps1.setString(1, requestId);
            rs = ps1.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Chưa có, tạo mới từ purchase_order_items
                String insertSql = "INSERT INTO warehouse_pending_items "
                        + "(purchase_id, product_name, product_code, unit, price_per_unit, "
                        + "quantity_ordered, quantity_imported, quantity_pending, note) "
                        + "SELECT purchase_id, product_name, product_code, unit, price_per_unit, "
                        + "quantity, 0, quantity, note "
                        + "FROM purchase_order_items WHERE purchase_id = ?";

                ps2 = conn.prepareStatement(insertSql);
                ps2.setString(1, requestId);
                int result = ps2.executeUpdate();

                conn.commit();
                return result > 0;
            } else {
                // Đã có rồi
                return true;
            }

        } catch (Exception e) {
            System.err.println("Error initializing pending items: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back initialization: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto commit: " + e.getMessage());
            }
            closeResources(conn, ps1, rs);
            if (ps2 != null) try {
                ps2.close();
            } catch (SQLException e) {
            }
        }

        return false;
    }

    /**
     * Đóng resources an toàn
     */
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing ResultSet: " + e.getMessage());
        }

        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing PreparedStatement: " + e.getMessage());
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing Connection: " + e.getMessage());
        }
    }

    /**
     * Validate input parameters
     */
    private boolean isValidSearchType(String searchType) {
        return searchType != null && ("requestId".equals(searchType)
                || "productName".equals(searchType)
                || "productCode".equals(searchType)
                || "supplier".equals(searchType));
    }

    /**
     * Log SQL queries for debugging (chỉ trong development)
     */
    private void logQuery(String sql, Object... params) {
        if (System.getProperty("debug.sql", "false").equals("true")) {
            System.out.println("SQL: " + sql);
            if (params.length > 0) {
                System.out.print("Parameters: ");
                for (int i = 0; i < params.length; i++) {
                    System.out.print(params[i]);
                    if (i < params.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }
        }
    }

    /**
     * Test connection
     */
    public boolean testConnection() {
        Connection conn = null;
        try {
            conn = Context.getJDBCConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing test connection: " + e.getMessage());
                }
            }
        }
    }
}
>>>>>>> fa762af737b06f7bad2f50ac83eff6970e72fed2
