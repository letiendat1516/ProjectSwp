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
     * Lấy danh sách yêu cầu xuất kho đã duyệt (chỉ approved) với phân trang và
     * tìm kiếm
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

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND eri.product_name LIKE ? ");
                        break;
                    case "productCode":
                        sql.append("AND eri.product_code LIKE ? ");
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

        } catch (Exception e) {
            System.err.println("Error getting approved export requests: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    /**
     * Lấy danh sách yêu cầu xuất kho đã hoàn thành hoặc từ chối với phân trang và tìm kiếm
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
                    .append("eri.product_name, ")
                    .append("eri.product_code, ")
                    .append("eri.unit, ")
                    .append("eri.quantity, ")
                    .append("eri.exported_qty, ")
                    .append("eri.note ")
                    .append("FROM export_request er ")
                    .append("INNER JOIN export_request_items eri ON er.id = eri.export_request_id ")
                    .append("WHERE er.status IN ('completed', 'rejected') ");

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND eri.product_name LIKE ? ");
                        break;
                    case "productCode":
                        sql.append("AND eri.product_code LIKE ? ");
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
                ExportRequestItem item = new ExportRequestItem();
                item.setExportRequestId(rs.getString("export_request_id"));
                item.setDayRequest(rs.getString("day_request"));
                item.setStatus(rs.getString("status"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setExportedQty(rs.getDouble("exported_qty"));
                item.setNote(rs.getString("note"));
                item.setReasonDetail(rs.getString("reason"));
                item.setRejectReason(rs.getString("reject_reason"));

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
     * Đếm số lượng approved export items (chỉ approved)
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

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND eri.product_name LIKE ? ");
                        break;
                    case "productCode":
                        sql.append("AND eri.product_code LIKE ? ");
                        break;
                }
            }

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String searchPattern = "%" + searchValue.trim() + "%";
                ps.setString(paramIndex, searchPattern);
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
     * Đếm số lượng completed export items - SỬA LẠI ĐỂ DÙNG export_request và export_request_items
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

            // Thêm điều kiện tìm kiếm
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                switch (searchType != null ? searchType : "requestId") {
                    case "requestId":
                        sql.append("AND er.id LIKE ? ");
                        break;
                    case "productName":
                        sql.append("AND eri.product_name LIKE ? ");
                        break;
                    case "productCode":
                        sql.append("AND eri.product_code LIKE ? ");
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

    /**
     * Đếm tổng số export requests đã hoàn thành (cho thống kê)
     */
    public int getCompletedExportRequestsCount() {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(DISTINCT er.id) FROM export_request er " +
                        "WHERE er.status IN ('completed', 'rejected')";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (Exception e) {
            System.err.println("Error counting completed export requests: " + e.getMessage());
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

            BigDecimal quantity = rs.getBigDecimal("quantity");
            item.setQuantity(quantity != null ? quantity.doubleValue() : 0.0);

            // Cho approved items, chưa có số lượng đã xuất
            item.setQuantityExported(0.0);

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
                item.setStatus("approved");
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
            item.setDayRequest(rs.getString("export_date"));
            item.setStatus(rs.getString("status"));
            item.setProductName(rs.getString("product_name"));
            item.setProductCode(rs.getString("product_code"));
            item.setUnit(rs.getString("unit"));

            BigDecimal quantityRequested = rs.getBigDecimal("quantity_requested");
            BigDecimal quantityExported = rs.getBigDecimal("quantity_exported");

            item.setQuantity(quantityRequested != null ? quantityRequested.doubleValue() : 0.0);
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
