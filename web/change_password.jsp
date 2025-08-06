<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users, model.ForgotPasswordRequest" %>

<%
    Users user = (Users) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    ForgotPasswordRequest req = (ForgotPasswordRequest) request.getAttribute("resetRequest");
    String msg = (String) request.getAttribute("msg");
    Boolean success = (Boolean) request.getAttribute("success");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Đổi mật khẩu</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f6f6f8;
                padding: 30px;
            }

            .custom-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background: #fff;
                border-radius: 0 0 7px 7px;
                padding: 20px 34px 16px 34px;
                box-shadow: 0 1px 5px rgba(0,0,0,0.07);
                margin-bottom: 28px;
            }
            .header-title {
                font-size: 1.8rem;
                color: #1567c1;
            }
            .header-right {
                display: flex;
                align-items: center;
                gap: 20px;
            }
            .admin-name {
                font-size: 1.13rem;
                color: #1b466a;
            }
            .logout-btn {
                background: #ff0202;
                color: #fff;
                border: none;
                padding: 8px 24px;
                border-radius: 7px;
                font-weight: 600;
                font-size: 1rem;
                text-decoration: none;
                transition: background 0.15s;
            }
            .logout-btn:hover {
                background: #c70000;
            }

            .form-container {
                background: #fff;
                max-width: 100%;
                margin: 36px auto;
                padding: 38px 34px 22px 34px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            }
            h2 {
                text-align: center;
                margin-bottom: 24px;
                color: #1567c1;
            }
            .form-group {
                margin-bottom: 18px;
            }
            label {
                display: block;
                font-weight: bold;
                margin-bottom: 6px;
            }
            input[type="password"] {
                width: 100%;
                padding: 10px;
                border-radius: 5px;
                border: 1px solid #ccc;
                font-size: 1rem;
            }
            .submit-btn {
                background: #3498db;
                color: #fff;
                padding: 10px 24px;
                border: none;
                border-radius: 6px;
                font-weight: bold;
                cursor: pointer;
                width: 100%;
                font-size: 1.05rem;
            }
            .submit-btn:hover {
                background: #217dbb;
            }
            .msg-success, .msg-error {
                padding: 12px 16px;
                border-radius: 5px;
                margin-bottom: 18px;
                font-weight: bold;
                text-align: center;
            }
            .msg-success {
                background: #d1f3d1;
                color: #278b2e;
                border: 1px solid #8fd08f;
            }
            .msg-error {
                background: #ffd2d2;
                color: #c0392b;
                border: 1px solid #e08b8b;
            }
            .back-link {
                display: block;
                margin-top: 20px;
                text-align: center;
                text-decoration: none;
                color: #3498db;
            }
            .layout-container {
                display: flex;
                min-height: 100vh;
            }
            .main-content {
                flex: 1;
                padding: 20px;
            }
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <div class="custom-header">
                    <h1 class="header-title">Đổi mật khẩu</h1>
                    <div class="header-right">
                        <span class="admin-name">${user.fullname}</span>
                        <a href="logout" class="logout-btn">Đăng xuất</a>
                    </div>
                </div>

                <div class="form-container">
                    <h2>Thay đổi mật khẩu</h2>

                    <% if (msg != null && !msg.trim().isEmpty()) { %>
                    <% if (msg.contains("thành công")) { %>
                    <div class="msg-success"><%= msg %></div>
                    <% } else { %>
                    <div class="msg-error"><%= msg %></div>
                    <% } %>
                    <% } else { %>
                    <% if (req == null) { %>
                    <div class="msg-error">Không tìm thấy yêu cầu đổi mật khẩu hợp lệ.</div>

                    <% } else if ("approved".equalsIgnoreCase(req.getStatus()) && (success == null || !success)) { %>
                    <form method="post" action="change-password">
                        <div class="form-group">
                            <label for="newPassword">Mật khẩu mới:</label>
                            <input type="password" id="newPassword" name="newPassword"
                                   required
                                   pattern="^[a-zA-Z0-9._%+-]{7,}$"
                                   title="Tối thiểu 7 kí tự, bao gồm cả chữ, số hoặc kí tự đặc biệt">
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
                            <input type="password" id="confirmPassword" name="confirmPassword"
                                   required
                                   pattern="^[a-zA-Z0-9._%+-]{7,}$"
                                   title="Tối thiểu 7 kí tự, bao gồm cả chữ, số hoặc kí tự đặc biệt">
                        </div>
                        <button type="submit" class="submit-btn">Cập nhật mật khẩu</button>
                    </form>

                    <% } else if ("rejected".equalsIgnoreCase(req.getStatus())) { %>
                    <div class="msg-error">Yêu cầu đổi mật khẩu của bạn đã bị từ chối.</div>

                    <% } else if ("used".equalsIgnoreCase(req.getStatus())) { %>
                    <div class="msg-error">Yêu cầu đổi mật khẩu này đã được sử dụng.</div>

                    <% } else { %>
                    <div class="msg-error">Chưa được quyền đổi mật khẩu.</div>
                    <% } %>
                    <% } %>

                    <a href="profile" class="back-link">← Quay lại</a>
                </div>
            </div>
        </div>
    </body>
</html>
