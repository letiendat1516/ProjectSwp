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
     * Retrieve approved export request items with pagination and search.
     */
    public List<ExportRequestItem> getApprovedExportItems(String searchType, String searchValue, int page, int pageSize) {
        List<ExportRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT ")
                    .append("er.id as export_request_id, ")
                    .append("er.day_request, ")
                    .append("er.status, ")
                    .append("er.user_id, ")
                    .append("er.role, ")
                    .append("er.reason, ")
                    .append("er.reject_reason, ")
                    .append("eri.product_name, ")
                    .append("eri.product_code, ")
                    .append("eri.unit, ")
                    .append("eri.quantity, ")
                    .append("eri.note, ")
                    .append("eri.product_id, ")
                    .append("eri.unit_id ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status = 'approved' ");

            // Add search condition
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String trimmedValue = searchValue.trim();
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                    case "productName":
                        sql.append("AND LOWER(eri.product_name) LIKE LOWER(?) ");
                        break;
                    case "productCode":
                        sql.append("AND LOWER(eri.product_code) LIKE LOWER(?) ");
                        break;
                    default:
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                }
            }

            sql.append("ORDER BY er.day_request DESC, er.id DESC, eri.product_name ASC ");
            sql.append("LIMIT ?, ?");

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String searchPattern = "%" + searchValue.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
            }

            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                ExportRequestItem item = mapResultSetToExportRequestItem(rs);
                list.add(item);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getApprovedExportItems: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    /**
     * Retrieve completed or rejected export request items with pagination and
     * search. Updated to include recipient information.
     */
    public List<ExportRequestItem> getCompletedExportItems(String searchType, String searchValue, int page, int pageSize) {
        List<ExportRequestItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ")
                    .append("er.id as export_request_id, ")
                    .append("er.day_request, ")
                    .append("er.status, ")
                    .append("er.reason, ")
                    .append("er.reject_reason, ")
                    .append("er.recipient, ") // Thêm cột recipient
                    .append("eri.product_name, ")
                    .append("eri.product_code, ")
                    .append("eri.unit, ")
                    .append("eri.quantity, ")
                    .append("COALESCE(eri.exported_qty, 0) as exported_qty, ")
                    .append("eri.note ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status IN ('completed', 'rejected') ");

            // Add search condition
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String trimmedValue = searchValue.trim();
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                    case "productName":
                        sql.append("AND LOWER(eri.product_name) LIKE LOWER(?) ");
                        break;
                    case "productCode":
                        sql.append("AND LOWER(eri.product_code) LIKE LOWER(?) ");
                        break;
                    default:
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                }
            }

            sql.append("ORDER BY er.day_request DESC, er.id DESC ");
            sql.append("LIMIT ?, ?");

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
                ExportRequestItem item = mapResultSetToExportRequestItem(rs);
                item.setExportedQty(rs.getDouble("exported_qty"));
                // Thêm recipient vào item
                item.setRecipient(rs.getString("recipient"));
                list.add(item);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getCompletedExportItems: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    /**
     * Count approved export items with search.
     */
    public int countApprovedExportItems(String searchType, String searchValue) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(DISTINCT CONCAT(er.id, '-', eri.product_code)) ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status = 'approved' ");

            // Add search condition
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                    case "productName":
                        sql.append("AND LOWER(eri.product_name) LIKE LOWER(?) ");
                        break;
                    case "productCode":
                        sql.append("AND LOWER(eri.product_code) LIKE LOWER(?) ");
                        break;
                    default:
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                }
            }

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            if (searchValue != null && !searchValue.trim().isEmpty()) {
                ps.setString(1, "%" + searchValue.trim() + "%");
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in countApprovedExportItems: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Count completed or rejected export items with search.
     */
    public int countCompletedExportItems(String searchType, String searchValue) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status IN ('completed', 'rejected') ");

            // Add search condition
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                    case "productName":
                        sql.append("AND LOWER(eri.product_name) LIKE LOWER(?) ");
                        break;
                    case "productCode":
                        sql.append("AND LOWER(eri.product_code) LIKE LOWER(?) ");
                        break;
                    default:
                        sql.append("AND LOWER(er.id) LIKE LOWER(?) ");
                        break;
                }
            }

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            if (searchValue != null && !searchValue.trim().isEmpty()) {
                ps.setString(1, "%" + searchValue.trim() + "%");
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in countCompletedExportItems: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return count;
    }

    /**
     * Map ResultSet to ExportRequestItem.
     */
    private ExportRequestItem mapResultSetToExportRequestItem(ResultSet rs) throws SQLException {
        ExportRequestItem item = new ExportRequestItem();

        try {
            item.setExportRequestId(rs.getString("export_request_id"));
            item.setDayRequest(rs.getString("day_request"));
            item.setStatus(rs.getString("status"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal quantity = rs.getBigDecimal("quantity");
            item.setQuantity(quantity != null ? quantity.doubleValue() : 0.0);

            item.setNote(rs.getString("note"));
            item.setReasonDetail(rs.getString("reason"));
            item.setRejectReason(rs.getString("reject_reason"));

            // Thêm recipient nếu có trong ResultSet
            try {
                String recipient = rs.getString("recipient");
                item.setRecipient(recipient);
            } catch (SQLException e) {
                // Nếu không có cột recipient thì bỏ qua
                item.setRecipient(null);
            }

        } catch (SQLException e) {
            System.err.println("Error mapping ResultSet to ExportRequestItem: " + e.getMessage());
            // Set default values
            if (item.getExportRequestId() == null) {
                item.setExportRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("approved");
            }
            if (item.getProductName() == null) {
                item.setProductName("N/A");
            }
            throw e;
        }

        return item;
    }

    /**
     * Close database resources.
     */
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        closeResource(rs);
        closeResource(ps);
        closeResource(conn);
    }

    private void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
}
