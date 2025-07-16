package dao;

import DBContext.Context;
import model.ExportRequest;
import model.ExportRequestItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ExportDAO {

  /**
   * Lấy thông tin export request theo ID
   */
  public ExportRequest getExportRequestById(String requestId) {
      ExportRequest request = null;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT * FROM export_request WHERE id = ? AND status IN ('approved', 'partial_exported')";
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, requestId);
          rs = ps.executeQuery();

          if (rs.next()) {
              request = mapResultSetToExportRequest(rs);
          }

      } catch (Exception e) {
          System.err.println("Error getting export request by ID: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return request;
  }

  /**
   * Lấy thông tin export request theo ID (bất kể status)
   */
  public ExportRequest getExportRequestByIdAnyStatus(String requestId) {
      ExportRequest request = null;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT * FROM export_request WHERE id = ?";
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, requestId);
          rs = ps.executeQuery();

          if (rs.next()) {
              request = mapResultSetToExportRequest(rs);
          }

      } catch (Exception e) {
          System.err.println("Error getting export request by ID (any status): " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return request;
  }

  /**
   * Kiểm tra export request có thể xử lý không
   */
  public boolean isExportRequestProcessable(String requestId) {
      boolean processable = false;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT COUNT(*) FROM export_request WHERE id = ? AND status IN ('approved', 'partial_exported')";
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, requestId);
          rs = ps.executeQuery();

          if (rs.next()) {
              processable = rs.getInt(1) > 0;
          }

      } catch (Exception e) {
          System.err.println("Error checking if export request is processable: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return processable;
  }

  /**
   * Lấy danh sách items của export request với thông tin xuất kho
   */
  public List<ExportRequestItem> getExportRequestItemsByRequestId(String requestId) {
      List<ExportRequestItem> items = new ArrayList<>();
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          // Khởi tạo export_pending_items nếu chưa có
          initializeExportPendingItems(requestId);

          String sql = "SELECT " +
                  "epi.id, epi.export_request_id, epi.product_name, epi.product_code, " +
                  "epi.unit, epi.unit_id, epi.quantity_requested, epi.quantity_exported, " +
                  "epi.quantity_pending, epi.note, epi.product_id " +
                  "FROM export_pending_items epi " +
                  "WHERE epi.export_request_id = ? " +
                  "ORDER BY epi.product_name ASC";

          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, requestId);
          rs = ps.executeQuery();

          while (rs.next()) {
              ExportRequestItem item = mapResultSetToExportRequestItem(rs);
              items.add(item);
          }

      } catch (Exception e) {
          System.err.println("Error getting export request items: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return items;
  }

  /**
   * Lấy lịch sử xuất kho của một request
   */
  public List<Object[]> getExportHistory(String requestId) {
      List<Object[]> history = new ArrayList<>();
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT product_name, product_code, quantity_exported, " +
                  "DATE_FORMAT(export_date, '%d/%m/%Y %H:%i') as formatted_date, " +
                  "exported_by, note " +
                  "FROM warehouse_export_history " +
                  "WHERE export_request_id = ? " +
                  "ORDER BY export_date DESC, product_name ASC";

          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, requestId);
          rs = ps.executeQuery();

          while (rs.next()) {
              Object[] record = new Object[6];
              record[0] = rs.getString("product_name");
              record[1] = rs.getString("product_code");
              record[2] = rs.getBigDecimal("quantity_exported");
              record[3] = rs.getString("formatted_date");
              record[4] = rs.getString("exported_by");
              record[5] = rs.getString("note");
              history.add(record);
          }

      } catch (Exception e) {
          System.err.println("Error getting export history: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return history;
  }

  /**
   * Xử lý xuất kho từng phần
   */
  public boolean processPartialExport(String requestId, String exportDate, String processor, 
                                    String additionalNote, List<ExportRequestItem> exportItems) {
      Connection conn = null;
      PreparedStatement psUpdate = null;
      PreparedStatement psHistory = null;
      PreparedStatement psStatus = null;
      PreparedStatement psCheck = null;
      ResultSet rs = null;

      try {
          conn = Context.getJDBCConnection();
          conn.setAutoCommit(false);

          System.out.println("=== PROCESSING PARTIAL EXPORT ===");
          System.out.println("Request ID: " + requestId);
          System.out.println("Export Date: " + exportDate);
          System.out.println("Processor: " + processor);
          System.out.println("Items to export: " + exportItems.size());

          // 1. Cập nhật export_pending_items
          String updateSql = "UPDATE export_pending_items SET " +
                  "quantity_exported = quantity_exported + ?, " +
                  "quantity_pending = quantity_pending - ? " +
                  "WHERE export_request_id = ? AND product_code = ? " +
                  "AND quantity_pending >= ?";

          psUpdate = conn.prepareStatement(updateSql);

          // 2. Thêm vào warehouse_export_history
          String historySql = "INSERT INTO warehouse_export_history " +
                  "(export_request_id, product_name, product_code, quantity_exported, " +
                  "export_date, note, exported_by) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?)";

          psHistory = conn.prepareStatement(historySql);

          // Xử lý từng item
          for (ExportRequestItem item : exportItems) {
              System.out.println("Processing item: " + item.getProductName() + 
                               ", Code: " + item.getProductCode() + 
                               ", Quantity: " + item.getQuantity());

              // Validate dữ liệu
              if (item.getProductCode() == null || item.getProductCode().trim().isEmpty()) {
                  throw new SQLException("Product code is null or empty for " + item.getProductName());
              }

              if (item.getQuantity() <= 0) {
                  throw new SQLException("Invalid quantity for " + item.getProductName());
              }

              // Cập nhật export_pending_items
              psUpdate.setBigDecimal(1, BigDecimal.valueOf(item.getQuantity()));
              psUpdate.setBigDecimal(2, BigDecimal.valueOf(item.getQuantity()));
              psUpdate.setString(3, requestId);
              psUpdate.setString(4, item.getProductCode());
              psUpdate.setBigDecimal(5, BigDecimal.valueOf(item.getQuantity()));

              int updateResult = psUpdate.executeUpdate();
              System.out.println("Update result for " + item.getProductName() + ": " + updateResult);

              if (updateResult == 0) {
                  throw new SQLException("Failed to update pending items for " + item.getProductName() + 
                                       " (Code: " + item.getProductCode() + ")");
              }

              // Thêm vào lịch sử
              psHistory.setString(1, requestId);
              psHistory.setString(2, item.getProductName());
              psHistory.setString(3, item.getProductCode());
              psHistory.setBigDecimal(4, BigDecimal.valueOf(item.getQuantity()));
              psHistory.setDate(5, Date.valueOf(exportDate));
              psHistory.setString(6, additionalNote);
              psHistory.setString(7, processor);

              int historyResult = psHistory.executeUpdate();
              System.out.println("History insert result for " + item.getProductName() + ": " + historyResult);

              if (historyResult == 0) {
                  throw new SQLException("Failed to insert export history for " + item.getProductName());
              }
          }

          // 3. Kiểm tra và cập nhật trạng thái request
          String checkSql = "SELECT COUNT(*) as pending_count FROM export_pending_items " +
                  "WHERE export_request_id = ? AND quantity_pending > 0";

          psCheck = conn.prepareStatement(checkSql);
          psCheck.setString(1, requestId);
          rs = psCheck.executeQuery();

          String newStatus = "partial_exported";
          if (rs.next() && rs.getInt("pending_count") == 0) {
              newStatus = "completed";
          }

          System.out.println("New status: " + newStatus);

          // Cập nhật trạng thái
          String statusSql = "UPDATE export_request SET status = ? WHERE id = ?";
          psStatus = conn.prepareStatement(statusSql);
          psStatus.setString(1, newStatus);
          psStatus.setString(2, requestId);

          int statusResult = psStatus.executeUpdate();
          System.out.println("Status update result: " + statusResult);

          if (statusResult == 0) {
              throw new SQLException("Failed to update export request status");
          }

          conn.commit();
          System.out.println("=== EXPORT COMPLETED SUCCESSFULLY ===");
          return true;

      } catch (Exception e) {
          System.err.println("Error processing partial export: " + e.getMessage());
          e.printStackTrace();
          try {
              if (conn != null) {
                  conn.rollback();
                  System.out.println("Transaction rolled back");
              }
          } catch (SQLException rollbackEx) {
              System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
          }
          return false;
      } finally {
          try {
              if (conn != null) {
                  conn.setAutoCommit(true);
              }
          } catch (SQLException e) {
              System.err.println("Error resetting auto commit: " + e.getMessage());
          }
          closeResources(conn, psUpdate, rs);
          closePreparedStatement(psHistory);
          closePreparedStatement(psStatus);
          closePreparedStatement(psCheck);
      }
  }

  /**
   * Cập nhật trạng thái export request thành rejected
   */
  public boolean updateExportRequestStatusToRejected(String requestId, String rejectReason) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
          String sql = "UPDATE export_request SET status = 'rejected', reject_reason = ? WHERE id = ?";
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, rejectReason);
          ps.setString(2, requestId);

          int result = ps.executeUpdate();
          return result > 0;

      } catch (Exception e) {
          System.err.println("Error updating export request status to rejected: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, null);
      }

      return false;
  }

  /**
   * Khởi tạo export_pending_items từ export_request_items
   */
  private boolean initializeExportPendingItems(String requestId) {
      Connection conn = null;
      PreparedStatement psCheck = null;
      PreparedStatement psInsert = null;
      ResultSet rs = null;

      try {
          conn = Context.getJDBCConnection();

          // Kiểm tra xem đã có pending items chưa
          String checkSql = "SELECT COUNT(*) FROM export_pending_items WHERE export_request_id = ?";
          psCheck = conn.prepareStatement(checkSql);
          psCheck.setString(1, requestId);
          rs = psCheck.executeQuery();

          if (rs.next() && rs.getInt(1) == 0) {
              // Chưa có, tạo mới từ export_request_items
              String insertSql = "INSERT INTO export_pending_items " +
                      "(export_request_id, product_name, product_code, unit, unit_id, " +
                      "quantity_requested, quantity_exported, quantity_pending, note, product_id) " +
                      "SELECT export_request_id, product_name, product_code, unit, unit_id, " +
                      "quantity, 0, quantity, note, product_id " +
                      "FROM export_request_items WHERE export_request_id = ?";

              psInsert = conn.prepareStatement(insertSql);
              psInsert.setString(1, requestId);
              int result = psInsert.executeUpdate();

              System.out.println("Initialized " + result + " pending items for request: " + requestId);
              return result > 0;
          }

          return true; // Đã có rồi

      } catch (Exception e) {
          System.err.println("Error initializing export pending items: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, psCheck, rs);
          closePreparedStatement(psInsert);
      }

      return false;
  }

  /**
   * Map ResultSet to ExportRequest
   */
  private ExportRequest mapResultSetToExportRequest(ResultSet rs) throws SQLException {
      ExportRequest request = new ExportRequest();
      
      request.setId(rs.getString("id"));
      request.setUserId(rs.getInt("user_id"));
      request.setDayRequest(rs.getDate("day_request"));
      request.setStatus(rs.getString("status"));
      request.setApproveBy(rs.getString("approve_by"));
      request.setRole(rs.getString("role"));
      request.setReason(rs.getString("reason"));
      request.setRejectReason(rs.getString("reject_reason"));
      request.setCreatedAt(rs.getTimestamp("created_at"));

      return request;
  }

  /**
   * Map ResultSet to ExportRequestItem
   */
  private ExportRequestItem mapResultSetToExportRequestItem(ResultSet rs) throws SQLException {
      ExportRequestItem item = new ExportRequestItem();

      item.setId(rs.getInt("id"));
      item.setExportRequestId(rs.getString("export_request_id"));
      item.setProductName(rs.getString("product_name"));
      item.setProductCode(rs.getString("product_code"));
      item.setUnit(rs.getString("unit"));
      item.setUnitId(rs.getInt("unit_id"));
      
      BigDecimal quantityRequested = rs.getBigDecimal("quantity_requested");
      BigDecimal quantityExported = rs.getBigDecimal("quantity_exported");
      BigDecimal quantityPending = rs.getBigDecimal("quantity_pending");

      item.setQuantityRequested(quantityRequested != null ? quantityRequested.doubleValue() : 0.0);
      item.setQuantityExported(quantityExported != null ? quantityExported.doubleValue() : 0.0);
      item.setQuantityPending(quantityPending != null ? quantityPending.doubleValue() : 0.0);
      
      // Set cho compatibility
      item.setQuantity(item.getQuantityRequested());
      item.setExportedQty(item.getQuantityExported());

      item.setNote(rs.getString("note"));
      item.setProductId(rs.getInt("product_id"));

      return item;
  }

  /**
   * Đóng resources an toàn
   */
  private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
      try {
          if (rs != null) rs.close();
      } catch (SQLException e) {
          System.err.println("Error closing ResultSet: " + e.getMessage());
      }

      try {
          if (ps != null) ps.close();
      } catch (SQLException e) {
          System.err.println("Error closing PreparedStatement: " + e.getMessage());
      }

      try {
          if (conn != null) conn.close();
      } catch (SQLException e) {
          System.err.println("Error closing Connection: " + e.getMessage());
      }
  }

  /**
   * Đóng PreparedStatement an toàn
   */
  private void closePreparedStatement(PreparedStatement ps) {
      try {
          if (ps != null) ps.close();
      } catch (SQLException e) {
          System.err.println("Error closing PreparedStatement: " + e.getMessage());
      }
  }

  // ===== ADDITIONAL UTILITY METHODS =====

  /**
   * Kiểm tra tồn kho trước khi xuất
   */
  public boolean checkInventoryAvailability(String productCode, double requestedQuantity) {
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT SUM(quantity) as available_quantity FROM inventory " +
                  "WHERE product_code = ? AND quantity > 0";
          
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, productCode);
          rs = ps.executeQuery();

          if (rs.next()) {
              double availableQuantity = rs.getDouble("available_quantity");
              return availableQuantity >= requestedQuantity;
          }

      } catch (Exception e) {
          System.err.println("Error checking inventory availability: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return false;
  }

  /**
   * Lấy danh sách export requests theo status
   */
  public List<ExportRequest> getExportRequestsByStatus(String status) {
      List<ExportRequest> requests = new ArrayList<>();
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT * FROM export_request WHERE status = ? ORDER BY created_at DESC";
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, status);
          rs = ps.executeQuery();

          while (rs.next()) {
              ExportRequest request = mapResultSetToExportRequest(rs);
              requests.add(request);
          }

      } catch (Exception e) {
          System.err.println("Error getting export requests by status: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return requests;
  }

  /**
   * Cập nhật ghi chú cho export request
   */
  public boolean updateExportRequestNote(String requestId, String note) {
      Connection conn = null;
      PreparedStatement ps = null;

      try {
          String sql = "UPDATE export_request SET reason = ? WHERE id = ?";
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, note);
          ps.setString(2, requestId);

          int result = ps.executeUpdate();
          return result > 0;

      } catch (Exception e) {
          System.err.println("Error updating export request note: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, null);
      }

      return false;
  }

  /**
   * Lấy tổng số lượng đã xuất của một sản phẩm trong một request
   */
  public double getTotalExportedQuantity(String requestId, String productCode) {
      double totalExported = 0.0;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {
          String sql = "SELECT SUM(quantity_exported) as total_exported " +
                  "FROM warehouse_export_history " +
                  "WHERE export_request_id = ? AND product_code = ?";
          
          conn = Context.getJDBCConnection();
          ps = conn.prepareStatement(sql);
          ps.setString(1, requestId);
          ps.setString(2, productCode);
          rs = ps.executeQuery();

          if (rs.next()) {
              BigDecimal total = rs.getBigDecimal("total_exported");
              totalExported = total != null ? total.doubleValue() : 0.0;
          }

      } catch (Exception e) {
          System.err.println("Error getting total exported quantity: " + e.getMessage());
          e.printStackTrace();
      } finally {
          closeResources(conn, ps, rs);
      }

      return totalExported;
  }

  /**
   * Xóa export request và các items liên quan (chỉ khi status = pending)
   */
  public boolean deleteExportRequest(String requestId) {
      Connection conn = null;
      PreparedStatement psCheck = null;
      PreparedStatement psDeleteItems = null;
      PreparedStatement psDeletePending = null;
      PreparedStatement psDeleteRequest = null;
      ResultSet rs = null;

      try {
          conn = Context.getJDBCConnection();
          conn.setAutoCommit(false);

          // Kiểm tra status
          String checkSql = "SELECT status FROM export_request WHERE id = ?";
          psCheck = conn.prepareStatement(checkSql);
          psCheck.setString(1, requestId);
          rs = psCheck.executeQuery();

          if (rs.next()) {
              String status = rs.getString("status");
              if (!"pending".equals(status)) {
                  System.out.println("Cannot delete export request with status: " + status);
                  return false;
              }
          } else {
              System.out.println("Export request not found: " + requestId);
              return false;
          }

          // Xóa export_request_items
          String deleteItemsSql = "DELETE FROM export_request_items WHERE export_request_id = ?";
          psDeleteItems = conn.prepareStatement(deleteItemsSql);
          psDeleteItems.setString(1, requestId);
          psDeleteItems.executeUpdate();

          // Xóa export_pending_items
          String deletePendingSql = "DELETE FROM export_pending_items WHERE export_request_id = ?";
          psDeletePending = conn.prepareStatement(deletePendingSql);
          psDeletePending.setString(1, requestId);
          psDeletePending.executeUpdate();

          // Xóa export_request
          String deleteRequestSql = "DELETE FROM export_request WHERE id = ?";
          psDeleteRequest = conn.prepareStatement(deleteRequestSql);
          psDeleteRequest.setString(1, requestId);
          int result = psDeleteRequest.executeUpdate();

          conn.commit();
          return result > 0;

      } catch (Exception e) {
          System.err.println("Error deleting export request: " + e.getMessage());
          e.printStackTrace();
          try {
              if (conn != null) conn.rollback();
          } catch (SQLException rollbackEx) {
              System.err.println("Error rolling back deletion: " + rollbackEx.getMessage());
          }
      } finally {
          try {
              if (conn != null) conn.setAutoCommit(true);
          } catch (SQLException e) {
              System.err.println("Error resetting auto commit: " + e.getMessage());
          }
          closeResources(conn, psCheck, rs);
          closePreparedStatement(psDeleteItems);
          closePreparedStatement(psDeletePending);
          closePreparedStatement(psDeleteRequest);
      }

      return false;
  }
}