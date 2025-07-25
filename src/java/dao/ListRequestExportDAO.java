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

            sql.append("SELECT DISTINCT ")
                    .append("er.id as export_request_id, ")
                    .append("er.day_request, ")
                    .append("er.status, ")
                    .append("er.user_id, ")
                    .append("er.role, ")
                    .append("er.reason, ")
                    .append("er.reject_reason, ")
                    .append("COALESCE(epi.product_name, eri.product_name) as product_name, ")
                    .append("COALESCE(epi.product_code, eri.product_code) as product_code, ")
                    .append("COALESCE(epi.unit, eri.unit) as unit, ")
                    .append("COALESCE(epi.quantity_requested, eri.quantity) as quantity_requested, ")
                    .append("COALESCE(epi.quantity_exported, 0) as quantity_exported, ")
                    .append("COALESCE(epi.quantity_pending, eri.quantity) as quantity_pending, ")
                    .append("COALESCE(epi.note, eri.note) as note ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("LEFT JOIN export_pending_items epi ON er.id = epi.export_request_id ")
                    .append("    AND eri.product_code = epi.product_code ")
                    .append("WHERE er.status IN ('approved', 'partial_exported') ");

            sql.append("AND (epi.quantity_pending > 0 OR epi.id IS NULL) ");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND (epi.product_name LIKE ? OR eri.product_name LIKE ?) ");
                        break;
                    case "productCode":
                        sql.append("AND (epi.product_code LIKE ? OR eri.product_code LIKE ?) ");
                        break;
                }
            }

            sql.append("ORDER BY ")
                    .append("CASE WHEN er.status = 'partial_exported' THEN 0 ELSE 1 END, ")
                    .append("er.day_request DESC, ")
                    .append("er.id DESC, ")
                    .append("COALESCE(epi.product_name, eri.product_name) ASC ");
            sql.append("LIMIT ?, ?");

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

            sql.append("SELECT ")
                    .append("weh.export_request_id, ")
                    .append("er.day_request, ")
                    .append("er.status, ")
                    .append("er.user_id, ")
                    .append("er.role, ")
                    .append("er.reason, ")
                    .append("er.reject_reason, ")
                    .append("weh.product_name, ")
                    .append("weh.product_code, ")
                    .append("COALESCE(eri.unit, epi.unit) as unit, ")
                    .append("COALESCE(eri.quantity, 0) as quantity_requested, ")
                    .append("weh.quantity_exported, ")
                    .append("weh.note, ")
                    .append("weh.export_date ")
                    .append("FROM warehouse_export_history weh ")
                    .append("INNER JOIN export_request er ON weh.export_request_id = er.id ")
                    .append("LEFT JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("    AND weh.product_name = eri.product_name ")
                    .append("LEFT JOIN export_pending_items epi ON er.id = epi.export_request_id ")
                    .append("    AND weh.product_name = epi.product_name ")
                    .append("WHERE er.status IN ('completed', 'rejected') ");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND weh.product_name LIKE ? ");
                        break;
                    case "productCode":
                        sql.append("AND weh.product_code LIKE ? ");
                        break;
                }
            }

            sql.append("ORDER BY weh.export_date DESC, er.id DESC, weh.product_name ASC ");
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
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(DISTINCT CONCAT(er.id, '-', COALESCE(epi.product_code, eri.product_code))) ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("LEFT JOIN export_pending_items epi ON er.id = epi.export_request_id ")
                    .append("    AND eri.product_code = epi.product_code ")
                    .append("WHERE er.status IN ('approved', 'partial_exported') ")
                    .append("AND (epi.quantity_pending > 0 OR epi.id IS NULL) ");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND (epi.product_name LIKE ? OR eri.product_name LIKE ?) ");
                        break;
                    case "productCode":
                        sql.append("AND (epi.product_code LIKE ? OR eri.product_code LIKE ?) ");
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
            sql.append("SELECT COUNT(*) FROM warehouse_export_history weh ")
                    .append("INNER JOIN export_request er ON weh.export_request_id = er.id ")
                    .append("WHERE er.status IN ('completed', 'rejected') ");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND weh.product_name LIKE ? ");
                        break;
                    case "productCode":
                        sql.append("AND weh.product_code LIKE ? ");
                        break;
                }
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

    // Helper methods để map ResultSet
    private ExportRequestItem mapResultSetToExportRequestItem(ResultSet rs) throws SQLException {
        ExportRequestItem item = new ExportRequestItem();

        try {
            item.setExportRequestId(rs.getString("export_request_id"));
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
            // Set default values
            if (item.getExportRequestId() == null) {
                item.setExportRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("unknown");
            }
            if (item.getProductName() == null) {
                item.setProductName("N/A");
            }
        }

        return item;
    }

    private ExportRequestItem mapResultSetToCompletedExportItem(ResultSet rs) throws SQLException {
        ExportRequestItem item = new ExportRequestItem();

        try {
            item.setExportRequestId(rs.getString("export_request_id"));
            item.setDayRequest(rs.getString("export_date")); // Sử dụng export_date thay vì day_request
            item.setStatus(rs.getString("status"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal quantityRequested = rs.getBigDecimal("quantity_requested");
            BigDecimal quantityExported = rs.getBigDecimal("quantity_exported");

            item.setQuantityRequested(quantityRequested != null ? quantityRequested.doubleValue() : 0.0);
            item.setQuantity(item.getQuantityRequested());

            // Cho lịch sử, quantity exported chính là quantity đã xuất
            item.setExportedQty(quantityExported != null ? quantityExported.doubleValue() : 0.0);

            item.setNote(rs.getString("note"));
            item.setReasonDetail(rs.getString("reason"));
            item.setRejectReason(rs.getString("reject_reason"));

        } catch (SQLException e) {
            System.err.println("Error mapping completed export result set: " + e.getMessage());
            // Set default values
            if (item.getExportRequestId() == null) {
                item.setExportRequestId("N/A");
            }
            if (item.getStatus() == null) {
                item.setStatus("unknown");
            }
            if (item.getProductName() == null) {
                item.setProductName("N/A");
            }
        }

        return item;
    }

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
