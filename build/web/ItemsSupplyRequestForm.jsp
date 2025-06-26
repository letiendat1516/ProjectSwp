<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn Yêu Cầu Mua Hàng</title>
    <style>
        /* Reset CSS và font chung */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        /* Layout chính */
        body {
            background-color: #f0f2f5;
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

        /* Form elements chung */
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

        /* Layout thông tin người dùng */
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

        /* Layout thông tin chi tiết */
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

        /* Bảng danh sách sản phẩm */
        .items-table {
            margin-top: 20px;
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

        /* Cố định width cho các cột */
        .items-table table th:nth-child(1), .items-table table td:nth-child(1) { width: 2cm; }
        .items-table table th:nth-child(2), .items-table table td:nth-child(2) { width: 11cm; }
        .items-table table th:nth-child(3), .items-table table td:nth-child(3) { width: 2.2cm; }
        .items-table table th:nth-child(4), .items-table table td:nth-child(4) { width: 3cm; }
        .items-table table th:nth-child(5), .items-table table td:nth-child(5) { width: 3cm; }
        .items-table table th:nth-child(6), .items-table table td:nth-child(6) { width: 8cm; }

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

        .items-table input[readonly] {
            background-color: transparent !important;
        }

        /* Căn giữa cho các cột số */
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

        /* Buttons */
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

        /* Container căn giữa 2 nút */
        .button-container {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 15px;
            margin-top: 20px;
            flex-wrap: wrap;
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
        <h1>ĐƠN YÊU CẦU MUA HÀNG</h1>
        
        <!-- Form chính gửi yêu cầu mua hàng -->
        <form id="myForm" action="loadingrequest" method="post">
            <h3>Thông tin</h3>
            
            <!-- Phần thông tin người dùng (readonly) -->
            <div class="information-user">
                <div class="form-group">
                    <label>Người dùng <span style="color: red">*</span></label>
                    <input type="text" value="${sessionScope.currentUser}" readonly> 
                </div>
                <div class="form-group">
                    <label>Tuổi <span style="color: red">*</span></label>
                    <input type="text" value="${age}" readonly>
                </div>
                <div class="form-group">
                    <label>Ngày tháng năm sinh <span style="color: red">*</span></label>
                    <input type="text" value="${sessionScope.DoB}" readonly>
                </div>
                <div class="form-group">
                    <label>Vai trò <span style="color: red">*</span></label>
                    <select name="role" required>
                        <option value="" disabled selected>-- Chọn vai trò --</option>
                        <option value="Nhân viên">Nhân viên</option>
                        <option value="Giám đốc">Giám đốc</option>
                        <option value="Admin">Admin</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Ngày yêu cầu <span style="color: red">*</span></label>
                    <input type="date" name="day_request" required>
                </div>
            </div>
            
            <h3>Chi tiết</h3>
            
            <!-- Phần thông tin chi tiết yêu cầu -->
            <div class="information-items">
                <div class="row">
                    <div class="form-group">
                        <label>ID <span style="color: red">*</span></label>
                        <textarea rows="1" name="request_id" style="resize: none; overflow: hidden; background-color: #f8f9fa; cursor: not-allowed;" oninput="autoResize(this)" readonly>${requestScope.nextID}</textarea>
                    </div>
                    <div class="form-group">
                        <label>Mục đích <span style="color: red">*</span></label>
                        <textarea rows="1" name="reason" style="resize: none; overflow: hidden;" oninput="autoResize(this)" required></textarea>
                    </div>
                    <div class="form-group">
                        <label>Gợi ý nhà cung cấp</label>
                        <textarea rows="1" name="supplier" style="resize: none; overflow: hidden;" oninput="autoResize(this)"></textarea>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group">
                        <label>Địa chỉ</label>
                        <textarea rows="1" name="address" style="resize: none; overflow: hidden;" oninput="autoResize(this)"></textarea>
                    </div>
                    <div class="form-group">
                        <label>Điện thoại</label>
                        <input name="phone" pattern="0[0-9]{9}" title="Số điện thoại phải bắt đầu bằng 0 và có đúng 10 chữ số">
                    </div>
                    <div class="form-group">
                            <label>Email</label>
                            <input type="email" 
                                   name="email" 
                                   pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
                                   title="Email phải đúng định dạng">
                        </div>
                </div>
            </div>
            
            <!-- Bảng danh sách sản phẩm -->
            <div class="items-table">
                <label>Vui lòng nhập chi tiết mặt hàng <span style="color: red">*</span></label>
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
                            <!-- STT tự động -->
                            <td><textarea name="stt" rows="1" style="box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)">1</textarea></td>
                            
                            <!-- Dropdown chọn sản phẩm từ database -->
                            <td>
                                <select name="product_name" onchange="updateProductInfo(this)" style="width: 100%;">
                                    <option value="" disabled selected>-- Chọn sản phẩm --</option>
                                    <c:forEach var="p" items="${products_list}">
                                        <option value="${p.name}" data-code="${p.code}">
                                            ${p.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                            
                            <!-- Code sản phẩm (tự động fill khi chọn sản phẩm) -->
                            <td><input type="text" name="product_code" readonly style="width: 100%;" /></td>

                            <!-- Dropdown chọn đơn vị từ database -->
                            <td>
                                <select name="unit" style="width: 100%;">
                                    <option value="" disabled selected>-- Chọn --</option>
                                    <c:forEach var="u" items="${unit_list}">
                                        <option value="${u.name}" data-symbol="${u.symbol}">
                                            ${u.symbol}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>

                            <!-- Số lượng (có validation) -->
                            <td><textarea name="quantity" rows="1" 
                                          class="quantity-input"
                                          style="box-sizing: border-box; overflow: hidden; resize: none;" 
                                          oninput="validateQuantity(this); autoResize(this)"
                                          onblur="formatQuantity(this)"
                                          onkeypress="return isNumberKey(event)"
                                          placeholder="Nhập số lượng"></textarea></td>
                            
                            <!-- Ghi chú -->
                            <td><textarea name="note" rows="1" style="box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)"></textarea></td>
                        </tr>
                    </tbody>
                </table>
                
                <!-- Lý do chi tiết -->
                <div class="form-group">
                    <label>Lý do chi tiết <span style="color: red">*</span></label>
                    <textarea rows="3" name="reason_detail" style="border: 1px solid #ddd; resize: none;" oninput="autoResize(this)" required></textarea>
                </div>
            </div>
            
            <!-- Buttons submit và quay lại -->
            <div class="button-container">
                <button type="submit" class="submit-btn">Gửi yêu cầu</button>
                <a href="Admin.jsp" class="back-btn">Quay lại</a>
            </div>
        </form>
    </div>
    </div>
    
    <script>
        // Tự động resize textarea theo nội dung
        function autoResize(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = textarea.scrollHeight + 'px';
        }

        // Validation input số lượng - chỉ cho phép số, dấu chấm và dấu phẩy
        function isNumberKey(evt) {
            var charCode = (evt.which) ? evt.which : evt.keyCode;
            var inputValue = evt.target.value;

            // Cho phép: backspace, delete, tab, escape, enter, arrow keys
            if ([8, 9, 27, 13, 37, 38, 39, 40, 46].indexOf(charCode) !== -1) {
                return true;
            }

            // Cho phép dấu chấm (.) - chỉ một dấu chấm
            if (charCode == 46) {
                if (inputValue.indexOf('.') !== -1) {
                    return false;
                }
                return true;
            }

            // Cho phép dấu phẩy (,) - chỉ một dấu phẩy
            if (charCode == 44) {
                if (inputValue.indexOf(',') !== -1 || inputValue.indexOf('.') !== -1) {
                    return false;
                }
                return true;
            }

            // Chỉ cho phép số (0-9)
            if (charCode < 48 || charCode > 57) {
                return false;
            }

            return true;
        }

        // Validate và chuẩn hóa số lượng
        function validateQuantity(textarea) {
            let value = textarea.value;

            // Chuẩn hóa dấu phẩy thành dấu chấm
            if (value.includes(',')) {
                value = value.replace(',', '.');
                textarea.value = value;
            }

            // Kiểm tra định dạng số hợp lệ
            const parts = value.split('.');
            if (parts.length > 2) {
                // Nếu có nhiều hơn 1 dấu chấm, chỉ giữ lại 2 phần đầu
                textarea.value = parts[0] + '.' + parts[1];
            } else if (parts.length === 2 && parts[1].length > 2) {
                // Giới hạn tối đa 2 chữ số thập phân
                textarea.value = parts[0] + '.' + parts[1].substring(0, 2);
            }
        }

        // Format số lượng theo định dạng Việt Nam khi blur
            function formatQuantity(textarea) {
                let value = textarea.value.trim();
                if (value === '')
                    return;
                // Chuyển dấu phẩy thành dấu chấm để parse
                value = value.replace(',', '.');
                const numValue = parseFloat(value);
                // Kiểm tra giới hạn số an toàn
                if (!isNaN(numValue) && numValue >= 0 && numValue <= Number.MAX_SAFE_INTEGER) {
                    textarea.value = numValue.toLocaleString('vi-VN', {
                        minimumFractionDigits: 0,
                        maximumFractionDigits: 2
                    });
                } else if (numValue > Number.MAX_SAFE_INTEGER) {
                    // Hiển thị cảnh báo và giữ nguyên giá trị
                    alert('Số lượng quá lớn! Vui lòng nhập số nhỏ hơn.');
                    textarea.value ='';
                }
            }

        // Thêm hàng mới vào bảng sản phẩm
        function addRow() {
            const tbody = document.getElementById('itemsTableBody');
            const currentRows = tbody.querySelectorAll('tr').length;
            const nextSTT = currentRows + 1;

            // Copy từ hàng đầu tiên
            const firstRow = tbody.querySelector('tr');
            const newRow = firstRow.cloneNode(true);

            // Cập nhật STT cho hàng mới
            const sttTextarea = newRow.querySelector('textarea[name="stt"]');
            sttTextarea.value = nextSTT;

            // Reset các giá trị khác về mặc định
            const productSelect = newRow.querySelector('select[name="product_name"]');
            productSelect.selectedIndex = 0;

            const codeInput = newRow.querySelector('input[name="product_code"]');
            codeInput.value = '';

            const unitSelect = newRow.querySelector('select[name="unit"]');
            unitSelect.selectedIndex = 0;

            const quantityTextarea = newRow.querySelector('textarea[name="quantity"]');
            quantityTextarea.value = '';

            const noteTextarea = newRow.querySelector('textarea[name="note"]');
            noteTextarea.value = '';

            tbody.appendChild(newRow);
        }

        // Tự động fill code sản phẩm khi chọn tên sản phẩm
        function updateProductInfo(selectElement) {
            const selectedOption = selectElement.options[selectElement.selectedIndex];
            const code = selectedOption.getAttribute("data-code");

            const row = selectElement.closest("tr");
            row.querySelector("input[name='product_code']").value = code || '';
        }
    </script>
</body>
</html>