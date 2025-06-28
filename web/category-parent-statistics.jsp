<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%@page import="model.Users"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Date formatter for LocalDateTime
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    request.setAttribute("dateFormatter", formatter);
    
    // Get current date for filter defaults
    LocalDate currentDate = LocalDate.now(); // This will be 2025-06-28
    LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
    DateTimeFormatter filterFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    request.setAttribute("filterStartDate", firstDayOfMonth.format(filterFormatter));
    request.setAttribute("filterEndDate", currentDate.format(filterFormatter));
%>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thống kê danh mục</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: Arial, sans-serif;
                background: #f5f5f5;
                color: #333;
            }

            .layout-container {
                display: flex;
                min-height: 100vh;
            }

            .main-content {
                flex: 1;
                padding: 20px;
            }

            .header {
                background: white;
                padding: 20px;
                margin-bottom: 20px;
                border-radius: 5px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .page-title {
                color: #333;
                font-size: 24px;
            }

            .header-user {
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .logout-btn {
                background: red;
                color: white;
                padding: 8px 16px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 14px;
            }

            .nav-buttons {
                margin-bottom: 20px;
            }

            .btn {
                padding: 10px 20px;
                border-radius: 4px;
                text-decoration: none;
                margin-right: 10px;
                display: inline-block;
                border: none;
                cursor: pointer;
            }

            .btn-info {
                background: #17a2b8;
                color: white;
            }

            .btn-success {
                background: #28a745;
                color: white;
            }

            .btn-primary {
                background: #007bff;
                color: white;
            }

            /* Stats Cards */
            .stats-container {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }

            .stat-card {
                background: white;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                text-align: center;
            }

            .stat-card h3 {
                font-size: 14px;
                color: #666;
                margin-bottom: 10px;
            }

            .stat-card .value {
                font-size: 32px;
                font-weight: bold;
                margin-bottom: 5px;
            }

            .stat-card .sub-text {
                font-size: 12px;
                color: #999;
            }

            .stat-card.primary .value {
                color: #007bff;
            }
            .stat-card.success .value {
                color: #28a745;
            }
            .stat-card.warning .value {
                color: #ffc107;
            }
            .stat-card.danger .value {
                color: #dc3545;
            }

            /* Charts */
            .charts-container {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }

            .chart-box {
                background: white;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .chart-box h3 {
                margin-bottom: 20px;
                color: #333;
            }

            .chart-wrapper {
                position: relative;
                height: 300px;
            }

            /* Filter */
            .filter-container {
                background: white;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                margin-bottom: 20px;
            }

            .filter-container h3 {
                margin-bottom: 15px;
            }

            .filter-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 15px;
                align-items: end;
            }

            .filter-group label {
                display: block;
                margin-bottom: 5px;
                color: #666;
                font-size: 14px;
            }

            .filter-group select,
            .filter-group input {
                width: 100%;
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }

            /* Table */
            .table-container {
                background: white;
                padding: 20px;
                border-radius: 5px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                overflow-x: auto;
                margin-bottom: 20px;
            }

            .table-container h3 {
                margin-bottom: 15px;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            th, td {
                padding: 12px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                background: #f8f9fa;
                font-weight: bold;
            }

            tr:hover {
                background: #f5f5f5;
            }

            .badge {
                padding: 4px 8px;
                border-radius: 15px;
                font-size: 12px;
                font-weight: bold;
            }

            .badge-success {
                background: #d4edda;
                color: #155724;
            }

            .badge-warning {
                background: #fff3cd;
                color: #856404;
            }

            .badge-danger {
                background: #f8d7da;
                color: #721c24;
            }

            /* Recent items */
            .recent-list {
                list-style: none;
            }

            .recent-item {
                padding: 10px;
                border-bottom: 1px solid #eee;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .recent-item:last-child {
                border-bottom: none;
            }

            .recent-date {
                font-size: 12px;
                color: #999;
            }

            /* Export buttons */
            .export-buttons {
                text-align: right;
                margin-top: 15px;
            }

            .btn-export {
                padding: 8px 16px;
                margin-left: 10px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                color: white;
            }

            .btn-excel {
                background: #107c41;
            }

            .btn-pdf {
                background: #d32f2f;
            }

            .total-row {
                font-weight: bold;
                background: #f8f9fa;
            }

            /* Loading overlay */
            .loading-overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.5);
                z-index: 9999;
                justify-content: center;
                align-items: center;
            }

            .loading-spinner {
                width: 50px;
                height: 50px;
                border: 5px solid #f3f3f3;
                border-top: 5px solid #3498db;
                border-radius: 50%;
                animation: spin 1s linear infinite;
            }

            @keyframes spin {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }
        </style>
    </head>
    <body>
        <!-- Loading overlay -->
        <div class="loading-overlay" id="loadingOverlay">
            <div class="loading-spinner"></div>
        </div>

        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
                <!-- Header -->
                <div class="header">
                    <h1 class="page-title">Thống kê danh mục</h1>
                    <div class="header-user">
                        <span><%= user.getFullname()%></span>
                        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                    </div>
                </div>

                <!-- Navigation -->
                <div class="nav-buttons">
                    <a href="${pageContext.request.contextPath}/category-parent/list" class="btn btn-info">← Quay lại</a>
                    <a href="${pageContext.request.contextPath}/category/statistics" class="btn btn-success">📊 Thống kê loại sản phẩm</a>
                </div>

                <!-- Error message if any -->
                <c:if test="${not empty errorMessage}">
                    <div style="background: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                        ${errorMessage}
                    </div>
                </c:if>

                <!-- Stats Cards -->
                <div class="stats-container">
                    <div class="stat-card primary">
                        <h3>Tổng số danh mục</h3>
                        <div class="value">${stats.totalParents}</div>
                        <div class="sub-text">${monthlyDifferenceSign}${monthlyDifference} so với tháng trước</div>
                    </div>
                    <div class="stat-card success">
                        <h3>Đang hoạt động</h3>
                        <div class="value">${stats.activeParents}</div>
                        <div class="sub-text">${activePercentage}%</div>
                    </div>
                    <div class="stat-card warning">
                        <h3>Không hoạt động</h3>
                        <div class="value">${stats.inactiveParents}</div>
                        <div class="sub-text">${inactivePercentage}%</div>
                    </div>
                    <div class="stat-card danger">
                        <h3>Danh mục nhiều loại SP nhất</h3>
                        <div class="value">${topParent.name}</div>
                        <div class="sub-text">${topParent.childCount} loại sản phẩm</div>
                    </div>
                </div>

                <!-- Charts -->
                <div class="charts-container">
                    <div class="chart-box">
                        <h3>Phân bố loại sản phẩm theo danh mục</h3>
                        <div class="chart-wrapper">
                            <canvas id="distributionChart"></canvas>
                        </div>
                    </div>
                    <div class="chart-box">
                        <h3>Top 10 danh mục có nhiều sản phẩm nhất</h3>
                        <div class="chart-wrapper">
                            <canvas id="topParentsChart"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Monthly Chart -->
                <div class="chart-box" style="margin-bottom: 20px;">
                    <h3>Biểu đồ thêm mới theo tháng (6 tháng gần nhất)</h3>
                    <div class="chart-wrapper">
                        <canvas id="monthlyChart"></canvas>
                    </div>
                </div>

                <!-- Filter -->
                <div class="filter-container">
                    <h3>Bộ lọc</h3>
                    <div class="filter-grid">
                        <div class="filter-group">
                            <label>Trạng thái</label>
                            <select id="filterStatus">
                                <option value="">Tất cả</option>
                                <option value="1">Hoạt động</option>
                                <option value="0">Không hoạt động</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>Từ ngày</label>
                            <input type="date" id="filterStartDate" value="${filterStartDate}">
                        </div>
                        <div class="filter-group">
                            <label>Đến ngày</label>
                            <input type="date" id="filterEndDate" value="${filterEndDate}">
                        </div>
                        <div class="filter-group">
                            <label>&nbsp;</label>
                            <button class="btn btn-primary" onclick="applyFilter()">Áp dụng lọc</button>
                        </div>
                    </div>
                </div>

                <!-- Table -->
                <div class="table-container">
                    <h3>Chi tiết thống kê theo danh mục</h3>
                    <table id="statsTable">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Tên danh mục</th>
                                <th>Trạng thái</th>
                                <th>Số loại SP</th>
                                <th>Hoạt động</th>
                                <th>Không hoạt động</th>
                                <th>Tổng sản phẩm</th>
                                <th>Tỷ lệ hoạt động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${parentStats}" var="stat" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${stat.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${stat.parentActive}">
                                                <span class="badge badge-success">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Không hoạt động</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${stat.totalCategories}</td>
                                    <td>${stat.activeCategories}</td>
                                    <td>${stat.inactiveCategories}</td>
                                    <td>${stat.totalProducts}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${stat.activePercentage >= 90}">
                                                <span class="badge badge-success">
                                                    <fmt:formatNumber value="${stat.activePercentage}" pattern="#.#"/>%
                                                </span>
                                            </c:when>
                                            <c:when test="${stat.activePercentage >= 70}">
                                                <span class="badge badge-warning">
                                                    <fmt:formatNumber value="${stat.activePercentage}" pattern="#.#"/>%
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">
                                                    <fmt:formatNumber value="${stat.activePercentage}" pattern="#.#"/>%
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr class="total-row">
                                <td colspan="3">Tổng cộng</td>
                                <td>${totalCategories}</td>
                                <td>${totalActiveCategories}</td>
                                <td>${totalInactiveCategories}</td>
                                <td>${totalProducts}</td>
                                <td>
                                    <span class="badge badge-success">${totalActivePercentage}%</span>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <div class="export-buttons">
                        <button class="btn-export btn-excel" onclick="exportToExcel()">📊 Xuất Excel</button>
                        <button class="btn-export btn-pdf" onclick="exportToPDF()">📄 Xuất PDF</button>
                    </div>
                </div>

                <!-- Recently Added -->
                <div class="table-container">
                    <h3>Danh mục được thêm gần đây</h3>
                    <ul class="recent-list">
                        <c:forEach items="${recentParents}" var="parent">
                            <li class="recent-item">
                                <div>
                                    <strong>${parent.name}</strong>
                                    <span style="color: #666; font-size: 14px;"> - ${parent.childCount} loại sản phẩm</span>
                                </div>
                                <div class="recent-date">
                                    <c:if test="${parent.createDate != null}">
                                        ${parent.createDate.format(dateFormatter)}
                                    </c:if>
                                </div>
                            </li>
                        </c:forEach>
                        <c:if test="${empty recentParents}">
                            <li class="recent-item">
                                <span style="color: #999;">Chưa có danh mục nào được thêm gần đây</span>
                            </li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </div>

        <script>
            // Parse JSON data from server
            const distributionData = ${empty distributionJson ? '{}' : distributionJson};
            const topParentsData = ${empty topParentsJson ? '[]' : topParentsJson};
            const monthlyData = ${empty monthlyStatsJson ? '[]' : monthlyStatsJson};

            // Chart 1: Distribution pie chart
            const ctx1 = document.getElementById('distributionChart').getContext('2d');
            const labels = Object.keys(distributionData);
            const values = Object.values(distributionData);

            // Only create chart if there's data
            if (labels.length > 0) {
                new Chart(ctx1, {
                    type: 'pie',
                    data: {
                        labels: labels,
                        datasets: [{
                                data: values,
                                backgroundColor: [
                                    '#007bff',
                                    '#28a745',
                                    '#ffc107',
                                    '#dc3545',
                                    '#17a2b8',
                                    '#6610f2',
                                    '#e83e8c',
                                    '#fd7e14',
                                    '#20c997',
                                    '#6f42c1'
                                ]
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'bottom',
                            },
                            tooltip: {
                                callbacks: {
                                    label: function (context) {
                                        let label = context.label || '';
                                        if (label) {
                                            label += ': ';
                                        }
                                        label += context.parsed + ' loại sản phẩm';
                                        return label;
                                    }
                                }
                            }
                        }
                    }
                });
            } else {
                ctx1.font = '16px Arial';
                ctx1.fillStyle = '#999';
                ctx1.textAlign = 'center';
                ctx1.fillText('Không có dữ liệu', ctx1.canvas.width / 2, ctx1.canvas.height / 2);
            }

            // Chart 2: Top parents bar chart
            const ctx2 = document.getElementById('topParentsChart').getContext('2d');
            const parentNames = topParentsData.map(item => item.name);
            const productCounts = topParentsData.map(item => item.productCount);

            if (parentNames.length > 0) {
                new Chart(ctx2, {
                    type: 'bar',
                    data: {
                        labels: parentNames,
                        datasets: [{
                                label: 'Số lượng sản phẩm',
                                data: productCounts,
                                backgroundColor: '#007bff'
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    stepSize: 1
                                }
                            }
                        }
                    }
                });
            } else {
                ctx2.font = '16px Arial';
                ctx2.fillStyle = '#999';
                ctx2.textAlign = 'center';
                ctx2.fillText('Không có dữ liệu', ctx2.canvas.width / 2, ctx2.canvas.height / 2);
            }

            // Chart 3: Monthly trend
            const ctx3 = document.getElementById('monthlyChart').getContext('2d');
            const months = monthlyData.map(item => {
                const [year, month] = item.month.split('-');
                return 'T' + month + '/' + year;
            });
            const newParents = monthlyData.map(item => item.newParents);

            if (months.length > 0) {
                new Chart(ctx3, {
                    type: 'line',
                    data: {
                        labels: months,
                        datasets: [{
                                label: 'Danh mục mới',
                                data: newParents,
                                borderColor: '#007bff',
                                backgroundColor: 'rgba(0, 123, 255, 0.1)',
                                tension: 0.1,
                                fill: true
                            }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    stepSize: 1
                                }
                            }
                        },
                        plugins: {
                            tooltip: {
                                callbacks: {
                                    label: function (context) {
                                        return 'Danh mục mới: ' + context.parsed.y;
                                    }
                                }
                            }
                        }
                    }
                });
            } else {
                ctx3.font = '16px Arial';
                ctx3.fillStyle = '#999';
                ctx3.textAlign = 'center';
                ctx3.fillText('Không có dữ liệu', ctx3.canvas.width / 2, ctx3.canvas.height / 2);
            }

            // Filter function
            function applyFilter() {
                const status = document.getElementById('filterStatus').value;
                const startDate = document.getElementById('filterStartDate').value;
                const endDate = document.getElementById('filterEndDate').value;

                // Validate dates
                if (!startDate || !endDate) {
                    alert('Vui lòng chọn khoảng thời gian');
                    return;
                }

                if (new Date(startDate) > new Date(endDate)) {
                    alert('Ngày bắt đầu phải trước ngày kết thúc');
                    return;
                }

                // Show loading
                document.getElementById('loadingOverlay').style.display = 'flex';

                // Create form data
                const formData = new FormData();
                formData.append('action', 'filter');
                formData.append('status', status);
                formData.append('startDate', startDate);
                formData.append('endDate', endDate);

                // AJAX call to filter data
                fetch('${pageContext.request.contextPath}/category-parent/statistics', {
                    method: 'POST',
                    body: formData
                })
                        .then(response => {
                            // Check if response is ok
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.text(); // Get as text first
                        })
                        .then(text => {
                            document.getElementById('loadingOverlay').style.display = 'none';

                            // Try to parse JSON
                            try {
                                const data = JSON.parse(text);
                                if (data.success) {
                                    alert(data.message);
                                    // Reload page with filter parameters
                                    window.location.href = '${pageContext.request.contextPath}/category-parent/statistics?status=' + status +
                                            '&startDate=' + startDate + '&endDate=' + endDate;
                                } else {
                                    alert(data.message || 'Có lỗi xảy ra khi lọc dữ liệu');
                                }
                            } catch (e) {
                                console.error('Response text:', text);
                                alert('Lỗi: Server không trả về dữ liệu hợp lệ');
                            }
                        })
                        .catch(error => {
                            document.getElementById('loadingOverlay').style.display = 'none';
                            console.error('Error:', error);
                            alert('Lỗi khi kết nối với server: ' + error.message);
                        });
            }

            // Export to Excel function
            function exportToExcel() {
                // Get table data
                const table = document.getElementById('statsTable');
                const rows = table.querySelectorAll('tr');
                let csvContent = '';

                // Add header
                csvContent += 'Thống kê danh mục - Xuất ngày: ' + new Date().toLocaleDateString('vi-VN') + '\n\n';

                // Add table data
                rows.forEach(row => {
                    const cols = row.querySelectorAll('td, th');
                    const rowData = Array.from(cols).map(col => {
                        // Remove HTML tags and get text content
                        return '"' + col.textContent.trim().replace(/"/g, '""') + '"';
                    });
                    csvContent += rowData.join(',') + '\n';
                });

                // Create blob and download
                const blob = new Blob(['\ufeff' + csvContent], {type: 'text/csv;charset=utf-8;'});
                const link = document.createElement('a');
                const url = URL.createObjectURL(blob);
                link.setAttribute('href', url);
                link.setAttribute('download', 'thong_ke_danh_muc_' + new Date().getTime() + '.csv');
                link.style.visibility = 'hidden';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }

            // Export to PDF function
            function exportToPDF() {
                // This would require a PDF library like jsPDF
                alert('Chức năng xuất PDF đang được phát triển. Bạn có thể sử dụng chức năng in (Ctrl+P) để lưu thành PDF.');
                // Alternatively, trigger print dialog
                window.print();
            }

            // Auto refresh every 5 minutes
            setInterval(function () {
                if (confirm('Dữ liệu có thể đã cũ. Bạn có muốn làm mới không?')) {
                    location.reload();
                }
            }, 300000); // 5 minutes

            // Add print styles
            const printStyles = document.createElement('style');
            printStyles.textContent = `
                @media print {
                    .header-user, .nav-buttons, .filter-container, .export-buttons, .layout-container > :first-child {
                        display: none !important;
                    }
                    .main-content {
                        margin: 0;
                        padding: 20px;
                    }
                    .chart-wrapper {
                        page-break-inside: avoid;
                    }
                }
            `;
            document.head.appendChild(printStyles);
        </script>
    </body>
</html>