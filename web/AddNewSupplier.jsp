<%-- 
    Document   : AddNewSupplier
    Created on : 28 thg 5, 2025, 21:45:04
    Author     : Fpt06
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page import="model.Users" %> 
<%
Users user = (Users) session.getAttribute("user");
if (user == null || (!"Admin".equalsIgnoreCase(user.getRoleName())&&!"Nhân viên kho".equalsIgnoreCase(user.getRoleName()))) {
    response.sendRedirect("login.jsp");
    return;
}

%>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Add New Supplier</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <style>
            * {
                box-sizing: border-box;
                font-family: 'Roboto', sans-serif;
            }

            body {
                margin: 0;
                background: linear-gradient(135deg, #e0f7fa, #e1bee7);
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                animation: fadeIn 1.2s ease-in-out;
            }

            @keyframes fadeIn {
                0% {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                100% {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .form-container {
                background: #fff;
                padding: 40px;
                border-radius: 16px;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
                width: 100%;
                max-width: 420px;
                animation: slideUp 0.8s ease-in-out;
            }

            @keyframes slideUp {
                0% {
                    transform: translateY(50px);
                    opacity: 0;
                }
                100% {
                    transform: translateY(0);
                    opacity: 1;
                }
            }

            h1 {
                text-align: center;
                color: #5e35b1;
                margin-bottom: 24px;
            }

            .input-group {
                margin-bottom: 20px;
            }

            .input-group label {
                display: block;
                margin-bottom: 6px;
                font-weight: 500;
            }

            .input-group input {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 8px;
                transition: 0.3s;
            }

            .input-group input:focus {
                border-color: #5e35b1;
                outline: none;
                box-shadow: 0 0 8px #ce93d8;
            }

            .input-group textarea {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 8px;
                resize: vertical; /* cho phép kéo giãn chiều cao */
                min-height: 80px;
                transition: 0.3s;
                font-family: 'Roboto', sans-serif;
            }

            .input-group textarea:focus {
                border-color: #5e35b1;
                outline: none;
                box-shadow: 0 0 8px #ce93d8;
            }

            .submit-btn {
                width: 100%;
                background: #43a047;
                color: white;
                font-weight: bold;
                border: none;
                padding: 12px;
                border-radius: 8px;
                font-size: 16px;
                cursor: pointer;
                transition: background 0.3s, transform 0.2s;
            }

            .submit-btn:hover {
                background: #388e3c;
                transform: translateY(-2px);
            }

            .back-btn {
                display: flex;
                align-items: center;
                justify-content: center;
                margin-top: 16px;
                text-decoration: none;
                font-size: 14px;
                color: #5e35b1;
                transition: all 0.3s ease;
            }

            .back-btn:hover {
                color: #311b92;
                transform: scale(1.05);
            }

            .material-icons {
                vertical-align: middle;
                margin-right: 6px;
            }
        </style>
    </head>
    <body>

        <div class="form-container">
            <h1>Add New Supplier</h1>
            <c:set var="mess" value="${requestScope.mess}"/>
            <c:set var="invalid" value="${requestScope.invalid}"/>
            <c:if test="${not empty mess}">
                <p style="color: red;font-size: larger" >${mess}</p>
            </c:if>
            <form action="AddNewSupplier" method="get">
                <div class="input-group">
                    <label><i class="material-icons">business</i> Tên nhà cung cấp</label>
                    <input type="text" name="nameSupplier" required>
                </div>

                <div class="input-group">
                    <label><i class="material-icons">phone</i> Số điện thoại</label>
                    <c:if test="${not empty invalid}">
                        <label style="color: red" >${invalid}</label>
                    </c:if>
                    <input type="number" name="phone" required>
                </div>
                <div class="input-group">
                    <label><i class="material-icons">email</i> Email</label>
                    <input type="email" name="email" required>
                </div>
                <div class="input-group">
                    <label><i class="material-icons">home</i> Địa chỉ</label>
                    <input type="text" name="address" required>
                </div>
                <div class="input-group">
                    <label><i class="material-icons">edit_note</i> Ghi chú</label>
                    <textarea name="note" rows="3"></textarea>
                </div>
                <button type="submit" class="submit-btn">Add</button>
            </form>
            <a href="LishSupplier" class="back-btn"><i class="material-icons">arrow_back</i> Back</a>
        </div>

    </body>
</html>
