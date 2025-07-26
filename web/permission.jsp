<%-- 
    Document   : permission
    Created on : 22 thg 5, 2025, 22:38:49
    Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="model.Users"%>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>

<%
    Users user = (Users) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
    <head>
        <title>Phân quyền chức năng</title>
        <style>
            body {
                font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
                background-color: #f4f4f9;
                padding: 0;
                margin: 0;
            }
            .container {
                display: flex;
                min-height: 100vh;
            }
            .sidebar {
                position: fixed;
                top: 0;
                left: 0;
                width: 250px;
                height: 100vh;
                background: #e6f0fa;
                padding: 20px 0;
                border-right: 1px solid #d6e0ef;
                z-index: 100;
            }
            .sidebar h2 {
                font-size: 1.4rem;
                color: #1567c1;
                text-align: center;
                margin-bottom: 20px;
            }
            .nav-item {
                display: block;
                padding: 10px 20px;
                color: #214463;
                text-decoration: none;
                font-size: 1rem;
            }
            .nav-item:hover {
                background: #c2e9fb;
                color: #1567c1;
            }
            .main-content {
                margin-left: 250px;
                padding: 20px;
                flex: 1;
            }
            .header {
                background: #fff;
                padding: 15px;
                border-bottom: 1px solid #d6e0ef;
                margin-bottom: 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .header-title {
                font-size: 1.8rem;
                color: #1567c1;
                margin: 0;
                display: flex;
                align-items: center;
            }
            .header-user {
                display: flex;
                align-items: center;
            }
            .user-name {
                font-size: 1rem;
                color: #214463;
                margin-right: 15px;
            }
            .logout-btn {
                background: red;
                color: #fff;
                border: #007BFF;
                padding: 8px 16px;
                border-radius: 4px;
                cursor: pointer;
                text-decoration: none;
            }
            .logout-btn:hover {
                background: orange;
            }
            .form-container {
                max-width: 1200px;
                margin: auto;
                background: white;
                padding: 5px 32px 15px 32px;
                border: 1px solid #d6e0ef;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.10);
            }
            .msg.success {
                color: green;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 16px;
                width: auto;
            }
            .msg.error {
                color: red;
                padding: 10px;
                border-radius: 4px;
                margin-bottom: 16px;
            }

            form.search-form {
                display: flex;
                gap: 12px;
                margin-bottom: 18px;
                align-items: flex-end;
                flex-wrap: wrap;
            }
            .search-group label {
                font-weight: bold;
                color: #1976d2;
                margin-right: 5px;
                padding: 5px;
            }
            .search-group input, .search-group select {
                padding: 7px 9px;
                border-radius: 4px;
                border: 1px solid #b8cdf1;
                font-size: 1rem;
            }
            .search-group {
                display: flex;
                flex-direction: column;
            }
            .search-btn {
                background: #1976d2;
                color: #fff;
                border: none;
                border-radius: 4px;
                padding: 9px 18px;
                font-weight: bold;
                font-size: 1rem;
                cursor: pointer;
                margin-top: 3px;
            }
            .search-btn:hover {
                background: #135bad;
            }
            .reset-btn {
                background: #6c757d;
                color: #fff;
                border: none;
                border-radius: 4px;
                padding: 9px 18px;
                font-weight: bold;
                font-size: 1rem;
                cursor: pointer;
                margin-top: 3px;
            }
            .reset-btn:hover {
                background: #5a6268;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
            }
            th, td {
                border: 1px solid #e0e7ef;
                padding: 6px 40px;
                text-align: center;
                font-size: 15px;
            }
            th {
                background: #e6f0fa;
                color: #1976d2;
            }
            input[type="checkbox"] {
                width: 24px;
                height: 24px;
                cursor: pointer;
                vertical-align: middle;
            }

            .checkbox-col {
                width: 62px;
            }
            .sticky {
                position: sticky;
                left: 0;
                background: #e6f0fa;
            }
            .action-buttons {
                margin: 22px 0;
                text-align: center;
                display: flex;
                gap: 15px;
                justify-content: center;
            }
            .save-btn {
                background: #28a745;
                color: #fff;
                border: none;
                border-radius: 6px;
                padding: 12px 32px;
                font-size: 1.1rem;
                font-weight: bold;
                cursor: pointer;
            }
            .save-btn:hover {
                background: #218838;
            }
            .reset-all-btn {
                background: #dc3545;
                color: #fff;
                border: none;
                border-radius: 6px;
                padding: 12px 32px;
                font-size: 1.1rem;
                font-weight: bold;
                cursor: pointer;
            }
            .reset-all-btn:hover {
                background: #c82333;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="sidebar">
                <h2>Warehouse Management</h2>
                <a href="usermanager" class="nav-item">Quản lý người dùng</a>
                <a href="role-permission" class="nav-item">Phân quyền người dùng</a>
                <a href="categoriesforward.jsp" class="nav-item">Thông tin vật tư</a>
                <a href="passwordrequest" class="nav-item">Reset mật khẩu</a>
                <a href="ApproveListForward.jsp" class="nav-item">Đơn từ</a>
                <a href="RequestForward.jsp" class="nav-item">Giao dịch</a>
                <a href="StatisticSupplierEvaluation.jsp" class="nav-item">Thống kê</a>
            </div>
            <div class="main-content">
                <div class="header">
                    <h1 class="header-title">Phân quyền chức năng theo vai trò</h1>
                    <div class="header-user">
                        <span class="user-name"><%= user.getFullname()%></span>
                        <a href="logout" class="logout-btn">Đăng xuất</a>
                    </div>
                </div>
                <div class="form-container">
                    <center>
                        <c:if test="${param.success == '1'}">
                            <div class="msg success">Lưu phân quyền thành công! Có thể sử dụng sau khi đăng nhập lại.</div>
                            <c:remove var="message" scope="session"/>
                        </c:if>
                        <c:if test="${param.error == '1'}">
                            <div class="msg error">Có lỗi xảy ra khi lưu phân quyền!</div>
                        </c:if>
                        <c:if test="${param.reset == '1'}">
                            <div class="msg success">Đã reset toàn bộ phân quyền thành công!</div>
                        </c:if>
                    </center>

                    <form class="search-form" method="get" action="role-permission">
                        <div class="search-group">
                            <label for="keyword">Từ khóa (Tên/Mã quyền):</label>
                            <input type="text" name="keyword" id="keyword" 
                                   value="${filterKeyword != null ? filterKeyword : ''}" 
                                   placeholder="Nhập tên quyền hoặc mã"/>
                        </div>
                        <div class="search-group">
                            <label for="role">Vai trò:</label>
                            <select name="role" id="role">
                                <option value="">Tất cả vai trò</option>
                               <c:forEach var="r" items="${allRoles}">
                                <option value="${r.id}" ${filterRole == r.id ? 'selected' : ''}>${r.roleName}</option>
                               </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="search-btn">Lọc</button>
                        <a href="role-permission" class="reset-btn" style="text-decoration: none; display: inline-block;">Reset</a>
                    </form>

                    <form action="role-permission" method="post">
                        <input type="hidden" name="keyword" value="${filterKeyword != null ? filterKeyword : ''}">
                        <input type="hidden" name="role" value="${filterRole != null ? filterRole : ''}">

                        <table>
                            <thead>
                                <tr>
                                    <th class="sticky">Tên quyền</th>
                                    <th>Mã quyền</th>
                                        <c:forEach var="role" items="${roles}">
                                        <th>${role.roleName}</th>
                                        </c:forEach>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="perm" items="${permissions}">
                                    <tr>
                                        <td class="sticky" style="text-align:left">${perm.name}</td>
                                        <td>${perm.code}</td>
                                        <c:forEach var="role" items="${roles}">
                                            <td class="checkbox-col">
                                                <input type="checkbox"
                                                       name="perm_${perm.id}_role_${role.id}"
                                                       <c:if test="${rolePermissions[role.id] != null && rolePermissions[role.id].contains(perm.id)}">checked</c:if>
                                                           />
                                                </td>
                                        </c:forEach>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="action-buttons">
                            <button class="save-btn" type="submit" name="action" value="save">Lưu phân quyền</button>
                            <button class="reset-all-btn" type="submit" name="action" value="reset" 
                                    onclick="return confirm('Bạn có chắc chắn muốn xóa toàn bộ phân quyền không?')">
                                Reset toàn bộ
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>