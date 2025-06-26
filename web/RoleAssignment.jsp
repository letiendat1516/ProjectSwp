<%-- 
  Document   : RoleAssignment
  Created on : 31 thg 5, 2025, 16:29:22
  Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.Users" %>

<%
  Users user = (Users) session.getAttribute("user");
  if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
      response.sendRedirect("login.jsp");
      return;
  }
%>

<%
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
  response.setHeader("Pragma", "no-cache"); // HTTP 1.0
  response.setDateHeader("Expires", 0); // Proxies
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý phân quyền</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f4f8;
            color: #222;
            margin: 0;
            padding: 0;
        }
        .container {
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 250px;
            background: #e6f0fa;
            padding: 20px 0;
            border-right: 1px solid #d6e0ef;
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
            flex: 1;
            padding: 20px;
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
            background: #3a8dde;
            color: #fff;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }
        .logout-btn:hover {
            background: #1567c1;
        }
        .dashboard-content {
            background: #fff;
            padding: 20px;
            border: 1px solid #d6e0ef;
            border-radius: 8px;
        }
        .page-title {
            font-size: 1.6rem;
            color: #222e45;
            margin-bottom: 10px;
        }
        .nav-buttons {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            background: #3a8dde;
            color: #fff;
        }
        .btn:hover {
            background: #1567c1;
        }
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            display: none; /* Auto-hide handled server-side or manually by user */
        }
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .alert-danger {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f1aeb5;
        }
        .alert-close {
            float: right;
            background: none;
            border: none;
            font-size: 20px;
            cursor: pointer;
        }
        .filter-section {
            background: #fff;
            border: 1px solid #d6e0ef;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .filter-title {
            font-size: 1.2rem;
            color: #1567c1;
            margin-bottom: 15px;
        }
        .filter-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            align-items: end;
        }
        .filter-group {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }
        .filter-label {
            font-size: 0.9rem;
            color: #5a7da0;
        }
        .form-select, .form-input {
            padding: 10px;
            border: 2px solid #e1e5e9;
            border-radius: 4px;
            font-size: 14px;
            width: 100%;
        }
        .form-select:focus, .form-input:focus {
            outline: none;
            border-color: #3a8dde;
        }
        .toolbar {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
            gap: 15px;
        }
        .table-container {
            background: #fff;
            border: 1px solid #d6e0ef;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .table {
            width: 100%;
            border-collapse: collapse;
        }
        .table th {
            background: #e6f0fa;
            padding: 15px;
            text-align: left;
            font-weight: bold;
            border-bottom: 2px solid #dee2e6;
        }
        .table th a {
            color: #333;
            text-decoration: none;
        }
        .table td {
            padding: 12px 15px;
            border-bottom: 1px solid #dee2e6;
        }
        .table tbody tr:hover {
            background: #f5f5f5;
        }
        .badge {
            padding: 5px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: bold;
        }
        .badge-success {
            background: #28a745;
            color: white;
        }
        .badge-secondary {
            background: #6c757d;
            color: white;
        }
        .badge-info {
            background: #17a2b8;
            color: white;
        }
        .badge-warning {
            background: #ffc107;
            color: #212529;
        }
        .badge-danger {
            background: #dc3545;
            color: white;
        }
        .text-muted {
            color: #6c757d;
        }
        .role-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }
        .role-form select {
            padding: 8px;
            border: 2px solid #e1e5e9;
            border-radius: 4px;
            font-size: 13px;
            min-width: 150px;
        }
        .empty-state {
            text-align: center;
            padding: 40px;
            color: #5a7da0;
        }
        @media (max-width: 900px) {
            .container {
                flex-direction: column;
            }
            .sidebar {
                width: 100%;
            }
            .filter-row {
                grid-template-columns: 1fr;
            }
            .toolbar {
                flex-direction: column;
            }
            .nav-buttons {
                flex-direction: column;
            }
            .role-form {
                flex-direction: column;
                align-items: stretch;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="sidebar">
            <h2>Warehouse Manager</h2>
            <a href="usermanager" class="nav-item">User Manager</a>
            <a href="roleAssignment" class="nav-item">Role Assignment</a>
            <a href="categoriesforward.jsp" class="nav-item">Material Information</a>
            <a href="passwordrequest" class="nav-item">Password Request</a>
            <a href="RequestForward.jsp" class="nav-item">Transaction</a>
            <a href="#" class="nav-item">Statistic</a>
        </div>
        <div class="main-content">
            <div class="header">
                <h1 class="header-title">Quản lý phân quyền</h1>
                <div class="header-user">
                    <span class="user-name">Admin</span>
                    <a href="logout" class="logout-btn">Log out</a>
                </div>
            </div>
            <div class="dashboard-content">
                <div class="nav-buttons">
                    <a href="/ProjectWarehouse/Admin.jsp" class="btn">Quay lại Dashboard</a>
                    <a href="usermanager" class="btn">Quản lý người dùng</a>
                </div>

                <!-- Success Messages -->
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success" style="display: block;">
                        ${sessionScope.message}
                        <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">×</button>
                        <c:remove var="message" scope="session"/>
                    </div>
                </c:if>              

                <div class="filter-section">
                    <div class="filter-title">Bộ lọc tìm kiếm</div>
                    <form action="userfilter" method="get">
                        <div class="filter-row">
                            <div class="filter-group">
                                <label class="filter-label">Vai trò hiện tại</label>
                                <select name="role" class="form-select">
                                    <option value="all" ${param.role eq 'all' ? 'selected' : ''}>Tất cả vai trò</option>
                                    <option value="2" ${param.role eq '2' ? 'selected' : ''}>Warehouse Staff</option>
                                    <option value="3" ${param.role eq '3' ? 'selected' : ''}>Company Employee</option>
                                    <option value="4" ${param.role eq '4' ? 'selected' : ''}>Company Director</option>
                                </select>
                            </div>
                            <div class="filter-group">
                                <label class="filter-label">Trạng thái</label>
                                <select name="status" class="form-select">
                                    <option value="">Tất cả trạng thái</option>
                                    <option value="active" ${param.status eq 'active' ? 'selected' : ''}>Hoạt động</option>
                                    <option value="inactive" ${param.status eq 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                                </select>
                            </div>
                            <div class="filter-group">
                                <label class="filter-label">Từ khóa</label>
                                <input type="text" name="keyword" class="form-input" 
                                       placeholder="Tìm theo tên, username, email..." value="${param.keyword}">
                            </div>
                            <div class="filter-group">
                                <input type="hidden" name="page" value="roleassignment" />
                                <button type="submit" class="btn">Tìm kiếm</button>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="toolbar">
                    <div class="search-form">
                        <span class="text-muted">
                            <c:if test="${not empty userList}">
                                Tổng số: <strong>${userList.size()}</strong> người dùng
                            </c:if>
                        </span>
                    </div>
                </div>

                <div class="table-container">
                    <c:choose>
                        <c:when test="${not empty userList}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Họ và tên</th>
                                        <th>Email</th>
                                        <th>Vai trò hiện tại</th>
                                        <th>Phân quyền mới</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${userList}">
                                        <tr>
                                            <td><strong>#${user.id}</strong></td>
                                            <td><strong>${user.username}</strong></td>
                                            <td>${user.fullname}</td>
                                            <td>${user.email}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${user.roleName == 'Admin'}">
                                                        <span class="badge badge-danger">${user.roleName}</span>
                                                    </c:when>
                                                    <c:when test="${user.roleName == 'Warehouse Staff'}">
                                                        <span class="badge badge-success">Warehouse Staff</span>
                                                    </c:when>
                                                    <c:when test="${user.roleName == 'Company Employee'}">
                                                        <span class="badge badge-info">Company Employee</span>
                                                    </c:when>
                                                    <c:when test="${user.roleName == 'Company Director'}">
                                                        <span class="badge badge-warning">Company Director</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-secondary">${user.roleName}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${user.roleName == 'Admin'}">
                                                        <span class="text-muted">Không thể thay đổi</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="roleAssignment" method="post" class="role-form" 
                                                              onsubmit="return confirm('Bạn có chắc muốn thay đổi quyền của người dùng này?');">
                                                            <input type="hidden" name="userId" value="${user.id}"/>
                                                            <select name="role">
                                                                <option value="2" ${user.roleName == 'Warehouse Staff' ? 'selected' : '' }>Warehouse Staff</option>
                                                                <option value="3" ${user.roleName == 'Company Employee' ? 'selected' : ''}>Company Employee</option>
                                                                <option value="4" ${user.roleName == 'Company Director' ? 'selected' : ''}>Company Director</option>
                                                            </select>
                                                            <button type="submit" class="btn">Phân quyền</button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>                           
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <h3>Không có người dùng nào</h3>
                                <p>Không tìm thấy người dùng phù hợp với bộ lọc</p>
                                <a href="admin" class="btn">Quản lý người dùng</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</body>
</html>