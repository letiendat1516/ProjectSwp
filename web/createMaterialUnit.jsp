<%-- 
    Document   : createMaterialUnit
    Created on : May 27, 2025, 7:35:19 PM
    Author     : tunga
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Material Unit</title>
    <!-- Thêm các stylesheet khác nếu cần -->
    <style>
        .container {
    width: 80%;
    margin: 0 auto;
    padding: 20px;
}

h1 {
    color: #333;
    margin-bottom: 20px;
}

.form-group {
    margin-bottom: 15px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

.form-group input[type="text"],
.form-group select,
.form-group textarea {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
}

.form-buttons {
    margin-top: 20px;
}

.btn-primary {
    display: inline-block;
    background-color: #4CAF50;
    color: white;
    padding: 8px 16px;
    text-decoration: none;
    border-radius: 4px;
    border: none;
    cursor: pointer;
}

.btn-secondary {
    display: inline-block;
    background-color: #f44336;
    color: white;
    padding: 8px 16px;
    text-decoration: none;
    border-radius: 4px;
    margin-left: 10px;
}
    </style>
</head>
<body>
    <div class="container">
        <h1>Thêm đơn vị mới</h1>
        
        <c:if test="${not empty errorMessage}">
        <div class="error-message" style="color: red; margin-bottom: 10px;">${errorMessage}</div>
    </c:if>
        
        <form action="createMaterialUnit" method="post">
            <div class="form-group">
                <label for="name">Tên:</label>
                <input type="text" id="name" name="name" required>
            </div>
            
            <div class="form-group">
                <label for="symbol">Kí hiệu:</label>
                <input type="text" id="symbol" name="symbol" required>
            </div>
            
            <div class="form-group">
                <label for="description">Mô tả:</label>
                <textarea id="description" name="description" rows="4"></textarea>
            </div>
            
            <div class="form-group">
                <label for="type">Loại đơn vị</label>
                <select id="type" name="type" required>
                    <option value="Khối lượng">Khối lượng</option>
                    <option value="Độ dài">Độ dài</option>
                    <option value="Số lượng">Số lượng</option>
                </select>
            </div>
            
            <div class="form-buttons">
                <button type="submit" class="btn-primary">Lưu</button>
                <a href="materialUnit" class="btn-secondary">Hủy bỏ</a>
            </div>
        </form>
    </div>
</body>
</html>
