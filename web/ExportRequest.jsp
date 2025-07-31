<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đơn Yêu Cầu Xuất Kho</title>
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

                <form id="exportForm" action="exportRequest" method="post">
                    <h3>Thông tin người yêu cầu</h3>
                    <div class="information-user">
                        <div class="form-group">
                            <label>Người dùng</label>
                            <input type="text" value="${currentUser}" readonly> 
                        </div>
                        <div class="form-group">
                            <label>Vai trò <span style="color: red;">*</span></label>
                            <select name="role" required>
                                <option value="" disabled selected>-- Chọn vai trò --</option>
                                <option value="Nhân viên">Nhân viên</option>
                                <option value="Trưởng phòng">Trưởng phòng</option>
                                <option value="Giám đốc">Giám đốc</option>
                                <option value="Kế toán">Kế toán</option>
                                <option value="Kho">Kho</option>
                                <option value="Admin">Admin</option>
                            </select>
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
                        <button type="button" class="add-row-btn" onclick="addRow()">Thêm hàng</button>
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
                                        <select name="unit" style="width: 100%;" disabled>
                                            <option value="">-- Chọn đơn vị --</option>
                                            <c:forEach var="unit" items="${unitsList}">
                                                <option value="${unit.symbol}" data-id="${unit.id}">${unit.symbol} - ${unit.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td><input type="number" name="quantity" class="quantity-input" style="width: 100%; text-align: center;" min="0.01" step="0.01" placeholder="0" required /></td>
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
            // Tạo dữ liệu products và units từ server
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
            function autoResize(textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = textarea.scrollHeight + 'px';
            }

            function addRow() {
            rowCounter++;
            const tbody = document.getElementById('itemsTableBody');
            const newRow = document.createElement('tr');
            // Tạo options cho sản phẩm
            let productOptions = '<option value="" disabled selected>-- Chọn sản phẩm --</option>';
            productsData.forEach(product => {
            productOptions += `<option value="${product.id}" data-code="${product.code}" data-unit="${product.unit_id}" data-name="${product.name}">${product.name}</option>`;
            });
            // Tạo options cho đơn vị
            let unitOptions = '<option value="">-- Chọn đơn vị --</option>';
            unitsData.forEach(unit => {
            unitOptions += `<option value="${unit.symbol}" data-id="${unit.id}">${unit.symbol} - ${unit.name}</option>`;
            });
            newRow.innerHTML = `
                  <td class="stt-cell">${rowCounter}</td>
                  <td>
                      <select name="product_id" onchange="updateProductInfo(this)" style="width: 100%;" required>
            ${productOptions}
                      </select>
                      <input type="hidden" name="product_name" />
                  </td>
                  <td><input type="text" name="product_code" readonly style="width: 100%;" /></td>
                  <td>
                      <select name="unit" style="width: 100%;" disabled>
            ${unitOptions}
                      </select>
                  </td>
                  <td><input type="number" name="quantity" class="quantity-input" style="width: 100%; text-align: center;" min="0.01" step="0.01" placeholder="0" required /></td>
                  <td><textarea name="note" rows="1" style="width: 100%; resize: none;" oninput="autoResize(this)" placeholder="Ghi chú..."></textarea></td>
                  <td><button type="button" class="delete-row-btn" onclick="deleteRow(this)">Xóa</button></td>
              `;
            tbody.appendChild(newRow);
            updateDeleteButtons();
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
            // Cập nhật product code
            row.querySelector("input[name='product_code']").value = code || "";
            // Cập nhật hidden product name
            row.querySelector("input[name='product_name']").value = productName || "";
            // Cập nhật dropdown đơn vị
            const unitSelect = row.querySelector("select[name='unit']");
            if (unitId && unitId !== 'null' && unitId !== '') {
            unitSelect.disabled = false;
            // Reset selection trước
            unitSelect.selectedIndex = 0;
            // Tìm và chọn option có data-id tương ứng với unit_id của sản phẩm
            const unitOptions = unitSelect.querySelectorAll('option');
            let found = false;
            unitOptions.forEach(option => {
            if (option.getAttribute('data-id') === unitId) {
            option.selected = true;
            found = true;
            console.log('Đã chọn unit:', option.text, 'cho sản phẩm:', productName);
            }
            });
            if (!found) {
            console.log('Không tìm thấy unit với ID:', unitId);
            }
            } else {
            unitSelect.disabled = true;
            unitSelect.selectedIndex = 0;
            console.log('Sản phẩm không có unit_id');
            }
            }

            // Validate form trước khi submit
            document.getElementById('exportForm').addEventListener('submit', function(e) {
            const rows = document.querySelectorAll('#itemsTableBody tr');
            let hasValidItem = false;
            rows.forEach(row => {
            const productSelect = row.querySelector('select[name="product_id"]');
            const quantityInput = row.querySelector('input[name="quantity"]');
            if (productSelect.value && quantityInput.value && parseFloat(quantityInput.value) > 0) {
            hasValidItem = true;
            }
            });
            if (!hasValidItem) {
            e.preventDefault();
            alert('Vui lòng thêm ít nhất một sản phẩm với số lượng hợp lệ!');
            return false;
            }
            });
            // Khởi tạo khi trang load
            document.addEventListener('DOMContentLoaded', function () {
            console.log('Products data:', productsData);
            console.log('Units data:', unitsData);
            updateDeleteButtons();
            });
        </script>
    </body>
</html>