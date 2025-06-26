<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đơn Báo Giá</title>
        <style>

            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

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
                color: #10b981;
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

            input[readonly], textarea[readonly] {
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
            /* Định nghĩa chiều rộng cột cho bảng items */
            .items-table table th:nth-child(1),
            .items-table table td:nth-child(1) {
                width: 2cm;
            }

            .items-table table th:nth-child(2),
            .items-table table td:nth-child(2) {
                width: 10cm;
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
                width: 4cm;
            }
            .items-table table th:nth-child(7),
            .items-table table td:nth-child(7) {
                width: 4cm;
            }
            .items-table table th:nth-child(8),
            .items-table table td:nth-child(8) {
                width: 7cm;
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
                background-color: #10b981;
                color: #fff;
            }

            .items-table textarea, .items-table input {
                border: none;
                resize: none;
                width: 100%;
                min-height: 30px;
                font-size: 14px;
            }

            .items-table textarea:focus, .items-table input:focus {
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

            .submit-btn {
                width: 150px;
                padding: 12px;
                background: #10b981;
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
                background: #059669;
            }

            .back-btn:hover {
                background: #5a6268;
            }

            .button-container {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 15px;
                margin-top: 20px;
                flex-wrap: wrap;
            }

            .readonly-info {
                background-color: #e9ecef;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
            }

            .readonly-info h4 {
                color: #495057;
                margin-bottom: 10px;
            }

            .readonly-info p {
                margin: 5px 0;
                color: #6c757d;
            }
            /* Style cho input readonly trong bảng items */
            .itemsTableBody input[readonly]{
                background-color: transparent !important;

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
            <h1>ĐƠN BÁO GIÁ SẢN PHẨM</h1>
            <!-- Form gửi báo giá đến servlet submitpurchaseorder -->
            <form id="myForm" action="submitpurchaseorder" method="post">
                <!-- Hidden field chứa ID của request gốc -->
                <input type="hidden" name="originalRequestId" value="${requestId}">

                <h3>Thông tin người tạo báo giá</h3>
                <div class="information-user">
                    <div class="form-group">
                        <label>Người tạo đơn <span style="color: red">*</span></label>
                        <!-- Hiển thị tên user từ session -->
                        <input type="text" value="${sessionScope.currentUser}" readonly> 
                    </div>
                    <div class="form-group">
                        <label>Tuổi <span style="color: red">*</span></label>
                        <!-- Hiển thị tuổi được tính từ server -->
                        <input type="text" value="${age}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Ngày tháng năm sinh <span style="color: red">*</span></label>
                        <!-- Hiển thị ngày sinh từ session -->
                        <input type="text" value="${sessionScope.DoB}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Ngày tạo báo giá <span style="color: red">*</span></label>
                        <!-- Input date bắt buộc nhập -->
                        <input type="date" name="quote_date" required>
                    </div>
                </div>

                <h3>Chi tiết báo giá</h3>
                <div class="information-items">
                    <div class="row">
                        <div class="form-group">
                            <label>ID Báo giá <span style="color: red">*</span></label>
                            <!-- ID báo giá tự động từ requestId -->
                            <textarea rows="1" name="quote_id" style="width: 100%; resize: none;overflow: hidden;background-color: #f8f9fa; cursor: not-allowed;" readonly>${requestId}</textarea>
                        </div>
                        <div class="form-group">
                            <label>Mục đích mua hàng <span style="color: red">*</span></label>
                            <!-- Lý do mua hàng từ request gốc -->
                            <textarea rows="1" name="quote_note" style="width: 100%; resize: none;overflow: hidden;background-color: #f8f9fa; cursor: not-allowed;" readonly>${reason}</textarea>
                        </div>
                        <div class="form-group">
                            <label>Nhà cung cấp <span style="color: red">*</span></label>
                            <select name="supplier_name" style="width: 100%;" onchange="handleSupplierChange(this)">
                                <option value="" disabled selected>-- Chọn nhà cung cấp --</option>
                                <c:forEach var="sn" items="${supplier_list}">
                                    <option value="${sn.name}">
                                        ${sn.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                    </div>
                    <div class="row">
                        <div class="form-group">
                            <label>Địa chỉ <span style="color: red">*</span></label>
                            <textarea rows="1" name="supplier_address" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" readonly></textarea>
                        </div>
                        <div class="form-group">
                            <label>Điện thoại <span style="color: red">*</span></label>
                            <textarea rows="1" name="supplier_phone" id="phone" 
                                      style="width: 100%; resize: none; overflow: hidden;" 
                                      oninput="autoResize(this)"readonly></textarea>

                        </div>
                        <div class="form-group">
                            <label>Email <span style="color: red">*</span></label>
                            <textarea rows="1" name="supplier_email" style="width: 100%; resize: none;overflow: hidden;" oninput="autoResize(this)" readonly></textarea>
                        </div>
                    </div>
                </div>

                <div class="items-table">
                    <label>Chi tiết mặt hàng và báo giá <span style="color: red">*</span></label>
                    <table>
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Tên mặt hàng</th>
                                <th>Code</th>
                                <th>Đơn vị</th>
                                <th>Số lượng</th>
                                <th>Giá trên 1 đơn vị</th>
                                <th>Thành tiền</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody id="itemsTableBody">
                            <!-- JSTL c:choose để kiểm tra có items hay không -->
                            <c:choose>
                                <c:when test="${not empty items}">
                                    <!-- Lặp qua danh sách items từ request gốc -->
                                    <c:forEach var="item" items="${items}" varStatus="loop">
                                        <tr>
                                            <td>
                                                <!-- STT tự động từ loop.count -->
                                                <textarea name="stt" rows="1" readonly 
                                                          style="background-color: #f8f9fa; text-align: center; resize: none; overflow: hidden; border: none;">${loop.count}</textarea>
                                            </td>
                                            <td>
                                                <!-- Tên sản phẩm từ item, readonly -->
                                                <textarea name="product_name" rows="1" readonly 
                                                          style="background-color: #f8f9fa; resize: none; overflow: hidden; border: none;" 
                                                          oninput="autoResize(this)">${item.productName}</textarea>
                                            </td>
                                            <td>
                                                <!-- Mã sản phẩm từ item, readonly -->
                                                <textarea name="product_code" rows="1" readonly 
                                                          style="background-color: #f8f9fa; text-align: center; resize: none; overflow: hidden; border: none;">${item.productCode}</textarea>
                                            </td>
                                            <td>
                                                <!-- Đơn vị từ item, readonly -->
                                                <textarea name="unit" rows="1" readonly 
                                                          style="background-color: #f8f9fa; text-align: center; resize: none; overflow: hidden; border: none;">${item.unit}</textarea>
                                            </td>
                                            <td>
                                                <!-- Số lượng từ item, readonly -->
                                                <textarea name="quantity" rows="1" readonly 
                                                          style="background-color: #f8f9fa; text-align: center; resize: none; overflow: hidden; border: none;">${item.quantity}</textarea>
                                            </td>
                                            <td>
                                                <!-- Input giá - có thể chỉnh sửa, required -->
                                                <textarea name="pricePerUnit" rows="1" 
                                                          class="price-input"
                                                          placeholder="Nhập giá" 
                                                          required 
                                                          oninput="validatePrice(this); calculateTotal(this); autoResize(this)"
                                                          onblur="formatPrice(this)"
                                                          onkeypress="return isNumberKey(event)"></textarea>
                                            </td>

                                            <td>
                                                <!-- Thành tiền tự động tính, readonly -->
                                                <textarea name="totalPrice" rows="1" readonly 
                                                          style="background-color: #f8f9fa; text-align: center; resize: none; overflow: hidden; border: none;"></textarea>
                                            </td>
                                            <td>
                                                <!-- Ghi chú có thể chỉnh sửa -->
                                                <textarea name="quote_item_note" rows="1" 
                                                          style="resize: none; overflow: hidden; border: none;" 
                                                          oninput="autoResize(this)" 
                                                          placeholder="Ghi chú báo giá">${item.note}</textarea>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </tbody>

                    </table>

                    <div class="form-group">
                        <label>Tổng kết báo giá <span style="color: red">*</span></label>
                        <!-- Textarea tổng kết bắt buộc -->
                        <textarea rows="3" name="quote_summary" style="width: 100%; border: 1px solid #ddd; resize: none;" oninput="autoResize(this)" required placeholder="Nhập tổng kết về báo giá này..."></textarea>
                    </div>
                </div>
                <div class="button-container">
                    <button type="submit" class="submit-btn">Gửi báo giá</button>
                    <a href="listpurchaseorder" class="back-btn">Quay lại</a>
                </div>
            </form>
        </div>
                        </div>
                        
        <script>
            function autoResize(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = Math.min(textarea.scrollHeight, 100) + 'px';
            }

            function calculateTotal(input) {
            const row = input.closest('tr');
            const quantityElement = row.querySelector('input[name="quantity"], textarea[name="quantity"]');
            const priceElement = row.querySelector('input[name="pricePerUnit"], textarea[name="pricePerUnit"]');
            const totalPriceElement = row.querySelector('input[name="totalPrice"], textarea[name="totalPrice"]');
            if (quantityElement && priceElement && totalPriceElement) {
            const quantity = parseFloat(quantityElement.value.replace(',', '.')) || 0;
            const pricePerUnit = parseFloat(priceElement.value.replace(',', '.')) || 0;
            const totalPrice = quantity * pricePerUnit;
            totalPriceElement.value = totalPrice.toLocaleString('vi-VN', {
            minimumFractionDigits: 0,
                    maximumFractionDigits: 2
            });
            }
            }

            function isNumberKey(evt) {
            var charCode = (evt.which) ? evt.which : evt.keyCode;
            var inputValue = evt.target.value;
            if ([8, 9, 27, 13, 37, 38, 39, 40, 46].indexOf(charCode) !== - 1) {
            return true;
            }

            if (charCode == 46) {
            if (inputValue.indexOf('.') !== - 1) {
            return false;
            }
            return true;
            }

            if (charCode == 44) {
            if (inputValue.indexOf(',') !== - 1 || inputValue.indexOf('.') !== - 1) {
            return false;
            }
            return true;
            }

            if (charCode < 48 || charCode > 57) {
            return false;
            }

            return true;
            }

            function validatePrice(textarea) {
            let value = textarea.value;
            if (value.includes(',')) {
            value = value.replace(',', '.');
            textarea.value = value;
            }

            const parts = value.split('.');
            if (parts.length > 2) {
            textarea.value = parts[0] + '.' + parts[1];
            } else if (parts.length === 2 && parts[1].length > 2) {
            textarea.value = parts[0] + '.' + parts[1].substring(0, 2);
            }
            }

            // Format giá theo định dạng Việt Nam khi blur
            function formatPrice(textarea) {
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
            // Hiển thị cảnh báo và xóa giá trị
            alert('Giá trị quá lớn! Vui lòng nhập số nhỏ hơn.');
            textarea.value = '';
            }
            }

            function handlePriceFocus(textarea) {
            let value = textarea.value;
            if (value) {
            value = value.replace(/\./g, '').replace(',', '.');
            const numValue = parseFloat(value);
            if (!isNaN(numValue)) {
            textarea.value = numValue.toString();
            }
            }
            }

            function validateAndCalculate(textarea) {
            validatePrice(textarea);
            calculateTotal(textarea);
            autoResize(textarea);
            }
            // Dữ liệu supplier từ server (sẽ được render từ JSTL)
            const supplierData = {
            <c:forEach var="supplier" items="${supplier_list}" varStatus="status">
            "${supplier.name}": {
            address: "${supplier.address}",
                    phone: "${supplier.phone}",
                    email: "${supplier.email}"
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            };
// Hàm xử lý khi thay đổi supplier
            function handleSupplierChange(selectElement) {
            const selectedSupplier = selectElement.value;
            // Lấy các element cần điền thông tin
            const addressField = document.querySelector('textarea[name="supplier_address"]');
            const phoneField = document.querySelector('textarea[name="supplier_phone"]');
            const emailField = document.querySelector('textarea[name="supplier_email"]');
            if (selectedSupplier && supplierData[selectedSupplier]) {
            // Điền thông tin tự động
            const supplier = supplierData[selectedSupplier];
            addressField.value = supplier.address || '';
            phoneField.value = supplier.phone || '';
            emailField.value = supplier.email || '';
            // Auto resize các textarea
            autoResize(addressField);
            autoResize(phoneField);
            autoResize(emailField);
            } else {
            // Xóa thông tin nếu không chọn supplier
            addressField.value = '';
            phoneField.value = '';
            emailField.value = '';
            autoResize(addressField);
            autoResize(phoneField);
            autoResize(emailField);
            }
            }

// Gắn event listener khi trang load
            document.addEventListener('DOMContentLoaded', function() {
            const supplierSelect = document.querySelector('select[name="supplier_name"]');
            if (supplierSelect) {
            supplierSelect.addEventListener('change', function() {
            handleSupplierChange(this);
            });
            }
            });
        </script>

    </body>
</html>