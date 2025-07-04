<%-- 
    Document   : EditUser
    Created on : 23 thg 5, 2025, 16:24:57
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
if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<html>
    <head>
        <title>Sửa đổi thông tin người dùng</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f9;
                padding: 30px;
            }
            .form-container {
                max-width: 600px;
                margin: 32px auto;
                background: white;
                padding: 32px 32px 24px 32px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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
            .success-message {
                color: green;
                margin-bottom: 15px;
                text-align: center;
            }
            .error-message {
                color: red;
                margin-bottom: 15px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>Sửa đổi thông tin người dùng</h2>

            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>
            <c:if test="${not empty sessionScope.message}">
                <div class="success-message">${sessionScope.message}</div>
                <c:remove var="message" scope="session"/>
            </c:if>


            <form action="edituser" method="post">
                <input type="hidden" name="id" value="${editUser.id}"/>
                <table>
                    <tr>
                        <td class="label"><label for="username">Username:</label></td>
                        <td class="input"><input type="text" id="username" name="username" value="${editUser.username}" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="password">Mật khẩu:</label></td>
                        <td class="input"><input type="text" id="password" name="password" value="${editUser.password}" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="fullname">Họ và Tên:</label></td>
                        <td class="input"><input type="text" id="fullname" name="fullname" value="${editUser.fullname}" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="email">Email:</label></td>
                        <td class="input"><input type="email" id="email" name="email" value="${editUser.email}"
                                                 required pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
                                                 title="Email phải đúng định dạng: example@domain.com">
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><label for="phone">SĐT:</label></td>
                        <td class="input"><input type="text" id="phone" name="phone" value="${editUser.phone}" 
                                                 required pattern="^[0-9]{10}$"
                                                 title="Số điện thoại chỉ được chứa chữ số">
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><label for="dob">Sinh nhật:</label></td>
                        <td class="input"><input type="date" id="dob" name="dob" value="${editUser.dob}" required></td>
                    </tr>
                    <tr>
                        <td class="label"><label for="role">Vai trò:</label></td>
                        <td class="input">
                            <div class="row-flex">
                                <div>
                                    <select id="role" name="role" required>
                                        <option value="">-- Select Role --</option>
                                        <option value="2" ${editUser.roleName == 'Warehouse Staff' ? 'selected' : ''}>Nhân viên kho</option>
                                        <option value="3" ${editUser.roleName == 'Company Employee' ? 'selected' : ''}>Nhân viên công ty</option>
                                        <option value="4" ${editUser.roleName == 'Company Director' ? 'selected' : ''}>Giám đốc</option>
                                    </select>
                                </div>
                                <div>
                                    <select id="activeFlag" name="activeFlag" required>
                                        <option value="1" ${editUser.activeFlag == 1 ? 'selected' : ''}>Hoạt động</option>
                                        <option value="0" ${editUser.activeFlag == 0 ? 'selected' : ''}>Không hoạt động</option>
                                    </select>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
                <button type="submit" class="btn-submit">Cập nhật thông tin</button>
            </form>
            <a href="usermanager" class="back-link">Quay lại danh sách người dùng</a>
        </div>
    </body>
</html>
