<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách yêu cầu nhập kho | Hệ thống quản lý kho</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                font-family: 'Inter', sans-serif;
            }

            :root {
                --primary: #2563eb;
                --primary-dark: #1d4ed8;
                --secondary: #64748b;
                --success: #10b981;
                --danger: #ef4444;
                --warning: #f59e0b;
                --info: #3b82f6;
                --light: #f9fafb;
                --dark: #1e293b;
                --gray-50: #f8fafc;
                --gray-100: #f1f5f9;
                --gray-200: #e2e8f0;
                --gray-300: #cbd5e1;
                --gray-400: #94a3b8;
                --gray-500: #64748b;
                --gray-600: #475569;
                --gray-700: #334155;
                --gray-800: #1e293b;
                --gray-900: #0f172a;
                --border-radius: 0.375rem;
                --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
                --shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
                --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
                --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            }

            body {
                background-color: var(--gray-100);
                color: var(--gray-800);
                line-height: 1.5;
                font-size: 0.875rem;
            }

            .container {
                max-width: 1200px;
                margin: 2rem auto;
                padding: 0 1rem;
            }

            .card {
                background-color: white;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow-md);
                overflow: hidden;
            }

            .card-header {
                background-color: white;
                padding: 1.25rem 1.5rem;
                border-bottom: 1px solid var(--gray-200);
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .card-body {
                padding: 1.5rem;
            }

            .card-footer {
                background-color: var(--gray-50);
                padding: 1rem 1.5rem;
                border-top: 1px solid var(--gray-200);
            }

            h1, h2, h3, h4, h5, h6 {
                color: var(--gray-900);
                font-weight: 600;
                line-height: 1.25;
                margin-bottom: 1rem;
            }

            h1 {
                font-size: 1.5rem;
            }

            h2 {
                font-size: 1.25rem;
            }

            p {
                margin-bottom: 1rem;
            }

            .app-header {
                background-color: white;
                box-shadow: var(--shadow);
                padding: 1rem 0;
                margin-bottom: 2rem;
            }

            .app-header-inner {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 1rem;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .brand {
                display: flex;
                align-items: center;
                gap: 0.75rem;
            }

            .brand-logo {
                width: 2.5rem;
                height: 2.5rem;
                background-color: var(--primary);
                border-radius: var(--border-radius);
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                font-weight: 700;
                font-size: 1.25rem;
            }

            .brand-text {
                font-size: 1.25rem;
                font-weight: 600;
                color: var(--gray-900);
            }

            .brand-text span {
                color: var(--primary);
            }

            .breadcrumbs {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                font-size: 0.875rem;
                margin-bottom: 1.5rem;
                color: var(--gray-500);
            }

            .breadcrumbs a {
                color: var(--gray-600);
                text-decoration: none;
            }

            .breadcrumbs a:hover {
                color: var(--primary);
            }

            .breadcrumbs .separator {
                color: var(--gray-400);
            }

            .breadcrumbs .current {
                color: var(--gray-800);
                font-weight: 500;
            }

            .alert {
                padding: 1rem 1.25rem;
                border-radius: var(--border-radius);
                margin-bottom: 1.5rem;
                display: flex;
                align-items: center;
                gap: 0.75rem;
            }

            .alert-icon {
                flex-shrink: 0;
                width: 1.5rem;
                height: 1.5rem;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 50%;
            }

            .alert-content {
                flex: 1;
            }

            .alert-title {
                font-weight: 600;
                margin-bottom: 0.25rem;
            }

            .alert-close {
                flex-shrink: 0;
                cursor: pointer;
                color: inherit;
                opacity: 0.7;
                transition: opacity 0.15s;
            }

            .alert-close:hover {
                opacity: 1;
            }

            .alert-success {
                background-color: #ecfdf5;
                color: #065f46;
            }

            .alert-success .alert-icon {
                background-color: #10b981;
                color: white;
            }

            .alert-danger {
                background-color: #fef2f2;
                color: #b91c1c;
            }

            .alert-danger .alert-icon {
                background-color: #ef4444;
                color: white;
            }

            .form-group {
                margin-bottom: 1.25rem;
            }

            .form-label {
                display: block;
                margin-bottom: 0.5rem;
                font-weight: 500;
                color: var(--gray-700);
            }

            .form-control {
                display: block;
                width: 100%;
                padding: 0.625rem 0.875rem;
                font-size: 0.875rem;
                line-height: 1.5;
                color: var(--gray-900);
                background-color: white;
                background-clip: padding-box;
                border: 1px solid var(--gray-300);
                border-radius: var(--border-radius);
                transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
            }

            .form-control:focus {
                border-color: var(--primary);
                outline: 0;
                box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.25);
            }

            .form-control::placeholder {
                color: var(--gray-400);
            }

            .input-group {
                position: relative;
                display: flex;
                flex-wrap: wrap;
                align-items: stretch;
                width: 100%;
            }

            .input-group .form-control {
                position: relative;
                flex: 1 1 auto;
                width: 1%;
                min-width: 0;
                border-top-right-radius: 0;
                border-bottom-right-radius: 0;
            }

            .input-group-append {
                display: flex;
            }

            .input-group-append .btn {
                border-top-left-radius: 0;
                border-bottom-left-radius: 0;
            }

            .btn {
                display: inline-block;
                font-weight: 500;
                text-align: center;
                vertical-align: middle;
                user-select: none;
                padding: 0.625rem 1rem;
                font-size: 0.875rem;
                line-height: 1.5;
                border-radius: var(--border-radius);
                border: 1px solid transparent;
                transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out,
                    border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
                cursor: pointer;
                text-decoration: none;
            }

            .btn:focus {
                outline: 0;
                box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.25);
            }

            .btn-sm {
                padding: 0.375rem 0.75rem;
                font-size: 0.75rem;
            }

            .btn-primary {
                color: white;
                background-color: var(--primary);
                border-color: var(--primary);
            }

            .btn-primary:hover {
                background-color: var(--primary-dark);
                border-color: var(--primary-dark);
            }

            .btn-outline-primary {
                color: var(--primary);
                background-color: transparent;
                border-color: var(--primary);
            }

            .btn-outline-primary:hover {
                color: white;
                background-color: var(--primary);
                border-color: var(--primary);
            }

            .btn-secondary {
                color: white;
                background-color: var(--secondary);
                border-color: var(--secondary);
            }

            .btn-secondary:hover {
                background-color: #4b5563;
                border-color: #4b5563;
            }

            .btn-success {
                color: white;
                background-color: var(--success);
                border-color: var(--success);
            }

            .btn-success:hover {
                background-color: #059669;
                border-color: #059669;
            }

            .btn-danger {
                color: white;
                background-color: var(--danger);
                border-color: var(--danger);
            }

            .btn-danger:hover {
                background-color: #dc2626;
                border-color: #dc2626;
            }

            .btn-warning {
                color: var(--gray-900);
                background-color: var(--warning);
                border-color: var(--warning);
            }

            .btn-warning:hover {
                background-color: #d97706;
                border-color: #d97706;
            }

            .btn-info {
                color: white;
                background-color: var(--info);
                border-color: var(--info);
            }

            .btn-info:hover {
                background-color: #2563eb;
                border-color: #2563eb;
            }

            .btn-icon {
                display: inline-flex;
                align-items: center;
                gap: 0.5rem;
            }

            .table-container {
                margin-bottom: 1.5rem;
                overflow-x: auto;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow);
            }

            .table {
                width: 100%;
                margin-bottom: 0;
                color: var(--gray-700);
                border-collapse: collapse;
            }

            .table th,
            .table td {
                padding: 0.875rem 1.25rem;
                vertical-align: middle;
                border-bottom: 1px solid var(--gray-200);
            }

            .table th {
                font-weight: 600;
                color: var(--gray-800);
                background-color: var(--gray-50);
                text-align: left;
                font-size: 0.75rem;
                text-transform: uppercase;
                letter-spacing: 0.05em;
            }

            .table tbody tr:last-child td {
                border-bottom: none;
            }

            .table-hover tbody tr:hover {
                background-color: var(--gray-50);
            }

            .badge {
                display: inline-flex;
                align-items: center;
                padding: 0.25rem 0.625rem;
                font-size: 0.75rem;
                font-weight: 500;
                line-height: 1;
                text-align: center;
                white-space: nowrap;
                vertical-align: baseline;
                border-radius: 9999px;
            }

            .badge-success {
                color: #065f46;
                background-color: #d1fae5;
            }

            .badge-danger {
                color: #b91c1c;
                background-color: #fee2e2;
            }

            .badge-warning {
                color: #92400e;
                background-color: #fef3c7;
            }

            .badge-info {
                color: #1e40af;
                background-color: #dbeafe;
            }

            .badge-primary {
                color: #1e40af;
                background-color: #dbeafe;
            }

            .badge-secondary {
                color: #475569;
                background-color: #f1f5f9;
            }

            .pagination {
                display: flex;
                padding-left: 0;
                list-style: none;
                border-radius: var(--border-radius);
            }

            .page-item {
                margin: 0 0.125rem;
            }

            .page-item.active .page-link {
                background-color: var(--primary);
                border-color: var(--primary);
                color: white;
            }

            .page-item.disabled .page-link {
                color: var(--gray-400);
                pointer-events: none;
                background-color: var(--gray-100);
                border-color: var(--gray-200);
            }

            .page-link {
                position: relative;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 0.5rem 0.75rem;
                margin-left: -1px;
                line-height: 1.25;
                color: var(--primary);
                background-color: white;
                border: 1px solid var(--gray-200);
                text-decoration: none;
                min-width: 2.25rem;
                transition: all 0.15s ease-in-out;
            }

            .page-link:hover {
                z-index: 2;
                color: var(--primary-dark);
                text-decoration: none;
                background-color: var(--gray-100);
                border-color: var(--gray-200);
            }

            .page-link:focus {
                z-index: 3;
                outline: 0;
                box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.25);
            }

            .filter-panel {
                background-color: white;
                border-radius: var(--border-radius);
                padding: 1.25rem;
                margin-bottom: 1.5rem;
                box-shadow: var(--shadow);
            }

            .filter-title {
                font-size: 0.875rem;
                font-weight: 600;
                margin-bottom: 1rem;
                color: var(--gray-700);
                text-transform: uppercase;
                letter-spacing: 0.05em;
            }

            .filter-row {
                display: flex;
                flex-wrap: wrap;
                gap: 1rem;
                align-items: center;
            }

            .filter-item {
                flex: 1;
                min-width: 200px;
            }

            .select-group {
                display: flex;
                align-items: center;
                gap: 0.5rem;
            }

            .select-group .form-control {
                flex: 1;
                border-top-left-radius: var(--border-radius);
                border-bottom-left-radius: var(--border-radius);
            }

            .select-group .btn {
                border-top-left-radius: 0;
                border-bottom-left-radius: 0;
            }

            .d-flex {
                display: flex !important;
            }

            .flex-wrap {
                flex-wrap: wrap !important;
            }

            .align-items-center {
                align-items: center !important;
            }

            .justify-content-between {
                justify-content: space-between !important;
            }

            .justify-content-center {
                justify-content: center !important;
            }

            .gap-2 {
                gap: 0.5rem !important;
            }

            .gap-3 {
                gap: 0.75rem !important;
            }

            .mb-0 {
                margin-bottom: 0 !important;
            }

            .mb-2 {
                margin-bottom: 0.5rem !important;
            }

            .mb-3 {
                margin-bottom: 0.75rem !important;
            }

            .mb-4 {
                margin-bottom: 1rem !important;
            }

            .mb-5 {
                margin-bottom: 1.25rem !important;
            }

            .mt-3 {
                margin-top: 0.75rem !important;
            }

            .mt-4 {
                margin-top: 1rem !important;
            }

            .mt-5 {
                margin-top: 1.25rem !important;
            }

            .ms-auto {
                margin-left: auto !important;
            }

            .text-center {
                text-align: center !important;
            }

            .text-end {
                text-align: right !important;
            }

            .text-muted {
                color: var(--gray-500) !important;
            }

            .text-success {
                color: var(--success) !important;
            }

            .text-danger {
                color: var(--danger) !important;
            }

            .text-warning {
                color: var(--warning) !important;
            }

            .text-primary {
                color: var(--primary) !important;
            }

            .fw-bold {
                font-weight: 700 !important;
            }

            .fw-semibold {
                font-weight: 600 !important;
            }

            .fw-medium {
                font-weight: 500 !important;
            }

            .fs-sm {
                font-size: 0.875rem !important;
            }

            .fs-xs {
                font-size: 0.75rem !important;
            }

            .footer {
                margin-top: 3rem;
                padding: 1.5rem 0;
                background-color: white;
                border-top: 1px solid var(--gray-200);
                font-size: 0.875rem;
                color: var(--gray-600);
            }

            .footer-content {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 1rem;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .footer-links {
                display: flex;
                gap: 1.5rem;
            }

            .footer-links a {
                color: var(--gray-600);
                text-decoration: none;
                transition: color 0.15s;
            }

            .footer-links a:hover {
                color: var(--primary);
            }

            .modal {
                display: none;
                position: fixed;
                z-index: 1050;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0, 0, 0, 0.4);
            }

            .modal-dialog {
                position: relative;
                width: auto;
                margin: 1.75rem auto;
                max-width: 500px;
            }

            .modal-content {
                position: relative;
                display: flex;
                flex-direction: column;
                width: 100%;
                background-color: white;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow-lg);
                outline: 0;
            }

            .modal-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 1.25rem;
                border-bottom: 1px solid var(--gray-200);
            }

            .modal-title {
                margin-bottom: 0;
                font-size: 1.25rem;
            }

            .modal-close {
                background: transparent;
                border: 0;
                font-size: 1.25rem;
                cursor: pointer;
                color: var(--gray-500);
            }

            .modal-body {
                position: relative;
                flex: 1 1 auto;
                padding: 1.25rem;
            }

            .modal-footer {
                display: flex;
                align-items: center;
                justify-content: flex-end;
                padding: 1.25rem;
                border-top: 1px solid var(--gray-200);
                gap: 0.5rem;
            }

            @media (max-width: 768px) {
                .filter-row {
                    flex-direction: column;
                    gap: 1rem;
                }

                .filter-item {
                    width: 100%;
                }

                .select-group {
                    flex-direction: column;
                    align-items: stretch;
                }

                .select-group .form-control {
                    border-radius: var(--border-radius);
                    margin-bottom: 0.5rem;
                }

                .select-group .btn {
                    border-radius: var(--border-radius);
                }

                .footer-content {
                    flex-direction: column;
                    gap: 1rem;
                    text-align: center;
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
        </style>
    </head>
    <body>
         <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
            <!-- Breadcrumbs -->
            <div class="breadcrumbs mb-4">
                <a href="${pageContext.request.contextPath}/Admin.jsp">Trang chủ</a>
                <span class="separator"><i class="fas fa-chevron-right" style="font-size: 0.75rem;"></i></span>
                <span class="current">Danh sách yêu cầu nhập kho</span>
            </div>

            <div class="card mb-4">
                <div class="card-header">
                    <h1 class="mb-0">Danh sách yêu cầu nhập kho</h1>
                </div>

                <div class="card-body">
                    <!-- Thông báo -->
                    <c:if test="${param.message != null}">
                        <div class="alert alert-success" id="successAlert">
                            <div class="alert-icon">
                                <i class="fas fa-check"></i>
                            </div>
                            <div class="alert-content">
                                <div class="alert-title">Thành công</div>
                                <c:choose>
                                    <c:when test="${param.message eq 'approve_success'}">Phê duyệt nhập kho thành công!</c:when>
                                    <c:when test="${param.message eq 'reject_success'}">Từ chối yêu cầu thành công!</c:when>
                                    <c:otherwise>Thao tác thành công!</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="alert-close" onclick="this.parentElement.style.display = 'none'">
                                <i class="fas fa-times"></i>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger" id="errorAlert">
                            <div class="alert-icon">
                                <i class="fas fa-exclamation-triangle"></i>
                            </div>
                            <div class="alert-content">
                                <div class="alert-title">Lỗi</div>
                                <c:choose>
                                    <c:when test="${param.error eq 'invalid_id'}">ID yêu cầu không hợp lệ!</c:when>
                                    <c:when test="${param.error eq 'request_not_found'}">Không tìm thấy yêu cầu!</c:when>
                                    <c:when test="${param.error eq 'approve_failed'}">Không thể phê duyệt nhập kho!</c:when>
                                    <c:when test="${param.error eq 'invalid_data'}">Dữ liệu không hợp lệ!</c:when>
                                    <c:when test="${param.error eq 'permission_denied'}">Bạn không có quyền thực hiện hành động này!</c:when>
                                    <c:when test="${param.error eq 'invalid_quantity'}">Số lượng nhập không hợp lệ!</c:when>
                                    <c:when test="${param.error eq 'invalid_action'}">Hành động không hợp lệ!</c:when>
                                    <c:when test="${param.error eq 'processing_failed'}">Xử lý yêu cầu thất bại!</c:when>
                                </c:choose>
                            </div>
                            <div class="alert-close" onclick="this.parentElement.style.display = 'none'">
                                <i class="fas fa-times"></i>
                            </div>
                        </div>
                    </c:if>

                    <!-- Bộ lọc và sắp xếp -->
                    <div class="filter-panel mb-4">
                        <div class="filter-title">Tìm kiếm</div>
                        <div class="filter-row">
                            <div class="filter-item">
                                <form action="${pageContext.request.contextPath}/request/list" method="get">
                                    <input type="hidden" name="type" value="purchase">
                                    <div class="select-group">
                                        <select name="searchType" class="form-control" required>
                                            <option value="requestId" ${param.searchType == 'requestId' ? 'selected' : ''}>Mã yêu cầu</option>
                                            <option value="productName" ${param.searchType == 'productName' ? 'selected' : ''}>Tên sản phẩm</option>
                                            <option value="productCode" ${param.searchType == 'productCode' ? 'selected' : ''}>Mã sản phẩm</option>
                                        </select>
                                        <input type="text" name="searchValue" value="${param.searchValue}" class="form-control" placeholder="Nhập giá trị tìm kiếm...">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-search"></i> Tìm kiếm
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Bảng danh sách yêu cầu nhập đã duyệt -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h2 class="mb-0">Danh sách các yêu cầu đã duyệt</h2>
                        </div>
                        <div class="card-body">
                            <div class="table-container">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>STT</th>
                                            <th>Mã yêu cầu</th>
                                            <th>Ngày yêu cầu</th>
                                            <th>Nhà cung cấp</th>
                                            <th>Tên sản phẩm</th>
                                            <th>Mã sản phẩm</th>
                                            <th>Đơn vị</th>
                                            <th>Số lượng</th>
                                            <th>Giá</th>
                                            <th>Ghi chú</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty items}">
                                                <tr>
                                                    <td colspan="11" class="text-center py-4">
                                                        <div style="color: gray;">
                                                            <i class="fas fa-inbox mb-2" style="font-size: 2rem;"></i>
                                                            <p>Không có dữ liệu để hiển thị.</p>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="item" items="${items}" varStatus="status">
                                                    <tr>
                                                        <td>${status.index + 1}</td>
                                                        <td>${item.requestId}</td>
                                                        <td>${item.dayRequest}</td>
                                                        <td>${item.supplier != null ? item.supplier : 'N/A'}</td>
                                                        <td>${item.productName != null ? item.productName : 'No items'}</td>
                                                        <td>${item.productCode != null ? item.productCode : 'N/A'}</td>
                                                        <td>${item.unit != null ? item.unit : 'N/A'}</td>
                                                        <td><fmt:formatNumber value="${item.quantity != null ? item.quantity : 0}" pattern="#,##0.##" /></td>
                                                        <td><fmt:formatNumber value="${item.price != null ? item.price : 0}" pattern="#,##0.## VNĐ" /></td>
                                                        <td>${item.note != null ? item.note : 'N/A'}</td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/import-confirm?id=${item.requestId}" class="btn btn-sm btn-success">
                                                                Xử lý nhập kho
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Phân trang -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Page navigation" class="d-flex justify-content-center">
                            <ul class="pagination">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/request/list?type=purchase&page=${currentPage - 1}&searchType=${param.searchType}&searchValue=${param.searchValue}" aria-label="Previous">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="${pageContext.request.contextPath}/request/list?type=purchase&page=${i}&searchType=${param.searchType}&searchValue=${param.searchValue}">${i}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/request/list?type=purchase&page=${currentPage + 1}&searchType=${param.searchType}&searchValue=${param.searchValue}" aria-label="Next">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </div>

                <div class="card-footer">
                    <div class="d-flex justify-content-between align-items-center">
                        <button type="button" class="btn btn-secondary btn-icon" onclick="window.location.href = '${pageContext.request.contextPath}/import'">
                            <i class="fas fa-sync-alt"></i> Làm mới
                        </button>
                        <a href="${pageContext.request.contextPath}/Admin.jsp" class="btn btn-primary btn-icon">
                            <i class="fas fa-arrow-left"></i> Quay lại Trang chủ
                        </a>
                    </div>
                </div>
            </div>

            <!-- Lịch sử nhập kho -->
            <div class="card mb-4">
                <div class="card-header">
                    <h2 class="mb-0">Lịch sử nhập kho</h2>
                </div>
                <div class="card-body">
                    <div class="table-container">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Mã đơn</th>
                                    <th>Tên sản phẩm</th>
                                    <th>Mã sản phẩm</th>
                                    <th>Nhà cung cấp</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty historyItems}">
                                        <tr>
                                            <td colspan="6" class="text-center py-4">
                                                <div style="color: gray;">
                                                    <i class="fas fa-inbox mb-2" style="font-size: 2rem;"></i>
                                                    <p>Không có dữ liệu để hiển thị.</p>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="item" items="${historyItems}" varStatus="status">                                                                   
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${item.requestId}</td>
                                                <td>${item.productName != null ? item.productName : 'No items'}</td>
                                                <td>${item.productCode != null ? item.productCode : 'N/A'}</td>
                                                <td>${item.supplier}</td>
                                                
                                                <td>
                                                    <a href="LishSupplier" class="btn btn-sm btn-info btn-icon">
                                                        <i class="fas fa-star"></i> Đánh giá
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal xác nhận phê duyệt nhập kho -->
        <div id="approveModal" class="modal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Xác nhận phê duyệt nhập kho</h5>
                        <button type="button" class="modal-close" onclick="closeApproveModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <form id="approveForm" action="${pageContext.request.contextPath}/request/list" method="post">
                        <div class="modal-body">
                            <div class="d-flex align-items-center gap-3 mb-3">
                                <div style="width: 3rem; height: 3rem; background-color: #d1fae5; border-radius: 50%; display: flex; align-items: center; justify-content: center;">
                                    <i class="fas fa-check-circle" style="font-size: 1.5rem; color: #10b981;"></i>
                                </div>
                                <div>
                                    <h2 class="mb-1">Xác nhận nhập kho</h2>
                                    <p class="mb-0 text-muted">Hàng hóa sẽ được cập nhật vào kho sau khi phê duyệt.</p>
                                </div>
                            </div>

                            <input type="hidden" id="requestIdInput" name="id" value="">
                            <p>Bạn đang phê duyệt nhập kho yêu cầu có ID: <strong id="approveRequestId"></strong></p>

                            <div class="form-group">
                                <label for="approveNote" class="form-label">Ghi chú:</label>
                                <textarea id="approveNote" name="note" class="form-control" rows="3" placeholder="Nhập ghi chú phê duyệt (nếu có)"></textarea>
                            </div>

                            <div class="form-group">
                                <label for="approveDate" class="form-label">Ngày nhập kho:</label>
                                <input type="date" id="approveDate" name="approveDate" class="form-control" value="${currentDate}" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" onclick="closeApproveModal()">
                                Hủy bỏ
                            </button>
                            <button type="submit" class="btn btn-success btn-icon">
                                <i class="fas fa-check-circle"></i> Xác nhận nhập kho
                            </button>
                        </div>
                    </form>
                </div>
                            </div>
                            </div>
            </div>

        <script>
            // Xử lý đóng alert sau 5 giây
            window.addEventListener('DOMContentLoaded', function () {
                setTimeout(function () {
                    var successAlert = document.getElementById('successAlert');
                    var errorAlert = document.getElementById('errorAlert');

                    if (successAlert) {
                        successAlert.style.display = 'none';
                    }

                    if (errorAlert) {
                        errorAlert.style.display = 'none';
                    }
                }, 5000);
            });

            // Xử lý modal phê duyệt nhập kho
            function openApproveModal(requestId) {
                document.getElementById('approveRequestId').textContent = requestId;
                document.getElementById('requestIdInput').value = requestId;
                document.getElementById('approveModal').style.display = 'block';
                document.body.style.overflow = 'hidden';
            }

            function closeApproveModal() {
                document.getElementById('approveModal').style.display = 'none';
                document.body.style.overflow = '';
            }

            // Đóng modal khi click bên ngoài
            window.onclick = function (event) {
                if (event.target.className === 'modal') {
                    event.target.style.display = 'none';
                    document.body.style.overflow = '';
                }
            };
        </script>
    </body>
</html>