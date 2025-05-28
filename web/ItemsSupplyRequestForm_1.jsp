<%--
    Document   : ItemsSupplyRequestForm
    Created on : May 22, 2025, 5:47:12 PM
    Author     : Admin
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đơn Yêu Cầu Nhập Kho</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            body {
                background-color: #f0f2f5;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                padding: 20px;
            }

            .container {
                background: #fff;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                width: 100%;
                max-width: 1400px;
            }

            h1 {
                text-align: center;
                color: #333;
                margin-bottom: 20px;
                font-size: 28px;
            }

            h3 {
                text-align: center;
                color: #e63946;
                font-style: italic;
                margin: 20px 0;
            }

            .form-group {
                margin-bottom: 20px;
            }

            label {
                display: block;
                font-size: 14px;
                color: #555;
                margin-bottom: 8px;
                font-weight: 500;
            }

            input, select, textarea {
                width: 100%;
                padding: 12px;
                border: 1px solid #ddd;
                border-radius: 8px;
                font-size: 14px;
                transition: border-color 0.3s;
            }

            input:focus, select:focus, textarea:focus {
                outline: none;
                border-color: #007bff;
            }

            input[readonly] {
                background-color: #f8f9fa;
                cursor: not-allowed;
            }
            .information-user {
                display: flex;
                flex-wrap: wrap;
                gap: 15px;
                justify-content: center;
                margin-bottom: 20px;
            }

            .information-user .form-group {
                flex: 1;
                min-width: 150px;
            }

            .information-items {
                display: flex;
                flex-direction: column;
                gap: 15px;
            }

            .information-items .row {
                display: flex;
                gap: 15px;
                justify-content: center;
            }

            .information-items .row .form-group {
                flex: 1;
                min-width: 200px;
            }

            .items-table {
                margin-top: 20px;
            }

            .items-table table th:nth-child(1),
            .items-table table td:nth-child(1) {
                width: 2cm;
            }

            .items-table table th:nth-child(2),
            .items-table table td:nth-child(2) {
                width: 13cm;
            }

            .items-table table th:nth-child(3),
            .items-table table td:nth-child(3) {
                width: 2.2cm;
            }

            .items-table table th:nth-child(4),
            .items-table table td:nth-child(4) {
                width: 2cm;
            }

            .items-table table th:nth-child(5),
            .items-table table td:nth-child(5) {
                width: 2cm;
            }

            .items-table table th:nth-child(6),
            .items-table table td:nth-child(6) {
                width: 8cm;
            }

            .items-table table {
                width: 100%;
                border-collapse: collapse;
                margin: 15px 0;
            }

            .items-table th, .items-table td {
                border: 1px solid #ddd;
                padding: 10px;
                text-align: center;
                font-size: 14px;
            }

            .items-table th {
                background-color: #007bff;
                color: #fff;
            }

            .items-table textarea {
                border: none;
                resize: none;
                width: 100%;
                min-height: 30px;
                font-size: 14px;
            }

            .items-table textarea:focus {
                outline: none;
            }

            .add-row-btn {
                display: block;
                width: 200px;
                padding: 10px;
                background: #28a745;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 14px;
                margin: 15px auto;
                transition: background 0.3s;
            }

            .add-row-btn:hover {
                background: #218838;
            }

            .submit-btn {
                display: block;
                width: 5cm;
                padding: 12px;
                background: #007bff;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 16px;
                transition: background 0.3s;
                margin-left: auto;
                margin-right: auto;
            }

            .submit-btn:hover {
                background: #0056b3;
            }

            @media (max-width: 768px) {
                .container {
                    padding: 20px;
                }

                .information-user, .information-items {
                    flex-direction: column;
                    align-items: center;
                }

                .items-table table {
                    font-size: 12px;
                }

                .items-table th, .items-table td {
                    padding: 8px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>ĐƠN YÊU CẦU NHẬP KHO</h1>
            <form id="myForm" action="loadingrequest" method="post">
                <h3>Thông tin</h3>
                <div class="information-user">
                    <div class="form-group">
                        <label>Người dùng</label>
                        <input type="text" value="QuocMDB" readonly>
                    </div>
                    <div class="form-group">
                        <label>Tuổi</label>
                        <input type="text" value="21" readonly>
                    </div>
                    <div class="form-group">
                        <label>Ngày tháng năm sinh</label>
                        <input type="text" value="20/09/2004" readonly>
                    </div>
                    <div class="form-group">
                        <label>Mã số nhân viên</label>
                        <input type="text" readonly>
                    </div>
                    <div class="form-group">
                        <label>Vai trò</label>
                        <input type="text" value="Nhân viên" readonly>
                    </div>
                    <div class="form-group">
                        <label>Ngày yêu cầu</label>
                        <input type="date" name="day_request" required>
                    </div>
                </div>
                <h3>Chi tiết</h3>
                <div class="information-items">
                    <div class="row">
                        <div class="form-group">
                            <label>ID</label>
                            <textarea rows="1" name="request_id" style="width: 100%; resize: none;overflow: hidden;background-color: #f8f9fa; cursor: not-allowed;" oninput="autoResize(this)" readonly>${requestScope.nextID}</textarea>
                        </div>
                        <div class="form-group">
                            <label>Mục đích</label>
                            <textarea rows="1" name="reason" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" required></textarea>
                        </div>
                        <div class="form-group">
                            <label>Tên nhà cung cấp</label>
                            <textarea rows="1" name="supplier" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" required></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group">
                            <label>Địa chỉ</label>
                            <textarea rows="1" name="address" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" required></textarea>
                        </div>
                        <div class="form-group">
                            <label>Điện thoại</label>
                            <textarea rows="1" name="phone" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" required></textarea>
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <textarea rows="1" name="email" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" required></textarea>
                        </div>
                    </div>
                </div>
                <div class="items-table">
                    <label>Vui lòng nhập chi tiết mặt hàng</label>
                    <button type="button" class="add-row-btn" onclick="addRow()">Thêm hàng</button>
                    <table>
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Tên mặt hàng</th>
                                <th>Code</th>
                                <th>Đơn vị</th>
                                <th>Số lượng</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody id="itemsTableBody">
                            <tr>
                                <td><textarea name="stt" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                                <td><textarea name="product_name" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                                <td><textarea name="product_code" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                                <td><textarea name="unit" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                                <td><textarea name="quantity" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                                <td><textarea name="note" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="form-group">
                        <label>Lý do chi tiết</label>
                        <textarea rows="3" name="reason_detail" style="width: 100%; border: 1px solid #ddd; resize: none;" oninput="autoResize(this)" required></textarea>
                    </div>
                </div>
                <button type="submit" class="submit-btn">Gửi yêu cầu</button>
            </form>
        </div>
        <script>
            function autoResize(textarea) {
                textarea.style.height = 'auto';
                textarea.style.height = textarea.scrollHeight + 'px';
            }

            function addRow() {
                const tbody = document.getElementById('itemsTableBody');
                const newRow = document.createElement('tr');

                newRow.innerHTML = `
        <td><textarea name="stt" ...></textarea></td>
        <td><textarea name="product_name" ...></textarea></td>
        <td><textarea name="product_code" ...></textarea></td>
        <td><textarea name="unit" ...></textarea></td>
        <td><textarea name="quantity" ...></textarea></td>
        <td><textarea name="note" ...></textarea></td>
    `;
                tbody.appendChild(newRow);
            }

        </script>
    </body>
</html>