<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý danh mục & đơn vị tính</title>
        <!-- Font Awesome (giữ lại nếu cần) -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <style>
            /* Reset CSS */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: Arial, sans-serif;
                line-height: 1.6;
                color: #333;
                background-color: #f4f4f4;
            }

            .container-fluid {
                width: 95%;
                margin: 0 auto;
                padding: 15px;
            }

            /* Navbar styles */
            .navbar {
                background-color: #333;
                color: white;
                padding: 10px 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .navbar-brand {
                color: white;
                font-size: 24px;
                text-decoration: none;
                font-weight: bold;
            }

            .navbar-nav {
                display: flex;
                list-style: none;
            }

            .nav-item {
                margin-left: 20px;
            }

            .nav-link {
                color: white;
                text-decoration: none;
                padding: 5px 10px;
            }

            .nav-link:hover {
                background-color: #555;
                border-radius: 3px;
            }

            /* Headers */
            h1, h5 {
                margin-bottom: 15px;
                color: #333;
            }

            /* Alerts */
            .alert {
                padding: 10px 15px;
                margin-bottom: 15px;
                border-radius: 4px;
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

            .close {
                position: absolute;
                top: 5px;
                right: 10px;
                cursor: pointer;
                font-size: 20px;
                font-weight: bold;
            }

            /* Tabs */
            .nav-tabs {
                display: flex;
                border-bottom: 1px solid #dee2e6;
                margin-bottom: 20px;
                list-style: none;
            }

            .nav-item {
                margin-bottom: -1px;
            }

            .nav-link {
                display: block;
                padding: 10px 15px;
                border: 1px solid transparent;
                border-top-left-radius: 4px;
                border-top-right-radius: 4px;
                color: #333;
                text-decoration: none;
            }

            .nav-link.active {
                background-color: #fff;
                border-color: #dee2e6 #dee2e6 #fff;
            }

            .tab-content {
                padding: 20px 0;
            }

            .tab-pane {
                display: none;
            }

            .tab-pane.active {
                display: block;
            }

            /* Cards */
            .card {
                background-color: white;
                border-radius: 4px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }

            .card-header {
                padding: 10px 15px;
                background-color: #f8f9fa;
                border-bottom: 1px solid #dee2e6;
            }

            .card-body {
                padding: 15px;
            }

            /* Forms */
            .form-group {
                margin-bottom: 15px;
            }

            label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
            }

            .form-control {
                width: 100%;
                padding: 8px 10px;
                border: 1px solid #ced4da;
                border-radius: 4px;
                font-size: 16px;
            }

            .form-check {
                margin-right: 15px;
                display: inline-block;
            }

            /* Buttons */
            .btn {
                display: inline-block;
                font-weight: 400;
                text-align: center;
                white-space: nowrap;
                vertical-align: middle;
                user-select: none;
                border: 1px solid transparent;
                padding: 8px 12px;
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

            .btn-secondary {
                color: #fff;
                background-color: #6c757d;
                border-color: #6c757d;
            }

            .btn-danger {
                color: #fff;
                background-color: #dc3545;
                border-color: #dc3545;
            }

            /* Tables */
            .table-responsive {
                overflow-x: auto;
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
                text-align: left;
            }

            .table thead th {
                vertical-align: bottom;
                border-bottom: 2px solid #dee2e6;
                background-color: #f8f9fa;
            }

            .table-striped tbody tr:nth-of-type(odd) {
                background-color: rgba(0, 0, 0, 0.05);
            }

            .table-bordered {
                border: 1px solid #dee2e6;
            }

            .table-bordered th,
            .table-bordered td {
                border: 1px solid #dee2e6;
            }

            /* Badges */
            .badge {
                display: inline-block;
                padding: 3px 7px;
                font-size: 12px;
                font-weight: 700;
                line-height: 1;
                text-align: center;
                white-space: nowrap;
                vertical-align: baseline;
                border-radius: 10px;
            }

            .badge-success {
                background-color: #28a745;
                color: white;
            }

            .badge-danger {
                background-color: #dc3545;
                color: white;
            }

            .badge-info {
                background-color: #17a2b8;
                color: white;
            }

            /* Modal */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0,0,0,0.4);
            }

            .modal-dialog {
                margin: 30px auto;
                max-width: 500px;
            }

            .modal-content {
                position: relative;
                background-color: #fefefe;
                border: 1px solid #888;
                border-radius: 5px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            }

            .modal-header {
                padding: 15px;
                border-bottom: 1px solid #dee2e6;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .modal-body {
                padding: 15px;
            }

            .modal-footer {
                padding: 15px;
                border-top: 1px solid #dee2e6;
                display: flex;
                justify-content: flex-end;
            }

            /* Layout */
            .row {
                display: flex;
                flex-wrap: wrap;
                margin-right: -15px;
                margin-left: -15px;
            }

            .col-md-4 {
                flex: 0 0 33.333333%;
                max-width: 33.333333%;
                padding-right: 15px;
                padding-left: 15px;
            }

            .col-md-8 {
                flex: 0 0 66.666667%;
                max-width: 66.666667%;
                padding-right: 15px;
                padding-left: 15px;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .col-md-4, .col-md-8 {
                    flex: 0 0 100%;
                    max-width: 100%;
                }

                .navbar {
                    flex-direction: column;
                }

                .navbar-nav {
                    margin-top: 10px;
                }
            }
        </style>
    </head>
    <body>
        <!-- Navbar -->
        <nav class="navbar">
            <a href="#" class="navbar-brand">Hệ thống quản lý</a>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" href="dashboard">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="category">Danh mục</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="product">Sản phẩm</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="order">Đơn hàng</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="logout">Đăng xuất</a>
                </li>
            </ul>
        </nav>

        <div class="container-fluid">
            <h1>Quản lý danh mục & đơn vị tính</h1>

            <!-- Hiển thị thông báo lỗi nếu có -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    ${errorMessage}
                    <span class="close" onclick="this.parentElement.style.display = 'none';">&times;</span>
                </div>
            </c:if>

            <!-- Hiển thị thông báo thành công nếu có -->
            <c:if test="${param.success eq 'add_category'}">
                <div class="alert alert-success">
                    Thêm danh mục thành công!
                    <span class="close" onclick="this.parentElement.style.display = 'none';">&times;</span>
                </div>
            </c:if>
            <c:if test="${param.success eq 'update_category'}">
                <div class="alert alert-success">
                    Cập nhật danh mục thành công!
                    <span class="close" onclick="this.parentElement.style.display = 'none';">&times;</span>
                </div>
            </c:if>
            <c:if test="${param.success eq 'add_unit'}">
                <div class="alert alert-success">
                    Thêm đơn vị tính thành công!
                    <span class="close" onclick="this.parentElement.style.display = 'none';">&times;</span>
                </div>
            </c:if>
            <c:if test="${param.success eq 'update_unit'}">
                <div class="alert alert-success">
                    Cập nhật đơn vị tính thành công!
                    <span class="close" onclick="this.parentElement.style.display = 'none';">&times;</span>
                </div>
            </c:if>

            <!-- Tab Navigation -->
            <ul class="nav-tabs" id="myTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="category-tab" href="#category" role="tab">
                        <i class="fas fa-folder"></i> Danh mục
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="parent-category-tab" href="#parent-category" role="tab">
                        <i class="fas fa-folder-open"></i> Danh mục cha
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="unit-tab" href="#unit" role="tab">
                        <i class="fas fa-ruler"></i> Đơn vị tính
                    </a>
                </li>
            </ul>

            <!-- Tab Content -->
            <div class="tab-content" id="myTabContent">
                <!-- Category Tab -->
                <div class="tab-pane active" id="category" role="tabpanel">
                    <!-- Nội dung tab danh mục (giữ nguyên) -->
                    <div class="row">
                        <!-- Form thêm danh mục -->
                        <div class="col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Thêm danh mục mới</h5>
                                </div>
                                <div class="card-body">
                                    <form action="category" method="POST">
                                        <input type="hidden" name="action" value="add">
                                        <div class="form-group">
                                            <label for="categoryName">Tên danh mục *</label>
                                            <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="parentCategory">Danh mục cha</label>
                                            <select class="form-control" id="parentCategory" name="parentCategory">
                                                <option value="0">-- Không có danh mục cha --</option>
                                                <c:forEach items="${parentCategories}" var="parent">
                                                    <option value="${parent.id}">${parent.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Trạng thái</label>
                                            <div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="status" id="statusActive" value="1" checked>
                                                    <label class="form-check-label" for="statusActive">Hoạt động</label>
                                                </div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="status" id="statusInactive" value="0">
                                                    <label class="form-check-label" for="statusInactive">Vô hiệu hóa</label>
                                                </div>
                                            </div>
                                        </div>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> Lưu
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Bảng hiển thị danh sách danh mục -->
                        <div class="col-md-8">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Danh sách danh mục</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped" id="categoryTable">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Tên danh mục</th>
                                                    <th>Danh mục cha</th>
                                                    <th>Trạng thái</th>
                                                    <th>Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${categories}" var="category">
                                                    <tr class="category-row" data-parent="${category.parentId}" data-id="${category.id}">
                                                        <td>${category.id}</td>
                                                        <td>
                                                            <c:if test="${category.parentId != 0}">
                                                                <span class="ml-4">└─ </span>
                                                            </c:if>
                                                            ${category.name}
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${empty category.parentName}">
                                                                    <span class="text-muted">Không có</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${category.parentName}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <span class="badge ${category.activeFlag == 1 ? 'badge-success' : 'badge-danger'}">
                                                                ${category.activeFlag == 1 ? 'Hoạt động' : 'Vô hiệu hóa'}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <button class="btn btn-sm btn-primary edit-category-btn" data-id="${category.id}">
                                                                <i class="fas fa-edit"></i> Sửa
                                                            </button>
                                                            <button class="btn btn-sm btn-danger delete-category-btn" data-id="${category.id}">
                                                                <i class="fas fa-trash"></i> Xóa
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Parent Category Tab -->
                <div class="tab-pane" id="parent-category" role="tabpanel">
                    <!-- Nội dung tab danh mục cha (giữ nguyên) -->
                    <div class="row">
                        <!-- Form thêm danh mục cha -->
                        <div class="col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Thêm danh mục cha mới</h5>
                                </div>
                                <div class="card-body">
                                    <form action="category" method="POST">
                                        <input type="hidden" name="action" value="add_parent">
                                        <input type="hidden" name="parentCategory" value="0">
                                        <div class="form-group">
                                            <label for="parentCategoryName">Tên danh mục cha *</label>
                                            <input type="text" class="form-control" id="parentCategoryName" name="categoryName" required>
                                        </div>
                                        <div class="form-group">
                                            <label>Trạng thái</label>
                                            <div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="status" id="parentStatusActive" value="1" checked>
                                                    <label class="form-check-label" for="parentStatusActive">Hoạt động</label>
                                                </div>
                                                <div class="form-check">
                                                    <input class="form-check-input" type="radio" name="status" id="parentStatusInactive" value="0">
                                                    <label class="form-check-label" for="parentStatusInactive">Vô hiệu hóa</label>
                                                </div>
                                            </div>
                                        </div>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> Lưu
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Bảng hiển thị danh sách danh mục cha -->
                        <div class="col-md-8">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Danh sách danh mục cha</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped" id="parentCategoryTable">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Tên danh mục cha</th>
                                                    <th>Trạng thái</th>
                                                    <th>Số danh mục con</th>
                                                    <th>Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${parentCategoriesWithCount}" var="parent">
                                                    <tr>
                                                        <td>${parent.id}</td>
                                                        <td>${parent.name}</td>
                                                        <td>
                                                            <span class="badge ${parent.activeFlag == 1 ? 'badge-success' : 'badge-danger'}">
                                                                ${parent.activeFlag == 1 ? 'Hoạt động' : 'Vô hiệu hóa'}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <span class="badge badge-info">${parent.childCount}</span>
                                                        </td>
                                                        <td>
                                                            <button class="btn btn-sm btn-primary edit-parent-btn" data-id="${parent.id}">
                                                                <i class="fas fa-edit"></i> Sửa
                                                            </button>
                                                            <button class="btn btn-sm btn-danger delete-parent-btn" data-id="${parent.id}" data-count="${parent.childCount}">
                                                                <i class="fas fa-trash"></i> Xóa
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Unit Tab -->
                <div class="tab-pane" id="unit" role="tabpanel">
                    <!-- Nội dung tab đơn vị tính (giữ nguyên) -->
                    <div class="row">
                        <!-- Form thêm đơn vị tính -->
                        <div class="col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Thêm đơn vị tính mới</h5>
                                </div>
                                <div class="card-body">
                                    <form action="category" method="POST">
                                        <input type="hidden" name="action" value="add_unit">
                                        <div class="form-group">
                                            <label for="unitName">Tên đơn vị tính *</label>
                                            <input type="text" class="form-control" id="unitName" name="unitName" required>
                                        </div>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> Lưu
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Bảng hiển thị danh sách đơn vị tính -->
                        <div class="col-md-8">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Danh sách đơn vị tính</h5>
                                </div>
                                <div class="card-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-striped" id="unitTable">
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Tên đơn vị tính</th>
                                                    <th>Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${units}" var="unit">
                                                    <tr>
                                                        <td>${unit.id}</td>
                                                        <td>${unit.name}</td>
                                                        <td>
                                                            <button class="btn btn-sm btn-primary edit-unit-btn" data-id="${unit.id}">
                                                                <i class="fas fa-edit"></i> Sửa
                                                            </button>
                                                            <button class="btn btn-sm btn-danger delete-unit-btn" data-id="${unit.id}">
                                                                <i class="fas fa-trash"></i> Xóa
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal sửa danh mục -->
        <div class="modal" id="editCategoryModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Sửa danh mục</h5>
                        <button type="button" class="close" onclick="closeModal('editCategoryModal')">&times;</button>
                    </div>
                    <form id="editCategoryForm" action="category" method="POST">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" id="editCategoryId" name="categoryId">
                            <div class="form-group">
                                <label for="editCategoryName">Tên danh mục *</label>
                                <input type="text" class="form-control" id="editCategoryName" name="categoryName" required>
                            </div>
                            <div class="form-group">
                                <label for="editParentCategory">Danh mục cha</label>
                                <select class="form-control" id="editParentCategory" name="parentCategory">
                                    <option value="0">-- Không có danh mục cha --</option>
                                    <c:forEach items="${parentCategories}" var="parent">
                                        <option value="${parent.id}">${parent.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Trạng thái</label>
                                <div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="status" id="editStatusActive" value="1">
                                        <label class="form-check-label" for="editStatusActive">Hoạt động</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="status" id="editStatusInactive" value="0">
                                        <label class="form-check-label" for="editStatusInactive">Vô hiệu hóa</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" onclick="closeModal('editCategoryModal')">Đóng</button>
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal sửa danh mục cha -->
        <div class="modal" id="editParentModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Sửa danh mục cha</h5>
                        <button type="button" class="close" onclick="closeModal('editParentModal')">&times;</button>
                    </div>
                    <form id="editParentForm" action="category" method="POST">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="update_parent">
                            <input type="hidden" id="editParentId" name="categoryId">
                            <input type="hidden" name="parentCategory" value="0">
                            <div class="form-group">
                                <label for="editParentName">Tên danh mục cha *</label>
                                <input type="text" class="form-control" id="editParentName" name="categoryName" required>
                            </div>
                            <div class="form-group">
                                <label>Trạng thái</label>
                                <div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="status" id="editParentStatusActive" value="1">
                                        <label class="form-check-label" for="editParentStatusActive">Hoạt động</label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="status" id="editParentStatusInactive" value="0">
                                        <label class="form-check-label" for="editParentStatusInactive">Vô hiệu hóa</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" onclick="closeModal('editParentModal')">Đóng</button>
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal sửa đơn vị tính -->
        <div class="modal" id="editUnitModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Sửa đơn vị tính</h5>
                        <button type="button" class="close" onclick="closeModal('editUnitModal')">&times;</button>
                    </div>
                    <form id="editUnitForm" action="category" method="POST">
                        <div class="modal-body">
                            <input type="hidden" name="action" value="update_unit">
                            <input type="hidden" id="editUnitId" name="unitId">
                            <div class="form-group">
                                <label for="editUnitName">Tên đơn vị tính *</label>
                                <input type="text" class="form-control" id="editUnitName" name="unitName" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" onclick="closeModal('editUnitModal')">Đóng</button>
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- JavaScript -->
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script>
                                // Hàm đóng modal
                                function closeModal(modalId) {
                                    document.getElementById(modalId).style.display = 'none';
                                }

                                // Hàm hiển thị modal
                                function showModal(modalId) {
                                    document.getElementById(modalId).style.display = 'block';
                                }

                                // Xử lý tab
                                document.addEventListener('DOMContentLoaded', function () {
                                    // Lấy tất cả các tab và tab content
                                    const tabs = document.querySelectorAll('.nav-link');
                                    const tabContents = document.querySelectorAll('.tab-pane');

                                    // Thêm sự kiện click cho mỗi tab
                                    tabs.forEach(tab => {
                                        tab.addEventListener('click', function (e) {
                                            e.preventDefault();

                                            // Loại bỏ active class từ tất cả tabs
                                            tabs.forEach(t => t.classList.remove('active'));

                                            // Thêm active class cho tab được click
                                            this.classList.add('active');

                                            // Lấy target tab content
                                            const targetId = this.getAttribute('href').substring(1);

                                            // Ẩn tất cả tab content
                                            tabContents.forEach(content => {
                                                content.classList.remove('active');
                                            });

                                            // Hiển thị tab content được chọn
                                            document.getElementById(targetId).classList.add('active');

                                            // Cập nhật URL (tùy chọn)
                                            const tabId = this.id;
                                            if (tabId === 'unit-tab') {
                                                history.replaceState(null, null, '?tab=unit');
                                            } else if (tabId === 'parent-category-tab') {
                                                history.replaceState(null, null, '?tab=parent');
                                            } else {
                                                history.replaceState(null, null, '?tab=category');
                                            }
                                        });
                                    });

                                    // Xử lý tab active dựa trên URL parameter
                                    const urlParams = new URLSearchParams(window.location.search);
                                    const activeTab = urlParams.get('tab');

                                    if (activeTab === 'unit') {
                                        document.getElementById('unit-tab').click();
                                    } else if (activeTab === 'parent') {
                                        document.getElementById('parent-category-tab').click();
                                    }

                                    // Xử lý đóng thông báo
                                    const closeButtons = document.querySelectorAll('.close');
                                    closeButtons.forEach(button => {
                                        button.addEventListener('click', function () {
                                            this.parentElement.style.display = 'none';
                                        });
                                    });

                                    // Xử lý sự kiện khi nhấn nút sửa danh mục
                                    const editCategoryBtns = document.querySelectorAll('.edit-category-btn');
                                    editCategoryBtns.forEach(btn => {
                                        btn.addEventListener('click', function () {
                                            const categoryId = this.getAttribute('data-id');

                                            // Gửi AJAX request để lấy thông tin danh mục
                                            fetch('category', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/x-www-form-urlencoded',
                                                },
                                                body: 'action=getCategory&categoryId=' + categoryId
                                            })
                                                    .then(response => response.json())
                                                    .then(data => {
                                                        if (data.error) {
                                                            alert(data.error);
                                                            return;
                                                        }

                                                        // Điền thông tin vào form
                                                        document.getElementById('editCategoryId').value = data.id;
                                                        document.getElementById('editCategoryName').value = data.name;
                                                        document.getElementById('editParentCategory').value = data.parentId;

                                                        if (data.activeFlag == 1) {
                                                            document.getElementById('editStatusActive').checked = true;
                                                        } else {
                                                            document.getElementById('editStatusInactive').checked = true;
                                                        }

                                                        // Hiển thị modal
                                                        showModal('editCategoryModal');
                                                    })
                                                    .catch(error => {
                                                        alert('Đã xảy ra lỗi khi lấy thông tin danh mục');
                                                    });
                                        });
                                    });

                                    // Xử lý sự kiện khi nhấn nút xóa danh mục
                                    const deleteCategoryBtns = document.querySelectorAll('.delete-category-btn');
                                    deleteCategoryBtns.forEach(btn => {
                                        btn.addEventListener('click', function () {
                                            const categoryId = this.getAttribute('data-id');

                                            if (confirm('Bạn có chắc chắn muốn xóa danh mục này?')) {
                                                // Gửi AJAX request để xóa danh mục
                                                fetch('category', {
                                                    method: 'POST',
                                                    headers: {
                                                        'Content-Type': 'application/x-www-form-urlencoded',
                                                    },
                                                    body: 'action=delete&categoryId=' + categoryId
                                                })
                                                        .then(response => response.json())
                                                        .then(data => {
                                                            if (data.success) {
                                                                alert('Xóa danh mục thành công');
                                                                location.reload();
                                                            } else {
                                                                alert(data.message || 'Không thể xóa danh mục');
                                                            }
                                                        })
                                                        .catch(error => {
                                                            alert('Đã xảy ra lỗi khi xóa danh mục');
                                                        });
                                            }
                                        });
                                    });

                                    // Xử lý sự kiện khi nhấn nút sửa danh mục cha
                                    const editParentBtns = document.querySelectorAll('.edit-parent-btn');
                                    editParentBtns.forEach(btn => {
                                        btn.addEventListener('click', function () {
                                            const categoryId = this.getAttribute('data-id');

                                            // Gửi AJAX request để lấy thông tin danh mục cha
                                            fetch('category', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/x-www-form-urlencoded',
                                                },
                                                body: 'action=getCategory&categoryId=' + categoryId
                                            })
                                                    .then(response => response.json())
                                                    .then(data => {
                                                        if (data.error) {
                                                            alert(data.error);
                                                            return;
                                                        }

                                                        // Điền thông tin vào form
                                                        document.getElementById('editParentId').value = data.id;
                                                        document.getElementById('editParentName').value = data.name;

                                                        if (data.activeFlag == 1) {
                                                            document.getElementById('editParentStatusActive').checked = true;
                                                        } else {
                                                            document.getElementById('editParentStatusInactive').checked = true;
                                                        }

                                                        // Hiển thị modal
                                                        showModal('editParentModal');
                                                    })
                                                    .catch(error => {
                                                        alert('Đã xảy ra lỗi khi lấy thông tin danh mục cha');
                                                    });
                                        });
                                    });

                                    // Xử lý sự kiện khi nhấn nút xóa danh mục cha
                                    const deleteParentBtns = document.querySelectorAll('.delete-parent-btn');
                                    deleteParentBtns.forEach(btn => {
                                        btn.addEventListener('click', function () {
                                            const categoryId = this.getAttribute('data-id');
                                            const childCount = parseInt(this.getAttribute('data-count'));

                                            if (childCount > 0) {
                                                alert('Không thể xóa danh mục cha đang có ' + childCount + ' danh mục con!');
                                                return;
                                            }

                                            if (confirm('Bạn có chắc chắn muốn xóa danh mục cha này?')) {
                                                // Gửi AJAX request để xóa danh mục cha
                                                fetch('category', {
                                                    method: 'POST',
                                                    headers: {
                                                        'Content-Type': 'application/x-www-form-urlencoded',
                                                    },
                                                    body: 'action=delete&categoryId=' + categoryId
                                                })
                                                        .then(response => response.json())
                                                        .then(data => {
                                                            if (data.success) {
                                                                alert('Xóa danh mục cha thành công');
                                                                location.reload();
                                                            } else {
                                                                alert(data.message || 'Không thể xóa danh mục cha');
                                                            }
                                                        })
                                                        .catch(error => {
                                                            alert('Đã xảy ra lỗi khi xóa danh mục cha');
                                                        });
                                            }
                                        });
                                    });

                                    // Xử lý sự kiện khi nhấn nút sửa đơn vị tính
                                    const editUnitBtns = document.querySelectorAll('.edit-unit-btn');
                                    editUnitBtns.forEach(btn => {
                                        btn.addEventListener('click', function () {
                                            const unitId = this.getAttribute('data-id');

                                            // Gửi AJAX request để lấy thông tin đơn vị tính
                                            fetch('category', {
                                                method: 'POST',
                                                headers: {
                                                    'Content-Type': 'application/x-www-form-urlencoded',
                                                },
                                                body: 'action=getUnit&unitId=' + unitId
                                            })
                                                    .then(response => response.json())
                                                    .then(data => {
                                                        if (data.error) {
                                                            alert(data.error);
                                                            return;
                                                        }

                                                        // Điền thông tin vào form
                                                        document.getElementById('editUnitId').value = data.id;
                                                        document.getElementById('editUnitName').value = data.name;

                                                        // Hiển thị modal
                                                        showModal('editUnitModal');
                                                    })
                                                    .catch(error => {
                                                        alert('Đã xảy ra lỗi khi lấy thông tin đơn vị tính');
                                                    });
                                        });
                                    });

                                    // Xử lý sự kiện khi nhấn nút xóa đơn vị tính
                                    const deleteUnitBtns = document.querySelectorAll('.delete-unit-btn');
                                    deleteUnitBtns.forEach(btn => {
                                        btn.addEventListener('click', function () {
                                            const unitId = this.getAttribute('data-id');

                                            if (confirm('Bạn có chắc chắn muốn xóa đơn vị tính này?')) {
                                                // Gửi AJAX request để xóa đơn vị tính
                                                fetch('category', {
                                                    method: 'POST',
                                                    headers: {
                                                        'Content-Type': 'application/x-www-form-urlencoded',
                                                    },
                                                    body: 'action=delete_unit&unitId=' + unitId
                                                })
                                                        .then(response => response.json())
                                                        .then(data => {
                                                            if (data.success) {
                                                                alert('Xóa đơn vị tính thành công');
                                                                location.reload();
                                                            } else {
                                                                alert(data.message || 'Không thể xóa đơn vị tính');
                                                            }
                                                        })
                                                        .catch(error => {
                                                            alert('Đã xảy ra lỗi khi xóa đơn vị tính');
                                                        });
                                            }
                                        });
                                    });
                                });

                                // Hàm lấy parameter từ URL
                                function getUrlParameter(name) {
                                    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
                                    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
                                    var results = regex.exec(location.search);
                                    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
                                }
        </script>
    </body>
</html>
