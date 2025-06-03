<%-- 
    Document   : RoleAssignment
    Created on : 31 thg 5, 2025, 16:29:22
    Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Role Assignment</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f4f4f9;
            }
            .container {
                margin: 20px;
            }

            .sidebar {
                width: 250px;
                background-color: #2c3e50;
                color: white;
                padding: 20px;
            }
            .sidebar h2 {
                text-align: center;
                font-size: 24px;
                margin-bottom: 30px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            th {
                background-color: #f2f2f2;
            }
            select, button {
                padding: 8px;
                font-size: 14px;
            }
            .btn {
                background-color: #3498db;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            .btn:hover {
                background-color: #2980b9;
            }
            .message {
                color: green;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div>
                <h2>Assign Roles to Users</h2>
            </div>


            <div class="main-content">
                <div class="header">
                    <div class="admin-logout">
                        <strong>Admin</strong><a href="admin">Back to the list</a>
                    </div>
                </div>
            </div>

            <c:if test="${not empty sessionScope.message}">
                <div class="message">${sessionScope.message}</div>
                <c:remove var="message" scope="session"/>
            </c:if>

            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Current Role</th>
                        <th>New Role</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${userList}">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.fullname}</td>
                            <td>${user.email}</td>
                            <td>${user.roleName}</td>
                            <td>
                                <form action="roleAssignment" method="post">
                                    <input type="hidden" name="userId" value="${user.id}"/>
                                    <select name="role">
                                        <option value="2" ${user.roleName == 'Warehouse Staff' ? 'selected' : ''}>Warehouse Staff</option>
                                        <option value="3" ${user.roleName == 'Company Employee' ? 'selected' : ''}>Company Employee</option>
                                        <option value="4" ${user.roleName == 'Company Director' ? 'selected' : ''}>Company Director</option>
                                    </select>
                                    <button type="submit" class="btn">Assign Role</button>
                                </form>
                            </td>                           
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>

