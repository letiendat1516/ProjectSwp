package dao;

import DBContext.Context;
import model.ExportRequest;
import model.ExportRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
     * Lấy thông tin export request theo ID (chỉ approved) với thông tin người
     * yêu cầu
     */
    public ExportRequest getExportRequestById(String id) {
        ExportRequest request = null;
        String sql = """
            SELECT er.*, 
                   u.username as requester_username,
                   u.fullname as requester_fullname
            FROM export_request er
            LEFT JOIN users u ON er.user_id = u.id
            WHERE er.id = ? AND er.status = 'approved'
            """;

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
                request.setRole(rs.getString("role"));
                request.setReason(rs.getString("reason"));
                request.setRejectReason(rs.getString("reject_reason"));
                request.setRecipient(rs.getString("recipient"));
                request.setApproveBy(rs.getString("approve_by"));
                request.setCreatedAt(rs.getTimestamp("created_at"));
                request.setExportAt(rs.getTimestamp("export_at"));

                // Set thông tin người yêu cầu
                request.setRequesterName(rs.getString("requester_username"));
                request.setRequesterFullName(rs.getString("requester_fullname"));

                System.out.println("✅ Found export request: " + id + " with status: " + request.getStatus());
                System.out.println("   Requester: " + request.getRequesterDisplayName());
            } else {
                System.err.println("❌ Export request not found or not approved: " + id);
            }
        } catch (SQLException e) {
            System.err.println("💥 Error getting export request by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return request;
    }

    /**
     * Lấy danh sách items của export request
     */
    public List<ExportRequestItem> getExportRequestItems(String requestId) {
        List<ExportRequestItem> list = new ArrayList<>();
        String sql = """
            SELECT 
                eri.id,
                eri.export_request_id,
                eri.product_name,
                eri.product_code,
                eri.unit,
                eri.quantity,
                eri.note,
                eri.product_id,
                eri.unit_id,
                eri.exported_qty
            FROM export_request_items eri
            WHERE eri.export_request_id = ?
            ORDER BY eri.id
            """;

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExportRequestItem item = new ExportRequestItem();
                item.setId(rs.getInt("id"));
                item.setExportRequestId(rs.getString("export_request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getDouble("quantity"));
                item.setNote(rs.getString("note"));
                item.setProductId(rs.getInt("product_id"));
                item.setUnitId(rs.getInt("unit_id"));
                item.setExportedQty(rs.getDouble("exported_qty"));

                list.add(item);
                System.out.println("   📦 Item: " + item.getProductCode() + " - Qty: " + item.getQuantity());
            }

            System.out.println("✅ Found " + list.size() + " items for request: " + requestId);
        } catch (SQLException e) {
            System.err.println("💥 Error getting export request items: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    /**
     * Xử lý xuất kho hoàn toàn (approved → completed) - Updated with recipient
     */
    public boolean processCompleteExport(String requestId, String exportDate, String recipient,
            String processor, String additionalNote, List<ExportRequestItem> exportItems) {

        System.out.println("🔍 DEBUG - Starting processCompleteExport:");
        System.out.println("   Request ID: " + requestId);
        System.out.println("   Export Date: " + exportDate);
        System.out.println("   Recipient: " + recipient);
        System.out.println("   Processor: " + processor);
        System.out.println("   Additional Note: " + additionalNote);
        System.out.println("   Items count: " + exportItems.size());

        Connection connection = null;
        try {
            connection = Context.getJDBCConnection();
            connection.setAutoCommit(false);
            System.out.println("   📡 Connection established, auto-commit disabled");

            // 1. Kiểm tra trạng thái đơn hàng trước khi xử lý
            if (!isOrderProcessableInTransaction(connection, requestId)) {
                System.err.println("❌ Order is not processable in transaction");
                connection.rollback();
                return false;
            }

            // 2. Kiểm tra tồn kho trước khi xử lý
            for (ExportRequestItem item : exportItems) {
                double exportQuantity = item.getQuantity();
                System.out.println("   🔍 Checking inventory for: " + item.getProductCode() + " - Quantity: " + exportQuantity);

                if (!checkInventoryAvailabilityInTransaction(connection, item.getProductCode(), exportQuantity)) {
                    System.err.println("❌ Insufficient inventory for: " + item.getProductCode());
                    connection.rollback();
                    return false;
                }
            }

            // 3. Xử lý từng item - cập nhật tồn kho và exported_qty
            for (ExportRequestItem item : exportItems) {
                double exportQuantity = item.getQuantity();

                System.out.println("   🔄 Processing item: " + item.getProductCode() + " - Quantity: " + exportQuantity);

                // Cập nhật tồn kho (trừ đi số lượng xuất)
                if (!updateInventoryAfterExport(connection, item.getProductCode(), exportQuantity)) {
                    System.err.println("❌ Failed to update inventory for: " + item.getProductCode());
                    connection.rollback();
                    return false;
                }

                // Cập nhật exported_qty trong export_request_items
                if (!updateExportedQuantity(connection, item.getId(), exportQuantity)) {
                    System.err.println("❌ Failed to update exported quantity for item: " + item.getId());
                    connection.rollback();
                    return false;
                }

                System.out.println("   ✅ Successfully processed item: " + item.getProductCode());
            }

            // 4. Cập nhật trạng thái export_request thành completed và ghi thông tin xuất kho
            if (!updateRequestStatusToCompleted(connection, requestId, recipient, processor, additionalNote)) {
                System.err.println("❌ Failed to update request status to completed");
                connection.rollback();
                return false;
            }

            // 5. Commit transaction
            connection.commit();
            System.out.println("✅ Export completed successfully for request: " + requestId);
            return true;

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                    System.err.println("🔄 Transaction rolled back due to error");
                }
            } catch (SQLException ex) {
                System.err.println("💥 Error rolling back transaction: " + ex.getMessage());
                ex.printStackTrace();
            }
            System.err.println("💥 Error processing complete export: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("💥 Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Kiểm tra đơn hàng có thể xử lý trong transaction
     */
    private boolean isOrderProcessableInTransaction(Connection connection, String requestId) {
        String sql = "SELECT status FROM export_request WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    boolean processable = "approved".equals(status);
                    System.out.println("   📋 Order status check: " + status + " (processable: " + processable + ")");
                    return processable;
                } else {
                    System.err.println("❌ Order not found in transaction: " + requestId);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("💥 Error checking order status in transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái export_request thành completed - Updated with
     * recipient
     */
    private boolean updateRequestStatusToCompleted(Connection connection, String requestId,
            String recipient, String processor, String additionalNote) {

        System.out.println("🔍 DEBUG - updateRequestStatusToCompleted:");
        System.out.println("   Request ID: " + requestId);
        System.out.println("   Recipient: " + recipient);
        System.out.println("   Processor: " + processor);
        System.out.println("   Additional Note: " + additionalNote);

        // Tạo reason mới bao gồm ghi chú xuất kho
        String newReason = null;
        if (additionalNote != null && !additionalNote.trim().isEmpty()) {
            newReason = "Xuất kho thành công | " + additionalNote.trim();
        } else {
            newReason = "Xuất kho thành công";
        }

        String sql = """
        UPDATE export_request 
        SET status = 'completed',
            recipient = ?,
            approve_by = ?,
            reason = CONCAT(COALESCE(reason, ''), ' | ', ?),
            export_at = CURRENT_TIMESTAMP
        WHERE id = ? AND status = 'approved'
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, recipient);
            ps.setString(2, processor);
            ps.setString(3, newReason);
            ps.setString(4, requestId);

            System.out.println("   Executing SQL with recipient: " + recipient + ", newReason: " + newReason);

            int updatedRows = ps.executeUpdate();
            System.out.println("   Rows affected: " + updatedRows);

            if (updatedRows > 0) {
                System.out.println("   ✅ Updated export_request status to completed with recipient and export_at");
                return true;
            } else {
                System.err.println("❌ No rows updated - request may not exist or not in approved status");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("💥 SQL Error updating request status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật exported_qty trong export_request_items
     */
    private boolean updateExportedQuantity(Connection connection, int itemId, double exportedQuantity) {
        String sql = "UPDATE export_request_items SET exported_qty = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, exportedQuantity);
            ps.setInt(2, itemId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("   📊 Updated exported_qty for item ID: " + itemId + " = " + exportedQuantity);
                return true;
            } else {
                System.err.println("❌ Failed to update exported_qty for item ID: " + itemId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("💥 Error updating exported quantity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra tồn kho trong transaction từ bảng product_in_stock
     */
    private boolean checkInventoryAvailabilityInTransaction(Connection connection, String productCode, double requestedQuantity) {
        String getProductIdSql = "SELECT id FROM product_info WHERE code = ?";
        String checkStockSql = "SELECT SUM(qty) as total_stock FROM product_in_stock WHERE product_id = ? AND status = 'active'";

        try (PreparedStatement getIdPs = connection.prepareStatement(getProductIdSql)) {
            getIdPs.setString(1, productCode);
            try (ResultSet idRs = getIdPs.executeQuery()) {
                if (idRs.next()) {
                    int productId = idRs.getInt("id");

                    try (PreparedStatement stockPs = connection.prepareStatement(checkStockSql)) {
                        stockPs.setInt(1, productId);
                        try (ResultSet stockRs = stockPs.executeQuery()) {
                            if (stockRs.next()) {
                                double totalStock = stockRs.getDouble("total_stock");
                                boolean available = totalStock >= requestedQuantity;
                                System.out.println("   📦 Stock check for " + productCode + " (ID: " + productId + "): " + totalStock + " >= " + requestedQuantity + " = " + available);
                                return available;
                            }
                        }
                    }
                } else {
                    System.err.println("❌ Product not found: " + productCode);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("💥 Error checking inventory in transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Cập nhật tồn kho sau khi xuất từ bảng product_in_stock
     */
    private boolean updateInventoryAfterExport(Connection connection, String productCode, double exportedQuantity) {
        String getProductIdSql = "SELECT id FROM product_info WHERE code = ?";
        String getStockRecordsSql = """
        SELECT id, qty 
        FROM product_in_stock 
        WHERE product_id = ? AND status = 'active' AND qty > 0 
        ORDER BY id ASC
        """;
        String updateStockSql = "UPDATE product_in_stock SET qty = ? WHERE id = ?";

        try (PreparedStatement getIdPs = connection.prepareStatement(getProductIdSql)) {
            getIdPs.setString(1, productCode);
            try (ResultSet idRs = getIdPs.executeQuery()) {
                if (idRs.next()) {
                    int productId = idRs.getInt("id");
                    double remainingToExport = exportedQuantity;

                    System.out.println("   🔄 Updating inventory for product ID: " + productId + ", export quantity: " + exportedQuantity);

                    try (PreparedStatement getStockPs = connection.prepareStatement(getStockRecordsSql)) {
                        getStockPs.setInt(1, productId);
                        try (ResultSet stockRs = getStockPs.executeQuery()) {
                            while (stockRs.next() && remainingToExport > 0) {
                                int stockId = stockRs.getInt("id");
                                double currentQty = stockRs.getDouble("qty");

                                if (currentQty <= remainingToExport) {
                                    try (PreparedStatement updatePs = connection.prepareStatement(updateStockSql)) {
                                        updatePs.setDouble(1, 0);
                                        updatePs.setInt(2, stockId);
                                        updatePs.executeUpdate();
                                        remainingToExport -= currentQty;
                                        System.out.println("     📉 Stock record ID " + stockId + ": " + currentQty + " → 0 (exported: " + currentQty + ")");
                                    }
                                } else {
                                    double newQty = currentQty - remainingToExport;
                                    try (PreparedStatement updatePs = connection.prepareStatement(updateStockSql)) {
                                        updatePs.setDouble(1, newQty);
                                        updatePs.setInt(2, stockId);
                                        updatePs.executeUpdate();
                                        System.out.println("     📉 Stock record ID " + stockId + ": " + currentQty + " → " + newQty + " (exported: " + remainingToExport + ")");
                                        remainingToExport = 0;
                                    }
                                }
                            }

                            if (remainingToExport > 0) {
                                System.err.println("❌ Insufficient stock to complete export. Remaining: " + remainingToExport);
                                return false;
                            }

                            System.out.println("   ✅ Successfully updated inventory for " + productCode + " (exported: " + exportedQuantity + ")");
                            return true;
                        }
                    }
                } else {
                    System.err.println("❌ Product not found: " + productCode);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("💥 Error updating inventory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng thành rejected
     */
    public boolean updateRequestStatusToRejected(String requestId, String rejectReason) {
        String sql = """
            UPDATE export_request 
            SET status = 'rejected', 
                reject_reason = ?
            WHERE id = ? AND status = 'approved'
            """;

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, rejectReason);
            ps.setString(2, requestId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Request " + requestId + " rejected successfully (rows affected: " + rows + ")");
                return true;
            } else {
                System.err.println("❌ Failed to reject request - may not exist or not in approved status: " + requestId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("💥 Error updating request status to rejected: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    /**
     * Kiểm tra xem đơn hàng có thể xử lý không (chỉ approved)
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
                boolean processable = "approved".equals(status);
                System.out.println("🔍 Order " + requestId + " status: " + status + " (processable: " + processable + ")");
                return processable;
            } else {
                System.err.println("❌ Order not found: " + requestId);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("💥 Error checking order processable status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    /**
     * Kiểm tra tồn kho trước khi xuất từ bảng product_in_stock
     */
    public boolean checkInventoryAvailability(String productCode, double requestedQuantity) {
        String getProductIdSql = "SELECT id FROM product_info WHERE code = ?";
        String checkStockSql = "SELECT SUM(qty) as total_stock FROM product_in_stock WHERE product_id = ? AND status = 'active'";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(getProductIdSql);
            ps.setString(1, productCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("id");
                rs.close();
                ps.close();

                ps = conn.prepareStatement(checkStockSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();

                if (rs.next()) {
                    double totalStock = rs.getDouble("total_stock");
                    boolean available = totalStock >= requestedQuantity;
                    System.out.println("📦 Inventory check for " + productCode + " (ID: " + productId + "): " + totalStock + " >= " + requestedQuantity + " = " + available);
                    return available;
                }
            } else {
                System.err.println("❌ Product not found for inventory check: " + productCode);
            }
        } catch (SQLException e) {
            System.err.println("💥 Error checking inventory availability: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    /**
     * Lấy thông tin tồn kho hiện tại của sản phẩm từ bảng product_in_stock
     */
    public double getCurrentStock(String productCode) {
        String getProductIdSql = "SELECT id FROM product_info WHERE code = ?";
        String getStockSql = "SELECT SUM(qty) as total_stock FROM product_in_stock WHERE product_id = ? AND status = 'active'";

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(getProductIdSql);
            ps.setString(1, productCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("id");
                rs.close();
                ps.close();

                ps = conn.prepareStatement(getStockSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();

                if (rs.next()) {
                    double stock = rs.getDouble("total_stock");
                    System.out.println("📊 Current stock for " + productCode + " (ID: " + productId + "): " + stock);
                    return stock;
                }
            } else {
                System.err.println("❌ Product not found for stock check: " + productCode);
            }
        } catch (SQLException e) {
            System.err.println("💥 Error getting current stock: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }

    /**
     * Thêm method để lấy chi tiết tồn kho theo từng record
     */
    public void getStockDetails(String productCode) {
        String getProductIdSql = "SELECT id FROM product_info WHERE code = ?";
        String getDetailsSql = """
        SELECT id, qty, status 
        FROM product_in_stock 
        WHERE product_id = ? 
        ORDER BY id ASC
        """;

        try {
            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(getProductIdSql);
            ps.setString(1, productCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                int productId = rs.getInt("id");
                rs.close();
                ps.close();

                System.out.println("📋 Stock details for " + productCode + " (ID: " + productId + "):");

                ps = conn.prepareStatement(getDetailsSql);
                ps.setInt(1, productId);
                rs = ps.executeQuery();

                double totalStock = 0;
                int recordCount = 0;

                while (rs.next()) {
                    int stockId = rs.getInt("id");
                    double qty = rs.getDouble("qty");
                    String status = rs.getString("status");

                    System.out.println("   Record ID: " + stockId + ", Qty: " + qty + ", Status: " + status);

                    if ("active".equals(status)) {
                        totalStock += qty;
                    }
                    recordCount++;
                }

                System.out.println("   Total active stock: " + totalStock + " (from " + recordCount + " records)");
            }
        } catch (SQLException e) {
            System.err.println("💥 Error getting stock details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    /**
     * Test database connection
     */
    public void testDatabaseConnection() {
        try {
            Connection conn = Context.getJDBCConnection();
            if (conn != null) {
                System.out.println("✅ Database connection successful");
                String sql = "SELECT COUNT(*) FROM export_request";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("✅ Total export requests: " + count);
                }

                rs.close();
                ps.close();
                conn.close();
            } else {
                System.err.println("❌ Database connection failed");
            }
        } catch (SQLException e) {
            System.err.println("💥 Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
