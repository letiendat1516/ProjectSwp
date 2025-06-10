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
                margin: auto;
                background: white;
                padding: 25px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            h2 {
                text-align: center;
                margin-bottom: 20px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            td {
                padding: 10px;
                vertical-align: middle;
            }
            td.label {
                width: 30%;
                font-weight: bold;
                text-align: right;
                padding-right: 20px;
            }
            input[type="text"], input[type="email"], select {
                width: 100%;
                padding: 8px 10px;
                border-radius: 4px;
                border: 1px solid #ccc;
                font-size: 1rem;
            }
            .btn-submit {
                margin-top: 25px;
                width: 100%;
                padding: 12px;
                background-color: #3498db;
                border: none;
                border-radius: 5px;
                color: white;
                font-size: 1.1rem;
                cursor: pointer;
            }
            .btn-submit:hover {
                background-color: #2980b9;
            }
            .back-link {
                display: block;
                margin-top: 15px;
                text-align: center;
                color: #3498db;
                text-decoration: none;
            }
            .back-link:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Edit User</h2>

            <%-- Hiển thị thông báo lỗi nếu có --%>
            <c:if test="${not empty error}">
                <div style="color: red; margin-bottom: 15px;">${error}</div>
            </c:if>
                
            <%-- Hiển thị thông báo nếu có thông báo thành công từ session --%>
            <c:if test="${empty sessionScope.message}">
                <div class="success-message">
                    ${sessionScope.message}
                </div>
<<<<<<< HEAD
                <%-- Xóa thông báo sau khi hiển thị --%>
=======
>>>>>>> 2b6e81ddddeda52d5cd39bd59c65cba42934ef8e
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
                                <option value="">-- Select Role --</option>
                                <option value="2" ${editUser.roleName == 'Warehouse Staff' ? 'selected' : ''}>Warehouse Staff</option>
                                <option value="3" ${editUser.roleName == 'Company Employee' ? 'selected' : ''}>Company Employee</option>
                                <option value="4" ${editUser.roleName == 'Company Director' ? 'selected' : ''}>Company Director</option>
                            </select>
                        </td>

                        <td class="label" style="text-align: left; padding-left: 30px; width: 15%;">
                            <label for="activeFlag">Status:</label>
                        </td>
                        <td style="width: 35%;">
                            <select id="activeFlag" name="activeFlag" required style="width: 100%;">
                                <option value="1" ${editUser.activeFlag == 1 ? 'selected' : ''}>Active</option>
                                <option value="0" ${editUser.activeFlag == 0 ? 'selected' : ''}>Inactive</option>
                            </select>
                        </td>
                    </tr>
                </table>

                <button type="submit" class="btn-submit">Update User</button>
            </form>

            <a href="admin" class="back-link">Back to User List</a>
        </div>
    </body>
</html>


