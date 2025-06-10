<%-- 
    Document   : password_request
    Created on : 10 thg 6, 2025, 14:33:01
    Author     : phucn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản lý yêu cầu quên mật khẩu</title>
        <style>
            body {
                background: #f8f9fa;
                font-family: Arial, sans-serif;
            }
            .admin-container {
                max-width: 900px;
                margin: 40px auto;
                background: #fff;
                padding: 30px;
                border-radius: 14px;
                box-shadow: 0 2px 16px rgba(0,0,0,0.08);
            }
            h2 {
                text-align: center;
                margin-bottom: 18px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
            }
            th, td {
                border-bottom: 1px solid #ddd;
                padding: 10px;
                text-align: left;
            }
            th {
                background: #f2f2f2;
            }
            .btn-process {
                background: #27ae60;
                color: #fff;
                border: none;
                padding: 7px 16px;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-process:hover {
                background: #219150;
            }
            .status-pending {
                color: #e67e22;
            }
            .status-done {
                color: #27ae60;
            }
            .status-denied {
                color: #e74c3c;
            }
        </style>
    </head>
    <body>
        <div class="admin-container">
            <h2>Quản lý yêu cầu cấp lại mật khẩu</h2>
            <!-- Có thể hiển thị thông báo thành công/thất bại ở đây -->
            <table>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Lý do</th>
                    <th>Thời gian gửi</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                
            </table>
        </div>
    </body>
</html>

