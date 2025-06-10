<%-- 
    Document   : profile
    Created on : 7 thg 6, 2025, 15:42:05
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

     String homePage = "homepage.jsp";
     String role = currentUser.getRoleName();
     if ("Admin".equalsIgnoreCase(role)) {
         homePage = "Admin.jsp";
     } 
     else if ("Nhân viên kho".equalsIgnoreCase(role)) {
        homePage = "categoriesforward.jsp";          
     }
     else{
        homePage = "homepage.jsp"; 
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>My Profile</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f6f6f8; padding: 30px; }
        .profile-container { background: #fff; border-radius: 8px; max-width: 460px; margin: 36px auto; box-shadow: 0 2px 10px rgba(0,0,0,0.08); padding: 38px 34px 22px 34px; }
        h2 { text-align: center; margin-bottom: 24px; }
        table { width: 100%; }
        td { padding: 10px 6px; font-size: 1.06rem; }
        td.label { font-weight: bold; color: #3498db; width: 34%; }
        .edit-link, .logout-link {
            display: inline-block; margin: 22px 7px 0 0; text-decoration: none; color: #fff; background: #3498db;
            padding: 9px 22px; border-radius: 5px; font-weight: bold;
            transition: background 0.18s;
        }
        .logout-link { background: #e74c3c; }
        .edit-link:hover { background: #217dbb; }
        .logout-link:hover { background: #c0392b; }
        .btn-row { text-align: center; margin-top: 14px; }
    </style>
</head>
<body>
    <div class="profile-container">
        <h2>My Profile</h2>
        <table>
            <tr><td class="label">Username:</td><td>${user.username}</td></tr>
            <tr><td class="label">Full Name:</td><td>${user.fullname}</td></tr>
            <tr><td class="label">Email:</td><td>${user.email}</td></tr>
            <tr><td class="label">Phone:</td><td>${user.phone}</td></tr>
            <tr><td class="label">Date of Birth:</td><td>
                <c:choose>
                    <c:when test="${not empty user.dob}">${user.dob}</c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td></tr>
            <tr><td class="label">Role:</td><td>${user.roleName}</td></tr>
            <tr><td class="label">Status:</td>
                <td>
                    <c:choose>
                        <c:when test="${user.activeFlag == 1}">Active</c:when>
                        <c:otherwise>Inactive</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
        <div class="btn-row">
            <a href="<%= homePage %>" class="edit-link">Back</a>
            <a href="editprofile" class="edit-link">Edit Profile</a>
            <a href="logout" class="logout-link">Log Out</a>
        </div>
    </div>
</body>
</html>


