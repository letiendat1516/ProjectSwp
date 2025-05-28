/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import model.Request;
import model.RequestItem;

public class RequestDAO {

    protected Connection connection;
    PreparedStatement stm;
    ResultSet rs;

    public ArrayList<Request> getRequests() {
        ArrayList<Request> data = new ArrayList<>();
        try {
            String strSQL = "select r.*, u.fullname from request r join users u on r.user_id = u.id";
            stm = connection.prepareStatement(strSQL);
            rs = stm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);  // code
                int userId = rs.getInt(2);    // user_id
                Date dayRequest = rs.getDate(3);  // create_date
                String status = rs.getString(4);   // status
                String reason = rs.getString(5);   // reason
                String supplier = rs.getString(6);  // supplier
                String address = rs.getString(7);   // address
                String phone = rs.getString(8);     // phone
                String email = rs.getString(9);     // email

                Request request = new Request(id, userId, dayRequest, status, reason,
                        supplier, address, phone, email);
                data.add(request);
            }
        } catch (Exception e) {
            System.out.println("getRequests" + e.getMessage());
        }
        return data;
    }

    public Request getRequestById(String id) {
        try {
            String strSQL = "select * from request where code=?";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, id);
            rs = stm.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt(2);        // user_id
                Date dayRequest = rs.getDate(3);  // create_date
                String status = rs.getString(4);   // status
                String reason = rs.getString(5);   // reason
                String supplier = rs.getString(6);  // supplier
                String address = rs.getString(7);   // address
                String phone = rs.getString(8);     // phone
                String email = rs.getString(9);     // email

                Request p = new Request(id, userId, dayRequest, status, reason,
                        supplier, address, phone, email);
                return p;
            }
        } catch (Exception e) {
            System.out.println("getRequests" + e.getMessage());
        }
        return null;
    }

    public ArrayList<RequestItem> getRequestItems(String requestId) {
        ArrayList<RequestItem> data = new ArrayList<>();
        try {
            String strSQL = "select ri.*, p.name, p.code, u.name from request_items ri "
                    + "join product_info p on ri.product_id = p.id "
                    + "join unit u on p.unit_id = u.id "
                    + "where ri.request_id = (select id from request where code = ?)";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, requestId);
            rs = stm.executeQuery();
            while (rs.next()) {
                String productName = rs.getString(4);   // p.name
                String productCode = rs.getString(5);   // p.code
                String unit = rs.getString(6);         // u.name
                int quantity = rs.getInt(2);           // ri.quantity
                String note = rs.getString(3);         // ri.note
                String reasonDetail = "";              // Nếu có trường này trong DB

                RequestItem item = new RequestItem(requestId, productName, productCode,
                        unit, quantity, note, reasonDetail);
                data.add(item);
            }
        } catch (Exception e) {
            System.out.println("getRequestItems" + e.getMessage());
        }
        return data;
    }

    public void add(Request request) {
        try {
            String strSQL = "INSERT INTO request (code, user_id, type, status, reason, "
                    + "supplier, address, phone, email) "
                    + "VALUES (?, ?, 'purchase', 'pending', ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, request.getId());
            stm.setInt(2, request.getUserId());
            stm.setString(3, request.getReason());
            stm.setString(4, request.getSupplier());
            stm.setString(5, request.getAddress());
            stm.setString(6, request.getPhone());
            stm.setString(7, request.getEmail());
            stm.execute();
        } catch (Exception e) {
            System.out.println("add: " + e.getMessage());
        }
    }

    public void addRequestItem(RequestItem item) {
        try {
            String strSQL = "INSERT INTO request_items (request_id, product_id, quantity, note) "
                    + "VALUES ((SELECT id FROM request WHERE code = ?), "
                    + "(SELECT id FROM product_info WHERE code = ?), ?, ?)";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, item.getRequestId());
            stm.setString(2, item.getProductCode());
            stm.setInt(3, item.getQuantity());
            stm.setString(4, item.getNote());
            stm.execute();
        } catch (Exception e) {
            System.out.println("addRequestItem: " + e.getMessage());
        }
    }

    public void update(Request request) {
        try {
            String strSQL = "UPDATE request SET status = ?, reason = ?, "
                    + "supplier = ?, address = ?, phone = ?, email = ? WHERE code = ?";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, request.getStatus());
            stm.setString(2, request.getReason());
            stm.setString(3, request.getSupplier());
            stm.setString(4, request.getAddress());
            stm.setString(5, request.getPhone());
            stm.setString(6, request.getEmail());
            stm.setString(7, request.getId());
            stm.execute();
        } catch (Exception e) {
            System.out.println("update: " + e.getMessage());
        }
    }

    public void delete(String id) {
        try {
            // Xóa các request_items trước
            String deleteItems = "DELETE FROM request_items WHERE request_id = "
                    + "(SELECT id FROM request WHERE code = ?)";
            stm = connection.prepareStatement(deleteItems);
            stm.setString(1, id);
            stm.executeUpdate();

            // Sau đó xóa request
            String deleteRequest = "DELETE FROM request WHERE code = ?";
            stm = connection.prepareStatement(deleteRequest);
            stm.setString(1, id);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println("delete: " + e.getMessage());
        }
    }

    public ArrayList<Request> getRequestsBySupplier(String supplierName) {
        ArrayList<Request> data = new ArrayList<>();
        try {
            String strSQL = "select r.* from request r where r.supplier like ?";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, "%" + supplierName + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);      // code
                int userId = rs.getInt(2);        // user_id
                Date dayRequest = rs.getDate(3);   // create_date
                String status = rs.getString(4);   // status
                String reason = rs.getString(5);   // reason
                String supplier = rs.getString(6);  // supplier
                String address = rs.getString(7);   // address
                String phone = rs.getString(8);     // phone
                String email = rs.getString(9);     // email

                Request request = new Request(id, userId, dayRequest, status, reason,
                        supplier, address, phone, email);
                data.add(request);
            }
        } catch (Exception e) {
            System.out.println("getRequestsBySupplier" + e.getMessage());
        }
        return data;
    }

}
