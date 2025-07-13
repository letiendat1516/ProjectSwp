package dao;

import DBContext.Context;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import model.PurchaseOrderInfo;
import model.PurchaseOrderItems;
import model.Request;
import model.RequestItem;

public class PurchaseOrderDAO {

    /**
     * Tạo Purchase Order mới với danh sách items Status mặc định là "pending"
     */
    public boolean insertPurchaseOrder(PurchaseOrderInfo purchaseOrderInfo) {
        if (purchaseOrderInfo == null || purchaseOrderInfo.getId() == null || purchaseOrderInfo.getId().trim().isEmpty()) {
            return false;
        }

        try (Connection con = Context.getJDBCConnection()) {
            con.setAutoCommit(false); // Bắt đầu transaction

            // BƯỚC 1: Insert thông tin chính vào bảng purchase_order_info
            String sqlOrder = "INSERT INTO purchase_order_info (id, fullname, DoB, day_purchase, status, reason, supplier, address, phone, email, summary) VALUES (?, ?, ?, ?, 'pending', ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmtOrder = con.prepareStatement(sqlOrder)) {
                pstmtOrder.setString(1, purchaseOrderInfo.getId());
                pstmtOrder.setString(2, purchaseOrderInfo.getFullname());

                // Chuyển đổi java.util.Date sang java.sql.Date
//                if (purchaseOrderInfo.getDoB() != null) {
//                    pstmtOrder.setDate(3, new java.sql.Date(purchaseOrderInfo.getDoB().getTime()));
//                } else {
//                    pstmtOrder.setNull(3, Types.DATE);
//                }
                if (purchaseOrderInfo.getDayPurchase() != null) {
                    pstmtOrder.setDate(4, new java.sql.Date(purchaseOrderInfo.getDayPurchase().getTime()));
                } else {
                    pstmtOrder.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Ngày hiện tại
                }

                pstmtOrder.setString(5, purchaseOrderInfo.getReason());
                pstmtOrder.setString(6, purchaseOrderInfo.getSupplier());
                pstmtOrder.setString(7, purchaseOrderInfo.getAddress());
                pstmtOrder.setString(8, purchaseOrderInfo.getPhone());
                pstmtOrder.setString(9, purchaseOrderInfo.getEmail());
                pstmtOrder.setString(10, purchaseOrderInfo.getSummary());

                int orderResult = pstmtOrder.executeUpdate();

                // BƯỚC 2: Insert items nếu có
                if (orderResult > 0 && purchaseOrderInfo.getPurchaseItems() != null
                        && !purchaseOrderInfo.getPurchaseItems().isEmpty()) {

                    String sqlItem = "INSERT INTO purchase_order_items (purchase_id, product_name, product_code, unit, quantity, price_per_unit, total_price, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement pstmtItem = con.prepareStatement(sqlItem)) {
                        // Sử dụng batch insert để tối ưu performance
                        for (PurchaseOrderItems item : purchaseOrderInfo.getPurchaseItems()) {
                            pstmtItem.setString(1, purchaseOrderInfo.getId());
                            pstmtItem.setString(2, item.getProductName());
                            pstmtItem.setString(3, item.getProductCode());
                            pstmtItem.setString(4, item.getUnit());
                            pstmtItem.setBigDecimal(5, item.getQuantity());
                            pstmtItem.setBigDecimal(6, item.getPricePerUnit());
                            pstmtItem.setBigDecimal(7, item.getTotalPrice());
                            pstmtItem.setString(8, item.getNote());
                            pstmtItem.addBatch();
                        }

                        int[] itemResults = pstmtItem.executeBatch();

                        // Kiểm tra tất cả items có insert thành công không
                        boolean allItemsSuccess = true;
                        for (int result : itemResults) {
                            if (result <= 0) {
                                allItemsSuccess = false;
                                break;
                            }
                        }

                        if (allItemsSuccess) {
                            con.commit(); // Commit transaction
                            return true;
                        } else {
                            con.rollback(); // Rollback nếu có lỗi
                            return false;
                        }
                    }
                } else if (orderResult > 0) {
                    con.commit(); // Commit nếu chỉ có order info
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy danh sách Request đã được duyệt để tạo Purchase Order Chỉ lấy các
     * request có status: approved, quoted, pending re-quote, completed
     */
    public List<Request> getAllPurchaseOrder(int index, String startDateStr, String endDateStr, String statusFilter, String requestIdFilter) {
        List<Request> allRequests = new ArrayList<>();

        // BƯỚC 1: Lấy danh sách request IDs với phân trang
        StringBuilder sqlRequestIds = new StringBuilder(
                "SELECT DISTINCT r.id "
                + "FROM request r "
                + "WHERE r.status IN ('approved', 'quoted', 'pending re-quote', 'completed') "
        );

        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Thêm các điều kiện filter
            if (startDateStr != null && !startDateStr.isEmpty()) {
                sqlRequestIds.append("AND r.day_request >= ? ");
                params.add(sdf.parse(startDateStr));
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                sqlRequestIds.append("AND r.day_request <= ? ");
                params.add(sdf.parse(endDateStr));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                if (statusFilter.equals("approved") || statusFilter.equals("completed")
                        || statusFilter.equals("quoted") || statusFilter.equals("pending re-quote")) {
                    sqlRequestIds.append("AND r.status = ? ");
                    params.add(statusFilter);
                }
            }
            if (requestIdFilter != null && !requestIdFilter.isEmpty()) {
                sqlRequestIds.append("AND r.id LIKE ? ");
                params.add("%" + requestIdFilter.toUpperCase() + "%");
            }

            // ✅ Phân trang trên DISTINCT request IDs
            sqlRequestIds.append("ORDER BY r.id LIMIT ? OFFSET ?");
            params.add(10);
            params.add((index - 1) * 10);

            // Lấy danh sách request IDs
            List<String> requestIds = new ArrayList<>();
            try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sqlRequestIds.toString())) {

                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        requestIds.add(rs.getString("id"));
                    }
                }
            }

            // BƯỚC 2: Lấy chi tiết cho từng request ID
            if (!requestIds.isEmpty()) {
                StringBuilder sqlDetails = new StringBuilder(
                        "SELECT r.id AS request_id, u.fullname, r.user_id, r.role, r.day_request, r.status, r.reason, r.supplier, "
                        + "r.address, r.phone, r.email, r.approve_by, r.warehouse, "
                        + "ri.id AS item_id, ri.product_name, ri.product_code, ri.unit, ri.quantity, ri.note, ri.reason_detail "
                        + "FROM request r "
                        + "JOIN users u ON r.user_id = CAST(u.id AS CHAR) "
                        + "LEFT JOIN request_items ri ON r.id = ri.request_id "
                        + "WHERE r.id IN ("
                );

                // Tạo placeholders cho IN clause
                for (int i = 0; i < requestIds.size(); i++) {
                    sqlDetails.append("?");
                    if (i < requestIds.size() - 1) {
                        sqlDetails.append(",");
                    }
                }
                sqlDetails.append(") ORDER BY r.id, ri.id");

                try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sqlDetails.toString())) {

                    // Gán request IDs vào prepared statement
                    for (int i = 0; i < requestIds.size(); i++) {
                        stmt.setString(i + 1, requestIds.get(i));
                    }

                    try (ResultSet rs = stmt.executeQuery()) {
                        Request currentRequest = null;
                        while (rs.next()) {
                            String requestId = rs.getString("request_id");

                            // Tạo Request object mới khi gặp request_id khác
                            if (currentRequest == null || !currentRequest.getId().equals(requestId)) {
                                currentRequest = new Request();
                                currentRequest.setId(requestId);
                                currentRequest.setFullname(rs.getString("fullname"));
                                //  currentRequest.setUser_id(rs.getInt("user_id"));
                                currentRequest.setRole(rs.getString("role"));
                                currentRequest.setDay_request(rs.getDate("day_request"));
                                currentRequest.setStatus(rs.getString("status"));
                                currentRequest.setReason(rs.getString("reason"));
//                                currentRequest.setSupplier(rs.getString("supplier"));
//                                currentRequest.setAddress(rs.getString("address"));
//                                currentRequest.setPhone(rs.getString("phone"));
//                                currentRequest.setEmail(rs.getString("email"));
//                                currentRequest.setApprove_by(rs.getString("approve_by"));
//                                currentRequest.setWarehouse(rs.getString("warehouse"));
                                currentRequest.setItems(new ArrayList<>());
                                allRequests.add(currentRequest);
                            }

                            // Thêm item vào request hiện tại
                            if (rs.getObject("item_id") != null) {
                                RequestItem item = new RequestItem();
                                item.setId(rs.getInt("item_id"));
                                item.setRequestId(requestId);
                                item.setProductName(rs.getString("product_name"));
                                item.setProductCode(rs.getString("product_code"));
                                item.setUnit(rs.getString("unit"));
                                item.setQuantity(rs.getDouble("quantity"));
                                item.setNote(rs.getString("note") != null ? rs.getString("note") : "");
//                                item.setReasonDetail(rs.getString("reason_detail") != null ? rs.getString("reason_detail") : "");
                                currentRequest.getItems().add(item);
                            }
                        }
                    }
                }
            }

            System.out.println("✅ Total requests loaded: " + allRequests.size());
            return allRequests;

        } catch (SQLException | ParseException e) {
            System.err.println("Error in getAllPurchaseOrder: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Đếm tổng số Request đã duyệt (để tính pagination)
     */
    public int getTotalFilteredPurchaseOrders(String startDateStr, String endDateStr, String statusFilter, String requestIdFilter) {
        StringBuilder sql = new StringBuilder(
                // Chỉ đếm các request đã được duyệt
                "SELECT COUNT(DISTINCT r.id) FROM request r WHERE r.status IN ('approved', 'quoted', 'pending re-quote', 'completed') "
        );

        List<Object> params = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Áp dụng cùng logic filter như getAllPurchaseOrder
            if (startDateStr != null && !startDateStr.isEmpty()) {
                sql.append("AND r.day_request >= ? ");
                params.add(sdf.parse(startDateStr));
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                sql.append("AND r.day_request <= ? ");
                params.add(sdf.parse(endDateStr));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                if (statusFilter.equals("approved") || statusFilter.equals("completed")
                        || statusFilter.equals("quoted") || statusFilter.equals("pending re-quote")) {
                    sql.append("AND r.status = ? ");
                    params.add(statusFilter);
                }
            }
            if (requestIdFilter != null && !requestIdFilter.isEmpty()) {
                sql.append("AND r.id LIKE ? ");
                params.add("%" + requestIdFilter.toUpperCase() + "%");
            }

            // Debug logging
            System.out.println("=== getTotalFilteredPurchaseOrders DEBUG ===");
            System.out.println("SQL: " + sql.toString());
            System.out.println("Params: " + params);

            try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql.toString())) {

                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("Total count: " + count);
                        return count;
                    }
                }
            }
        } catch (SQLException | ParseException e) {
            System.err.println("Error in getTotalFilteredPurchaseOrders: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Xóa Purchase Order và tất cả items liên quan
     */
    public boolean deletePurchaseOrder(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return false;
        }

        try (Connection con = Context.getJDBCConnection()) {
            con.setAutoCommit(false); // Bắt đầu transaction

            // Xóa items trước (foreign key constraint)
            String deleteItemsSql = "DELETE FROM purchase_order_items WHERE purchase_id = ?";
            try (PreparedStatement deleteItemsStmt = con.prepareStatement(deleteItemsSql)) {
                deleteItemsStmt.setString(1, orderId);
                deleteItemsStmt.executeUpdate();
            }

            // Xóa order chính
            String sql = "DELETE FROM purchase_order_info WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, orderId);
                int rowsAffected = stmt.executeUpdate();
                con.commit(); // Commit transaction
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái Purchase Order thành "approved"
     */
    public boolean updateApprovedStatus(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE purchase_order_info SET status = ? WHERE id = ?";
        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            stmt.setString(1, "approved");
            stmt.setString(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái Purchase Order thành "rejected"
     */
    public boolean updateRejectedStatus(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE purchase_order_info SET status = ? WHERE id = ?";
        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            stmt.setString(1, "rejected");
            stmt.setString(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật trạng thái Purchase Order thành "completed"
     */
    public boolean updateCompletedStatus(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE purchase_order_info SET status = ? WHERE id = ?";
        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            stmt.setString(1, "completed");
            stmt.setString(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            con.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy thông tin chi tiết Purchase Order theo ID
     */
    public PurchaseOrderInfo getPurchaseOrderById(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT p.id AS order_id, p.fullname, p.DoB, p.day_purchase, p.status, p.reason, p.supplier, "
                + "p.address, p.phone, p.email, p.summary, "
                + "pi.id AS item_id, pi.product_name, pi.product_code, pi.unit, pi.quantity, pi.price_per_unit, pi.total_price, pi.note "
                + "FROM purchase_order_info p "
                + "LEFT JOIN purchase_order_items pi ON p.id = pi.purchase_id "
                + "WHERE p.id = ? "
                + "ORDER BY pi.id";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                PurchaseOrderInfo order = null;
                while (rs.next()) {
                    // Tạo order object một lần
                    if (order == null) {
                        order = new PurchaseOrderInfo();
                        order.setId(rs.getString("order_id"));
                        order.setFullname(rs.getString("fullname"));
//                        order.setDoB(rs.getDate("DoB"));
                        order.setDayPurchase(rs.getDate("day_purchase"));
                        order.setStatus(rs.getString("status"));
                        order.setReason(rs.getString("reason"));
                        order.setSupplier(rs.getString("supplier"));
                        order.setAddress(rs.getString("address"));
                        order.setPhone(rs.getString("phone"));
                        order.setEmail(rs.getString("email"));
                        order.setSummary(rs.getString("summary"));
                        order.setPurchaseItems(new ArrayList<>());
                    }

                    // Thêm items vào order
                    if (rs.getObject("item_id") != null) {
                        PurchaseOrderItems item = new PurchaseOrderItems();
                        item.setId(rs.getInt("item_id"));
                        item.setPurchaseId(orderId);
                        item.setProductName(rs.getString("product_name"));
                        item.setProductCode(rs.getString("product_code"));
                        item.setUnit(rs.getString("unit"));
                        item.setQuantity(rs.getBigDecimal("quantity"));
                        item.setPricePerUnit(rs.getBigDecimal("price_per_unit"));
                        item.setTotalPrice(rs.getBigDecimal("total_price"));
                        item.setNote(rs.getString("note") != null ? rs.getString("note") : "");
                        order.getPurchaseItems().add(item);
                    }
                }
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
