<%-- 
    Document   : Dashboard
    Created on : May 19, 2025, 9:38:23 PM
    Author     : phucn
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" session="true" %> 
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>

<%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>




<html>
    <head>
        <title>Admin</title>
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
                position: fixed;
                top: 0;
                left: 0;
                width: 250px;
                height: 100vh;
                background-color: #2c3e50;
                color: white;
                padding: 20px;
                overflow-y: auto;
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
                    <li class="add-user-btn">Transaction</li>
                    <li class="add-user-btn">Statistic</li>
                </ul>
            </div>

            <div class="main-content">
                <div class="header">
                    <div><h2>Admin Dashboard</h2></div>
                    <div class="admin-logout">
                        <strong>Admin</strong><a href="logout">Log out</a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

