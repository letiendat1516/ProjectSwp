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
                overflow-x: auto;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 16px rgba(0,0,0,0.09);
                padding: 30px 24px;
                max-width: 1200px;
                margin: 0 auto;
            }

            .msg {
                text-align: center;
                margin-bottom: 14px;
                color: #156809;
            }

            .msg.err {
                color: #a21313;
            }

            .table-wrapper {
                overflow-x: auto;
            }

            table {
                width: 100%;
                min-width: 900px;
                border-collapse: collapse;
                margin-top: 24px;
            }

            th, td {
                padding: 9px 10px;
                border-bottom: 1px solid #ddd;
                text-align: left;
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
                font-size: 14px;
                margin: 0 2px;
            }

            .btn-approve {
                background: #28b21a;
            }

            .btn-reject {
                background: #e44844;
            }

            .small {
                font-size: 13px;
                color: #5c5c5c;
            }

            .status-pending {
                color: #ec9103;
                font-weight: bold;
            }

            .status-approved {
                color: #28b21a;
                font-weight: bold;
            }

            .status-rejected {
                color: #e44844;
                font-weight: bold;
            }
            table {
                width: 100%;
                table-layout: fixed; /* Chia đều chiều rộng các cột */
                border-collapse: collapse;
                margin-top: 24px;
            }

            th, td {
                padding: 9px 10px;
                border-bottom: 1px solid #ddd;
                text-align: left;
                word-wrap: break-word; /* Đảm bảo nội dung không bị tràn */
            }

        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp"/>
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
                    <div style="font-weight: bold; margin-bottom: 10px;">Số lượng: <%= (requests != null ? requests.size() : 0) %></div>
                    <div class="table-wrapper">
                        <table>
                            <thead>
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
                                    <td class="<%= statusClass %>"><%= req.getStatus() %></td>
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
                                <tr>
                                    <td colspan="8" style="text-align:center; color:#888;">Không có yêu cầu nào!</td>
                                </tr>
                                <% } %>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
