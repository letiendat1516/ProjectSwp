<!-- Đat -->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    
    <meta charset="UTF-8">
    <title>Chỉnh sửa danh mục</title>
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
        }
        
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .card {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-bottom: 20px;
        }
        
        .card-header {
            padding: 15px 20px;
            font-weight: bold;
        }
        
        .card-header-warning {
            background-color: #ffc107;
            color: #212529;
        }
        
        .card-header-info {
            background-color: #17a2b8;
            color: white;
        }
        
        .card-header h4, .card-header h5 {
            font-size: 18px;
            margin: 0;
        }
        
        .card-body {
            padding: 20px;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
            position: relative;
        }
        
        .alert-close {
            position: absolute;
            right: 10px;
            top: 10px;
            cursor: pointer;
            font-weight: bold;
            font-size: 20px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        
        .text-muted {
            color: #6c757d;
            font-size: 0.9em;
        }
        
        .form-control {
            width: 100%;
            padding: 10px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            font-size: 16px;
        }
        
        .form-select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            font-size: 16px;
            background-color: white;
        }
        
        .radio-group {
            margin-top: 10px;
        }
        
        .radio-item {
            margin-bottom: 8px;
            display: flex;
            align-items: center;
        }
        
        .radio-input {
            margin-right: 8px;
        }
        
        .button-group {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        
        .btn-warning {
            background-color: #ffc107;
            color: #212529;
        }
        
        .btn-warning:hover {
            background-color: #e0a800;
        }
        
        .btn-sm {
            padding: 5px 10px;
            font-size: 14px;
        }
        
        .btn-outline-primary {
            background-color: transparent;
            color: #007bff;
            border: 1px solid #007bff;
        }
        
        .btn-outline-primary:hover {
            background-color: #007bff;
            color: white;
        }
        
        .icon {
            display: inline-block;
            margin-right: 5px;
            font-size: 16px;
            line-height: 1;
        }
        
        .icon-check-circle:before {
            content: "✓";
        }
        
        .icon-x-circle:before {
            content: "✕";
        }
        
        .icon-arrow-left:before {
            content: "←";
        }
        
        .icon-save:before {
            content: "💾";
        }
        
        .icon-pencil:before {
            content: "✎";
        }
        
        .list-group {
            list-style: none;
            padding: 0;
            margin-top: 10px;
        }
        
        .list-group-item {
            border: 1px solid #ddd;
            padding: 12px 15px;
            margin-bottom: -1px;
        }
        
        .list-group-item:first-child {
            border-top-left-radius: 4px;
            border-top-right-radius: 4px;
        }
        
        .list-group-item:last-child {
            border-bottom-left-radius: 4px;
            border-bottom-right-radius: 4px;
            margin-bottom: 0;
        }
        
        .d-flex {
            display: flex;
        }
        
        .justify-content-between {
            justify-content: space-between;
        }
        
        .align-items-center {
            align-items: center;
        }
        
        .mt-2 {
            margin-top: 10px;
        }
        
        .mt-3 {
            margin-top: 15px;
        }
        
        .mt-4 {
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header card-header-warning">
                <h4>Chỉnh sửa danh mục</h4>
            </div>
            <div class="card-body">
                <c:if test="${error != null}">
                    <div class="alert-danger">
                        ${error}
                        <span class="alert-close" onclick="this.parentElement.style.display='none'">&times;</span>
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/category/edit" method="post">
                    <input type="hidden" name="id" value="${category.id}">
                    
                    <div class="form-group">
                        <label for="name" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" value="${category.name}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="parentId" class="form-label">Danh mục cha</label>
                        <select class="form-select" id="parentId" name="parentId">
                            <option value="0">-- Không có danh mục cha --</option>
                            <c:forEach items="${parentCategories}" var="parent">
                                <c:if test="${parent.id != category.id}">
                                    <option value="${parent.id}" ${category.parentId == parent.id ? 'selected' : ''}>${parent.name}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <small class="text-muted">Lưu ý: Danh mục không thể chọn chính nó làm danh mục cha</small>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Trạng thái</label>
                        <div class="radio-group">
                            <div class="radio-item">
                                <input class="radio-input" type="radio" name="activeFlag" id="activeTrue" value="1" ${category.activeFlag ? 'checked' : ''}>
                                <label for="activeTrue">
                                    <span class="text-success"><span class=""></span> Hoạt động</span>
                                </label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input" type="radio" name="activeFlag" id="activeFalse" value="0" ${!category.activeFlag ? 'checked' : ''}>
                                <label for="activeFalse">
                                    <span class="text-danger"><span class=""></span> Không hoạt động</span>
                                </label>
                            </div>
                        </div>
                    </div>
                    
                    <div class="button-group mt-4">
                        <a href="${pageContext.request.contextPath}/category/list" class="btn btn-secondary">
                            <span class="icon icon-arrow-left"></span> Quay lại
                        </a>
                        <button type="submit" class="btn btn-warning">
                            <span class=""></span> Cập nhật
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Thông tin bổ sung -->
        <div class="card">
            <div class="card-header card-header-info">
                <h5>Thông tin bổ sung</h5>
            </div>
            <div class="card-body">
                <p><strong>ID danh mục:</strong> ${category.id}</p>
                
                <!-- Hiển thị thông tin về danh mục cha nếu có -->
                <c:if test="${category.parentId != null}">
                    <p><strong>Thuộc danh mục:</strong>
                        <c:forEach items="${parentCategories}" var="parent">
                            <c:if test="${category.parentId == parent.id}">
                                <a href="${pageContext.request.contextPath}/category/edit?id=${parent.id}">${parent.name}</a>
                            </c:if>
                        </c:forEach>
                    </p>
                </c:if>
                
                <!-- Hiển thị các danh mục con (nếu có) -->
                <div class="mt-3">
                    <strong>Các danh mục con:</strong>
                    <ul class="list-group mt-2">
                        <c:set var="hasChildren" value="false" />
                        <c:forEach items="${parentCategories}" var="child">
                            <c:if test="${child.parentId == category.id}">
                                <c:set var="hasChildren" value="true" />
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span>${child.name}</span>
                                    <a href="${pageContext.request.contextPath}/category/edit?id=${child.id}" class="btn btn-sm btn-outline-primary">
                                        <span class="icon icon-pencil"></span> Sửa
                                    </a>
                                </li>
                            </c:if>
                        </c:forEach>
                        <c:if test="${!hasChildren}">
                            <li class="list-group-item text-muted">Không có danh mục con</li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        // Xử lý đóng alert khi click vào nút đóng
        document.addEventListener('DOMContentLoaded', function() {
            const closeButtons = document.querySelectorAll('.alert-close');
            closeButtons.forEach(function(button) {
                button.addEventListener('click', function() {
                    this.parentElement.style.display = 'none';
                });
            });
        });
    </script>
</body>
</html>
