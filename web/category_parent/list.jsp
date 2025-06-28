<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<%
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
  response.setHeader("Pragma", "no-cache"); // HTTP 1.0
  response.setDateHeader("Expires", 0); // Proxies
%>
<%@page import="model.Users"%>
<%
  Users user = (Users) session.getAttribute("user");
  if (user == null || !"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName())) {
      response.sendRedirect("login.jsp");
      return;
  }
%>
<html lang="vi">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Danh sách danh mục</title>
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
              line-height: 1.6;
              padding: 20px;
          }

          .container {
              max-width: 1200px;
              margin: 0 auto;
              background: white;
              padding: 30px;
              border-radius: 8px;
              box-shadow: 0 2px 10px rgba(0,0,0,0.1);
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

          /* Header */
          .header {
              display: flex;
              justify-content: space-between;
              align-items: center;
              margin-bottom: 30px;
              padding-bottom: 15px;
              border-bottom: 2px solid #e9ecef;
          }

          .page-title {
              color: #3f51b5;
              font-size: 2rem;
              margin: 0;
              flex: 1;
          }

          .header-user {
              display: flex;
              align-items: center;
              gap: 15px;
          }

          .label {
              color: #555;
              font-weight: 500;
              font-size: 14px;
          }

          .logout-btn {
              background: #dc3545;
              color: #fff;
              border: none;
              padding: 8px 16px;
              border-radius: 4px;
              cursor: pointer;
              text-decoration: none;
              font-size: 14px;
              transition: background-color 0.3s;
          }

          .logout-btn:hover {
              background: #c82333;
          }

          /* Navigation Buttons */
          .nav-buttons {
              display: flex;
              gap: 15px;
              margin-bottom: 25px;
          }

          /* Alert Styles */
          .alert {
              padding: 15px;
              border-radius: 5px;
              margin-bottom: 20px;
              position: relative;
          }

          .alert-success {
              background: #d4edda;
              color: #155724;
              border: 1px solid #c3e6cb;
          }

          .alert-danger {
              background: #f8d7da;
              color: #721c24;
              border: 1px solid #f1aeb5;
          }

          .alert-close {
              position: absolute;
              top: 10px;
              right: 15px;
              background: none;
              border: none;
              font-size: 20px;
              cursor: pointer;
              color: inherit;
          }

          /* Toolbar */
          .toolbar {
              display: flex;
              justify-content: space-between;
              align-items: center;
              margin-bottom: 25px;
              gap: 15px;
              flex-wrap: wrap;
          }

          .search-form {
              display: flex;
              gap: 10px;
              align-items: center;
          }

          .form-input {
              padding: 10px 12px;
              border: 1px solid #ddd;
              border-radius: 4px;
              font-size: 14px;
              width: 250px;
              max-width: 100%;
          }

          .btn {
              padding: 10px 20px;
              border: none;
              border-radius: 4px;
              font-size: 14px;
              cursor: pointer;
              text-decoration: none;
              display: inline-block;
              transition: all 0.3s;
              text-align: center;
          }

          .btn-primary {
              background: #007bff;
              color: white;
          }

          .btn-secondary {
              background: #6c757d;
              color: white;
          }

          .btn-warning {
              background: #ffc107;
              color: #212529;
          }

          .btn-danger {
              background: #dc3545;
              color: white;
          }

          .btn-info {
              background: #17a2b8;
              color: white;
          }

          .btn-success {
              background: #28a745;
              color: white;
          }

          .btn:hover {
              opacity: 0.9;
              transform: translateY(-1px);
          }

          .btn-sm {
              padding: 6px 10px;
              font-size: 12px;
              min-width: 35px;
              border-radius: 3px;
          }

          .btn-sm:disabled {
              opacity: 0.6;
              cursor: not-allowed;
          }

/* Table Styles - Updated for better balance */
.table-container {
    border: 1px solid #ddd;
    border-radius: 5px;
    overflow: hidden;
    overflow-x: auto;
}

.table {
    width: 100%;
    border-collapse: collapse;
    min-width: 1100px;
}

.table th {
    background: #f8f9fa;
    padding: 12px 10px;
    text-align: left;
    font-weight: bold;
    border-bottom: 2px solid #dee2e6;
    white-space: nowrap;
}

.table th a {
    color: #333;
    text-decoration: none;
}

.table td {
    padding: 10px 8px;
    border-bottom: 1px solid #dee2e6;
    vertical-align: middle;
}

.table tbody tr:hover {
    background: #f5f5f5;
}

/* Specific column widths - UPDATED for better balance */
.table th:nth-child(1), 
.table td:nth-child(1) {
    width: 5%;
    text-align: center;
}

.table th:nth-child(2), 
.table td:nth-child(2) {
    width: 15%;
    font-weight: 500;
}

/* Mô tả - Giảm độ rộng */
.table th:nth-child(3), 
.table td:nth-child(3) {
    width: 20%;
    max-width: 200px;
    word-wrap: break-word;
    overflow-wrap: break-word;
    line-height: 1.4;
    font-size: 13px;
}

.table th:nth-child(4), 
.table td:nth-child(4) {
    width: 12%;
    text-align: center;
}

.table th:nth-child(5), 
.table td:nth-child(5) {
    width: 8%;
    text-align: center;
}

.table th:nth-child(6), 
.table td:nth-child(6) {
    width: 12%;
    text-align: center;
}

.table th:nth-child(7), 
.table td:nth-child(7) {
    width: 14%;
    text-align: center;
}

.table th:nth-child(8), 
.table td:nth-child(8) {
    width: 14%;
    text-align: center;
}

/* Date time display */
.date-time {
    font-size: 12px;
    color: #666;
    line-height: 1.3;
}

.date-time small {
    display: block;
    color: #999;
    font-size: 11px;
}

/* Badge Styles */
.badge {
    padding: 5px 10px;
    border-radius: 15px;
    font-size: 12px;
    font-weight: bold;
    display: inline-block;
    text-align: center;
    white-space: nowrap;
}

.badge-success {
    background: #28a745;
    color: white;
    min-width: 70px;
}

.badge-secondary {
    background: #6c757d;
    color: white;
    min-width: 90px;
}

.badge-info {
    background: #17a2b8;
    color: white;
    min-width: 25px;
    padding: 4px 8px;
}

/* Action Buttons - Compact */
.action-buttons {
    display: flex;
    gap: 4px;
    justify-content: center;
    flex-wrap: nowrap;
}

.btn-sm {
    padding: 5px 8px;
    font-size: 12px;
    min-width: 32px;
    border-radius: 3px;
}

/* Responsive adjustments */
@media (max-width: 1400px) {
    .table {
        min-width: 1000px;
    }
    
    .table th:nth-child(3), 
    .table td:nth-child(3) {
        max-width: 180px;
        font-size: 12px;
    }
}

@media (max-width: 1200px) {
    .table {
        min-width: 950px;
        font-size: 13px;
    }
    
    .table th, 
    .table td {
        padding: 8px 6px;
    }
    
    .date-time {
        font-size: 11px;
    }
    
    .date-time small {
        font-size: 10px;
    }
}
          /* Badge Styles */
          .badge {
              padding: 6px 12px;
              border-radius: 15px;
              font-size: 12px;
              font-weight: bold;
              display: inline-block;
              min-width: 80px;
              text-align: center;
          }

          .badge-success {
              background: #28a745;
              color: white;
          }

          .badge-secondary {
              background: #6c757d;
              color: white;
          }

          .badge-info {
              background: #17a2b8;
              color: white;
              min-width: 30px;
              padding: 4px 8px;
          }

          .text-muted {
              color: #6c757d;
          }

          /* Action Buttons - Updated */
          .action-buttons {
              display: flex;
              gap: 5px;
              justify-content: center;
              flex-wrap: wrap;
          }

          /* Pagination */
          .pagination-container {
              text-align: center;
              margin-top: 25px;
          }

          .pagination {
              display: inline-flex;
              list-style: none;
              gap: 5px;
          }

          .page-link {
              display: block;
              padding: 8px 12px;
              color: #007bff;
              text-decoration: none;
              border: 1px solid #dee2e6;
              border-radius: 4px;
          }

          .page-link:hover {
              background: #e9ecef;
          }

          .page-item.active .page-link {
              background: #007bff;
              color: white;
              border-color: #007bff;
          }

          /* Empty State */
          .empty-state {
              text-align: center;
              padding: 50px;
              color: #6c757d;
          }

          /* Stats */
          .stats {
              margin-top: 20px;
              padding: 15px;
              background: #f8f9fa;
              border-radius: 5px;
              text-align: center;
              color: #6c757d;
          }

          /* Responsive adjustments for action buttons */
          @media (max-width: 768px) {
              .container {
                  padding: 15px;
              }

              .header {
                  flex-direction: column;
                  text-align: center;
                  gap: 15px;
              }

              .toolbar {
                  flex-direction: column;
                  align-items: stretch;
              }

              .search-form {
                  justify-content: center;
              }

              .form-input {
                  width: 200px;
              }

              .nav-buttons {
                  flex-direction: column;
              }

              .table {
                  font-size: 14px;
              }
              
              .table th, 
              .table td {
                  padding: 8px 6px;
              }
              
              .table th:nth-child(3), 
              .table td:nth-child(3) {
                  max-width: 100px;
                  font-size: 13px;
              }
              
              .badge {
                  font-size: 11px;
                  padding: 4px 8px;
                  min-width: 60px;
              }
              
              .btn-sm {
                  padding: 4px 6px;
                  font-size: 11px;
                  min-width: 30px;
              }

              .action-buttons {
                  gap: 3px;
              }
          }

          @media (max-width: 1200px) {
              .table th:nth-child(3), 
              .table td:nth-child(3) {
                  max-width: 150px;
              }
          }

          @media (max-width: 992px) {
              .table th:nth-child(2), 
              .table td:nth-child(2) {
                  width: 20%;
              }
              
              .table th:nth-child(3), 
              .table td:nth-child(3) {
                  width: 25%;
                  max-width: 120px;
              }
              
              .table th:nth-child(5), 
              .table td:nth-child(5) {
                  width: 12%;
              }
          }
          /* Statistics Button Specific Styles */
.btn-success {
    background: linear-gradient(135deg, #28a745, #20c997);
    border: none;
    box-shadow: 0 2px 4px rgba(40, 167, 69, 0.3);
}

.btn-success:hover {
    background: linear-gradient(135deg, #218838, #1ea085);
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(40, 167, 69, 0.4);
}

/* Icon spacing */
.nav-buttons .btn {
    display: flex;
    align-items: center;
    gap: 8px;
}

/* Responsive cho nav buttons */
@media (max-width: 768px) {
    .nav-buttons {
        flex-direction: column;
        gap: 10px;
    }
    
    .nav-buttons .btn {
        justify-content: center;
        text-align: center;
    }
}

      </style>
  </head>
  <body>
      <div class="layout-container">
          <jsp:include page="/include/sidebar.jsp" />
          <div class="main-content">                             
              <div class="header">
                  <h1 class="page-title">Quản lý danh mục</h1>
                  <div class="header-user">
                      <label class="label"><%= user.getFullname()%></label>
                      <a href="logout" class="logout-btn">Đăng xuất</a>
                  </div>
              </div>

              <!-- Navigation Buttons -->
              <div class="nav-buttons">
                  <a href="${pageContext.request.contextPath}/category/list" class="btn btn-info">← Quay lại trang trước</a>
              </div>

              <!-- Thông báo -->
              <c:if test="${not empty successMessage}">
                  <div class="alert alert-success">
                      ${successMessage}
                      <button type="button" class="alert-close" onclick="this.parentElement.remove()">&times;</button>
                  </div>
              </c:if>

              <c:if test="${not empty errorMessage}">
                  <div class="alert alert-danger">
                      ${errorMessage}
                      <button type="button" class="alert-close" onclick="this.parentElement.remove()">&times;</button>
                  </div>
              </c:if>

              <!-- Thanh công cụ -->
              <div class="toolbar">
    <div style="display: flex; gap: 10px;">
        <a href="${pageContext.request.contextPath}/category-parent/create" class="btn btn-primary">+ Thêm danh mục</a>
        <a href="${pageContext.request.contextPath}/category_parent/statistics" class="btn btn-success">Thống kê danh mục</a>
    </div>
    <form method="get" class="search-form">
        <input type="text" name="search" class="form-input" 
               placeholder="Tìm kiếm..." value="${searchKeyword}">
        <button type="submit" class="btn btn-secondary">Tìm kiếm</button>
    </form>
</div>

              <!-- Bảng danh sách -->
              <div class="table-container">
                  <c:choose>
                      <c:when test="${not empty categoryParents}">
                          <table class="table">
                              <thead>
                                  <tr>
                                      <th>
                                          <a href="?sortField=id&sortDir=${sortField eq 'id' ? reverseSortDir : 'asc'}&search=${searchKeyword}">
                                              ID ${sortField eq 'id' ? (sortDir eq 'asc' ? '↑' : '↓') : ''}
                                          </a>
                                      </th>
                                      <th>
                                          <a href="?sortField=name&sortDir=${sortField eq 'name' ? reverseSortDir : 'asc'}&search=${searchKeyword}">
                                              Tên ${sortField eq 'name' ? (sortDir eq 'asc' ? '↑' : '↓') : ''}
                                          </a>
                                      </th>
                                      <th>Mô tả</th>
                                      <th>Trạng thái</th>
                                      <th>Số danh mục loại sản phẩm</th>
                                      <th>Thao tác</th>
                                      <th>Ngày tạo</th>
<th>Ngày cập nhật</th>
                                  </tr>
                              </thead>
                              <tbody>
                                  <c:forEach var="category" items="${categoryParents}">
                                      <tr>
                                          <td>#${category.id}</td>
                                          <td>${category.name}</td>
                                          <td>${category.description}</td>
                                          <td>
                                              <span class="badge ${category.activeFlag ? 'badge-success' : 'badge-secondary'}" 
                                                    id="status-${category.id}">
                                                  ${category.activeFlag ? 'Hoạt động' : 'Không hoạt động'}
                                              </span>
                                          </td>
                                          <td><span class="badge badge-info">${category.childCount}</span></td>
                                          <td>
    <c:if test="${not empty category.createDate}">
        ${category.createDate.toLocalDate()}
        <br><small class="text-muted">${category.createDate.toLocalTime()}</small>
    </c:if>
</td>
<td>
    <c:if test="${not empty category.updateDate}">
        ${category.updateDate.toLocalDate()}
        <br><small class="text-muted">${category.updateDate.toLocalTime()}</small>
    </c:if>
</td>

                                          <td>
                                              <div class="action-buttons">
                                                  <!-- Nút Toggle Status -->
                                                  <button onclick="toggleStatus(${category.id}, ${category.childCount})" 
                                                          class="btn btn-sm ${category.activeFlag ? 'btn-danger' : 'btn-success'}"
                                                          title="${category.activeFlag ? 'Vô hiệu hóa' : 'Kích hoạt'}"
                                                          id="toggle-btn-${category.id}">
                                                      ${category.activeFlag ? '❌' : '✅'}
                                                  </button>
                                                  
                                                  <!-- Nút Sửa -->
                                                  <a href="${pageContext.request.contextPath}/category-parent/edit?id=${category.id}" 
                                                     class="btn btn-warning btn-sm" title="Chỉnh sửa">Sửa️</a>
                                              </div>
                                          </td>
                                      </tr>
                                  </c:forEach>
                              </tbody>
                          </table>
                      </c:when>
                      <c:otherwise>
                          <div class="empty-state">
                              <h3>Không có danh mục nào</h3>
                              <p>Hãy thêm danh mục đầu tiên của bạn</p>
                              <a href="${pageContext.request.contextPath}/category-parent/create" class="btn btn-primary">Thêm danh mục</a>
                          </div>
                      </c:otherwise>
                  </c:choose>
              </div>

              <!-- Phân trang -->
              <c:if test="${totalPages > 1}">
                  <div class="pagination-container">
                      <ul class="pagination">
                          <c:if test="${currentPage > 1}">
                              <li class="page-item">
                                  <a class="page-link" href="?page=${currentPage-1}&search=${searchKeyword}&sortField=${sortField}&sortDir=${sortDir}">Trước</a>
                              </li>
                          </c:if>

                          <c:forEach begin="${startPage}" end="${endPage}" var="i">
                              <li class="page-item ${i eq currentPage ? 'active' : ''}">
                                  <a class="page-link" href="?page=${i}&search=${searchKeyword}&sortField=${sortField}&sortDir=${sortDir}">${i}</a>
                              </li>
                          </c:forEach>

                          <c:if test="${currentPage < totalPages}">
                              <li class="page-item">
                                  <a class="page-link" href="?page=${currentPage+1}&search=${searchKeyword}&sortField=${sortField}&sortDir=${sortDir}">Sau</a>
                              </li>
                          </c:if>
                      </ul>
                  </div>
              </c:if>
          </div>
      </div>

      <script>
          function toggleStatus(id, childCount) {
        // Disable button during request
        const button = document.getElementById('toggle-btn-' + id);
        const originalText = button.innerHTML;
        button.disabled = true;
        button.innerHTML = '⏳';
        
        fetch('${pageContext.request.contextPath}/category-parent/toggle-status?id=' + id, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Update status badge
                const statusElement = document.getElementById('status-' + id);
                statusElement.textContent = data.newStatus;
                statusElement.className = 'badge ' + data.statusClass;
                
                // Update button
                button.className = 'btn btn-sm ' + data.buttonClass;
                button.innerHTML = data.buttonText;
                button.title = data.newStatus === 'Hoạt động' ? 'Vô hiệu hóa' : 'Kích hoạt';
            } else {
                // Chỉ reset button về trạng thái cũ, không hiện thông báo lỗi
                button.innerHTML = originalText;
            }
            button.disabled = false;
        })
        .catch(error => {
            // Chỉ reset button, không hiện thông báo lỗi
            button.innerHTML = originalText;
            button.disabled = false;
        });
    }


          // Helper function to show alerts
          function showAlert(type, message) {
              const alertDiv = document.createElement('div');
              alertDiv.className = `alert alert-${type}`;
              alertDiv.innerHTML = `
                  ${message}
                  <button type="button" class="alert-close" onclick="this.parentElement.remove()">&times;</button>
              `;
              
              const container = document.querySelector('.main-content');
              const firstChild = container.querySelector('.header').nextElementSibling;
              container.insertBefore(alertDiv, firstChild);
              
              // Auto remove after 5 seconds
              setTimeout(() => {
                  if (alertDiv.parentNode) {
                      alertDiv.remove();
                  }
              }, 5000);
          }
      </script>
  </body>
</html>
