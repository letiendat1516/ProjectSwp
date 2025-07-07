<%-- 
    Document   : EditProfile
    Created on : 7 thg 6, 2025, 16:56:05
    Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" %>
<%
    Users currentUser = (Users) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f6f6f8;
            padding: 30px;
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
        /* Header giống mẫu */
        .custom-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #fff;
            border-radius: 0 0 7px 7px;
            padding: 20px 34px 16px 34px;
            box-shadow: 0 1px 5px rgba(0,0,0,0.07);
            margin-bottom: 28px;
        }
        .header-left {
            display: flex;
            align-items: center;
            gap: 32px;
        }
        .logo-title {
            font-size: 1.44rem;
            font-weight: bold;
            color: #206ed8;
            margin-right: 34px;
            letter-spacing: 0.5px;
        }
        .dashboard-title {
            font-size: 2.1rem;
            color: #206ed8;
            font-weight: 700;
        }
        .header-right {
            display: flex;
            align-items: center;
            gap: 20px;
        }
        .admin-name {
            font-size: 1.13rem;
            color: #1b466a;
            margin-right: 8px;
        }
        .logout-btn {
            background: #ff0202;
            color: #fff;
            border: none;
            padding: 8px 24px;
            border-radius: 7px;
            font-weight: 600;
            font-size: 1rem;
            text-decoration: none;
            transition: background 0.15s;
        }
        .logout-btn:hover {
            background: #c70000;
        }

        /* Container Edit Profile */
        .edit-container {
            background: #fff;
            border-radius: 8px;
            max-width: 480px;
            margin: 36px auto;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            padding: 32px 34px 24px 34px;
        }
        h2 {
            text-align: center;
            margin-bottom: 28px;
        }
        table {
            width: 100%;
        }
        td.label {
            font-weight: bold;
            text-align: right;
            padding-right: 20px;
            color: #3498db;
            width: 32%;
        }
        td.input {
            width: 68%;
        }
        input[type="text"], input[type="email"], input[type="date"] {
            width: 100%;
            padding: 10px 12px;
            border-radius: 4px;
            border: 1px solid #ccc;
            font-size: 1rem;
            box-sizing: border-box;
            margin-bottom: 6px;
        }
        .btn-submit {
            margin-top: 18px;
            width: 100%;
            padding: 13px;
            background-color: #3498db;
            border: none;
            border-radius: 5px;
            color: white;
            font-size: 1.15rem;
            cursor: pointer;
            font-weight: bold;
        }
        .btn-submit:hover {
            background-color: #217dbb;
        }
        .back-link {
            display: block;
            margin-top: 16px;
            text-align: center;
            color: #3498db;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .success-message {
            color: green;
            margin-bottom: 13px;
            text-align: center;
        }
        .error-message {
            color: red;
            margin-bottom: 13px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="layout-container">
        <jsp:include page="/include/sidebar.jsp" />
        <div class="main-content">
            <!-- HEADER giống như dashboard -->
            <div class="custom-header">
                <div class="header-left">
                    <span class="logo-title">Warehouse Manager</span>
                    <span class="dashboard-title">Admin Dashboard</span>
                </div>
                <div class="header-right">
                    <span class="admin-name">Admin</span>
                    <a href="logout" class="logout-btn">Đăng xuất</a>
                </div>
            </div>
            <!-- FORM SỬA PROFILE -->
            <div class="edit-container">
                <h2>Edit Profile</h2>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
                <c:if test="${not empty sessionScope.message}">
                    <div class="success-message">${sessionScope.message}</div>
                    <c:remove var="message" scope="session"/>
                </c:if>

                <form action="editprofile" method="post">
                    <table>           
                        <tr>
                            <td class="label">Full Name:</td>
                            <td class="input"><input type="text" name="fullname" value="${currentUser.fullname}" required></td>
                        </tr>
                        <tr>
                            <td class="label">Email:</td>
                            <td class="input"><input type="email" name="email" value="${currentUser.email}" required></td>
                        </tr>
                        <tr>
                            <td class="label">Phone:</td>
                            <td class="input"><input type="text" name="phone" value="${currentUser.phone}" maxlength="20"></td>
                        </tr>
                        <tr>
                            <td class="label">Date of Birth:</td>
                            <td class="input"><input type="date" name="dob" value="${currentUser.dob}"></td>
                        </tr>
                    </table>
                    <button type="submit" class="btn-submit">Update</button>
                </form>
                <!-- Back về trang profile -->
                <a href="profile" class="back-link">Back to Profile</a>
                <!-- Back về homepage -->
                <a href="<%= homePage %>" class="back-link">Back</a>
            </div>
        </div>
    </div>
</body>
</html>
