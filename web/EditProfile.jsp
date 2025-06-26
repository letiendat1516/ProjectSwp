<%-- 
    Document   : EditProfile
    Created on : 7 thg 6, 2025, 16:56:05
    Author     : phucn
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <title>Edit Profile</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f5f5fa;
                padding: 30px;
            }
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
            .layout-container {
                display: flex;
                min-height: 100vh;
            }

            .main-content {
                flex: 1;
                padding: 20px;
                background: #f5f5f5;
            }
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
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
            <a href="profile" class="back-link">Back to Profile</a>
            <a href="<%= homePage %>" class="back-link">Back</a>
        </div>
        </div>
    </body>
</html>

