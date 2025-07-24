<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="model.Users" session="true" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cập Nhật Sản Phẩm - Warehouse Manager</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f5f7fa;
                color: #333;
                line-height: 1.6;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }

            .header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 30px;
                border-radius: 10px;
                margin-bottom: 30px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }

            .header h1 {
                font-size: 2.5rem;
                margin-bottom: 10px;
                font-weight: 300;
                color: black;
            }

            .header p {
                font-size: 1.1rem;
                opacity: 0.9;
                color: black;
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

            .btn-warning {
                background: #ffc107;
                color: #212529;
            }

            .btn-warning:hover {
                background: #e0a800;
            }

            .btn-danger {
                background: #dc3545;
                color: white;
            }

            .btn-danger:hover {
                background: #c82333;
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
            
            .form-control:disabled {
                background-color: #f8f9fa;
                color: #6c757d;
            }

            .form-control[readonly] {
                background-color: #f8f9fa;
                color: #6c757d;
                cursor: not-allowed;
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

            .image-preview {
                max-width: 200px;
                max-height: 200px;
                border-radius: 5px;
                margin-top: 10px;
            }

            .current-image {
                max-width: 150px;
                max-height: 150px;
                border-radius: 5px;
                border: 2px solid #dee2e6;
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

            @media (max-width: 768px) {
                .form-grid {
                    grid-template-columns: 1fr;
                }

                .nav-buttons {
                    flex-direction: column;
                }

                .container {
                    padding: 10px;
                }

                .product-info-grid {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <div class="header">
                    <h1>Cập Nhật Sản Phẩm</h1>
                    <p>Chỉnh sửa thông tin sản phẩm trong hệ thống kho</p>
                </div>
                
                <div class="nav-buttons">
                    <a href="product-list" class="btn btn-primary">← Quay lại Danh Sách</a>
                </div>

                <!-- Error/Success Messages -->
                <c:if test="${not empty error}">
                    <div class="error-message">
                        <strong>Lỗi:</strong> ${error}
                    </div>
                </c:if>

                <c:if test="${not empty success}">
                    <div class="success-message">
                        <strong>Thành công:</strong> ${success}
                    </div>
                </c:if>
                
                <!-- Check if product exists -->
                <c:choose>
                    <c:when test="${empty product}">
                        <div class="error-message">
                            <strong>Lỗi:</strong> Không tìm thấy sản phẩm. Vui lòng kiểm tra lại đường dẫn hoặc quay lại danh sách sản phẩm.
                            <br><br>
                            <strong>Debug Info:</strong><br>
                            - Product object: ${product}<br>
                            - Request URI: ${pageContext.request.requestURI}<br>
                            - Query String: ${pageContext.request.queryString}<br>
                            - Attributes: ${requestScope}<br>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Current Product Information -->
                        <div class="product-info-card">
                            <h3 style="margin-bottom: 15px; color: #495057;">Thông Tin Hiện Tại</h3>
                            <div class="product-info-grid">
                                <div class="info-item">
                                    <span class="info-label">Mã Sản Phẩm</span>
                                    <span class="info-value">${product.code}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Tên Sản Phẩm</span>
                                    <span class="info-value">${product.name}</span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Danh Mục</span>
                                    <span class="info-value">
                                        <c:forEach var="category" items="${categories}">
                                            <c:if test="${product.cate_id == category.id}">
                                                ${category.name}
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${empty categories}">ID: ${product.cate_id}</c:if>
                                    </span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Trạng Thái</span>
                                    <span class="info-value">
                                        <c:choose>
                                            <c:when test="${product.status == 'active'}">Hoạt động</c:when>
                                            <c:otherwise>Ngưng hoạt động</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Ngày Tạo</span>
                                    <span class="info-value">
                                        <fmt:formatDate value="${product.createdDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                </div>
                                <div class="info-item">
                                    <span class="info-label">Cập Nhật Cuối</span>
                                    <span class="info-value">
                                        <fmt:formatDate value="${product.updatedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-container">
                            <form method="post" action="update-product" enctype="application/x-www-form-urlencoded">
                                <input type="hidden" name="id" value="${product.id}">

                                <div class="required-note">
                                    Các trường có dấu <span style="color: #dc3545;">*</span> là bắt buộc
                                </div>

                                <!-- Basic Information -->
                                <div class="section-title">Thông Tin Cơ Bản</div>
                                <div class="form-grid">
                                    <div class="form-group">
                                        <label class="form-label required" for="name">Tên Sản Phẩm</label>
                                        <input type="text" id="name" name="name" class="form-control" 
                                               value="${product.name}" maxlength="100" required>
                                        <div class="form-help">Tối đa 100 ký tự</div>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label class="form-label" for="code">Mã Sản Phẩm</label>
                                        <input type="text" id="code" name="code" class="form-control" 
                                               value="${product.code}" maxlength="50" readonly>
                                        <div class="form-help">Mã sản phẩm không thể thay đổi</div>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label class="form-label required" for="categoryId">Danh Mục</label>
                                        <select id="categoryId" name="categoryId" class="form-control" required>
                                            <option value="">-- Chọn danh mục --</option>
                                            <c:forEach var="category" items="${categories}">
                                                <option value="${category.id}" 
                                                        ${product.cate_id == category.id ? 'selected' : ''}>
                                                    ${category.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label required" for="unitId">Đơn Vị Tính</label>
                                        <select id="unitId" name="unitId" class="form-control" required>
                                            <option value="">-- Chọn đơn vị --</option>
                                            <c:forEach var="unit" items="${units}">
                                                <option value="${unit.id}" 
                                                        ${product.unit_id == unit.id ? 'selected' : ''}
                                                        ${unit.status != 1 ? 'style="color: #999; font-style: italic;"' : ''}>
                                                    ${unit.name} (${unit.symbol})${unit.status != 1 ? ' [Ngừng hoạt động]' : ''}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty units}">
                                            <c:forEach var="unit" items="${units}">
                                                <c:if test="${product.unit_id == unit.id && unit.status != 1}">
                                                    <small class="text-warning">Đơn vị hiện tại đã ngừng hoạt động. Bạn có thể giữ nguyên hoặc thay đổi sang đơn vị khác.</small>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label required" for="status">Trạng Thái</label>
                                        <select id="status" name="status" class="form-control" required>
                                            <option value="">-- Chọn trạng thái --</option>
                                            <option value="active" ${product.status == 'active' ? 'selected' : ''}>Hoạt động</option>
                                            <option value="inactive" ${product.status == 'inactive' ? 'selected' : ''}>Ngưng hoạt động</option>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label" for="stockQuantity">Số Lượng Tồn Kho</label>
                                        <input type="number" id="stockQuantity" name="stockQuantity" class="form-control" 
                                               value="${product.stockQuantity != null ? product.stockQuantity : ''}" 
                                               min="0" step="0.01" placeholder="0">
                                        <div class="form-help">Cập nhật số lượng hiện có trong kho</div>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label" for="minStockThreshold">Ngưỡng Cảnh Báo Tồn Kho</label>
                                        <input type="number" id="minStockThreshold" name="minStockThreshold" class="form-control" 
                                               value="${product.minStockThreshold != null ? product.minStockThreshold : ''}" 
                                               min="0" step="0.01" placeholder="10">
                                        <div class="form-help">Số lượng tối thiểu để cảnh báo sắp hết hàng (mặc định: 10)</div>
                                    </div>
                                </div>

                                <!-- Additional Information -->
                                <div class="section-title">Thông Tin Bổ Sung</div>
                                <div class="form-grid">
                                    <div class="form-group">
                                        <label class="form-label" for="supplierId">Nhà Cung Cấp</label>
                                        <select id="supplierId" name="supplierId" class="form-control">
                                            <option value="">-- Chọn nhà cung cấp --</option>
                                            <c:forEach var="supplier" items="${suppliers}">
                                                <option value="${supplier.supplierID}" 
                                                        ${product.supplierId == supplier.supplierID ? 'selected' : ''}>
                                                    ${supplier.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label" for="expirationDate">Ngày Hết Hạn</label>
                                        <input type="date" id="expirationDate" name="expirationDate" class="form-control" 
                                               value="<fmt:formatDate value='${product.expirationDate}' pattern='yyyy-MM-dd'/>">
                                        <div class="form-help">Để trống nếu sản phẩm không có hạn sử dụng</div>
                                    </div>

                                    <div class="form-group full-width">
                                        <label class="form-label" for="description">Mô Tả Sản Phẩm</label>
                                        <textarea id="description" name="description" class="form-control" 
                                                  rows="4" placeholder="Nhập mô tả chi tiết về sản phẩm...">${product.description}</textarea>
                                    </div>

                                    <div class="form-group full-width">
                                        <label class="form-label" for="additionalNotes">Ghi Chú Bổ Sung</label>
                                        <textarea id="additionalNotes" name="additionalNotes" class="form-control" 
                                                  rows="3" placeholder="Ghi chú thêm về sản phẩm...">${product.additionalNotes}</textarea>
                                        <div class="form-help">Thông tin bổ sung, lưu ý đặc biệt về sản phẩm</div>
                                    </div>
                                </div>

                                <!-- Submit Buttons -->
                                <div style="text-align: center; margin-top: 30px;">
                                    <button type="submit" class="btn btn-warning" style="padding: 15px 30px; font-size: 16px;">
                                        Cập Nhật Sản Phẩm
                                    </button>
                                    <a href="product-list" class="btn btn-secondary" style="padding: 15px 30px; font-size: 16px; margin-left: 15px;">
                                        Hủy Bỏ
                                    </a>
                                    <button type="button" onclick="confirmDelete()" class="btn btn-danger" 
                                            style="padding: 15px 30px; font-size: 16px; margin-left: 15px;">
                                        Xóa Sản Phẩm
                                    </button>
                                </div>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <script>
            // Form validation
            document.querySelector('form').addEventListener('submit', function(e) {
                const requiredFields = document.querySelectorAll('[required]');
                let isValid = true;

                requiredFields.forEach(field => {
                    if (!field.value.trim()) {
                        field.classList.add('error');
                        isValid = false;
                    } else {
                        field.classList.remove('error');
                    }
                });

                if (!isValid) {
                    e.preventDefault();
                    alert('Vui lòng điền đầy đủ các trường bắt buộc.');
                }
            });

            // Remove error styling on input
            document.querySelectorAll('.form-control').forEach(field => {
                field.addEventListener('input', function() {
                    this.classList.remove('error');
                });
            });

            // Delete confirmation
            function confirmDelete() {
                <c:if test="${not empty product}">
                    var productName = '${product.name}';
                    var productCode = '${product.code}';
                    
                    var confirmMessage = 'XÁC NHẬN XÓA SẢN PHẨM\n\n' +
                        'Sản phẩm: ' + productName + '\n' +
                        'Mã sản phẩm: ' + productCode + '\n\n' +
                        'Bạn có chắc chắn muốn xóa sản phẩm này?';
                    
                    if (confirm(confirmMessage)) {
                        window.location.href = 'delete-product?id=${product.id}';
                    }
                </c:if>
            }

            // Initialize image preview on page load
            window.addEventListener('load', function() {
                const imageUrlField = document.getElementById('imageUrl');
                if (imageUrlField && imageUrlField.value) {
                    imageUrlField.dispatchEvent(new Event('input'));
                }
            });
        </script>
    </body>
</html>
