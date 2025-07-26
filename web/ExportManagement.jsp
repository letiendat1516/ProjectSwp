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
                    background: #f8fafc;
                    font-family: 'Inter', sans-serif;
                    min-height: 100vh;
                    color: var(--gray-800);
                    font-size: 15px;
                    line-height: 1.6;
                }

                .layout-container {
                    display: flex;
                    min-height: 100vh;
                    width: 100%;
                }

                .main-content {
                    flex: 1;
                    padding: 20px;
                    background: #f8fafc;
                    width: 100%;
                    min-width: 0;
                }

                .header {
                    background: linear-gradient(135deg, var(--danger) 0%, var(--danger-dark) 100%);
                    color: white;
                    padding: 2rem;
                    text-align: center;
                    border-radius: var(--border-radius);
                    margin-bottom: 2rem;
                    box-shadow: var(--shadow-lg);
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
                    width: 100%;
                }

                .section {
                    background: white;
                    border-radius: var(--border-radius);
                    box-shadow: var(--shadow-md);
                    margin-bottom: 2rem;
                    overflow: hidden;
                    border: 1px solid var(--gray-200);
                    width: 100%;
                }

                .section-header {
                    background: var(--gray-50);
                    padding: 1.5rem 2rem;
                    border-bottom: 1px solid var(--gray-200);
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    gap: 1rem;
                    flex-wrap: wrap;
                }

                .section-header-left {
                    display: flex;
                    align-items: center;
                    gap: 1rem;
                }

                .section-header h3 {
                    font-size: 1.25rem;
                    font-weight: 600;
                    color: var(--gray-800);
                    margin: 0;
                }

                .section-header .icon {
                    width: 2.5rem;
                    height: 2.5rem;
                    background: var(--danger);
                    color: white;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 1rem;
                    flex-shrink: 0;
                }

                .section-body {
                    padding: 2rem;
                    background: white;
                    width: 100%;
                }

                .info-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                    gap: 1.5rem;
                    width: 100%;
                }

                .info-item {
                    display: flex;
                    flex-direction: column;
                    gap: 0.75rem;
                }

                .info-label {
                    font-size: 0.875rem;
                    font-weight: 600;
                    color: var(--gray-600);
                    text-transform: uppercase;
                    letter-spacing: 0.05em;
                }

                .info-value {
                    display: flex;
                    align-items: center;
                    padding: 1rem;
                    background: var(--gray-50);
                    border: 1px solid var(--gray-200);
                    border-radius: var(--border-radius);
                    font-size: 1rem;
                    color: var(--gray-800);
                    width: 100%;
                    min-height: 3rem;
                    font-weight: 500;
                }

                .info-value i {
                    margin-right: 0.75rem;
                    color: var(--danger);
                    font-size: 1.1rem;
                }

                .table-container {
                    overflow-x: auto;
                    border-radius: var(--border-radius);
                    border: 1px solid var(--gray-200);
                    background: white;
                    width: 100%;
                    box-shadow: var(--shadow);
                }

                table {
                    width: 100%;
                    min-width: 1200px;
                    border-collapse: collapse;
                    background: white;
                    font-size: 0.95rem;
                }

                th, td {
                    padding: 1rem 0.75rem;
                    text-align: left;
                    border-bottom: 1px solid var(--gray-200);
                    font-size: 0.95rem;
                    white-space: nowrap;
                    vertical-align: middle;
                }

                th {
                    background: linear-gradient(135deg, var(--gray-100) 0%, var(--gray-50) 100%);
                    font-weight: 700;
                    color: var(--gray-700);
                    text-transform: uppercase;
                    letter-spacing: 0.05em;
                    font-size: 0.85rem;
                    position: sticky;
                    top: 0;
                    z-index: 10;
                    height: 3.5rem;
                    border-bottom: 2px solid var(--gray-300);
                }

                th i {
                    margin-right: 0.5rem;
                    color: var(--danger);
                }

                td {
                    color: var(--gray-800);
                    background: white;
                    height: auto;
                    min-height: 3rem;
                }

                tr:last-child td {
                    border-bottom: none;
                }

                tr:hover {
                    background: var(--gray-50);
                }

                .form-input {
                    width: 100%;
                    padding: 1rem;
                    border: 1px solid var(--gray-300);
                    border-radius: var(--border-radius);
                    font-size: 1rem;
                    transition: all 0.2s ease;
                    background: white;
                    height: 3rem;
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

                .quantity-input {
                    width: 100px;
                    text-align: center;
                    border: 2px solid var(--danger);
                    background: #fef2f2;
                    font-weight: 700;
                    font-size: 1rem;
                    padding: 0.75rem;
                    height: 2.5rem;
                    border-radius: var(--border-radius);
                }

                .quantity-input:focus {
                    border-color: var(--danger-dark);
                    box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.15);
                }

                .quantity-input.invalid {
                    border-color: var(--danger) !important;
                    background: #fef2f2 !important;
                    animation: shake 0.4s ease-in-out;
                    box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.25) !important;
                }

                @keyframes shake {
                    0%, 100% {
                        transform: translateX(0);
                    }
                    25% {
                        transform: translateX(-5px);
                    }
                    75% {
                        transform: translateX(5px);
                    }
                }

                .quantity-input::placeholder {
                    color: var(--gray-400);
                    font-style: italic;
                    font-weight: normal;
                }

                .quantity-input::-webkit-outer-spin-button,
                .quantity-input::-webkit-inner-spin-button {
                    -webkit-appearance: none;
                    margin: 0;
                }

                .quantity-input[type=number] {
                    -moz-appearance: textfield;
                }

                .btn {
                    display: inline-flex;
                    align-items: center;
                    gap: 0.75rem;
                    padding: 1rem 2rem;
                    font-size: 1rem;
                    font-weight: 600;
                    border: none;
                    border-radius: var(--border-radius);
                    cursor: pointer;
                    transition: all 0.2s ease;
                    text-decoration: none;
                    justify-content: center;
                    min-width: 180px;
                    height: 3rem;
                }

                .btn:hover {
                    transform: translateY(-2px);
                    box-shadow: var(--shadow-lg);
                }

                .btn-primary {
                    background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
                    color: white;
                }

                .btn-primary:hover {
                    background: linear-gradient(135deg, var(--primary-dark) 0%, #1e40af 100%);
                }

                .btn-warning {
                    background: linear-gradient(135deg, var(--warning) 0%, var(--warning-dark) 100%);
                    color: white;
                }

                .btn-warning:hover {
                    background: linear-gradient(135deg, var(--warning-dark) 0%, #b45309 100%);
                }

                .btn-danger {
                    background: linear-gradient(135deg, var(--danger) 0%, var(--danger-dark) 100%);
                    color: white;
                }

                .btn-danger:hover {
                    background: linear-gradient(135deg, var(--danger-dark) 0%, #b91c1c 100%);
                }

                .btn-secondary {
                    background: linear-gradient(135deg, var(--gray-500) 0%, var(--gray-600) 100%);
                    color: white;
                }

                .btn-secondary:hover {
                    background: linear-gradient(135deg, var(--gray-600) 0%, var(--gray-700) 100%);
                }

                .form-footer {
                    background: white;
                    padding: 2rem;
                    border-top: 1px solid var(--gray-200);
                    display: flex;
                    gap: 1.5rem;
                    justify-content: center;
                    flex-wrap: wrap;
                    width: 100%;
                    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
                    border-radius: 0 0 var(--border-radius) var(--border-radius);
                }

                .status-badge {
                    display: inline-flex;
                    align-items: center;
                    gap: 0.5rem;
                    padding: 0.5rem 1rem;
                    font-size: 0.875rem;
                    font-weight: 600;
                    border-radius: 9999px;
                    text-transform: uppercase;
                    letter-spacing: 0.05em;
                }

                .status-approved {
                    background: #d1fae5;
                    color: #065f46;
                    border: 1px solid #10b981;
                }

                .status-partial {
                    background: #fef3c7;
                    color: #92400e;
                    border: 1px solid #f59e0b;
                }

                .status-completed {
                    background: #dbeafe;
                    color: #1e40af;
                    border: 1px solid #2563eb;
                }

                .alert {
                    padding: 1.5rem;
                    border-radius: var(--border-radius);
                    margin-bottom: 2rem;
                    display: flex;
                    align-items: flex-start;
                    gap: 1rem;
                    border-left: 4px solid;
                    width: 100%;
                    font-size: 1rem;
                    box-shadow: var(--shadow);
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

                .alert-danger {
                    background: #fef2f2;
                    border-left-color: var(--danger);
                    color: #991b1b;
                }

                .alert-content {
                    flex: 1;
                }

                .alert ul {
                    margin: 0.75rem 0 0 1.5rem;
                    padding: 0;
                }

                .alert li {
                    margin-bottom: 0.5rem;
                }

                .breadcrumb {
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    margin-bottom: 2rem;
                    font-size: 1rem;
                    color: var(--gray-600);
                    flex-wrap: wrap;
                }

                .breadcrumb a {
                    color: var(--primary);
                    text-decoration: none;
                    font-weight: 500;
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
                    background: rgba(0, 0, 0, 0.6);
                    backdrop-filter: blur(5px);
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
                    padding: 1.5rem 2rem;
                    border-bottom: 1px solid var(--gray-200);
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    background: white;
                }

                .modal-title {
                    font-size: 1.5rem;
                    font-weight: 700;
                    color: var(--gray-800);
                }

                .modal-close {
                    background: none;
                    border: none;
                    font-size: 1.5rem;
                    cursor: pointer;
                    color: var(--gray-400);
                    padding: 0.5rem;
                    border-radius: 50%;
                    width: 2.5rem;
                    height: 2.5rem;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                }

                .modal-close:hover {
                    background: var(--gray-100);
                    color: var(--gray-600);
                }

                .modal-body {
                    padding: 2rem;
                    background: white;
                }

                .modal-footer {
                    padding: 1.5rem 2rem;
                    border-top: 1px solid var(--gray-200);
                    display: flex;
                    gap: 1rem;
                    justify-content: flex-end;
                    background: white;
                }

                .reject-form textarea {
                    width: 100%;
                    min-height: 120px;
                    padding: 1rem;
                    border: 1px solid var(--gray-300);
                    border-radius: var(--border-radius);
                    resize: vertical;
                    font-family: inherit;
                    font-size: 1rem;
                }

                .reject-form textarea:focus {
                    outline: none;
                    border-color: var(--danger);
                    box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
                }

                .progress-bar {
                    width: 100%;
                    height: 8px;
                    background: var(--gray-200);
                    border-radius: 4px;
                    overflow: hidden;
                    margin-top: 0.5rem;
                }

                .progress-fill {
                    height: 100%;
                    background: linear-gradient(90deg, var(--danger) 0%, var(--danger-dark) 100%);
                    transition: width 0.3s ease;
                }

                .progress-fill.partial {
                    background: linear-gradient(90deg, var(--warning) 0%, var(--warning-dark) 100%);
                }

                .quantity-status {
                    display: flex;
                    align-items: center;
                    gap: 0.5rem;
                    font-size: 0.875rem;
                    margin-top: 0.5rem;
                    font-weight: 600;
                }

                .quantity-status.completed {
                    color: var(--success-dark);
                }

                .quantity-status.partial {
                    color: var(--warning-dark);
                }

                .quantity-status.pending {
                    color: var(--gray-500);
                }

                .integer-only-note {
                    font-size: 0.8rem;
                    color: var(--danger-dark);
                    font-style: italic;
                    margin-top: 0.5rem;
                    display: flex;
                    align-items: center;
                    gap: 0.25rem;
                    font-weight: 500;
                }

                .validation-error {
                    background: #fef2f2;
                    border: 1px solid var(--danger);
                    border-radius: var(--border-radius);
                    padding: 1.5rem;
                    margin-bottom: 2rem;
                    width: 100%;
                    box-shadow: var(--shadow);
                }

                .validation-error h4 {
                    color: var(--danger-dark);
                    margin-bottom: 1rem;
                    font-size: 1rem;
                    font-weight: 700;
                }

                .validation-error ul {
                    margin: 0;
                    padding-left: 2rem;
                    color: var(--danger-dark);
                }

                .validation-error li {
                    margin-bottom: 0.5rem;
                    font-size: 1rem;
                }

                /* Responsive Design */
                @media (max-width: 1366px) {
                    .main-content {
                        padding: 15px;
                    }

                    .info-grid {
                        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                    }

                    table {
                        min-width: 1100px;
                    }
                }

                @media (max-width: 1024px) {
                    .main-content {
                        padding: 12px;
                    }

                    .header {
                        padding: 1.5rem;
                    }

                    .section-body {
                        padding: 1.5rem;
                    }

                    .info-grid {
                        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                        gap: 1rem;
                    }

                    table {
                        min-width: 1000px;
                    }

                    th, td {
                        padding: 0.75rem 0.5rem;
                    }
                }

                @media (max-width: 768px) {
                    .form-footer {
                        flex-direction: column;
                    }

                    .btn {
                        width: 100%;
                    }

                    .info-grid {
                        grid-template-columns: 1fr;
                    }

                    table {
                        min-width: 900px;
                    }
                }

                /* Scrollbar styling */
                .table-container::-webkit-scrollbar {
                    height: 10px;
                    width: 10px;
                }

                .table-container::-webkit-scrollbar-track {
                    background: var(--gray-100);
                    border-radius: 5px;
                }

                .table-container::-webkit-scrollbar-thumb {
                    background: var(--gray-400);
                    border-radius: 5px;
                }

                .table-container::-webkit-scrollbar-thumb:hover {
                    background: var(--gray-500);
                }
            </style>
        </head>
        <body>
            <div class="layout-container">
                <jsp:include page="/include/sidebar.jsp" />
                <div class="main-content">
                    <div class="content">
                        <div class="breadcrumb">
                            <a href="${pageContext.request.contextPath}/Admin.jsp"><i class="fas fa-home"></i> Trang chủ</a>
                            <i class="fas fa-chevron-right"></i>
                            <a href="${pageContext.request.contextPath}/exportList">Danh sách yêu cầu xuất</a>
                            <i class="fas fa-chevron-right"></i>
                            <span>Xử lý xuất kho</span>
                        </div>

                        <!-- Hiển thị lỗi validation nếu có -->
                        <c:if test="${not empty validationErrors}">
                            <div class="validation-error">
                                <h4><i class="fas fa-exclamation-triangle"></i> ${errorMessage != null ? errorMessage : 'Có lỗi trong dữ liệu nhập:'}</h4>
                                <ul>
                                    <c:forEach var="error" items="${validationErrors}">
                                        <li>${error}</li>
                                        </c:forEach>
                                </ul>
                            </div>
                        </c:if>

                        <c:if test="${exportRequest.status == 'partial_exported'}">
                            <div class="alert alert-warning">
                                <i class="fas fa-exclamation-triangle"></i>
                                <div class="alert-content">
                                    <strong>Xuất kho từng phần:</strong> Đơn hàng này đã được xuất kho một phần. 
                                    Vui lòng kiểm tra số lượng còn lại và tiếp tục xuất kho.
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${exportRequest.status == 'approved'}">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i>
                                <div class="alert-content">
                                    <strong>Lưu ý:</strong> Bạn có thể xuất kho từng phần hoặc xuất toàn bộ số lượng. 
                                    Hệ thống sẽ tự động theo dõi số lượng còn lại. <strong>Chỉ được xuất số nguyên dương.</strong>
                                </div>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/exportList" method="post" id="exportForm">
                            <input type="hidden" name="id" value="${exportRequest.id}">

                            <!-- Thông tin đơn xuất kho -->
                            <div class="section">
                                <div class="section-header">
                                    <div class="section-header-left">
                                        <div class="icon"><i class="fas fa-file-export"></i></div>
                                        <h3>Thông tin đơn xuất kho</h3>
                                    </div>
                                    <c:choose>
                                        <c:when test="${exportRequest.status == 'approved'}">
                                            <span class="status-badge status-approved">
                                                <i class="fas fa-check-circle"></i> Đã duyệt
                                            </span>
                                        </c:when>
                                        <c:when test="${exportRequest.status == 'partial_exported'}">
                                            <span class="status-badge status-partial">
                                                <i class="fas fa-clock"></i> Xuất từng phần
                                            </span>
                                        </c:when>
                                        <c:when test="${exportRequest.status == 'completed'}">
                                            <span class="status-badge status-completed">
                                                <i class="fas fa-check-double"></i> Hoàn thành
                                            </span>
                                        </c:when>
                                    </c:choose>
                                </div>
                                <div class="section-body">
                                    <div class="info-grid">
                                        <div class="info-item">
                                            <label class="info-label">Mã đơn xuất</label>
                                            <div class="info-value"><i class="fas fa-hashtag"></i>${exportRequest.id}</div>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Người yêu cầu</label>
                                            <div class="info-value"><i class="fas fa-user"></i>${exportRequest.userId}</div>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Ngày yêu cầu</label>
                                            <div class="info-value"><i class="fas fa-calendar"></i>${exportRequest.dayRequest}</div>
                                        </div>
                                        <div class="info-item">
                                            <label class="info-label">Vai trò</label>
                                            <div class="info-value"><i class="fas fa-user-tag"></i>${exportRequest.role != null ? exportRequest.role : 'Chưa có'}</div>
                                        </div>
                                        <div class="info-item" style="grid-column: 1 / -1;">
                                            <label class="info-label">Lý do xuất kho</label>
                                            <div class="info-value"><i class="fas fa-comment"></i>${exportRequest.reason != null ? exportRequest.reason : 'Không có ghi chú'}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Danh sách vật tư với xuất từng phần -->
                            <div class="section">
                                <div class="section-header">
                                    <div class="section-header-left">
                                        <div class="icon"><i class="fas fa-boxes"></i></div>
                                        <h3>Danh sách vật tư xuất kho</h3>
                                    </div>
                                </div>
                                <div class="section-body">
                                    <div class="alert alert-danger">
                                        <i class="fas fa-exclamation-circle"></i>
                                        <div class="alert-content">
                                            <strong>Lưu ý quan trọng:</strong> Chỉ được xuất số nguyên dương (1, 2, 3...). 
                                            Không được xuất số thập phân (1.5, 2.3...) hoặc số âm.
                                            <br><strong>Nhấp đúp vào ô nhập để tự động điền số lượng tối đa.</strong>
                                        </div>
                                    </div>
                                    <div class="table-container">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th><i class="fas fa-barcode"></i> Mã Vật Tư</th>
                                                    <th><i class="fas fa-tag"></i> Tên Vật Tư</th>
                                                    <th><i class="fas fa-ruler"></i> Đơn Vị</th>
                                                    <th><i class="fas fa-list-ol"></i> SL Yêu Cầu</th>
                                                    <th><i class="fas fa-check"></i> Đã Xuất</th>
                                                    <th><i class="fas fa-clock"></i> Còn Lại</th>
                                                    <th><i class="fas fa-minus"></i> Xuất Lần Này</th>
                                                    <th><i class="fas fa-sticky-note"></i> Ghi Chú</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${itemList}" varStatus="status">
                                                    <tr>
                                                        <td style="font-weight: 600; color: var(--primary);">${item.productCode != null ? item.productCode : 'N/A'}</td>
                                                        <td style="font-weight: 600;">${item.productName}</td>
                                                        <td style="text-align: center;">${item.unit != null ? item.unit : 'N/A'}</td>
                                                        <td style="text-align: center; font-weight: 700; font-size: 1.1rem; color: var(--info-dark);">${item.quantityRequested}</td>
                                                        <td style="text-align: center;">
                                                            <span style="color: var(--danger-dark); font-weight: 700; font-size: 1.1rem;">${item.quantityExported}</span>
                                                            <div class="progress-bar">
                                                                <div class="progress-fill ${item.quantityExported >= item.quantityRequested ? '' : 'partial'}" 
                                                                     style="width: ${(item.quantityExported / item.quantityRequested) * 100}%"></div>
                                                            </div>
                                                        </td>
                                                        <td style="text-align: center;">
                                                            <span style="color: var(--warning-dark); font-weight: 700; font-size: 1.1rem;">${item.quantityPending}</span>
                                                            <div class="quantity-status ${item.quantityPending == 0 ? 'completed' : 'partial'}">
                                                                <i class="fas ${item.quantityPending == 0 ? 'fa-check-circle' : 'fa-clock'}"></i>
                                                                ${item.quantityPending == 0 ? 'Hoàn thành' : 'Chờ xuất'}
                                                            </div>
                                                        </td>
                                                        <td style="text-align: center;">
                                                            <c:if test="${item.quantityPending > 0}">
                                                                <input type="text" 
                                                                       name="export_quantity_${item.id}" 
                                                                       class="quantity-input" 
                                                                       data-max="${item.quantityPending}" 
                                                                       placeholder="0"
                                                                       title="Tối đa: ${item.quantityPending} (chỉ số nguyên)"
                                                                       autocomplete="off">
                                                                <div class="integer-only-note">
                                                                    <i class="fas fa-info-circle"></i>
                                                                    Chỉ số nguyên (1-${item.quantityPending})
                                                                </div>
                                                            </c:if>
                                                            <c:if test="${item.quantityPending == 0}">
                                                                <span style="color: var(--success-dark); font-weight: 700; font-size: 1.1rem;">
                                                                    <i class="fas fa-check-circle"></i> Đã đủ
                                                                </span>
                                                            </c:if>
                                                        </td>
                                                        <td style="font-style: italic; color: var(--gray-600);">${item.note != null ? item.note : ''}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <!-- Lịch sử xuất kho (nếu có) -->
                            <c:if test="${not empty exportHistory}">
                                <div class="section">
                                    <div class="section-header">
                                        <div class="section-header-left">
                                            <div class="icon"><i class="fas fa-history"></i></div>
                                            <h3>Lịch sử xuất kho</h3>
                                        </div>
                                    </div>
                                    <div class="section-body">
                                        <div class="table-container">
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th><i class="fas fa-tag"></i> Tên Vật Tư</th>
                                                        <th><i class="fas fa-barcode"></i> Mã Vật Tư</th>
                                                        <th><i class="fas fa-minus"></i> SL Xuất</th>
                                                        <th><i class="fas fa-calendar"></i> Ngày Xuất</th>
                                                        <th><i class="fas fa-user"></i> Người Xử Lý</th>
                                                        <th><i class="fas fa-sticky-note"></i> Ghi Chú</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="history" items="${exportHistory}">
                                                        <tr>
                                                            <td style="font-weight: 600;">${history[0]}</td>
                                                            <td style="font-weight: 600; color: var(--primary);">${history[1]}</td>
                                                            <td style="text-align: center; font-weight: 700; color: var(--danger-dark); font-size: 1.1rem;">
                                                                ${history[2]}
                                                            </td>
                                                            <td style="font-weight: 500;">${history[3]}</td>
                                                            <td style="font-weight: 500; color: var(--info-dark);">${history[4]}</td>
                                                            <td style="font-style: italic; color: var(--gray-600);">${history[5] != null ? history[5] : ''}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <!-- Thông tin xuất kho -->
                            <c:if test="${exportRequest.status != 'completed'}">
                                <div class="section">
                                    <div class="section-header">
                                        <div class="section-header-left">
                                            <div class="icon"><i class="fas fa-shipping-fast"></i></div>
                                            <h3>Thông tin xuất kho</h3>
                                        </div>
                                    </div>
                                    <div class="section-body">
                                        <div class="info-grid">
                                            <div class="info-item">
                                                <label class="info-label">Ngày xuất kho *</label>
                                                <input type="date" name="exportDate" class="form-input editable" 
                                                       value="${currentDate}" required>
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
                            </c:if>

                            <div class="form-footer">
                                <c:if test="${exportRequest.status != 'completed'}">
                                    <button type="button" onclick="confirmExport()" class="btn btn-danger">
                                        <i class="fas fa-minus-circle"></i> Thực Hiện Xuất Kho
                                    </button>
                                    <button type="button" onclick="rejectExport()" class="btn btn-secondary">
                                        <i class="fas fa-times-circle"></i> Từ chối xuất kho
                                    </button>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/exportList" class="btn btn-primary">
                                    <i class="fas fa-arrow-left"></i> Quay lại danh sách
                                </a>
                            </div>
                        </form>
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
                            <div id="modalIcon" style="font-size: 4rem; margin-bottom: 1rem;"></div>
                            <p id="modalMessage" style="font-size: 1.1rem; line-height: 1.6;"></p>
                            <div id="rejectReasonContainer" style="display: none; margin-top: 1rem; text-align: left;">
                                <div class="reject-form">
                                    <label class="info-label">Lý do từ chối *</label>
                                    <textarea id="rejectReason" placeholder="Nhập lý do từ chối yêu cầu xuất kho..." required></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" onclick="closeModal()">
                            <i class="fas fa-times"></i> Hủy bỏ
                        </button>
                        <button type="button" id="confirmButton" class="btn">
                            <i class="fas fa-check"></i> Xác nhận
                        </button>
                    </div>
                </div>
            </div>

            <script>
                // JavaScript functions - giữ nguyên logic, tối ưu performance
                function validateIntegerInput(input) {
                    let value = input.value.trim();
                    const max = parseInt(input.getAttribute('data-max')) || 0;

                    input.classList.remove('invalid');
                    input.style.borderColor = 'var(--danger)';
                    input.style.background = '#fef2f2';

                    if (value === '') {
                        input.title = `Tối đa: ${max} (chỉ số nguyên)`;
                        return true;
                    }

                    if (!/^\d+$/.test(value)) {
                        input.classList.add('invalid');
                        input.title = 'Chỉ được nhập số nguyên dương (không có dấu thập phân)';
                        return false;
                    }

                    const numValue = parseInt(value);

                    if (numValue <= 0) {
                        input.classList.add('invalid');
                        input.title = 'Số lượng phải lớn hơn 0';
                        return false;
                    }

                    if (numValue > max) {
                        input.classList.add('invalid');
                        input.title = `Số lượng không được vượt quá ${max}`;
                        return false;
                    }

                    input.title = `Hợp lệ (tối đa: ${max})`;
                    return true;
                }

                function validateAllQuantities() {
                    let hasValidQuantity = false;
                    let hasInvalidInput = false;
                    let errorMessages = [];

                    const quantityInputs = document.querySelectorAll('.quantity-input');

                    quantityInputs.forEach(input => {
                        const value = input.value.trim();
                        if (value !== '') {
                            const isValid = validateIntegerInput(input);
                            if (!isValid) {
                                hasInvalidInput = true;
                                const productName = input.closest('tr').cells[1].textContent;
                                errorMessages.push(`${productName}: ${input.title}`);
                                                } else {
                                                    hasValidQuantity = true;
                                                }
                                            }
                                        });

                                        return {
                                            hasValid: hasValidQuantity,
                                            hasInvalid: hasInvalidInput,
                                            errors: errorMessages
                                        };
                                    }

                                    function confirmExport() {
                                        const form = document.getElementById('exportForm');
                                        const exportDate = form.exportDate.value;

                                        if (!exportDate) {
                                            alert('Vui lòng chọn ngày xuất kho!');
                                            form.exportDate.focus();
                                            return;
                                        }

                                        const validation = validateAllQuantities();

                                        if (validation.hasInvalid) {
                                            let errorMsg = 'Có lỗi trong dữ liệu nhập:\n\n';
                                            validation.errors.forEach(error => {
                                                errorMsg += '• ' + error + '\n';
                                            });
                                            errorMsg += '\nVui lòng chỉ nhập số nguyên dương (1, 2, 3...)';
                                            alert(errorMsg);
                                            return;
                                        }

                                        if (!validation.hasValid) {
                                            alert('Vui lòng nhập số lượng hợp lệ cho ít nhất một sản phẩm!');
                                            return;
                                        }

                                        document.getElementById('modalTitle').textContent = 'Xác nhận xuất kho từng phần';
                                        document.getElementById('modalIcon').innerHTML = '<i class="fas fa-minus-circle" style="color: var(--danger);"></i>';
                                        document.getElementById('modalMessage').textContent = 'Bạn có chắc chắn muốn xuất kho với số lượng đã chọn? Các sản phẩm chưa xuất đủ sẽ được chuyển vào danh sách chờ.';
                                        document.getElementById('rejectReasonContainer').style.display = 'none';

                                        const confirmBtn = document.getElementById('confirmButton');
                                        confirmBtn.className = 'btn btn-danger';
                                        confirmBtn.innerHTML = '<i class="fas fa-minus-circle"></i> Xác nhận xuất kho';
                                        confirmBtn.onclick = () => submitForm('confirm');

                                        document.getElementById('confirmModal').style.display = 'block';
                                    }

                                    function rejectExport() {
                                        document.getElementById('modalTitle').textContent = 'Từ chối xuất kho';
                                        document.getElementById('modalIcon').innerHTML = '<i class="fas fa-times-circle" style="color: var(--danger);"></i>';
                                        document.getElementById('modalMessage').textContent = 'Bạn có chắc chắn muốn từ chối yêu cầu xuất kho này?';
                                        document.getElementById('rejectReasonContainer').style.display = 'block';

                                        const confirmBtn = document.getElementById('confirmButton');
                                        confirmBtn.className = 'btn btn-danger';
                                        confirmBtn.innerHTML = '<i class="fas fa-times-circle"></i> Từ chối xuất kho';
                                        confirmBtn.onclick = () => submitForm('reject');

                                        document.getElementById('confirmModal').style.display = 'block';
                                    }

                                    function submitForm(action) {
                                        const form = document.getElementById('exportForm');

                                        if (action === 'reject') {
                                            const rejectReason = document.getElementById('rejectReason').value.trim();
                                            if (!rejectReason) {
                                                alert('Vui lòng nhập lý do từ chối!');
                                                document.getElementById('rejectReason').focus();
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
                                        const quantityInputs = document.querySelectorAll('.quantity-input');
                                        let debounceTimer;

                                        quantityInputs.forEach(input => {
                                            // Chỉ cho phép nhập số
                                            input.addEventListener('keypress', function (e) {
                                                const allowedKeys = ['Backspace', 'Delete', 'Tab', 'Enter', 'ArrowLeft', 'ArrowRight', 'Home', 'End'];
                                                if (allowedKeys.includes(e.key))
                                                    return;
                                                if (!/[0-9]/.test(e.key))
                                                    e.preventDefault();
                                            });

                                            // Xử lý paste
                                            input.addEventListener('paste', function (e) {
                                                e.preventDefault();
                                                let paste = (e.clipboardData || window.clipboardData).getData('text');
                                                paste = paste.replace(/[^0-9]/g, '');
                                                if (paste && parseInt(paste) > 0) {
                                                    this.value = paste;
                                                    validateIntegerInput(this);
                                                }
                                            });

                                            // Validate khi nhập
                                            input.addEventListener('input', function () {
                                                this.value = this.value.replace(/[^0-9]/g, '');
                                                clearTimeout(debounceTimer);
                                                debounceTimer = setTimeout(() => validateIntegerInput(this), 200);
                                            });

                                            // Double click để điền max
                                            input.addEventListener('dblclick', function () {
                                                const max = parseInt(this.getAttribute('data-max')) || 0;
                                                if (max > 0) {
                                                    this.value = max.toString();
                                                    validateIntegerInput(this);
                                                }
                                            });

                                            // Ngăn scroll wheel
                                            input.addEventListener('wheel', e => e.preventDefault());

                                            // Validate khi blur
                                            input.addEventListener('blur', function () {
                                                validateIntegerInput(this);
                                            });
                                        });

                                        // Modal events
                                        window.onclick = function (event) {
                                            const modal = document.getElementById('confirmModal');
                                            if (event.target === modal)
                                                closeModal();
                                        };

                                        // Keyboard shortcuts
                                        document.addEventListener('keydown', function (e) {
                                            if (e.ctrlKey && e.key === 'Enter') {
                                                e.preventDefault();
                                                confirmExport();
                                            }
                                            if (e.key === 'Escape')
                                                closeModal();
                                        });

                                        // Highlight pending items
                                        highlightPendingItems();
                                    });

                                    function highlightPendingItems() {
                                        const rows = document.querySelectorAll('tbody tr');
                                        rows.forEach(row => {
                                            const quantityInput = row.querySelector('.quantity-input');
                                            if (quantityInput) {
                                                row.style.backgroundColor = '#fef2f2';
                                                row.style.borderLeft = '4px solid var(--danger)';
                                            }
                                        });
                                    }
            </script>
        </body>
    </html>
