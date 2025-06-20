<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Thêm danh mục</title>
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
          background: #17a2b8;
          color: white;
          border-bottom: 2px solid #138496;
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

      .alert-success {
          background: #d4edda;
          color: #155724;
          border: 1px solid #c3e6cb;
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
          border-color: #17a2b8;
          box-shadow: 0 0 0 2px rgba(23,162,184,0.25);
      }

      textarea.form-control {
          resize: vertical;
          min-height: 80px;
      }

      .text-danger {
          color: #dc3545;
      }

      .text-muted {
          color: #6c757d;
          font-size: 12px;
          margin-top: 5px;
      }

      .char-counter {
          font-size: 12px;
          color: #6c757d;
          text-align: right;
          margin-top: 5px;
      }

      .char-counter.warning {
          color: #ffc107;
      }

      .char-counter.danger {
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
          background: #17a2b8;
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
          background: #e7f3ff;
          padding: 20px;
          border-radius: 5px;
          margin-bottom: 20px;
          border-left: 4px solid #17a2b8;
      }

      .info-title {
          font-weight: bold;
          margin-bottom: 10px;
          color: #0c5460;
      }

      .info-list {
          list-style: none;
          padding: 0;
      }

      .info-list li {
          padding: 5px 0;
          color: #0c5460;
      }

      .info-list li:before {
          content: "🔹 ";
          color: #17a2b8;
          font-weight: bold;
      }

      /* Form validation styles */
      .form-control.error {
          border-color: #dc3545;
          box-shadow: 0 0 0 2px rgba(220,53,69,0.25);
      }

      .form-control.success {
          border-color: #28a745;
          box-shadow: 0 0 0 2px rgba(40,167,69,0.25);
      }

      /* Preview Section */
      .preview-section {
          background: #f8f9fa;
          padding: 15px;
          border-radius: 5px;
          margin-top: 20px;
          border: 1px solid #dee2e6;
      }

      .preview-title {
          font-weight: bold;
          margin-bottom: 10px;
          color: #495057;
      }

      .preview-item {
          margin-bottom: 8px;
          font-size: 14px;
      }

      .preview-label {
          font-weight: bold;
          color: #6c757d;
      }

      .preview-value {
          color: #495057;
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

      <!-- Form thêm mới -->
      <div class="card">
          <div class="card-header">
              🏗️ Thêm danh mục mới
          </div>
          <div class="card-body">
              <!-- Thông báo lỗi -->
              <c:if test="${not empty error}">
                  <div class="alert alert-danger">
                      <span>⚠️</span>
                      ${error}
                      <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
                  </div>
              </c:if>
              
              <!-- Thông báo thành công -->
              <c:if test="${not empty success}">
                  <div class="alert alert-success">
                      <span>✅</span>
                      ${success}
                      <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
                  </div>
              </c:if>
              
              <form method="post" action="${pageContext.request.contextPath}/category-parent/create" id="categoryParentForm">
                  <div class="form-group">
                      <label for="name" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                      <input type="text" class="form-control" id="name" name="name" 
                             value="${name}" required maxlength="255" 
                             placeholder="Nhập tên danh mục...">
                        </div>
                  
                  <div class="form-group">
                      <label for="description" class="form-label">Mô tả danh mục</label>
                      <textarea class="form-control" id="description" name="description" 
                                rows="4" maxlength="1000" 
                                placeholder="Nhập mô tả chi tiết về danh mục này...">${description}</textarea>
                      </div>
                  
                  <div class="form-group">
                      <div class="form-check">
                          <input class="form-check-input" type="checkbox" id="activeFlag" 
                                 name="activeFlag" value="1" ${activeFlag eq '1' ? 'checked' : 'checked'}>
                          <label class="form-check-label" for="activeFlag">
                              ✅ Kích hoạt danh mục ngay sau khi tạo
                          </label>
                      </div>
                  </div>

                  <!-- Preview Section -->
                  <div class="preview-section" id="previewSection" style="display: none;">
                      <div class="preview-title">👁️ Xem trước thông tin</div>
                      <div class="preview-item">
                          <span class="preview-label">Tên:</span>
                          <span class="preview-value" id="previewName">-</span>
                      </div>
                      <div class="preview-item">
                          <span class="preview-label">Mô tả:</span>
                          <span class="preview-value" id="previewDesc">-</span>
                      </div>
                      <div class="preview-item">
                          <span class="preview-label">Trạng thái:</span>
                          <span class="preview-value" id="previewStatus">Kích hoạt</span>
                      </div>
                  </div>
                  
                  <div class="button-group">
                      <a href="${pageContext.request.contextPath}/category-parent/list" 
                         class="btn btn-secondary">← Hủy</a>
                      <button type="submit" class="btn btn-primary">🏗️ Thêm danh mục</button>
                  </div>
              </form>
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

          // Initialize counters
          updateCharCounter('name', 'nameCounter', 255);
          updateCharCounter('description', 'descCounter', 1000);
          updatePreview();
      });

      // Character counter function
      function updateCharCounter(inputId, counterId, maxLength) {
          const input = document.getElementById(inputId);
          const counter = document.getElementById(counterId);
          const length = input.value.length;
          
          counter.textContent = length + '/' + maxLength + ' ký tự';
          
          if (length > maxLength * 0.9) {
              counter.className = 'char-counter danger';
          } else if (length > maxLength * 0.7) {
              counter.className = 'char-counter warning';
          } else {
              counter.className = 'char-counter';
          }
      }

      // Update preview
      function updatePreview() {
          const name = document.getElementById('name').value.trim();
          const desc = document.getElementById('description').value.trim();
          const active = document.getElementById('activeFlag').checked;
          
          document.getElementById('previewName').textContent = name || '-';
          document.getElementById('previewDesc').textContent = desc || '-';
          document.getElementById('previewStatus').textContent = active ? 'Kích hoạt' : 'Không kích hoạt';
          
          // Show/hide preview section
          const previewSection = document.getElementById('previewSection');
          if (name) {
              previewSection.style.display = 'block';
          } else {
              previewSection.style.display = 'none';
          }
      }

      // Event listeners
      document.getElementById('name').addEventListener('input', function() {
          updateCharCounter('name', 'nameCounter', 255);
          updatePreview();
          
          // Validation styling
          this.classList.remove('error', 'success');
          if (this.value.trim() && this.value.length <= 255) {
              this.classList.add('success');
          } else if (this.value.length > 255) {
              this.classList.add('error');
          }
      });

      document.getElementById('description').addEventListener('input', function() {
          updateCharCounter('description', 'descCounter', 1000);
          updatePreview();
          
          // Validation styling
          this.classList.remove('error', 'success');
          if (this.value.length <= 1000) {
              this.classList.add('success');
          } else {
              this.classList.add('error');
          }
      });

      document.getElementById('activeFlag').addEventListener('change', function() {
          updatePreview();
      });

      // Form validation
      document.getElementById('categoryParentForm').addEventListener('submit', function(e) {
          const nameInput = document.getElementById('name');
          const descInput = document.getElementById('description');
          const name = nameInput.value.trim();
          
          // Reset styles
          nameInput.classList.remove('error', 'success');
          descInput.classList.remove('error', 'success');
          
          let hasError = false;
          
          if (!name) {
              nameInput.classList.add('error');
              alert('⚠️ Vui lòng nhập tên danh mục!');
              nameInput.focus();
              hasError = true;
          } else if (name.length > 255) {
              nameInput.classList.add('error');
              alert('⚠️ Tên danh mục không được vượt quá 255 ký tự!');
              nameInput.focus();
              hasError = true;
          }
          
          if (descInput.value.length > 1000) {
              descInput.classList.add('error');
              alert('⚠️ Mô tả không được vượt quá 1000 ký tự!');
              descInput.focus();
              hasError = true;
          }
          
          if (hasError) {
              e.preventDefault();
              return;
          }
          
          nameInput.classList.add('success');
          if (descInput.value.trim()) {
              descInput.classList.add('success');
          }
      });
  </script>
</body>
</html>