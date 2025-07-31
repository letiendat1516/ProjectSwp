package dao;

import DBContext.Context;
import model.ExportRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ListRequestExportDAO {

    /**
     * Lấy danh sách yêu cầu xuất kho đã duyệt với phân trang và tìm kiếm
     */
    public List<ExportRequestItem> getApprovedExportItems(String searchType, String searchValue, int page, int pageSize) {
        List<ExportRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT DISTINCT er.id, er.day_request, er.status, er.user_id, er.role, ")
                    .append("COALESCE(epi.product_name, eri.product_name) as product_name, ")
                    .append("COALESCE(epi.product_code, eri.product_code) as product_code, ")
                    .append("COALESCE(epi.unit, eri.unit) as unit, ")
                    .append("COALESCE(epi.quantity_requested, eri.quantity) as quantity_requested, ")
                    .append("COALESCE(epi.quantity_exported, 0) as quantity_exported, ")
                    .append("COALESCE(epi.quantity_pending, eri.quantity) as quantity_pending, ")
                    .append("COALESCE(epi.note, eri.note) as note, ")
                    .append("er.reason, er.reject_reason ")
                    .append("FROM export_request er ")
                    .append("LEFT JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("LEFT JOIN export_pending_items epi ON er.id = epi.export_request_id AND eri.product_name = epi.product_name ")
                    .append("WHERE er.status IN ('approved', 'partial_exported') ")
                    .append("AND (epi.quantity_pending > 0 OR (epi.quantity_pending IS NULL AND eri.quantity > 0))");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append(" AND er.id LIKE ?");
                        break;
                    case "productName":
                        sql.append(" AND (epi.product_name LIKE ? OR eri.product_name LIKE ?)");
                        break;
                    case "productCode":
                        sql.append(" AND (epi.product_code LIKE ? OR eri.product_code LIKE ?)");
                        break;
                }
            }

            sql.append(" ORDER BY er.status DESC, er.day_request DESC, er.id DESC, ")
                    .append("COALESCE(epi.product_name, eri.product_name) ASC");
            sql.append(" LIMIT ?, ?");

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String searchPattern = "%" + searchValue.trim() + "%";
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
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
                ExportRequestItem item = mapResultSetToExportRequestItem(rs);
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error getting approved export requests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    /**
     * Lấy danh sách lịch sử xuất kho hoàn thành
     */
    public List<ExportRequestItem> getCompletedExportItems(String searchType, String searchValue, int page, int pageSize) {
        List<ExportRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT * FROM (")
                    // Trường hợp 1: Có trong warehouse_export_history
                    .append("(SELECT er.id, er.day_request, er.status, er.user_id, er.role, ")
                    .append("weh.product_name, weh.product_code, weh.quantity_exported as quantity, ")
                    .append("COALESCE(epi.unit, eri.unit) as unit, ")
                    .append("weh.note, er.reason, er.reject_reason, weh.export_date as actual_date ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN warehouse_export_history weh ON er.id = weh.export_request_id ")
                    .append("LEFT JOIN export_pending_items epi ON er.id = epi.export_request_id AND weh.product_name = epi.product_name ")
                    .append("LEFT JOIN export_request_items eri ON er.id = eri.export_request_id AND weh.product_name = eri.product_name ")
                    .append("WHERE er.status IN ('completed', 'rejected')) ")
                    .append("UNION ")
                    // Trường hợp 2: Không có trong warehouse_export_history
                    .append("(SELECT er.id, er.day_request, er.status, er.user_id, er.role, ")
                    .append("eri.product_name, eri.product_code, eri.quantity, ")
                    .append("eri.unit, eri.note, er.reason, er.reject_reason, er.day_request as actual_date ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status IN ('completed', 'rejected') ")
                    .append("AND NOT EXISTS (")
                    .append("  SELECT 1 FROM warehouse_export_history weh ")
                    .append("  WHERE weh.export_request_id = er.id AND weh.product_name = eri.product_name")
                    .append("))");

            // Thêm điều kiện tìm kiếm
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
                ExportRequestItem item = mapResultSetToCompletedExportItem(rs);
                list.add(item);
            }

        } catch (Exception e) {
            System.err.println("Error getting completed export requests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    /**
     * Đếm số lượng approved export items
     */
    public int countApprovedExportItems(String searchType, String searchValue) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder(
                    "SELECT COUNT(DISTINCT CONCAT(er.id, '-', COALESCE(epi.product_name, eri.product_name))) "
                    + "FROM export_request er "
                    + "LEFT JOIN export_request_items eri ON er.id = eri.export_request_id "
                    + "LEFT JOIN export_pending_items epi ON er.id = epi.export_request_id AND eri.product_name = epi.product_name "
                    + "WHERE er.status IN ('approved', 'partial_exported') "
                    + "AND (epi.quantity_pending > 0 OR (epi.quantity_pending IS NULL AND eri.quantity > 0))"
            );

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append(" AND er.id LIKE ?");
                        break;
                    case "productName":
                        sql.append(" AND (epi.product_name LIKE ? OR eri.product_name LIKE ?)");
                        break;
                    case "productCode":
                        sql.append(" AND (epi.product_code LIKE ? OR eri.product_code LIKE ?)");
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
            System.err.println("Error counting approved export items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Đếm số lượng completed export items
     */
    public int countCompletedExportItems(String searchType, String searchValue) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT COUNT(*) FROM (")
                    .append("(SELECT er.id, eri.product_name FROM export_request er ")
                    .append("INNER JOIN warehouse_export_history weh ON er.id = weh.export_request_id ")
                    .append("LEFT JOIN export_request_items eri ON er.id = eri.export_request_id AND weh.product_name = eri.product_name ")
                    .append("WHERE er.status IN ('completed', 'rejected')) ")
                    .append("UNION ")
                    .append("(SELECT er.id, eri.product_name FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status IN ('completed', 'rejected') ")
                    .append("AND NOT EXISTS (SELECT 1 FROM warehouse_export_history weh ")
                    .append("WHERE weh.export_request_id = er.id AND weh.product_name = eri.product_name))");

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
            System.err.println("Error counting completed export items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Khởi tạo export_pending_items từ export_request_items
     */
    public boolean initializeExportPendingItems(String requestId) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            // Kiểm tra xem đã có pending items chưa
            String checkSql = "SELECT COUNT(*) FROM export_pending_items WHERE export_request_id = ?";
            ps1 = conn.prepareStatement(checkSql);
            ps1.setString(1, requestId);
            rs = ps1.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Chưa có, tạo mới từ export_request_items
                String insertSql = "INSERT INTO export_pending_items "
                        + "(export_request_id, product_name, product_code, unit, unit_id, "
                        + "quantity_requested, quantity_exported, quantity_pending, note, product_id) "
                        + "SELECT export_request_id, product_name, product_code, unit, unit_id, "
                        + "quantity, 0, quantity, note, product_id "
                        + "FROM export_request_items WHERE export_request_id = ?";

                ps2 = conn.prepareStatement(insertSql);
                ps2.setString(1, requestId);
                int result = ps2.executeUpdate();

                conn.commit();
                return result > 0;
            } else {
                return true; // Đã có rồi
            }

        } catch (Exception e) {
            System.err.println("Error initializing export pending items: " + e.getMessage());
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
     * Helper method để map ResultSet cho approved items
     */
    private ExportRequestItem mapResultSetToExportRequestItem(ResultSet rs) throws SQLException {
        ExportRequestItem item = new ExportRequestItem();

        try {
            item.setExportRequestId(rs.getString("id"));
            item.setDayRequest(rs.getString("day_request"));
            item.setStatus(rs.getString("status"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal quantityRequested = rs.getBigDecimal("quantity_requested");
            BigDecimal quantityExported = rs.getBigDecimal("quantity_exported");
            BigDecimal quantityPending = rs.getBigDecimal("quantity_pending");

            item.setQuantityRequested(quantityRequested != null ? quantityRequested.doubleValue() : 0.0);
            item.setQuantityExported(quantityExported != null ? quantityExported.doubleValue() : 0.0);
            item.setQuantityPending(quantityPending != null ? quantityPending.doubleValue() : 0.0);

            // Set cho compatibility
            item.setQuantity(item.getQuantityRequested());
            item.setExportedQty(item.getQuantityExported());

            item.setNote(rs.getString("note"));
            item.setReasonDetail(rs.getString("reason"));
            item.setRejectReason(rs.getString("reject_reason"));

        } catch (SQLException e) {
            System.err.println("Error mapping export result set: " + e.getMessage());
            if (item.getExportRequestId() == null) {
                item.setExportRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("unknown");
            }
        }

        return item;
    }

    /**
     * Helper method để map ResultSet cho completed items
     */
    private ExportRequestItem mapResultSetToCompletedExportItem(ResultSet rs) throws SQLException {
        ExportRequestItem item = new ExportRequestItem();

        try {
            item.setExportRequestId(rs.getString("id"));
            item.setDayRequest(rs.getString("actual_date"));
            item.setStatus(rs.getString("status"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal quantity = rs.getBigDecimal("quantity");
            item.setQuantity(quantity != null ? quantity.doubleValue() : 0.0);
            item.setExportedQty(item.getQuantity());

            item.setNote(rs.getString("note"));
            item.setReasonDetail(rs.getString("reason"));
            item.setRejectReason(rs.getString("reject_reason"));

        } catch (SQLException e) {
            System.err.println("Error mapping completed export result set: " + e.getMessage());
            if (item.getExportRequestId() == null) {
                item.setExportRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("unknown");
            }
        }

        return item;
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
}
