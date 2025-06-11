package dao;

import DBContext.Context;
import java.sql.*;
import java.util.Date;

public class ExportRequestDAO {

    public String addExportRequestIntoDB(int user_id, String role, Date day_request,
            String status, String reason, String department, String recipient_name,
            String recipient_phone, String recipient_email) {
        String generatedId = getNextExportRequestId(); // Tạo ID trước
        
        if (generatedId == null) {
            System.out.println("Không thể tạo ID cho export request");
            return null;
        }

        String insertSql = "INSERT INTO export_request (id, user_id, role, day_request, status, reason, department, recipient_name, recipient_phone, recipient_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
            
            insertStmt.setString(1, generatedId);  // Thêm ID vào đây
            insertStmt.setInt(2, user_id);
            insertStmt.setString(3, role);
            insertStmt.setDate(4, new java.sql.Date(day_request.getTime()));
            insertStmt.setString(5, status);
            insertStmt.setString(6, reason);
            insertStmt.setString(7, department);
            insertStmt.setString(8, recipient_name);
            insertStmt.setString(9, recipient_phone);
            insertStmt.setString(10, recipient_email);
            
            int rowsAffected = insertStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Đã thêm export request thành công với ID: " + generatedId);
                return generatedId;
            } else {
                System.out.println("Không thể thêm export request");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi SQL khi thêm export request: ");
            e.printStackTrace();
            return null;
        }
    }

    public String getNextExportRequestId() {
        String sql = "SELECT id FROM export_request WHERE id LIKE 'XK%-___' ORDER BY id DESC LIMIT 1";
        try (Connection con = Context.getJDBCConnection(); 
             PreparedStatement stmt = con.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) {
                return "XK1-000";
            } else {
                String lastId = rs.getString("id");
                String prefix = lastId.split("-")[0]; // XK1
                int num = Integer.parseInt(lastId.split("-")[1]); // 003

                if (num < 999) {
                    num++;
                    return prefix + "-" + String.format("%03d", num);
                } else {
                    int prefixNum = Integer.parseInt(prefix.substring(2)) + 1;
                    return "XK" + prefixNum + "-000";
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi tạo ID export request: ");
            e.printStackTrace();
        }
        return null;
    }
}
