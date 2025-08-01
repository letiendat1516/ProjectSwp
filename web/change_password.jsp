<%-- 
    Document   : reset_user_password
    Created on : 6 thg 7, 2025, 10:12:12
    Author     : phucn
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.ForgotPasswordRequest" %>
<%@ page import="model.Users" %>

<%
    ForgotPasswordRequest req = (ForgotPasswordRequest) request.getAttribute("resetRequest");
    Users targetUser = (Users) request.getAttribute("targetUser");
    String msg = (String) request.getAttribute("msg");
    Boolean success = (Boolean) request.getAttribute("success");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Đổi mật khẩu</title>

    </head>
    <body>
        <div class="pw-reset-container">
            <h2>Đổi mật khẩu</h2>

            <% if (msg != null && !msg.trim().isEmpty()) { %>
            <div class="<%= msg.contains("thành công") ? "msg-success" : "msg-error" %>">
                <%= msg %>
            </div>
            <% } %>

            <% if (req == null) { %>
            <div class="msg-error">Bạn chưa có yêu cầu đổi mật khẩu!</div>

            <% } else if ("approved".equalsIgnoreCase(req.getStatus()) && (success == null || !success)) { %>
            <div class="info-row"><span class="info-label">Tên người dùng:</span> <%= targetUser.getFullname() %></div>
            <div class="info-row"><span class="info-label">Email:</span> <%= targetUser.getEmail() %></div>
            <div class="info-row"><span class="info-label">Lý do yêu cầu:</span> <%= req.getNote() != null ? req.getNote() : "(Không có ghi chú)" %></div>

            <form method="post" action="change-password">
                <div class="form-group">
                    <label>Mật khẩu mới:</label>
                    <input type="password" name="newPassword" required minlength="7">
                </div>
                <div class="form-group">
                    <label>Nhập lại mật khẩu mới:</label>
                    <input type="password" name="confirmPassword" required minlength="7">
                </div>
                <button type="submit">Đổi mật khẩu</button>
            </form>

            <% } else if ("rejected".equalsIgnoreCase(req.getStatus())) { %>
            <div class="msg-error">Yêu cầu đổi mật khẩu của bạn đã bị từ chối.</div>

            <% } else if ("used".equalsIgnoreCase(req.getStatus())) { %>
            <div class="msg-error">Yêu cầu đã được sử dụng. Vui lòng gửi yêu cầu mới nếu cần đổi lại mật khẩu.</div>

            <% } else { %>
            <div class="msg-error">Trạng thái yêu cầu không hợp lệ.</div>
            <% } %>

            <a href="user_dashboard.jsp" class="back-link">Quay lại trang chính</a>
        </div>
    </body>
</html>


