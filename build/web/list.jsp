<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Danh mục</title>
        <style>
            /* Reset và Base */
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            body {
                background-color: #f8f9fa;
                padding: 20px;
                line-height: 1.6;
                color: #343a40;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 30px;
                background-color: #fff;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
            }

            /* Typography */
            h1 {
                margin-bottom: 30px;
                color: #212529;
                font-weight: 600;
                font-size: 32px;
                border-bottom: 2px solid #6c5ce7;
                padding-bottom: 15px;
                letter-spacing: 0.5px;
            }

            /* Alerts */
            .alert {
                padding: 15px 20px;
                border-radius: 6px;
                margin-bottom: 25px;
                position: relative;
                border-left: 5px solid;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .alert-success {
                background-color: #e3f9e5;
                color: #1b5e20;
                border-left-color: #2ecc71;
            }

            .alert-danger {
                background-color: #fdecea;
                color: #c0392b;
                border-left-color: #e74c3c;
            }

            .alert-close {
                position: absolute;
                right: 15px;
                top: 15px;
                font-size: 20px;
                font-weight: bold;
                cursor: pointer;
                color: inherit;
                opacity: 0.7;
                transition: opacity 0.2s;
            }

            .alert-close:hover {
                opacity: 1;
            }

            /* Layout */
            .row {
                display: flex;
                flex-wrap: wrap;
                margin: 0 -15px;
            }

            .col-md-6 {
                width: 50%;
                padding: 0 15px;
            }

            .mb-3 {
                margin-bottom: 20px;
            }

            .mb-4 {
                margin-bottom: 30px;
            }

            .text-end {
                text-align: right;
            }

            .text-center {
                text-align: center;
            }

            .d-flex {
                display: flex;
            }

            /* Forms */
            .form-control {
                display: block;
                width: 100%;
                padding: 12px 15px;
                font-size: 16px;
                border: 1px solid #ddd;
                border-radius: 6px;
                background-color: #f9f9f9;
                transition: all 0.3s;
                color: #333;
            }

            .form-control:focus {
                outline: none;
                border-color: #6c5ce7;
                box-shadow: 0 0 0 3px rgba(108, 92, 231, 0.2);
                background-color: #fff;
            }

            .me-2 {
                margin-right: 12px;
            }

            /* Buttons */
            .btn {
                display: inline-block;
                font-weight: 500;
                text-align: center;
                white-space: nowrap;
                vertical-align: middle;
                user-select: none;
                border: 1px solid transparent;
                padding: 10px 18px;
                font-size: 16px;
                line-height: 1.5;
                border-radius: 6px;
                cursor: pointer;
                text-decoration: none;
                transition: all 0.3s ease;
                letter-spacing: 0.3px;
            }

            .btn-sm {
                padding: 8px 12px;
                font-size: 14px;
            }

            .btn-primary {
                color: #fff;
                background-color: #6c5ce7;
                border-color: #6c5ce7;
            }

            .btn-primary:hover {
                background-color: #5d4fd1;
                border-color: #5d4fd1;
                box-shadow: 0 4px 8px rgba(108, 92, 231, 0.3);
            }

            .btn-outline-primary {
                color: #6c5ce7;
                background-color: transparent;
                border-color: #6c5ce7;
            }

            .btn-outline-primary:hover {
                color: #fff;
                background-color: #6c5ce7;
                border-color: #6c5ce7;
                box-shadow: 0 4px 8px rgba(108, 92, 231, 0.3);
            }

            .btn-secondary {
                color: #fff;
                background-color: #6c757d;
                border-color: #6c757d;
            }

            .btn-secondary:hover {
                background-color: #5a6268;
                border-color: #545b62;
                box-shadow: 0 4px 8px rgba(108, 117, 125, 0.3);
            }

            .btn-warning {
                color: #212529;
                background-color: #fbc531;
                border-color: #fbc531;
            }

            .btn-warning:hover {
                background-color: #e9b308;
                border-color: #e9b308;
                box-shadow: 0 4px 8px rgba(251, 197, 49, 0.3);
            }

            .btn-danger {
                color: #fff;
                background-color: #e74c3c;
                border-color: #e74c3c;
            }

            .btn-danger:hover {
                background-color: #c0392b;
                border-color: #c0392b;
                box-shadow: 0 4px 8px rgba(231, 76, 60, 0.3);
            }
            
            .btn-admin {
                color: #fff;
                background-color: #3498db;
                border-color: #3498db;
            }
            
            .btn-admin:hover {
                background-color: #2980b9;
                border-color: #2980b9;
                box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
            }

            /* Tables */
            .table-responsive {
                overflow-x: auto;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                margin-bottom: 30px;
            }

            .table {
                width: 100%;
                margin-bottom: 0;
                color: #212529;
                border-collapse: collapse;
                border: 1px solid #e9ecef;
            }

            .table th,
            .table td {
                padding: 15px;
                vertical-align: middle;
                border-top: 1px solid #e9ecef;
            }

            .table thead th {
                vertical-align: bottom;
                background-color: #2c3e50;
                color: #fff;
                font-weight: 600;
                border-bottom: 2px solid #1a252f;
                text-transform: uppercase;
                font-size: 14px;
                letter-spacing: 1px;
            }

            .table-striped tbody tr:nth-of-type(odd) {
                background-color: #f8f9fa;
            }

            .table-hover tbody tr:hover {
                background-color: #e9f0fd;
            }

            .table-dark {
                color: #fff;
                background-color: #343a40;
            }

            .table-dark th {
                border-color: #454d55;
            }

            .text-left {
                text-align: left !important;
            }

            /* Sort Icons */
            .sort-icon {
                display: inline-block;
                margin-left: 5px;
                cursor: pointer;
                font-size: 14px;
            }

            .sort-icon.active {
                color: #fbc531;
            }

            /* Badges */
            .badge {
                display: inline-block;
                padding: 6px 10px;
                font-size: 12px;
                font-weight: 700;
                line-height: 1;
                text-align: center;
                white-space: nowrap;
                vertical-align: baseline;
                border-radius: 30px;
                letter-spacing: 0.5px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }

            .bg-success {
                background-color: #2ecc71;
                color: white;
            }

            .bg-danger {
                background-color: #e74c3c;
                color: white;
            }

            /* Modals */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0, 0, 0, 0.5);
                animation: fadeIn 0.3s;
            }

            @keyframes fadeIn {
                from {opacity: 0}
                to {opacity: 1}
            }

            .modal-dialog {
                position: relative;
                width: auto;
                margin: 10% auto;
                max-width: 500px;
                animation: slideDown 0.3s;
            }

            @keyframes slideDown {
                from {transform: translateY(-50px); opacity: 0;}
                to {transform: translateY(0); opacity: 1;}
            }

            .modal-content {
                position: relative;
                display: flex;
                flex-direction: column;
                background-color: #fff;
                border-radius: 8px;
                outline: 0;
                box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
                overflow: hidden;
            }

            .modal-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 20px;
                border-bottom: 1px solid #e9ecef;
                background-color: #f8f9fa;
            }

            .modal-title {
                margin: 0;
                font-size: 20px;
                color: #2c3e50;
                font-weight: 600;
            }

            .modal-body {
                position: relative;
                flex: 1 1 auto;
                padding: 25px;
                font-size: 16px;
                line-height: 1.6;
            }

            .modal-footer {
                display: flex;
                justify-content: flex-end;
                padding: 20px;
                border-top: 1px solid #e9ecef;
                background-color: #f8f9fa;
            }

            .modal-footer > * {
                margin: 0 5px;
            }

            .btn-close {
                background: transparent;
                border: 0;
                font-size: 24px;
                font-weight: 700;
                line-height: 1;
                color: #6c757d;
                cursor: pointer;
                transition: color 0.2s;
            }

            .btn-close:hover {
                color: #343a40;
            }

            /* Pagination */
            .pagination {
                display: flex;
                padding-left: 0;
                list-style: none;
                border-radius: 6px;
            }

            .justify-content-center {
                justify-content: center;
            }

            .page-item {
                margin: 0 3px;
            }

            .page-item.active .page-link {
                background-color: #6c5ce7;
                border-color: #6c5ce7;
                color: white;
                box-shadow: 0 4px 8px rgba(108, 92, 231, 0.3);
            }

            .page-item.disabled .page-link {
                color: #6c757d;
                pointer-events: none;
                cursor: default;
                background-color: #fff;
                border-color: #dee2e6;
            }

            .page-link {
                position: relative;
                display: block;
                padding: 10px 15px;
                margin-left: -1px;
                line-height: 1.25;
                color: #6c5ce7;
                background-color: #fff;
                border: 1px solid #dee2e6;
                text-decoration: none;
                border-radius: 6px;
                transition: all 0.3s;
            }

            .page-link:hover {
                z-index: 2;
                color: #fff;
                text-decoration: none;
                background-color: #6c5ce7;
                border-color: #6c5ce7;
                box-shadow: 0 4px 8px rgba(108, 92, 231, 0.3);
            }

            /* Icons */
            .icon {
                display: inline-block;
                margin-right: 5px;
                font-size: 16px;
                line-height: 1;
            }

            .icon-plus-circle:before {
                content: "+";
            }

            .icon-pencil-square:before {
                content: "✎";
            }

            .icon-trash:before {
                content: "🗑";
            }
            
            .icon-admin:before {
                content: "👤";
            }
            
            .icon-sort:before {
                content: "⇕";
            }
            
            .icon-sort-asc:before {
                content: "↑";
            }
            
            .icon-sort-desc:before {
                content: "↓";
            }
            
            /* Action buttons */
            .action-buttons {
                display: flex;
                justify-content: space-between;
                margin-top: 30px;
                align-items: center;
            }
            
            .left-buttons, .right-buttons {
                display: flex;
                gap: 12px;
            }
            
            /* Filter panel */
            .filter-panel {
                background-color: #f8f9fa;
                border: 1px solid #e9ecef;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 25px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            }
            
            .filter-title {
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 15px;
                color: #2c3e50;
                border-bottom: 1px solid #e9ecef;
                padding-bottom: 10px;
            }
            
            .filter-row {
                display: flex;
                flex-wrap: wrap;
                gap: 15px;
                align-items: center;
            }
            
            .filter-item {
                flex: 1;
                min-width: 200px;
            }
            
            .filter-label {
                display: block;
                margin-bottom: 8px;
                font-weight: 500;
                color: #495057;
            }
            
            .filter-buttons {
                display: flex;
                justify-content: flex-end;
                margin-top: 20px;
                gap: 10px;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .container {
                    padding: 20px;
                }
                
                .col-md-6 {
                    width: 100%;
                    margin-bottom: 15px;
                }

                .text-end {
                    text-align: left;
                }
                
                .action-buttons {
                    flex-direction: column;
                    gap: 15px;
                }
                
                .left-buttons, .right-buttons {
                    justify-content: center;
                }
                
                .filter-row {
                    flex-direction: column;
                    gap: 20px;
                }
                
                .filter-item {
                    width: 100%;
                }
                
                .table th, .table td {
                    padding: 10px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1 class="mb-4">Quản lý Danh mục</h1>

            <!-- Thông báo -->
            <c:if test="${param.message != null}">
                <div class="alert alert-success" id="successAlert">
                    <c:choose>
                        <c:when test="${param.message eq 'create_success'}">Thêm danh mục thành công!</c:when>
                        <c:when test="${param.message eq 'update_success'}">Cập nhật danh mục thành công!</c:when>
                        <c:when test="${param.message eq 'delete_success'}">Xóa danh mục thành công!</c:when>
                    </c:choose>
                    <span class="alert-close" onclick="this.parentElement.style.display = 'none'">&times;</span>
                </div>
            </c:if>

            <c:if test="${param.error != null}">
                <div class="alert alert-danger" id="errorAlert">
                    <c:choose>
                        <c:when test="${param.error eq 'invalid_id'}">ID danh mục không hợp lệ!</c:when>
                        <c:when test="${param.error eq 'category_not_found'}">Không tìm thấy danh mục!</c:when>
                        <c:when test="${param.error eq 'delete_failed'}">Không thể xóa danh mục! Danh mục đang được sử dụng.</c:when>
                        <c:when test="${param.error eq 'invalid_data'}">Dữ liệu không hợp lệ!</c:when>
                    </c:choose>
                    <span class="alert-close" onclick="this.parentElement.style.display = 'none'">&times;</span>
                </div>
            </c:if>

            <!-- Bộ lọc và sắp xếp -->
            <div class="filter-panel">
                <div class="filter-title">Tìm kiếm & Sắp xếp</div>
                <div class="filter-row">
                    <div class="filter-item">
                        <label class="filter-label">Tìm kiếm danh mục</label>
                        <form action="${pageContext.request.contextPath}/category/list" method="get" class="d-flex">
                            <input type="text" name="search" value="${searchKeyword}" class="form-control me-2" placeholder="Nhập tên danh mục...">
                            <input type="hidden" name="sortField" value="${param.sortField || 'id'}">
                            <input type="hidden" name="sortDir" value="${param.sortDir || 'asc'}">
                            <button type="submit" class="btn btn-outline-primary">Tìm kiếm</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Nút thêm mới -->
            <div class="action-buttons mb-3">
                <div class="left-buttons">
                    <a href="${pageContext.request.contextPath}/category/create" class="btn btn-primary">
                        <span class="icon icon-plus-circle"></span> Thêm danh mục mới
                    </a>
                </div>
            </div>

            <!-- Bảng danh mục -->
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th style="text-align: left;">
                                ID
                                <a href="${pageContext.request.contextPath}/category/list?search=${searchKeyword}&sortField=id&sortDir=${param.sortField eq 'id' && param.sortDir eq 'asc' ? 'desc' : 'asc'}" class="sort-icon ${param.sortField eq 'id' ? 'active' : ''}">
                                    <span class="icon ${param.sortField eq 'id' && param.sortDir eq 'asc' ? 'icon-sort-asc' : param.sortField eq 'id' && param.sortDir eq 'desc' ? 'icon-sort-desc' : 'icon-sort'}"></span>
                                </a>
                            </th>
                            <th style="text-align: left;">
                                Tên danh mục
                                <a href="${pageContext.request.contextPath}/category/list?search=${searchKeyword}&sortField=name&sortDir=${param.sortField eq 'name' && param.sortDir eq 'asc' ? 'desc' : 'asc'}" class="sort-icon ${param.sortField eq 'name' ? 'active' : ''}">
                                    <span class="icon ${param.sortField eq 'name' && param.sortDir eq 'asc' ? 'icon-sort-asc' : param.sortField eq 'name' && param.sortDir eq 'desc' ? 'icon-sort-desc' : 'icon-sort'}"></span>
                                </a>
                            </th>
                            <th style="text-align: left;">Danh mục cha</th>
                            <th style="text-align: left;">Trạng thái</th>
                            <th style="text-align: left;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${categories}" var="category">
                            <tr>
                                <td>${category.id}</td>
                                <td>${category.name}</td>
                                <td>
                                    <c:forEach items="${categories}" var="parentCat">
                                        <c:if test="${category.parentId eq parentCat.id}">
                                            ${parentCat.name}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${category.activeFlag}">
                                            <span class="badge bg-success">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Không hoạt động</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/category/edit?id=${category.id}" class="btn btn-sm btn-warning">
                                        <span class="icon icon-pencil-square"></span> Sửa
                                    </a>
                                    <button type="button" class="btn btn-sm btn-danger" onclick="openModal('deleteModal${category.id}')">
                                        <span class="icon icon-trash"></span> Xóa
                                    </button>

                                    <!-- Modal xác nhận xóa -->
                                    <div id="deleteModal${category.id}" class="modal">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title">Xác nhận xóa</h5>
                                                    <button type="button" class="btn-close" onclick="closeModal('deleteModal${category.id}')">&times;</button>
                                                </div>
                                                <div class="modal-body">
                                                    <p>Bạn có chắc chắn muốn xóa danh mục <strong>${category.name}</strong> không?</p>
                                                    <p>Hành động này không thể hoàn tác.</p>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" onclick="closeModal('deleteModal${category.id}')">Hủy bỏ</button>
                                                    <a href="${pageContext.request.contextPath}/category/delete?id=${category.id}" class="btn btn-danger">Xác nhận xóa</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty categories}">
                            <tr>
                                <td colspan="5" class="text-center">Không có danh mục nào được tìm thấy</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Phân trang -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/category/list?page=${currentPage - 1}&search=${searchKeyword}&sortField=${param.sortField || 'id'}&sortDir=${param.sortDir || 'asc'}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/category/list?page=${i}&search=${searchKeyword}&sortField=${param.sortField || 'id'}&sortDir=${param.sortDir || 'asc'}">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/category/list?page=${currentPage + 1}&search=${searchKeyword}&sortField=${param.sortField || 'id'}&sortDir=${param.sortDir || 'asc'}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
            
            <!-- Nút quay lại -->
            <div class="action-buttons">
                <div class="left-buttons">
                    <button type="button" class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/category/list'">
                        Làm mới
                    </button>
                </div>
                <div class="right-buttons">
                    <a href="${pageContext.request.contextPath}/admin" class="btn btn-admin">
                        <span class="icon icon-admin"></span> Quay lại Trang Quản trị
                    </a>
                </div>
            </div>
        </div>

        <script>
            // Xử lý đóng alert sau 5 giây
            window.addEventListener('DOMContentLoaded', function () {
                setTimeout(function () {
                    var successAlert = document.getElementById('successAlert');
                    var errorAlert = document.getElementById('errorAlert');

                    if (successAlert) {
                        successAlert.style.display = 'none';
                    }

                    if (errorAlert) {
                        errorAlert.style.display = 'none';
                    }
                }, 5000);
            });

            // Xử lý modal
            function openModal(modalId) {
                document.getElementById(modalId).style.display = 'block';
                document.body.style.overflow = 'hidden'; // Ngăn cuộn trang khi modal mở
            }

            function closeModal(modalId) {
                document.getElementById(modalId).style.display = 'none';
                document.body.style.overflow = ''; // Cho phép cuộn trang khi modal đóng
            }

            // Đóng modal khi click bên ngoài
            window.onclick = function (event) {
                if (event.target.className === 'modal') {
                    event.target.style.display = 'none';
                    document.body.style.overflow = ''; // Cho phép cuộn trang khi modal đóng
                }
            };
        </script>
    </body>
</html>
