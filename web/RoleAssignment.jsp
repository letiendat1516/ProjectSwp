<%-- 
  Document   : RoleAssignment
  Created on : 31 thg 5, 2025, 16:29:22
  Author     : phucn
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.Users" %>

<%@page import="model.Users"%>
<%
  Users user = (Users) session.getAttribute("user");
  if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
      response.sendRedirect("login.jsp");
      return;
  }
%>

<%
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
  response.setHeader("Pragma", "no-cache"); // HTTP 1.0
  response.setDateHeader("Expires", 0); // Proxies
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý phân quyền</title>
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
          align-items: center;
      }

      .form-input, .form-select {
          padding: 10px;
          border: 1px solid #ddd;
          border-radius: 4px;
          font-size: 14px;
          width: 200px;
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

      .badge-warning {
          background: #ffc107;
          color: #212529;
      }

      .badge-danger {
          background: #dc3545;
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

      /* User Info Header */
      .user-info-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 20px;
          padding: 15px;
          background: #f8f9fa;
          border-radius: 5px;
      }

      .user-welcome {
          font-weight: bold;
          color: #333;
      }

      .logout-btn {
          background: #dc3545;
          color: white;
          padding: 8px 16px;
          border: none;
          border-radius: 4px;
          text-decoration: none;
          font-size: 14px;
      }

      .logout-btn:hover {
          background: #c82333;
      }

      /* Filter Section */
      .filter-section {
          background: #f8f9fa;
          padding: 20px;
          border-radius: 5px;
          margin-bottom: 20px;
      }

      .filter-title {
          font-weight: bold;
          margin-bottom: 15px;
          color: #333;
      }

      .filter-row {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
          gap: 15px;
          align-items: end;
      }

      .filter-group {
          display: flex;
          flex-direction: column;
          gap: 5px;
      }

      .filter-label {
          font-size: 14px;
          font-weight: bold;
          color: #555;
      }

      /* Role Assignment Form */
      .role-form {
          display: flex;
          gap: 10px;
          align-items: center;
      }

      .role-form select {
          padding: 8px;
          border: 1px solid #ddd;
          border-radius: 4px;
          font-size: 13px;
          min-width: 150px;
      }

      /* Empty State */
      .empty-state {
          text-align: center;
          padding: 50px;
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

          .filter-row {
              grid-template-columns: 1fr;
          }

          .user-info-header {
              flex-direction: column;
              gap: 10px;
          }

          .role-form {
              flex-direction: column;
              align-items: stretch;
          }
      }
  </style>
</head>
<body>
  <div class="container">
      <!-- User Info Header -->

      <div class="page-header">
          <h1 class="page-title">Quản lý phân quyền</h1>
      </div>

      <!-- Navigation Buttons -->
      <div class="nav-buttons">
          <a href="/ProjectWarehouse/Admin.jsp" class="btn btn-info">← Quay lại Dashboard</a>
          <a href="admin" class="btn btn-primary">Quản lý người dùng</a>
      </div>

      <!-- Thông báo -->
      <c:if test="${not empty sessionScope.message}">
          <div class="alert alert-success">
              ${sessionScope.message}
              <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
              <c:remove var="message" scope="session"/>
          </div>
      </c:if>

      <c:if test="${not empty param.error}">
          <div class="alert alert-danger">
              <c:choose>
                  <c:when test="${param.error eq 'invalid_role'}">Vai trò không hợp lệ!</c:when>
                  <c:when test="${param.error eq 'user_not_found'}">Không tìm thấy người dùng!</c:when>
                  <c:when test="${param.error eq 'permission_denied'}">Không có quyền thực hiện!</c:when>
                  <c:otherwise>${param.error}</c:otherwise>
              </c:choose>
              <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
          </div>
      </c:if>

      <!-- Filter Section -->
      <div class="filter-section">
          <div class="filter-title">Bộ lọc tìm kiếm</div>
          <form action="userfilter" method="get">
              <div class="filter-row">
                  <div class="filter-group">
                      <label class="filter-label">Vai trò hiện tại</label>
                      <select name="role" class="form-select">
                          <option value="all" ${param.role eq 'all' ? 'selected' : ''}>Tất cả vai trò</option>
                          <option value="2" ${param.role eq '2' ? 'selected' : ''}>Warehouse Staff</option>
                          <option value="3" ${param.role eq '3' ? 'selected' : ''}>Company Employee</option>
                          <option value="4" ${param.role eq '4' ? 'selected' : ''}>Company Director</option>
                      </select>
                  </div>

                  <div class="filter-group">
                      <label class="filter-label">Trạng thái</label>
                      <select name="status" class="form-select">
                          <option value="">Tất cả trạng thái</option>
                          <option value="active" ${param.status eq 'active' ? 'selected' : ''}>Hoạt động</option>
                          <option value="inactive" ${param.status eq 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                      </select>
                  </div>

                  <div class="filter-group">
                      <label class="filter-label">Từ khóa</label>
                      <input type="text" name="keyword" class="form-input" 
                             placeholder="Tìm theo tên, username, email..." value="${param.keyword}">
                  </div>

                  <div class="filter-group">
                      <input type="hidden" name="page" value="roleassignment" />
                      <button type="submit" class="btn btn-secondary">Tìm kiếm</button>
                  </div>
              </div>
          </form>
      </div>

      <!-- Thanh công cụ -->
      <div class="toolbar">
          <div class="search-form">
              <span class="text-muted">
                  <c:if test="${not empty userList}">
                      Tổng số: <strong>${userList.size()}</strong> người dùng
                  </c:if>
              </span>
          </div>
      </div>

      <!-- Bảng danh sách -->
      <div class="table-container">
          <c:choose>
              <c:when test="${not empty userList}">
                  <table class="table">
                      <thead>
                          <tr>
                              <th>ID</th>
                              <th>Username</th>
                              <th>Họ và tên</th>
                              <th>Email</th>
                              <th>Vai trò hiện tại</th>
                              <th>Phân quyền mới</th>
                          </tr>
                      </thead>
                      <tbody>
                          <c:forEach var="user" items="${userList}">
                              <tr>
                                  <td><strong>#${user.id}</strong></td>
                                  <td><strong>${user.username}</strong></td>
                                  <td>${user.fullname}</td>
                                  <td>${user.email}</td>
                                  <td>
                                      <c:choose>
                                          <c:when test="${user.roleName == 'Admin'}">
                                              <span class="badge badge-danger">${user.roleName}</span>
                                          </c:when>
                                          <c:when test="${user.roleName == 'Warehouse Staff'}">
                                              <span class="badge badge-success">Warehouse Staff</span>
                                          </c:when>
                                          <c:when test="${user.roleName == 'Company Employee'}">
                                              <span class="badge badge-info">Company Employee</span>
                                          </c:when>
                                          <c:when test="${user.roleName == 'Company Director'}">
                                              <span class="badge badge-warning">Company Director</span>
                                          </c:when>
                                          <c:otherwise>
                                              <span class="badge badge-secondary">${user.roleName}</span>
                                          </c:otherwise>
                                      </c:choose>
                                  </td>
                                  <td>
                                      <c:choose>
                                          <c:when test="${user.roleName == 'Admin'}">
                                              <span class="text-muted">Không thể thay đổi</span>
                                          </c:when>
                                          <c:otherwise>
                                              <form action="roleAssignment" method="post" class="role-form">
                                                  <input type="hidden" name="userId" value="${user.id}"/>
                                                  <select name="role">                                           
                                                      <option value="2" ${user.roleName == 'Warehouse Staff' ? 'selected' : ''}>Warehouse Staff</option>
                                                      <option value="3" ${user.roleName == 'Company Employee' ? 'selected' : ''}>Company Employee</option>
                                                      <option value="4" ${user.roleName == 'Company Director' ? 'selected' : ''}>Company Director</option>
                                                  </select>
                                                  <button type="submit" class="btn btn-primary btn-sm" 
                                                          onclick="return confirmAction('Bạn có chắc muốn thay đổi quyền của người dùng này?')">
                                                      Phân quyền
                                                  </button>
                                              </form>
                                          </c:otherwise>
                                      </c:choose>
                                  </td>                           
                              </tr>
                          </c:forEach>
                      </tbody>
                  </table>
              </c:when>
              <c:otherwise>
                  <div class="empty-state">
                      <h3>Không có người dùng nào</h3>
                      <p>Không tìm thấy người dùng phù hợp với bộ lọc</p>
                      <a href="admin" class="btn btn-primary">Quản lý người dùng</a>
                  </div>
              </c:otherwise>
          </c:choose>
      </div>
  </div>

  <script>
      // Preserve filter values after form submission
      document.addEventListener('DOMContentLoaded', function() {
          // Auto-hide alerts after 5 seconds
          const alerts = document.querySelectorAll('.alert');
          alerts.forEach(alert => {
              setTimeout(() => {
                  alert.style.display = 'none';
              }, 5000);
          });

          // Add search on Enter key
          const keywordInput = document.querySelector('input[name="keyword"]');
          if (keywordInput) {
              keywordInput.addEventListener('keypress', function(e) {
                  if (e.key === 'Enter') {
                      e.preventDefault();
                      this.form.submit();
                  }
              });
          }
      });

      // Confirm before role assignment
      function confirmAction(message) {
          return confirm(message);
      }

      // Auto-submit form when role changes
      document.addEventListener('change', function(e) {
          if (e.target.name === 'role' && e.target.form.action.includes('roleAssignment')) {
              // Optional: Auto-submit when role changes
              // e.target.form.submit();
          }
      });
  </script>
</body>
</html>