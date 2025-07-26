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
                color: #dc3545;
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
                border-color: #dc3545;
            }

            input[readonly] {
                background-color: #f8f9fa;
                cursor: not-allowed;
            }

            /* Validation styles */
            .error {
                border-color: #dc3545 !important;
                background-color: #fff5f5;
            }

            .error-message {
                color: #dc3545;
                font-size: 12px;
                margin-top: 5px;
                display: none;
            }

            .error-message.show {
                display: block;
            }

            .valid {
                border-color: #28a745 !important;
                background-color: #f8fff8;
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
                min-width: 200px;
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
                background-color: #dc3545;
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

            .stt-cell {
                width: 60px;
                text-align: center;
                font-weight: bold;
                background-color: #f8f9fa;
            }

            .quantity-input {
                text-align: center;
            }

            .quantity-container {
                display: flex;
                align-items: center;
                gap: 5px;
            }

            .quantity-btn {
                background: #dc3545;
                color: white;
                border: none;
                padding: 5px 10px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                transition: background 0.3s;
            }

            .quantity-btn:hover {
                background: #c82333;
            }

            .quantity-input-field {
                width: 60px;
                text-align: center;
                padding: 5px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }

            /* Style cho nút thêm/xóa */
            .table-buttons {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 8px;
                margin: 12px 0;
            }

            .btn {
                padding: 10px 16px;
                border: none;
                border-radius: 6px;
                font-size: 13px;
                font-weight: 500;
                cursor: pointer;
                transition: all 0.2s ease;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 6px;
                min-width: 120px;
                justify-content: center;
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
                background: #dc3545;
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
                background: #c82333;
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

            .delete-row-btn {
                background: #dc3545;
                color: white;
                border: none;
                padding: 5px 10px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 12px;
            }

            .delete-row-btn:hover {
                background: #c82333;
            }

            /* Alert styles */
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
            .layout-container {
                display: flex;
                min-height: 100vh;
            }

            .main-content {
                flex: 1;
                padding: 20px;
                background: #f5f5f5;
            }

            .alert {
                padding: 15px;
                margin-bottom: 20px;
                border: 1px solid transparent;
                border-radius: 4px;
            }

            .alert-danger {
                color: #721c24;
                background-color: #f8d7da;
                border-color: #f5c6cb;
            }

            .alert-success {
                color: #155724;
                background-color: #d4edda;
                border-color: #c3e6cb;
            }

            .alert-success {
                animation: slideDown 0.5s ease-out;
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

            .alert-success.auto-hide {
                animation: slideDown 0.5s ease-out, fadeOut 0.5s ease-out 4.5s forwards;
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
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <h1>ĐƠN YÊU CẦU XUẤT KHO</h1>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success auto-hide" id="successAlert">
                        <strong>✓ Thành công!</strong> ${sessionScope.successMessage}
                    </div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>
                <c:set var="roleID" value="${requestScope.roleID}"/>
                <form id="exportForm" action="exportRequest" method="post">
                    <h3>Thông tin người yêu cầu</h3>
                    <div class="information-user">
                        <div class="form-group">
                            <label>Người dùng</label>
                            <input type="text" value="${currentUser}" readonly> 
                        </div>
                        <div class="form-group">
                            <label>Vai trò</label>
                            <c:choose>
                                <c:when test="${roleID == 3}">
                                    <input type="text" name="role" value="Nhân viên" readonly 
                                           style="background-color: #f8f9fa; cursor: not-allowed;">
                                </c:when>
                                <c:when test="${roleID == 4}">
                                    <input type="text" name="role" value="Giám đốc" readonly 
                                           style="background-color: #f8f9fa; cursor: not-allowed;">
                                </c:when>
                                <c:when test="${roleID == 2}">
                                    <input type="text" name="role" value="Kho" readonly 
                                           style="background-color: #f8f9fa; cursor: not-allowed;">
                                </c:when>
                                <c:when test="${roleID == 1}">
                                    <input type="text" name="role" value="Admin" readonly 
                                           style="background-color: #f8f9fa; cursor: not-allowed;">
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="role" value="Chưa xác định" readonly 
                                           style="background-color: #f8f9fa; cursor: not-allowed;">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="form-group">
                            <label>Ngày yêu cầu</label>
                            <input type="text" value="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='dd/MM/yyyy'/>" readonly>
                        </div>
                    </div>

                    <h3>Chi tiết xuất kho</h3>
                    <div class="information-items">
                        <div class="row">
                            <div class="form-group">
                                <label>ID</label>
                                <textarea rows="1" name="export_request_id" style="width: 100%; resize: none;overflow: hidden;background-color: #f8f9fa; cursor: not-allowed;" oninput="autoResize(this)" readonly>${nextExportID}</textarea>
                            </div>
                        </div>
                    </div>

                    <div class="items-table">
                        <label>Vui lòng nhập chi tiết mặt hàng xuất kho</label>

                        <div class="alert alert-info" style="margin: 15px 0;">
                            <i class="fas fa-info-circle"></i>
                            Vui lòng chọn sản phẩm và nhập đầy đủ thông tin cho từng mặt hàng xuất kho
                        </div>

                        <div class="table-buttons">
                            <button type="button" class="btn btn-success" onclick="addRow()">
                                <i class="fas fa-plus"></i> Thêm
                            </button>
                        </div>

                        <table>
                            <thead>
                                <tr>
                                    <th style="width: 60px;">STT</th>
                                    <th>Tên mặt hàng <span style="color: red;">*</span></th>
                                    <th>Code</th>
                                    <th>Đơn vị</th>
                                    <th>Số lượng <span style="color: red;">*</span></th>
                                    <th>Ghi chú</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="itemsTableBody">
                                <tr>
                                    <td class="stt-cell">1</td>
                                    <td>
                                        <select name="product_id" onchange="updateProductInfo(this)" style="width: 100%;" required>
                                            <option value="" disabled selected>-- Chọn sản phẩm --</option>
                                            <c:forEach var="product" items="${productsList}">
                                                <option 
                                                    value="${product.id}" 
                                                    data-code="${product.code}" 
                                                    data-unit="${product.unit_id}"
                                                    data-name="${product.name}">
                                                    ${product.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <input type="hidden" name="product_name" />
                                    </td>
                                    <td><input type="text" name="product_code" readonly style="width: 100%;" /></td>
                                    <td>
                                        <input type="text" name="unit" readonly style="width: 100%; text-align: center;" />
                                        <input type="hidden" name="unit_value" />
                                    </td>
                                    <td>
                                        <div class="quantity-container">
                                            <button type="button" class="quantity-btn" onclick="decrementQuantity(this)">-</button>
                                            <input type="number" name="quantity" class="quantity-input-field" min="1" step="1" placeholder="1" required />
                                            <button type="button" class="quantity-btn" onclick="incrementQuantity(this)">+</button>
                                        </div>
                                    </td>
                                    <td><textarea name="note" rows="1" style="width: 100%; box-sizing: border-box; overflow: hidden; resize: none;" oninput="autoResize(this)" placeholder="Ghi chú..."></textarea></td>
                                    <td><button type="button" class="delete-row-btn" onclick="deleteRow(this)" style="display: none;">Xóa</button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="button-container">
                        <button type="submit" class="submit-btn">Gửi yêu cầu</button>
                        <a href="Admin.jsp" class="back-btn">Quay lại</a>
                    </div>
                </form>
            </div>
        </div>

        <script>
            let rowCounter = 1;
            const productsData = [
            <c:forEach var="product" items="${productsList}" varStatus="status">
            {
                id: '${product.id}',
                name: '${product.name}',
                code: '${product.code}',
                unit_id: '${product.unit_id}'
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];
            const unitsData = [
            <c:forEach var="unit" items="${unitsList}" varStatus="status">
            {
                id: '${unit.id}',
                name: '${unit.name}',
                symbol: '${unit.symbol}'
            }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
            ];

            document.addEventListener('DOMContentLoaded', function() {
                const successAlert = document.getElementById('successAlert');
                if (successAlert) {
                    setTimeout(function() {
                        successAlert.style.display = 'none';
                    }, 5000);
                }

                if (successAlert) {
                    resetForm();
                }

                updateDeleteButtons();
                console.log('Products data:', productsData);
                console.log('Units data:', unitsData);
            });

            function resetForm() {
                const roleSelect = document.querySelector('select[name="role"]');
                if (roleSelect) {
                    roleSelect.selectedIndex = 0;
                }

                const tbody = document.getElementById('itemsTableBody');
                const rows = tbody.querySelectorAll('tr');
                for (let i = rows.length - 1; i > 0; i--) {
                    rows[i].remove();
                }

                const firstRow = tbody.querySelector('tr');
                if (firstRow) {
                    firstRow.querySelector('select[name="product_id"]').selectedIndex = 0;
                    firstRow.querySelector('input[name="product_name"]').value = '';
                    firstRow.querySelector('input[name="product_code"]').value = '';
                    firstRow.querySelector('input[name="unit"]').value = '';
                    firstRow.querySelector('input[name="unit_value"]').value = '';
                    firstRow.querySelector('input[name="quantity"]').value = '';
                    firstRow.querySelector('textarea[name="note"]').value = '';
                }

                rowCounter = 1;
                updateDeleteButtons();
            }

            function autoResize(textarea) {
                textarea.style.height = 'auto';
                textarea.style.height = textarea.scrollHeight + 'px';
            }

            function addRow() {
                const tbody = document.getElementById('itemsTableBody');
                const currentRows = tbody.querySelectorAll('tr').length;
                const nextSTT = currentRows + 1;
                const firstRow = tbody.querySelector('tr');
                const newRow = firstRow.cloneNode(true);

                const sttCell = newRow.querySelector('.stt-cell');
                sttCell.textContent = nextSTT;

                const productSelect = newRow.querySelector('select[name="product_id"]');
                productSelect.selectedIndex = 0;
                const codeInput = newRow.querySelector('input[name="product_code"]');
                codeInput.value = '';
                const productNameInput = newRow.querySelector('input[name="product_name"]');
                productNameInput.value = '';
                const unitInput = newRow.querySelector('input[name="unit"]');
                unitInput.value = '';
                const unitValueInput = newRow.querySelector('input[name="unit_value"]');
                unitValueInput.value = '';
                const quantityInput = newRow.querySelector('input[name="quantity"]');
                quantityInput.value = '';
                const noteTextarea = newRow.querySelector('textarea[name="note"]');
                noteTextarea.value = '';

                const deleteBtn = newRow.querySelector('.delete-row-btn');
                deleteBtn.style.display = 'inline-block';

                newRow.querySelector('select[name="product_id"]').onchange = function () {
                    updateProductInfo(this);
                };
                newRow.querySelector('textarea[name="note"]').oninput = function () {
                    autoResize(this);
                };

                const quantityButtons = newRow.querySelectorAll('.quantity-btn');
                quantityButtons[0].onclick = function() { decrementQuantity(this); };
                quantityButtons[1].onclick = function() { incrementQuantity(this); };

                tbody.appendChild(newRow);
                rowCounter = nextSTT;
                updateDeleteButtons();
            }

            function removeLastRow() {
                const tbody = document.getElementById('itemsTableBody');
                const rows = tbody.querySelectorAll('tr');
                if (rows.length > 1) {
                    tbody.removeChild(rows[rows.length - 1]);
                    rowCounter = rows.length - 1;
                    updateDeleteButtons();
                } else {
                    alert('Phải có ít nhất một sản phẩm trong đơn xuất kho!');
                }
            }

            function openSupplementModal() {
                alert('Chức năng bổ sung sản phẩm sẽ được phát triển!');
            }

            function deleteRow(button) {
                const row = button.closest('tr');
                row.remove();
                updateRowNumbers();
                updateDeleteButtons();
            }

            function updateRowNumbers() {
                const tbody = document.getElementById('itemsTableBody');
                const rows = tbody.querySelectorAll('tr');
                rows.forEach((row, index) => {
                    const sttCell = row.querySelector('.stt-cell');
                    if (sttCell) {
                        sttCell.textContent = index + 1;
                    }
                });
                rowCounter = rows.length;
            }

            function updateDeleteButtons() {
                const tbody = document.getElementById('itemsTableBody');
                const rows = tbody.querySelectorAll('tr');
                const deleteButtons = tbody.querySelectorAll('.delete-row-btn');
                deleteButtons.forEach(button => {
                    if (rows.length > 1) {
                        button.style.display = 'inline-block';
                    } else {
                        button.style.display = 'none';
                    }
                });
            }

            function updateProductInfo(selectElement) {
                const selectedOption = selectElement.options[selectElement.selectedIndex];
                const code = selectedOption.getAttribute("data-code");
                const unitId = selectedOption.getAttribute("data-unit");
                const productName = selectedOption.getAttribute("data-name");
                console.log('Selected product:', productName, 'Unit ID:', unitId);
                const row = selectElement.closest("tr");
                row.querySelector("input[name='product_code']").value = code || "";
                row.querySelector("input[name='product_name']").value = productName || "";
                const unitInput = row.querySelector("input[name='unit']");
                const unitValueInput = row.querySelector("input[name='unit_value']");
                if (unitId && unitId !== 'null' && unitId !== '') {
                    const unit = unitsData.find(u => u.id === unitId);
                    if (unit) {
                        unitInput.value = unit.symbol;
                        unitValueInput.value = unit.name;
                        console.log('Đã chọn unit:', unit.symbol, '-', unit.name, 'cho sản phẩm:', productName);
                    } else {
                        unitInput.value = '';
                        unitValueInput.value = '';
                        console.log('Không tìm thấy unit với ID:', unitId);
                    }
                } else {
                    unitInput.value = '';
                    unitValueInput.value = '';
                    console.log('Sản phẩm không có unit_id');
                }
            }

            function incrementQuantity(button) {
                const input = button.parentElement.querySelector('input[name="quantity"]');
                let value = parseInt(input.value) || 0;
                input.value = value + 1;
            }

            function decrementQuantity(button) {
                const input = button.parentElement.querySelector('input[name="quantity"]');
                let value = parseInt(input.value) || 0;
                if (value > 1) {
                    input.value = value - 1;
                }
            }

            document.getElementById('exportForm').addEventListener('submit', function(e) {
                const rows = document.querySelectorAll('#itemsTableBody tr');
                let hasValidItem = false;
                rows.forEach(row => {
                    const productSelect = row.querySelector('select[name="product_id"]');
                    const quantityInput = row.querySelector('input[name="quantity"]');
                    if (productSelect.value && quantityInput.value && parseInt(quantityInput.value) > 0) {
                        hasValidItem = true;
                    }
                });
                if (!hasValidItem) {
                    e.preventDefault();
                    alert('Vui lòng thêm ít nhất một sản phẩm với số lượng hợp lệ!');
                    return false;
                }
            });
        </script>
    </body>
</html>