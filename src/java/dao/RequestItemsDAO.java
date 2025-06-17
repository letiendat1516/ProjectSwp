/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DBContext.Context;
import java.sql.*;

/**
 *
 * @author Admin
 */
public class RequestItemsDAO {

    public void addItemsIntoDB(String request_id, String[] product_name, String[] product_code,
                           String[] unit, int[] quantity, String[] note, String reason_detail) {
    String sql = "INSERT INTO request_items (request_id, product_name, product_code, unit, quantity, note, reason_detail) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try {
        Connection con = Context.getJDBCConnection();
        PreparedStatement stmt = con.prepareStatement(sql);

        for (int i = 0; i < product_code.length; i++) {
            // DEBUG: In từng giá trị sẽ insert
            System.out.println(">>> Inserting item " + (i + 1));
            System.out.println("request_id: " + request_id);
            System.out.println("product_name: " + product_name[i]);
            System.out.println("product_code: " + product_code[i]);
            System.out.println("unit: " + unit[i]);
            System.out.println("quantity: " + quantity[i]);
            System.out.println("note: " + note[i]);
            System.out.println("reason_detail: " + reason_detail);

            // Gán giá trị vào statement
            stmt.setString(1, request_id);
            stmt.setString(2, product_name[i]);
            stmt.setString(3, product_code[i]);
            stmt.setString(4, unit[i]);
            stmt.setInt(5, quantity[i]);
            stmt.setString(6, note[i]);
            stmt.setString(7, reason_detail);

            int rowsAffected = stmt.executeUpdate(); // thực hiện insert

            // DEBUG: xác nhận insert thành công
            if (rowsAffected > 0) {
                System.out.println(">>> Insert thành công dòng " + (i + 1));
            } else {
                System.out.println(">>> LỖI: Không insert được dòng " + (i + 1));
            }
        }

    } catch (SQLException e) {
        System.out.println(">>> LỖI SQL khi insert request_items: ");
        e.printStackTrace();
    }
}

}