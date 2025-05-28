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
                String role = rs.getString("fullname"); // Lấy tên người dùng làm role
                Date dayRequest = rs.getDate(3);  // create_date
                String status = rs.getString(4);   // status
                String reason = rs.getString(5);   // reason
                String supplier = rs.getString(6);  // supplier
                String address = rs.getString(7);   // address
                String phone = rs.getString(8);     // phone
                String email = rs.getString(9);     // email

                Request request = new Request(id, userId, role, dayRequest, status, reason,
                        supplier, address, phone, email);
                data.add(request);
            }
        } catch (Exception e) {
            System.out.println("getRequests: " + e.getMessage());
        }
        return data;
    }

    public Request getRequestById(String id) {
        try {
            String strSQL = "select r.*, u.fullname from request r join users u on r.user_id = u.id where r.code=?";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, id);
            rs = stm.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt(2);        // user_id
                String role = rs.getString("fullname"); // Lấy tên người dùng làm role
                Date dayRequest = rs.getDate(3);  // create_date
                String status = rs.getString(4);   // status
                String reason = rs.getString(5);   // reason
                String supplier = rs.getString(6);  // supplier
                String address = rs.getString(7);   // address
                String phone = rs.getString(8);     // phone
                String email = rs.getString(9);     // email

                Request p = new Request(id, userId, role, dayRequest, status, reason,
                        supplier, address, phone, email);
                return p;
            }
        } catch (Exception e) {
            System.out.println("getRequestById: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<RequestItem> getRequestItems(String requestId) {
        ArrayList<RequestItem> data = new ArrayList<>();
        try {
            String strSQL = "select ri.*, p.name, p.code, u.name, ri.reason_detail from request_items ri "
                    + "join product_info p on ri.product_id = p.id "
                    + "join unit u on p.unit_id = u.id "
                    + "where ri.request_id = (select id from request where code = ?)";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, requestId);
            rs = stm.executeQuery();
            while (rs.next()) {
                String productName = rs.getString("p.name");
                String productCode = rs.getString("p.code");
                String unit = rs.getString("u.name");
                int quantity = rs.getInt("ri.quantity");
                String note = rs.getString("ri.note");
                String reasonDetail = rs.getString("ri.reason_detail");  // Thêm trường reason_detail

                RequestItem item = new RequestItem(requestId, productName, productCode,
                        unit, quantity, note, reasonDetail);
                data.add(item);
            }
        } catch (Exception e) {
            System.out.println("getRequestItems: " + e.getMessage());
        }
        return data;
    }

    public void add(Request request) {
        try {
            String strSQL = "INSERT INTO request (code, user_id, role, day_request, status, reason, "
                    + "supplier, address, phone, email) "
                    + "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?)";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, request.getId());
            stm.setInt(2, request.getUser_id());
            stm.setString(3, request.getRole());
            stm.setString(4, request.getStatus());
            stm.setString(5, request.getReason());
            stm.setString(6, request.getSupplier());
            stm.setString(7, request.getAddress());
            stm.setString(8, request.getPhone());
            stm.setString(9, request.getEmail());
            stm.execute();
        } catch (Exception e) {
            System.out.println("add: " + e.getMessage());
        }
    }

    public void addRequestItem(RequestItem item) {
        try {
            String strSQL = "INSERT INTO request_items (request_id, product_id, quantity, note, reason_detail) "
                    + "VALUES ((SELECT id FROM request WHERE code = ?), "
                    + "(SELECT id FROM product_info WHERE code = ?), ?, ?, ?)";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, item.getRequestId());
            stm.setString(2, item.getProductCode());
            stm.setInt(3, item.getQuantity());
            stm.setString(4, item.getNote());
            stm.setString(5, item.getReasonDetail());
            stm.execute();
        } catch (Exception e) {
            System.out.println("addRequestItem: " + e.getMessage());
        }
    }

    public void update(Request request) {
        try {
            String strSQL = "UPDATE request SET user_id = ?, role = ?, day_request = ?, status = ?, reason = ?, "
                    + "supplier = ?, address = ?, phone = ?, email = ? WHERE code = ?";
            stm = connection.prepareStatement(strSQL);
            stm.setInt(1, request.getUser_id());
            stm.setString(2, request.getRole());
            stm.setDate(3, new java.sql.Date(request.getDay_request().getTime()));
            stm.setString(4, request.getStatus());
            stm.setString(5, request.getReason());
            stm.setString(6, request.getSupplier());
            stm.setString(7, request.getAddress());
            stm.setString(8, request.getPhone());
            stm.setString(9, request.getEmail());
            stm.setString(10, request.getId());
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
            String strSQL = "select r.*, u.fullname from request r join users u on r.user_id = u.id where r.supplier like ?";
            stm = connection.prepareStatement(strSQL);
            stm.setString(1, "%" + supplierName + "%");
            rs = stm.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);      // code
                int userId = rs.getInt(2);        // user_id
                String role = rs.getString("fullname"); // Lấy tên người dùng làm role
                Date dayRequest = rs.getDate(3);   // create_date
                String status = rs.getString(4);   // status
                String reason = rs.getString(5);   // reason
                String supplier = rs.getString(6);  // supplier
                String address = rs.getString(7);   // address
                String phone = rs.getString(8);     // phone
                String email = rs.getString(9);     // email

                Request request = new Request(id, userId, role, dayRequest, status, reason,
                        supplier, address, phone, email);
                data.add(request);
            }
        } catch (Exception e) {
            System.out.println("getRequestsBySupplier: " + e.getMessage());
        }
        return data;
    }

}
