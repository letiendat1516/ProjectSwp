<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<%-- Tính toán reverseSortDir --%>
<c:set var="reverseSortDir" value="${sortDir eq 'asc' ? 'desc' : 'asc'}" />

<%-- Tính toán phân trang hiển thị --%>
<c:set var="startPage" value="${currentPage - 2 > 0 ? currentPage - 2 : 1}" />
<c:set var="endPage" value="${startPage + 4 < totalPages ? startPage + 4 : totalPages}" />
<c:if test="${endPage - startPage < 4 && totalPages > 5}">
    <c:set var="startPage" value="${endPage - 4 > 0 ? endPage - 4 : 1}" />
</c:if>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách danh mục loại sản phẩm</title>
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
            }

            .form-input, .form-select {
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }

            .form-input {
                width: 250px;
            }

            .form-select {
                width: 150px;
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

            /* Filter Panel */
            .filter-panel {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                border: 1px solid #dee2e6;
            }

            .filter-row {
                display: flex;
                gap: 15px;
                align-items: end;
                flex-wrap: wrap;
            }

            .filter-group {
                display: flex;
                flex-direction: column;
                gap: 5px;
            }

            .filter-group label {
                font-weight: 500;
                color: #555;
                font-size: 14px;
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
                position: relative;
                white-space: nowrap;
            }

            .table th a {
                color: #333;
                text-decoration: none;
                display: block;
                width: 100%;
                height: 100%;
            }

            .sort-icon {
                font-size: 12px;
                margin-left: 5px;
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

            /* Info Panel */
            .info-panel {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                border-left: 4px solid #007bff;
            }

            /* Date time styles */
            .date-time {
                font-size: 12px;
                color: #666;
            }

            /* Responsive */
            @media (max-width: 1200px) {
                .table-container {
                    overflow-x: auto;
                }
            }

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

                .action-buttons {
                    flex-direction: column;
                }

                .search-form {
                    flex-direction: column;
                }

                .form-input {
                    width: 100%;
                }

                .table th, .table td {
                    padding: 8px;
                    font-size: 12px;
                }

                .filter-row {
                    flex-direction: column;
                }

                .filter-group {
                    width: 100%;
                }
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

            .header {
                text-align: center;
                margin-bottom: 30px;
            }
            .header-user {
                display: flex;
                align-items: center;
            }
            .label {
                color: #888;
                width: 120px;
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
            .page-title {
                color: #3f51b5;
                font-size: 2rem;
                margin-bottom: 10px;
            }
            /* Filter Section Styles */
            .filter-section {
                background: white;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                border: 1px solid #e9ecef;
            }

            .filter-row {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
                gap: 15px;
                align-items: end;
            }

            .filter-item {
                display: flex;
                flex-direction: column;
            }

            .filter-item label {
                margin-bottom: 5px;
                font-weight: 500;
                color: #555;
                font-size: 14px;
            }

            .filter-input,
            .filter-select {
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
                transition: border-color 0.3s;
            }

            .filter-input:focus,
            .filter-select:focus {
                outline: none;
                border-color: #007bff;
            }

            .filter-actions {
                display: flex;
                gap: 10px;
                align-items: flex-end;
            }

            .filter-actions button {
                flex: 1;
            }

            /* Responsive cho filter */
            @media (max-width: 768px) {
                .filter-row {
                    grid-template-columns: 1fr;
                }

                .filter-actions {
                    margin-top: 10px;
                }
            }
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <div class="header">
                    <h1 class="page-title">Quản lý danh mục loại sản phẩm</h1>
                    <div class="header-user">
                        <label class="label"><%= user.getFullname()%></label>
                        <a href="logout" class="logout-btn">Đăng xuất</a>
                    </div>
                </div>

                <!-- Navigation Buttons -->
                <div class="nav-buttons">
                    <a href="/ProjectWarehouse/categoriesforward.jsp" class="btn btn-info">← Quay lại trang trước</a>
                    <a href="${pageContext.request.contextPath}/category-parent/list" class="btn btn-success">Quản lý danh mục</a>
                </div>

                <!-- Thông báo -->
                <c:if test="${not empty param.message}">
                    <div class="alert alert-success">
                        <c:choose>
                            <c:when test="${param.message eq 'create_success'}">Thêm danh mục thành công!</c:when>
                            <c:when test="${param.message eq 'update_success'}">Cập nhật danh mục thành công!</c:when>
                            <c:when test="${param.message eq 'delete_success'}">Xóa danh mục thành công!</c:when>
                            <c:otherwise>${param.message}</c:otherwise>
                        </c:choose>
                        <button type="button" class="alert-close" onclick="this.parentElement.style.display = 'none'">&times;</button>
                    </div>
                </c:if>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">
                        <c:choose>
                            <c:when test="${param.error eq 'invalid_id'}">ID không hợp lệ!</c:when>
                            <c:when test="${param.error eq 'category_not_found'}">Không tìm thấy danh mục!</c:when>
                            <c:when test="${param.error eq 'invalid_data'}">Dữ liệu không hợp lệ!</c:when>
                            <c:when test="${param.error eq 'delete_failed'}">Không thể xóa danh mục!</c:when>
                            <c:otherwise>${param.error}</c:otherwise>
                        </c:choose>
                        <button type="button" class="alert-close" onclick="this.parentElement.style.display = 'none'">&times;</button>
                    </div>
                </c:if>

                <!-- Thanh công cụ -->
                <div class="toolbar">
                    <div style="display: flex; gap: 10px;">
                        <a href="${pageContext.request.contextPath}/category/create" class="btn btn-primary">+ Thêm danh mục loại sản phẩm</a>
                        <a href="${pageContext.request.contextPath}/category-parent/statistics" class="btn btn-success">Thống kê danh mục loại sản phẩm</a>
                    </div>
                </div>

                <!-- Filter Section -->
                <div class="filter-section">
                    <h3 style="margin-bottom: 15px; color: #333;">Bộ lọc</h3>
                    <form id="filterForm" method="get" action="${pageContext.request.contextPath}/category/list">
                        <div class="filter-row">
                            <div class="filter-item">
                                <label>Tìm kiếm:</label>
                                <input type="text" name="search" value="${searchKeyword}" 
                                       placeholder="Nhập tên danh mục..." class="filter-input">
                            </div>
                            <div class="filter-item">
                                <label>Trạng thái:</label>
                                <select name="status" class="filter-select">
                                    <option value="">Tất cả</option>
                                    <option value="1" ${status == '1' ? 'selected' : ''}>Hoạt động</option>
                                    <option value="0" ${status == '0' ? 'selected' : ''}>Không hoạt động</option>
                                </select>
                            </div>
                            <div class="filter-item">
                                <label>Danh mục cha:</label>
                                <select name="parentId" class="filter-select">
                                    <option value="">Tất cả</option>
                                    <c:forEach var="parent" items="${parentCategories}">
                                        <option value="${parent.id}" ${parentId == parent.id ? 'selected' : ''}>
                                            ${parent.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="filter-item">
                                <label>Sắp xếp theo:</label>
                                <select name="sortField" class="filter-select">
                                    <option value="id" ${sortField == 'id' ? 'selected' : ''}>ID</option>
                                    <option value="name" ${sortField == 'name' ? 'selected' : ''}>Tên</option>
                                    <option value="parent_name" ${sortField == 'parent_name' ? 'selected' : ''}>Danh mục cha</option>
                                    <option value="active_flag" ${sortField == 'active_flag' ? 'selected' : ''}>Trạng thái</option>
                                    <option value="create_date" ${sortField == 'create_date' ? 'selected' : ''}>Ngày tạo</option>
                                    <option value="update_date" ${sortField == 'update_date' ? 'selected' : ''}>Ngày cập nhật</option>
                                </select>
                            </div>
                            <div class="filter-item">
                                <label>Thứ tự:</label>
                                <select name="sortDir" class="filter-select">
                                    <option value="asc" ${sortDir == 'asc' ? 'selected' : ''}>Tăng dần</option>
                                    <option value="desc" ${sortDir == 'desc' ? 'selected' : ''}>Giảm dần</option>
                                </select>
                            </div>
                            <div class="filter-actions">
                                <button type="submit" class="btn btn-primary">Lọc</button>
                                <button type="button" class="btn btn-secondary" onclick="resetFilter()">Đặt lại</button>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Bảng danh sách -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${not empty categories}">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>
                                            <a href="?sortField=id&sortDir=${sortField eq 'id' ? reverseSortDir : 'asc'}&search=${searchKeyword}&status=${status}&parentId=${parentId}">
                                                ID 
                                                <c:if test="${sortField eq 'id'}">
                                                    <span class="sort-icon">${sortDir eq 'asc' ? '↑' : '↓'}</span>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortField=name&sortDir=${sortField eq 'name' ? reverseSortDir : 'asc'}&search=${searchKeyword}&status=${status}&parentId=${parentId}">
                                                Tên 
                                                <c:if test="${sortField eq 'name'}">
                                                    <span class="sort-icon">${sortDir eq 'asc' ? '↑' : '↓'}</span>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortField=parent_name&sortDir=${sortField eq 'parent_name' ? reverseSortDir : 'asc'}&search=${searchKeyword}&status=${status}&parentId=${parentId}">
                                                Danh mục 
                                                <c:if test="${sortField eq 'parent_name'}">
                                                    <span class="sort-icon">${sortDir eq 'asc' ? '↑' : '↓'}</span>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortField=active_flag&sortDir=${sortField eq 'active_flag' ? reverseSortDir : 'asc'}&search=${searchKeyword}&status=${status}&parentId=${parentId}">
                                                Trạng thái 
                                                <c:if test="${sortField eq 'active_flag'}">
                                                    <span class="sort-icon">${sortDir eq 'asc' ? '↑' : '↓'}</span>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortField=create_date&sortDir=${sortField eq 'create_date' ? reverseSortDir : 'asc'}&search=${searchKeyword}&status=${status}&parentId=${parentId}">
                                                Ngày tạo 
                                                <c:if test="${sortField eq 'create_date'}">
                                                    <span class="sort-icon">${sortDir eq 'asc' ? '↑' : '↓'}</span>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="?sortField=update_date&sortDir=${sortField eq 'update_date' ? reverseSortDir : 'asc'}&search=${searchKeyword}&status=${status}&parentId=${parentId}">
                                                Ngày cập nhật 
                                                <c:if test="${sortField eq 'update_date'}">
                                                    <span class="sort-icon">${sortDir eq 'asc' ? '↑' : '↓'}</span>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="category" items="${categories}">
                                        <tr>
                                            <td>#${category.id}</td>
                                            <td>${category.name}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty category.parentName}">
                                                        <span class="badge badge-info">${category.parentName}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Không có</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge ${category.activeFlag ? 'badge-success' : 'badge-secondary'}" 
                                                      id="status-${category.id}">
                                                    ${category.activeFlag ? 'Hoạt động' : 'Không hoạt động'}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="date-time">
                                                    <c:choose>
                                                        <c:when test="${not empty category.createdAt}">
                                                            ${dateTimeFormatter.format(category.createdAt)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">--</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="date-time">
                                                    <c:choose>
                                                        <c:when test="${not empty category.updatedAt}">
                                                            ${dateTimeFormatter.format(category.updatedAt)}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">--</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="action-buttons">
                                                    <a href="${pageContext.request.contextPath}/category/edit?id=${category.id}" 
                                                       class="btn btn-warning btn-sm" title="Chỉnh sửa">
                                                        ✏️ Sửa
                                                    </a>
                                                    <button type="button" 
                                                            class="btn ${category.activeFlag ? 'btn-danger' : 'btn-success'} btn-sm" 
                                                            id="toggle-${category.id}"
                                                            onclick="toggleCategoryStatus(${category.id})" 
                                                            title="${category.activeFlag ? 'Vô hiệu hóa' : 'Kích hoạt'}">
                                                        ${category.activeFlag ? '❌' : '✅'}
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <h3>Không có danh mục nào</h3>
                                <c:choose>
                                    <c:when test="${not empty searchKeyword or not empty status or not empty parentId}">
                                        <p>Không tìm thấy danh mục nào với bộ lọc hiện tại</p>
                                        <a href="${pageContext.request.contextPath}/category/list" class="btn btn-info">← Xem tất cả danh mục</a>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Hãy thêm danh mục đầu tiên của bạn</p>
                                        <a href="${pageContext.request.contextPath}/category/create" class="btn btn-primary">+ Thêm danh mục loại sản phẩm</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Phân trang -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination-container">
                        <ul class="pagination">
                            <!-- First page -->
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="?page=${currentPage-1}&search=${searchKeyword}&status=${status}&parentId=${parentId}&sortField=${sortField}&sortDir=${sortDir}">Trước</a>
                                </li>
                            </c:if>

                            <!-- Page numbers -->
                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <li class="page-item ${i eq currentPage ? 'active' : ''}">
                                    <a class="page-link" href="?page=${i}&search=${searchKeyword}&status=${status}&parentId=${parentId}&sortField=${sortField}&sortDir=${sortDir}">${i}</a>
                                </li>
                            </c:forEach>

                            <!-- Next page -->
                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="?page=${currentPage+1}&search=${searchKeyword}&status=${status}&parentId=${parentId}&sortField=${sortField}&sortDir=${sortDir}">Sau</a>
                                </li>
                            </c:if>
                        </ul>
                    </div>
                </c:if>
            </div>
        </div>

        <script>
            function confirmDelete(categoryName) {
                return confirm('Bạn có chắc chắn muốn xóa danh mục "' + categoryName + '"?\n\nHành động này không thể hoàn tác!');
            }

            // Auto hide alerts after 5 seconds
            document.addEventListener('DOMContentLoaded', function () {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(function (alert) {
                    setTimeout(function () {
                        alert.style.display = 'none';
                    }, 5000);
                });
            });

            // Auto submit form when filter changes
            document.getElementById('status').addEventListener('change', function () {
                document.getElementById('filterForm').submit();
            });

            document.getElementById('parentId').addEventListener('change', function () {
                document.getElementById('filterForm').submit();
            });

            document.getElementById('sortField').addEventListener('change', function () {
                document.getElementById('filterForm').submit();
            });

            document.getElementById('sortDir').addEventListener('change', function () {
                document.getElementById('filterForm').submit();
            });

            // Toggle category status function
            function toggleCategoryStatus(categoryId) {
                fetch('${pageContext.request.contextPath}/category/toggle-status?id=' + categoryId, {
                    method: 'GET'
                })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                // Update status badge
                                const statusBadge = document.getElementById('status-' + categoryId);
                                statusBadge.textContent = data.newStatus;
                                statusBadge.className = 'badge ' + data.statusClass;

                                // Update toggle button
                                const toggleBtn = document.getElementById('toggle-' + categoryId);
                                toggleBtn.textContent = data.buttonText;
                                toggleBtn.className = 'btn ' + data.buttonClass + ' btn-sm';
                                toggleBtn.title = data.newStatus === 'Hoạt động' ? 'Vô hiệu hóa' : 'Kích hoạt';

                                // Show success message
                                showMessage(data.message, 'success');
                            } else {
                                showMessage(data.message, 'error');
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            showMessage('Đã xảy ra lỗi khi cập nhật trạng thái', 'error');
                        });
            }

            // Show message function
            function showMessage(message, type) {
                const alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
                const alertDiv = document.createElement('div');
                alertDiv.className = `alert ${alertClass}`;
                alertDiv.innerHTML = `
            ${message}
                    <button type="button" class="alert-close" onclick="this.parentElement.remove()">&times;</button>
                `;

                // Insert after header
                const header = document.querySelector('.header');
                header.parentNode.insertBefore(alertDiv, header.nextSibling);

                // Auto hide after 5 seconds
                setTimeout(() => {
                    if (alertDiv.parentNode) {
                        alertDiv.remove();
                    }
                }, 5000);
            }
            // Reset filter function
            function resetFilter() {
                window.location.href = '${pageContext.request.contextPath}/category/list';
            }

// Auto-submit form when select changes (optional)
            document.addEventListener('DOMContentLoaded', function () {
                const selects = document.querySelectorAll('.filter-select');
                selects.forEach(select => {
                    select.addEventListener('change', function () {
                        // Uncomment if you want auto-submit
                        // document.getElementById('filterForm').submit();
                    });
                });
            });
        </script>
    </body>
</html>