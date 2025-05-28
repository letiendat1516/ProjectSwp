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

/**
 *
 * @author Fpt06
 */
public class SupplierDAO{
    
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
    public List<Supplier> getLishSupplier(){
        String sql = "select * from swp.supplier";
        List<Supplier> list = new ArrayList<>();
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Supplier s = new Supplier();
                s.setSupplierID(rs.getInt("id"));
                s.setActiveFlag(rs.getInt("active_flag"));
                s.setAddress(rs.getString("address"));
                s.setCreateDate(rs.getDate("create_date"));
                s.setEmail(rs.getString("email"));
                s.setName(rs.getString("name"));
                s.setNote(rs.getString("note"));
                s.setPhone(rs.getString("phone"));
                list.add(s);
            }
        } catch (SQLException e) {
        }
        return list;
    }
    
    public List<Supplier> searchLishSupplierByName(String name){
        List<Supplier> list = new ArrayList<>();
        String sql = "select * from swp.supplier where name like '%"+name+"%'";
        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Supplier s = new Supplier();
                s.setSupplierID(rs.getInt("id"));
                s.setActiveFlag(rs.getInt("active_flag"));
                s.setAddress(rs.getString("address"));
                s.setCreateDate(rs.getDate("create_date"));
                s.setEmail(rs.getString("email"));
                s.setName(rs.getString("name"));
                s.setNote(rs.getString("note"));
                s.setPhone(rs.getString("phone"));
                list.add(s);
            }
        } catch (SQLException e) {
        }
        return list;
    }
    
    public static void main(String[] args) {
        SupplierDAO sd = new SupplierDAO();
        List<Supplier> l = sd.searchLishSupplierByName("tnhh");
        for(int i=0;i<l.size();i++){
            System.out.println(l.get(i).getSupplierID());
            System.out.println(l.get(i).getName());
        }
    }
}
