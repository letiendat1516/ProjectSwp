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
                margin: 30px auto;
                background: white;
                padding: 32px 32px 24px 32px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.10);
            }
            h2 {
                text-align: center;
                margin-bottom: 30px;
                font-size: 2rem;
            }
            table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0 10px;
            }
            td.label {
                width: 32%;
                font-weight: bold;
                text-align: right;
                padding-right: 16px;
                vertical-align: middle;
            }
            td.input {
                width: 68%;
            }
            input[type="text"], input[type="password"], input[type="email"], input[type="date"], select {
                width: 100%;
                padding: 10px 12px;
                border-radius: 4px;
                border: 1px solid #ccc;
                font-size: 1rem;
                box-sizing: border-box;
            }
            .row-flex {
                display: flex;
                gap: 18px;
            }
            .row-flex > div {
                flex: 1;
            }
            .btn-submit {
                margin-top: 28px;
                width: 100%;
                padding: 14px;
                background-color: #3498db;
                border: none;
                border-radius: 5px;
                color: white;
                font-size: 1.18rem;
                cursor: pointer;
                font-weight: bold;
            }
            .btn-submit:hover {
                background-color: #2980b9;
            }
            .back-link {
                display: block;
                margin-top: 17px;
                text-align: center;
                color: #3498db;
                text-decoration: none;
                font-size: 1.04rem;
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
                    session.removeAttribute("message");
                }
            %>
            <form action="adduser" method="post">
                <table>
                    <tr>
                        <td class="label"><label for="username">Username:</label></td>
                        <td class="input"><input type="text" id="username" name="username" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="password">Password:</label></td>
                        <td class="input"><input type="password" id="password" name="password" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="fullname">Full Name:</label></td>
                        <td class="input"><input type="text" id="fullname" name="fullname" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="email">Email:</label></td>
                        <td class="input"><input type="email" id="email" name="email" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="phone">Phone:</label></td>
                        <td class="input"><input type="text" id="phone" name="phone" maxlength="20" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="dob">Date of Birth:</label></td>
                        <td class="input"><input type="date" id="dob" name="dob" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="role">Role:</label></td>
                        <td class="input">
                            <div class="row-flex">
                                <div>
                                    <select id="role" name="role" required>
                                        <option value="">-- Select Role --</option>
                                        <option value="2">Warehouse Staff</option>
                                        <option value="3">Company Director</option>
                                        <option value="4">Company Employee</option>
                                    </select>
                                </div>
                                <div>
                                    <select id="activeFlag" name="activeFlag" required>
                                        <option value="1" selected>Active</option>
                                        <option value="0">Inactive</option>
                                    </select>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
                <button type="submit" class="btn-submit">Add User</button>
            </form>
            <a href="admin" class="back-link">Back to User List</a>
        </div>
    </body>
</html>



