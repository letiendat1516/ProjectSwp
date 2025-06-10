<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Chỉnh sửa danh mục cha</title>
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
          max-width: 800px;
          margin: 0 auto;
      }

      .card {
          background: white;
          border-radius: 8px;
          box-shadow: 0 2px 10px rgba(0,0,0,0.1);
          margin-bottom: 20px;
          overflow: hidden;
      }

      .card-header {
          padding: 20px;
          font-size: 1.5rem;
          font-weight: bold;
          background: #007bff;
          color: white;
          border-bottom: 2px solid #0056b3;
      }

      .card-body {
          padding: 30px;
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
          display: flex;
          align-items: center;
          gap: 10px;
      }

      .alert-danger {
          background: #f8d7da;
          color: #721c24;
          border: 1px solid #f1aeb5;
      }

      .alert-info {
          background: #d1ecf1;
          color: #0c5460;
          border: 1px solid #bee5eb;
      }

      .alert-close {
          margin-left: auto;
          background: none;
          border: none;
          font-size: 20px;
          cursor: pointer;
      }

      /* Form Styles */
      .form-group {
          margin-bottom: 25px;
      }

      .form-label {
          display: block;
          margin-bottom: 8px;
          font-weight: bold;
          font-size: 14px;
      }

      .form-control {
          width: 100%;
          padding: 12px;
          border: 1px solid #ddd;
          border-radius: 4px;
          font-size: 14px;
          background: white;
      }

      .form-control:focus {
          outline: none;
          border-color: #007bff;
          box-shadow: 0 0 0 2px rgba(0,123,255,0.25);
      }

      .form-control[readonly] {
          background: #f8f9fa;
          color: #6c757d;
      }

      textarea.form-control {
          resize: vertical;
          min-height: 80px;
      }

      .text-danger {
          color: #dc3545;
      }

      /* Checkbox Styles */
      .form-check {
          display: flex;
          align-items: center;
          gap: 10px;
          padding: 15px;
          border: 1px solid #eee;
          border-radius: 4px;
          cursor: pointer;
      }

      .form-check:hover {
          background: #f8f9fa;
      }

      .form-check-input {
          width: 18px;
          height: 18px;
          cursor: pointer;
      }

      .form-check-label {
          cursor: pointer;
          font-weight: 500;
      }

      /* Button Styles */
      .btn {
          padding: 12px 24px;
          border: none;
          border-radius: 4px;
          font-size: 14px;
          cursor: pointer;
          text-decoration: none;
          display: inline-flex;
          align-items: center;
          gap: 8px;
      }

      .btn-primary {
          background: #007bff;
          color: white;
      }

      .btn-secondary {
          background: #6c757d;
          color: white;
      }

      .btn:hover {
          opacity: 0.9;
      }

      .button-group {
          display: flex;
          justify-content: flex-end;
          gap: 15px;
          margin-top: 30px;
      }

      /* Info Section */
      .info-section {
          background: #f8f9fa;
          padding: 20px;
          border-radius: 5px;
          margin-bottom: 20px;
      }

      .info-item {
          margin-bottom: 10px;
      }

      .info-label {
          font-weight: bold;
          color: #495057;
      }

      .info-value {
          color: #6c757d;
          margin-left: 10px;
      }

      /* Badge */
      .badge {
          padding: 5px 10px;
          border-radius: 15px;
          font-size: 12px;
          font-weight: bold;
      }

      .badge-info {
          background: #17a2b8;
          color: white;
      }

      /* Icons */
      .icon {
          font-size: 16px;
      }

      /* Responsive */
      @media (max-width: 768px) {
          .container {
              padding: 10px;
          }
          
          .card-body {
              padding: 20px;
          }
          
          .button-group {
              flex-direction: column;
          }
          
          .nav-buttons {
              flex-direction: column;
          }
      }
  </style>
</head>
<body>
  <div class="container">
      
      <!-- Form chỉnh sửa -->
      <div class="card">
          <div class="card-header">
              Chỉnh sửa danh mục cha
          </div>
          <div class="card-body">
              <!-- Thông báo lỗi -->
              <c:if test="${not empty error}">
                  <div class="alert alert-danger">
                      <span class="icon">⚠️</span>
                      ${error}
                      <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
                  </div>
              </c:if>
              
              <!-- Thông tin số danh mục con -->
              <c:if test="${childCount > 0}">
                  <div class="alert alert-info">
                      <span class="icon">ℹ️</span>
                      Danh mục này có <span class="badge badge-info">${childCount}</span> danh mục con
                  </div>
              </c:if>
              
              <form method="post" action="${pageContext.request.contextPath}/category-parent/edit">
                  <input type="hidden" name="id" value="${categoryParent.id}">
                  
                  <div class="form-group">
                      <label for="id" class="form-label">ID</label>
                      <input type="text" class="form-control" id="id" value="#${categoryParent.id}" readonly>
                  </div>
                  
                  <div class="form-group">
                      <label for="name" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                      <input type="text" class="form-control" id="name" name="name" 
                             value="${categoryParent.name}" required maxlength="255">
                  </div>
                  
                  <div class="form-group">
                      <label for="description" class="form-label">Mô tả</label>
                      <textarea class="form-control" id="description" name="description" 
                                rows="4" maxlength="1000" placeholder="Nhập mô tả cho danh mục cha...">${categoryParent.description}</textarea>
                  </div>
                  
                  <div class="form-group">
                      <div class="form-check">
                          <input class="form-check-input" type="checkbox" id="activeFlag" 
                                 name="activeFlag" value="1" ${categoryParent.activeFlag ? 'checked' : ''}>
                          <label class="form-check-label" for="activeFlag">
                              ✓ Kích hoạt danh mục cha
                          </label>
                      </div>
                  </div>
                  
                  <div class="button-group">
                      <a href="${pageContext.request.contextPath}/category-parent/list" 
                         class="btn btn-secondary">← Hủy</a>
                      <button type="submit" class="btn btn-primary">💾 Cập nhật</button>
                  </div>
              </form>
          </div>
      </div>

      <!-- Thông tin bổ sung -->
      <div class="info-section">
          <h5 style="margin-bottom: 15px; color: #495057;">📋 Thông tin chi tiết</h5>
          
          <div class="info-item">
              <span class="info-label">ID danh mục cha:</span>
              <span class="info-value">#${categoryParent.id}</span>
          </div>
          
          <div class="info-item">
              <span class="info-label">Trạng thái hiện tại:</span>
              <span class="info-value">
                  <c:choose>
                      <c:when test="${categoryParent.activeFlag}">
                          <span style="color: #28a745; font-weight: bold;">✓ Đang hoạt động</span>
                      </c:when>
                      <c:otherwise>
                          <span style="color: #dc3545; font-weight: bold;">✕ Không hoạt động</span>
                      </c:otherwise>
                  </c:choose>
              </span>
          </div>
          
          <c:if test="${childCount > 0}">
              <div class="info-item">
                  <span class="info-label">Số danh mục con:</span>
                  <span class="info-value">
                      <span class="badge badge-info">${childCount}</span> danh mục
                  </span>
              </div>
          </c:if>
          
          <div style="margin-top: 15px; padding: 10px; background: #e9ecef; border-radius: 4px; font-size: 13px; color: #6c757d;">
              <strong>💡 Lưu ý:</strong> Nếu bạn thay đổi trạng thái thành "Không hoạt động", 
              tất cả danh mục con thuộc danh mục cha này sẽ bị ảnh hưởng.
          </div>
      </div>
  </div>
  
  <script>
      // Xử lý đóng alert
      document.addEventListener('DOMContentLoaded', function() {
          const closeButtons = document.querySelectorAll('.alert-close');
          closeButtons.forEach(function(button) {
              button.addEventListener('click', function() {
                  this.parentElement.style.display = 'none';
              });
          });
      });

      // Validation form
      document.querySelector('form').addEventListener('submit', function(e) {
          const name = document.getElementById('name').value.trim();
          if (!name) {
              e.preventDefault();
              alert('Vui lòng nhập tên danh mục cha!');
              document.getElementById('name').focus();
          }
      });
  </script>
</body>
</html>