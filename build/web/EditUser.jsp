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
        <title>Edit User</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                margin: 0;
                padding: 0;
                min-height: 100vh;
            }

            .layout-container {
                display: flex;
                min-height: 100vh;
            }

            .main-content {
                flex: 1;
                padding: 20px;
                background: #f5f7fa;
            }

            .form-container {
                max-width: 800px;
                margin: 20px auto;
                background: white;
                padding: 40px;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            }

            h2 {
                text-align: center;
                margin-bottom: 30px;
                font-size: 2.2rem;
                color: #333;
                font-weight: 600;
            }

            .success-message {
                background: #d4edda;
                color: #155724;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                border: 1px solid #c3e6cb;
                text-align: center;
            }

            .error-message {
                background: #f8d7da;
                color: #721c24;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                border: 1px solid #f5c6cb;
                text-align: center;
            }

            table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0 15px;
            }

            td.label {
                width: 30%;
                font-weight: 600;
                text-align: right;
                padding-right: 20px;
                vertical-align: middle;
                color: #495057;
            }

            td.input {
                width: 70%;
            }

            input[type="text"], 
            input[type="password"], 
            input[type="email"], 
            input[type="date"], 
            select {
                width: 100%;
                padding: 12px 15px;
                border-radius: 8px;
                border: 2px solid #e9ecef;
                font-size: 1rem;
                box-sizing: border-box;
                transition: border-color 0.3s ease;
                font-family: inherit;
            }

            input[type="text"]:focus, 
            input[type="password"]:focus, 
            input[type="email"]:focus, 
            input[type="date"]:focus, 
            select:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .row-flex {
                display: flex;
                gap: 20px;
            }

            .row-flex > div {
                flex: 1;
            }

            .btn-submit {
                margin-top: 30px;
                width: 100%;
                padding: 15px;
                background: linear-gradient(45deg, #667eea, #764ba2);
                border: none;
                border-radius: 8px;
                color: white;
                font-size: 1.1rem;
                cursor: pointer;
                font-weight: 600;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            .btn-submit:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
            }

            .back-link {
                display: block;
                margin-top: 20px;
                text-align: center;
                color: #667eea;
                text-decoration: none;
                font-size: 1.1rem;
                font-weight: 500;
                transition: color 0.3s ease;
            }

            .back-link:hover {
                color: #764ba2;
                text-decoration: underline;
            }

            .form-section {
                margin-bottom: 25px;
            }

            .form-section h3 {
                color: #495057;
                margin-bottom: 15px;
                padding-bottom: 10px;
                border-bottom: 2px solid #e9ecef;
                font-size: 1.2rem;
            }

            @media (max-width: 768px) {
                .form-container {
                    margin: 10px;
                    padding: 20px;
                }

                .row-flex {
                    flex-direction: column;
                    gap: 10px;
                }

                td.label {
                    text-align: left;
                    padding-right: 0;
                    padding-bottom: 5px;
                }

                table {
                    border-spacing: 0 10px;
                }
            }
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <div class="form-container">
                    <h2>✏️ Edit User</h2>

                    <c:if test="${not empty error}">
                        <div class="error-message">
                            <strong>❌ Error:</strong> ${error}
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.message}">
                        <div class="success-message">
                            <strong>✅ Success:</strong> ${sessionScope.message}
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <form action="edituser" method="post">
                        <input type="hidden" name="id" value="${editUser.id}"/>
                        
                        <div class="form-section">
                            <h3>👤 Basic Information</h3>
                            <table>
                                <tr>
                                    <td class="label"><label for="username">Username:</label></td>
                                    <td class="input">
                                        <input type="text" id="username" name="username" 
                                               value="${editUser.username}" required
                                               pattern="^[a-zA-Z0-9_]{3,20}$"
                                               title="Username must be 3-20 characters, only letters, numbers and underscore">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label"><label for="password">Password:</label></td>
                                    <td class="input">
                                        <input type="password" id="password" name="password" 
                                               value="${editUser.password}" required
                                               minlength="6"
                                               title="Password must be at least 6 characters">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label"><label for="fullname">Full Name:</label></td>
                                    <td class="input">
                                        <input type="text" id="fullname" name="fullname" 
                                               value="${editUser.fullname}" required
                                               maxlength="100"
                                               title="Full name is required">
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="form-section">
                            <h3>📧 Contact Information</h3>
                            <table>
                                <tr>
                                    <td class="label"><label for="email">Email:</label></td>
                                    <td class="input">
                                        <input type="email" id="email" name="email" 
                                               value="${editUser.email}" required
                                               pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
                                               title="Please enter a valid email address">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label"><label for="phone">Phone:</label></td>
                                    <td class="input">
                                        <input type="text" id="phone" name="phone" 
                                               value="${editUser.phone}" required
                                               pattern="^[0-9]{10,11}$"
                                               title="Phone number must be 10-11 digits">
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label"><label for="dob">Date of Birth:</label></td>
                                    <td class="input">
                                        <input type="date" id="dob" name="dob" 
                                               value="${editUser.dob}" required
                                               max="2010-12-31"
                                               title="Please select a valid date of birth">
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="form-section">
                            <h3>🔐 Role & Status</h3>
                            <table>
                                <tr>
                                    <td class="label"><label for="role">Role & Status:</label></td>
                                    <td class="input">
                                        <div class="row-flex">
                                            <div>
                                                <label for="role" style="display: block; margin-bottom: 5px; font-weight: 500;">Role:</label>
                                                <select id="role" name="role" required>
                                                    <option value="">-- Select Role --</option>
                                                    <option value="2" ${editUser.roleName == 'Warehouse Staff' ? 'selected' : ''}>
                                                        Warehouse Staff
                                                    </option>
                                                    <option value="3" ${editUser.roleName == 'Company Employee' ? 'selected' : ''}>
                                                        Company Employee
                                                    </option>
                                                    <option value="4" ${editUser.roleName == 'Company Director' ? 'selected' : ''}>
                                                        Company Director
                                                    </option>
                                                </select>
                                            </div>
                                            <div>
                                                <label for="activeFlag" style="display: block; margin-bottom: 5px; font-weight: 500;">Status:</label>
                                                <select id="activeFlag" name="activeFlag" required>
                                                    <option value="1" ${editUser.activeFlag == 1 ? 'selected' : ''}>
                                                        🟢 Active
                                                    </option>
                                                    <option value="0" ${editUser.activeFlag == 0 ? 'selected' : ''}>
                                                        🔴 Inactive
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <button type="submit" class="btn-submit">💾 Update User</button>
                    </form>
                    
                    <a href="usermanager" class="back-link">← Back to User List</a>
                </div>
            </div>
        </div>

        <script>
            // Form validation
            document.querySelector('form').addEventListener('submit', function(e) {
                const requiredFields = document.querySelectorAll('[required]');
                let isValid = true;

                requiredFields.forEach(field => {
                    if (!field.value.trim()) {
                        field.style.borderColor = '#dc3545';
                        isValid = false;
                    } else {
                        field.style.borderColor = '#e9ecef';
                    }
                });

                if (!isValid) {
                    e.preventDefault();
                    alert('Please fill in all required fields.');
                }
            });

            // Remove error styling on input
            document.querySelectorAll('input, select').forEach(field => {
                field.addEventListener('input', function() {
                    this.style.borderColor = '#e9ecef';
                });
            });

            // Set max date for date of birth (must be at least 15 years old)
            const dobField = document.getElementById('dob');
            const today = new Date();
            const maxDate = new Date(today.getFullYear() - 15, today.getMonth(), today.getDate());
            dobField.max = maxDate.toISOString().split('T')[0];
        </script>
    </body>
</html>
