<%-- 
    Document   : AddUser
    Created on : 22 thg 5, 2025, 22:38:49
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
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Phân quyền theo vai trò</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
        }
        th {
            background: #e4e4e4;
        }
        .permission-row {
            background: #fafafa;
        }
    </style>
</head>
<body>
    <h2 style="align-items: center">PHÂN QUYỀN CHỨC NĂNG THEO VAI TRÒ</h2>
    <center><a href="Admin.jsp">Quay lại</a></center>
<form action="role-permission" method="post">
    <table>
        <tr>
            <th>Chức năng</th>
            <c:forEach var="role" items="${roles}">
                <th>${role.roleName}</th>
            </c:forEach>
        </tr>
        <c:forEach var="perm" items="${permissions}">
            <tr class="permission-row">
                <td>${perm.name}</td>
                <c:forEach var="role" items="${roles}">
                    <td style="text-align: center;">
                        <input 
                            type="checkbox" 
                            name="perm_${perm.id}_role_${role.id}" 
                            <c:if test="${rolePermissions[role.id] != null && rolePermissions[role.id].contains(perm.id)}">checked</c:if>
                        />
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
    <br>
    <button type="submit">Lưu phân quyền</button>
</form>
</body>
</html>