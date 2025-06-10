/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import DBContext.Context;
import java.sql.*;
import java.util.Date;
public class ExportRequestDAO {

    public String addExportRequestIntoDB(int user_id, String role, Date day_request,
            String status, String reason, String department, String recipient_name, 
            String recipient_phone, String recipient_email) {
        String generatedId = null;

        String insertSql = "INSERT INTO export_request (user_id, role, day_request, status, reason, department, recipient_name, recipient_phone, recipient_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String selectSql = "SELECT id FROM export_request WHERE user_id = ? AND role = ? AND day_request = ? AND status = ? AND reason = ? AND department = ? AND recipient_name = ? AND recipient_phone = ? AND recipient_email = ? ORDER BY id DESC LIMIT 1";

        try (
            Connection con = Context.getJDBCConnection();
            PreparedStatement insertStmt = con.prepareStatement(insertSql);
            PreparedStatement selectStmt = con.prepareStatement(selectSql)
        ) {
            // B1: Insert
            insertStmt.setInt(1, user_id);
            insertStmt.setString(2, role);
            insertStmt.setDate(3, new java.sql.Date(day_request.getTime()));
            insertStmt.setString(4, status);
            insertStmt.setString(5, reason);
            insertStmt.setString(6, department);
            insertStmt.setString(7, recipient_name);
            insertStmt.setString(8, recipient_phone);
            insertStmt.setString(9, recipient_email);
            insertStmt.executeUpdate();

            // B2: Truy lại ID vừa chèn
            selectStmt.setInt(1, user_id);
            selectStmt.setString(2, role);
            selectStmt.setDate(3, new java.sql.Date(day_request.getTime()));
            selectStmt.setString(4, status);
            selectStmt.setString(5, reason);
            selectStmt.setString(6, department);
            selectStmt.setString(7, recipient_name);
            selectStmt.setString(8, recipient_phone);
            selectStmt.setString(9, recipient_email);

            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                generatedId = rs.getString("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
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
            e.printStackTrace();
        }
        return null;
    }
    
    
}
