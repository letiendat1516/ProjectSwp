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
        <h1>Edit Material Unit</h1>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        
        <c:if test="${empty unit}">
            <div class="error-message">Material unit not found. <a href="materialUnit">Return to list</a></div>
        </c:if>
        
        <c:if test="${not empty unit}">
            <form action="editMaterialUnit" method="post">
                <input type="hidden" name="id" value="${unit.id}">
                
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="${unit.name}" required>
                </div>
                
                <div class="form-group">
                    <label for="symbol">Symbol:</label>
                    <input type="text" id="symbol" name="symbol" value="${unit.symbol}" required>
                </div>
                
                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" rows="4">${unit.description}</textarea>
                </div>
                
                <div class="form-group">
                    <label for="status">Status:</label>
                    <select id="status" name="status">
                        <option value="active" ${unit.status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${unit.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                
                <div class="form-buttons">
                    <button type="submit" class="btn-primary">Update</button>
                    <a href="materialUnit" class="btn-secondary">Cancel</a>
                </div>
            </form>
        </c:if>
    </div>
</body>
</html>

