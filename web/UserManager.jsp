<%-- 
    Document   : UserManager
    Created on : 27 thg 5, 2025, 14:39:06
    Author     : phucn
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
        <title>Quản lý người dùng - Hệ thống kho</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f5f5f5;
                color: #333;
                line-height: 1.6;
            }

            .container {
                display: flex;
                min-height: 100vh;
            }

            /* Main Content */
            .main-content {
                flex: 1;
                background: #f5f5f5;
                display: flex;
                flex-direction: column;
            }

            /* Header */
            .header {
                background: white;
                padding: 20px 30px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 1px solid #ddd;
                margin-bottom: 20px;
            }

            .header-title {
                font-size: 1.8rem;
                font-weight: bold;
                color: #333;
                margin: 0;
            }

            .header-actions {
                display: flex;
                gap: 15px;
                align-items: center;
            }

            /* User Manager Content */
            .user-manager-content {
                flex: 1;
                padding: 0 30px 30px;
                max-width: 1400px;
                margin: 0 auto;
                width: 100%;
            }

            .page-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 30px;
                padding-bottom: 20px;
                border-bottom: 2px solid #eee;
            }

            .page-title {
                font-size: 2rem;
                color: #333;
                margin: 0;
            }

            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                text-decoration: none;
                font-size: 14px;
                font-weight: 500;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.2s ease;
                cursor: pointer;
            }

            .btn-primary {
                background: #007bff;
                color: white;
            }

            .btn-primary:hover {
                background: #0056b3;
            }

            .btn-success {
                background: #28a745;
                color: white;
            }

            .btn-success:hover {
                background: #218838;
            }

            .btn-secondary {
                background: #6c757d;
                color: white;
            }

            .btn-secondary:hover {
                background: #545b62;
            }

            /* Filter Section */
            .filter-section {
                background: white;
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 25px;
                margin-bottom: 25px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }

            .filter-title {
                font-size: 1.2rem;
                font-weight: bold;
                color: #333;
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .filter-bar {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
                align-items: end;
            }

            .filter-group {
                display: flex;
                flex-direction: column;
                gap: 8px;
            }

            .filter-label {
                font-size: 0.9rem;
                color: #555;
                font-weight: 600;
            }

            .filter-bar select, 
            .filter-bar input[type="text"] {
                padding: 12px 15px;
                border: 2px solid #e1e5e9;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.2s ease;
                background: white;
            }

            .filter-bar select:focus,
            .filter-bar input[type="text"]:focus {
                outline: none;
                border-color: #007bff;
                box-shadow: 0 0 0 3px rgba(0,123,255,0.1);
            }

            .search-btn {
                background: #007bff;
                color: white;
                border: none;
                padding: 12px 25px;
                border-radius: 6px;
                cursor: pointer;
                font-size: 14px;
                font-weight: 600;
                display: flex;
                align-items: center;
                gap: 8px;
                transition: background-color 0.2s ease;
            }

            .search-btn:hover {
                background: #0056b3;
            }

            /* Table Styles */
            .table-container {
                background: white;
                border: 1px solid #ddd;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                margin-bottom: 25px;
            }

            .table-header {
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
                padding: 20px 25px;
                border-bottom: 1px solid #ddd;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .table-title {
                font-size: 1.3rem;
                color: #333;
                margin: 0;
                font-weight: bold;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .table-count {
                font-size: 0.9rem;
                color: #666;
                background: #e9ecef;
                padding: 5px 12px;
                border-radius: 15px;
                font-weight: 500;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: white;
            }

            table th, 
            table td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #eee;
            }

            table th {
                background: #f8f9fa;
                font-weight: 600;
                color: #555;
                font-size: 0.9rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            table tbody tr {
                transition: all 0.2s ease;
            }

            table tbody tr:hover {
                background: #f8f9fa;
                transform: translateY(-1px);
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            table tr:last-child td {
                border-bottom: none;
            }

            /* Status Badge */
            .status-badge {
                padding: 6px 12px;
                border-radius: 20px;
                font-size: 0.8rem;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                display: inline-block;
            }

            .status-active {
                background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
                color: #155724;
                border: 1px solid #c3e6cb;
            }

            .status-inactive {
                background: linear-gradient(135deg, #f8d7da 0%, #f1b0b7 100%);
                color: #721c24;
                border: 1px solid #f1b0b7;
            }

            /* Action Button */
            .action-btn {
                background: #007bff;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 5px;
                text-decoration: none;
                font-size: 12px;
                font-weight: 500;
                transition: all 0.2s ease;
                display: inline-flex;
                align-items: center;
                gap: 5px;
            }

            .action-btn:hover {
                background: #0056b3;
                transform: translateY(-1px);
                box-shadow: 0 2px 5px rgba(0,123,255,0.3);
            }

            /* Pagination */
            .pagination-container {
                background: white;
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 20px 25px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .pagination-info {
                color: #666;
                font-size: 0.9rem;
                font-weight: 500;
            }

            .pagination {
                display: flex;
                gap: 8px;
                align-items: center;
            }

            .pagination a, 
            .pagination button {
                display: inline-block;
                padding: 10px 15px;
                text-decoration: none;
                border: 2px solid #e1e5e9;
                color: #007bff;
                border-radius: 6px;
                background: white;
                font-size: 14px;
                cursor: pointer;
                transition: all 0.2s ease;
                font-weight: 500;
            }

            .pagination a:hover, 
            .pagination button:hover {
                background: #e9ecef;
                border-color: #007bff;
                transform: translateY(-1px);
            }

            .pagination .active {
                background: #007bff;
                color: white;
                border-color: #007bff;
            }

            /* Empty State */
            .empty-state {
                text-align: center;
                padding: 80px 20px;
                color: #666;
            }

            .empty-state-icon {
                font-size: 4rem;
                margin-bottom: 20px;
                opacity: 0.3;
            }

            .empty-state-title {
                font-size: 1.4rem;
                margin-bottom: 15px;
                color: #333;
                font-weight: 600;
            }

            .empty-state-text {
                font-size: 1rem;
                color: #666;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .header {
                    padding: 15px 20px;
                    flex-direction: column;
                    gap: 15px;
                    align-items: stretch;
                }
                
                .header-title {
                    font-size: 1.5rem;
                    text-align: center;
                }
                
                .user-manager-content {
                    padding: 0 20px 20px;
                }
                
                .page-header {
                    flex-direction: column;
                    gap: 15px;
                    align-items: stretch;
                }
                
                .filter-bar {
                    grid-template-columns: 1fr;
                }
                
                .table-container {
                    overflow-x: auto;
                }
                
                table {
                    min-width: 700px;
                }
                
                .pagination-container {
                    flex-direction: column;
                    gap: 15px;
                }

                .table-header {
                    flex-direction: column;
                    gap: 10px;
                    align-items: stretch;
                    text-align: center;
                }
            }

            /* Loading Animation */
            .loading {
                display: inline-block;
                width: 20px;
                height: 20px;
                border: 3px solid #f3f3f3;
                border-top: 3px solid #007bff;
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <!-- Main Content -->
            <div class="main-content">
                <!-- Header -->
                <div class="header">
                    <h1 class="header-title">👥 Quản lý người dùng</h1>
                    <div class="header-actions">
                        <a href="Admin.jsp" class="btn btn-secondary">🔙 Quay lại</a>
                        <a href="AddUser.jsp" class="btn btn-success">➕ Thêm người dùng</a>
                    </div>
                </div>

                <!-- User Manager Content -->
                <div class="user-manager-content">
                    <!-- Filter Section -->
                    <div class="filter-section">
                        <h3 class="filter-title">🔍 Tìm kiếm & Lọc dữ liệu</h3>
                        <form action="userfilter" method="get">
                            <div class="filter-bar">
                                <div class="filter-group">
                                    <label class="filter-label">Vai trò</label>
                                    <select id="role" name="role">
                                        <option value="all" selected>Tất cả vai trò</option>
                                        <option value="2">Nhân viên kho</option>
                                        <option value="3">Nhân viên công ty</option>
                                        <option value="4">Giám đốc công ty</option>
                                    </select>
                                </div>

                                <div class="filter-group">
                                    <label class="filter-label">Trạng thái</label>
                                    <select name="status">
                                        <option value="">Tất cả trạng thái</option>
                                        <option value="active">Đang hoạt động</option>
                                        <option value="inactive">Không hoạt động</option>
                                    </select>
                                </div>

                                <div class="filter-group">
                                    <label class="filter-label">Từ khóa</label>
                                    <input type="text" name="keyword" placeholder="Tìm theo tên đăng nhập, họ tên...">
                                </div>

                                <div class="filter-group">
                                    <button type="submit" class="search-btn">
                                        🔍 Tìm kiếm
                                    </button>
                                </div>
                            </div>
                            <input type="hidden" name="page" value="usermanager" />
                        </form>
                    </div>

                    <!-- Table Section -->
                    <div class="table-container">
                        <div class="table-header">
                            <h3 class="table-title">📋 Danh sách người dùng</h3>
                            <div class="table-count">
                                <c:set var="userCount" value="0" />
                                <c:forEach var="user" items="${userList}">
                                    <c:set var="userCount" value="${userCount + 1}" />
                                </c:forEach>
                                <c:choose>
                                    <c:when test="${userCount > 0}">
                                        Hiển thị ${userCount} người dùng
                                    </c:when>
                                    <c:otherwise>
                                        Không tìm thấy người dùng
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <c:choose>
                            <c:when test="${not empty userList}">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Tên đăng nhập</th>
                                            <th>Họ và tên</th>
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
                                                <td><strong>${user.username}</strong></td>
                                                <td>${user.fullname}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${user.roleName == 'Warehouse Staff'}">
                                                            📦 Nhân viên kho
                                                        </c:when>
                                                        <c:when test="${user.roleName == 'Company Employee'}">
                                                            👔 Nhân viên công ty
                                                        </c:when>
                                                        <c:when test="${user.roleName == 'Company Director'}">
                                                            👨‍💼 Giám đốc công ty
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${user.roleName}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${user.activeFlag == 1}">
                                                            <span class="status-badge status-active">✅ Hoạt động</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge status-inactive">❌ Không hoạt động</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${user.createDate}" pattern="dd/MM/yyyy" />
                                                </td>
                                                <td>
                                                    <a href="edituser?id=${user.id}" class="action-btn">
                                                        ✏️ Chỉnh sửa
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <div class="empty-state-icon">👥</div>
                                    <h3 class="empty-state-title">Không tìm thấy người dùng</h3>
                                    <p class="empty-state-text">Hãy thử điều chỉnh tiêu chí tìm kiếm hoặc thêm người dùng mới.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <div class="pagination-container">
                            <div class="pagination-info">
                                Trang ${currentPage} / ${totalPages}
                            </div>
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="admin?page=${currentPage - 1}">← Trước</a>
                                </c:if>
                                
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <button class="active">${i}</button>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="admin?page=${i}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                
                                <c:if test="${currentPage < totalPages}">
                                    <a href="admin?page=${currentPage + 1}">Sau →</a>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                // Form validation
                const searchForm = document.querySelector('form[action="userfilter"]');
                if (searchForm) {
                    searchForm.addEventListener('submit', function(e) {
                        const keyword = this.querySelector('input[name="keyword"]').value.trim();
                        const role = this.querySelector('select[name="role"]').value;
                        const status = this.querySelector('select[name="status"]').value;
                        
                        if (!keyword && role === 'all' && !status) {
                            if (!confirm('Chưa có tiêu chí tìm kiếm nào được chỉ định. Hiển thị tất cả người dùng?')) {
                                e.preventDefault();
                            }
                        }
                        
                        // Add loading state
                        const submitBtn = this.querySelector('.search-btn');
                        const originalText = submitBtn.innerHTML;
                        submitBtn.innerHTML = '<div class="loading"></div> Đang tìm kiếm...';
                        submitBtn.disabled = true;
                        
                        // Reset after 3 seconds if still on page
                        setTimeout(() => {
                            if (submitBtn) {
                                submitBtn.innerHTML = originalText;
                                submitBtn.disabled = false;
                            }
                        }, 3000);
                    });
                }

                // Enhanced table interactions
                const tableRows = document.querySelectorAll('tbody tr');
                tableRows.forEach(row => {
                    row.addEventListener('click', function(e) {
                        if (e.target.tagName !== 'A' && e.target.tagName !== 'BUTTON') {
                            // Optional: Add row selection functionality
                            this.style.backgroundColor = this.style.backgroundColor ? '' : '#e3f2fd';
                        }
                    });
                });

                // Auto-refresh functionality (optional)
                let autoRefreshInterval;
                const enableAutoRefresh = false; // Set to true to enable
                
                if (enableAutoRefresh) {
                    autoRefreshInterval = setInterval(() => {
                        // Only refresh if no form is being filled
                        const activeElement = document.activeElement;
                        if (activeElement.tagName !== 'INPUT' && activeElement.tagName !== 'SELECT') {
                            location.reload();
                        }
                    }, 30000); // Refresh every 30 seconds
                }

                // Keyboard shortcuts
                document.addEventListener('keydown', function(e) {
                    // Ctrl + F to focus search
                    if (e.ctrlKey && e.key === 'f') {
                        e.preventDefault();
                        const searchInput = document.querySelector('input[name="keyword"]');
                        if (searchInput) {
                            searchInput.focus();
                        }
                    }
                    
                    // Escape to clear search
                    if (e.key === 'Escape') {
                        const searchInput = document.querySelector('input[name="keyword"]');
                        if (searchInput && searchInput === document.activeElement) {
                            searchInput.value = '';
                        }
                    }
                });
            });
        </script>
    </body>
</html>