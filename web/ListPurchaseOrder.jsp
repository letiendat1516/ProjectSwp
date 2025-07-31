<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.PurchaseOrderInfo"%>
<%@page import="model.PurchaseOrderItems"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Material Icons -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <title>Danh sách cần báo giá</title>
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
            .view-btn {
                background: #17a2b8;
            }
            .view-btn:hover {
                background: #138496;
                transform: translateY(-1px);
            }
            .quote-btn {
                background: #28a745;
            }
            .quote-btn:hover {
                background: #218838;
                transform: translateY(-1px);
            }
            .requote-btn {
                background: #ffc107;
                color: #212529 !important;
            }
            .requote-btn:hover {
                background: #e0a800;
                transform: translateY(-1px);
            }
            .action-btn .material-icons {
                font-size: 16px;
            }
            .status-completed {
                background-color: #d4edda !important;
                border-left: 4px solid #28a745 !important;
                color: #155724 !important;
                font-weight: bold !important;
            }

            .status-quoted {
                background-color: #fff3cd !important;
                border-left: 4px solid #ffc107 !important;
                color: #856404 !important;
                font-weight: bold !important;
            }
            .status-re-quote {
                background-color: #f8d7da !important;
                border-left: 4px solid #dc3545 !important;
                color: #721c24 !important;
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
            .detail-table th:nth-child(2), .detail-table td:nth-child(2) {
                width: 10cm;
            }
            .detail-table th:nth-child(1), .detail-table td:nth-child(1) {
                width: 1.5cm;
            }
            .detail-table th:nth-child(3), .detail-table td:nth-child(3) {
                width: 1.5cm;
            }
            .detail-table th:nth-child(4), .detail-table td:nth-child(4) {
                width: 1.5cm;
            }
            .detail-table th:nth-child(5), .detail-table td:nth-child(5) {
                width: 2cm;
            }
            .detail-table th:nth-child(6), .detail-table td:nth-child(6) {
                width: 2cm;
            }
            .detail-table th:nth-child(7), .detail-table td:nth-child(7) {
                width: 6cm;
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
            .message {
                padding: 10px;
                margin-bottom: 20px;
                border-radius: 8px;
                text-align: center;
            }
            .message.success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .message.error {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
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
                <h1>DANH SÁCH CẦN BÁO GIÁ</h1>

                <!-- Hiển thị thông báo -->
                <c:if test="${not empty param.message}">
                    <div class="message success">
                        ${param.message}
                    </div>
                </c:if>

                <!-- Hiển thị thông báo lỗi -->
                <c:if test="${not empty errorMessage}">
                    <div class="message error">
                        ${errorMessage}
                    </div>
                </c:if>

                <!-- Form lọc -->
                <form action="listpurchaseorder" method="post" class="filter-section" id="filterForm">
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
                            <option value="pending_quote" ${param.statusFilter == 'pending_quote' ? 'selected' : ''}>Chờ báo giá</option>
                            <option value="quoted" ${param.statusFilter == 'quoted' ? 'selected' : ''}>Đã báo giá</option>                    
                            <option value="re-quote" ${param.statusFilter == 're-quote' ? 'selected' : ''}>Báo giá lại</option>
                            <option value="approved" ${param.statusFilter == 'approved' || param.statusFilter == 'rejected' || param.statusFilter == 'completed'||param.statusFilter == 'done' ? 'selected' : ''}>Đã hoàn thành</option>

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
                        <span class="material-icons">clear</span> Xóa lọc
                    </button>
                </form>

                <table class="requests-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>ID</th>
                            <th>Người yêu cầu</th>
                            <th>Ngày tạo PO</th>
                            <th>Trạng thái</th>
                            <th>Mục đích</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody id="requestsTableBody">
                        <c:choose>
                            <c:when test="${not empty purchaseOrders}">
                                <c:forEach var="po" items="${purchaseOrders}" varStatus="loop">
                                    <c:set var="status" value="" />
                                    <c:set var="rowClass" value="" />
                                    <c:choose>
                                        <c:when test="${po.status == 'pending_quote'}">
                                            <c:set var="status" value="Chờ báo giá" />
                                            <c:set var="rowClass" value="status-pending_quote" />
                                        </c:when>
                                        <c:when test="${po.status == 'quoted'}">
                                            <c:set var="status" value="Đã báo giá" />
                                            <c:set var="rowClass" value="status-quoted" />
                                        </c:when>
                                        <c:when test="${po.status == 're-quote'}">
                                            <c:set var="status" value="Báo giá lại" />
                                            <c:set var="rowClass" value="status-re-quote" />
                                        </c:when>
                                        <c:when test="${po.status == 'approved' || po.status == 'completed' || po.status == 'rejected'|| po.status == 'done'}">
                                            <c:set var="status" value="Đã hoàn thành" />
                                            <c:set var="rowClass" value="status-completed" />
                                        </c:when>
                                    </c:choose>
                                    <tr class="${rowClass}">
                                        <td>${(currentPage - 1) * 11 + loop.count}</td>
                                        <td>${po.id}</td>
                                        <td>${po.fullname}</td>
                                        <td>
                                            <c:if test="${not empty po.dayPurchase}">
                                                <fmt:formatDate value="${po.dayPurchase}" pattern="dd/MM/yyyy" />
                                            </c:if>
                                        </td>
                                        <td>${status}</td>
                                        <td>${po.reason}</td>
                                        <td>
                                            <div class="action-buttons-cell">
                                                <!-- Nút xem chi tiết -->
                                                <button onclick="toggleDetails(this)" class="action-btn view-btn">
                                                    <span class="material-icons">visibility</span>
                                                    <span>Chi tiết</span>
                                                </button>

                                                <!-- ✅ LOGIC HIỂN THỊ NÚT BÁO GIÁ THEO STATUS -->
                                                <c:choose>
                                                    <c:when test="${po.status == 'completed' || po.status == 'quoted' || po.status == 'approved'|| po.status == 'rejected' || po.status == 'done'}">
                                                        <!-- Đã hoàn thành/Đã báo giá/Đã duyệt: Không hiển thị nút báo giá -->
                                                    </c:when>
                                                    <c:when test="${po.status == 're-quote'}">
                                                        <!-- Báo giá lại: Form với action=update -->
                                                        <form action="requoteform" method="post" class="quote-form">
                                                            <input type="hidden" name="purchaseOrderId" value="${po.id}">
                                                            <input type="hidden" name="action" value="update">

                                                            <button type="submit" class="action-btn requote-btn">
                                                                <span class="material-icons">refresh</span>
                                                                <span>Báo giá lại</span>
                                                            </button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <!-- Chờ báo giá (pending quote): Form tạo báo giá mới -->
                                                        <form action="purchaseorderform" method="post" class="quote-form">
                                                            <input type="hidden" name="purchaseOrderId" value="${po.id}">
                                                            <input type="hidden" name="action" value="create">

                                                            <button type="submit" class="action-btn quote-btn">
                                                                <span class="material-icons">description</span>
                                                                <span>Tạo báo giá</span>
                                                            </button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr class="detail-row">
                                        <td colspan="8">
                                            <div style="margin-bottom: 10px;">
                                                <strong>Nhà cung cấp:</strong> ${empty po.supplier ? 'Chưa có' : po.supplier}<br>
                                                <strong>Địa chỉ:</strong> ${empty po.address ? 'Chưa có' : po.address}<br>
                                                <strong>SĐT:</strong> ${empty po.phone ? 'Chưa có' : po.phone}<br>
                                                <strong>Email:</strong> ${empty po.email ? 'Chưa có' : po.email}<br>

                                                <!-- ✅ THÊM phần hiển thị lý do từ chối cho đơn re-quote -->
                                                <c:if test="${po.status == 're-quote' && not empty po.rejectReason2}">
                                                    <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 10px; border-radius: 5px; margin-top: 10px;">
                                                        <strong style="color: #856404;">
                                                            <i class="material-icons" style="font-size: 16px; vertical-align: middle;">warning</i>
                                                            Lý do từ chối:
                                                        </strong><br>
                                                        <span style="color: #856404; font-style: italic;">${po.rejectReason2}</span>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <table class="detail-table">
                                                <thead>
                                                    <tr>
                                                        <th>Mã SP</th>
                                                        <th>Tên Hàng</th>

                                                        <th>Đơn Vị</th>
                                                        <th>Số Lượng</th>
                                                        <th>Đơn giá</th>
                                                        <th>Thành tiền</th>
                                                        <th>Ghi Chú</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty po.purchaseItems}">
                                                            <c:forEach var="item" items="${po.purchaseItems}">
                                                                <tr>
                                                                    <td>${item.productCode}</td>
                                                                    <td>${item.productName}</td>

                                                                    <td>${item.unit}</td>
                                                                    <td>
                                                                        <fmt:formatNumber value="${item.quantity}" pattern="#,##0.##" />
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty item.pricePerUnit}">
                                                                                <fmt:formatNumber value="${item.pricePerUnit}" pattern="#,##0" /> VNĐ
                                                                            </c:when>
                                                                            <c:otherwise>Chưa có giá</c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty item.totalPrice}">
                                                                                <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0" /> VNĐ
                                                                            </c:when>
                                                                            <c:otherwise>Chưa có giá</c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>${item.note}</td>
                                                                </tr>

                                                            </c:forEach>
                                                        <div style="margin-bottom: 10px;">
                                                            <strong>Tổng kết báo giá:</strong> ${empty po.summary ? 'Chưa có' : po.summary}<br>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr>
                                                            <td colspan="7">Không có dữ liệu chi tiết</td>
                                                        </tr>
                                                    </c:otherwise>
                                                </c:choose>
                                </tbody>
                            </table>
                            </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" style="text-align: center; padding: 20px; color: #666;">
                                <span class="material-icons" style="font-size: 48px; color: #ccc;">inbox</span><br>
                                Không có dữ liệu để hiển thị
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
                </table>

                <!-- Phân trang -->

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <!-- Nút Previous -->
                        <c:if test="${currentPage > 1}">
                            <button type="button" onclick="goToPage(${currentPage - 1})">
                                <span class="material-icons">chevron_left</span>
                                Trước
                            </button>
                        </c:if>
                        <c:if test="${currentPage <= 1}">
                            <button type="button" disabled>
                                <span class="material-icons">chevron_left</span>
                                Trước
                            </button>
                        </c:if>

                        <!-- Hiển thị trang hiện tại -->
                        <span class="current-page">Trang ${currentPage}</span>

                        <!-- Nút Next -->
                        <c:if test="${currentPage < totalPages}">
                            <button type="button" onclick="goToPage(${currentPage + 1})">
                                Sau
                                <span class="material-icons">chevron_right</span>
                            </button>
                        </c:if>
                        <c:if test="${currentPage >= totalPages}">
                            <button type="button" disabled>
                                Sau
                                <span class="material-icons">chevron_right</span>
                            </button>
                        </c:if>

                        <!-- Thông tin tổng số trang -->
                        <div class="total-pages">
                            Tổng số: ${totalRecords} mục | ${totalPages} trang
                        </div>
                    </div>
                </c:if>

                <!-- Footer -->
                <div class="footer">
                    <a href="Admin.jsp" class="btn-secondary">
                        <span class="material-icons back-btn-icon">arrow_back</span>
                        Quay về Dashboard
                    </a>
                </div>
            </div>
        </div>

        <script>
            // Hàm toggle chi tiết
            function toggleDetails(button) {
                const currentRow = button.closest('tr');
                const detailRow = currentRow.nextElementSibling;

                if (detailRow && detailRow.classList.contains('detail-row')) {
                    if (detailRow.style.display === 'none' || detailRow.style.display === '') {
                        detailRow.style.display = 'table-row';
                        button.innerHTML = '<span class="material-icons">visibility_off</span><span>Ẩn chi tiết</span>';
                    } else {
                        detailRow.style.display = 'none';
                        button.innerHTML = '<span class="material-icons">visibility</span><span>Chi tiết</span>';
                    }
                }
            }

            // Hàm xóa bộ lọc
            function clearFilters() {
                document.getElementById('startDate').value = '';
                document.getElementById('endDate').value = '';
                document.getElementById('statusFilter').value = '';
                document.getElementById('requestIdFilter').value = '';
                document.getElementById('filterForm').submit();
            }

            // Hàm chuyển trang
            function goToPage(page) {
                const form = document.getElementById('filterForm');

                // ✅ SỬA: Dùng 'page' thay vì 'index'
                let pageInput = document.querySelector('input[name="page"]');
                if (!pageInput) {
                    pageInput = document.createElement('input');
                    pageInput.type = 'hidden';
                    pageInput.name = 'page';
                    form.appendChild(pageInput);
                }
                pageInput.value = page;

                form.submit();
            }

            // Validation form
            document.getElementById('filterForm').addEventListener('submit', function (e) {
                const startDate = document.getElementById('startDate').value;
                const endDate = document.getElementById('endDate').value;

                if (startDate && endDate && startDate > endDate) {
                    e.preventDefault();
                    alert('Ngày bắt đầu không thể lớn hơn ngày kết thúc!');
                    return false;
                }
            });
            function changePageSize(size) {
                const form = document.getElementById('filterForm');

                let sizeInput = document.querySelector('input[name="size"]');
                if (!sizeInput) {
                    sizeInput = document.createElement('input');
                    sizeInput.type = 'hidden';
                    sizeInput.name = 'size';
                    form.appendChild(sizeInput);
                }
                sizeInput.value = size;
                form.submit();
            }

            // Auto-submit form khi thay đổi filter (optional)
            document.getElementById('statusFilter').addEventListener('change', function () {
                // Uncomment dòng dưới nếu muốn tự động submit khi thay đổi status
                // document.getElementById('filterForm').submit();
            });
        </script>
    </body>
</html>