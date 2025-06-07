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
                width: 150px;
                padding: 12px;
                background: #007bff;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 14px;
                transition: background 0.3s;
            }

            .back-btn {
                width: 150px;
                padding: 12px;
                background: #6c757d;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 14px;
                text-align: center;
                text-decoration: none;
                transition: background 0.3s;
            }

            .submit-btn:hover {
                background: #0056b3;
            }

            .back-btn:hover {
                background: #5a6268;
            }

            /* Container để căn giữa hai nút */
            .button-container {
                display: flex;
                justify-content: center; /* Căn giữa ngang */
                align-items: center; /* Căn giữa dọc */
                gap: 15px; /* Khoảng cách giữa hai nút */
                margin-top: 20px;
                flex-wrap: wrap; /* Đảm bảo nút không bị tràn trên màn hình nhỏ */
            }

            .items-table table td:nth-child(1),
            .items-table table td:nth-child(3),
            .items-table table td:nth-child(4),
            .items-table table td:nth-child(5) {
                text-align: center;
            }

            .items-table textarea[name="stt"],
            .items-table input[name="product_code"],
            .items-table textarea[name="unit"],
            .items-table textarea[name="quantity"] {
                text-align: center;
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

                .submit-btn, .back-btn {
                    width: 150px;
                    font-size: 12px;
                }

                .button-container {
                    flex-direction: column;
                    align-items: center;
                    display: flex;
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
                        <input type="text" value="${sessionScope.currentUser}" readonly> 
                    </div>
                    <div class="form-group">
                        <label>Tuổi</label>
                        <input type="text" value="${age}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Ngày tháng năm sinh</label>
                        <input type="text" value="${sessionScope.DoB}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Vai trò</label>
                        <select name="role" required>
                            <option value="" disabled selected>-- Chọn vai trò --</option>
                            <option value="Nhân viên">Nhân viên</option>
                            <option value="Giám đốc">Giám đốc</option>
                            <option value="Admin">Admin</option>
                        </select>
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
                            <label>Gợi ý nhà cung cấp</label>
                            <textarea rows="1" name="supplier" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)"></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group">
                            <label>Địa chỉ</label>
                            <textarea rows="1" name="address" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)"></textarea>
                        </div>
                        <div class="form-group">
                            <label>Điện thoại</label>
                            <input rows="1" name="phone" id="phone" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" pattern="0[0-9]{9}" title="Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số">
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <textarea rows="1" name="email" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)"></textarea>
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
                                <!-- Dropdown chọn tên sản phẩm -->
                                <td>
                                    <select name="product_name" onchange="updateProductInfo(this)" style="width: 100%;">
                                        <option value="" disabled selected>-- Chọn sản phẩm --</option>
                                        <c:forEach var="p" items="${products_list}">
                                            <option 
                                                value="${p.name}" 
                                                data-code="${p.code}" 
                                                data-unit="${p.unit_id}">
                                                ${p.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <!-- Code tương ứng đễ readonly -->
                                <td><input type="text" name="product_code" readonly style="width: 100%;" /></td>
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
                <div class="button-container">
                    <button type="submit" class="submit-btn">Gửi yêu cầu</button>
                    <a href="admin" class="back-btn">Quay lại</a>
                </div>
            </form>
        </div>
        <script>
            function autoResize(textarea) {
                textarea.style.height = 'auto';
                textarea.style.height = textarea.scrollHeight + 'px';
            }

            function addRow() {
                const tbody = document.getElementById('itemsTableBody');
                const firstSelect = document.querySelector("select[name='product_name']");

                const newRow = document.createElement('tr');

                //Copy danh sách sản phẩm từ select đầu tiên
                let productOptions = "";
                if (firstSelect) {
                    productOptions = [...firstSelect.options].map(opt =>
                            `<option value="${opt.value}" data-code="${opt.getAttribute('data-code')}" data-unit="${opt.getAttribute('data-unit')}">
            ${opt.text}
             </option>`).join('');
                }

                newRow.innerHTML = `
        <td><textarea name="stt" rows="1" style="width: 100%; resize: none;" oninput="autoResize(this)"></textarea></td>

        <td>
            <select name="product_name" onchange="updateProductInfo(this)" style="width: 100%;">
                   <option value="" disabled selected>-- Chọn sản phẩm --</option>
            <c:forEach var="p" items="${products_list}">
                              <option 
                              value="${p.id}" 
                              data-code="${p.code}" 
                              data-unit="${p.unit_id}">
                ${p.name}
                              </option>
            </c:forEach>
            </select>
        </td>

        <td><input type="text" name="product_code" readonly style="width: 100%;" /></td>

        <td><textarea name="unit" rows="1" style="width: 100%; resize: none;" oninput="autoResize(this)"></textarea></td>

        <td><textarea name="quantity" rows="1" style="width: 100%; resize: none;" oninput="autoResize(this)"></textarea></td>

        <td><textarea name="note" rows="1" style="width: 100%; resize: none;" oninput="autoResize(this)"></textarea></td>
    `;

                tbody.appendChild(newRow);
            }
            function updateProductInfo(selectElement) {
                const selectedOption = selectElement.options[selectElement.selectedIndex];
                const code = selectedOption.getAttribute("data-code");

                const row = selectElement.closest("tr");
                row.querySelector("input[name='product_code']").value = code;
            }
        </script>
    </body>
</html>