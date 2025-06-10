/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Context {
    public static Connection getJDBCConnection() {

        String url = "jdbc:mysql://localhost:3306/warehouses?serverTimezone=UTC";
        String user = "root";
<<<<<<< HEAD
        String password = "PhuC2004";
=======
        String password = "tunganh2005";
>>>>>>> ab14aaaef10bf31af5dd9fb036ff479f1fe1a5ae
        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static void main(String[] args) {
        Connection conn = getJDBCConnection();
        if(conn != null){
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }
}