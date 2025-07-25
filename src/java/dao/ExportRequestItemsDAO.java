    package dao;

import DBContext.Context;
import model.ExportRequestItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportRequestItemsDAO {

    /**
     * Thêm danh sách items vào đơn xuất kho
     */
    public boolean addExportRequestItems(List<ExportRequestItem> items) {
        String sql = "INSERT INTO export_request_items (export_request_id, product_name, "
                + "product_code, unit, quantity, note, product_id, unit_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = Context.getJDBCConnection();
            con.setAutoCommit(false); // Bắt đầu transaction

            stmt = con.prepareStatement(sql);

            for (ExportRequestItem item : items) {
                // Lấy unit_id từ product_info thông qua product_id
                int unitId = getUnitIdByProductId(item.getProductId());
                
                stmt.setString(1, item.getExportRequestId());
                stmt.setString(2, item.getProductName());
                stmt.setString(3, item.getProductCode());
                stmt.setString(4, item.getUnit());
                stmt.setBigDecimal(5, java.math.BigDecimal.valueOf(item.getQuantity()));
                stmt.setString(6, item.getNote());
                stmt.setInt(7, item.getProductId());
                stmt.setInt(8, unitId);

                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();
            con.commit(); // Commit transaction

            System.out.println("Đã thêm " + results.length + " items vào đơn xuất kho");
            return true;

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm items: " + e.getMessage());
            e.printStackTrace();
            
            // Rollback nếu có lỗi
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    System.out.println("Lỗi rollback: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            // Đóng resources
            try {
                if (stmt != null) stmt.close();
                if (con != null) {
                    con.setAutoCommit(true); // Reset auto commit
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Lỗi đóng connection: " + e.getMessage());
            }
        }
    }

    /**
     * Lấy unit_id từ product_info theo product_id
     */
    private int getUnitIdByProductId(int productId) {
        String sql = "SELECT unit_id FROM product_info WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("unit_id");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy unit_id: " + e.getMessage());
            e.printStackTrace();
        }

        return 1; // Trả về 1 (default unit) nếu không tìm thấy
    }

    /**
     * Lấy danh sách items theo export request ID với thông tin unit đầy đủ
     */
    public List<ExportRequestItem> getItemsByExportRequestId(String exportRequestId) {
        List<ExportRequestItem> items = new ArrayList<>();
        String sql = "SELECT eri.*, u.symbol as unit_symbol, u.name as unit_name "
                + "FROM export_request_items eri "
                + "LEFT JOIN unit u ON eri.unit_id = u.id "
                + "WHERE eri.export_request_id = ? "
                + "ORDER BY eri.id";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, exportRequestId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExportRequestItem item = new ExportRequestItem();
                item.setId(rs.getInt("id"));
                item.setExportRequestId(rs.getString("export_request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit_symbol")); // Lấy symbol từ bảng unit
                item.setQuantity(rs.getBigDecimal("quantity").doubleValue());
                item.setNote(rs.getString("note"));
                item.setProductId(rs.getInt("product_id"));
                item.setUnitId(rs.getInt("unit_id"));
                item.setExportedQty(rs.getBigDecimal("exported_qty").doubleValue());

                items.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy items: " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Cập nhật số lượng đã xuất
     */
    public boolean updateExportedQuantity(int itemId, double exportedQty) {
        String sql = "UPDATE export_request_items SET exported_qty = ? WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setBigDecimal(1, java.math.BigDecimal.valueOf(exportedQty));
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Cập nhật exported_qty cho item " + itemId + ": " + exportedQty);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật số lượng xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa tất cả items của một export request
     */
    public boolean deleteItemsByExportRequestId(String exportRequestId) {
        String sql = "DELETE FROM export_request_items WHERE export_request_id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, exportRequestId);
            int rowsAffected = stmt.executeUpdate();
            
            System.out.println("Đã xóa " + rowsAffected + " items của export request: " + exportRequestId);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa items: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra xem export request có items hay không
     */
    public boolean hasItems(String exportRequestId) {
        String sql = "SELECT COUNT(*) as item_count FROM export_request_items WHERE export_request_id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, exportRequestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_count") > 0;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra items: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy tổng số lượng items của một export request
     */
    public int getItemCount(String exportRequestId) {
        String sql = "SELECT COUNT(*) as item_count FROM export_request_items WHERE export_request_id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, exportRequestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_count");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi đếm items: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Lấy thông tin chi tiết một item
     */
    public ExportRequestItem getItemById(int itemId) {
        String sql = "SELECT eri.*, u.symbol as unit_symbol, u.name as unit_name "
                + "FROM export_request_items eri "
                + "LEFT JOIN unit u ON eri.unit_id = u.id "
                + "WHERE eri.id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ExportRequestItem item = new ExportRequestItem();
                item.setId(rs.getInt("id"));
                item.setExportRequestId(rs.getString("export_request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit_symbol"));
                item.setQuantity(rs.getBigDecimal("quantity").doubleValue());
                item.setNote(rs.getString("note"));
                item.setProductId(rs.getInt("product_id"));
                item.setUnitId(rs.getInt("unit_id"));
                item.setExportedQty(rs.getBigDecimal("exported_qty").doubleValue());

                return item;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy item by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Cập nhật thông tin một item
     */
    public boolean updateItem(ExportRequestItem item) {
        String sql = "UPDATE export_request_items SET product_name = ?, product_code = ?, "
                + "unit = ?, quantity = ?, note = ?, product_id = ?, unit_id = ? "
                + "WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, item.getProductName());
            stmt.setString(2, item.getProductCode());
            stmt.setString(3, item.getUnit());
            stmt.setBigDecimal(4, java.math.BigDecimal.valueOf(item.getQuantity()));
            stmt.setString(5, item.getNote());
            stmt.setInt(6, item.getProductId());
            stmt.setInt(7, item.getUnitId());
            stmt.setInt(8, item.getId());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Cập nhật item " + item.getId() + ": " + (rowsAffected > 0 ? "thành công" : "thất bại"));
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
