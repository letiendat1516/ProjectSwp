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
            String sql = "SELECT COUNT(*) as total_items, "
                    + "COUNT(CASE WHEN quantity_imported >= quantity_ordered THEN 1 END) as completed_items "
                    + "FROM purchase_order_items "
                    + "WHERE purchase_id = ?";

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
