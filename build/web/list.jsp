<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý danh mục</title>
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                font-family: Arial, sans-serif;
            }

            body {
                background-color: #f5f5f5;
                padding: 20px;
                line-height: 1.6;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }

            h1 {
                margin-bottom: 20px;
                color: #333;
            }

            .alert {
                padding: 15px;
                border-radius: 4px;
                margin-bottom: 20px;
                position: relative;
            }

            .alert-success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }

            .alert-danger {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }

            .alert-close {
                position: absolute;
                right: 10px;
                top: 10px;
                font-size: 20px;
                font-weight: bold;
                cursor: pointer;
            }

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
                margin-bottom: 15px;
            }

            .mb-4 {
                margin-bottom: 20px;
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

            .form-control {
                display: block;
                width: 100%;
                padding: 10px;
                font-size: 16px;
                border: 1px solid #ced4da;
                border-radius: 4px;
            }

            .me-2 {
                margin-right: 10px;
            }

            .btn {
                display: inline-block;
                font-weight: 400;
                text-align: center;
                white-space: nowrap;
                vertical-align: middle;
                user-select: none;
                border: 1px solid transparent;
                padding: 8px 16px;
                font-size: 16px;
                line-height: 1.5;
                border-radius: 4px;
                cursor: pointer;
                text-decoration: none;
            }

            .btn-sm {
                padding: 5px 10px;
                font-size: 14px;
            }

            .btn-primary {
                color: #fff;
                background-color: #007bff;
                border-color: #007bff;
            }

            .btn-primary:hover {
                background-color: #0069d9;
                border-color: #0062cc;
            }

            .btn-outline-primary {
                color: #007bff;
                background-color: transparent;
                border-color: #007bff;
            }

            .btn-outline-primary:hover {
                color: #fff;
                background-color: #007bff;
                border-color: #007bff;
            }

            .btn-secondary {
                color: #fff;
                background-color: #6c757d;
                border-color: #6c757d;
            }

            .btn-secondary:hover {
                background-color: #5a6268;
                border-color: #545b62;
            }

            .btn-warning {
                color: #212529;
                background-color: #ffc107;
                border-color: #ffc107;
            }

            .btn-warning:hover {
                background-color: #e0a800;
                border-color: #d39e00;
            }

            .btn-danger {
                color: #fff;
                background-color: #dc3545;
                border-color: #dc3545;
            }

            .btn-danger:hover {
                background-color: #c82333;
                border-color: #bd2130;
            }
            
            .btn-admin {
                color: #fff;
                background-color: #28a745;
                border-color: #28a745;
            }
            
            .btn-admin:hover {
                background-color: #218838;
                border-color: #1e7e34;
            }

            .table-responsive {
                overflow-x: auto;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }

            .table {
                width: 100%;
                margin-bottom: 1rem;
                color: #212529;
                border-collapse: collapse;
            }

            .table th,
            .table td {
                padding: 12px;
                vertical-align: top;
                border-top: 1px solid #dee2e6;
            }

            .table thead th {
                vertical-align: bottom;
                border-bottom: 2px solid #dee2e6;
            }

            .table-striped tbody tr:nth-of-type(odd) {
                background-color: rgba(0, 0, 0, 0.05);
            }

            .table-hover tbody tr:hover {
                background-color: rgba(0, 0, 0, 0.075);
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

            .badge {
                display: inline-block;
                padding: 0.25em 0.6em;
                font-size: 75%;
                font-weight: 700;
                line-height: 1;
                text-align: center;
                white-space: nowrap;
                vertical-align: baseline;
                border-radius: 0.25rem;
            }

            .bg-success {
                background-color: #28a745;
                color: white;
            }

            .bg-danger {
                background-color: #dc3545;
                color: white;
            }

            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0, 0, 0, 0.4);
            }

            .modal-dialog {
                position: relative;
                width: auto;
                margin: 10% auto;
                max-width: 500px;
            }

            .modal-content {
                position: relative;
                display: flex;
                flex-direction: column;
                background-color: #fff;
                border-radius: 0.3rem;
                outline: 0;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.5);
            }

            .modal-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 1rem;
                border-bottom: 1px solid #dee2e6;
            }

            .modal-title {
                margin: 0;
                font-size: 1.25rem;
            }

            .modal-body {
                position: relative;
                flex: 1 1 auto;
                padding: 1rem;
            }

            .modal-footer {
                display: flex;
                justify-content: flex-end;
                padding: 1rem;
                border-top: 1px solid #dee2e6;
            }

            .modal-footer > * {
                margin: 0 0.25rem;
            }

            .btn-close {
                background: transparent;
                border: 0;
                font-size: 1.5rem;
                font-weight: 700;
                line-height: 1;
                color: #000;
                cursor: pointer;
            }

            .pagination {
                display: flex;
                padding-left: 0;
                list-style: none;
                border-radius: 0.25rem;
            }

            .justify-content-center {
                justify-content: center;
            }

            .page-item {
                margin: 0 2px;
            }

            .page-item.active .page-link {
                background-color: #007bff;
                border-color: #007bff;
                color: white;
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
                padding: 0.5rem 0.75rem;
                margin-left: -1px;
                line-height: 1.25;
                color: #007bff;
                background-color: #fff;
                border: 1px solid #dee2e6;
                text-decoration: none;
            }

            .page-link:hover {
                z-index: 2;
                color: #0056b3;
                text-decoration: none;
                background-color: #e9ecef;
                border-color: #dee2e6;
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
            
            .action-buttons {
                display: flex;
                justify-content: space-between;
                margin-top: 20px;
            }
            
            .left-buttons, .right-buttons {
                display: flex;
                gap: 10px;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .col-md-6 {
                    width: 100%;
                    margin-bottom: 15px;
                }

                .text-end {
                    text-align: left;
                }
                
                .action-buttons {
                    flex-direction: column;
                    gap: 10px;
                }
                
                .left-buttons, .right-buttons {
                    justify-content: center;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1 class="mb-4">Quản lý danh mục</h1>

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

            <!-- Tìm kiếm và thêm mới -->
            <div class="row mb-3">
                <div class="col-md-6">
                    <form action="${pageContext.request.contextPath}/category/list" method="get" class="d-flex">
                        <input type="text" name="search" value="${searchKeyword}" class="form-control me-2" placeholder="Tìm kiếm danh mục...">
                        <button type="submit" class="btn btn-outline-primary">Tìm kiếm</button>
                    </form>
                </div>
                <div class="col-md-6 text-end">
                    <a href="${pageContext.request.contextPath}/category/create" class="btn btn-primary">
                        <span class="icon icon-plus-circle"></span> Thêm danh mục
                    </a>
                </div>
            </div>

            <!-- Bảng danh mục -->
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th style="text-align: left;">ID</th>
                            <th style="text-align: left;">Tên danh mục</th>
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
                                                    Bạn có chắc chắn muốn xóa danh mục <strong>${category.name}</strong> không?
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" onclick="closeModal('deleteModal${category.id}')">Hủy</button>
                                                    <a href="${pageContext.request.contextPath}/category/delete?id=${category.id}" class="btn btn-danger">Xóa</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty categories}">
                            <tr>
                                <td colspan="5" class="text-center">Không có danh mục nào</td>
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
                            <a class="page-link" href="${pageContext.request.contextPath}/category/list?page=${currentPage - 1}&search=${searchKeyword}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/category/list?page=${i}&search=${searchKeyword}">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/category/list?page=${currentPage + 1}&search=${searchKeyword}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
            
            <!-- Nút quay lại -->
                <div class="right-buttons">
                    <a href="${pageContext.request.contextPath}/admin" class="btn btn-admin">
                         Quay lại Trang Admin
                    </a>
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
            }

            function closeModal(modalId) {
                document.getElementById(modalId).style.display = 'none';
            }

            // Đóng modal khi click bên ngoài
            window.onclick = function (event) {
                if (event.target.className === 'modal') {
                    event.target.style.display = 'none';
                }
            }
        </script>
    </body>
</html>