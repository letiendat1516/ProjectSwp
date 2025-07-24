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

            // Query chính để lấy các items chưa xuất hoàn thành
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

            // Chỉ lấy những item còn pending > 0 hoặc chưa có trong export_pending_items
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

            System.out.println("Approved items loaded: " + list.size());

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

            // Lấy từ warehouse_export_history (ưu tiên)
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

            System.out.println("History items loaded: " + list.size());

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

    /**
     * Cập nhật trạng thái partial export - FIXED VERSION
     */
    public boolean updatePartialExportStatus(String requestId, String productName, double quantityExported, String note) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        PreparedStatement ps5 = null;
        ResultSet rs = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            System.out.println("=== Updating Partial Export Status ===");
            System.out.println("Request ID: " + requestId);
            System.out.println("Product Name: " + productName);
            System.out.println("Quantity Exported: " + quantityExported);

            // 1. Khởi tạo export_pending_items nếu chưa có
            String checkPendingSql = "SELECT COUNT(*) FROM export_pending_items WHERE export_request_id = ?";
            ps1 = conn.prepareStatement(checkPendingSql);
            ps1.setString(1, requestId);
            rs = ps1.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Chưa có pending items, tạo mới
                String initSql = """
                    INSERT INTO export_pending_items 
                    (export_request_id, product_name, product_code, unit, unit_id,
                     quantity_requested, quantity_exported, quantity_pending, note, product_id)
                    SELECT export_request_id, product_name, product_code, unit, unit_id,
                           quantity, 0, quantity, note, product_id
                    FROM export_request_items 
                    WHERE export_request_id = ?
                    """;
                ps2 = conn.prepareStatement(initSql);
                ps2.setString(1, requestId);
                int initResult = ps2.executeUpdate();
                System.out.println("Initialized " + initResult + " pending items");
                ps2.close();
            }
            rs.close();
            ps1.close();

            // 2. Kiểm tra và lấy thông tin hiện tại từ export_pending_items
            String checkSql = "SELECT quantity_requested, quantity_exported, quantity_pending, product_code "
                    + "FROM export_pending_items WHERE export_request_id = ? AND product_name = ?";
            ps1 = conn.prepareStatement(checkSql);
            ps1.setString(1, requestId);
            ps1.setString(2, productName);
            rs = ps1.executeQuery();

            if (rs.next()) {
                double quantityRequested = rs.getDouble("quantity_requested");
                double currentExported = rs.getDouble("quantity_exported");
                double currentPending = rs.getDouble("quantity_pending");
                String productCode = rs.getString("product_code");

                // Tính toán giá trị mới
                double newExported = currentExported + quantityExported;
                double newPending = Math.max(0, quantityRequested - newExported);

                System.out.println("Current exported: " + currentExported);
                System.out.println("Current pending: " + currentPending);
                System.out.println("New exported: " + newExported);
                System.out.println("New pending: " + newPending);

                // Kiểm tra xem có vượt quá số lượng yêu cầu không
                if (newExported > quantityRequested) {
                    System.err.println("Export quantity exceeds requested quantity!");
                    conn.rollback();
                    return false;
                }

                // 3. Cập nhật export_pending_items
                String updateSql = "UPDATE export_pending_items SET quantity_exported = ?, quantity_pending = ?, "
                        + "note = CASE WHEN ? IS NOT NULL AND ? != '' THEN ? ELSE note END, "
                        + "updated_at = CURRENT_TIMESTAMP "
                        + "WHERE export_request_id = ? AND product_name = ?";
                ps2 = conn.prepareStatement(updateSql);
                ps2.setDouble(1, newExported);
                ps2.setDouble(2, newPending);
                ps2.setString(3, note);
                ps2.setString(4, note);
                ps2.setString(5, note);
                ps2.setString(6, requestId);
                ps2.setString(7, productName);
                int updateResult = ps2.executeUpdate();
                System.out.println("Export pending items update result: " + updateResult);
                ps2.close();

                // 4. Thêm vào warehouse_export_history
                String historySql = """
                    INSERT INTO warehouse_export_history 
                    (export_request_id, product_name, product_code, quantity_exported, 
                     export_date, note, exported_by)
                    VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, 'System')
                    """;
                ps3 = conn.prepareStatement(historySql);
                ps3.setString(1, requestId);
                ps3.setString(2, productName);
                ps3.setString(3, productCode);
                ps3.setDouble(4, quantityExported);
                ps3.setString(5, note != null ? note : "");
                int historyResult = ps3.executeUpdate();
                System.out.println("Export history insert result: " + historyResult);
                ps3.close();

                // 5. Cập nhật tồn kho trong product_info
                String updateInventorySql = "UPDATE product_info SET current_stock = current_stock - ? "
                        + "WHERE code = ? AND current_stock >= ?";
                ps4 = conn.prepareStatement(updateInventorySql);
                ps4.setDouble(1, quantityExported);
                ps4.setString(2, productCode);
                ps4.setDouble(3, quantityExported);
                int inventoryResult = ps4.executeUpdate();
                System.out.println("Inventory update result: " + inventoryResult);
                ps4.close();

                if (inventoryResult == 0) {
                    System.err.println("Failed to update inventory - insufficient stock!");
                    conn.rollback();
                    return false;
                }

                // 6. Kiểm tra xem còn pending items nào khác không
                String checkAllPendingSql = "SELECT COUNT(*) FROM export_pending_items "
                        + "WHERE export_request_id = ? AND quantity_pending > 0";
                ps5 = conn.prepareStatement(checkAllPendingSql);
                ps5.setString(1, requestId);
                ResultSet rs2 = ps5.executeQuery();

                String newStatus = "partial_exported"; // Mặc định
                if (rs2.next() && rs2.getInt(1) == 0) {
                    // Không còn pending items nào, cập nhật status thành completed
                    newStatus = "completed";
                }
                rs2.close();
                ps5.close();

                // 7. Cập nhật trạng thái export_request
                String updateStatusSql = "UPDATE export_request SET status = ?, "
                        + "reason = CONCAT(COALESCE(reason, ''), '; Xuất từng phần: ', ?, ' - Số lượng: ', ?) "
                        + "WHERE id = ?";
                ps5 = conn.prepareStatement(updateStatusSql);
                ps5.setString(1, newStatus);
                ps5.setString(2, productName);
                ps5.setDouble(3, quantityExported);
                ps5.setString(4, requestId);
                int statusResult = ps5.executeUpdate();
                System.out.println("Status update result: " + statusResult + " - New status: " + newStatus);
                ps5.close();

                conn.commit();
                System.out.println("=== Partial export update completed successfully ===");
                return true;

            } else {
                System.err.println("No pending item found for request: " + requestId + ", product: " + productName);
                conn.rollback();
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error updating partial export status: " + e.getMessage());
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
            closeResource(ps2);
            closeResource(ps3);
            closeResource(ps4);
            closeResource(ps5);
        }

        return false;
    }

    /**
     * Thêm record vào warehouse_export_history
     */
    public boolean addExportHistory(String requestId, String productName, double quantity, String note) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO warehouse_export_history "
                    + "(export_request_id, product_name, product_code, quantity_exported, export_date, note, exported_by) "
                    + "SELECT ?, eri.product_name, eri.product_code, ?, CURRENT_TIMESTAMP, ?, ? "
                    + "FROM export_request_items eri "
                    + "WHERE eri.export_request_id = ? AND eri.product_name = ?";

            conn = Context.getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, requestId);
            ps.setDouble(2, quantity);
            ps.setString(3, note != null ? note : "");
            ps.setString(4, "System"); // Có thể truyền user name vào
            ps.setString(5, requestId);
            ps.setString(6, productName);

            int result = ps.executeUpdate();
            System.out.println("Export history added: " + result + " rows affected");
            return result > 0;

        } catch (Exception e) {
            System.err.println("Error adding export history: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, null);
        }

        return false;
    }

    /**
     * Hoàn thành xuất kho toàn bộ
     */
    public boolean completeExportRequest(String requestId, String note, String username) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        PreparedStatement ps5 = null;
        ResultSet rs = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            System.out.println("=== Completing Export Request: " + requestId + " ===");

            // 1. Khởi tạo export_pending_items nếu chưa có
            String checkPendingSql = "SELECT COUNT(*) FROM export_pending_items WHERE export_request_id = ?";
            ps1 = conn.prepareStatement(checkPendingSql);
            ps1.setString(1, requestId);
            rs = ps1.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Chưa có pending items, tạo mới
                String initSql = """
                    INSERT INTO export_pending_items 
                    (export_request_id, product_name, product_code, unit, unit_id,
                     quantity_requested, quantity_exported, quantity_pending, note, product_id)
                    SELECT export_request_id, product_name, product_code, unit, unit_id,
                           quantity, 0, quantity, note, product_id
                    FROM export_request_items 
                    WHERE export_request_id = ?
                    """;
                ps2 = conn.prepareStatement(initSql);
                ps2.setString(1, requestId);
                int initResult = ps2.executeUpdate();
                System.out.println("Initialized " + initResult + " pending items");
                ps2.close();
            }
            rs.close();
            ps1.close();

            // 2. Lấy tất cả pending items
            String getPendingSql = "SELECT product_name, product_code, quantity_pending FROM export_pending_items "
                    + "WHERE export_request_id = ? AND quantity_pending > 0";
            ps1 = conn.prepareStatement(getPendingSql);
            ps1.setString(1, requestId);
            rs = ps1.executeQuery();

            List<Object[]> pendingItems = new ArrayList<>();
            while (rs.next()) {
                pendingItems.add(new Object[]{
                    rs.getString("product_name"),
                    rs.getString("product_code"),
                    rs.getDouble("quantity_pending")
                });
            }
            rs.close();
            ps1.close();

            if (pendingItems.isEmpty()) {
                System.out.println("No pending items to complete");
                conn.commit();
                return true;
            }

            // 3. Xuất hết tất cả pending items
            for (Object[] item : pendingItems) {
                String productName = (String) item[0];
                String productCode = (String) item[1];
                double pendingQty = (Double) item[2];

                // Cập nhật pending items
                String updatePendingSql = "UPDATE export_pending_items SET "
                        + "quantity_exported = quantity_requested, quantity_pending = 0, "
                        + "updated_at = CURRENT_TIMESTAMP "
                        + "WHERE export_request_id = ? AND product_name = ?";
                ps2 = conn.prepareStatement(updatePendingSql);
                ps2.setString(1, requestId);
                ps2.setString(2, productName);
                int updateResult = ps2.executeUpdate();
                System.out.println("Updated pending item: " + productName + " - " + updateResult + " rows");
                ps2.close();

                // Thêm vào warehouse_export_history
                String historySql = """
                    INSERT INTO warehouse_export_history 
                    (export_request_id, product_name, product_code, quantity_exported, 
                     export_date, note, exported_by)
                    VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)
                    """;
                ps3 = conn.prepareStatement(historySql);
                ps3.setString(1, requestId);
                ps3.setString(2, productName);
                ps3.setString(3, productCode);
                ps3.setDouble(4, pendingQty);
                ps3.setString(5, note != null ? note : "Hoàn thành xuất kho");
                ps3.setString(6, username != null ? username : "System");
                int historyResult = ps3.executeUpdate();
                System.out.println("Added export history: " + productName + " - " + historyResult + " rows");
                ps3.close();

                // Cập nhật tồn kho
                String updateInventorySql = "UPDATE product_info SET current_stock = current_stock - ? "
                        + "WHERE code = ? AND current_stock >= ?";
                ps4 = conn.prepareStatement(updateInventorySql);
                ps4.setDouble(1, pendingQty);
                ps4.setString(2, productCode);
                ps4.setDouble(3, pendingQty);
                int inventoryResult = ps4.executeUpdate();
                System.out.println("Updated inventory: " + productCode + " - " + inventoryResult + " rows");
                ps4.close();

                if (inventoryResult == 0) {
                    System.err.println("Insufficient inventory for product: " + productCode);
                    conn.rollback();
                    return false;
                }
            }

            // 4. Cập nhật trạng thái request thành completed
            String updateStatusSql = "UPDATE export_request SET status = 'completed', "
                    + "reason = CONCAT(COALESCE(reason, ''), '; Hoàn thành xuất kho toàn bộ') "
                    + "WHERE id = ?";
            ps5 = conn.prepareStatement(updateStatusSql);
            ps5.setString(1, requestId);
            int statusResult = ps5.executeUpdate();
            System.out.println("Updated request status: " + statusResult + " rows");
            ps5.close();

            conn.commit();
            System.out.println("=== Export request completed successfully ===");
            return true;

        } catch (Exception e) {
            System.err.println("Error completing export request: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back: " + rollbackEx.getMessage());
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
            closeResource(ps2);
            closeResource(ps3);
            closeResource(ps4);
            closeResource(ps5);
        }

        return false;
    }

    /**
     * Từ chối xuất kho
     */
    public boolean rejectExportRequest(String requestId, String rejectReason, String username) {
        Connection conn = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try {
            conn = Context.getJDBCConnection();
            conn.setAutoCommit(false);

            // 1. Cập nhật trạng thái export_request
            String updateSql = "UPDATE export_request SET status = 'rejected', reject_reason = ? WHERE id = ?";
            ps1 = conn.prepareStatement(updateSql);
            ps1.setString(1, rejectReason);
            ps1.setString(2, requestId);
            int updateResult = ps1.executeUpdate();
            System.out.println("Export request rejected: " + updateResult + " rows affected");
            ps1.close();

            // 2. Thêm vào warehouse_export_history để ghi lại việc từ chối
            String historySql = """
                INSERT INTO warehouse_export_history 
                (export_request_id, product_name, product_code, quantity_exported, 
                 export_date, note, exported_by)
                SELECT ?, product_name, product_code, 0, CURRENT_TIMESTAMP, ?, ?
                FROM export_request_items 
                WHERE export_request_id = ?
                """;
            ps2 = conn.prepareStatement(historySql);
            ps2.setString(1, requestId);
            ps2.setString(2, "Từ chối xuất kho: " + rejectReason);
            ps2.setString(3, username != null ? username : "System");
            ps2.setString(4, requestId);
            int historyResult = ps2.executeUpdate();
            System.out.println("Added reject history: " + historyResult + " rows");
            ps2.close();

            conn.commit();
            return updateResult > 0;

        } catch (Exception e) {
            System.err.println("Error rejecting export request: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto commit: " + e.getMessage());
            }
            closeResources(conn, ps1, null);
            closeResource(ps2);
        }

        return false;
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
                System.out.println("Initialized " + result + " pending items for request: " + requestId);
                return result > 0;
            } else {
                System.out.println("Pending items already exist for request: " + requestId);
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
            closeResource(ps2);
        }

        return false;
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

            System.out.println("Mapped approved item: " + item.getProductName()
                    + " - Status: " + item.getStatus()
                    + " - Pending: " + item.getQuantityPending());

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

            System.out.println("Mapped history item: " + item.getProductName()
                    + " - Status: " + item.getStatus()
                    + " - Exported: " + item.getExportedQty());

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
