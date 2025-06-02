package dao;

import DBContext.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Request;
import model.RequestItem;

/**
 *
 * @author Dell
 */
public class ImportDAO {
        private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Request> getApprovedRequestsWithItems(int page, int pageSize, String searchKeyword, String sortField, String sortDir) {
        List<Request> requests = new ArrayList<>();
        Map<String, Request> requestMap = new HashMap<>();

        StringBuilder query = new StringBuilder(
                "SELECT r.id AS request_id, r.user_id, r.day_request, r.status, r.reason, "
                + "r.supplier, r.address, r.phone, r.email, "
                + "ri.id AS item_id, ri.product_name, ri.product_code, ri.unit, ri.quantity, ri.note, ri.reason_detail "
                + "FROM request r "
                + "LEFT JOIN request_items ri ON r.id = ri.request_id "
                + "WHERE r.status = 'approved'"
        );

        // Thêm điều kiện tìm kiếm
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            query.append(" AND (r.supplier LIKE ? OR r.email LIKE ?)");
        }

        // Sort field & direction
        String validSortField = "r.id";
        if ("day_request".equalsIgnoreCase(sortField)) {
            validSortField = "r.day_request";
        } else if ("supplier".equalsIgnoreCase(sortField)) {
            validSortField = "r.supplier";
        }

        String validSortDir = "ASC";
        if ("desc".equalsIgnoreCase(sortDir)) {
            validSortDir = "DESC";
        }

        query.append(" ORDER BY ").append(validSortField).append(" ").append(validSortDir);
        query.append(" LIMIT ?, ?");

        try {
            conn = new Context().getJDBCConnection();
            ps = conn.prepareStatement(query.toString());

            int paramIndex = 1;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
            }

            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                String requestId = rs.getString("request_id");

                Request req = requestMap.get(requestId);
                if (req == null) {
                    req = new Request();
                    req.setId(requestId);
                    req.setUser_id(rs.getInt("user_id"));
                    req.setDay_request(rs.getDate("day_request"));
                    req.setStatus(rs.getString("status"));
                    req.setReason(rs.getString("reason"));
                    req.setSupplier(rs.getString("supplier"));
                    req.setAddress(rs.getString("address"));
                    req.setPhone(rs.getString("phone"));
                    req.setEmail(rs.getString("email"));
                    req.setItems(new ArrayList<>());

                    requestMap.put(requestId, req);
                    requests.add(req);
                }

                int itemId = rs.getInt("item_id");
                if (itemId > 0) {
                    RequestItem item = new RequestItem();
                    item.setRequestId(requestId);
                    item.setProductName(rs.getString("product_name"));
                    item.setProductCode(rs.getString("product_code"));
                    item.setUnit(rs.getString("unit"));
                    item.setQuantity(rs.getInt("quantity")); // Sửa kiểu dữ liệu
                    item.setNote(rs.getString("note"));
                    item.setReasonDetail(rs.getString("reason_detail"));
                    req.getItems().add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return requests;
    }
}
