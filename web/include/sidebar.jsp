<%-- 
Document   : sidebar
Created on : [Date]
Author     : [Your name]
Description: Reusable sidebar component - extracted from Dashboard.jsp
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Users" session="true" %>

<%
Users user = (Users) session.getAttribute("user");
if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<style>
  body {
      font-family: 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
      background: #f0f4f8;
      color: #222;
      margin: 0;
      padding: 0;
  }
  .container {
      display: flex;
      min-height: 100vh;
  }
  .sidebar {
      width: 250px;
      background: #e6f0fa;
      padding: 20px 0;
      border-right: 1px solid #d6e0ef;
  }
  .sidebar h2 {
      font-size: 1.4rem;
      color: #1567c1;
      text-align: center;
      margin-bottom: 20px;
  }
  .nav-item {
      display: block;
      padding: 10px 20px;
      color: #214463;
      text-decoration: none;
      font-size: 1rem;
  }
  .nav-item:hover {
      background: #c2e9fb;
      color: #1567c1;
  }
  .nav-item.active {
      background: #c2e9fb;
      color: #1567c1;
      font-weight: bold;
  }
  .main-content {
      flex: 1;
      padding: 20px;
  }
  .header {
      background: #fff;
      padding: 15px;
      border-bottom: 1px solid #d6e0ef;
      margin-bottom: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
  }
  .header-title {
      font-size: 1.8rem;
      color: #1567c1;
      margin: 0;
      display: flex;
      align-items: center;
  }
  .header-user {
      display: flex;
      align-items: center;
  }
  .user-name {
      font-size: 1rem;
      color: #214463;
      margin-right: 15px;
  }
  .logout-btn {
      background: red;
      color: #fff;
      border: #007BFF;
      padding: 8px 16px;
      border-radius: 4px;
      cursor: pointer;
      text-decoration: none;
  }
  .logout-btn:hover {
      background: orange;
  }
  .dashboard-content {
      background: #fff;
      padding: 20px;
      border: 1px solid #d6e0ef;
      border-radius: 8px;
  }
  .page-title {
      font-size: 1.6rem;
      color: #222e45;
      margin-bottom: 10px;
  }
  .notification {
      color: #5a7da0;
      font-size: 1.1rem;
  }
  @media (max-width: 900px) {
      .container {
          flex-direction: column;
      }
      .sidebar {
          width: 100%;
      }
  }
</style>

<div class="sidebar">
  <h2>Warehouse Manager</h2>
  <a href="${pageContext.request.contextPath}/usermanager" class="nav-item ${param.activePage == 'usermanager' ? 'active' : ''}">User Manager</a>
  <a href="${pageContext.request.contextPath}/roleAssignment" class="nav-item ${param.activePage == 'roleAssignment' ? 'active' : ''}">Role Assignment</a>
  <a href="${pageContext.request.contextPath}/categoriesforward.jsp" class="nav-item ${param.activePage == 'categories' ? 'active' : ''}">Material Information</a>
  <a href="${pageContext.request.contextPath}/passwordrequest" class="nav-item ${param.activePage == 'passwordrequest' ? 'active' : ''}">Password Request</a>
  <a href="${pageContext.request.contextPath}/ApproveListForward.jsp" class="nav-item ${param.activePage == 'approve' ? 'active' : ''}">Approve</a>
  <a href="${pageContext.request.contextPath}/RequestForward.jsp" class="nav-item ${param.activePage == 'transaction' ? 'active' : ''}">Transaction</a>
  <a href="#" class="nav-item ${param.activePage == 'statistic' ? 'active' : ''}">Statistic</a>
</div>