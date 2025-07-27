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
import model.StatisticExport;
import java.sql.Date;

/**
 *
 * @author IUHADU
 */
public class StatisticExportDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public List<StatisticExport> getStatic(Date fdate, Date tdate) {
        String sql = "SELECT \n"
                + "	a.id, \n"
                + "	a.export_at,\n"
                + "    b.product_name,\n"
                + "    b.quantity \n"
                + "FROM \n"
                + "	export_request as a join export_request_items as b on a.id = b.export_request_id \n"
                + "where \n"
                + "a.status = \"completed\" and a.export_at BETWEEN '" + fdate + "'AND '" + tdate + "' ";
        try {
            System.out.println(sql);
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<StatisticExport> l = new ArrayList<>();
            while (rs.next()) {
                StatisticExport se = new StatisticExport();
                se.setExport_at(rs.getDate("export_at"));
                se.setId(rs.getString("id"));
                se.setProduct_name(rs.getString("product_name"));
                se.setQuantity(rs.getDouble("quantity"));
                l.add(se);
            }
            return l;

        } catch (SQLException e) {
        }
        return null;
    }

    public List<StatisticExport> topExport(Date fdate, Date tdate) {
        String sql = "SELECT \n"
                + "        b.product_name,\n"
                + "        SUM(b.quantity) AS quantity_exported\n"
                + "    FROM export_request AS a \n"
                + "    JOIN export_request_items AS b ON a.id = b.export_request_id \n"
                + "    WHERE a.status = 'completed' \n"
                + "      AND a.export_at BETWEEN '" + fdate + "' AND '" + tdate + "' \n"
                + "    GROUP BY b.product_name ";
        try {
            System.out.println(sql);
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<StatisticExport> l = new ArrayList<>();
            while (rs.next()) {
                StatisticExport se = new StatisticExport();
                se.setProduct_name(rs.getString("product_name"));
                se.setQuantity(rs.getDouble("quantity_exported"));
                l.add(se);
            }
            return l;

        } catch (SQLException e) {
        }
        return null;
    }

    public List<StatisticExport> topImport(Date fdate, Date tdate) {
        String sql = " SELECT \n"
                + "        b.product_name,\n"
                + "        SUM(c.quantity_imported) AS quantity_imported\n"
                + "    FROM purchase_order_info AS a \n"
                + "    JOIN purchase_order_items AS b ON a.id = b.purchase_id \n"
                + "    JOIN warehouse_import_history AS c ON c.purchase_id = b.purchase_id \n"
                + "    WHERE c.created_at BETWEEN '"+fdate+"' AND '"+tdate+"' \n"
                + "      AND a.status IN ('completed', 'partial_imported')\n"
                + "    GROUP BY b.product_name";
        try {
            System.out.println(sql);
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<StatisticExport> l = new ArrayList<>();
            while (rs.next()) {
                StatisticExport se = new StatisticExport();
                se.setProduct_name(rs.getString("product_name"));
                se.setQuantity(rs.getDouble("quantity_imported"));
                l.add(se);
            }
            return l;

        } catch (SQLException e) {
        }
        return null;
    }

    public static void main(String[] args) {
        StatisticExportDAO sed = new StatisticExportDAO();
        List<StatisticExport> l = sed.topExport(Date.valueOf("2025-07-01"), Date.valueOf("2025-07-31"));
        System.out.println(l.get(3).getProduct_name());
    }

}
