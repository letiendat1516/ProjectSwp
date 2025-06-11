/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import DBContext.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import java.sql.Date;
/**
 *
 * @author Fpt06
 */
public class SupplierEvaluationDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public void evaluation(int sid, int uid, int dt, int pq, int mpc, int tr, int sq, String comment) {
        String sql = "INSERT INTO `swp`.`supplier_evaluation`\n"
                + "(\n"
                + "`supplier_id`,\n"
                + "`user_id`,\n"
                + "`expected_delivery_time`,\n"
                + "`product_quality`,\n"
                + "`market_price_comparison`,\n"
                + "`transparency_reputation`,\n"
                + "`service_quality`,\n"
                + "`comment`,\n"
                + "`avg_rate`\n"
                + ")\n"
                + "VALUES\n"
                + "(?,?,?,?,?,?,?,?,?);";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sid);
            ps.setInt(2, uid);
            ps.setInt(3, dt);
            ps.setInt(4, pq);
            ps.setInt(5, mpc);
            ps.setInt(6, tr);
            ps.setInt(7, sq);
            ps.setString(8, comment);
            float avg_rate = (dt + pq + mpc + tr + sq) / 4.0f;
            ps.setFloat(9, avg_rate);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public static void main(String[] args) {
        SupplierEvaluationDAO sed = new SupplierEvaluationDAO();
        sed.evaluation(1, 10, 1, 1, 1, 5, 5, "test chá»©c nÄƒng");
    }

}
