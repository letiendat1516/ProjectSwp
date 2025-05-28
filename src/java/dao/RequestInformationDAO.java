/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DBContext.Context;
import java.sql.*;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class RequestInformationDAO {

   public String addRequestInformationIntoDB(int user_id, String role, Date day_request,
        String status, String reason, String supplier, String address, String phone, String email) {
    String generatedId = null;

    String insertSql = "INSERT INTO request (user_id, role, day_request, status, reason, supplier, address, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String selectSql = "SELECT id FROM request WHERE user_id = ? AND role = ? AND day_request = ? AND status = ? AND reason = ? AND supplier = ? AND address = ? AND phone = ? AND email = ? ORDER BY id DESC LIMIT 1";

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
        insertStmt.setString(6, supplier);
        insertStmt.setString(7, address);
        insertStmt.setString(8, phone);
        insertStmt.setString(9, email);
        insertStmt.executeUpdate();

        // B2: Truy lại ID vừa chèn
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


    public String getNextRequestId() {
        String sql = "SELECT id FROM request WHERE id LIKE 'NK%-___' ORDER BY id DESC LIMIT 1";
        try (Connection con = Context.getJDBCConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (!rs.next()) {
                return "NK1-000";
            } else {
                String lastId = rs.getString("id");
                String prefix = lastId.split("-")[0]; // NK1
                int num = Integer.parseInt(lastId.split("-")[1]); // 003

                if (num < 999) {
                    num++;
                    return prefix + "-" + String.format("%03d", num);
                } else {
                    int prefixNum = Integer.parseInt(prefix.substring(2)) + 1;
                    return "NK" + prefixNum + "-000";
                }
            }

        } catch (Exception e) {
        }
        return null;
    }
}
