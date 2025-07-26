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
import model.PurchaseOrderInfo;
import model.PurchaseOrderItems;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ImportDAO {

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

    /**
     * Lấy thông tin purchase order theo ID với thông tin nhập kho
     */
    public PurchaseOrderInfo getPurchaseOrderById(String id) {
        PurchaseOrderInfo order = null;
        String sql = "SELECT * FROM purchase_order_info WHERE id = ? AND status IN ('approved', 'partial_imported')";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                order = new PurchaseOrderInfo();
                order.setId(rs.getString("id"));
                order.setFullname(rs.getString("fullname"));
                order.setDayPurchase(rs.getDate("day_purchase"));
                order.setDayQuote(rs.getDate("day_quote"));
                order.setStatus(rs.getString("status"));
                order.setReason(rs.getString("reason"));
                order.setRejectReason(rs.getString("reject_reason")); // Thêm dòng này
                order.setSupplier(rs.getString("supplier"));
                order.setAddress(rs.getString("address"));
                order.setPhone(rs.getString("phone"));
                order.setEmail(rs.getString("email"));
                order.setSummary(rs.getString("summary"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting purchase order by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return order;
    }

    /**
     * Lấy danh sách items với thông tin nhập kho từng phần
     */
    public List<PurchaseOrderItems> getPurchaseOrderItemsByOrderId(String orderId) {
        List<PurchaseOrderItems> list = new ArrayList<>();
        String sql = """
            SELECT 
                poi.id,
                poi.purchase_id,
                poi.product_name,
                poi.product_code,
                poi.unit,
                poi.quantity as quantity_ordered,
                poi.price_per_unit,
                poi.total_price,
                poi.note,
                COALESCE(wpi.quantity_imported, 0) as quantity_imported,
                COALESCE(wpi.quantity_pending, poi.quantity) as quantity_pending
            FROM purchase_order_items poi
            LEFT JOIN warehouse_pending_items wpi ON poi.purchase_id = wpi.purchase_id 
                AND poi.product_code = wpi.product_code
            WHERE poi.purchase_id = ?
            ORDER BY poi.id
            """;

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                PurchaseOrderItems item = new PurchaseOrderItems();
                item.setId(rs.getInt("id"));
                item.setPurchaseId(rs.getString("purchase_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));

                BigDecimal quantityOrdered = rs.getBigDecimal("quantity_ordered");
                item.setQuantity(quantityOrdered);
                item.setQuantityOrdered(quantityOrdered);
                item.setQuantityImported(rs.getBigDecimal("quantity_imported"));
                item.setQuantityPending(rs.getBigDecimal("quantity_pending"));

                item.setPricePerUnit(rs.getBigDecimal("price_per_unit"));
                item.setTotalPrice(rs.getBigDecimal("total_price"));
                item.setNote(rs.getString("note"));
                list.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error getting purchase order items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    /**
     * Xử lý nhập kho từng phần - Cập nhật ghi chú bổ sung vào reason
     */
    public boolean processPartialImport(String requestId, String importDate, String processor,
            String additionalNote, List<PurchaseOrderItems> importItems) {
        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            // 1. Cập nhật hoặc tạo mới warehouse_pending_items
            for (PurchaseOrderItems item : importItems) {
                BigDecimal importQuantity = item.getQuantity(); // Số lượng nhập lần này
                if (importQuantity != null && importQuantity.compareTo(BigDecimal.ZERO) > 0) {

                    // Kiểm tra xem item đã tồn tại trong pending chưa
                    String checkSql = "SELECT quantity_imported, quantity_pending FROM warehouse_pending_items WHERE purchase_id = ? AND product_code = ?";
                    ps = conn.prepareStatement(checkSql);
                    ps.setString(1, requestId);
                    ps.setString(2, item.getProductCode());
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        // Cập nhật item đã tồn tại
                        BigDecimal currentImported = rs.getBigDecimal("quantity_imported");
                        BigDecimal newImported = currentImported.add(importQuantity);
                        BigDecimal newPending = item.getQuantityOrdered().subtract(newImported);

                        String updateSql = """
                            UPDATE warehouse_pending_items 
                            SET quantity_imported = ?, quantity_pending = ?, last_import_date = NOW()
                            WHERE purchase_id = ? AND product_code = ?
                            """;
                        ps = conn.prepareStatement(updateSql);
                        ps.setBigDecimal(1, newImported);
                        ps.setBigDecimal(2, newPending);
                        ps.setString(3, requestId);
                        ps.setString(4, item.getProductCode());
                        ps.executeUpdate();
                    } else {
                        // Tạo mới item
                        BigDecimal newPending = item.getQuantityOrdered().subtract(importQuantity);
                        String insertSql = """
                            INSERT INTO warehouse_pending_items 
                            (purchase_id, product_name, product_code, unit, quantity_ordered, 
                             quantity_imported, quantity_pending, price_per_unit, note)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """;
                        ps = conn.prepareStatement(insertSql);
                        ps.setString(1, requestId);
                        ps.setString(2, item.getProductName());
                        ps.setString(3, item.getProductCode());
                        ps.setString(4, item.getUnit());
                        ps.setBigDecimal(5, item.getQuantityOrdered());
                        ps.setBigDecimal(6, importQuantity);
                        ps.setBigDecimal(7, newPending);
                        ps.setBigDecimal(8, item.getPricePerUnit());
                        ps.setString(9, item.getNote());
                        ps.executeUpdate();
                    }

                    // 2. Lưu lịch sử nhập kho
                    String historySql = """
                        INSERT INTO warehouse_import_history 
                        (purchase_id, product_name, product_code, quantity_imported, 
                         import_date, processor, note)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;
                    ps = conn.prepareStatement(historySql);
                    ps.setString(1, requestId);
                    ps.setString(2, item.getProductName());
                    ps.setString(3, item.getProductCode());
                    ps.setBigDecimal(4, importQuantity);
                    ps.setString(5, importDate);
                    ps.setString(6, processor);
                    ps.setString(7, additionalNote);
                    ps.executeUpdate();
                }
            }

            // 3. Kiểm tra xem đã nhập đủ hàng chưa
            boolean isCompleted = checkIfOrderCompleted(requestId);
            String newStatus = isCompleted ? "completed" : "partial_imported";

            // 4. Cập nhật trạng thái đơn hàng và reason (ghi chú bổ sung)
            String updateOrderSql = "UPDATE purchase_order_info SET status = ?, reason = ?, summary = ? WHERE id = ?";

            // Tạo reason từ ghi chú bổ sung
            String reasonText = null;
            if (additionalNote != null && !additionalNote.trim().isEmpty()) {
                reasonText = additionalNote.trim();
            }

            // Tạo summary
            StringBuilder summary = new StringBuilder();
            summary.append("Ngày nhập: ").append(importDate);
            summary.append("; Người xử lý: ").append(processor);
            if (additionalNote != null && !additionalNote.trim().isEmpty()) {
                summary.append("; Ghi chú: ").append(additionalNote.trim());
            }

            ps = conn.prepareStatement(updateOrderSql);
            ps.setString(1, newStatus);
            ps.setString(2, reasonText);
            ps.setString(3, summary.toString());
            ps.setString(4, requestId);
            ps.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error processing partial import: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources();
        }
    }

    /**
     * Kiểm tra xem đơn hàng đã được nhập đủ chưa
     */
    private boolean checkIfOrderCompleted(String requestId) {
        String sql = "SELECT COUNT(*) FROM warehouse_pending_items WHERE purchase_id = ? AND quantity_pending > 0";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái đơn hàng thành rejected với lý do từ chối riêng
     */
    public boolean updateRequestStatusToRejected(String requestId, String rejectReason) {
        String sql = "UPDATE purchase_order_info SET status = 'rejected', reject_reason = ? WHERE id = ? AND status IN ('approved', 'partial_imported')";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rejectReason);
            ps.setString(2, requestId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating request status to rejected: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    /**
     * Kiểm tra xem đơn hàng có tồn tại và có thể xử lý không
     */
    public boolean isOrderProcessable(String requestId) {
        String sql = "SELECT status FROM purchase_order_info WHERE id = ?";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                return "approved".equals(status) || "partial_imported".equals(status);
            }
        } catch (SQLException e) {
            System.err.println("Error checking order processable status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    /**
     * Lấy lịch sử nhập kho của một đơn hàng
     */
    public List<Object[]> getImportHistory(String requestId) {
        List<Object[]> history = new ArrayList<>();
        String sql = """
            SELECT product_name, product_code, quantity_imported, 
                   import_date, processor, note, created_at
            FROM warehouse_import_history 
            WHERE purchase_id = ? 
            ORDER BY created_at DESC
            """;

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] record = {
                    rs.getString("product_name"),
                    rs.getString("product_code"),
                    rs.getBigDecimal("quantity_imported"),
                    rs.getDate("import_date"),
                    rs.getString("processor"),
                    rs.getString("note"),
                    rs.getTimestamp("created_at")
                };
                history.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error getting import history: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return history;
    }
    // Thêm vào ImportDAO
public boolean isOrderFullyImported(String purchaseOrderId) {
    try (Connection con = Context.getJDBCConnection()) {
        String sql = "SELECT COUNT(*) as total_items, " +
                    "COUNT(CASE WHEN quantity_imported >= quantity_ordered THEN 1 END) as completed_items " +
                    "FROM purchase_order_items " +
                    "WHERE purchase_id = ?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, purchaseOrderId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            int totalItems = rs.getInt("total_items");
            int completedItems = rs.getInt("completed_items");
            
            System.out.println("📊 Đơn " + purchaseOrderId + ": " + completedItems + "/" + totalItems + " items đã hoàn thành");
            return totalItems > 0 && totalItems == completedItems;
        }
        
    } catch (SQLException e) {
        System.err.println("❌ Lỗi kiểm tra isOrderFullyImported: " + e.getMessage());
        e.printStackTrace();
    }
    
    return false;
}
}
>>>>>>> fa762af737b06f7bad2f50ac83eff6970e72fed2
