package dao;

import DBContext.Context;
import model.ExportRequest;
import model.ExportRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ExportDAO {

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
     * Lấy thông tin export request theo ID
     */
    public ExportRequest getExportRequestById(String id) {
        ExportRequest request = null;
        String sql = "SELECT * FROM export_request WHERE id = ? AND status IN ('approved', 'partial_exported')";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                request = new ExportRequest();
                request.setId(rs.getString("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setDayRequest(rs.getDate("day_request"));
                request.setStatus(rs.getString("status"));
                request.setApproveBy(rs.getString("approve_by"));
                request.setRole(rs.getString("role"));
                request.setReason(rs.getString("reason"));
                request.setRejectReason(rs.getString("reject_reason"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting export request by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return request;
    }

    /**
     * Lấy danh sách items với thông tin xuất kho từng phần
     */
    public List<ExportRequestItem> getExportRequestItemsByOrderId(String orderId) {
        List<ExportRequestItem> list = new ArrayList<>();
        String sql = """
            SELECT 
                eri.id,
                eri.export_request_id,
                eri.product_name,
                eri.product_code,
                eri.unit,
                eri.quantity as quantity_requested,
                eri.note,
                eri.product_id,
                eri.unit_id,
                COALESCE(epi.quantity_exported, 0) as quantity_exported,
                COALESCE(epi.quantity_pending, eri.quantity) as quantity_pending
            FROM export_request_items eri
            LEFT JOIN export_pending_items epi ON eri.export_request_id = epi.export_request_id 
                AND eri.product_code = epi.product_code
            WHERE eri.export_request_id = ?
            ORDER BY eri.id
            """;

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExportRequestItem item = new ExportRequestItem();
                item.setId(rs.getInt("id"));
                item.setExportRequestId(rs.getString("export_request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setProductId(rs.getInt("product_id"));
                item.setUnitId(rs.getInt("unit_id"));

                BigDecimal quantityRequested = rs.getBigDecimal("quantity_requested");
                item.setQuantity(quantityRequested != null ? quantityRequested.doubleValue() : 0.0);
                item.setQuantityRequested(quantityRequested != null ? quantityRequested.doubleValue() : 0.0);

                BigDecimal quantityExported = rs.getBigDecimal("quantity_exported");
                item.setQuantityExported(quantityExported != null ? quantityExported.doubleValue() : 0.0);

                BigDecimal quantityPending = rs.getBigDecimal("quantity_pending");
                item.setQuantityPending(quantityPending != null ? quantityPending.doubleValue() : 0.0);

                item.setNote(rs.getString("note"));
                list.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error getting export request items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    /**
     * Xử lý xuất kho từng phần - FIXED VERSION
     */
    public boolean processPartialExport(String requestId, String exportDate, String processor,
            String additionalNote, List<ExportRequestItem> exportItems) {

        Connection connection = null;

        try {
            connection = Context.getJDBCConnection();
            connection.setAutoCommit(false);

            System.out.println("=== Starting processPartialExport ===");
            System.out.println("Request ID: " + requestId);
            System.out.println("Export Date: " + exportDate);
            System.out.println("Processor: " + processor);
            System.out.println("Items to export: " + exportItems.size());

            // 1. Khởi tạo export_pending_items nếu chưa có
            if (!initializeExportPendingItemsInTransaction(connection, requestId)) {
                System.err.println("Failed to initialize pending i  tems");
                connection.rollback();
                return false;
            }

            // 2. Xử lý từng item
            for (ExportRequestItem item : exportItems) {
                double exportQuantity = item.getExportQuantity();
                if (exportQuantity <= 0) {
                    System.out.println("Skipping item with zero export quantity: " + item.getProductName());
                    continue;
                }

                System.out.println("Processing item: " + item.getProductName() + " - Export Quantity: " + exportQuantity);

                // 2.1 Cập nhật export_pending_items
                if (!updateExportPendingItemsInTransaction(connection, requestId, item.getProductCode(), exportQuantity)) {
                    System.err.println("Failed to update pending items for: " + item.getProductCode());
                    connection.rollback();
                    return false;
                }

                // 2.2 Thêm vào warehouse_export_history
                if (!addExportHistoryInTransaction(connection, requestId, item.getProductName(),
                        item.getProductCode(), exportQuantity, exportDate, additionalNote, processor)) {
                    System.err.println("Failed to add export history for: " + item.getProductCode());
                    connection.rollback();
                    return false;
                }

                // 2.3 Cập nhật tồn kho
                if (!updateInventoryAfterExportInTransaction(connection, item.getProductCode(), exportQuantity)) {
                    System.err.println("Failed to update inventory for: " + item.getProductCode());
                    connection.rollback();
                    return false;
                }
            }

            // 3. Kiểm tra xem đã xuất đủ hàng chưa
            boolean isCompleted = checkIfExportCompletedInTransaction(connection, requestId);
            String newStatus = isCompleted ? "completed" : "partial_exported";

            System.out.println("Export completed check: " + isCompleted);
            System.out.println("New status: " + newStatus);

            // 4. Cập nhật trạng thái đơn hàng
            String reasonText = buildReasonText(exportDate, processor, additionalNote);
            if (!updateExportRequestStatusInTransaction(connection, requestId, newStatus, reasonText)) {
                System.err.println("Failed to update export request status");
                connection.rollback();
                return false;
            }

            connection.commit();
            System.out.println("=== Transaction committed successfully ===");

            // 5. Verify data after commit
            verifyExportResults(requestId);

            return true;

        } catch (Exception e) {
            System.err.println("=== Error in processPartialExport ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Transaction rolled back");
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back: " + ex.getMessage());
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Khởi tạo export_pending_items trong transaction
     */
    private boolean initializeExportPendingItemsInTransaction(Connection connection, String requestId) {
        try {
            // Kiểm tra xem đã có pending items chưa
            String checkSql = "SELECT COUNT(*) FROM export_pending_items WHERE export_request_id = ?";
            try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
                checkPs.setString(1, requestId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // Chưa có, tạo mới từ export_request_items
                        String insertSql = """
                            INSERT INTO export_pending_items 
                            (export_request_id, product_name, product_code, unit, unit_id,
                             quantity_requested, quantity_exported, quantity_pending, note, product_id)
                            SELECT export_request_id, product_name, product_code, unit, unit_id,
                                   quantity, 0, quantity, note, product_id
                            FROM export_request_items 
                            WHERE export_request_id = ?
                            """;

                        try (PreparedStatement insertPs = connection.prepareStatement(insertSql)) {
                            insertPs.setString(1, requestId);
                            int result = insertPs.executeUpdate();
                            System.out.println("Initialized " + result + " pending items for request " + requestId);
                            return result > 0;
                        }
                    } else {
                        System.out.println("Pending items already exist for request " + requestId);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing export pending items: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật export_pending_items trong transaction
     */
    private boolean updateExportPendingItemsInTransaction(Connection connection, String requestId,
            String productCode, double exportQuantity) {
        try {
            // Lấy thông tin hiện tại
            String selectSql = "SELECT quantity_requested, quantity_exported, quantity_pending "
                    + "FROM export_pending_items WHERE export_request_id = ? AND product_code = ?";

            try (PreparedStatement selectPs = connection.prepareStatement(selectSql)) {
                selectPs.setString(1, requestId);
                selectPs.setString(2, productCode);

                try (ResultSet rs = selectPs.executeQuery()) {
                    if (rs.next()) {
                        double quantityRequested = rs.getDouble("quantity_requested");
                        double currentExported = rs.getDouble("quantity_exported");
                        double newExported = currentExported + exportQuantity;
                        double newPending = Math.max(0, quantityRequested - newExported);

                        System.out.println("Updating pending item for " + productCode + ":");
                        System.out.println("  Requested: " + quantityRequested);
                        System.out.println("  Current exported: " + currentExported);
                        System.out.println("  Export quantity: " + exportQuantity);
                        System.out.println("  New exported: " + newExported);
                        System.out.println("  New pending: " + newPending);

                        // Kiểm tra không vượt quá số lượng yêu cầu
                        if (newExported > quantityRequested) {
                            System.err.println("Export quantity exceeds requested quantity!");
                            return false;
                        }

                        // Cập nhật
                        String updateSql = """
                            UPDATE export_pending_items 
                            SET quantity_exported = ?, quantity_pending = ?, updated_at = CURRENT_TIMESTAMP
                            WHERE export_request_id = ? AND product_code = ?
                            """;

                        try (PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                            updatePs.setDouble(1, newExported);
                            updatePs.setDouble(2, newPending);
                            updatePs.setString(3, requestId);
                            updatePs.setString(4, productCode);

                            int result = updatePs.executeUpdate();
                            System.out.println("Update pending items result: " + result);
                            return result > 0;
                        }
                    } else {
                        System.err.println("No pending item found for: " + productCode);
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating export pending items: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm vào warehouse_export_history trong transaction
     */
    private boolean addExportHistoryInTransaction(Connection connection, String requestId,
            String productName, String productCode, double exportQuantity, String exportDate,
            String note, String exportedBy) {
        try {
            String sql = """
                INSERT INTO warehouse_export_history 
                (export_request_id, product_name, product_code, quantity_exported, 
                 export_date, note, exported_by)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, requestId);
                ps.setString(2, productName);
                ps.setString(3, productCode);
                ps.setDouble(4, exportQuantity);
                ps.setString(5, exportDate);
                ps.setString(6, note != null ? note : "");
                ps.setString(7, exportedBy);

                int result = ps.executeUpdate();
                System.out.println("Export history added for " + productName + ": " + result + " rows");
                return result > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error adding export history: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái export_request trong transaction
     */
    private boolean updateExportRequestStatusInTransaction(Connection connection, String requestId,
            String status, String reason) {
        try {
            String sql = "UPDATE export_request SET status = ?, reason = ? WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, status);
                ps.setString(2, reason);
                ps.setString(3, requestId);

                int result = ps.executeUpdate();
                System.out.println("Export request status updated: " + result + " rows");
                return result > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error updating export request status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verify export results after transaction
     */
    private void verifyExportResults(String requestId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Context.getJDBCConnection();

            // Check export_pending_items
            String sql1 = "SELECT product_name, quantity_requested, quantity_exported, quantity_pending "
                    + "FROM export_pending_items WHERE export_request_id = ?";
            ps = conn.prepareStatement(sql1);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            System.out.println("=== Export Pending Items After Transaction ===");
            while (rs.next()) {
                System.out.println("Product: " + rs.getString("product_name"));
                System.out.println("  Requested: " + rs.getDouble("quantity_requested"));
                System.out.println("  Exported: " + rs.getDouble("quantity_exported"));
                System.out.println("  Pending: " + rs.getDouble("quantity_pending"));
            }
            rs.close();
            ps.close();

            // Check warehouse_export_history
            String sql2 = "SELECT product_name, quantity_exported, export_date "
                    + "FROM warehouse_export_history WHERE export_request_id = ? ORDER BY export_date DESC";
            ps = conn.prepareStatement(sql2);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            System.out.println("=== Export History After Transaction ===");
            while (rs.next()) {
                System.out.println("Product: " + rs.getString("product_name"));
                System.out.println("  Exported: " + rs.getDouble("quantity_exported"));
                System.out.println("  Date: " + rs.getTimestamp("export_date"));
            }

            // Check export_request status
            rs.close();
            ps.close();
            String sql3 = "SELECT status, reason FROM export_request WHERE id = ?";
            ps = conn.prepareStatement(sql3);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("=== Export Request Status ===");
                System.out.println("Status: " + rs.getString("status"));
                System.out.println("Reason: " + rs.getString("reason"));
            }

        } catch (SQLException e) {
            System.err.println("Error verifying export results: " + e.getMessage());
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(conn);
        }
    }

    /**
     * Helper method để build reason text
     */
    private String buildReasonText(String exportDate, String processor, String additionalNote) {
        StringBuilder reason = new StringBuilder();
        reason.append("Ngày xuất: ").append(exportDate);
        reason.append("; Người xử lý: ").append(processor);

        if (additionalNote != null && !additionalNote.trim().isEmpty()) {
            reason.append("; Ghi chú: ").append(additionalNote.trim());
        }

        return reason.toString();
    }

    /**
     * Cập nhật tồn kho trong transaction
     */
    private boolean updateInventoryAfterExportInTransaction(Connection connection, String productCode, double exportedQuantity) {
        String sql = "UPDATE product_info SET current_stock = current_stock - ? WHERE code = ? AND current_stock >= ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, exportedQuantity);
            ps.setString(2, productCode);
            ps.setDouble(3, exportedQuantity);

            int rows = ps.executeUpdate();
            System.out.println("Inventory update for " + productCode + ": " + rows + " rows affected");
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating inventory for " + productCode + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra xem đơn hàng đã được xuất đủ chưa trong transaction
     */
    private boolean checkIfExportCompletedInTransaction(Connection connection, String requestId) {
        String sql = "SELECT COUNT(*) FROM export_pending_items WHERE export_request_id = ? AND quantity_pending > 0";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int pendingCount = rs.getInt(1);
                    System.out.println("Pending items count: " + pendingCount);
                    return pendingCount == 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking export completion: " + e.getMessage());
        }
        return false;
    }

    /**
     * Helper method để đóng resource an toàn
     */
    private void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng thành rejected
     */
    public boolean updateRequestStatusToRejected(String requestId, String rejectReason) {
        String sql = "UPDATE export_request SET status = 'rejected', reject_reason = ? WHERE id = ? AND status IN ('approved', 'partial_exported')";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rejectReason);
            ps.setString(2, requestId);

            int rows = ps.executeUpdate();
            System.out.println("Reject update result: " + rows + " rows affected");
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
        String sql = "SELECT status FROM export_request WHERE id = ?";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                boolean processable = "approved".equals(status) || "partial_exported".equals(status);
                System.out.println("Order " + requestId + " status: " + status + ", processable: " + processable);
                return processable;
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
     * Lấy lịch sử xuất kho của một đơn hàng
     */
    public List<Object[]> getExportHistory(String requestId) {
        List<Object[]> history = new ArrayList<>();
        String sql = """
            SELECT product_name, product_code, quantity_exported, 
                   export_date, exported_by, note
            FROM warehouse_export_history 
            WHERE export_request_id = ? 
            ORDER BY export_date DESC
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
                    rs.getDouble("quantity_exported"),
                    rs.getTimestamp("export_date"),
                    rs.getString("exported_by"),
                    rs.getString("note")
                };
                history.add(record);
            }
            System.out.println("Export history loaded: " + history.size() + " records");
        } catch (SQLException e) {
            System.err.println("Error getting export history: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return history;
    }

    /**
     * Kiểm tra tồn kho trước khi xuất
     */
    public boolean checkInventoryAvailability(String productCode, double requestedQuantity) {
        String sql = "SELECT current_stock FROM product_info WHERE code = ?";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, productCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                double currentStock = rs.getDouble("current_stock");
                boolean available = currentStock >= requestedQuantity;
                System.out.println("Inventory check for " + productCode + ": current=" + currentStock + ", requested=" + requestedQuantity + ", available=" + available);
                return available;
            }
        } catch (SQLException e) {
            System.err.println("Error checking inventory: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    /**
     * Khởi tạo export_pending_items từ export_request_items
     */
    public boolean initializeExportPendingItems(String requestId) {
        Connection connection = null;

        try {
            connection = Context.getJDBCConnection();
            connection.setAutoCommit(false);

            boolean result = initializeExportPendingItemsInTransaction(connection, requestId);

            if (result) {
                connection.commit();
                System.out.println("Successfully initialized pending items for request " + requestId);
            } else {
                connection.rollback();
                System.err.println("Failed to initialize pending items for request " + requestId);
            }

            return result;

        } catch (Exception e) {
            System.err.println("Error initializing export pending items: " + e.getMessage());
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back initialization: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }

        return false;
    }

    /**
     * Lấy trạng thái cuối cùng của export request
     */
    public String getFinalExportStatus(String requestId) {
        String sql = "SELECT status FROM export_request WHERE id = ?";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                System.out.println("Final export status for " + requestId + ": " + status);
                return status;
            }
        } catch (SQLException e) {
            System.err.println("Error getting final export status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return "unknown";
    }
}
