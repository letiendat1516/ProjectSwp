<%-- 
    Document   : UserManager
    Created on : 27 thg 5, 2025, 14:39:06
    Author     : phucn
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" session="true" %> 
<!DOCTYPE html>

    <%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

    
        <%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>

<html>
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
            .pagination a, .pagination button {
                padding: 8px 12px;
                margin: 0 2px;
                border: 1px solid #ddd;
                text-decoration: none;
                color: #3498db;
                border-radius: 3px;
                cursor: pointer;
            }

            .pagination button.active {
                background-color: #3498db;
                color: white;
                border-color: #3498db;
            }

            .pagination a:hover {
                background-color: #ecf0f1;
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
                    <li class="add-user-btn"><a href="RequestForward.jsp" >Xuất nhập kho</a></li>
                    <li class="add-user-btn"><a href="ApproveListForward.jsp" >Phê duyệt</a></li>
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

                <button class="add-user-btn">
                    <a href="AddUser.jsp">+ Add User</a>
                </button>

                <form action="userfilter" method="get" class="filter-bar">
                    <div class="filter-bar">
                        <select id="role" name="role">
                            <option value="all" selected>All Roles</option>
                            <option value="2">Warehouse Staff</option>
                            <option value="3">Company Employee</option>
                            <option value="4">Company Director</option>
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
                    <thead style="background-color: #eee;">
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
                            <tr><td colspan="7">No users found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
                
                
                <div class="pagination">
                    <p>Total pages: ${totalPages}</p>
                    <c:if test="${totalPages > 1}">
                        <c:if test="${currentPage > 1}">
                            <a href="admin?page=${currentPage - 1}">&lt; Prev</a>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <button class="active">${i}</button>
                                </c:when>
                                <c:otherwise>
                                    <a href="admin?page=${i}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <a href="admin?page=${currentPage + 1}">Next &gt;</a>
                        </c:if>
                    </c:if>
                </div>

            </div>
        </div>
    </body>
</html>


