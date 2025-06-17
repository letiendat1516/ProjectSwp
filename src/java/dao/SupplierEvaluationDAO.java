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
import model.SupplierEvaluation;
import model.Users;

/**
 *
 * @author Fpt06
 */
public class SupplierEvaluationDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public List<Supplier> staticMarketPrice(String sort) {
        List<Supplier> list = new ArrayList<>();
        String sql = "select swp.supplier_evaluation.supplier_id,avg(swp.supplier_evaluation.market_price_comparison) as avgrate \n"
                + "from swp.supplier_evaluation group by supplier_id order by avg(swp.supplier_evaluation.market_price_comparison) " + sort;
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SupplierDAO sd = new SupplierDAO();
                Supplier s = sd.getSupplierByID(rs.getInt("supplier_id"));
                list.add(s);
            }

        } catch (Exception e) {
        }
        return list;
    }

    public List<Supplier> staticExpectedDelivery(String sort) {
        List<Supplier> list = new ArrayList<>();
        String sql = "select swp.supplier_evaluation.supplier_id,avg(swp.supplier_evaluation.expected_delivery_time) as avgrate \n"
                + "from swp.supplier_evaluation group by supplier_id order by avg(swp.supplier_evaluation.expected_delivery_time) " + sort;
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SupplierDAO sd = new SupplierDAO();
                Supplier s = sd.getSupplierByID(rs.getInt("supplier_id"));
                list.add(s);
            }

        } catch (Exception e) {
        }
        return list;
    }

    public List<Supplier> staticRated(String sort) {
        List<Supplier> list = new ArrayList<>();
        String sql = "select swp.supplier_evaluation.supplier_id,avg(swp.supplier_evaluation.avg_rate) as avgrate \n"
                + "from swp.supplier_evaluation group by supplier_id order by avg(swp.supplier_evaluation.avg_rate) " + sort;
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SupplierDAO sd = new SupplierDAO();
                Supplier s = sd.getSupplierByID(rs.getInt("supplier_id"));
                list.add(s);
            }

        } catch (Exception e) {
        }
        return list;
    }

    public List<SupplierEvaluation> sortDescendingByDate(int id) {
        List<SupplierEvaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM swp.supplier_evaluation where supplier_id = " + id + " order by comment_time desc;";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                UserDAO ud = new UserDAO();
                SupplierDAO sd = new SupplierDAO();
                Users u = ud.getUserById(rs.getInt("user_id"));
                Supplier s = sd.getSupplierByID(rs.getInt("supplier_id"));

                SupplierEvaluation se = new SupplierEvaluation();

                se.setAvgRate(rs.getFloat("avg_rate"));
                se.setComment(rs.getString("comment"));
                se.setCommentTime(rs.getDate("comment_time"));
                se.setEditCount(rs.getInt("edit_count"));
                se.setExpectedDeliveryTime(rs.getInt("expected_delivery_time"));
                se.setMarketPriceComparison(rs.getInt("market_price_comparison"));
                se.setProductQuality(rs.getInt("product_quality"));
                se.setSupplierEvaluationID(rs.getInt("service_quality"));
                se.setTransparencyReputation(rs.getInt("transparency_reputation"));
                se.setSupplierEvaluationID(rs.getInt("id"));
                se.setSupplierID(s);
                se.setUserID(u);
                list.add(se);

            }
        } catch (SQLException e) {
        }
        return list;
    }

    public List<SupplierEvaluation> sortDescendingByStar(int id) {
        List<SupplierEvaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM swp.supplier_evaluation where supplier_id = " + id + " order by avg_rate desc;";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                UserDAO ud = new UserDAO();
                SupplierDAO sd = new SupplierDAO();
                Users u = ud.getUserById(rs.getInt("user_id"));
                Supplier s = sd.getSupplierByID(rs.getInt("supplier_id"));

                SupplierEvaluation se = new SupplierEvaluation();

                se.setAvgRate(rs.getFloat("avg_rate"));
                se.setComment(rs.getString("comment"));
                se.setCommentTime(rs.getDate("comment_time"));
                se.setEditCount(rs.getInt("edit_count"));
                se.setExpectedDeliveryTime(rs.getInt("expected_delivery_time"));
                se.setMarketPriceComparison(rs.getInt("market_price_comparison"));
                se.setProductQuality(rs.getInt("product_quality"));
                se.setSupplierEvaluationID(rs.getInt("service_quality"));
                se.setTransparencyReputation(rs.getInt("transparency_reputation"));
                se.setSupplierEvaluationID(rs.getInt("id"));
                se.setSupplierID(s);
                se.setUserID(u);
                list.add(se);

            }
        } catch (SQLException e) {
        }
        return list;
    }

    public void deleteSupplierEvaluation(int id) {
        String sql = "delete from swp.supplier_evaluation where id = " + id;
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public List<SupplierEvaluation> getSupplierEvaluationByID(int id) {

        List<SupplierEvaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM swp.supplier_evaluation where supplier_id = " + id;
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                UserDAO ud = new UserDAO();
                SupplierDAO sd = new SupplierDAO();
                Users u = ud.getUserById(rs.getInt("user_id"));
                Supplier s = sd.getSupplierByID(rs.getInt("supplier_id"));

                SupplierEvaluation se = new SupplierEvaluation();

                se.setAvgRate(rs.getFloat("avg_rate"));
                se.setComment(rs.getString("comment"));
                se.setCommentTime(rs.getDate("comment_time"));
                se.setEditCount(rs.getInt("edit_count"));
                se.setExpectedDeliveryTime(rs.getInt("expected_delivery_time"));
                se.setMarketPriceComparison(rs.getInt("market_price_comparison"));
                se.setProductQuality(rs.getInt("product_quality"));
                se.setSupplierEvaluationID(rs.getInt("service_quality"));
                se.setTransparencyReputation(rs.getInt("transparency_reputation"));
                se.setSupplierEvaluationID(rs.getInt("id"));
                se.setSupplierID(s);
                se.setUserID(u);
                list.add(se);

            }
        } catch (SQLException e) {
        }
        return list;
    }

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
            float avg_rate = (dt + mpc + tr + sq) / 4.0f;
            ps.setFloat(9, avg_rate);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public void editComment(int id, int dt, int mpc, int tr, int sq, String comment) {
        String sql = "UPDATE `swp`.`supplier_evaluation`\n"
                + "SET\n"
                + "`expected_delivery_time` = ?,\n"
                + "`market_price_comparison` = ?,\n"
                + "`transparency_reputation` = ?,\n"
                + "`service_quality` = ?,\n"
                + "`comment` = ?,\n"
                + "`avg_rate` = ?,\n"
                + "`edit_count` = ?\n"
                + "WHERE `id` = ?;";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, dt);
            ps.setInt(2, mpc);
            ps.setInt(3, tr);
            ps.setInt(4, sq);
            ps.setString(5, comment);
            float avg_rate = (dt + mpc + tr + sq) / 4.0f;
            ps.setFloat(6, avg_rate);
            ps.setInt(7, 1);
            ps.setInt(8, id);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public static void main(String[] args) {
        SupplierEvaluationDAO sed = new SupplierEvaluationDAO();
        List<SupplierEvaluation> list = sed.getSupplierEvaluationByID(1);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).getSupplierEvaluationID());
        }
//        List<Supplier> listt = sed.staticMarketPrice("desc");
//        for (int i = 0; i < listt.size(); i++) {
//            System.out.println(listt.get(i).getSupplierID());
//        }
        sed.editComment(59, 1, 2, 3, 4, "ok");
    }
}
