package dao;

import DBContext.Context;
import model.ExportRequest;
import model.ExportRequestItem;
import java.sql.*;
import java.util.*;

public class GetExportRequestWithItemsDAO {
  
  /**
   * Lấy danh sách export requests với filter và phân trang
   */
  public List<ExportRequest> getFilteredExportRequests(java.sql.Date startDate, java.sql.Date endDate,
                                                     String status, String role,
                                                     int page, int pageSize,
                                                     String requestIdFilter) {
    List<ExportRequest> requests = new ArrayList<>();

    try (Connection conn = Context.getJDBCConnection()) {
        if (conn == null) {
            System.err.println("Cannot get database connection!");
            return requests;
        }

        // BƯỚC 1: Lấy danh sách export request IDs với filter (có join với users)
        String sql = buildExportRequestQuery(startDate, endDate, status, role, requestIdFilter, true);
        System.out.println("=== STEP 1: Get Export Request IDs ===");
        System.out.println("SQL: " + sql);

        PreparedStatement ps = conn.prepareStatement(sql);
        int paramIndex = setQueryParameters(ps, startDate, endDate, status, role, requestIdFilter, 1);

        // Set pagination
        ps.setInt(paramIndex++, pageSize);
        ps.setInt(paramIndex, (page - 1) * pageSize);

        ResultSet rs = ps.executeQuery();
        List<String> requestIds = new ArrayList<>();

        while (rs.next()) {
            requestIds.add(rs.getString("id"));
        }
        rs.close();
        ps.close();

        if (requestIds.isEmpty()) {
            return requests;
        }

        // BƯỚC 2: Get export request details
        String detailSql = "SELECT er.*, COALESCE(u.fullname, u.username, 'Unknown User') AS requester_name " +
                "FROM export_request er " +
                "LEFT JOIN users u ON er.user_id = u.id " +
                "WHERE er.id IN (" +
                String.join(",", Collections.nCopies(requestIds.size(), "?")) +
                ") ORDER BY er.day_request DESC, er.id DESC";

        PreparedStatement detailPs = conn.prepareStatement(detailSql);
        for (int i = 0; i < requestIds.size(); i++) {
            detailPs.setString(i + 1, requestIds.get(i));
        }

        ResultSet detailRs = detailPs.executeQuery();
        Map<String, ExportRequest> requestMap = new HashMap<>();

        while (detailRs.next()) {
            ExportRequest request = new ExportRequest();
            String requestId = detailRs.getString("id");

            request.setId(requestId);
            request.setUserId(detailRs.getInt("user_id"));
            request.setRequesterName(detailRs.getString("requester_name"));
            request.setStatus(detailRs.getString("status"));
            request.setReason(detailRs.getString("reason"));
            request.setDayRequest(detailRs.getDate("day_request"));
            request.setRole(detailRs.getString("role"));

            try { request.setDepartment(detailRs.getString("department")); } catch (SQLException e) {}
            try { request.setRecipientName(detailRs.getString("recipient_name")); } catch (SQLException e) {}
            try { request.setRecipientPhone(detailRs.getString("recipient_phone")); } catch (SQLException e) {}
            try { request.setRecipientEmail(detailRs.getString("recipient_email")); } catch (SQLException e) {}
            try { request.setApproveBy(detailRs.getString("approve_by")); } catch (SQLException e) {}
            try { request.setWarehouse(detailRs.getString("warehouse")); } catch (SQLException e) {}

            request.setItems(new ArrayList<>());
            requestMap.put(requestId, request);
            requests.add(request);
        }
        detailRs.close();
        detailPs.close();

        // BƯỚC 3: Lấy items
        String itemsSql = "SELECT * FROM export_request_items WHERE export_request_id IN (" +
                String.join(",", Collections.nCopies(requestIds.size(), "?")) + ")";
        PreparedStatement itemsPs = conn.prepareStatement(itemsSql);
        for (int i = 0; i < requestIds.size(); i++) {
            itemsPs.setString(i + 1, requestIds.get(i));
        }

        ResultSet itemsRs = itemsPs.executeQuery();
        while (itemsRs.next()) {
            String reqId = itemsRs.getString("export_request_id");
            ExportRequest req = requestMap.get(reqId);
            if (req != null) {
                ExportRequestItem item = new ExportRequestItem();
                item.setExportRequestId(reqId);
                item.setProductCode(itemsRs.getString("product_code"));
                item.setProductName(itemsRs.getString("product_name"));
                item.setQuantity(itemsRs.getDouble("quantity"));
                item.setUnit(itemsRs.getString("unit"));
                item.setNote(itemsRs.getString("note"));

                try { item.setId(itemsRs.getInt("id")); } catch (SQLException e) {}
                try { item.setReasonDetail(itemsRs.getString("reason_detail")); } catch (SQLException e) {}
                try { item.setProductId(itemsRs.getInt("product_id")); } catch (SQLException e) {}
                try { item.setExportedQty(itemsRs.getDouble("exported_qty")); } catch (SQLException e) {}

                req.getItems().add(item);
            }
        }
        itemsRs.close();
        itemsPs.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return requests;
}

  
  /**
   * Đếm tổng số export requests với filter (có join với users)
   */
public int getTotalFilteredExportRequests(java.sql.Date startDate, java.sql.Date endDate,
                                          String status, String role,
                                          String requestIdFilter) {
    try (Connection conn = Context.getJDBCConnection()) {
        if (conn == null) return 0;

        String sql = "SELECT COUNT(DISTINCT er.id) AS total " +
                "FROM export_request er " +
                "LEFT JOIN users u ON er.user_id = u.id " +
                "WHERE 1=1";

        if (startDate != null) sql += " AND er.day_request >= ?";
        if (endDate != null) sql += " AND er.day_request <= ?";
        if (status != null && !status.trim().isEmpty()) {
            if ("approved".equals(status)) {
                sql += " AND er.status NOT IN ('pending', 'rejected')";
            } else {
                sql += " AND er.status = ?";
            }
        }
        if (role != null && !role.trim().isEmpty()) {
            sql += " AND er.role = ?";
        }
        if (requestIdFilter != null && !requestIdFilter.trim().isEmpty()) {
            sql += " AND er.id LIKE ?";
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        setQueryParameters(ps, startDate, endDate, status, role, requestIdFilter, 1);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("total");

    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}


  
  /**
 * Xây dựng câu query cho export requests với xử lý trạng thái approved
 */
private String buildExportRequestQuery(java.sql.Date startDate, java.sql.Date endDate, String status, String role,
                                       String requestIdFilter, boolean withPagination) {
    String sql = "SELECT DISTINCT er.id, er.day_request " +
            "FROM export_request er " +
            "LEFT JOIN users u ON er.user_id = u.id " +
            "WHERE 1=1";

    if (startDate != null) sql += " AND er.day_request >= ?";
    if (endDate != null) sql += " AND er.day_request <= ?";
    if (status != null && !status.trim().isEmpty()) {
        if ("approved".equals(status)) {
            sql += " AND er.status NOT IN ('pending', 'rejected')";
        } else {
            sql += " AND er.status = ?";
        }
    }
    if (role != null && !role.trim().isEmpty()) sql += " AND er.role = ?";
    if (requestIdFilter != null && !requestIdFilter.trim().isEmpty()) sql += " AND er.id LIKE ?";

    sql += " ORDER BY er.day_request DESC, er.id DESC";

    if (withPagination) sql += " LIMIT ? OFFSET ?";

    return sql;
}


/**
 * Set parameters cho PreparedStatement với xử lý trạng thái approved
 */
private int setQueryParameters(PreparedStatement ps, java.sql.Date startDate, java.sql.Date endDate,
                               String status, String role, String requestIdFilter, int startIndex) throws SQLException {
    int paramIndex = startIndex;

    if (startDate != null) ps.setDate(paramIndex++, startDate);
    if (endDate != null) ps.setDate(paramIndex++, endDate);
    if (status != null && !status.trim().isEmpty() && !"approved".equals(status)) {
        ps.setString(paramIndex++, status);
    }
    if (role != null && !role.trim().isEmpty()) ps.setString(paramIndex++, role);
    if (requestIdFilter != null && !requestIdFilter.trim().isEmpty()) {
        ps.setString(paramIndex++, "%" + requestIdFilter.trim() + "%");
    }

    return paramIndex;
}


  /**
 * Cập nhật trạng thái export request và thông tin người phê duyệt
 */
public boolean updateExportRequestStatus(String requestId, String status, int approverUserId) {
    String sql = "UPDATE export_request er " +
                "JOIN users u ON u.id = ? " +
                "JOIN user_role ur ON ur.user_id = u.id " +
                "JOIN role r ON r.id = ur.role_id " +
                "SET er.status = ?, " +
                "    er.approve_by = u.fullname, " +
                "    er.role = r.role_name " +
                "WHERE er.id = ?";
    
    try (Connection conn = Context.getJDBCConnection()) {
        if (conn == null) {
            System.err.println("Cannot get database connection!");
            return false;
        }
        
        System.out.println("=== UPDATE EXPORT REQUEST STATUS ===");
        System.out.println("SQL: " + sql);
        System.out.println("Params: [" + approverUserId + ", " + status + ", " + requestId + "]");
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, approverUserId);      // User ID của người phê duyệt
        ps.setString(2, status);           // Status mới (approved/rejected)
        ps.setString(3, requestId);        // ID của export request
        
        int rowsAffected = ps.executeUpdate();
        ps.close();
        
        System.out.println("Rows affected: " + rowsAffected);
        
        if (rowsAffected > 0) {
            // Verify update
            verifyUpdate(conn, requestId, status);
            return true;
        }
        
    } catch (Exception e) {
        System.err.println("Error updating export request status: " + e.getMessage());
        e.printStackTrace();
    }
    
    return false;
}

/**
 * Verify update - kiểm tra xem update có thành công không
 */
private void verifyUpdate(Connection conn, String requestId, String expectedStatus) {
    String verifySql = "SELECT er.status, er.approve_by, er.role " +
                      "FROM export_request er WHERE er.id = ?";
try (PreparedStatement ps = conn.prepareStatement(verifySql)) {
        ps.setString(1, requestId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            System.out.println("=== VERIFY UPDATE RESULT ===");
            System.out.println("Request ID: " + requestId);
            System.out.println("Status: " + rs.getString("status"));
            System.out.println("Approved by: " + rs.getString("approve_by"));
            System.out.println("Role: " + rs.getString("role"));
            
            if (!expectedStatus.equals(rs.getString("status"))) {
                System.err.println("⚠️ WARNING: Expected status '" + expectedStatus + 
                                 "' but got '" + rs.getString("status") + "'");
            } else {
                System.out.println("✅ Status update verified successfully!");
            }
        }
        rs.close();
    } catch (Exception e) {
        System.err.println("Error verifying update: " + e.getMessage());
    }
}

/**
 * Lấy thông tin người dùng và role (để test)
 */
public void getUserAndRoleInfo(int userId) {
    String sql = "SELECT u.id, u.fullname, u.username, r.role_name " +
                "FROM users u " +
                "JOIN user_role ur ON ur.user_id = u.id " +
                "JOIN role r ON r.id = ur.role_id " +
                "WHERE u.id = ?";
    
    try (Connection conn = Context.getJDBCConnection()) {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        
        System.out.println("=== USER AND ROLE INFO ===");
        while (rs.next()) {
            System.out.println("User ID: " + rs.getInt("id"));
            System.out.println("Fullname: " + rs.getString("fullname"));
            System.out.println("Username: " + rs.getString("username"));
            System.out.println("Role: " + rs.getString("role_name"));
            System.out.println("---");
        }
        rs.close();
        ps.close();
    } catch (Exception e) {
        System.err.println("Error getting user info: " + e.getMessage());
    }
}

}