<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Danh sách danh mục cha</title>
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

      .page-header {
          text-align: center;
          margin-bottom: 30px;
          padding-bottom: 20px;
          border-bottom: 2px solid #eee;
      }

      .page-title {
          font-size: 2rem;
          color: #333;
          margin-bottom: 10px;
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
          float: right;
          background: none;
          border: none;
          font-size: 20px;
          cursor: pointer;
      }

      /* Toolbar */
      .toolbar {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 25px;
          gap: 15px;
      }

      .search-form {
          display: flex;
          gap: 10px;
      }

      .form-input {
          padding: 10px;
          border: 1px solid #ddd;
          border-radius: 4px;
          font-size: 14px;
          width: 250px;
      }

      .btn {
          padding: 10px 20px;
          border: none;
          border-radius: 4px;
          font-size: 14px;
          cursor: pointer;
          text-decoration: none;
          display: inline-block;
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
      }

      .btn-sm {
          padding: 8px 15px;
          font-size: 13px;
      }

      /* Table Styles */
      .table-container {
          border: 1px solid #ddd;
          border-radius: 5px;
          overflow: hidden;
      }

      .table {
          width: 100%;
          border-collapse: collapse;
      }

      .table th {
          background: #f8f9fa;
          padding: 15px;
          text-align: left;
          font-weight: bold;
          border-bottom: 2px solid #dee2e6;
      }

      .table th a {
          color: #333;
          text-decoration: none;
      }

      .table td {
          padding: 12px 15px;
          border-bottom: 1px solid #dee2e6;
      }

      .table tbody tr:hover {
          background: #f5f5f5;
      }

      /* Badge Styles */
      .badge {
          padding: 5px 10px;
          border-radius: 15px;
          font-size: 12px;
          font-weight: bold;
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
      }

      .text-muted {
          color: #6c757d;
      }

      /* Action Buttons */
      .action-buttons {
          display: flex;
          gap: 8px;
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

      /* Responsive */
      @media (max-width: 768px) {
          .container {
              padding: 15px;
          }
          
          .toolbar {
              flex-direction: column;
              align-items: stretch;
          }
          
          .nav-buttons {
              flex-direction: column;
          }
          
          .table-container {
              overflow-x: auto;
          }
          
          .action-buttons {
              flex-direction: column;
          }
      }
  </style>
</head>
<body>
  <div class="container">
      <div class="page-header">
          <h1 class="page-title">Quản lý danh mục cha</h1>
      </div>

      <!-- Navigation Buttons -->
      <div class="nav-buttons">
          <a href="${pageContext.request.contextPath}/category/list" class="btn btn-info">← Quay lại danh mục</a>
      </div>

      <!-- Thông báo -->
      <c:if test="${not empty message}">
          <div class="alert alert-success">
              ${message}
              <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
          </div>
      </c:if>

      <c:if test="${not empty error}">
          <div class="alert alert-danger">
              ${error}
              <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
          </div>
      </c:if>

      <!-- Thanh công cụ -->
      <div class="toolbar">
          <a href="${pageContext.request.contextPath}/category-parent/create" class="btn btn-primary">Thêm danh mục cha</a>
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
                              <th>Số danh mục con</th>
                              <th>Thao tác</th>
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
                                      <div class="action-buttons">
                                          <a href="${pageContext.request.contextPath}/category-parent/edit?id=${category.id}" 
                                             class="btn btn-warning btn-sm">Sửa</a>
                                          <button onclick="toggleStatus(${category.id})" 
                                                  class="btn btn-secondary btn-sm">Đổi trạng thái</button>
                                          <a href="${pageContext.request.contextPath}/category-parent/delete?id=${category.id}" 
                                             class="btn btn-danger btn-sm" 
                                             onclick="return confirm('Bạn có chắc muốn xóa?')">Xóa</a>
                                      </div>
                                  </td>
                              </tr>
                          </c:forEach>
                      </tbody>
                  </table>
              </c:when>
              <c:otherwise>
                  <div class="empty-state">
                      <h3>Không có danh mục cha nào</h3>
                      <p>Hãy thêm danh mục cha đầu tiên của bạn</p>
                      <a href="${pageContext.request.contextPath}/category-parent/create" class="btn btn-primary">Thêm danh mục cha</a>
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

      <!-- Thống kê -->
      <div class="stats">
          Hiển thị ${(currentPage-1)*pageSize + 1} - ${currentPage*pageSize > totalCategoryParents ? totalCategoryParents : currentPage*pageSize} 
          trong tổng số ${totalCategoryParents} danh mục cha
      </div>
  </div>

  <script>
  function toggleStatus(id) {
    fetch('${pageContext.request.contextPath}/category-parent/toggle-status?id=' + id, {
        method: 'GET'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            const statusElement = document.getElementById('status-' + id);
            statusElement.textContent = data.newStatus;
            statusElement.className = 'badge ' + (data.statusClass === 'badge-success' ? 'badge-success' : 'badge-secondary');
        } else {
            alert('Lỗi: ' + data.message);
        }
    })
    .catch(error => {
        alert('Có lỗi xảy ra khi cập nhật trạng thái');
    });
  }
  </script>
</body>
</html>