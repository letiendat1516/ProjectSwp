package dao;

import DBContext.Context;
import model.ExportRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExportRequestDAO {

    /**
     * Tạo ID mới cho đơn xuất kho theo format XK-XXX
     */
    public String generateNextExportRequestId() {
        String sql = "SELECT id FROM export_request WHERE id LIKE 'XK-%' ORDER BY CAST(SUBSTRING(id, 4) AS UNSIGNED) DESC LIMIT 1";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("id");
                // Lấy số từ XK-XXX
                String numberPart = lastId.substring(3);
                int nextNumber = Integer.parseInt(numberPart) + 1;
                return String.format("XK-%03d", nextNumber);
            } else {
                return "XK-001"; // ID đầu tiên
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi tạo ID: " + e.getMessage());
            e.printStackTrace();
            return "XK-001";
        }
    }

    /**
     * Thêm đơn yêu cầu xuất kho vào database
     */
    public String addExportRequest(ExportRequest request) {
        String sql = "INSERT INTO export_request (id, user_id, day_request, status, role) "
                + "VALUES (?, ?, ?, ?, ?)";

        // Tạo ID mới
        String newId = generateNextExportRequestId();
        request.setId(newId);

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, request.getId());
            stmt.setInt(2, request.getUserId());
            stmt.setDate(3, new java.sql.Date(request.getDayRequest().getTime()));
            stmt.setString(4, request.getStatus());
            stmt.setString(5, request.getRole());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Đã thêm đơn xuất kho: " + newId);
                return newId;
            } else {
                System.out.println("Không thể thêm đơn xuất kho");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm đơn xuất kho: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy danh sách đơn xuất kho theo user
     */
    public List<ExportRequest> getExportRequestsByUser(int userId) {
        List<ExportRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM export_request WHERE user_id = ? ORDER BY day_request DESC";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ExportRequest request = new ExportRequest();
                request.setId(rs.getString("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setDayRequest(rs.getDate("day_request"));
                request.setStatus(rs.getString("status"));
                request.setRole(rs.getString("role"));
                request.setApproveBy(rs.getString("approve_by"));
                request.setCreatedAt(rs.getTimestamp("created_at"));

                requests.add(request);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách đơn xuất kho: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Lấy tất cả đơn xuất kho (cho admin)
     */
    public List<ExportRequest> getAllExportRequests() {
        List<ExportRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM export_request ORDER BY day_request DESC";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ExportRequest request = new ExportRequest();
                request.setId(rs.getString("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setDayRequest(rs.getDate("day_request"));
                request.setStatus(rs.getString("status"));
                request.setRole(rs.getString("role"));
                request.setApproveBy(rs.getString("approve_by"));
                request.setCreatedAt(rs.getTimestamp("created_at"));

                requests.add(request);
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách đơn xuất kho: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Lấy đơn xuất kho theo ID
     */
    public ExportRequest getExportRequestById(String id) {
        String sql = "SELECT * FROM export_request WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ExportRequest request = new ExportRequest();
                request.setId(rs.getString("id"));
                request.setUserId(rs.getInt("user_id"));
                request.setDayRequest(rs.getDate("day_request"));
                request.setStatus(rs.getString("status"));
                request.setRole(rs.getString("role"));
                request.setApproveBy(rs.getString("approve_by"));
                request.setCreatedAt(rs.getTimestamp("created_at"));

                return request;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy đơn xuất kho: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Cập nhật trạng thái đơn xuất kho
     */
    public boolean updateExportRequestStatus(String id, String status, String approveBy) {
        String sql = "UPDATE export_request SET status = ?, approve_by = ? WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, approveBy);
            stmt.setString(3, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật trạng thái: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
