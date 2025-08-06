<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.ExportRequest"%>
<%@page import="model.ExportRequestItem"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <!-- Material Icons -->
      <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
      <title>Lịch Sử Yêu Cầu Xuất Kho</title>
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
          .status-rejected {
              background-color: #f8d7da !important;
              border-left: 4px solid #dc3545 !important;
              color: #721c24 !important;
              font-weight: bold !important;
          }
          .status-approved {
              background-color: #d4edda !important;
              border-left: 4px solid #28a745 !important;
              color: #155724 !important;
              font-weight: bold !important;
          }
          .status-pending {
              background-color: #fff3cd !important;
              border-left: 4px solid #ffc107 !important;
              color: #856404 !important;
              font-weight: bold !important;
          }

          /* Điều chỉnh độ rộng cột */
          .requests-table th:nth-child(1),
          .requests-table td:nth-child(1) {
              width: 1cm;
          }
          .requests-table th:nth-child(2),
          .requests-table td:nth-child(2) {
              width: 1.5cm;
          }
          .requests-table th:nth-child(3),
          .requests-table td:nth-child(3) {
              width: 3cm;
          }
          .requests-table th:nth-child(4),
          .requests-table td:nth-child(4) {
              width: 2cm;
          }
          .requests-table th:nth-child(5),
          .requests-table td:nth-child(5) {
              width: 1.5cm;
          }
          .requests-table th:nth-child(6),
          .requests-table td:nth-child(6) {
              width: 5cm;
          }
          .requests-table th:nth-child(7),
          .requests-table td:nth-child(7) {
              width: 2cm;
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
              width: 8cm;
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

          /* Reject reason display styles */
          .reject-reason-display {
              background-color: #fff3cd;
              border: 1px solid #ffeaa7;
              padding: 10px;
              border-radius: 5px;
              margin-top: 10px;
          }
          .reject-reason-display strong {
              color: #856404;
          }
          .reject-reason-display span {
              color: #856404;
              font-style: italic;
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

          /* Message styles */
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

          /* Style cho tên người yêu cầu */
          .requester-name {
              font-weight: 600;
              color: #2c5aa0;
              text-align: left;
              padding-left: 8px;
          }

          /* Fallback styling cho Unknown User */
          .unknown-user {
              color: #dc3545;
              font-style: italic;
              font-weight: normal;
          }
      </style>
  </head>
  <body>
      <div class="layout-container">
          <jsp:include page="/include/sidebar.jsp" />
          <div class="main-content">
              <h1>LỊCH SỬ YÊU CẦU XUẤT KHO</h1>

              <!-- Hiển thị thông báo -->
              <c:if test="${not empty sessionScope.successMessage}">
                  <div class="message success">
                      ${sessionScope.successMessage}
                  </div>
                  <c:remove var="successMessage" scope="session" />
              </c:if>

              <c:if test="${not empty sessionScope.errorMessage}">
                  <div class="message error">
                      ${sessionScope.errorMessage}
                  </div>
                  <c:remove var="errorMessage" scope="session" />
              </c:if>

              <c:if test="${not empty param.message}">
                  <div class="message success">
                      ${param.message}
                  </div>
              </c:if>

              <c:if test="${not empty requestScope.errorMessage}">
                  <div class="message error">
                      ${requestScope.errorMessage}
                  </div>
              </c:if>

              <!-- Form lọc -->
              <form action="historyexportrequest" method="get" class="filter-section" id="filterForm">
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
                          <option value="approved" ${param.statusFilter == 'approved' ? 'selected' : ''}>Đã duyệt</option>
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

              <!-- Bảng danh sách -->
              <table class="requests-table">
                  <thead>
                      <tr>
                          <th>STT</th>
                          <th>ID</th>
                          <th>Người Yêu Cầu</th>
                          <th>Ngày Yêu Cầu</th>
                          <th>Trạng thái</th>
                          <th>Lý Do Xuất Kho</th>
                          <th>Hành Động</th>
                      </tr>
                  </thead>
                  <tbody id="requestsTableBody">
                      <c:choose>
                          <c:when test="${not empty exportRequests}">
                              <c:forEach var="req" items="${exportRequests}" varStatus="loop">
                                  <c:set var="status" value="" />
                                  <c:set var="rowClass" value="" />
                                  <c:choose>
                                      <c:when test="${req.status == 'pending'}">
                                          <c:set var="status" value="Đang chờ" />
                                          <c:set var="rowClass" value="status-pending" />
                                      </c:when>
                                      <c:when test="${req.status == 'approved' || req.status == 'completed' || req.status == 'rejected' || req.status == 'partial_exported'}">
                                          <c:set var="status" value="Đã duyệt" />
                                          <c:set var="rowClass" value="status-approved" />
                                      </c:when>
                                      <c:when test="${req.status == 'rejected_request'}">
                                          <c:set var="status" value="Đã từ chối" />
                                          <c:set var="rowClass" value="status-rejected" />
                                      </c:when>
                                  </c:choose>

                                  <tr class="${rowClass}">
                                      <td>${loop.count}</td>
                                      <td>${req.id}</td>
                                      <td class="requester-name">
                                          <c:choose>
                                              <c:when test="${req.requesterName == 'Unknown User'}">
                                                  <span class="unknown-user">${req.requesterName}</span>
                                              </c:when>
                                              <c:otherwise>
                                                  <span title="User ID: ${req.userId}">${req.requesterName}</span>
                                              </c:otherwise>
                                          </c:choose>
                                      </td>
                                      <td>
                                          <c:if test="${not empty req.dayRequest}">
                                              <fmt:formatDate value="${req.dayRequest}" pattern="dd/MM/yyyy" />
                                          </c:if>
                                      </td>
                                      <td>${status}</td>
                                      <td>${req.reason}</td>
                                      <td>
                                          <div class="action-buttons-cell">
                                              <!-- Chỉ có nút xem chi tiết -->
                                              <button onclick="toggleDetails(this)" class="action-btn view-btn">
                                                  <span class="material-icons">visibility</span>
                                                  <span>Chi tiết</span>
                                              </button>
                                          </div>
                                      </td>
                                  </tr>
                                  <tr class="detail-row">
                                      <td colspan="7">
                                          <!-- Hiển thị lý do từ chối nếu có -->
                                          <c:if test="${req.status == 'rejected_request' && not empty req.rejectReason2}">
                                              <div class="reject-reason-display">
                                                  <strong>
                                                      <i class="material-icons" style="font-size: 16px; vertical-align: middle;">warning</i>
                                                      Lý do từ chối:
                                                  </strong><br>
                                                  <span>${req.rejectReason2}</span>
                                              </div>
                                          </c:if>

                                          <table class="detail-table">
                                              <thead>
                                                  <tr>
                                                      <th>Mã SP</th>
                                                      <th>Tên Sản Phẩm</th>     
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
                                                                  <td>${item.productCode}</td>
                                                                  <td>${item.productName}</td>
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
                                      </td>
                                  </tr>
                              </c:forEach>
                          </c:when>
                          <c:otherwise>
                              <tr>
                                  <td colspan="7" style="text-align: center; padding: 20px; color: #666;">
                                      <span class="material-icons" style="font-size: 48px; color: #ccc;">inventory_2</span><br>
                                      Không có yêu cầu xuất kho nào trong khoảng thời gian hoặc trạng thái này.
                                  </td>
                              </tr>
                          </c:otherwise>
                      </c:choose>
                  </tbody>
              </table>

              <!-- Pagination -->
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
                          <a href="historyexportrequest?index=${currentPage - 1}${filterParams}" class="pagination-button">
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
                          <a href="historyexportrequest?index=${currentPage + 1}${filterParams}" class="pagination-button">
                              <button>
                                  <span class="material-icons">chevron_right</span>
                              </button>
                          </a>
                      </c:otherwise>
                  </c:choose>
                  <div class="total-pages">Tổng số: ${totalRequests} mục | ${endPage} trang</div>
              </div>

              <div class="footer">
                  <a href="${pageContext.request.contextPath}/RequestForward.jsp" class="btn-secondary">
                      <span class="material-icons back-btn-icon">arrow_back</span> Quay Lại
                  </a>
              </div>
          </div>
      </div>

      <script>
          // Hàm toggle chi tiết
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

          // Hàm xóa bộ lọc
          function clearFilters() {
              document.getElementById('startDate').value = '';
              document.getElementById('endDate').value = '';
              document.getElementById('statusFilter').value = '';
              document.getElementById('requestIdFilter').value = '';
              window.location.href = 'historyexportrequest';
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
      </script>
  </body>
</html>