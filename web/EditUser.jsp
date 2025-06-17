<%-- 
    Document   : EditUser
    Created on : 23 thg 5, 2025, 16:24:57
    Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="model.Users"%>

<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>

    
    <%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Edit User</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            padding: 30px;
        }
        .form-container {
            max-width: 600px;
            margin: 32px auto;
            background: white;
            padding: 32px 32px 24px 32px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            font-size: 2rem;
        }
        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0 10px;
        }
        td.label {
            width: 32%;
            font-weight: bold;
            text-align: right;
            padding-right: 16px;
            vertical-align: middle;
        }
        td.input {
            width: 68%;
        }
        input[type="text"], input[type="password"], input[type="email"], input[type="date"], select {
            width: 100%;
            padding: 10px 12px;
            border-radius: 4px;
            border: 1px solid #ccc;
            font-size: 1rem;
            box-sizing: border-box;
        }
        .row-flex {
            display: flex;
            gap: 18px;
        }
        .row-flex > div {
            flex: 1;
        }
        .btn-submit {
            margin-top: 28px;
            width: 100%;
            padding: 14px;
            background-color: #3498db;
            border: none;
            border-radius: 5px;
            color: white;
            font-size: 1.18rem;
            cursor: pointer;
            font-weight: bold;
        }
        .btn-submit:hover {
            background-color: #2980b9;
        }
        .back-link {
            display: block;
            margin-top: 17px;
            text-align: center;
            color: #3498db;
            text-decoration: none;
            font-size: 1.04rem;
        }
        .back-link:hover {
            text-decoration: underline;
        }
        .success-message { color: green; margin-bottom: 15px; text-align: center;}
        .error-message { color: red; margin-bottom: 15px; text-align: center;}
    </style>
</head>
<body>
<div class="form-container">
    <h2>Edit User</h2>

<<<<<<< HEAD
            <%-- Hiển thị thông báo lỗi nếu có --%>
            <c:if test="${not empty error}">
                <div style="color: red; margin-bottom: 15px;">${error}</div>
            </c:if>
                
            <%-- Hiển thị thông báo nếu có thông báo thành công từ session --%>
            <c:if test="${empty sessionScope.message}">
                <div class="success-message">
                    ${sessionScope.message}
                </div>
                <%-- Xóa thông báo sau khi hiển thị --%>
                <c:remove var="message"/>
            </c:if>

            <form action="edituser" method="post">               
                <input type="hidden" name="id" value="${editUser.id}"/>


                <table>
                    <tr>
                        <td class="label" style="text-align: left; width: 30%;"><label for="username">Username:</label></td>
                        <td colspan="3"><input type="text" id="username" name="username" value="${editUser.username}" required style="width: 100%;"></td>
                    </tr>
                    <tr>
                        <td class="label" style="text-align: left;"><label for="password">Password:</label></td>
                        <td colspan="3"><input type="text" id="password" name="password" value="${editUser.password}" required style="width: 100%;"></td>
                    </tr>
                    <tr>
                        <td class="label" style="text-align: left;"><label for="fullname">Full Name:</label></td>
                        <td colspan="3"><input type="text" id="fullname" name="fullname" value="${editUser.fullname}" required style="width: 100%;"></td>
                    </tr>
                    <tr>
                        <td class="label" style="text-align: left;"><label for="email">Email:</label></td>
                        <td colspan="3"><input type="email" id="email" name="email" value="${editUser.email}" required style="width: 100%;"></td>
                    </tr>

                    <tr>
                        <td class="label" style="text-align: left; padding-right: 10px; width: 15%;">
                            <label for="role">Role:</label>
                        </td>
                        <td style="width: 35%;">
                            <select id="role" name="role" required style="width: 100%;">
=======
    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="success-message">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>
        

    <form action="edituser" method="post">
        <input type="hidden" name="id" value="${editUser.id}"/>
        <table>
            <tr>
                <td class="label"><label for="username">Username:</label></td>
                <td class="input"><input type="text" id="username" name="username" value="${editUser.username}" required></td>
            </tr>
            <tr>
                <td class="label"><label for="password">Password:</label></td>
                <td class="input"><input type="text" id="password" name="password" value="${editUser.password}" required></td>
            </tr>
            <tr>
                <td class="label"><label for="fullname">Full Name:</label></td>
                <td class="input"><input type="text" id="fullname" name="fullname" value="${editUser.fullname}" required></td>
            </tr>
            <tr>
                <td class="label"><label for="email">Email:</label></td>
                <td class="input"><input type="email" id="email" name="email" value="${editUser.email}" required></td>
            </tr>
            <tr>
                <td class="label"><label for="phone">Phone:</label></td>
                <td class="input"><input type="text" id="phone" name="phone" value="${editUser.phone}" maxlength="20" required></td>
            </tr>
            <tr>
                <td class="label"><label for="dob">Date of Birth:</label></td>
                <td class="input"><input type="date" id="dob" name="dob" value="${editUser.dob}" required></td>
            </tr>
            <tr>
                <td class="label"><label for="role">Role:</label></td>
                <td class="input">
                    <div class="row-flex">
                        <div>
                            <select id="role" name="role" required>
>>>>>>> 0abc521ef0adf66e19c09e6f699bf98f190d5360
                                <option value="">-- Select Role --</option>
                                <option value="2" ${editUser.roleName == 'Warehouse Staff' ? 'selected' : ''}>Warehouse Staff</option>
                                <option value="3" ${editUser.roleName == 'Company Employee' ? 'selected' : ''}>Company Employee</option>
                                <option value="4" ${editUser.roleName == 'Company Director' ? 'selected' : ''}>Company Director</option>
                            </select>
<<<<<<< HEAD
                        </td>

                        <td class="label" style="text-align: left; padding-left: 30px; width: 15%;">
                            <label for="activeFlag">Status:</label>
                        </td>
                        <td style="width: 35%;">
                            <select id="activeFlag" name="activeFlag" required style="width: 100%;">
=======
                        </div>
                        <div>
                            <select id="activeFlag" name="activeFlag" required>
>>>>>>> 0abc521ef0adf66e19c09e6f699bf98f190d5360
                                <option value="1" ${editUser.activeFlag == 1 ? 'selected' : ''}>Active</option>
                                <option value="0" ${editUser.activeFlag == 0 ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        <button type="submit" class="btn-submit">Update User</button>
    </form>
    <a href="admin" class="back-link">Back to User List</a>
</div>
</body>
</html>


