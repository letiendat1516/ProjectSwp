/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DBContext.Context;
import java.sql.*;
import java.util.Date;

public class RequestInformationDAO {

    /**
     * Thêm thông tin request mới vào database và trả về ID được tạo
     * 
     * Quy trình:
     * 1. Insert record mới vào bảng request
     * 2. Query lại để lấy ID vừa được tạo (vì ID có thể auto-generated hoặc trigger)
     */
    public String addRequestInformationIntoDB(int user_id, String role, Date day_request,
            String status, String reason, String supplier, String address, String phone, String email) {
        String generatedId = null;

        // SQL insert record mới
        String insertSql = "INSERT INTO request (user_id, role, day_request, status, reason, supplier, address, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // SQL query lại record vừa insert để lấy ID
        // Sử dụng tất cả fields để đảm bảo tìm đúng record vừa tạo
        String selectSql = "SELECT id FROM request WHERE user_id = ? AND role = ? AND day_request = ? AND status = ? AND reason = ? AND supplier = ? AND address = ? AND phone = ? AND email = ? ORDER BY id DESC LIMIT 1";

        try (
            Connection con = Context.getJDBCConnection();
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            PreparedStatement selectStmt = con.prepareStatement(selectSql)
        ) {
            // BƯỚC 1: Insert record mới
            insertStmt.setInt(1, user_id);
            insertStmt.setString(2, role);
            insertStmt.setDate(3, new java.sql.Date(day_request.getTime())); // Chuyển đổi Date
            insertStmt.setString(4, status);
            insertStmt.setString(5, reason);
            insertStmt.setString(6, supplier);
            insertStmt.setString(7, address);
            insertStmt.setString(8, phone);
            insertStmt.setString(9, email);
            insertStmt.executeUpdate();

            // BƯỚC 2: Query lại để lấy ID vừa được tạo
            // Gán cùng parameters để tìm record vừa insert
            selectStmt.setInt(1, user_id);
            selectStmt.setString(2, role);
            selectStmt.setDate(3, new java.sql.Date(day_request.getTime()));
            selectStmt.setString(4, status);
            selectStmt.setString(5, reason);
            selectStmt.setString(6, supplier);
            selectStmt.setString(7, address);
            selectStmt.setString(8, phone);
            selectStmt.setString(9, email);

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                generatedId = rs.getString("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    /**
     * Tự động tạo ID tiếp theo cho request theo format: NK{số}-{số thứ tự}
     */
    public String getNextRequestId() {
        // Query lấy ID cuối cùng có format NK{số}-{3 chữ số}
        String sql = "SELECT id FROM request WHERE id LIKE 'NK%-___' ORDER BY id DESC LIMIT 1";
        
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) {
                // Nếu chưa có record nào, bắt đầu từ NK1-000
                return "NK1-000";
            } else {
                // Parse ID cuối cùng để tạo ID tiếp theo
                String lastId = rs.getString("id");
                String prefix = lastId.split("-")[0]; // Ví dụ: "NK1"
                int num = Integer.parseInt(lastId.split("-")[1]); // Ví dụ: 003

                if (num < 999) {
                    // Nếu chưa đến 999, tăng số thứ tự
                    num++;
                    return prefix + "-" + String.format("%03d", num); // NK1-004
                } else {
                    // Nếu đã đến 999, chuyển sang prefix tiếp theo
                    int prefixNum = Integer.parseInt(prefix.substring(2)) + 1; // NK1 -> 1 -> 2
                    return "NK" + prefixNum + "-000"; // NK2-000
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Nên có error handling
        }
        return null;
    }
}