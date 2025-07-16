package dao;

import DBContext.Context;
import java.math.BigDecimal;
import model.PurchaseOrderInfo;
import model.PurchaseOrderItems;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderDAO {

    /**
     * Add approved request vào purchase order
     */
    public boolean addApprovedRequestToPurchaseOrder(String requestId) {
        Connection con = null;
        try {
            con = Context.getJDBCConnection();
            con.setAutoCommit(false);

            // Kiểm tra xem đã có trong purchase_order chưa
            String checkSql = "SELECT COUNT(*) as count FROM purchase_order_info WHERE id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, requestId);
            ResultSet checkRs = checkPs.executeQuery();
            checkRs.next();
            int exists = checkRs.getInt("count");

            if (exists > 0) {
                System.out.println("ℹ️ Request " + requestId + " đã có trong purchase order rồi");
                return true; // Đã có rồi thì coi như thành công
            }

            // 1. Add vào purchase_order_info
            String sql1 = "INSERT INTO purchase_order_info (id, fullname, day_purchase, status, reason) "
                    + "SELECT id, fullname, day_request, 'pending_quote', reason "
                    + "FROM request WHERE id = ? AND status = 'approved'";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setString(1, requestId);
            int rows1 = ps1.executeUpdate();

<<<<<<< HEAD
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
=======
            if (rows1 == 0) {
                System.out.println("❌ Không thể add request " + requestId + " vào purchase_order_info");
                con.rollback();
                return false;
            }

            // 2. Add vào purchase_order_items
            String sql2 = "INSERT INTO purchase_order_items (purchase_id, product_name, product_code, unit, quantity, note) "
                    + "SELECT request_id, product_name, product_code, unit, quantity, note "
                    + "FROM request_items WHERE request_id = ?";
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setString(1, requestId);
            int rows2 = ps2.executeUpdate();
>>>>>>> 31e5107d6d34587f671590d0382a74961088ae84

            con.commit();
            System.out.println("✅ Đã add request " + requestId + " vào purchase order (" + rows2 + " items)");
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Lỗi add vào purchase order: " + e.getMessage());
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Xem những request approved chưa được add vào purchase order
     */
<<<<<<< HEAD
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

=======
    public void showPendingRequests() {
>>>>>>> 31e5107d6d34587f671590d0382a74961088ae84
        try (Connection con = Context.getJDBCConnection()) {
            String sql = "SELECT id, fullname, day_request "
                    + "FROM request "
                    + "WHERE status = 'approved' "
                    + "AND id NOT IN (SELECT id FROM purchase_order_info)";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("📋 Request approved CHƯA được add:");
            int count = 0;
            while (rs.next()) {
                System.out.println("  - " + rs.getString("id")
                        + " | " + rs.getString("fullname")
                        + " | " + rs.getDate("day_request"));
                count++;
            }

            if (count == 0) {
                System.out.println("  (Không có)");
            }

        } catch (SQLException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }

    // ========== CÁC METHOD CŨ (GIỮ NGUYÊN) ==========
    /**
     * Lấy danh sách Purchase Orders với phân trang và filter
     */
    public List<PurchaseOrderInfo> getAllPurchaseOrders(int page, String startDate, String endDate, String status, String searchId) {
        List<PurchaseOrderInfo> purchaseOrders = new ArrayList<>();

        try {
            System.out.println("=== STEP 1: Get Purchase Order IDs ===");

            // Bước 1: Lấy danh sách ID với phân trang và filter
            List<String> purchaseOrderIds = getPurchaseOrderIds(page, startDate, endDate, status, searchId);

            if (purchaseOrderIds.isEmpty()) {
                System.out.println("No purchase order IDs found");
                return purchaseOrders;
            }

            System.out.println("Found " + purchaseOrderIds.size() + " purchase order IDs: " + purchaseOrderIds);

            // Bước 2: Lấy thông tin chi tiết cho từng purchase order
            System.out.println("=== STEP 2: Get Purchase Order Details ===");
            purchaseOrders = getPurchaseOrdersByIds(purchaseOrderIds);

            System.out.println("=== STEP 3: Get Purchase Order Items ===");
            // Bước 3: Lấy items cho từng purchase order
            for (PurchaseOrderInfo po : purchaseOrders) {
                List<PurchaseOrderItems> items = getPurchaseOrderItems(po.getId());
                po.setPurchaseItems(items);
                System.out.println("Purchase Order " + po.getId() + " has " + items.size() + " items");
            }

            System.out.println("=== COMPLETED: Found " + purchaseOrders.size() + " purchase orders ===");

        } catch (Exception e) {
            System.out.println("Error in getAllPurchaseOrders: " + e.getMessage());
            e.printStackTrace();
        }

        return purchaseOrders;
    }

    /**
     * Lấy danh sách ID của Purchase Orders với filter và phân trang
     */
    private List<String> getPurchaseOrderIds(int page, String startDate, String endDate, String status, String searchId) {
        List<String> ids = new ArrayList<>();

        StringBuilder sqlIds = new StringBuilder("SELECT DISTINCT po.id FROM purchase_order_info po WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Thêm các điều kiện filter
        if (startDate != null && !startDate.trim().isEmpty()) {
            sqlIds.append(" AND po.day_purchase >= ?");
            params.add(Date.valueOf(startDate));
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sqlIds.append(" AND po.day_purchase <= ?");
            params.add(Date.valueOf(endDate));
        }

        if (status != null && !status.trim().isEmpty()) {
            if ("approved".equals(status)) {
            // Khi chọn "Đã hoàn thành", lọc cả 3 status
            sqlIds.append(" AND po.status IN ('approved', 'completed', 'rejected')");
        } else {
            // Các status khác thì filter bình thường
            sqlIds.append(" AND po.status = ?");
            params.add(status);
        }
        }

        if (searchId != null && !searchId.trim().isEmpty()) {
            sqlIds.append(" AND po.id LIKE ?");
            params.add("%" + searchId + "%");
        }

        sqlIds.append(" ORDER BY po.id");

        // Thêm phân trang
        int limit = 10;
        int offset = (page - 1) * limit;
        sqlIds.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        System.out.println("SQL: " + sqlIds.toString());
        System.out.println("Params: " + params);

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sqlIds.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
<<<<<<< HEAD
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
=======
                    ids.add(rs.getString("id"));
>>>>>>> 31e5107d6d34587f671590d0382a74961088ae84
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting purchase order IDs: " + e.getMessage());
            e.printStackTrace();
        }

        return ids;
    }

    /**
     * Lấy thông tin chi tiết Purchase Orders theo danh sách IDs
     */
    private List<PurchaseOrderInfo> getPurchaseOrdersByIds(List<String> ids) {
        List<PurchaseOrderInfo> purchaseOrders = new ArrayList<>();

        if (ids.isEmpty()) {
            return purchaseOrders;
        }

        String placeholders = String.join(",", ids.stream().map(id -> "?").toArray(String[]::new));

        String sql = "SELECT po.id, po.fullname, po.day_purchase, po.day_quote, po.status, "
                + "po.reason, po.supplier, po.address, po.phone, po.email, po.summary "
                + "FROM purchase_order_info po "
                + "WHERE po.id IN (" + placeholders + ") "
                + "ORDER BY po.id";

        System.out.println("SQL for details: " + sql);
        System.out.println("IDs: " + ids);

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < ids.size(); i++) {
                ps.setString(i + 1, ids.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderInfo po = new PurchaseOrderInfo();
                    po.setId(rs.getString("id"));
                    po.setFullname(rs.getString("fullname"));
                    po.setDayPurchase(rs.getDate("day_purchase"));
                    po.setDayQuote(rs.getDate("day_quote"));
                    po.setStatus(rs.getString("status"));
                    po.setReason(rs.getString("reason"));
                    po.setSupplier(rs.getString("supplier"));
                    po.setAddress(rs.getString("address"));
                    po.setPhone(rs.getString("phone"));
                    po.setEmail(rs.getString("email"));
                    po.setSummary(rs.getString("summary"));

                    purchaseOrders.add(po);
                    System.out.println("Loaded purchase order: " + po.getId());
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting purchase order details: " + e.getMessage());
            e.printStackTrace();
        }

        return purchaseOrders;
    }

    /**
     * Lấy danh sách items của một Purchase Order
     */
    private List<PurchaseOrderItems> getPurchaseOrderItems(String purchaseOrderId) {
        List<PurchaseOrderItems> items = new ArrayList<>();

        String sql = "SELECT id, purchase_id, product_name, product_code, unit, quantity, "
                + "price_per_unit, total_price, note "
                + "FROM purchase_order_items "
                + "WHERE purchase_id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, purchaseOrderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PurchaseOrderItems item = new PurchaseOrderItems();
                    item.setId(rs.getInt("id"));
                    item.setPurchaseId(rs.getString("purchase_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductCode(rs.getString("product_code"));
                    item.setUnit(rs.getString("unit"));
                    item.setQuantity(rs.getBigDecimal("quantity"));
                    item.setPricePerUnit(rs.getBigDecimal("price_per_unit"));
                    item.setTotalPrice(rs.getBigDecimal("total_price"));
                    item.setNote(rs.getString("note"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting purchase order items for " + purchaseOrderId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return items;
    }

    /**
     * Lấy Purchase Order theo ID
     */
    public PurchaseOrderInfo getPurchaseOrderById(String id) {
        PurchaseOrderInfo purchaseOrder = null;

        String sql = "SELECT po.id, po.fullname, po.day_purchase, po.day_quote, po.status, "
                + "po.reason, po.supplier, po.address, po.phone, po.email, po.summary "
                + "FROM purchase_order_info po "
                + "WHERE po.id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    purchaseOrder = new PurchaseOrderInfo();
                    purchaseOrder.setId(rs.getString("id"));
                    purchaseOrder.setFullname(rs.getString("fullname"));
                    purchaseOrder.setDayPurchase(rs.getDate("day_purchase"));
                    purchaseOrder.setDayQuote(rs.getDate("day_quote"));
                    purchaseOrder.setStatus(rs.getString("status"));
                    purchaseOrder.setReason(rs.getString("reason"));
                    purchaseOrder.setSupplier(rs.getString("supplier"));
                    purchaseOrder.setAddress(rs.getString("address"));
                    purchaseOrder.setPhone(rs.getString("phone"));
                    purchaseOrder.setEmail(rs.getString("email"));
                    purchaseOrder.setSummary(rs.getString("summary"));

                    List<PurchaseOrderItems> items = getPurchaseOrderItems(id);
                    purchaseOrder.setPurchaseItems(items);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting purchase order by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return purchaseOrder;
    }

    /**
     * Đếm tổng số Purchase Orders với filter
     */
    public int getTotalFilteredPurchaseOrders(String startDate, String endDate, String status, String searchId) {
        int count = 0;

        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT po.id) FROM purchase_order_info po WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND po.day_purchase >= ?");
            params.add(Date.valueOf(startDate));
        }

        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND po.day_purchase <= ?");
            params.add(Date.valueOf(endDate));
        }

        if (status != null && !status.trim().isEmpty()) {
            if ("completed_group".equals(status)) {
                // Khi filter "completed_group", bao gồm cả 3 status
                sql.append(" AND po.status IN ('approved', 'completed', 'rejected')");
            } else {
                // Các status khác thì filter bình thường
                sql.append(" AND po.status = ?");
                params.add(status);
            }
        }

        if (searchId != null && !searchId.trim().isEmpty()) {
            sql.append(" AND po.id LIKE ?");
            params.add("%" + searchId + "%");
        }

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error counting purchase orders: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Lấy tất cả status có sẵn
     */
    public List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();

        String sql = "SELECT DISTINCT status FROM purchase_order_info WHERE status IS NOT NULL ORDER BY status";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                statuses.add(rs.getString("status"));
            }

        } catch (SQLException e) {
            System.out.println("Error getting statuses: " + e.getMessage());
            e.printStackTrace();
        }

        return statuses;
    }

    /**
     * Cập nhật thông tin cơ bản của purchase order
     */
    public boolean updatePurchaseOrderInfo(String purchaseOrderId, Date quoteDate,
            String supplier, String address, String phone,
            String email, String summary) {
        String sql = "UPDATE purchase_order_info SET "
                + "day_quote = ?, supplier = ?, address = ?, phone = ?, email = ?, summary = ? "
                + "WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, quoteDate != null ? new java.sql.Date(quoteDate.getTime()) : null);
            ps.setString(2, supplier);
            ps.setString(3, address);
            ps.setString(4, phone);
            ps.setString(5, email);
            ps.setString(6, summary);
            ps.setString(7, purchaseOrderId);

            int rowsAffected = ps.executeUpdate();
            System.out.println("✅ Updated purchase_order_info for ID: " + purchaseOrderId + " (" + rowsAffected + " rows)");
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updating purchase_order_info: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật giá cho một item cụ thể
     */
    public boolean updatePurchaseOrderItem(String purchaseOrderId, String productCode,
            BigDecimal pricePerUnit, BigDecimal totalPrice, String note) {
        String sql = "UPDATE purchase_order_items SET "
                + "price_per_unit = ?, total_price = ?, note = ? "
                + "WHERE purchase_id = ? AND product_code = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, pricePerUnit);
            ps.setBigDecimal(2, totalPrice);
            ps.setString(3, note);
            ps.setString(4, purchaseOrderId);
            ps.setString(5, productCode);

            int rowsAffected = ps.executeUpdate();
            System.out.println("✅ Updated item: " + productCode + " for PO: " + purchaseOrderId + " (" + rowsAffected + " rows)");
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updating purchase_order_item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật status của purchase order
     */
    public boolean updatePurchaseOrderStatus(String purchaseOrderId, String status) {
        String sql = "UPDATE purchase_order_info SET status = ? WHERE id = ?";

        try (Connection con = Context.getJDBCConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, purchaseOrderId);

            int rowsAffected = ps.executeUpdate();
            System.out.println("✅ Updated status to '" + status + "' for PO: " + purchaseOrderId + " (" + rowsAffected + " rows)");
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("❌ Error updating purchase_order status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
