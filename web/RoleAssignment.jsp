<%-- 
    Document   : RoleAssignment
    Created on : 31 thg 5, 2025, 16:29:22
    Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" %>

<%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
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
                display: flex;
                min-height: 100vh;
            }
            .sidebar {
                position: fixed;
                top: 0;
                left: 0;
                width: 250px;
                height: 100vh; /* chiếm full chiều cao viewport */
                background-color: #2c3e50;
                color: white;
                padding: 20px;
                overflow-y: auto; /* nếu menu dài thì có scrollbar riêng */
                box-sizing: border-box;
            }

            .sidebar h2 {
                text-align: center;
                font-size: 24px;
                margin-bottom: 30px;
            }
            .sidebar ul {
                list-style: none;
                padding: 0;
            }
            .sidebar ul li {
                padding: 15px;
                font-size: 18px;
                cursor: pointer;
                border-radius: 5px;
                margin-bottom: 10px;
            }
            .sidebar ul li:hover {
                background-color: #34495e;
            }
            .add-user-btn {
                background-color: #3498db;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 5px;
                cursor: pointer;
                margin-bottom: 20px;
            }
            .add-user-btn a {
                color: white;
                text-decoration: none;
            }
            .add-user-btn:hover {
                background-color: #2980b9;
            }
            .main-content {
                margin-left: 250px;
                padding: 20px;
                flex: 1;
            }

            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #fff;
                padding: 15px 20px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
            }
            .header h2 {
                margin: 0;
                font-size: 24px;
            }
            .admin-logout {
                font-size: 16px;
            }
            .admin-logout a {
                color: #e74c3c;
                text-decoration: none;
                margin-left: 10px;
            }
            .admin-logout a:hover {
                text-decoration: underline;
            }
            .filter-bar {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
                align-items: center;
            }
            .filter-bar select, .filter-bar input {
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 5px;
                font-size: 14px;
            }
            .filter-bar input {
                flex: 1;
                min-width: 200px;
            }
            .filter-bar button {
                background-color: #3498db;
                color: white;
                border: none;
                padding: 8px 15px;
                border-radius: 5px;
                cursor: pointer;
            }
            .filter-bar button:hover {
                background-color: #2980b9;
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
            <div class="sidebar">
                <h2>Warehouse<br>Manager</h2>
                <ul >
                    <li class="add-user-btn"><a href="admin">User Manager</a></li>
                    <li class="add-user-btn"><a href="roleAssignment"> Role Assignment </a></li>
                    <li class="add-user-btn"><a href="categoriesforward.jsp">Material Information</a></li>
                    <li class="add-user-btn"><a href="RequestForward.jsp" >Transaction</a></li>
                    <li class="add-user-btn"><a href="password_request.jsp">Password request</a></li>
                    <li class="add-user-btn">Statistic</li>
                </ul>
            </div>

            <div class="main-content">
                <div class="header">
                    <div><h2>Setting List</h2></div>
                    <div class="admin-logout">
                        <strong>Admin</strong> <a href="logout">Log out</a>
                    </div>
                </div>

                <form action="userfilter" method="get" class="filter-bar">
                    <div class="filter-bar">
                        <select id="role" name="role">
                            <option value="all" selected>All Roles</option>
                            <option value="2">Warehouse Staff</option>
                            <option value="3">Company Employee</option>
                            <option value="4">Company Director</option>
                        </select>


                        <select name="status">
                            <option value="">All Statuses</option>
                            <option value="active">Active</option>
                            <option value="inactive">Inactive</option>
                        </select>

                        <input type="text" name="keyword" placeholder="Enter keyword(s) to search">
                        <input type="hidden" name="page" value="roleassignment" />
                        <button class="search-btn">Search</button>
                    </div>
                </form>


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
        </div>



    </body>
</html>

