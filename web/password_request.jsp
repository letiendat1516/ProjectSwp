<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ForgotPasswordRequest" %>
<%@ page import="model.Users" %>
<%
  Users user = (Users) session.getAttribute("user");
  if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
      response.sendRedirect("login.jsp");
      return;
  }
  String msg = (String) request.getAttribute("msg");
  List<ForgotPasswordRequest> requests = (List<ForgotPasswordRequest>) request.getAttribute("requests");
%>
<!DOCTYPE html>
<html>
  <head>
      <title>Yêu cầu reset mật khẩu</title>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <style>
          body {
              background: #eef2f7;
              font-family: Arial, sans-serif;
              margin: 0;
              padding: 0;
          }

          .layout-container {
              display: flex;
              min-height: 100vh;
          }

          .main-content {
              flex: 1;
              padding: 20px;
              background: #f5f5f5;
          }

          .admin-navbar {
              display: flex;
              justify-content: space-between;
              align-items: center;
              background: #fff;
              padding: 18px 32px;
              border-radius: 9px;
              box-shadow: 0 2px 10px rgba(0,0,0,0.06);
              margin-bottom: 28px;
              flex-wrap: wrap;
          }

          .page-title {
              font-size: 28px;
              font-weight: bold;
              color: #1976d2;
          }

          .admin-name {
              font-size: 20px;
              font-weight: bold;
              color: #222;
              margin-right: 10px;
          }

          .back-btn, .logout-btn {
              padding: 9px 22px;
              border: none;
              border-radius: 8px;
              font-size: 16px;
              font-weight: bold;
              cursor: pointer;
              text-decoration: none;
              transition: background 0.2s;
              display: inline-block;
          }

          .back-btn {
              background: #1976d2;
              color: #fff;
          }

          .back-btn:hover {
              background: #125a9c;
          }

          .logout-btn {
              background: #f60000;
              color: #fff;
          }

          .logout-btn:hover {
              background: #c10000;
          }

          .container {
              width: 100%;
              background: #fff;
              border-radius: 10px;
              box-shadow: 0 2px 16px rgba(0,0,0,0.09);
              padding: 30px 24px;
              max-width: 1200px;
              margin: 0 auto;
          }

          .msg {
              text-align: center;
              margin-bottom: 20px;
              padding: 10px;
              border-radius: 5px;
              color: #156809;
              background: #d4edda;
              border: 1px solid #c3e6cb;
          }

          .msg.err {
              color: #a21313;
              background: #f8d7da;
              border: 1px solid #f5c6cb;
          }

          .count-info {
              font-weight: bold;
              margin-bottom: 20px;
              font-size: 16px;
              color: #333;
          }

          .table-wrapper {
              overflow-x: auto;
          }

          table {
              width: 100%;
              min-width: 900px;
              border-collapse: collapse;
              margin-top: 10px;
          }

          th, td {
              padding: 12px 10px;
              border-bottom: 1px solid #ddd;
              text-align: left;
              word-wrap: break-word;
              vertical-align: middle;
          }

          th {
              background: #dbeafe;
              font-weight: bold;
              color: #1976d2;
              position: sticky;
              top: 0;
              z-index: 10;
          }

          tr:hover {
              background: #f8f9fa;
          }

          .btn-approve, .btn-reject {
              padding: 6px 12px;
              border: none;
              border-radius: 5px;
              color: #fff;
              cursor: pointer;
              font-size: 13px;
              margin: 2px;
              transition: background 0.2s;
          }

          .btn-approve {
              background: #28a745;
          }

          .btn-approve:hover {
              background: #218838;
          }

          .btn-reject {
              background: #dc3545;
          }

          .btn-reject:hover {
              background: #c82333;
          }

          .small {
              font-size: 13px;
              color: #6c757d;
              font-style: italic;
          }

          .status-pending {
              color: #ffc107;
              font-weight: bold;
              background: #fff3cd;
              padding: 4px 8px;
              border-radius: 4px;
              border: 1px solid #ffecb5;
          }

          .status-approved {
              color: #28a745;
              font-weight: bold;
              background: #d4edda;
              padding: 4px 8px;
              border-radius: 4px;
              border: 1px solid #c3e6cb;
          }

          .status-rejected {
              color: #dc3545;
              font-weight: bold;
              background: #f8d7da;
              padding: 4px 8px;
              border-radius: 4px;
              border: 1px solid #f5c6cb;
          }

          .action-buttons {
              white-space: nowrap;
          }

          .no-data {
              text-align: center;
              color: #6c757d;
              font-style: italic;
              padding: 40px;
          }

          /* Responsive */
          @media (max-width: 768px) {
              .admin-navbar {
                  flex-direction: column;
                  gap: 10px;
                  text-align: center;
              }

              .page-title {
                  font-size: 24px;
              }

              .container {
                  padding: 20px 15px;
              }

              table {
                  font-size: 14px;
              }

              th, td {
                  padding: 8px 6px;
              }
          }
      </style>
  </head>
  <body>
      <div class="layout-container">
          <jsp:include page="/include/sidebar.jsp" />
          <div class="main-content">
              <div class="admin-navbar">
                  <div class="left">
                      <a href="Admin.jsp" class="back-btn">← Quay lại trang chính</a>
                  </div>
                  <div class="center">
                      <span class="page-title">Yêu cầu reset mật khẩu</span>
                  </div>
                  <div class="right">
                      <span class="admin-name">Admin</span>
                      <a href="logout" class="logout-btn">Đăng xuất</a>
                  </div>
              </div>

              <div class="container">
                  <% if (msg != null && !msg.trim().isEmpty()) { %>
                  <div class="msg<%= (msg.contains("lỗi") || msg.contains("thất bại")) ? " err" : "" %>">
                      <%= msg %>
                  </div>
                  <% } %>

                  <div class="count-info">
                      Số lượng yêu cầu: <%= (requests != null ? requests.size() : 0) %>
                  </div>

                  <div class="table-wrapper">
                      <table>
                          <thead>
                              <tr>
                                  <th style="width: 60px;">STT</th>
                                  <th style="width: 150px;">Tên người dùng</th>
                                  <th style="width: 200px;">Email</th>
                                  <th style="width: 150px;">Thời gian gửi</th>
                                  <th style="width: 200px;">Lý do</th>
                                  <th style="width: 150px;">Thời gian phản hồi</th>
                                  <th style="width: 120px;">Trạng thái</th>
                                  <th style="width: 150px;">Hành động</th>
                              </tr>
                          </thead>
                          <tbody>
                              <%
                                  if (requests != null && !requests.isEmpty()) {
                                      int stt = 1;
                                      for (ForgotPasswordRequest req : requests) {
                                          String statusClass = "status-pending";
                                          String statusText = "Đang chờ";
                                          
                                          if ("approved".equalsIgnoreCase(req.getStatus())) {
                                              statusClass = "status-approved";
                                              statusText = "Đã duyệt";
                                          } else if ("rejected".equalsIgnoreCase(req.getStatus())) {
                                              statusClass = "status-rejected";
                                              statusText = "Đã từ chối";
                                          }
                              %>
                              <tr>
                                  <td><%= stt++ %></td>
                                  <td><%= req.getUsername() != null ? req.getUsername() : "N/A" %></td>
                                  <td><%= req.getEmail() != null ? req.getEmail() : "N/A" %></td>
                                  <td>
                                      <%= req.getRequestTime() != null ? 
                                          new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(req.getRequestTime()) : 
                                          "N/A" %>
                                  </td>
                                  <td><%= req.getNote() != null ? req.getNote() : "Không có ghi chú" %></td>
                                  <td>
                                      <%= req.getResponseTime() != null ? 
                                          new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(req.getResponseTime()) : 
                                          "<span class='small'>Chưa phản hồi</span>" %>
                                  </td>
                                  <td>
                                      <span class="<%= statusClass %>"><%= statusText %></span>
                                  </td>
                                  <td class="action-buttons">
                                      <% if (req.getResponseTime() == null) { %>
                                      <form action="passwordrequest" method="post" style="display:inline-block;">
                                          <input type="hidden" name="reqId" value="<%= req.getId() %>">
                                          <button class="btn-approve" name="action" value="approve" type="submit" 
                                                  onclick="return confirm('Bạn có chắc chắn muốn duyệt yêu cầu này?')">
                                              Duyệt
                                          </button>
                                      </form>
                                      <form action="passwordrequest" method="post" style="display:inline-block;">
                                          <input type="hidden" name="reqId" value="<%= req.getId() %>">
                                          <button class="btn-reject" name="action" value="reject" type="submit"
                                                  onclick="return confirm('Bạn có chắc chắn muốn từ chối yêu cầu này?')">
                                              Từ chối
                                          </button>
                                      </form>
                                      <% } else { %>
                                      <span class="small">Đã xử lý</span>
                                      <% } %>
                                  </td>
                              </tr>
                              <%
                                      }
                                  } else {
                              %>
                              <tr>
                                  <td colspan="8" class="no-data">
                                      Hiện tại không có yêu cầu reset mật khẩu nào!
                                  </td>
                              </tr>
                              <% } %>
                          </tbody>
                      </table>
                  </div>
              </div>
          </div>
      </div>
  </body>
</html>