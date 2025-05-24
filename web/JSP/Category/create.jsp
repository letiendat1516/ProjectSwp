<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục mới</title>
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
        }
        
        .card-header {
            background-color: #1a73e8;
            color: white;
            padding: 15px 20px;
        }
        
        .card-header h4 {
            font-size: 18px;
            font-weight: bold;
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
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        
        .text-danger {
            color: #dc3545;
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
        }
        
        .btn-primary {
            background-color: #1a73e8;
            color: white;
        }
        
        .btn-primary:hover {
            background-color: #0d62c9;
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-secondary:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="card-header">
                <h4>Thêm danh mục mới</h4>
            </div>
            <div class="card-body">
                <c:if test="${error != null}">
                    <div class="alert-danger">
                        ${error}
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/category/create" method="post">
                    <div class="form-group">
                        <label for="name" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="parentId" class="form-label">Danh mục cha</label>
                        <select class="form-select" id="parentId" name="parentId">
                            <option value="0">-- Không có danh mục cha --</option>
                            <c:forEach items="${parentCategories}" var="parent">
                                <option value="${parent.id}">${parent.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Trạng thái</label>
                        <div class="radio-group">
                            <div class="radio-item">
                                <input class="radio-input" type="radio" name="activeFlag" id="activeTrue" value="1" checked>
                                <label for="activeTrue">Hoạt động</label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input" type="radio" name="activeFlag" id="activeFalse" value="0">
                                <label for="activeFalse">Không hoạt động</label>
                            </div>
                        </div>
                    </div>
                    
                    <div class="button-group">
                        <a href="${pageContext.request.contextPath}/category/list" class="btn btn-secondary">Hủy</a>
                        <button type="submit" class="btn btn-primary">Lưu</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
