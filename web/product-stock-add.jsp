<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Users" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Thông Tin Tồn Kho</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #f5f5f5;
            line-height: 1.6;
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

        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .header h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
            font-weight: 300;
            color: white;
        }

        .header p {
            font-size: 1.1rem;
            opacity: 0.9;
            color: white;
        }

        .nav-buttons {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: #667eea;
            color: white;
        }

        .btn-primary:hover {
            background: #5a67d8;
            text-decoration: none;
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
            text-decoration: none;
            color: white;
        }

        .btn-success {
            background: #28a745;
            color: white;
        }

        .btn-success:hover {
            background: #218838;
        }

        .form-container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 25px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group.full-width {
            grid-column: 1 / -1;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #495057;
        }

        .form-label.required::after {
            content: " *";
            color: #dc3545;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s ease;
            font-family: inherit;
        }

        .form-control:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-control.error {
            border-color: #dc3545;
        }

        textarea.form-control {
            min-height: 100px;
            resize: vertical;
        }

        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }

        .success-message {
            background: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #c3e6cb;
        }

        .form-help {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }

        .required-note {
            color: #dc3545;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .section-title {
            font-size: 1.2rem;
            font-weight: 600;
            color: #495057;
            margin: 30px 0 15px 0;
            padding-bottom: 10px;
            border-bottom: 2px solid #e9ecef;
        }

        .product-info-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
            margin-bottom: 20px;
        }

        .product-info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }

        .info-item {
            display: flex;
            flex-direction: column;
        }

        .info-label {
            font-weight: 600;
            color: #495057;
            font-size: 12px;
            text-transform: uppercase;
            margin-bottom: 5px;
        }

        .info-value {
            color: #333;
            font-size: 14px;
        }

        .button-group {
            display: flex;
            gap: 15px;
            justify-content: flex-end;
            margin-top: 30px;
        }

        @media (max-width: 768px) {
            .form-grid {
                grid-template-columns: 1fr;
            }
            
            .nav-buttons {
                flex-direction: column;
            }
            
            .main-content {
                padding: 10px;
            }
            
            .button-group {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="layout-container">
        <jsp:include page="/include/sidebar.jsp" />
        <div class="main-content">
            <div class="header">
                <h1>Thêm Thông Tin Tồn Kho</h1>
                <p>Thêm thông tin tồn kho cho sản phẩm</p>
            </div>

            <!-- Navigation Buttons -->
            <div class="nav-buttons">
                <a href="${pageContext.request.contextPath}/product-stock/list" class="btn btn-secondary">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">arrow_back</span>
                    Quay lại danh sách
                </a>
                <a href="${pageContext.request.contextPath}/categoriesforward.jsp" class="btn btn-secondary">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">home</span>
                    Trang chủ
                </a>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="error-message">
                    <strong>Lỗi!</strong> ${error}
                </div>
            </c:if>

            <!-- Form -->
            <div class="form-container">
                <div class="required-note">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px; color: #dc3545;">info</span>
                    Các trường có dấu (*) là bắt buộc
                </div>

                <form method="post" action="${pageContext.request.contextPath}/product-stock/add" onsubmit="return validateForm()">
                    <div class="form-grid">
                        <!-- Product Selection -->
                        <div class="form-group">
                            <label class="form-label required" for="productId">Sản phẩm</label>
                            <select name="productId" id="productId" class="form-control" required>
                                <option value="">-- Chọn sản phẩm --</option>
                                <c:forEach var="availableProduct" items="${availableProducts}">
                                    <option value="${availableProduct.id}" 
                                            ${availableProduct.id == param.productId ? 'selected' : ''}>
                                        ${availableProduct.code} - ${availableProduct.name}
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-help">Chọn sản phẩm muốn thêm thông tin tồn kho</div>
                        </div>

                        <!-- Quantity -->
                        <div class="form-group">
                            <label class="form-label required" for="qty">Số lượng tồn kho</label>
                            <input type="number" id="qty" name="qty" class="form-control" 
                                   value="${param.qty}" min="0" step="0.01" required placeholder="0">
                            <div class="form-help">Nhập số lượng hiện có trong kho</div>
                        </div>

                        <!-- Minimum Threshold -->
                        <div class="form-group">
                            <label class="form-label" for="minThreshold">Ngưỡng cảnh báo</label>
                            <input type="number" id="minThreshold" name="minThreshold" class="form-control" 
                                   value="${param.minThreshold}" min="0" step="0.01" placeholder="10">
                            <div class="form-help">Ngưỡng tối thiểu để cảnh báo sắp hết hàng (tùy chọn)</div>
                        </div>
                    </div>

                    <!-- Button Group -->
                    <div class="button-group">
                        <a href="${pageContext.request.contextPath}/product-stock/list" class="btn btn-secondary">
                            <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">cancel</span>
                            Hủy bỏ
                        </a>
                        <button type="submit" class="btn btn-success">
                            <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">save</span>
                            Lưu thông tin
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function validateForm() {
            const productId = document.getElementById('productId');
            const qty = document.getElementById('qty');

            // Reset error states
            productId.classList.remove('error');
            qty.classList.remove('error');

            let isValid = true;

            // Validate product selection
            if (!productId.value || productId.value.trim() === '') {
                productId.classList.add('error');
                isValid = false;
            }

            // Validate quantity
            if (!qty.value || qty.value.trim() === '' || parseFloat(qty.value) < 0) {
                qty.classList.add('error');
                isValid = false;
            }

            if (!isValid) {
                alert('Vui lòng điền đầy đủ thông tin bắt buộc và kiểm tra dữ liệu nhập vào!');
                return false;
            }

            return true;
        }

        // Auto-focus on first input
        document.addEventListener('DOMContentLoaded', function() {
            const firstInput = document.querySelector('.form-control:not([readonly]):not([type="hidden"])');
            if (firstInput) {
                firstInput.focus();
            }
        });
    </script>
</body>
</html>
