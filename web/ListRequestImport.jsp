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
            /* CSS Variables */
            :root {
                --primary: #2563eb;
                --primary-dark: #1d4ed8;
                --secondary: #64748b;
                --success: #10b981;
                --danger: #ef4444;
                --warning: #f59e0b;
                --info: #3b82f6;
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
                background: #059669;
            }

            .btn-warning {
                color: white;
                background: var(--warning);
                border-color: var(--warning);
            }
            .btn-warning:hover {
                background: #d97706;
            }

            .btn-continue {
                background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
                color: white;
                border: none;
            }
            .btn-continue:hover {
                background: linear-gradient(135deg, #d97706 0%, #b45309 100%);
                transform: translateY(-1px);
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
            }
            .alert-info .alert-icon {
                background: var(--info);
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

            .table-warning {
                background: #fffbeb !important;
                border-left: 3px solid var(--warning);
            }
            .table-warning:hover {
                background: #fef3c7 !important;
            }

            /* Progress Bars */
            .progress-bar {
                background: #e5e7eb;
                border-radius: 2px;
                overflow: hidden;
                height: 4px;
                margin: 2px auto;
                width: 60px;
            }

            .progress-fill {
                height: 100%;
                border-radius: 2px;
                transition: width 0.3s ease;
            }

            .progress-complete {
                background: linear-gradient(90deg, var(--success) 0%, #059669 100%);
            }
            .progress-partial {
                background: linear-gradient(90deg, var(--warning) 0%, #d97706 100%);
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
                background: #fef3c7;
                color: #92400e;
            }
            .status-processing {
                background: #dbeafe;
                color: #1e40af;
            }
            .status-rejected {
                background: #fee2e2;
                color: #991b1b;
            }
            .status-partial-imported {
                background: #fef3c7;
                color: #92400e;
                border: 1px solid #fbbf24;
            }

            /* Reason Cell (Only for History Table) */
            .reason-cell {
                max-width: 200px;
                position: relative;
            }

            .reason-content {
                font-size: 0.75rem;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                cursor: help;
            }

            .reason-success {
                color: var(--success);
            }
            .reason-danger {
                color: var(--danger);
            }
            .reason-info {
                color: var(--info);
            }
            .reason-muted {
                color: var(--gray-400);
            }

            .reason-tooltip {
                position: absolute;
                bottom: 100%;
                left: 50%;
                transform: translateX(-50%);
                background: var(--gray-900);
                color: white;
                padding: 0.5rem;
                border-radius: var(--border-radius);
                font-size: 0.75rem;
                white-space: normal;
                z-index: 1000;
                opacity: 0;
                visibility: hidden;
                transition: opacity 0.2s, visibility 0.2s;
                max-width: 300px;
            }

            .reason-cell:hover .reason-tooltip {
                opacity: 1;
                visibility: visible;
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
                .reason-cell {
                    max-width: 150px;
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
                    <span class="current">Danh sách yêu cầu nhập kho</span>
                </div>

                <!-- Statistics Cards -->
                <div class="stats-grid">
                    <div class="stat-card success">
                        <div class="stat-value">${approvedCount}</div>
                        <div class="stat-label">Yêu cầu đơn nhập kho đã duyệt</div>
                        <div class="stat-change">
                            <i class="fas fa-arrow-up"></i> Đang chờ xử lý
                        </div>
                    </div>
                    <div class="stat-card info">
                        <div class="stat-value">${historyCount}</div>
                        <div class="stat-label">Đã hoàn thành</div>
                        <div class="stat-change">
                            <i class="fas fa-check-circle"></i> Nhập kho thành công
                        </div>
                    </div>
                    <div class="stat-card warning">
                        <div class="stat-value">${totalCount}</div>
                        <div class="stat-label">Tổng yêu cầu</div>
                        <div class="stat-change">
                            <i class="fas fa-chart-line"></i> Tất cả yêu cầu
                        </div>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header">
                        <h1>Quản lý yêu cầu nhập kho</h1>
                    </div>

                    <div class="card-body">
                        <!-- Tab Navigation -->
                        <div class="tab-nav">
                            <a href="?tab=approved" class="tab-button ${currentTab == 'approved' || empty currentTab ? 'active' : ''}">
                                <i class="fas fa-clock"></i>
                                Yêu cầu đơn nhập kho đã duyệt
                                <span class="tab-badge">${approvedCount}</span>
                            </a>
                            <a href="?tab=history" class="tab-button ${currentTab == 'history' ? 'active' : ''}">
                                <i class="fas fa-history"></i>
                                Lịch sử nhập kho
                                <span class="tab-badge">${historyCount}</span>
                            </a>
                        </div>

                        <!-- Tab Content: Yêu cầu đã duyệt (NO reason column) -->
                        <c:if test="${currentTab == 'approved' || empty currentTab}">
                            <div class="tab-content active">
                                <!-- Filter Panel -->
                                <div class="filter-panel">
                                    <div class="filter-title">
                                        <i class="fas fa-search"></i> Tìm kiếm yêu cầu đã duyệt
                                    </div>
                                    <form method="get" action="">
                                        <input type="hidden" name="tab" value="approved">
                                        <div class="filter-row">
                                            <div class="filter-item">
                                                <label class="form-label">Tìm kiếm theo:</label>
                                                <select name="searchType" class="form-control">
                                                    <option value="requestId" ${searchType == 'requestId' ? 'selected' : ''}>Mã yêu cầu</option>
                                                    <option value="productName" ${searchType == 'productName' ? 'selected' : ''}>Tên sản phẩm</option>
                                                    <option value="productCode" ${searchType == 'productCode' ? 'selected' : ''}>Mã sản phẩm</option>
                                                    <option value="supplier" ${searchType == 'supplier' ? 'selected' : ''}>Nhà cung cấp</option>
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
                                                    <a href="?tab=approved" class="btn btn-secondary btn-icon">
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
                                                        <c:when test="${searchType == 'supplier'}">Nhà cung cấp</c:when>
                                                        <c:otherwise>Mã yêu cầu</c:otherwise>
                                                    </c:choose>
                                                </strong><br>
                                                Tìm thấy <strong>${approvedTotal}</strong> kết quả.
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- Approved Requests Table (NO reason column) -->
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
                                                <th>SL Đặt</th>
                                                <th>Đã nhập</th>
                                                <th>Còn lại</th>
                                                <th>Giá</th>
                                                <th>Trạng thái</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty approvedItems}">
                                                    <tr>
                                                        <td colspan="13" class="text-center" style="padding: 2rem;">
                                                            <div style="color: var(--gray-400);">
                                                                <i class="fas fa-inbox mb-2" style="font-size: 2rem;"></i>
                                                                <p>Không có yêu cầu đơn nhập kho đã duyệt nào để hiển thị.</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${approvedItems}" varStatus="status">
                                                        <tr class="${item.status == 'partial_imported' ? 'table-warning' : ''}">
                                                            <td>${(currentPage - 1) * pageSize + status.index + 1}</td>
                                                            <td>
                                                                <strong>${item.requestId}</strong>
                                                                <c:if test="${item.status == 'partial_imported'}">
                                                                    <br><small class="text-warning"><i class="fas fa-clock"></i> Nhập từng phần</small>
                                                                </c:if>
                                                            </td>
                                                            <td>${item.dayRequest}</td>
                                                            <td>${item.supplier}</td>
                                                            <td>${item.productName}</td>
                                                            <td>${item.productCode}</td>
                                                            <td>${item.unit}</td>
                                                            <td style="text-align: center; font-weight: 600;">
                                                                <fmt:formatNumber value="${item.quantityOrdered > 0 ? item.quantityOrdered : item.quantity}"/>
                                                            </td>
                                                            <td style="text-align: center;">
                                                                <c:if test="${item.quantityImported > 0}">
                                                                    <span style="color: var(--success); font-weight: 600;">
                                                                        <fmt:formatNumber value="${item.quantityImported}"/>
                                                                    </span>
                                                                    <div class="progress-bar">
                                                                        <div class="progress-fill ${item.importProgress >= 100 ? 'progress-complete' : 'progress-partial'}" 
                                                                             style="width: ${item.importProgress}%;"></div>
                                                                    </div>
                                                                    <small style="color: #6b7280;">${String.format("%.0f", item.importProgress)}%</small>
                                                                </c:if>
                                                                <c:if test="${item.quantityImported == 0}">
                                                                    <span style="color: #9ca3af;">Chưa nhập</span>
                                                                </c:if>
                                                            </td>
                                                            <td style="text-align: center;">
                                                                <c:choose>
                                                                    <c:when test="${item.quantityPending > 0}">
                                                                        <span style="color: var(--warning); font-weight: 600;">
                                                                            <fmt:formatNumber value="${item.quantityPending}"/>
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span style="color: var(--success); font-weight: 600;">
                                                                            <i class="fas fa-check"></i> Hoàn thành
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td style="text-align: right;">
                                                                <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/>
                                                            </td>
                                                            <td style="text-align: center;">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'approved'}">
                                                                        <span class="status-badge" style="background: #dbeafe; color: #1e40af;">
                                                                            <i class="fas fa-check-circle"></i> Đã duyệt
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'partial_imported'}">
                                                                        <span class="status-badge status-partial-imported">
                                                                            <i class="fas fa-clock"></i> Nhập từng phần
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="status-badge status-pending">
                                                                            ${item.status}
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <div class="d-flex gap-2">
                                                                    <a href="${pageContext.request.contextPath}/import-confirm?id=${item.requestId}" 
                                                                       class="btn btn-sm ${item.status == 'partial_imported' ? 'btn-continue' : 'btn-success'}">
                                                                        <i class="fas fa-${item.status == 'partial_imported' ? 'plus' : 'check'}"></i> 
                                                                        ${item.status == 'partial_imported' ? 'Tiếp tục nhập' : 'Xử lý nhập kho'}
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
                                        <c:forEach begin="1" end="${approvedPages}" var="i">
                                            <div class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?tab=approved&page=${i}&searchType=${searchType}&searchValue=${searchValue}">${i}</a>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>

                        <!-- Tab Content: Lịch sử nhập kho (WITH reason column) -->
                        <c:if test="${currentTab == 'history'}">
                            <div class="tab-content active">
                                <!-- Filter Panel -->
                                <div class="filter-panel">
                                    <div class="filter-title">
                                        <i class="fas fa-search"></i> Tìm kiếm lịch sử nhập kho
                                    </div>
                                    <form method="get" action="">
                                        <input type="hidden" name="tab" value="history">
                                        <div class="filter-row">
                                            <div class="filter-item">
                                                <label class="form-label">Tìm kiếm theo:</label>
                                                <select name="historySearchType" class="form-control">
                                                    <option value="requestId" ${historySearchType == 'requestId' ? 'selected' : ''}>Mã đơn</option>
                                                    <option value="productName" ${historySearchType == 'productName' ? 'selected' : ''}>Tên sản phẩm</option>
                                                    <option value="productCode" ${historySearchType == 'productCode' ? 'selected' : ''}>Mã sản phẩm</option>
                                                    <option value="supplier" ${historySearchType == 'supplier' ? 'selected' : ''}>Nhà cung cấp</option>
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
                                                    <a href="?tab=history" class="btn btn-secondary btn-icon">
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
                                                        <c:when test="${historySearchType == 'supplier'}">Nhà cung cấp</c:when>
                                                        <c:otherwise>Mã đơn</c:otherwise>
                                                    </c:choose>
                                                </strong><br>
                                                Tìm thấy <strong>${historyTotal}</strong> kết quả.
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <!-- History Table (WITH reason column) -->
                                <div class="table-container">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Mã đơn</th>
                                                <th>Ngày nhập</th>
                                                <th>Tên sản phẩm</th>
                                                <th>Mã sản phẩm</th>
                                                <th>Nhà cung cấp</th>
                                                <th>Số lượng</th>
                                                <th>Đơn giá</th>
                                                <th>Thành tiền</th>
                                                <th>Trạng thái</th>
                                                <th>Lý do</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty historyItems}">
                                                    <tr>
                                                        <td colspan="12" class="text-center" style="padding: 2rem;">
                                                            <div style="color: var(--gray-400);">
                                                                <i class="fas fa-inbox mb-2" style="font-size: 2rem;"></i>
                                                                <p>Không có lịch sử nhập kho nào để hiển thị.</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${historyItems}" varStatus="status">
                                                        <tr>
                                                            <td>${(currentPage - 1) * pageSize + status.index + 1}</td>
                                                            <td>${item.requestId}</td>
                                                            <td>${item.dayRequest}</td>
                                                            <td>${item.productName}</td>
                                                            <td>${item.productCode}</td>
                                                            <td>${item.supplier}</td>
                                                            <td style="text-align: center;">
                                                                <fmt:formatNumber value="${item.quantity}"/>
                                                            </td>
                                                            <td style="text-align: right;">
                                                                <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/>
                                                            </td>
                                                            <td style="text-align: right;">
                                                                <fmt:formatNumber value="${item.quantity * item.price}" type="currency" currencySymbol="₫"/>
                                                            </td>
                                                            <td style="text-align: center;">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'completed' || item.status == 'COMPLETED'}">
                                                                        <span class="status-badge status-completed">
                                                                            <i class="fas fa-check-circle"></i> Hoàn thành
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'rejected' || item.status == 'REJECTED'}">
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
                                                            <!-- Reason Column (ONLY in History tab) -->
                                                            <td class="reason-cell">
                                                                <c:choose>
                                                                    <c:when test="${item.status == 'rejected' || item.status == 'REJECTED'}">
                                                                        <c:if test="${not empty item.rejectReason}">
                                                                            <div class="reason-content reason-danger" title="Lý do từ chối: ${item.rejectReason}">
                                                                                <i class="fas fa-times-circle"></i>
                                                                                ${item.rejectReason}
                                                                            </div>
                                                                            <div class="reason-tooltip">
                                                                                Lý do từ chối: ${item.rejectReason}
                                                                            </div>
                                                                        </c:if>
                                                                        <c:if test="${empty item.rejectReason}">
                                                                            <div class="reason-content reason-danger">
                                                                                <i class="fas fa-times-circle"></i> Từ chối (không có lý do)
                                                                            </div>
                                                                        </c:if>
                                                                    </c:when>
                                                                    <c:when test="${item.status == 'completed' || item.status == 'COMPLETED'}">
                                                                        <c:if test="${not empty item.reasonDetail}">
                                                                            <div class="reason-content reason-success" title="Ghi chú: ${item.reasonDetail}">
                                                                                <i class="fas fa-check-circle"></i>
                                                                                ${item.reasonDetail}
                                                                            </div>
                                                                            <div class="reason-tooltip">
                                                                                Ghi chú: ${item.reasonDetail}
                                                                            </div>
                                                                        </c:if>
                                                                        <c:if test="${empty item.reasonDetail}">
                                                                            <div class="reason-content reason-success">
                                                                                <i class="fas fa-check-circle"></i> Hoàn thành
                                                                            </div>
                                                                        </c:if>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <div class="reason-content reason-muted">
                                                                            <i class="fas fa-minus"></i> Không có
                                                                        </div>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <a href="${pageContext.request.contextPath}/LishSupplier" 
                                                                   class="btn btn-sm btn-warning">
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

                                <!-- Pagination for history tab -->
                                <c:if test="${historyPages > 1}">
                                    <div class="pagination">
                                        <c:forEach begin="1" end="${historyPages}" var="i">
                                            <div class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?tab=history&page=${i}&historySearchType=${historySearchType}&historySearchValue=${historySearchValue}">${i}</a>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                    </div>

                    <div class="card-footer">
                        <div class="d-flex justify-content-between align-items-center">
                            <div class="text-muted">
                                Tổng: ${totalCount} yêu cầu
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
                if (searchValue) {
                    const cells = document.querySelectorAll('td');
                    cells.forEach(cell => {
                        if (cell.textContent.toLowerCase().includes(searchValue.toLowerCase())) {
                            cell.innerHTML = cell.innerHTML.replace(
                                    new RegExp(searchValue, 'gi'),
                                    '<mark style="background-color: #fef08a; padding: 0.1rem 0.2rem; border-radius: 0.25rem;">$&</mark>'
                                    );
                        }
                    });
                }

                // Loading effect on form submit
                const forms = document.querySelectorAll('form');
                forms.forEach(form => {
                    form.addEventListener('submit', function () {
                        const submitBtn = form.querySelector('button[type="submit"]');
                        if (submitBtn) {
                            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang tìm...';
                            submitBtn.disabled = true;
                        }
                    });
                });

                // Animate progress bars
                const progressBars = document.querySelectorAll('.progress-fill');
                progressBars.forEach(bar => {
                    const width = bar.style.width;
                    bar.style.width = '0%';
                    setTimeout(() => {
                        bar.style.width = width;
                    }, 100);
                });

                // Keyboard shortcuts
                document.addEventListener('keydown', function (e) {
                    if (e.ctrlKey && e.key === 'f') {
                        e.preventDefault();
                        const activeSearchInput = document.querySelector('.tab-content.active input[type="text"]');
                        if (activeSearchInput) {
                            activeSearchInput.focus();
                            activeSearchInput.select();
                        }
                    }

                    if (e.key === 'Escape') {
                        const activeSearchInput = document.querySelector('.tab-content.active input[type="text"]');
                        if (activeSearchInput && activeSearchInput.value) {
                            activeSearchInput.value = '';
                            activeSearchInput.focus();
                        }
                    }
                });

                // Auto-refresh for partial imports
                if (document.querySelector('.table-warning')) {
                    setInterval(() => {
                        if (!document.querySelector('input:focus') && !document.querySelector('select:focus')) {
                            const lastActivity = localStorage.getItem('lastActivity');
                            const now = Date.now();
                            if (!lastActivity || (now - parseInt(lastActivity)) > 30000) {
                                window.location.reload();
                            }
                        }
                    }, 30000);

                    ['click', 'keypress', 'scroll'].forEach(event => {
                        document.addEventListener(event, () => {
                            localStorage.setItem('lastActivity', Date.now().toString());
                        });
                    });
                }
            });
        </script>
    </body>
</html>
