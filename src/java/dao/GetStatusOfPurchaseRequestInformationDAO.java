package dao;

import DBContext.Context;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.Request;
import model.RequestItem;

public class GetStatusOfPurchaseRequestInformationDAO {

    public List<Request> getAllPurchaseRequests(int index, String startDateStr, String endDateStr, String statusFilter, String requestIdFilter) {
        List<Request> allRequests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.id AS request_id, u.fullname, r.user_id, r.role, r.day_request, r.status, r.reason, r.supplier, " +
            "r.address, r.phone, r.email, r.approve_by, r.warehouse, " +
            "ri.id AS item_id, ri.product_name, ri.product_code, ri.unit, ri.quantity, ri.note, ri.reason_detail " +
            "FROM request r " +
            "JOIN users u ON r.user_id = CAST(u.id AS CHAR) " +
            "LEFT JOIN request_items ri ON r.id = ri.request_id " +
            "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Thêm điều kiện lọc
            if (startDateStr != null && !startDateStr.isEmpty()) {
                sql.append("AND r.day_request >= ? ");
                params.add(sdf.parse(startDateStr));
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                sql.append("AND r.day_request <= ? ");
                params.add(sdf.parse(endDateStr));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                sql.append("AND r.status = ? ");
                params.add(statusFilter);
            }
            if (requestIdFilter != null && !requestIdFilter.isEmpty()) {
                sql.append("AND r.id LIKE ? ");
                params.add("%" + requestIdFilter.toUpperCase() + "%");
            }

            // Thêm phân trang
            sql.append("ORDER BY r.id LIMIT ? OFFSET ?");
            params.add(11); // 10 yêu cầu mỗi trang
            params.add((index - 1) * 11);

            try (Connection con = Context.getJDBCConnection();
                 PreparedStatement stmt = con.prepareStatement(sql.toString())) {
                // Gán các tham số
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    Request currentRequest = null;
                    while (rs.next()) {
                        String requestId = rs.getString("request_id");
                        if (currentRequest == null || !currentRequest.getId().equals(requestId)) {
                            currentRequest = new Request();
                            currentRequest.setId(requestId);
                            currentRequest.setFullname(rs.getString("fullname"));
                            currentRequest.setUser_id(rs.getInt("user_id"));
                            currentRequest.setRole(rs.getString("role"));
                            currentRequest.setDay_request(rs.getDate("day_request"));
                            currentRequest.setStatus(rs.getString("status"));
                            currentRequest.setReason(rs.getString("reason"));
                            currentRequest.setSupplier(rs.getString("supplier"));
                            currentRequest.setAddress(rs.getString("address"));
                            currentRequest.setPhone(rs.getString("phone"));
                            currentRequest.setEmail(rs.getString("email"));
                            currentRequest.setApprove_by(rs.getString("approve_by"));
                            currentRequest.setWarehouse(rs.getString("warehouse"));
                            currentRequest.setItems(new ArrayList<>());
                            allRequests.add(currentRequest);
                        }
                        if (rs.getObject("item_id") != null) {
                            RequestItem item = new RequestItem();
                            item.setId(rs.getInt("item_id"));
                            item.setRequestId(requestId);
                            item.setProductName(rs.getString("product_name"));
                            item.setProductCode(rs.getString("product_code"));
                            item.setUnit(rs.getString("unit"));
                            item.setQuantity(rs.getDouble("quantity"));
                            item.setNote(rs.getString("note") != null ? rs.getString("note") : "");
                            item.setReasonDetail(rs.getString("reason_detail") != null ? rs.getString("reason_detail") : "");
                            currentRequest.getItems().add(item);
                        }
                    }
                    System.out.println("getAllPurchaseRequests: index=" + index + ", requests returned=" + allRequests.size());
                }
            }
        } catch (SQLException | ParseException e) {
            System.err.println("Error in getAllPurchaseRequests: " + e.getMessage());
            e.printStackTrace();
        }
        return allRequests.isEmpty() ? new ArrayList<>() : allRequests;
    }

    public int getTotalFilteredRequests(String startDateStr, String endDateStr, String statusFilter, String requestIdFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT r.id) FROM request r WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                sql.append("AND r.day_request >= ? ");
                params.add(sdf.parse(startDateStr));
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                sql.append("AND r.day_request <= ? ");
                params.add(sdf.parse(endDateStr));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                sql.append("AND r.status = ? ");
                params.add(statusFilter);
            }
            if (requestIdFilter != null && !requestIdFilter.isEmpty()) {
                sql.append("AND r.id LIKE ? ");
                params.add("%" + requestIdFilter.toUpperCase() + "%");
            }

            try (Connection con = Context.getJDBCConnection();
                 PreparedStatement stmt = con.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            System.err.println("Error in getTotalFilteredRequests: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteRequest(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) {
            return false;
        }

        try (Connection con = Context.getJDBCConnection()) {
            con.setAutoCommit(false);

            String deleteItemsSql = "DELETE FROM request_items WHERE request_id = ?";
            try (PreparedStatement deleteItemsStmt = con.prepareStatement(deleteItemsSql)) {
                deleteItemsStmt.setString(1, requestId);
                deleteItemsStmt.executeUpdate();
            }

            String sql = "DELETE FROM request WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, requestId);
                int rowsAffected = stmt.executeUpdate();
                con.commit();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateApprovedStatus(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE request SET status = ? WHERE id = ?";
        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            stmt.setString(1, "approved");
            stmt.setString(2, requestId);
            int rowsAffected = stmt.executeUpdate();
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRejectedStatus(String requestId) {
        if (requestId == null || requestId.trim().isEmpty()) {
            System.err.println("Request ID is null or empty");
            return false;
        }

        String sql = "UPDATE request SET status = ? WHERE id = ?";
        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            stmt.setString(1, "rejected");
            stmt.setString(2, requestId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("updateRejectedStatus: requestId=" + requestId + ", rowsAffected=" + rowsAffected);
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQLException in updateRejectedStatus: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        GetStatusOfPurchaseRequestInformationDAO dao = new GetStatusOfPurchaseRequestInformationDAO();
        int index = 1;
        List<Request> list = dao.getAllPurchaseRequests(index, null, null, null, null);
        if (list != null && !list.isEmpty()) {
            System.out.println("Danh sách yêu cầu (Trang " + index + ", 10 bản ghi):");
            for (Request req : list) {
                System.out.println("Request ID: " + req.getId());
                System.out.println("Fullname: " + req.getFullname());
                System.out.println("Status: " + req.getStatus());
                System.out.println("Items: ");
                for (RequestItem item : req.getItems()) {
                    System.out.println("  - Product: " + item.getProductName() + ", Quantity: " + item.getQuantity());
                }
                System.out.println("-------------------");
            }
        } else {
            System.out.println("Không có yêu cầu nào được tìm thấy trên trang " + index + ".");
        }
    }
}