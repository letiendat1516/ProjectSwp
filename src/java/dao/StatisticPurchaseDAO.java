/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DBContext.Context;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.StatisticPurchase;
import java.sql.Date;

/**
 *
 * @author IUHADU
 */
public class StatisticPurchaseDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public List<StatisticPurchase> getStatistic(Date fdate, Date tdate) {
        String sql = "SELECT \n"
                + "    a.id,\n"
                + "    a.supplier,\n"
                + "    a.status,\n"
                + "    b.product_name,\n"
                + "    b.price_per_unit,\n"
                + "    c.created_at,\n"
                + "    c.quantity_imported\n"
                + "FROM purchase_order_info AS a \n"
                + "JOIN purchase_order_items AS b ON a.id = b.purchase_id \n"
                + "JOIN warehouse_import_history AS c ON c.purchase_id = b.purchase_id \n"
                + "WHERE c.created_at BETWEEN '" + fdate + "' AND '" + tdate + "' and a.status in('completed','partial_imported')";
        try {
            System.out.println(sql);
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<StatisticPurchase> l = new ArrayList<>();
            while (rs.next()) {
                StatisticPurchase sp = new StatisticPurchase();
                sp.setCreateAt(rs.getDate("created_at"));
                sp.setId(rs.getString("id"));
                sp.setPricePerUnit(rs.getDouble("price_per_unit"));
                sp.setProductName(rs.getString("product_name"));
                sp.setQuantity(rs.getDouble("quantity_imported"));
                sp.setSupplierName(rs.getString("supplier"));
                sp.setStatus(rs.getString("status"));
                l.add(sp);
            }
            return l;
        } catch (SQLException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        StatisticPurchaseDAO spd = new StatisticPurchaseDAO();
        List<StatisticPurchase> l = spd.getStatistic(Date.valueOf("2025-07-01"), Date.valueOf("2025-07-01"));
    }
}
