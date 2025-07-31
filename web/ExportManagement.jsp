
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Xử lý Xuất Kho | Hệ thống Quản lý Kho</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            :root {
                --primary: #2563eb;
                --primary-dark: #1d4ed8;
                --success: #10b981;
                --success-dark: #059669;
                --danger: #ef4444;
                --danger-dark: #dc2626;
                --warning: #f59e0b;
                --warning-dark: #d97706;
                --info: #06b6d4;
                --info-dark: #0891b2;
                --secondary: #64748b;
                --light: #f8fafc;
                --dark: #1e293b;
                --gray-50: #f9fafb;
                --gray-100: #f3f4f6;
                --gray-200: #e5e7eb;
                --gray-300: #d1d5db;
                --gray-400: #9ca3af;
                --gray-500: #6b7280;
                --gray-600: #4b5563;
                --gray-700: #374151;
                --gray-800: #1f2937;
                --gray-900: #111827;
                --border-radius: 0.5rem;
                --shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
                --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
                --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            }

            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                background: #f5f5f5;
                font-family: 'Inter', sans-serif;
                min-height: 100vh;
                color: var(--gray-800);
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

            .container {
                max-width: 1400px;
                margin: 0 auto;
                background: white;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow-lg);
                overflow: hidden;
                border: 1px solid var(--gray-200);
            }

            .header {
                background: linear-gradient(135deg, var(--danger) 0%, var(--danger-dark) 100%);
                color: white;
                padding: 2rem;
                text-align: center;
                position: relative;
            }

            .header h1 {
                font-size: 2rem;
                font-weight: 700;
                margin-bottom: 0.5rem;
            }

            .header p {
                font-size: 1rem;
                opacity: 0.9;
            }

            .content {
                padding: 2rem;
                background: white;
            }

            .section {
                background: white;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow);
                margin-bottom: 2rem;
                overflow: hidden;
                border: 1px solid var(--gray-200);
            }

            .section-header {
                background: var(--gray-50);
                padding: 1.25rem 1.5rem;
                border-bottom: 1px solid var(--gray-200);
                display: flex;
                align-items: center;
                gap: 0.75rem;
            }

            .section-header h3 {
                font-size: 1.125rem;
                font-weight: 600;
                color: var(--gray-800);
                margin: 0;
            }

            .section-header .icon {
                width: 2rem;
                height: 2rem;
                background: var(--danger);
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 0.875rem;
            }

            .section-body {
                padding: 1.5rem;
                background: white;
            }

            .info-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 1.5rem;
            }

            .info-item {
                display: flex;
                flex-direction: column;
                gap: 0.5rem;
            }

            .info-label {
                font-size: 0.875rem;
                font-weight: 500;
                color: var(--gray-600);
                text-transform: uppercase;
                letter-spacing: 0.025em;
            }

            .info-value {
                display: flex;
                align-items: center;
                padding: 0.75rem;
                background: var(--gray-50);
                border: 1px solid var(--gray-200);
                border-radius: var(--border-radius);
                font-size: 0.875rem;
                color: var(--gray-800);
            }

            .info-value i {
                margin-right: 0.5rem;
                color: var(--danger);
            }

            .table-container {
                overflow-x: auto;
                border-radius: var(--border-radius);
                border: 1px solid var(--gray-200);
                background: white;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background: white;
            }

            th, td {
                padding: 1rem;
                text-align: left;
                border-bottom: 1px solid var(--gray-200);
                font-size: 0.875rem;
            }

            th {
                background: var(--gray-50);
                font-weight: 600;
                color: var(--gray-700);
                text-transform: uppercase;
                letter-spacing: 0.025em;
                font-size: 0.75rem;
                white-space: nowrap;
            }

            td {
                color: var(--gray-800);
                background: white;
            }

            tr:last-child td {
                border-bottom: none;
            }

            tr:hover {
                background: var(--gray-50);
            }

            .form-input {
                width: 100%;
                padding: 0.75rem;
                border: 1px solid var(--gray-300);
                border-radius: var(--border-radius);
                font-size: 0.875rem;
                transition: all 0.2s ease;
                background: white;
            }

            .form-input:focus {
                outline: none;
                border-color: var(--danger);
                box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
            }

            .form-input:read-only {
                background: var(--gray-50);
                color: var(--gray-600);
            }

            .form-input.editable {
                border-color: var(--danger);
                background: #fef2f2;
            }

            .form-input.editable:focus {
                border-color: var(--danger-dark);
                box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
            }

            .form-input.required {
                border-color: var(--danger);
            }

            .quantity-display {
                text-align: center;
                font-weight: 600;
                color: var(--danger-dark);
                background: #fef2f2;
                padding: 0.75rem;
                border: 2px solid var(--danger);
                border-radius: var(--border-radius);
                font-size: 0.875rem;
            }

            .btn {
                display: inline-flex;
                align-items: center;
                gap: 0.5rem;
                padding: 0.875rem 1.5rem;
                font-size: 0.875rem;
                font-weight: 500;
                border: none;
                border-radius: var(--border-radius);
                cursor: pointer;
                transition: all 0.2s ease;
                text-decoration: none;
                justify-content: center;
                min-width: 140px;
            }

            .btn:hover {
                transform: translateY(-1px);
                box-shadow: var(--shadow-md);
            }

            .btn-primary {
                background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
                color: white;
            }

            .btn-primary:hover {
                background: linear-gradient(135deg, var(--primary-dark) 0%, #1e40af 100%);
            }

            .btn-danger {
                background: linear-gradient(135deg, var(--danger) 0%, var(--danger-dark) 100%);
                color: white;
            }

            .btn-danger:hover {
                background: linear-gradient(135deg, var(--danger-dark) 0%, #b91c1c 100%);
            }

            .btn-secondary {
                background: var(--gray-500);
                color: white;
            }

            .btn-secondary:hover {
                background: var(--gray-600);
            }

            .form-footer {
                background: var(--gray-50);
                padding: 2rem;
                border-top: 1px solid var(--gray-200);
                display: flex;
                gap: 1rem;
                justify-content: center;
                flex-wrap: wrap;
            }

            .status-badge {
                display: inline-flex;
                align-items: center;
                gap: 0.25rem;
                padding: 0.375rem 0.75rem;
                font-size: 0.75rem;
                font-weight: 500;
                border-radius: 9999px;
                text-transform: uppercase;
                letter-spacing: 0.025em;
            }

            .status-approved {
                background: #d1fae5;
                color: #065f46;
            }

            .alert {
                padding: 1rem 1.25rem;
                border-radius: var(--border-radius);
                margin-bottom: 1.5rem;
                display: flex;
                align-items: center;
                gap: 0.75rem;
                border-left: 4px solid;
            }

            .alert-info {
                background: #eff6ff;
                border-left-color: var(--primary);
                color: #1e40af;
            }

            .alert-warning {
                background: #fffbeb;
                border-left-color: var(--warning);
                color: #92400e;
            }

            .alert-error {
                background: #fef2f2;
                border-left-color: var(--danger);
                color: #991b1b;
            }

            .breadcrumb {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                margin-bottom: 1.5rem;
                font-size: 0.875rem;
                color: var(--gray-600);
            }

            .breadcrumb a {
                color: var(--primary);
                text-decoration: none;
            }

            .breadcrumb a:hover {
                text-decoration: underline;
            }

            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                backdrop-filter: blur(4px);
            }

            .modal-content {
                background: white;
                margin: 5% auto;
                padding: 0;
                border-radius: var(--border-radius);
                width: 90%;
                max-width: 500px;
                box-shadow: var(--shadow-lg);
                animation: modalSlideIn 0.3s ease;
            }

            @keyframes modalSlideIn {
                from {
                    transform: translateY(-50px);
                    opacity: 0;
                }
                to {
                    transform: translateY(0);
                    opacity: 1;
                }
            }

            .modal-header {
                padding: 1.5rem;
                border-bottom: 1px solid var(--gray-200);
                display: flex;
                align-items: center;
                justify-content: space-between;
                background: white;
            }

            .modal-title {
                font-size: 1.25rem;
                font-weight: 600;
                color: var(--gray-800);
            }

            .modal-close {
                background: none;
                border: none;
                font-size: 1.5rem;
                cursor: pointer;
                color: var(--gray-400);
                padding: 0.25rem;
                border-radius: 50%;
                width: 2rem;
                height: 2rem;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .modal-close:hover {
                background: var(--gray-100);
                color: var(--gray-600);
            }

            .modal-body {
                padding: 1.5rem;
                background: white;
            }

            .modal-footer {
                padding: 1.5rem;
                border-top: 1px solid var(--gray-200);
                display: flex;
                gap: 1rem;
                justify-content: flex-end;
                background: white;
            }

            .reject-form textarea {
                width: 100%;
                min-height: 100px;
                padding: 0.75rem;
                border: 1px solid var(--gray-300);
                border-radius: var(--border-radius);
                resize: vertical;
                font-family: inherit;
            }

            .reject-form textarea:focus {
                outline: none;
                border-color: var(--danger);
                box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
            }

            .required-field {
                color: var(--danger);
                font-weight: 500;
            }

            @media (max-width: 768px) {
                .main-content {
                    padding: 1rem;
                }
                .header {
                    padding: 1.5rem 1rem;
                }
                .header h1 {
                    font-size: 1.5rem;
                }
                .content {
                    padding: 1rem;
                }
                .section-body {
                    padding: 1rem;
                }
                .info-grid {
                    grid-template-columns: 1fr;
                    gap: 1rem;
                }
                .form-footer {
                    padding: 1.5rem 1rem;
                    flex-direction: column;
                }
                .btn {
                    width: 100%;
                }
                th, td {
                    padding: 0.75rem 0.5rem;
                    font-size: 0.75rem;
                }
            }
        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <div class="container">
                    <div class="header">
                        <h1><i class="fas fa-shipping-fast"></i> Xử lý Xuất Kho</h1>
                        <p>Xác nhận hoặc từ chối yêu cầu xuất kho từ hệ thống</p>
                    </div>

                    <div class="content">
                        <div class="breadcrumb">
                            <a href="${pageContext.request.contextPath}/Admin.jsp"><i class="fas fa-home"></i> Trang chủ</a>
                            <i class="fas fa-chevron-right"></i>
                            <a href="${pageContext.request.contextPath}/exportList">Danh sách yêu cầu xuất</a>
                            <i class="fas fa-chevron-right"></i>
                            <span>Xử lý xuất kho</span>
                        </div>

                        <!-- Error/Success Messages -->
                        <c:if test="${param.error != null}">
                            <div class="alert alert-error">
                                <i class="fas fa-exclamation-circle"></i>
                                <div>
                                    <c:choose>
                                        <c:when test="${param.error == 'missing_required_fields'}">
                                            <strong>Lỗi:</strong> Vui lòng điền đầy đủ thông tin bắt buộc!
                                        </c:when>
                                        <c:when test="${param.error == 'recipient_required'}">
                                            <strong>Lỗi:</strong> Vui lòng nhập thông tin người nhận!
                                        </c:when>
                                        <c:when test="${param.error == 'insufficient_inventory'}">
                                            <strong>Lỗi:</strong> Không đủ tồn kho cho sản phẩm ${param.product}!
                                        </c:when>
                                        <c:when test="${param.error == 'export_failed'}">
                                            <strong>Lỗi:</strong> Xuất kho thất bại. Không đủ tồn kho cho sản phẩm. Vui lòng thử lại!
                                        </c:when>
                                        <c:when test="${param.error == 'reject_reason_required'}">
                                            <strong>Lỗi:</strong> Vui lòng nhập lý do từ chối!
                                        </c:when>
                                        <c:when test="${param.error == 'reject_failed'}">
                                            <strong>Lỗi:</strong> Từ chối thất bại. Vui lòng thử lại!
                                        </c:when>
                                        <c:when test="${param.error == 'order_not_processable'}">
                                            <strong>Lỗi:</strong> Đơn hàng không thể xử lý hoặc đã được xử lý!
                                        </c:when>
                                        <c:when test="${param.error == 'no_items_to_export'}">
                                            <strong>Lỗi:</strong> Không có sản phẩm nào để xuất kho!
                                        </c:when>
                                        <c:otherwise>
                                            <strong>Lỗi:</strong> Có lỗi xảy ra trong quá trình xử lý!
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:if>

                        <div class="alert alert-info">
                            <i class="fas fa-info-circle"></i>
                            <div>
                                <strong>Lưu ý:</strong> Hệ thống sẽ tự động xuất đúng số lượng yêu cầu cho tất cả sản phẩm. 
                                Tồn kho sẽ được cập nhật và đơn hàng chuyển trạng thái thành hoàn thành.
                            </div>
                        </div>

                        <form action="${pageContext.request.contextPath}/export" method="post" id="exportForm">
                            <input type="hidden" name="id" value="${exportRequest.id}">

                            <!-- Thông tin đơn xuất kho -->
                            <div class="section">
                                <div class="section-header">
                                    <div class="icon"><i class="fas fa-file-export"></i></div>
                                    <h3>Thông tin đơn xuất kho</h3>
                                    <span class="status-badge status-approved">
                                        <i class="fas fa-check-circle"></i> Đã duyệt
                                    </span>
                                </div>
                                <div class="section-body">
                                    <div class="info-grid">
                                        <div class="info-item">
                                            <label class="info-label">Mã đơn xuất</label>
                                            <div class="info-value">
                                                <i class="fas fa-hashtag"></i> ${exportRequest.id}
                                            </div>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Người yêu cầu</label>
                                            <div class="info-value">
                                                <i class="fas fa-user"></i> ${exportRequest.requesterDisplayName}
                                            </div>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Ngày yêu cầu</label>
                                            <div class="info-value">
                                                <i class="fas fa-calendar"></i> ${exportRequest.dayRequest}
                                            </div>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Người duyệt</label>
                                            <div class="info-value">
                                                <i class="fas fa-user-check"></i> ${exportRequest.approveBy != null ? exportRequest.approveBy : 'Chưa có'}
                                            </div>
                                        </div>
                                        <div class="info-item" style="grid-column: 1 / -1;">
                                            <label class="info-label">Lý do xuất kho</label>
                                            <div class="info-value">
                                                <i class="fas fa-comment"></i> ${exportRequest.reason != null ? exportRequest.reason : 'Không có ghi chú'}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Danh sách vật tư xuất kho -->
                            <div class="section">
                                <div class="section-header">
                                    <div class="icon"><i class="fas fa-boxes"></i></div>
                                    <h3>Danh sách vật tư xuất kho</h3>
                                </div>
                                <div class="section-body">
                                    <div class="alert alert-warning">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <div>
                                            <strong>Thông tin:</strong> Hệ thống sẽ tự động xuất đúng số lượng yêu cầu cho tất cả sản phẩm. 
                                            Vui lòng kiểm tra thông tin trước khi xác nhận.
                                        </div>
                                    </div>
                                    <div class="table-container">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th><i class="fas fa-barcode"></i> Mã VT</th>
                                                    <th><i class="fas fa-tag"></i> Tên VT</th>
                                                    <th><i class="fas fa-ruler"></i> Đơn vị</th>
                                                    <th><i class="fas fa-minus"></i> Số lượng xuất</th>
                                                    <th><i class="fas fa-sticky-note"></i> Ghi chú</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${itemList}" varStatus="status">
                                                    <tr>
                                                        <td>${item.productCode != null ? item.productCode : 'N/A'}</td>
                                                        <td><strong>${item.productName}</strong></td>
                                                        <td>${item.unit != null ? item.unit : 'N/A'}</td>
                                                        <td style="text-align: center;">
                                                            <div class="quantity-display">
                                                                <i class="fas fa-box"></i> ${item.quantity}
                                                            </div>
                                                            <!-- Hidden input để gửi số lượng -->
                                                            <input type="hidden" name="export_quantity_${item.id}" value="${item.quantity}">
                                                        </td>
                                                        <td>${item.note != null ? item.note : '-'}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <!-- Thông tin xuất kho -->
                            <div class="section">
                                <div class="section-header">
                                    <div class="icon"><i class="fas fa-shipping-fast"></i></div>
                                    <h3>Thông tin xuất kho</h3>
                                </div>
                                <div class="section-body">
                                    <div class="info-grid">
                                        <div class="info-item">
                                            <label class="info-label">Ngày xuất kho <span class="required-field">*</span></label>
                                            <input type="date" name="exportDate" class="form-input editable required" 
                                                   value="${currentDate}" required>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Người nhận <span class="required-field">*</span></label>
                                            <input type="text" name="recipient" class="form-input editable required" 
                                                   placeholder="Nhập tên người nhận hàng" 
                                                   value="${exportRequest.recipient != null ? exportRequest.recipient : ''}" 
                                                   required>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Người xử lý</label>
                                            <div class="info-value">
                                                <i class="fas fa-user"></i> ${sessionScope.user.fullname != null ? sessionScope.user.fullname : sessionScope.user.username}
                                            </div>
                                        </div>
                                        <div class="info-item" style="grid-column: 1 / -1;">
                                            <label class="info-label">Ghi chú bổ sung</label>
                                            <input type="text" name="additionalNote" class="form-input editable" 
                                                   placeholder="Nhập ghi chú thêm (nếu có)">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-footer">
                                <button type="button" onclick="confirmExport()" class="btn btn-danger">
                                    <i class="fas fa-check-circle"></i> Xác nhận xuất kho
                                </button>
                                <button type="button" onclick="rejectExport()" class="btn btn-secondary">
                                    <i class="fas fa-times-circle"></i> Từ chối xuất kho
                                </button>
                                <a href="${pageContext.request.contextPath}/exportList" class="btn btn-primary">
                                    <i class="fas fa-arrow-left"></i> Quay lại danh sách
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal xác nhận -->
        <div id="confirmModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title" id="modalTitle">Xác nhận hành động</h3>
                    <button type="button" class="modal-close" onclick="closeModal()">&times;</button>
                </div>
                <div class="modal-body">
                    <div style="text-align: center; padding: 1rem;">
                        <div id="modalIcon" style="font-size: 3rem; margin-bottom: 1rem;"></div>
                        <p id="modalMessage"></p>
                        <div id="rejectReasonContainer" style="display: none; margin-top: 1rem;">
                            <div class="reject-form">
                                <label class="info-label">Lý do từ chối *</label>
                                <textarea id="rejectReason" placeholder="Nhập lý do từ chối yêu cầu xuất kho..." required></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">Hủy bỏ</button>
                    <button type="button" id="confirmButton" class="btn">Xác nhận</button>
                </div>
            </div>
        </div>

        <script>
            function confirmExport() {
                const form = document.getElementById('exportForm');
                const exportDate = form.exportDate.value;
                const recipient = form.recipient.value.trim();

                if (!exportDate) {
                    alert('Vui lòng chọn ngày xuất kho!');
                    form.exportDate.focus();
                    return;
                }

                if (!recipient) {
                    alert('Vui lòng nhập tên người nhận!');
                    form.recipient.focus();
                    return;
                }

                document.getElementById('modalTitle').textContent = 'Xác nhận xuất kho';
                document.getElementById('modalIcon').innerHTML = '<i class="fas fa-check-circle" style="color: var(--danger);"></i>';
                document.getElementById('modalMessage').innerHTML =
                        'Bạn có chắc chắn muốn xuất kho với số lượng đã hiển thị?<br>' +
                        '<strong>Người nhận:</strong> ' + recipient + '<br>' +
                        '<strong>Ngày xuất:</strong> ' + exportDate + '<br><br>' +
                        'Đơn hàng sẽ được chuyển thành trạng thái hoàn thành và tồn kho sẽ được cập nhật.';
                document.getElementById('rejectReasonContainer').style.display = 'none';

                const confirmBtn = document.getElementById('confirmButton');
                confirmBtn.className = 'btn btn-danger';
                confirmBtn.innerHTML = '<i class="fas fa-check-circle"></i> Xác nhận xuất kho';
                confirmBtn.onclick = () => submitForm('confirm');

                document.getElementById('confirmModal').style.display = 'block';
            }

            function rejectExport() {
                document.getElementById('modalTitle').textContent = 'Từ chối xuất kho';
                document.getElementById('modalIcon').innerHTML = '<i class="fas fa-times-circle" style="color: var(--secondary);"></i>';
                document.getElementById('modalMessage').textContent = 'Bạn có chắc chắn muốn từ chối yêu cầu xuất kho này?';
                document.getElementById('rejectReasonContainer').style.display = 'block';

                const confirmBtn = document.getElementById('confirmButton');
                confirmBtn.className = 'btn btn-secondary';
                confirmBtn.innerHTML = '<i class="fas fa-times-circle"></i> Từ chối xuất kho';
                confirmBtn.onclick = () => submitForm('reject');

                document.getElementById('confirmModal').style.display = 'block';
            }

            function submitForm(action) {
                const form = document.getElementById('exportForm');

                if (action === 'confirm') {
                    const exportDate = form.exportDate.value;
                    const recipient = form.recipient.value.trim();

                    if (!exportDate) {
                        alert('Vui lòng chọn ngày xuất kho!');
                        closeModal();
                        form.exportDate.focus();
                        return;
                    }

                    if (!recipient) {
                        alert('Vui lòng nhập tên người nhận!');
                        closeModal();
                        form.recipient.focus();
                        return;
                    }
                }

                if (action === 'reject') {
                    const rejectReason = document.getElementById('rejectReason').value.trim();
                    if (!rejectReason) {
                        alert('Vui lòng nhập lý do từ chối!');
                        return;
                    }

                    const reasonInput = document.createElement('input');
                    reasonInput.type = 'hidden';
                    reasonInput.name = 'rejectReason';
                    reasonInput.value = rejectReason;
                    form.appendChild(reasonInput);
                }

                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = action;
                form.appendChild(actionInput);

                const confirmBtn = document.getElementById('confirmButton');
                confirmBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
                confirmBtn.disabled = true;

                form.submit();
            }

            function closeModal() {
                document.getElementById('confirmModal').style.display = 'none';
                document.getElementById('rejectReason').value = '';
            }

            // Event listeners
            document.addEventListener('DOMContentLoaded', function () {
                // Validate required fields on input
                const requiredFields = document.querySelectorAll('.form-input.required');
                requiredFields.forEach(field => {
                    field.addEventListener('input', function () {
                        if (this.value.trim()) {
                            this.style.borderColor = 'var(--success)';
                        } else {
                            this.style.borderColor = 'var(--danger)';
                        }
                    });
                });

                window.onclick = function (event) {
                    const modal = document.getElementById('confirmModal');
                    if (event.target === modal) {
                        closeModal();
                    }
                };

                document.addEventListener('keydown', function (e) {
                    if (e.ctrlKey && e.key === 'Enter') {
                        e.preventDefault();
                        confirmExport();
                    }
                    if (e.key === 'Escape') {
                        closeModal();
                    }
                });

                // Auto-dismiss error alerts after 10 seconds
                const alerts = document.querySelectorAll('.alert-error');
                alerts.forEach(alert => {
                    setTimeout(() => {
                        alert.style.opacity = '0';
                        alert.style.transform = 'translateY(-20px)';
                        alert.style.transition = 'all 0.5s ease';
                        setTimeout(() => {
                            alert.remove();
                        }, 500);
                    }, 10000);
                });

                // Debug log for requester name
                console.log('🔍 DEBUG - JSP loaded with requester name:', '${exportRequest.requesterDisplayName}');
            });
        </script>
    </body>
</html>
