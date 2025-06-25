<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Request"%>
<%@page import="model.RequestItem"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Material Icons -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <title>Phê Duyệt Yêu Cầu Mua Hàng</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            body {
                background-color: #f0f2f5;
                align-items: flex-start;
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
            .filter-section {
                margin-bottom: 20px;
                display: flex;
                justify-content: center;
                gap: 20px;
                flex-wrap: wrap;
            }
            .filter-item {
                display: flex;
                align-items: center;
                gap: 5px;
            }
            .filter-section label {
                font-size: 14px;
                color: #555;
            }
            .filter-section select, .filter-section input {
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 8px;
                font-size: 14px;
            }
            .filter-section .filter-button, .filter-section .clear-filter-button {
                padding: 10px 20px;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 14px;
                display: flex;
                align-items: center;
                gap: 5px;
                transition: background 0.3s, transform 0.2s;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
            .filter-section .filter-button {
                background: #28a745;
            }
            .filter-section .filter-button:hover {
                background: #218838;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            .filter-section .clear-filter-button {
                background: #dc3545;
            }
            .filter-section .clear-filter-button:hover {
                background: #c82333;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            .filter-section .material-icons {
                font-size: 18px;
            }
            .requests-table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            .requests-table th, .requests-table td {
                border: 1px solid #ddd;
                padding: 12px;
                text-align: center;
                font-size: 14px;
            }
            .requests-table th {
                background-color: #007bff;
                color: #fff;
            }

            /* Style cho action buttons trong table */
            .action-buttons-cell {
                display: flex;
                gap: 5px;
                justify-content: center;
                align-items: center;
                flex-wrap: wrap;
            }
            .action-btn {
                padding: 6px 12px;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 12px;
                display: flex;
                align-items: center;
                gap: 3px;
                transition: all 0.3s;
                text-decoration: none;
                color: #fff;
            }
            .delete-btn {
                background: #dc3545;
            }
            .delete-btn:hover {
                background: #c82333;
                transform: translateY(-1px);
            }
            .view-btn {
                background: #17a2b8;
            }
            .view-btn:hover {
                background: #138496;
                transform: translateY(-1px);
            }
            .action-btn .material-icons {
                font-size: 16px;
            }

            .status-approved {
                background-color: #fff3cd !important;
                border-left: 4px solid #ffc107 !important;
                color: #856404 !important;
                font-weight: bold !important;
            }
            .status-rejected {
                background-color: #f8d7da !important;
                border-left: 4px solid #dc3545 !important;
                color: #721c24 !important;
                font-weight: bold !important;
            }
            .status-completed {
                background-color: #d4edda !important;
                border-left: 4px solid #28a745 !important;
                color: #155724 !important;
                font-weight: bold !important;
            }
            .detail-row {
                display: none;
            }
            .detail-table {
                width: 100%;
                border-collapse: collapse;
                background: #f8f9fa;
                animation: dropDown 0.3s ease-out;
            }
            .detail-table th, .detail-table td {
                border: 1px solid #ddd;
                padding: 10px;
                text-align: center;
                font-size: 14px;
            }
            .detail-table th {
                background-color: #28a745;
                color: #fff;
            }
            .detail-table th:nth-child(1), .detail-table td:nth-child(1) {
                width: 10cm;
            }
            .detail-table th:nth-child(2), .detail-table td:nth-child(2) {
                width: 1.5cm;
            }
            .detail-table th:nth-child(3), .detail-table td:nth-child(3) {
                width: 1.5cm;
            }
            .detail-table th:nth-child(4), .detail-table td:nth-child(4) {
                width: 1.5cm;
            }
            .detail-table th:nth-child(5), .detail-table td:nth-child(5) {
                width: 8cm;
            }
            .action-buttons {
                margin-top: 10px;
                text-align: center;
            }
            .action-buttons button {
                padding: 8px 16px;
                margin: 0 5px;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                transition: background 0.3s;
            }
            .action-buttons .approve-btn, .action-buttons .approve-all-btn {
                background: #28a745;
            }
            .action-buttons .approve-btn:hover, .action-buttons .approve-all-btn:hover {
                background: #218838;
            }
            .action-buttons .reject-btn, .action-buttons .reject-all-btn {
                background: #dc3545;
            }
            .action-buttons .reject-btn:hover, .action-buttons .reject-all-btn:hover {
                background: #c82333;
            }
            .status-message {
                color: #555;
                font-style: italic;
                text-align: center;
                margin-top: 10px;
            }
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 15px;
                margin-top: 20px;
                flex-wrap: wrap;
            }
            .pagination button {
                padding: 8px 16px;
                background: #007bff;
                color: #fff;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 14px;
                display: flex;
                align-items: center;
                gap: 5px;
                transition: background 0.3s, transform 0.2s;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
            .pagination .pagination-button {
                text-decoration: none;
            }
            .pagination button:hover:not(:disabled) {
                background: #0056b3;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            .pagination button:disabled {
                background: #6c757d;
                cursor: not-allowed;
                opacity: 0.6;
            }
            .pagination button a {
                color: #fff;
                text-decoration: none;
            }
            .pagination .current-page {
                font-size: 16px;
                color: #333;
                font-weight: bold;
                padding: 8px 12px;
                background: #e9ecef;
                border-radius: 8px;
            }
            .pagination .total-pages {
                font-size: 14px;
                color: #555;
                margin-top: 10px;
                text-align: center;
                width: 100%;
            }
            .footer {
                text-align: center;
                margin-top: 30px;
            }
            .btn-secondary {
                display: inline-block;
                padding: 10px 20px;
                border-radius: 5px;
                text-decoration: none;
                font-weight: bold;
                cursor: pointer;
                transition: background-color 0.3s;
                background-color: transparent;
                color: #666;
                border: 1px solid #ccc;
            }
            .btn-secondary:hover {
                background-color: #f0f0f0;
            }
            .back-btn-icon {
                font-size: 16px;
                vertical-align: text-bottom;
                margin-right: 5px;
            }
            @keyframes dropDown {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
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
            <h1>PHÊ DUYỆT YÊU CẦU MUA HÀNG</h1>
            <form action="approvepurchaserequest" method="post" class="filter-section" id="filterForm">
                <div class="filter-item">
                    <label for="startDate">Từ ngày:</label>
                    <input type="date" id="startDate" name="startDate" value="${param.startDate}">
                </div>
                <div class="filter-item">
                    <label for="endDate">Đến ngày:</label>
                    <input type="date" id="endDate" name="endDate" value="${param.endDate}">
                </div>
                <div class="filter-item">
                    <label for="statusFilter">Trạng thái:</label>
                    <select id="statusFilter" name="statusFilter">
                        <option value="" ${empty param.statusFilter ? 'selected' : ''}>Tất cả</option>
                        <option value="completed" ${param.statusFilter == 'completed' ? 'selected' : ''}>Đã xong</option>
                        <option value="approved" ${param.statusFilter == 'approved' || param.statusFilter == 'quoted' || param.statusFilter == 'pending re-quote' ? 'selected' : ''}>Đã duyệt (chờ báo giá)</option>
                        <option value="rejected" ${param.statusFilter == 'rejected' ? 'selected' : ''}>Đã từ chối</option>
                        <option value="pending" ${param.statusFilter == 'pending' ? 'selected' : ''}>Đang chờ</option>
                    </select>
                </div>
                <div class="filter-item">
                    <label for="requestIdFilter">ID:</label>
                    <input type="text" id="requestIdFilter" name="requestIdFilter" value="${param.requestIdFilter}" placeholder="Nhập ID">
                </div>
                <button type="submit" class="filter-button">
                    <span class="material-icons">filter_alt</span> Lọc
                </button>
                <button type="button" class="clear-filter-button" onclick="clearFilters()">
                    <span class="material-icons">clear</span> Xóa
                </button>
            </form>
            <table class="requests-table">
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>ID</th>
                        <th>Người Yêu Cầu</th>
                        <th>Ngày Yêu Cầu</th>
                        <th>Trạng thái</th>
                        <th>Mục Đích Yêu Cầu</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody id="requestsTableBody">
                    <c:choose>
                        <c:when test="${not empty pendingPurchaseRequests}">
                            <c:forEach var="req" items="${pendingPurchaseRequests}" varStatus="loop">
                                <c:set var="status" value="" />
                                <c:set var="rowClass" value="" />
                                <c:choose>
                                    <c:when test="${req.status == 'pending'}">
                                        <c:set var="status" value="Đang chờ" />
                                    </c:when>
                                    <c:when test="${req.status == 'approved'}">
                                        <c:set var="status" value="Đã duyệt (chờ báo giá)" />
                                        <c:set var="rowClass" value="status-approved" />
                                    </c:when>
                                    <c:when test="${req.status == 'quoted'}">
                                        <c:set var="status" value="Đã duyệt (chờ báo giá)" />
                                        <c:set var="rowClass" value="status-approved" />
                                    </c:when>
                                    <c:when test="${req.status == 'pending re-quote'}">
                                        <c:set var="status" value="Đã duyệt (chờ báo giá)" />
                                        <c:set var="rowClass" value="status-approved" />
                                    </c:when>
                                    <c:when test="${req.status == 'completed'}">
                                        <c:set var="status" value="Đã xong" />
                                        <c:set var="rowClass" value="status-completed" />
                                    </c:when>
                                    <c:when test="${req.status == 'rejected'}">
                                        <c:set var="status" value="Đã từ chối" />
                                        <c:set var="rowClass" value="status-rejected" />
                                    </c:when>
                                </c:choose>
                                <tr class="${rowClass}">
                                    <td>${loop.count}</td>
                                    <td>${req.id}</td>
                                    <td>${req.fullname}</td>
                                    <td>
                                        <c:if test="${not empty req.day_request}">
                                            <fmt:formatDate value="${req.day_request}" pattern="dd/MM/yyyy" />
                                        </c:if>
                                    </td>
                                    <td>${status}</td>
                                    <td>${req.reason}</td>
                                    <td>
                                        <div class="action-buttons-cell">
                                            <!-- Nút xóa -->
                                            <form action="${pageContext.request.contextPath}/approvepurchaserequest" method="post" style="display:inline;">
                                                <input type="hidden" name="requestId" value="${req.id}">
                                                <input type="hidden" name="action" value="delete">
                                                <!-- Giữ lại filter params -->
                                                <input type="hidden" name="startDate" value="${param.startDate}">
                                                <input type="hidden" name="endDate" value="${param.endDate}">
                                                <input type="hidden" name="statusFilter" value="${param.statusFilter}">
                                                <input type="hidden" name="requestIdFilter" value="${param.requestIdFilter}">
                                                <input type="hidden" name="index" value="${param.index}">
                                                <button type="submit" class="action-btn delete-btn" onclick="return confirm('Bạn có chắc muốn xóa đơn hàng này?')">
                                                    <span class="material-icons">delete</span>
                                                    <span>Xóa</span>
                                                </button>
                                            </form>

                                            <!-- Nút xem chi tiết -->
                                            <button onclick="toggleDetails(this)" class="action-btn view-btn">
                                                <span class="material-icons">visibility</span>
                                                <span>Chi tiết</span>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="detail-row">
                                    <td colspan="7">
                                        <div style="margin-bottom: 10px;">
                                            <strong>Gợi ý nhà cung cấp:</strong> ${empty req.supplier or req.supplier == 'null' ? 'Chưa có' : req.supplier}<br>
                                            <strong>Địa chỉ:</strong> ${empty req.address or req.address == 'null' ? 'Chưa có' : req.address}<br>
                                            <strong>SĐT:</strong> ${empty req.phone or req.phone == 'null' ? 'Chưa có' : req.phone}<br>
                                            <strong>Email:</strong> ${empty req.email or req.email == 'null' ? 'Chưa có' : req.email}
                                        </div>
                                        <table class="detail-table">
                                            <thead>
                                                <tr>
                                                    <th>Tên Hàng</th>
                                                    <th>ID</th>
                                                    <th>Đơn Vị</th>
                                                    <th>Số Lượng</th>
                                                    <th>Ghi Chú</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${not empty req.items}">
                                                        <c:forEach var="item" items="${req.items}">
                                                            <tr>
                                                                <td>${item.productName}</td>
                                                                <td>${item.productCode}</td>
                                                                <td>${item.unit}</td>
                                                                <td>${item.quantity}</td>
                                                                <td>${item.note}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="5">Không có dữ liệu chi tiết</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tbody>
                                        </table>
                                        <c:if test="${req.status == 'pending'}">
                                            <div class="action-buttons">
                                                <c:choose>
                                                    <c:when test="${req.items.size() >= 2}">
                                                        <form action="${pageContext.request.contextPath}/approvepurchaserequest" method="post" style="display:inline;">
                                                            <input type="hidden" name="requestId" value="${req.id}">
                                                            <input type="hidden" name="action" value="approve-all">
                                                            <button type="submit" class="approve-all-btn" onclick="return confirm('Bạn có chắc muốn phê duyệt hết sản phẩm trong yêu cầu này?')">Phê duyệt hết</button>
                                                        </form>
                                                        <form action="${pageContext.request.contextPath}/approvepurchaserequest" method="post" style="display:inline;">
                                                            <input type="hidden" name="requestId" value="${req.id}">
                                                            <input type="hidden" name="action" value="reject-all">
                                                            <button type="submit" class="reject-all-btn" onclick="return confirm('Bạn có chắc muốn từ chối hết sản phẩm trong yêu cầu này?')">Từ chối hết</button>
                                                        </form>
                                                    </c:when>
                                                    <c:when test="${req.items.size() == 1}">
                                                        <form action="${pageContext.request.contextPath}/approvepurchaserequest" method="post" style="display:inline;">
                                                            <input type="hidden" name="requestId" value="${req.id}">
                                                            <input type="hidden" name="action" value="approve">
                                                            <button type="submit" class="approve-btn" onclick="return confirm('Bạn có chắc muốn phê duyệt yêu cầu này?')">Phê duyệt</button>
                                                        </form>
                                                        <form action="${pageContext.request.contextPath}/approvepurchaserequest" method="post" style="display:inline;">
                                                            <input type="hidden" name="requestId" value="${req.id}">
                                                            <input type="hidden" name="action" value="reject">
                                                            <button type="submit" class="reject-btn" onclick="return confirm('Bạn có chắc muốn từ chối yêu cầu này?')">Từ chối</button>
                                                        </form>
                                                    </c:when>
                                                </c:choose>
                                            </div>
                                        </c:if>
                                        <c:if test="${req.status != 'pending'}">
                                            <div class="status-message">Bạn đã duyệt yêu cầu này</div>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7">Không có yêu cầu nào trong khoảng thời gian hoặc trạng thái này.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
            <div class="pagination">
                <c:set var="currentPage" value="${empty param.index ? 1 : param.index}" />
                <c:set var="endPage" value="${requestScope.endPage}" />
                <c:set var="filterParams" value="" />
                <c:if test="${not empty param.startDate}">
                    <c:set var="filterParams" value="${filterParams}&startDate=${param.startDate}" />
                </c:if>
                <c:if test="${not empty param.endDate}">
                    <c:set var="filterParams" value="${filterParams}&endDate=${param.endDate}" />
                </c:if>
                <c:if test="${not empty param.statusFilter}">
                    <c:set var="filterParams" value="${filterParams}&statusFilter=${param.statusFilter}" />
                </c:if>
                <c:if test="${not empty param.requestIdFilter}">
                    <c:set var="filterParams" value="${filterParams}&requestIdFilter=${param.requestIdFilter}" />
                </c:if>
                <c:choose>
                    <c:when test="${currentPage <= 1}">
                        <button disabled>
                            <span class="material-icons">chevron_left</span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="approvepurchaserequest?index=${currentPage - 1}${filterParams}" class="pagination-button">
                            <button>
                                <span class="material-icons">chevron_left</span>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <span class="current-page">${currentPage}</span>
                <c:choose>
                    <c:when test="${currentPage >= endPage}">
                        <button disabled>
                            <span class="material-icons">chevron_right</span>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <a href="approvepurchaserequest?index=${currentPage + 1}${filterParams}" class="pagination-button">
                            <button>
                                <span class="material-icons">chevron_right</span>
                            </button>
                        </a>
                    </c:otherwise>
                </c:choose>
                <div class="total-pages">Tổng ${endPage} trang</div>
            </div>
            <div class="footer">
                <a href="Admin.jsp" class="btn-secondary">
                    <span class="material-icons back-btn-icon">arrow_back</span> Quay Lại Trang Chủ
                </a>
            </div>
            </div>
        </div>
        <script>
            function toggleDetails(button) {
                const row = button.closest('tr').nextElementSibling;
                if (row.classList.contains('detail-row')) {
                    const isVisible = row.style.display === 'table-row';
                    row.style.display = isVisible ? 'none' : 'table-row';

                    // Cập nhật icon và text
                    const icon = button.querySelector('.material-icons');
                    const text = button.querySelector('span:last-child');
                    if (isVisible) {
                        icon.textContent = 'visibility';
                        text.textContent = 'Chi tiết';
                    } else {
                        icon.textContent = 'visibility_off';
                        text.textContent = 'Ẩn';
                    }
                }
            }

            function clearFilters() {
                // Xóa tất cả giá trị trong form
                document.getElementById('startDate').value = '';
                document.getElementById('endDate').value = '';
                document.getElementById('statusFilter').value = '';
                document.getElementById('requestIdFilter').value = '';

                // Gửi request để hiển thị tất cả yêu cầu
                window.location.href = 'approvepurchaserequest';
            }
        </script>
    </body>
</html>