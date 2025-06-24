<%-- 
    Document   : password_request
    Created on : 10 thg 6, 2025, 14:33:01
    Author     : phucn
--%>

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
%>

<%
    String msg = (String) request.getAttribute("msg");
    List<ForgotPasswordRequest> requests = (List<ForgotPasswordRequest>) request.getAttribute("requests");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Yêu cầu reset mật khẩu</title>
        <style>
            body {
                background: #eef2f7;
                font-family: Arial, sans-serif;
            }
            .container {
                width: 980px;
                margin: 30px auto;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 16px rgba(0,0,0,0.09);
                padding: 30px 24px;
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
                min-height: 60px;
            }
            .admin-navbar .left {
            }
            .admin-navbar .right {
                display: flex;
                align-items: center;
                gap: 18px;
            }
            .back-btn {
                background: #1976d2;
                color: #fff;
                font-weight: bold;
                border: none;
                border-radius: 8px;
                padding: 9px 22px;
                font-size: 16px;
                cursor: pointer;
                text-decoration: none;
                transition: background 0.2s;
                display: inline-block;
            }
            .back-btn:hover {
                background: #125a9c;
            }
            .page-title {
                font-size: 30px;
                font-weight: bold;
                color: #1976d2;
                letter-spacing: 1px;
            }
            .admin-name {
                margin-right: 6px;
                font-size: 20px;
                font-weight:bold;
                color: #222;
            }
            .logout-btn {
                background: #f60000;
                color: #fff;
                font-weight: bold;
                border: none;
                border-radius: 8px;
                padding: 9px 22px;
                font-size: 16px;
                cursor: pointer;
                text-decoration: none;
                transition: background 0.2s;
                display: inline-block;
            }
            .logout-btn:hover {
                background: #c10000;
            }
            h2 {
                text-align: center;
                color: #19568d;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 24px;
            }
            th, td {
                padding: 9px 10px;
                border-bottom: 1px solid #ddd;
            }
            th {
                background: #dbeafe;
            }
            .btn-approve, .btn-reject {
                padding: 5px 14px;
                border: none;
                border-radius: 7px;
                color: #fff;
                cursor: pointer;
                margin: 0 2px;
                font-size: 14px;
            }
            .btn-approve {
                background: #28b21a;
            }
            .btn-reject {
                background: #e44844;
            }
            .msg {
                text-align: center;
                margin-bottom: 14px;
                color: #156809;
            }
            .msg.err {
                color: #a21313;
            }
            .small {
                font-size: 13px;
                color: #5c5c5c;
            }
            .status-pending {
                color:#ec9103;
                font-weight:bold;
            }
            .status-approved {
                color:#28b21a;
                font-weight:bold;
            }
            .status-rejected {
                color:#e44844;
                font-weight:bold;
            }
        </style>
    </head>
    <body>
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
            <div class="msg<%= (msg != null && msg.contains("lỗi")) ? " err" : "" %>">
                <%= (msg != null) ? msg : "" %>
            </div>
            <table>
                Số lượng: <%= (requests != null ? requests.size() : "null") %>

                <tr>
                    <th>STT</th>
                    <th>Tên người dùng</th>
                    <th>Email</th>
                    <th>Thời gian gửi yêu cầu</th>
                    <th>Lý do</th>
                    <th>Thời gian phản hồi</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                <%
                    int stt = 1;
                    if (requests != null && !requests.isEmpty()) {
                        for (ForgotPasswordRequest req : requests) {
                            String statusClass = "status-pending";
                            if ("approved".equalsIgnoreCase(req.getStatus())) statusClass = "status-approved";
                            else if ("rejected".equalsIgnoreCase(req.getStatus())) statusClass = "status-rejected";
                %>
                <tr>
                    <td><%= stt++ %></td>
                    <td><%= req.getUsername() %></td>
                    <td><%= req.getEmail() %></td>
                    <td><%= (req.getRequestTime() != null ? req.getRequestTime().toString() : "") %></td>
                    <td><%= req.getNote() %></td>
                    <td><%= (req.getResponseTime() != null ? req.getResponseTime().toString() : "<span class='small'>Chưa phản hồi</span>") %></td>
                    <td class="<%=statusClass%>"><%= req.getStatus() %></td>
                    <td>
                        <% if (req.getResponseTime() == null) { %>
                        <form action="passwordrequest" method="post" style="display:inline">
                            <input type="hidden" name="reqId" value="<%= req.getId() %>">
                            <button class="btn-approve" name="action" value="approve" type="submit">Duyệt</button>
                        </form>
                        <form action="passwordrequest" method="post" style="display:inline">
                            <input type="hidden" name="reqId" value="<%= req.getId() %>">
                            <button class="btn-reject" name="action" value="reject" type="submit">Từ chối</button>
                        </form>
                        <% } else { %>
                        <span class="small">Đã phản hồi</span>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="8" style="text-align:center;color:#888;">Không có yêu cầu nào!</td></tr>
                <% } %>
            </table>
        </div>
    </body>
</html>




