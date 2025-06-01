<%-- 
    Document   : AddUser
    Created on : 22 thg 5, 2025, 22:38:49
    Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
        <title>Add New User</title>
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
            input[type="text"], input[type="password"], input[type="email"], select {
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
            <h2>Add New User</h2>
            <%
    String message = (String) session.getAttribute("message");
    if (message != null) {
            %>
            <div style="color: green; margin-bottom: 15px;">
                <%= message %>
            </div>
            <%
                    // Xóa thông báo sau khi hiển thị
                    session.removeAttribute("message");
                }
            %>

            <form action="adduser" method="post">
                <table>
                    <tr>
                        <td class="label" style="text-align: left; width: 30%;"><label for="username">Username:</label></td>
                        <td colspan="3"><input type="text" id="username" name="username" required style="width: 100%;"></td>
                    </tr>
                    <tr>
                        <td class="label" style="text-align: left;"><label for="password">Password:</label></td>
                        <td colspan="3"><input type="password" id="password" name="password" required style="width: 100%;"></td>
                    </tr>
                    <tr>
                        <td class="label" style="text-align: left;"><label for="fullname">Full Name:</label></td>
                        <td colspan="3"><input type="text" id="fullname" name="fullname" required style="width: 100%;"></td>
                    </tr>
                    <tr>
                        <td class="label" style="text-align: left;"><label for="email">Email:</label></td>
                        <td colspan="3"><input type="email" id="email" name="email" required style="width: 100%;"></td>
                    </tr>

                    <tr>
                        <td class="label" style="text-align: left; padding-right: 10px; width: 15%;">
                            <label for="role">Role:</label>
                        </td>
                        <td style="width: 35%;">
                            <select id="role" name="role" required style="width: 100%;">
                                <option value="">-- Select Role --</option>
                                <option value="2">Warehouse Staff</option>
                                <option value="3">Company Director</option>
                                <option value="4">Company Employee</option>
                            </select>
                        </td>

                        <td class="label" style="text-align: left; padding-left: 30px; width: 15%;">
                            <label for="activeFlag">Status:</label>
                        </td>
                        <td style="width: 35%;">
                            <select id="activeFlag" name="activeFlag" required style="width: 100%;">
                                <option value="1" selected>Active</option>
                                <option value="0">Inactive</option>
                            </select>
                        </td>
                    </tr>
                </table>


                <button type="submit" class="btn-submit">Add User</button>
            </form>

            <a href="Admin.jsp" class="back-link">Back to User List</a>
        </div>
    </body>
</html>


