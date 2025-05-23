<%-- 
    Document   : Dashboard
    Created on : May 19, 2025, 9:38:23 PM
    Author     : phucn
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <%
    if (request.getAttribute("userList") == null) {
        response.sendRedirect("admin");
        return;
    }
    %>

    <head>
        <title>Admin Dashboard</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f4f4f9;
                color: #333;
            }
            .container {
                display: flex;
                min-height: 100vh;
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
            .main-content {
                flex: 1;
                padding: 20px;
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
                background-color: #fff;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            th, td {
                padding: 12px 15px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            th {
                background-color: #ecf0f1;
                font-weight: bold;
            }
            tr:hover {
                background-color: #f9f9f9;
            }
            .status-active {
                color: #2ecc71;
                font-weight: bold;
            }
            .status-inactive {
                color: #e74c3c;
                font-weight: bold;
            }
            .action-btn {
                padding: 5px 10px;
                border: none;
                border-radius: 3px;
                cursor: pointer;
                margin-right: 5px;
                text-decoration: none;
            }
            .edit-btn {
                background-color: #3498db;
                color: white;
            }
            .edit-btn:hover {
                background-color: #2980b9;
            }
            .activate-btn {
                background-color: #2ecc71;
                color: white;
            }
            .activate-btn:hover {
                background-color: #27ae60;
            }
            .deactivate-btn {
                background-color: #e74c3c;
                color: white;
            }
            .deactivate-btn:hover {
                background-color: #c0392b;
            }
            .pagination {
                margin-top: 20px;
                display: flex;
                justify-content: center;
                gap: 5px;
            }
            .pagination button {
                padding: 8px 12px;
                border: 1px solid #ddd;
                background-color: #fff;
                cursor: pointer;
                border-radius: 3px;
            }
            .pagination button:hover {
                background-color: #ecf0f1;
            }
            .pagination button.active {
                background-color: #3498db;
                color: white;
                border-color: #3498db;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="sidebar">
                <h2>Warehouse<br>Manager</h2>
                <ul >
                    <li class="add-user-btn"><a>User Manager</a></li>
                    <li class="add-user-btn">Role Assignment</li>
                    <li class="add-user-btn">Material Information</li>
                    <li class="add-user-btn">Transaction</li>
                    <li class="add-user-btn">Statistic</li>
                </ul>
            </div>

            <div class="main-content">
                <div class="header">
                    <div><h2>Setting List</h2></div>
                    <div class="admin-logout">
                        <strong>Admin</strong> <a href="login.jsp">Log out</a>
                    </div>
                </div>

                <button class="add-user-btn">
                    <a href="AddUser.jsp">+ Add User</a>
                </button>

                <form action="UserFilterServlet" method="get" class="filter-bar">
                    <div class="filter-bar">
                        <select id="role" name="role">
                            <option value="Admin">Admin</option>
                            <option value="Warehouse Manager">Warehouse Manager</option>
                            <option value="Warehouse Staff">Warehouse Staff</option>
                            <option value="Company Director">Company Director</option>
                            <option value="Company Employee">Company Employee</option>
                        </select>


                        <select name="status">
                            <option>All Statuses</option>
                            <option value="active">Active</option>
                            <option value="inactive">Inactive</option>
                        </select>

                        <input type="text" name="keyword" placeholder="Enter keyword(s) to search">

                        <button class="search-btn">Search</button>
                    </div>
                </form>

                <table>
                    <thead  readonly style="background-color: #eee;">
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Fullname</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Created Date</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${userList}">
                            <tr>
                                <td>${user.id}</td>
                                <td>${user.username}</td>
                                <td>${user.fullname}</td>
                                <td>${user.roleName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.activeFlag == 1}">Active</c:when>
                                        <c:otherwise>Inactive</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${user.createDate}</td>
                                <td>
                                    <a href="edituser?id=${user.id}">Edit</a>
                                </td>

                            </tr>
                        </c:forEach>
                        <c:if test="${empty userList}">
                            <tr><td colspan="6">No users found.</td></tr>
                        </c:if>
                    </tbody>
                </table>

                <div class="pagination">
                    <button>&lt;</button>
                    <button class="active">1</button>
                    <button>2</button>
                    <button>...</button>
                    <button>9</button>
                    <button>10</button>
                    <button>&gt;</button>
                </div>
            </div>
        </div>
    </body>
</html>

