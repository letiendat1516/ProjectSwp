<%-- 
    Document   : forgot
    Created on : 19 thg 5, 2025, 22:57:15
    Author     : phucn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Forgot Password</title>
        <style>
            body {
                background: #f4f4f9;
                font-family: Arial, sans-serif;
            }
            .forgot-container {
                max-width: 400px;
                margin: 60px auto;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 6px 32px rgba(0,0,0,.1);
                padding: 32px 28px 24px 28px;
            }
            h2 {
                text-align: center;
                margin-bottom: 28px;
            }
            label {
                display: block;
                margin-bottom: 6px;
                font-weight: bold;
            }
            input, textarea {
                width: 100%;
                padding: 10px;
                margin-bottom: 16px;
                border-radius: 5px;
                border: 1px solid #ccc;
            }
            button {
                width: 100%;
                background: #3498db;
                color: #fff;
                padding: 12px;
                border: none;
                border-radius: 6px;
                font-size: 1.08rem;
                cursor: pointer;
            }
            button:hover {
                background: #2980b9;
            }
            .msg {
                color: green;
                text-align: center;
            }
            .err {
                color: red;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div class="forgot-container">
            <h2>Forgot Password</h2>
            <!-- Có thể hiển thị thông báo thành công/thất bại ở đây -->
            <form action="forgotPassword" method="post">
                <label for="username">Username hoặc Email:</label>
                <input type="text" id="username" name="username" required>

                <label for="reason">Lý do (tuỳ chọn):</label>
                <textarea id="reason" name="reason" rows="3"></textarea>

                <button type="submit">Gửi yêu cầu</button>
            </form>
        </div>
    </body>
</html>



