<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Danh sách yêu cầu xuất kho | Hệ thống quản lý kho</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            /* CSS Variables */
            :root {
                --primary: #2563eb;
                --primary-dark: #1d4ed8;
                --secondary: #64748b;
                --success: #10b981;
                --success-dark: #059669;
                --danger: #ef4444;
                --danger-dark: #dc2626;
                --warning: #f59e0b;
                --warning-dark: #d97706;
                --info: #3b82f6;
                --info-dark: #1e40af;
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
                --shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
                --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
                --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            }

            /* Reset & Base */
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                font-family: 'Inter', sans-serif;
            }

            body {
                background-color: var(--gray-100);
                color: var(--gray-800);
                line-height: 1.5;
                font-size: 0.875rem;
            }

            /* Layout */
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
                max-width: 1200px;
                margin: 2rem auto;
                padding: 0 1rem;
            }

            /* Components */
            .card {
                background: white;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow-md);
                overflow: hidden;
            }

            .card-header {
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
                background: var(--gray-50);
                padding: 1rem 1.5rem;
                border-top: 1px solid var(--gray-200);
            }

            /* Typography */
            h1 {
                font-size: 1.5rem;
                font-weight: 600;
                color: var(--gray-900);
                margin: 0;
            }

            /* Breadcrumbs */
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
            .breadcrumbs .current {
                color: var(--gray-800);
                font-weight: 500;
            }

            /* Stats Grid */
            .stats-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 1rem;
                margin-bottom: 1.5rem;
            }

            .stat-card {
                background: white;
                border-radius: var(--border-radius);
                padding: 1.25rem;
                box-shadow: var(--shadow);
                border-left: 4px solid var(--primary);
            }

            .stat-card.success {
                border-left-color: var(--success);
            }
            .stat-card.warning {
                border-left-color: var(--warning);
            }
            .stat-card.info {
                border-left-color: var(--info);
            }

            .stat-value {
                font-size: 2rem;
                font-weight: 700;
                color: var(--gray-900);
                margin-bottom: 0.25rem;
            }

            .stat-label {
                font-size: 0.875rem;
                color: var(--gray-600);
                margin-bottom: 0.5rem;
            }

            .stat-change {
                font-size: 0.75rem;
                font-weight: 500;
                color: var(--success);
            }

            /* Tabs */
            .tab-nav {
                display: flex;
                background: white;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow);
                overflow: hidden;
                margin-bottom: 1.5rem;
            }

            .tab-button {
                flex: 1;
                padding: 1rem 1.5rem;
                background: transparent;
                border: none;
                color: var(--gray-600);
                font-weight: 500;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
                text-decoration: none;
            }

            .tab-button:hover {
                background: var(--gray-50);
                color: var(--gray-800);
            }

            .tab-button.active {
                background: var(--primary);
                color: white;
            }

            .tab-badge {
                background: var(--gray-200);
                color: var(--gray-700);
                padding: 0.2rem 0.5rem;
                border-radius: 1rem;
                font-size: 0.75rem;
                font-weight: 600;
                margin-left: 0.5rem;
            }

            .tab-button.active .tab-badge {
                background: rgba(255, 255, 255, 0.2);
                color: white;
            }

            .tab-content {
                display: none;
            }
            .tab-content.active {
                display: block;
            }

            /* Filter Panel */
            .filter-panel {
                background: white;
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
                align-items: end;
            }

            .filter-item {
                flex: 1;
                min-width: 200px;
            }

            .search-group {
                display: flex;
                gap: 0.5rem;
                align-items: end;
            }

            /* Forms */
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
                color: var(--gray-900);
                background: white;
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

            /* Buttons */
            .btn {
                display: inline-block;
                font-weight: 500;
                text-align: center;
                padding: 0.625rem 1rem;
                font-size: 0.875rem;
                border-radius: var(--border-radius);
                border: 1px solid transparent;
                transition: all 0.15s ease-in-out;
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
                background: var(--primary);
                border-color: var(--primary);
            }
            .btn-primary:hover {
                background: var(--primary-dark);
            }

            .btn-secondary {
                color: white;
                background: var(--secondary);
                border-color: var(--secondary);
            }
            .btn-secondary:hover {
                background: #4b5563;
            }

            .btn-success {
                color: white;
                background: var(--success);
                border-color: var(--success);
            }
            .btn-success:hover {
                background: var(--success-dark);
            }

            .btn-warning {
                color: white;
                background: var(--warning);
                border-color: var(--warning);
            }
            .btn-warning:hover {
                background: var(--warning-dark);
            }

            .btn-icon {
                display: inline-flex;
                align-items: center;
                gap: 0.5rem;
            }

            /* Alerts */
            .alert {
                padding: 1rem 1.25rem;
                border-radius: var(--border-radius);
                margin-bottom: 1.5rem;
                display: flex;
                align-items: center;
                gap: 0.75rem;
                box-shadow: var(--shadow);
            }

            .alert-icon {
                width: 1.5rem;
                height: 1.5rem;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 50%;
                flex-shrink: 0;
            }

            .alert-content {
                flex: 1;
            }
            .alert-title {
                font-weight: 600;
                margin-bottom: 0.25rem;
            }

            .alert-info {
                background: #eff6ff;
                color: #1e40af;
                border-left: 4px solid var(--info);
            }
            .alert-info .alert-icon {
                background: var(--info);
                color: white;
            }

            .alert-success {
                background: #f0fdf4;
                color: #166534;
                border-left: 4px solid var(--success);
            }
            .alert-success .alert-icon {
                background: var(--success);
                color: white;
            }

            .alert-warning {
                background: #fffbeb;
                color: #92400e;
                border-left: 4px solid var(--warning);
            }
            .alert-warning .alert-icon {
                background: var(--warning);
                color: white;
            }

            .alert-danger {
                background: #fef2f2;
                color: #991b1b;
                border-left: 4px solid var(--danger);
            }
            .alert-danger .alert-icon {
                background: var(--danger);
                color: white;
            }

            /* Tables */
            .table-container {
                margin-bottom: 1.5rem;
                overflow-x: auto;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow);
            }

            .table {
                width: 100%;
                margin: 0;
                color: var(--gray-700);
                border-collapse: collapse;
                background: white;
            }

            .table th,
            .table td {
                padding: 0.875rem 1rem;
                vertical-align: middle;
                border-bottom: 1px solid var(--gray-200);
            }

            .table th {
                font-weight: 600;
                color: var(--gray-800);
                background: var(--gray-50);
                text-align: left;
                font-size: 0.75rem;
                text-transform: uppercase;
                letter-spacing: 0.05em;
            }

            .table tbody tr:last-child td {
                border-bottom: none;
            }
            .table-hover tbody tr:hover {
                background: var(--gray-50);
            }

            /* Status Badges */
            .status-badge {
                display: inline-block;
                padding: 0.25rem 0.5rem;
                font-size: 0.75rem;
                font-weight: 500;
                border-radius: 0.25rem;
                text-transform: uppercase;
                letter-spacing: 0.05em;
            }

            .status-completed {
                background: #d1fae5;
                color: #065f46;
            }
            .status-pending {
                background: #dbeafe;
                color: #1e40af;
            }
            .status-processing {
                background: #dbeafe;
                color: #1e40af;
            }
            .status-rejected {
                background: #fee2e2;
                color: #991b1b;
            }

            /* Reason Cell */
            .reason-cell {
                max-width: 200px;
                position: relative;
            }

            .reason-content {
                padding: 0.5rem;
                border-radius: 0.25rem;
                font-size: 0.75rem;
                line-height: 1.3;
                cursor: help;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
            }

            .reason-danger {
                background: #fee2e2;
                color: #991b1b;
                border: 1px solid #fca5a5;
            }

            .reason-success {
                background: #d1fae5;
                color: #065f46;
                border: 1px solid #86efac;
            }

            /* Pagination */
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 0.25rem;
                margin-top: 1.5rem;
                padding: 1rem 0;
            }

            .page-item {
                display: inline-block;
            }

            .page-link {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                min-width: 2.5rem;
                height: 2.5rem;
                padding: 0.5rem 0.75rem;
                font-size: 0.875rem;
                font-weight: 500;
                color: var(--primary);
                background: white;
                border: 1px solid var(--gray-300);
                border-radius: var(--border-radius);
                text-decoration: none;
                transition: all 0.2s ease;
            }

            .page-link:hover {
                background: var(--gray-50);
                border-color: var(--primary);
                color: var(--primary-dark);
            }

            .page-item.active .page-link {
                background: var(--primary);
                border-color: var(--primary);
                color: white;
            }

            .page-item.disabled .page-link {
                color: var(--gray-400);
                background: var(--gray-100);
                border-color: var(--gray-200);
                cursor: not-allowed;
                pointer-events: none;
            }

            /* Utilities */
            .d-flex {
                display: flex !important;
            }
            .align-items-center {
                align-items: center !important;
            }
            .justify-content-between {
                justify-content: space-between !important;
            }
            .gap-2 {
                gap: 0.5rem !important;
            }
            .mb-0 {
                margin-bottom: 0 !important;
            }
            .mb-3 {
                margin-bottom: 0.75rem !important;
            }
            .mb-4 {
                margin-bottom: 1rem !important;
            }
            .text-center {
                text-align: center !important;
            }
            .text-muted {
                color: var(--gray-500) !important;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .container {
                    padding: 0 0.5rem;
                }
                .card-body {
                    padding: 1rem;
                }
                .filter-row {
                    flex-direction: column;
                    gap: 1rem;
                }
                .filter-item {
                    width: 100%;
                }
                .search-group {
                    flex-direction: column;
                    align-items: stretch;
                }
                .table-container {
                    font-size: 0.75rem;
                }
                .table th, .table td {
                    padding: 0.5rem;
                }
                .stats-grid {
                    grid-template-columns: 1fr;
                }
                .tab-nav {
                    flex-direction: column;
                }
                .tab-button {
                    justify-content: flex-start;
                }
                .pagination {
                    flex-wrap: wrap;
                    gap: 0.125rem;
                }
                .page-link {
                    min-width: 2rem;
                    height: 2rem;
                    font-size: 0.75rem;
                }
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
                    <span class="current">Danh sách yêu cầu xuất kho</span>
                </div>

                <!-- Success/Error Messages -->
                <c:if test="${param.message == 'export_completed'}">
                    <div class="alert alert-success">
                        <div class="alert-icon">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <div class="alert-content">
                            <div class="alert-title">Xuất kho thành công!</div>
                            <div>Đơn hàng đã được xuất kho hoàn tất và chuyển vào lịch sử.</div>
                        </div>
                    </div>
                </c:if>

                <c:if test="${param.message == 'reject_success'}">
                    <div class="alert alert-danger">
                        <div class="alert-icon">
                            <i class="fas fa-times-circle"></i>
                        </div>
                        <div class="alert-content">
                            <div class="alert-title">Đã từ chối xuất kho!</div>
                            <div>Yêu cầu xuất kho đã được từ chối và chuyển vào lịch sử.</div>
                        </div>
                    </div>
                </c:if>

                <!-- Statistics Cards -->
                <div class="stats-grid">
                    <div class="stat-card success">
                        <div class="stat-value">${approvedCount != null ? approvedCount : 0}</div>
                        <div class="stat-label">Yêu cầu đã duyệt</div>
                        <div class="stat-change">
                            <i class="fas fa-arrow-up"></i> Đang chờ xử lý
                        </div>
                    </div>
                    <div class="stat-card info">
                        <div class="stat-value">${historyCount != null ? historyCount : 0}</div>
                        <div class="stat-label">Đã hoàn thành</div>
                        <div class="stat-change">
                            <i class="fas fa-check-circle"></i> Xuất kho thành công
                        </div>
                    </div>
                    <div class="stat-card warning">
                        <div class="stat-value">${totalCount != null ? totalCount : 0}</div>
                        <div class="stat-label">Tổng yêu cầu</div>
                        <div class="stat-change">
                            <i class="fas fa-chart-line"></i> Tất cả yêu cầu
                        </div>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header">
                        <h1>Quản lý yêu cầu xuất kho</h1>
                    </div>

                    <div class="card-body">
                        <!-- Tab Navigation -->
                        <div class="tab-nav">
                            <a href="?tab=approved" class="tab-button ${currentTab == 'approved' || empty currentTab ? 'active' : ''}">
                                <i class="fas fa-clock"></i>
                                Yêu cầu đã duyệt
                                <span class="tab-badge">${approvedCount != null ? approvedCount : 0}</span>
                            </a>
                            <a href="?tab=history" class="tab-button ${currentTab == 'history' ? 'active' : ''}">
                                <i class="fas fa-history"></i>
                                Lịch sử xuất kho
                                <span class="tab-badge">${historyCount != null ? historyCount : 0}</span>
                            </a>
                        </div>

                        <!-- Tab Content: Yêu cầu đã duyệt -->
                        <c:if test="${currentTab == 'approved' || empty currentTab}">
                            <div class="tab-content active">
                                <!-- Filter Panel -->
                                <div class="filter-panel">
                                    <div class="filter-title">
                                        <i class="fas fa-search"></i> Tìm kiếm yêu cầu đã duyệt
                                    </div>
                                    <form method="get" action="exportRequest">
                                        <input type="hidden" name="action" value="list">
                                        <input type="hidden" name="tab" value="approved">
                                        <div class="filter-row">
                                            <div class="filter-item">
                                                <label class="form-label">Tìm kiếm theo:</label>
                                                <select name="searchType" class="form-control">
                                                    <option value="requestId" ${searchType == 'requestId' ? 'selected' : ''}>Mã yêu cầu</option>
                                                    <option value="productName" ${searchType == 'productName' ? 'selected' : ''}>Tên sản phẩm</option>
                                                    <option value="productCode" ${searchType == 'productCode' ? 'selected' : ''}>Mã sản phẩm</option>
                                                </select>
                                            </div>
                                            <div class="filter-item">
                                                <label class="form-label">Từ khóa:</label>
                                                <input type="text" name="searchValue" value="${searchValue}" 
                                                       class="form-control" placeholder="Nhập từ khóa tìm kiếm..."
                                                       autocomplete="off">
                                            </div>
                                            <div class="filter-item">
                                                <div class="search-group">
                                                    <button type="submit" class="btn btn-primary btn-icon">
                                                        <i class="fas fa-search"></i> Tìm kiếm
                                                    </button>
                                                    <a href="?action=list&tab=approved" class="btn btn-secondary btn-icon">
                                                        <i class="fas fa-times"></i> Xóa
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <!-- Search Results Info -->
                                <c:if test="${not empty searchValue}">
                                    <div class="alert alert-info mb-3">
                                        <div class="alert-icon">
                                            <i class="fas fa-info-circle"></i>
                                        </div>
                                        <div class="alert-content">
                                            <div class="alert-title">Kết quả tìm kiếm</div>
                                            <div>
                                                Tìm kiếm "<strong>${searchValue}</strong>" theo <strong>
                                                    <c:choose>
                                                        <c:when test="${searchType == 'requestId'}">Mã yêu cầu</c:when>
                                                        <c:when test="${searchType == 'productName'}">Tên sản phẩm</c:when>
                                                        <c:when test="${searchType == 'productCode'}">Mã sản phẩm</c:when>
                                                        <c:otherwise>Mã yêu cầu</c:otherwise>
                                                    </c:choose>
                                                </strong><br>
                                                Tìm thấy <strong>${approvedTotal != null ? approvedTotal : 0}</strong> kết quả.
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- Approved Requests Table -->
                                <div class="table-container">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Mã yêu cầu</th>
                                                <th>Ngày yêu cầu</th>
                                                <th>Tên sản phẩm</th>
                                                <th>Mã sản phẩm</th>
                                                <th>Đơn vị</th>
                                                <th>Số lượng</th>
                                                <th>Trạng thái</th>
                                                <th>Ghi chú</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty approvedItems}">
                                                    <tr>
                                                        <td colspan="10" class="text-center" style="padding: 2rem;">
                                                            <div style="color: var(--gray-400);">
                                                                <i class="fas fa-inbox mb-2" style="font-size: 2rem;"></i>
                                                                <p>Không có yêu cầu đã duyệt nào để hiển thị.</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${approvedItems}" varStatus="status">
                                                        <tr>
                                                            <td>${(approvedPage - 1) * pageSize + status.index + 1}</td>
                                                            <td>
                                                                <strong>${item.exportRequestId}</strong>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty item.dayRequest}">
                                                                        <c:choose>
                                                                            <c:when test="${item.dayRequest.length() > 10}">
                                                                                <fmt:parseDate value="${item.dayRequest}" pattern="yyyy-MM-dd HH:mm:ss" var="parsedDate" />
                                                                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <fmt:parseDate value="${item.dayRequest}" pattern="yyyy-MM-dd" var="parsedDate" />
                                                                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">N/A</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <strong>${not empty item.productName ? item.productName : 'N/A'}</strong>
                                                            </td>
                                                            <td>
                                                                <code style="background: var(--gray-100); padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-size: 0.8rem;">
                                                                    ${not empty item.productCode ? item.productCode : 'N/A'}
                                                                </code>
                                                            </td>
                                                            <td>${not empty item.unit ? item.unit : 'N/A'}</td>
                                                            <td style="text-align: center; font-weight: 600; color: var(--info-dark);">
                                                                <fmt:formatNumber value="${item.quantity}" maxFractionDigits="2"/>
                                                            </td>
                                                            <td style="text-align: center;">
                                                                <span class="status-badge status-pending">
                                                                    <i class="fas fa-check-circle"></i> Chờ xuất kho
                                                                </span>
                                                            </td>
                                                            <td style="max-width: 150px; word-wrap: break-word;">
                                                                <c:choose>
                                                                    <c:when test="${not empty item.note}">
                                                                        <span style="font-size: 0.8rem; color: var(--gray-600);">
                                                                            ${item.note}
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">-</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <div class="d-flex gap-2">
                                                                    <a href="${pageContext.request.contextPath}/export?id=${item.exportRequestId}" 
                                                                       class="btn btn-sm btn-success">
                                                                        <i class="fas fa-check"></i> 
                                                                        Xử lý xuất kho
                                                                    </a>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination for approved tab -->
                                <c:if test="${approvedPages > 1}">
                                    <div class="pagination">
                                        <c:if test="${approvedPage > 1}">
                                            <div class="page-item">
                                                <a class="page-link" href="?action=list&tab=approved&page=${approvedPage - 1}&searchType=${searchType}&searchValue=${searchValue}">
                                                    <i class="fas fa-chevron-left"></i>
                                                </a>
                                            </div>
                                        </c:if>

                                        <c:forEach begin="1" end="${approvedPages}" var="i">
                                            <div class="page-item ${approvedPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?action=list&tab=approved&page=${i}&searchType=${searchType}&searchValue=${searchValue}">${i}</a>
                                            </div>
                                        </c:forEach>

                                        <c:if test="${approvedPage < approvedPages}">
                                            <div class="page-item">
                                                <a class="page-link" href="?action=list&tab=approved&page=${approvedPage + 1}&searchType=${searchType}&searchValue=${searchValue}">
                                                    <i class="fas fa-chevron-right"></i>
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>

                        <!-- Tab Content: Lịch sử xuất kho -->
                        <c:if test="${currentTab == 'history'}">
                            <div class="tab-content active">
                                <!-- Filter Panel -->
                                <div class="filter-panel">
                                    <div class="filter-title">
                                        <i class="fas fa-search"></i> Tìm kiếm lịch sử xuất kho
                                    </div>
                                    <form method="get" action="exportRequest">
                                        <input type="hidden" name="action" value="list">
                                        <input type="hidden" name="tab" value="history">
                                        <div class="filter-row">
                                            <div class="filter-item">
                                                <label class="form-label">Tìm kiếm theo:</label>
                                                <select name="historySearchType" class="form-control">
                                                    <option value="requestId" ${historySearchType == 'requestId' ? 'selected' : ''}>Mã đơn</option>
                                                    <option value="productName" ${historySearchType == 'productName' ? 'selected' : ''}>Tên sản phẩm</option>
                                                    <option value="productCode" ${historySearchType == 'productCode' ? 'selected' : ''}>Mã sản phẩm</option>
                                                </select>
                                            </div>
                                            <div class="filter-item">
                                                <label class="form-label">Từ khóa:</label>
                                                <input type="text" name="historySearchValue" value="${historySearchValue}" 
                                                       class="form-control" placeholder="Nhập từ khóa tìm kiếm..."
                                                       autocomplete="off">
                                            </div>
                                            <div class="filter-item">
                                                <div class="search-group">
                                                    <button type="submit" class="btn btn-primary btn-icon">
                                                        <i class="fas fa-search"></i> Tìm kiếm
                                                    </button>
                                                    <a href="?action=list&tab=history" class="btn btn-secondary btn-icon">
                                                        <i class="fas fa-times"></i> Xóa
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <!-- Search Results Info for History -->
                                <c:if test="${not empty historySearchValue}">
                                    <div class="alert alert-info mb-3">
                                        <div class="alert-icon">
                                            <i class="fas fa-info-circle"></i>
                                        </div>
                                        <div class="alert-content">
                                            <div class="alert-title">Kết quả tìm kiếm lịch sử</div>
                                            <div>
                                                Tìm kiếm "<strong>${historySearchValue}</strong>" theo <strong>
                                                    <c:choose>
                                                        <c:when test="${historySearchType == 'requestId'}">Mã đơn</c:when>
                                                        <c:when test="${historySearchType == 'productName'}">Tên sản phẩm</c:when>
                                                        <c:when test="${historySearchType == 'productCode'}">Mã sản phẩm</c:when>
                                                        <c:otherwise>Mã đơn</c:otherwise>
                                                    </c:choose>
                                                </strong><br>
                                                Tìm thấy <strong>${historyTotal != null ? historyTotal : 0}</strong> kết quả.
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- History Table -->
                                <div class="table-container">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Mã đơn</th>
                                                <th>Ngày yêu cầu</th>
                                                <th>Tên sản phẩm</th>
                                                <th>Mã sản phẩm</th>
                                                <th>SL Yêu cầu</th>
                                                <th>SL Xuất</th>
                                                <th>Đơn vị</th>
                                                <th>Trạng thái</th>
                                                <th>Lý do</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty historyItems}">
                                                    <tr>
                                                        <td colspan="10" class="text-center" style="padding: 2rem;">
                                                            <div style="color: var(--gray-400);">
                                                                <i class="fas fa-inbox mb-2" style="font-size: 2rem;"></i>
                                                                <p>Không có lịch sử xuất kho nào để hiển thị.</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${historyItems}" varStatus="status">
                                                        <tr>
                                                            <td>${(historyPage - 1) * pageSize + status.index + 1}</td>
                                                            <td>
                                                                <strong>${item.exportRequestId}</strong>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty item.dayRequest}">
                                                                        <c:choose>
                                                                            <c:when test="${item.dayRequest.length() > 10}">
                                                                                <fmt:parseDate value="${item.dayRequest}" pattern="yyyy-MM-dd HH:mm:ss" var="parsedDate" />
                                                                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <fmt:parseDate value="${item.dayRequest}" pattern="yyyy-MM-dd" var="parsedDate" />
                                                                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">N/A</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <strong>${not empty item.productName ? item.productName : 'N/A'}</strong>
                                                            </td>
                                                            <td>
                                                                <code style="background: var(--gray-100); padding: 0.25rem 0.5rem; border-radius: 0.25rem; font-size: 0.8rem;">
                                                                    ${not empty item.productCode ? item.productCode : 'N/A'}
                                                                </code>
                                                            </td>
                                                            <td style="text-align: center; font-weight: 600; color: var(--info-dark);">
                                                                <fmt:formatNumber value="${item.quantity}" maxFractionDigits="2"/>
                                                            </td>
                                                            <td style="text-align: center;">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'rejected'}">
                                                                        <span style="color: var(--danger); font-weight: 600;">
                                                                            0
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span style="color: var(--success); font-weight: 600;">
                                                                            <fmt:formatNumber value="${item.exportedQty}" maxFractionDigits="2"/>
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>${not empty item.unit ? item.unit : 'N/A'}</td>
                                                            <td style="text-align: center;">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'completed'}">
                                                                        <span class="status-badge status-completed">
                                                                            <i class="fas fa-check-circle"></i> Hoàn thành
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'rejected'}">
                                                                        <span class="status-badge status-rejected">
                                                                            <i class="fas fa-times-circle"></i> Từ chối
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="status-badge status-pending">
                                                                            <i class="fas fa-question-circle"></i> ${item.status}
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td class="reason-cell">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'rejected' && not empty item.rejectReason}">
                                                                        <div class="reason-content reason-danger" title="Lý do từ chối: ${item.rejectReason}">
                                                                            <i class="fas fa-times-circle"></i>
                                                                            ${item.rejectReason}
                                                                        </div>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'completed' && not empty item.reasonDetail}">
                                                                        <div class="reason-content reason-success" title="Lý do yêu cầu: ${item.reasonDetail}">
                                                                            <i class="fas fa-info-circle"></i>
                                                                            ${item.reasonDetail}
                                                                        </div>
                                                                    </c:when>
                                                                    <c:when test="${not empty item.note}">
                                                                        <div class="reason-content reason-success" title="Ghi chú: ${item.note}">
                                                                            <i class="fas fa-sticky-note"></i>
                                                                            ${item.note}
                                                                        </div>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">
                                                                            <i class="fas fa-minus"></i> Không có
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Pagination for history tab -->
                                <c:if test="${historyPages > 1}">
                                    <div class="pagination">
                                        <c:if test="${historyPage > 1}">
                                            <div class="page-item">
                                                <a class="page-link" href="?action=list&tab=history&page=${historyPage - 1}&historySearchType=${historySearchType}&historySearchValue=${historySearchValue}">
                                                    <i class="fas fa-chevron-left"></i>
                                                </a>
                                            </div>
                                        </c:if>

                                        <c:forEach begin="1" end="${historyPages}" var="i">
                                            <div class="page-item ${historyPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?action=list&tab=history&page=${i}&historySearchType=${historySearchType}&historySearchValue=${historySearchValue}">${i}</a>
                                            </div>
                                        </c:forEach>

                                        <c:if test="${historyPage < historyPages}">
                                            <div class="page-item">
                                                <a class="page-link" href="?action=list&tab=history&page=${historyPage + 1}&historySearchType=${historySearchType}&historySearchValue=${historySearchValue}">
                                                    <i class="fas fa-chevron-right"></i>
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                    </div>

                    <div class="card-footer">
                        <div class="d-flex justify-content-between align-items-center">
                            <div class="text-muted">
                                <c:choose>
                                    <c:when test="${currentTab == 'approved' || empty currentTab}">
                                        Hiển thị: ${not empty approvedItems ? fn:length(approvedItems) : 0} / ${approvedTotal != null ? approvedTotal : 0} yêu cầu đã duyệt
                                    </c:when>
                                    <c:otherwise>
                                        Hiển thị: ${not empty historyItems ? fn:length(historyItems) : 0} / ${historyTotal != null ? historyTotal : 0} lịch sử xuất kho
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <a href="${pageContext.request.contextPath}/Admin.jsp" class="btn btn-primary btn-icon">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Auto-focus search input
                const searchInputs = document.querySelectorAll('input[name="searchValue"], input[name="historySearchValue"]');
                searchInputs.forEach(input => {
                    if (input.value) {
                        input.focus();
                        input.setSelectionRange(input.value.length, input.value.length);
                    }
                });

                // Highlight search results
                const searchValue = '${searchValue}' || '${historySearchValue}';
                if (searchValue && searchValue.trim() !== '') {
                    const cells = document.querySelectorAll('td');
                    cells.forEach(cell => {
                        const cellText = cell.textContent || cell.innerText;
                        if (cellText.toLowerCase().includes(searchValue.toLowerCase())) {
                            const regex = new RegExp(`(${searchValue})`, 'gi');
                            cell.innerHTML = cell.innerHTML.replace(regex, '<mark style="background-color: #fef08a; padding: 0.1rem 0.2rem; border-radius: 0.25rem;">$1</mark>');
                        }
                    });
                }

                // Loading effect on form submit
                const forms = document.querySelectorAll('form');
                forms.forEach(form => {
                    form.addEventListener('submit', function () {
                        const submitBtn = form.querySelector('button[type="submit"]');
                        if (submitBtn) {
                            const originalContent = submitBtn.innerHTML;
                            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang tìm...';
                            submitBtn.disabled = true;
                            // Reset button after 5 seconds in case of error
                            setTimeout(() => {
                                submitBtn.innerHTML = originalContent;
                                submitBtn.disabled = false;
                            }, 5000);
                        }
                    });
                });

                // Keyboard shortcuts
                document.addEventListener('keydown', function (e) {
                    // Ctrl + F to focus search
                    if (e.ctrlKey && e.key === 'f') {
                        e.preventDefault();
                        const activeSearchInput = document.querySelector('.tab-content.active input[type="text"]');
                        if (activeSearchInput) {
                            activeSearchInput.focus();
                            activeSearchInput.select();
                        }
                    }

                    // Escape to clear search
                    if (e.key === 'Escape') {
                        const activeSearchInput = document.querySelector('.tab-content.active input[type="text"]');
                        if (activeSearchInput && activeSearchInput.value) {
                            activeSearchInput.value = '';
                            activeSearchInput.focus();
                        }
                    }
                });

                // Tooltip for truncated text
                const reasonCells = document.querySelectorAll('.reason-content');
                reasonCells.forEach(cell => {
                    if (cell.scrollWidth > cell.clientWidth) {
                        cell.style.cursor = 'help';
                        cell.addEventListener('mouseenter', function () {
                            this.style.whiteSpace = 'normal';
                            this.style.webkitLineClamp = 'unset';
                        });
                        cell.addEventListener('mouseleave', function () {
                            this.style.whiteSpace = '';
                            this.style.webkitLineClamp = '2';
                        });
                    }
                });

                // Auto refresh every 30 seconds if no search is active
                if (!searchValue || searchValue.trim() === '') {
                    setInterval(function () {
                        // Only refresh if user is not actively searching
                        const activeInput = document.activeElement;
                        if (!activeInput || activeInput.tagName !== 'INPUT') {
                            // Soft refresh - just reload the current page
                            const currentUrl = window.location.href;
                            if (!currentUrl.includes('searchValue=') && !currentUrl.includes('historySearchValue=')) {
                                window.location.reload();
                            }
                        }
                    }, 30000); // 30 seconds
                }

                // Show loading state for action buttons
                const actionButtons = document.querySelectorAll('a[href*="export"]');
                actionButtons.forEach(button => {
                    button.addEventListener('click', function (e) {
                        const icon = this.querySelector('i');
                        const originalText = this.innerHTML;

                        if (icon) {
                            this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang tải...';
                        }

                        // Disable button to prevent double click
                        this.style.pointerEvents = 'none';
                        this.style.opacity = '0.7';

                        // Re-enable after 3 seconds in case of error
                        setTimeout(() => {
                            this.innerHTML = originalText;
                            this.style.pointerEvents = '';
                            this.style.opacity = '';
                        }, 3000);
                    });
                });

                // Auto-dismiss alerts after 10 seconds
                const alerts = document.querySelectorAll('.alert');
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

                // Add click to dismiss alerts
                alerts.forEach(alert => {
                    alert.style.cursor = 'pointer';
                    alert.addEventListener('click', function () {
                        this.style.opacity = '0';
                        this.style.transform = 'translateY(-20px)';
                        this.style.transition = 'all 0.3s ease';
                        setTimeout(() => {
                            this.remove();
                        }, 300);
                    });
                });

                // Enhanced search functionality
                const searchInputsEnhanced = document.querySelectorAll('input[type="text"]');
                searchInputsEnhanced.forEach(input => {
                    let searchTimeout;

                    input.addEventListener('input', function () {
                        clearTimeout(searchTimeout);

                        // Add search icon animation
                        const form = this.closest('form');
                        const submitBtn = form.querySelector('button[type="submit"]');
                        const icon = submitBtn.querySelector('i');

                        if (this.value.length > 0) {
                            icon.className = 'fas fa-search fa-pulse';
                            submitBtn.style.background = 'linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%)';
                        } else {
                            icon.className = 'fas fa-search';
                            submitBtn.style.background = '';
                        }

                        // Auto-search after 1 second of inactivity
                        if (this.value.length >= 3) {
                            searchTimeout = setTimeout(() => {
                                form.submit();
                            }, 1000);
                        }
                    });
                });

                // Add keyboard navigation for tables
                document.addEventListener('keydown', function (e) {
                    if (e.altKey && e.key === 'ArrowUp') {
                        e.preventDefault();
                        // Navigate to previous page
                        const prevLink = document.querySelector('.pagination .page-item:not(.disabled) .page-link[href*="page=' + (parseInt('${currentTab == "approved" ? approvedPage : historyPage}') - 1) + '"]');
                        if (prevLink)
                            prevLink.click();
                    }

                    if (e.altKey && e.key === 'ArrowDown') {
                        e.preventDefault();
                        // Navigate to next page
                        const nextLink = document.querySelector('.pagination .page-item:not(.disabled) .page-link[href*="page=' + (parseInt('${currentTab == "approved" ? approvedPage : historyPage}') + 1) + '"]');
                        if (nextLink)
                            nextLink.click();
                    }

                    if (e.altKey && e.key === '1') {
                        e.preventDefault();
                        // Switch to approved tab
                        window.location.href = '?action=list&tab=approved';
                    }

                    if (e.altKey && e.key === '2') {
                        e.preventDefault();
                        // Switch to history tab
                        window.location.href = '?action=list&tab=history';
                    }
                });

                // Add tooltips for action buttons
                const actionButtonsTooltip = document.querySelectorAll('.btn');
                actionButtonsTooltip.forEach(btn => {
                    if (btn.classList.contains('btn-success')) {
                        btn.title = 'Bắt đầu xử lý xuất kho toàn bộ đơn hàng';
                    }
                });

                // Add visual feedback for successful operations
                const urlParams = new URLSearchParams(window.location.search);
                const message = urlParams.get('message');

                if (message) {
                    // Add confetti effect for successful operations
                    if (message === 'export_completed') {
                        // Simple confetti effect
                        setTimeout(() => {
                            for (let i = 0; i < 50; i++) {
                                createConfetti();
                            }
                        }, 500);
                    }
                }

                function createConfetti() {
                    const confetti = document.createElement('div');
                    confetti.style.position = 'fixed';
                    confetti.style.left = Math.random() * 100 + 'vw';
                    confetti.style.top = '-10px';
                    confetti.style.width = '10px';
                    confetti.style.height = '10px';
                    confetti.style.backgroundColor = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7'][Math.floor(Math.random() * 5)];
                    confetti.style.borderRadius = '50%';
                    confetti.style.pointerEvents = 'none';
                    confetti.style.zIndex = '9999';
                    confetti.style.animation = 'fall 3s linear forwards';

                    document.body.appendChild(confetti);

                    setTimeout(() => {
                        confetti.remove();
                    }, 3000);
                }

                // Add fall animation for confetti
                const style = document.createElement('style');
                style.textContent = `
                    @keyframes fall {
                        to {
                            transform: translateY(100vh) rotate(360deg);
                        }
                    }
                `;
                document.head.appendChild(style);

                // Performance optimization: Lazy load images if any
                const images = document.querySelectorAll('img[data-src]');
                const imageObserver = new IntersectionObserver((entries, observer) => {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            const img = entry.target;
                            img.src = img.dataset.src;
                            img.removeAttribute('data-src');
                            imageObserver.unobserve(img);
                        }
                    });
                });

                images.forEach(img => imageObserver.observe(img));

                // Add print functionality
                document.addEventListener('keydown', function (e) {
                    if (e.ctrlKey && e.key === 'p') {
                        e.preventDefault();
                        window.print();
                    }
                });

                // Enhanced accessibility
                const focusableElements = document.querySelectorAll('a, button, input, select, textarea');
                focusableElements.forEach((element, index) => {
                    element.addEventListener('focus', function () {
                        this.style.outline = '2px solid var(--primary)';
                        this.style.outlineOffset = '2px';
                    });

                    element.addEventListener('blur', function () {
                        this.style.outline = '';
                        this.style.outlineOffset = '';
                    });
                });

                console.log('ExportList.jsp loaded successfully - Full export mode enabled!');
            });
        </script>
    </body>
</html>


