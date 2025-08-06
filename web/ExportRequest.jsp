<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đơn Yêu Cầu Xuất Kho</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            /* Reset CSS */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            body {
                background-color: #f8f9fa;
                min-height: 100vh;
                color: #333;
                padding: 20px;
            }

            .container {
                background: white;
                padding: 40px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                max-width: 1200px;
                margin: 0 auto;
            }

            .header-section {
                text-align: center;
                margin-bottom: 30px;
                padding-bottom: 20px;
                border-bottom: 2px solid #e9ecef;
            }

            h1 {
                color: #2c3e50;
                font-size: 28px;
                font-weight: 600;
                margin-bottom: 8px;
            }

            .subtitle {
                color: #6c757d;
                font-size: 14px;
            }

            .order-info-section {
                display: flex;
                justify-content: center;
                margin-bottom: 25px;
            }

            .order-info-card {
                background: #fbfbfb;
                padding: 15px 20px;
                border-radius: 6px;
                border: 1px solid #e9ecef;
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
                max-width: 450px;
                width: 100%;
            }

            .order-info-card .form-group {
                margin-bottom: 0;
            }

            .order-info-card label {
                font-size: 13px;
                color: #6c757d;
                font-weight: 500;
                margin-bottom: 4px;
            }

            .order-info-card input, .order-info-card textarea {
                padding: 8px 10px;
                font-size: 13px;
                border: 1px solid #dee2e6;
                border-radius: 4px;
            }

            .order-info-card input[readonly], .order-info-card textarea[readonly] {
                background-color: #f8f9fa;
                color: #495057;
                font-weight: 500;
                cursor: not-allowed;
            }

            .section-header {
                display: flex;
                align-items: center;
                gap: 10px;
                margin: 30px 0 20px 0;
                padding-bottom: 8px;
                border-bottom: 1px solid #dee2e6;
            }

            .section-header i {
                color: #4a90e2;
                font-size: 18px;
            }

            .section-header h3 {
                color: #2c3e50;
                font-size: 18px;
                font-weight: 600;
            }

            .form-group {
                margin-bottom: 20px;
            }

            label {
                display: block;
                font-size: 14px;
                color: #495057;
                margin-bottom: 6px;
                font-weight: 500;
            }

            label i {
                color: #4a90e2;
                margin-right: 5px;
                width: 16px;
            }

            input, select, textarea {
                width: 100%;
                padding: 12px;
                border: 1px solid #ced4da;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.2s ease;
                background: white;
            }

            input:focus, select:focus, textarea:focus {
                outline: none;
                border-color: #4a90e2;
                box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.1);
            }

            input[readonly], textarea[readonly] {
                background-color: #f8f9fa;
                color: #6c757d;
                cursor: not-allowed;
            }

            .user-info {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
                gap: 20px;
                margin-bottom: 25px;
            }

            .items-section {
                background: #fafafa;
                padding: 20px;
                border-radius: 8px;
                border: 1px solid #e9ecef;
                margin: 20px 0;
                overflow: visible;
            }

            .items-table table {
                width: 100%;
                border-collapse: collapse;
                margin: 15px 0;
                background: white;
                border-radius: 6px;
                overflow: visible;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }

            .items-table th {
                background: #4a90e2;
                color: white;
                padding: 12px 8px;
                text-align: center;
                font-weight: 600;
                font-size: 13px;
            }

            .items-table td {
                padding: 10px 8px;
                text-align: center;
                border-bottom: 1px solid #e9ecef;
                background: white;
                position: relative;
                overflow: visible;
            }

            .items-table tr:hover td {
                background: #f8f9ff;
            }

            .items-table table th:nth-child(1), .items-table table td:nth-child(1) {
                width: 60px;
            }
            .items-table table th:nth-child(2), .items-table table td:nth-child(2) {
                width: 300px;
            }
            .items-table table th:nth-child(3), .items-table table td:nth-child(3) {
                width: 100px;
            }
            .items-table table th:nth-child(4), .items-table table td:nth-child(4) {
                width: 100px;
            }
            .items-table table th:nth-child(5), .items-table table td:nth-child(5) {
                width: 150px;
            }
            .items-table table th:nth-child(6), .items-table table td:nth-child(6) {
                width: auto;
            }
            .items-table table th:nth-child(7), .items-table table td:nth-child(7) {
                width: 100px;
            }

            .items-table textarea, .items-table input, .items-table select {
                border: 1px solid #dee2e6;
                border-radius: 4px;
                padding: 6px 8px;
                font-size: 13px;
            }

            .items-table textarea:focus, .items-table input:focus, .items-table select:focus {
                border-color: #4a90e2;
                box-shadow: 0 0 0 1px rgba(74, 144, 226, 0.1);
            }

            /* Fixed height for quantity and note inputs */
            .items-table textarea[name="quantity"] {
                height: 35px;
                resize: none;
                overflow: hidden;
            }

            .items-table textarea[name="note"] {
                height: 35px;
                resize: none;
                overflow: hidden;
            }

            /* Custom select styling */
            .product-select {
                width: 100%;
                padding: 6px 8px;
                border: 1px solid #dee2e6;
                border-radius: 4px;
                font-size: 13px;
                background: white;
                cursor: pointer;
            }

            .product-select:focus {
                border-color: #4a90e2;
                box-shadow: 0 0 0 1px rgba(74, 144, 226, 0.1);
                outline: none;
            }

            .product-select option {
                padding: 8px;
                font-size: 13px;
            }

            .product-select option:first-child {
                color: #6c757d;
                font-style: italic;
            }

            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-size: 14px;
                font-weight: 500;
                cursor: pointer;
                transition: all 0.2s ease;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
            }

            .btn-primary {
                background: #4a90e2;
                color: white;
            }

            .btn-primary:hover {
                background: #357abd;
                transform: translateY(-1px);
            }

            .btn-success {
                background: #28a745;
                color: white;
            }

            .btn-success:hover {
                background: #218838;
                transform: translateY(-1px);
            }

            .btn-danger {
                background: #dc3545;
                color: white;
            }

            .btn-danger:hover {
                background: #c82333;
                transform: translateY(-1px);
            }

            .btn-info {
                background: #17a2b8;
                color: white;
            }

            .btn-info:hover {
                background: #138496;
                transform: translateY(-1px);
            }

            .btn-secondary {
                background: #6c757d;
                color: white;
            }

            .btn-secondary:hover {
                background: #5a6268;
                transform: translateY(-1px);
            }

            .btn-sm {
                padding: 8px 16px;
                font-size: 13px;
                border-radius: 5px;
                gap: 6px;
            }

            .table-buttons {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 8px;
                margin: 12px 0;
            }

            .table-buttons .btn {
                min-width: 120px;
            }

            .supplement-button {
                display: flex;
                justify-content: center;
                margin: 20px 0 15px 0;
                padding-top: 15px;
                border-top: 1px solid #e9ecef;
            }

            .supplement-button .btn {
                background: #17a2b8;
                color: white;
                padding: 10px 20px;
                font-size: 14px;
                border-radius: 6px;
                min-width: 200px;
                justify-content: center;
            }

            .supplement-button .btn:hover {
                background: #138496;
                transform: translateY(-1px);
            }

            .button-container {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 15px;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid #dee2e6;
            }

            .required {
                color: #dc3545;
                margin-left: 2px;
            }

            .alert {
                padding: 12px 16px;
                border-radius: 6px;
                margin-bottom: 20px;
                border: 1px solid transparent;
            }

            .alert-info {
                background-color: #d1ecf1;
                border-color: #bee5eb;
                color: #0c5460;
            }

            .alert-success {
                color: #155724;
                background-color: #d4edda;
                border-color: #c3e6cb;
                animation: slideDown 0.5s ease-out;
            }

            .alert-success.auto-hide {
                animation: slideDown 0.5s ease-out, fadeOut 0.5s ease-out 4.5s forwards;
            }

            .alert-danger {
                color: #721c24;
                background-color: #f8d7da;
                border-color: #f5c6cb;
            }

            @keyframes slideDown {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            @keyframes fadeOut {
                from {
                    opacity: 1;
                    transform: translateY(0);
                }
                to {
                    opacity: 0;
                    transform: translateY(-20px);
                }
            }

            @media (max-width: 768px) {
                body {
                    padding: 10px;
                }

                .container {
                    padding: 20px;
                }

                .order-info-card {
                    grid-template-columns: 1fr;
                    gap: 15px;
                    max-width: none;
                }

                .user-info {
                    grid-template-columns: 1fr;
                }

                h1 {
                    font-size: 24px;
                }

                .button-container {
                    flex-direction: column;
                }

                .button-container .btn {
                    width: 100%;
                    justify-content: center;
                }

                .table-buttons {
                    gap: 6px;
                }

                .table-buttons .btn {
                    min-width: 110px;
                    font-size: 12px;
                    padding: 7px 12px;
                }

                .supplement-button .btn {
                    min-width: 180px;
                    font-size: 13px;
                    padding: 9px 18px;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <!-- Header -->
            <div class="header-section">
                <h1>Đơn Yêu Cầu Xuất Kho</h1>
                <p class="subtitle">Vui lòng điền đầy đủ thông tin để tạo yêu cầu xuất kho</p>
            </div>

            <!-- Alerts -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success auto-hide" id="successAlert">
                    <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <!-- Form -->
            <form id="exportForm" action="exportRequest" method="post">
                <!-- Order Info -->
                <div class="order-info-section">
                    <div class="order-info-card">
                        <div class="form-group">
                            <label>Mã đơn yêu cầu <span class="required">*</span></label>
                            <textarea name="export_request_id" rows="1" style="resize: none; text-align: center;" readonly>${nextExportID}</textarea>
                        </div>
                        <div class="form-group">
                            <label>Ngày yêu cầu <span class="required">*</span></label>
                            <input type="date" name="day_request" readonly>
                        </div>
                    </div>
                </div>

                <!-- User Info -->
                <div class="section-header">
                    <i class="fas fa-user"></i>
                    <h3>Thông Tin Người Yêu Cầu</h3>
                </div>
                <div class="user-info">
                    <div class="form-group">
                        <label>Người Yêu Cầu <span class="required">*</span></label>
                        <input type="text" value="${currentUser}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Vai trò <span class="required">*</span></label>
                        <c:choose>
                            <c:when test="${roleID == 3}">
                                <input type="text" name="role" value="Nhân viên" readonly>
                            </c:when>
                            <c:when test="${roleID == 4}">
                                <input type="text" name="role" value="Giám đốc" readonly>
                            </c:when>
                            <c:when test="${roleID == 2}">
                                <input type="text" name="role" value="Kho" readonly>
                            </c:when>
                            <c:when test="${roleID == 1}">
                                <input type="text" name="role" value="Admin" readonly>
                            </c:when>
                            <c:otherwise>
                                <input type="text" name="role" value="Chưa xác định" readonly>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Items Section -->
                <div class="section-header">
                    <i class="fas fa-boxes"></i>
                    <h3>Danh Sách Sản Phẩm</h3>
                </div>
                <div class="items-section">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle"></i>
                        Vui lòng chọn sản phẩm và điền đầy đủ thông tin cho từng mặt hàng xuất kho
                    </div>

                    <div class="table-buttons">
                        <button type="button" class="btn btn-success btn-sm" onclick="addRow()">
                            <i class="fas fa-plus"></i> Thêm
                        </button>
                    </div>

                    <div class="items-table">
                        <table>
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Tên mặt hàng <span class="required">*</span></th>
                                    <th>Mã SP</th>
                                    <th>Đơn vị</th>
                                    <th>Số lượng <span class="required">*</span></th>
                                    <th>Ghi chú</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="itemsTableBody">
                                <tr>
                                    <td><textarea name="stt" rows="1" style="resize: none; text-align: center;" oninput="autoResize(this)">1</textarea></td>
                                    <td>
                                        <select name="product_name" class="product-select" onchange="handleProductChange(this)" required>
                                            <option value="">-- Chọn sản phẩm --</option>
                                            <c:forEach var="product" items="${productsList}">
                                                <option value="${product.name}" 
                                                        data-id="${product.id}" 
                                                        data-code="${product.code}" 
                                                        data-unit="${product.unit_id}">
                                                    ${product.name} (${product.code})
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <input type="hidden" name="product_id" />
                                    </td>
                                    <td><input type="text" name="product_code" readonly style="width: 100%; text-align: center;" /></td>
                                    <td>
                                        <input type="text" name="unit" readonly style="width: 100%; text-align: center;" />
                                        <input type="hidden" name="unit_value" />
                                    </td>
                                    <td>
                                        <textarea name="quantity" rows="1" 
                                                  class="quantity-input"
                                                  style="resize: none; text-align: center; min-width: 120px; height: 35px;" 
                                                  oninput="validateQuantity(this)"
                                                  onblur="formatQuantity(this)"
                                                  onkeypress="return isNumberKey(event)"
                                                  placeholder="0" required></textarea>
                                    </td>
                                    <td><textarea name="note" rows="1" style="resize: none; height: 35px;" placeholder="Ghi chú..."></textarea></td>
                                    <td><button type="button" class="btn btn-danger btn-sm delete-row-btn" onclick="deleteRow(this)" style="display: none;">Xóa</button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="button-container">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-paper-plane"></i> Gửi Yêu Cầu
                    </button>
                    <a href="${pageContext.request.contextPath}/RequestForward.jsp" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Quay Lại
                    </a>
                </div>
            </form>
        </div>

        <!-- Hidden Data -->
        <script type="text/javascript">
            const productsData = [
            <c:forEach var="product" items="${productsList}" varStatus="status">
            {
            id: "${product.id}",
                    name: "${product.name}",
                    code: "${product.code}",
                    unitSymbol: "${product.unit_id}"
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];
            const unitsData = [
            <c:forEach var="unit" items="${unitsList}" varStatus="status">
            {
            id: "${unit.id}",
                    name: "${unit.name}",
                    symbol: "${unit.symbol}"
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];
        </script>

        <script>
            // Set current date and initialize
            document.addEventListener('DOMContentLoaded', function () {
            const dateInput = document.querySelector('input[name="day_request"]');
            const today = new Date().toISOString().split('T')[0];
            dateInput.value = today;
            const successAlert = document.getElementById('successAlert');
            if (successAlert) {
            setTimeout(function() {
            successAlert.style.display = 'none';
            }, 5000);
            }

            updateDeleteButtons();
            document.querySelectorAll('#itemsTableBody tr').forEach(row => setupRowEventHandlers(row));
            });
            // Auto-resize textarea (only for STT field)
            function autoResize(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = textarea.scrollHeight + 'px';
            }

            // Validate number input
            function isNumberKey(evt) {
            var charCode = evt.which ? evt.which : evt.keyCode;
            var inputValue = evt.target.value;
            if ([8, 9, 27, 13, 37, 38, 39, 40, 46].indexOf(charCode) !== - 1) return true;
            if (charCode === 46) {
            if (inputValue.indexOf('.') !== - 1) return false;
            return true;
            }
            if (charCode === 44) {
            if (inputValue.indexOf(',') !== - 1 || inputValue.indexOf('.') !== - 1) return false;
            return true;
            }
            if (charCode < 48 || charCode > 57) return false;
            return true;
            }

            // Validate quantity
            function validateQuantity(textarea) {
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

            // Format quantity
            function formatQuantity(textarea) {
            let value = textarea.value.trim();
            if (value === '') return;
            value = value.replace(',', '.');
            const numValue = parseFloat(value);
            if (!isNaN(numValue) && numValue >= 0 && numValue <= Number.MAX_SAFE_INTEGER) {
            textarea.value = numValue.toLocaleString('vi-VN', {
            minimumFractionDigits: 0,
                    maximumFractionDigits: 2
            });
            } else if (numValue > Number.MAX_SAFE_INTEGER) {
            alert('Số lượng quá lớn! Vui lòng nhập số nhỏ hơn.');
            textarea.value = '';
            }
            }

            // Handle product change from select dropdown
            function handleProductChange(select) {
            const row = select.closest('tr');
            const selectedOption = select.options[select.selectedIndex];
            if (selectedOption.value === '') {
            // Clear all fields if no product selected
            clearProductInfo(select);
            return;
            }

            // Get product data from selected option
            const product = {
            id: selectedOption.getAttribute('data-id'),
                    name: selectedOption.value,
                    code: selectedOption.getAttribute('data-code'),
                    unitSymbol: selectedOption.getAttribute('data-unit')
            };
            // Update hidden product ID
            row.querySelector('input[name="product_id"]').value = product.id;
            // Update product code
            row.querySelector('input[name="product_code"]').value = product.code;
            // Update unit information
            updateUnitInfo(row, product.unitSymbol);
            }

            // Update unit information
            function updateUnitInfo(row, unitId) {
            const unitInput = row.querySelector('input[name="unit"]');
            const unitValueInput = row.querySelector('input[name="unit_value"]');
            if (unitId) {
            const unit = unitsData.find(u => u.id === unitId);
            if (unit) {
            unitInput.value = unit.symbol;
            unitValueInput.value = unit.name;
            } else {
            unitInput.value = '';
            unitValueInput.value = '';
            }
            } else {
            unitInput.value = '';
            unitValueInput.value = '';
            }
            }

            // Clear product info
            function clearProductInfo(select) {
            const row = select.closest('tr');
            row.querySelector('input[name="product_id"]').value = '';
            row.querySelector('input[name="product_code"]').value = '';
            row.querySelector('input[name="unit"]').value = '';
            row.querySelector('input[name="unit_value"]').value = '';
            }

            // Add new row
            function addRow() {
            const tbody = document.getElementById('itemsTableBody');
            const currentRows = tbody.querySelectorAll('tr').length;
            const nextSTT = currentRows + 1;
            const firstRow = tbody.querySelector('tr');
            const newRow = firstRow.cloneNode(true);
            // Reset values in new row
            newRow.querySelector('textarea[name="stt"]').value = nextSTT;
            newRow.querySelector('select[name="product_name"]').selectedIndex = 0;
            newRow.querySelector('input[name="product_id"]').value = '';
            newRow.querySelector('input[name="product_code"]').value = '';
            newRow.querySelector('input[name="unit"]').value = '';
            newRow.querySelector('input[name="unit_value"]').value = '';
            newRow.querySelector('textarea[name="quantity"]').value = '';
            newRow.querySelector('textarea[name="note"]').value = '';
            newRow.querySelector('.delete-row-btn').style.display = 'inline-flex';
            setupRowEventHandlers(newRow);
            tbody.appendChild(newRow);
            updateDeleteButtons();
            }

// Setup event handlers for row
            function setupRowEventHandlers(row) {
            const productSelect = row.querySelector('select[name="product_name"]');
            productSelect.onchange = function() { handleProductChange(this); };
            const quantityTextarea = row.querySelector('textarea[name="quantity"]');
            quantityTextarea.oninput = function() {
            validateQuantity(this);
// Removed autoResize call for quantity
            };
            quantityTextarea.onblur = function() { formatQuantity(this); };
            quantityTextarea.onkeypress = function(event) { return isNumberKey(event); };
            const sttTextarea = row.querySelector('textarea[name="stt"]');
            sttTextarea.oninput = function() { autoResize(this); };
// Note textarea - removed autoResize
            const noteTextarea = row.querySelector('textarea[name="note"]');
// No oninput handler for note textarea to prevent auto-resize

            const deleteBtn = row.querySelector('.delete-row-btn');
            deleteBtn.onclick = function() { deleteRow(this); };
            }

// Delete row
            function deleteRow(button) {
            const row = button.closest('tr');
            row.remove();
            updateRowNumbers();
            updateDeleteButtons();
            }

// Update row numbers
            function updateRowNumbers() {
            const rows = document.querySelectorAll('#itemsTableBody tr');
            rows.forEach((row, index) => {
            row.querySelector('textarea[name="stt"]').value = index + 1;
            });
            }

// Update delete buttons visibility
            function updateDeleteButtons() {
            const rows = document.querySelectorAll('#itemsTableBody tr');
            const deleteButtons = document.querySelectorAll('.delete-row-btn');
            deleteButtons.forEach(button => {
            button.style.display = rows.length > 1 ? 'inline-flex' : 'none';
            });
            }

// Form submission validation
            document.getElementById('exportForm').addEventListener('submit', function(e) {
            const requiredFields = this.querySelectorAll('[required]');
            let isValid = true;
            requiredFields.forEach(field => {
            if (!field.value.trim()) {
            isValid = false;
            field.style.borderColor = '#dc3545';
            } else {
            field.style.borderColor = '#28a745';
            }
            });
// Check if at least one product is selected
            const productSelects = document.querySelectorAll('select[name="product_name"]');
            let hasProduct = false;
            productSelects.forEach(select => {
            if (select.value.trim()) {
            hasProduct = true;
            }
            });
            if (!hasProduct) {
            isValid = false;
            alert('Vui lòng chọn ít nhất một sản phẩm!');
            }

            if (!isValid) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ thông tin bắt buộc!');
            }
            });
        </script>
    </body>
</html>