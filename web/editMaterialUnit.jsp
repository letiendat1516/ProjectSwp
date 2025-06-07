<%-- 
    Document   : editMaterialUnit
    Created on : May 27, 2025, 7:36:08 PM
    Author     : tunga
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Material Unit</title>
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
        <h1>Thay đổi dữ liệu của đơn vị</h1>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-message" style="color: red; margin-bottom: 10px;">${errorMessage}</div>
        </c:if>
        
        <c:if test="${empty unit}">
            <div class="error-message">Không tìm thấy đơn vị. <a href="materialUnit">Quay về danh sách.</a></div>
        </c:if>
        
        <c:if test="${not empty unit}">
            <form action="editMaterialUnit" method="post">
                <input type="hidden" name="id" value="${unit.id}">
                
                <div class="form-group">
                    <label for="name">Tên:</label>
                    <input type="text" id="name" name="name" value="${unit.name}" required>
                </div>
                
                <div class="form-group">
                    <label for="symbol">Kí hiệu:</label>
                    <input type="text" id="symbol" name="symbol" value="${unit.symbol}" required>
                </div>
                
                <div class="form-group">
                    <label for="description">Mô tả:</label>
                    <textarea id="description" name="description" rows="4">${unit.description}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="type">Loại đơn vị</label>
                    <select id="type" name="type" required>
                        <option value="Khối lượng" ${unit.type == 'Khối lượng' ? 'selected' : ''}>Khối lượng</option>
                        <option value="Độ dài" ${unit.type == 'Độ dài' ? 'selected' : ''}>Độ dài</option>
                        <option value="Số lượng" ${unit.type == 'Số lượng' ? 'selected' : ''}>Số lượng</option>
                    </select>
                </div>
                
                <div class="form-buttons">
                    <button type="submit" class="btn-primary">Cập nhật</button>
                    <a href="materialUnit" class="btn-secondary">Hủy bỏ</a>
                </div>
            </form>
        </c:if>
    </div>
</body>
</html>

