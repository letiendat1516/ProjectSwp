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

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            con.setAutoCommit(false); // Bắt đầu transaction

            for (ExportRequestItem item : items) {
                stmt.setString(1, item.getExportRequestId());
                stmt.setString(2, item.getProductName());
                stmt.setString(3, item.getProductCode());
                stmt.setString(4, item.getUnit());
                stmt.setDouble(5, item.getQuantity());
                stmt.setString(6, item.getNote());
                stmt.setInt(7, item.getProductId());

                // Lấy unit_id từ product_info thông qua product_id
                int unitId = getUnitIdByProductId(item.getProductId());
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
            return false;
        }
    }

    /**
     * Lấy unit_id từ product_info theo product_id
     */
    private int getUnitIdByProductId(int productId) {
        String sql = "SELECT unit_id FROM product_info WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("unit_id");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy unit_id: " + e.getMessage());
            e.printStackTrace();
        }

        return 0; // Trả về 0 nếu không tìm thấy
    }

    /**
     * Lấy danh sách items theo export request ID với thông tin unit đầy đủ
     */
    public List<ExportRequestItem> getItemsByExportRequestId(String exportRequestId) {
        List<ExportRequestItem> items = new ArrayList<>();
        String sql = "SELECT eri.*, u.symbol as unit_symbol, u.name as unit_name "
                + "FROM export_request_items eri "
                + "LEFT JOIN unit u ON eri.unit_id = u.id "
                + "WHERE eri.export_request_id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, exportRequestId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExportRequestItem item = new ExportRequestItem();
                item.setId(rs.getInt("id"));
                item.setExportRequestId(rs.getString("export_request_id"));
                item.setProductName(rs.getString("product_name"));
                item.setProductCode(rs.getString("product_code"));
                item.setUnit(rs.getString("unit_symbol")); // Lấy symbol từ bảng unit
                item.setQuantity(rs.getDouble("quantity"));
                item.setNote(rs.getString("note"));
                item.setProductId(rs.getInt("product_id"));
                item.setExportedQty(rs.getDouble("exported_qty"));

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

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDouble(1, exportedQty);
            stmt.setInt(2, itemId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật số lượng xuất: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
