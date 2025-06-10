<%-- 
    Document   : UserManager
    Created on : 27 thg 5, 2025, 14:39:06
    Author     : phucn
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.Users" session="true" %> 
<!DOCTYPE html>

<%@page import="model.Users"%>
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

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý người dùng</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            color: #333;
            line-height: 1.6;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .page-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #eee;
        }

        .page-title {
            font-size: 2rem;
            color: #333;
            margin-bottom: 10px;
        }

        /* Navigation Buttons */
        .nav-buttons {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
        }

        /* Alert Styles */
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
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

        /* Toolbar */
        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            gap: 15px;
        }

        .search-form {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .form-input, .form-select {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            width: 200px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            font-size: 14px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: #007bff;
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-warning {
            background: #ffc107;
            color: #212529;
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn-info {
            background: #17a2b8;
            color: white;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn:hover {
            opacity: 0.9;
        }

        .btn-sm {
            padding: 8px 15px;
            font-size: 13px;
        }

        /* Table Styles */
        .table-container {
            border: 1px solid #ddd;
            border-radius: 5px;
            overflow: hidden;
        }

        .table {
            width: 100%;
            border-collapse: collapse;
        }

        .table th {
            background: #f8f9fa;
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

        /* Badge Styles */
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

        /* Action Buttons */
        .action-buttons {
            display: flex;
            gap: 8px;
        }

        /* Pagination */
        .pagination-container {
            text-align: center;
            margin-top: 25px;
        }

        .pagination {
            display: inline-flex;
            list-style: none;
            gap: 5px;
        }

        .page-link {
            display: block;
            padding: 8px 12px;
            color: #007bff;
            text-decoration: none;
            border: 1px solid #dee2e6;
            border-radius: 4px;
        }

        .page-link:hover {
            background: #e9ecef;
        }

        .page-item.active .page-link {
            background: #007bff;
            color: white;
            border-color: #007bff;
        }

        /* Empty State */
        .empty-state {
            text-align: center;
            padding: 50px;
            color: #6c757d;
        }

        /* User Info Header */
        .user-info-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 5px;
        }

        .user-welcome {
            font-weight: bold;
            color: #333;
        }

        .logout-btn {
            background: #dc3545;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
        }

        .logout-btn:hover {
            background: #c82333;
        }

        /* Filter Section */
        .filter-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .filter-title {
            font-weight: bold;
            margin-bottom: 15px;
            color: #333;
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
            font-size: 14px;
            font-weight: bold;
            color: #555;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }
            
            .toolbar {
                flex-direction: column;
                align-items: stretch;
            }
            
            .nav-buttons {
                flex-direction: column;
            }
            
            .table-container {
                overflow-x: auto;
            }
            
            .action-buttons {
                flex-direction: column;
            }

            .filter-row {
                grid-template-columns: 1fr;
            }

            .user-info-header {
                flex-direction: column;
                gap: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- User Info Header -->

        <div class="page-header">
            <h1 class="page-title">Quản lý người dùng</h1>
        </div>

        <!-- Navigation Buttons -->
        <div class="nav-buttons">
            <a href="/ProjectWarehouse/Admin.jsp" class="btn btn-info">← Quay lại Dashboard</a>
            <a href="roleAssignment" class="btn btn-success">Quản lý phân quyền</a>
        </div>

        <!-- Thông báo -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                <c:choose>
                    <c:when test="${param.message eq 'create_success'}">Thêm người dùng thành công!</c:when>
                    <c:when test="${param.message eq 'update_success'}">Cập nhật người dùng thành công!</c:when>
                    <c:when test="${param.message eq 'delete_success'}">Xóa người dùng thành công!</c:when>
                    <c:otherwise>${param.message}</c:otherwise>
                </c:choose>
                <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
            </div>
        </c:if>

        <c:if test="${not empty param.error}">
            <div class="alert alert-danger">
                <c:choose>
                    <c:when test="${param.error eq 'invalid_id'}">ID không hợp lệ!</c:when>
                    <c:when test="${param.error eq 'user_not_found'}">Không tìm thấy người dùng!</c:when>
                    <c:when test="${param.error eq 'invalid_data'}">Dữ liệu không hợp lệ!</c:when>
                    <c:when test="${param.error eq 'delete_failed'}">Không thể xóa người dùng!</c:when>
                    <c:otherwise>${param.error}</c:otherwise>
                </c:choose>
                <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
            </div>
        </c:if>

        <!-- Filter Section -->
        <div class="filter-section">
            <div class="filter-title">Bộ lọc tìm kiếm</div>
            <form action="userfilter" method="get">
                <div class="filter-row">
                    <div class="filter-group">
                        <label class="filter-label">Vai trò</label>
                        <select name="role" class="form-select">
                            <option value="all">Tất cả vai trò</option>
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
                               placeholder="Tìm theo tên, username..." value="${param.keyword}">
                    </div>

                    <div class="filter-group">
                        <button type="submit" class="btn btn-secondary">Tìm kiếm</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Thanh công cụ -->
        <div class="toolbar">
            <a href="AddUser.jsp" class="btn btn-primary">Thêm người dùng</a>
            <div class="search-form">
                <span class="text-muted">
                    <c:if test="${not empty userList}">
                        Tổng số: <strong>${userList.size()}</strong> người dùng
                    </c:if>
                </span>
            </div>
        </div>

        <!-- Bảng danh sách -->
        <div class="table-container">
            <c:choose>
                <c:when test="${not empty userList}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>
                                    <a href="?sortField=id&sortDir=${sortField eq 'id' ? reverseSortDir : 'asc'}&keyword=${param.keyword}&role=${param.role}&status=${param.status}">
                                        ID ${sortField eq 'id' ? (sortDir eq 'asc' ? '↑' : '↓') : ''}
                                    </a>
                                </th>
                                <th>
                                    <a href="?sortField=username&sortDir=${sortField eq 'username' ? reverseSortDir : 'asc'}&keyword=${param.keyword}&role=${param.role}&status=${param.status}">
                                        Username ${sortField eq 'username' ? (sortDir eq 'asc' ? '↑' : '↓') : ''}
                                    </a>
                                </th>
                                <th>
                                    <a href="?sortField=fullname&sortDir=${sortField eq 'fullname' ? reverseSortDir : 'asc'}&keyword=${param.keyword}&role=${param.role}&status=${param.status}">
                                        Họ và tên ${sortField eq 'fullname' ? (sortDir eq 'asc' ? '↑' : '↓') : ''}
                                    </a>
                                </th>
                                <th>Vai trò</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${userList}">
                                <tr>
                                    <td><strong>#${user.id}</strong></td>
                                    <td>
                                        <strong>${user.username}</strong>
                                    </td>
                                    <td>${user.fullname}</td>
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
                                            <c:when test="${user.activeFlag == 1}">
                                                <span class="badge badge-success">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-secondary">Không hoạt động</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${user.createDate}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <div class="action-buttons">
                                            <a href="edituser?id=${user.id}" class="btn btn-warning btn-sm">Sửa</a>
                                        </div>
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
                        <a href="AddUser.jsp" class="btn btn-primary">Thêm người dùng</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Phân trang -->
        <c:if test="${totalPages > 1}">
            <div class="pagination-container">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage-1}&keyword=${param.keyword}&role=${param.role}&status=${param.status}&sortField=${sortField}&sortDir=${sortDir}">Trước</a>
                        </li>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <li class="page-item ${i eq currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}&keyword=${param.keyword}&role=${param.role}&status=${param.status}&sortField=${sortField}&sortDir=${sortDir}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage+1}&keyword=${param.keyword}&role=${param.role}&status=${param.status}&sortField=${sortField}&sortDir=${sortDir}">Sau</a>
                        </li>
                    </c:if>
                </ul>
            </div>
        </c:if>
    </div>

    <script>
        // Preserve filter values after form submission
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-hide alerts after 5 seconds
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.display = 'none';
                }, 5000);
            });

            // Add search on Enter key
            const keywordInput = document.querySelector('input[name="keyword"]');
            if (keywordInput) {
                keywordInput.addEventListener('keypress', function(e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        this.form.submit();
                    }
                });
            }
        });

        // Confirm before sensitive actions
        function confirmAction(message) {
            return confirm(message);
        }
    </script>
</body>
</html>