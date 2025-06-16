<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cập Nhật Sản Phẩm</title>
  <style>
      body { background-color: #f8f9fa; }
      .container { max-width: 900px; }
      .card { border: none; border-radius: 15px; box-shadow: 0 0 20px rgba(0,0,0,0.1); }
      .card-header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 15px 15px 0 0 !important; }
      .btn-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border: none; }
      .btn-primary:hover { background: linear-gradient(135deg, #5a6fd8 0%, #6a4190 100%); }
      .form-label { font-weight: 600; color: #495057; }
      .required { color: #dc3545; }
  </style>
</head>
<body>
  <div class="container mt-4">
      <!-- Navigation -->
      <div class="row mb-4">
          <div class="col-12">
              <nav aria-label="breadcrumb">
                  <ol class="breadcrumb">
                      <li class="breadcrumb-item"><a href="Admin.jsp"><i class="fas fa-home"></i> Trang chủ</a></li>
                      <li class="breadcrumb-item"><a href="product-list"><i class="fas fa-box"></i> Sản phẩm</a></li>
                      <li class="breadcrumb-item active">Cập nhật sản phẩm</li>
                  </ol>
              </nav>
          </div>
      </div>      <!-- Alert Messages -->
      <c:if test="${not empty error}">
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <i class="fas fa-exclamation-triangle me-2"></i>
              <strong>Lỗi!</strong> ${error}
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
          </div>
      </c:if>

      <c:if test="${not empty success}">
          <div class="alert alert-success alert-dismissible fade show" role="alert">
              <i class="fas fa-check-circle me-2"></i>
              <strong>Thành công!</strong> ${success}
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
          </div>
      </c:if>      <!-- Debug Information (Development Only) -->
      <c:if test="${pageContext.request.serverName == 'localhost' or pageContext.request.serverName == '127.0.0.1'}">
          <div class="alert alert-warning">
              <h6><i class="fas fa-bug me-2"></i>Debug Information (Development Only)</h6>
              <p><strong>Product loaded:</strong> ${not empty product ? 'Yes - ' : 'No'}${product.name}</p>              <p><strong>Categories:</strong> ${not empty categories ? fn:length(categories) : '0'} found</p>
              <p><strong>Units:</strong> ${not empty units ? fn:length(units) : '0'} found</p>
              <p><strong>Suppliers:</strong> ${not empty suppliers ? fn:length(suppliers) : '0'} found</p>
              <p><strong>Storage Locations:</strong> ${not empty storageLocations ? fn:length(storageLocations) : '0'} found</p>
          </div>
      </c:if>

      <!-- Main Content -->
      <c:choose>
          <c:when test="${not empty product}">
              <div class="card">
                  <div class="card-header">
                      <h4 class="mb-0">
                          <i class="fas fa-edit me-2"></i>
                          Cập Nhật Sản Phẩm: ${product.name}
                      </h4>
                  </div>
                  <div class="card-body">
                      <!-- Current Product Info -->
                      <div class="alert alert-info">
                          <h6><i class="fas fa-info-circle me-2"></i>Thông tin hiện tại:</h6>
                          <div class="row">
                              <div class="col-md-6">
                                  <p><strong>ID:</strong> ${product.id}</p>
                                  <p><strong>Mã:</strong> ${product.code}</p>
                                  <p><strong>Tên:</strong> ${product.name}</p>
                              </div>
                              <div class="col-md-6">
                                  <p><strong>Giá:</strong> 
                                      <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                  </p>
                                  <p><strong>Trạng thái:</strong> 
                                      <span class="badge ${product.status == 'active' ? 'bg-success' : 'bg-secondary'}">
                                          ${product.status == 'active' ? 'Hoạt động' : 'Không hoạt động'}
                                      </span>
                                  </p>
                              </div>
                          </div>
                      </div>

                      <!-- Update Form -->
                      <form action="update-product" method="post" class="needs-validation" novalidate>
                          <input type="hidden" name="id" value="${product.id}">
                          
                          <div class="row">
                              <!-- Basic Information -->
                              <div class="col-md-6">
                                  <h6 class="text-primary mb-3"><i class="fas fa-info-circle me-2"></i>Thông tin cơ bản</h6>
                                  
                                  <div class="mb-3">
                                      <label for="name" class="form-label">Tên sản phẩm <span class="required">*</span></label>
                                      <input type="text" class="form-control" id="name" name="name" 
                                             value="${product.name}" required>
                                      <div class="invalid-feedback">Vui lòng nhập tên sản phẩm.</div>
                                  </div>

                                  <div class="mb-3">
                                      <label for="code" class="form-label">Mã sản phẩm <span class="required">*</span></label>
                                      <input type="text" class="form-control" id="code" name="code" 
                                             value="${product.code}" required>
                                      <div class="invalid-feedback">Vui lòng nhập mã sản phẩm.</div>
                                  </div>

                                  <div class="mb-3">
                                      <label for="price" class="form-label">Giá (VNĐ) <span class="required">*</span></label>
                                      <input type="number" class="form-control" id="price" name="price" 
                                             value="${product.price}" step="0.01" min="0" required>
                                      <div class="invalid-feedback">Vui lòng nhập giá hợp lệ.</div>
                                  </div>

                                  <div class="mb-3">
                                      <label for="status" class="form-label">Trạng thái</label>
                                      <select class="form-select" id="status" name="status">
                                          <option value="active" ${product.status == 'active' ? 'selected' : ''}>
                                              <i class="fas fa-check-circle"></i> Hoạt động
                                          </option>
                                          <option value="inactive" ${product.status == 'inactive' ? 'selected' : ''}>
                                              <i class="fas fa-times-circle"></i> Không hoạt động
                                          </option>
                                      </select>
                                  </div>
                              </div>

                              <!-- Categories and Relationships -->
                              <div class="col-md-6">
                                  <h6 class="text-primary mb-3"><i class="fas fa-tags me-2"></i>Phân loại & Quan hệ</h6>                                  <!-- Category Dropdown -->
                                  <div class="mb-3">
                                      <label for="categoryId" class="form-label">Danh mục</label>
                                      <select class="form-select" id="categoryId" name="categoryId">
                                          <option value="">-- Chọn danh mục --</option>
                                          <c:forEach var="category" items="${categories}">
                                              <option value="${category.id}" 
                                                      ${product.cate_id == category.id ? 'selected' : ''}>
                                                  ${category.name}
                                              </option>
                                          </c:forEach>
                                      </select>
                                  </div>                                  <!-- Unit Dropdown -->
                                  <div class="mb-3">
                                      <label for="unitId" class="form-label">Đơn vị tính</label>
                                      <select class="form-select" id="unitId" name="unitId">
                                          <option value="">-- Chọn đơn vị --</option>
                                          <c:forEach var="unit" items="${units}">
                                              <option value="${unit.id}" 
                                                      ${product.unit_id == unit.id ? 'selected' : ''}>
                                                  ${unit.name} (${unit.symbol})
                                              </option>
                                          </c:forEach>
                                      </select>
                                  </div><!-- Supplier Dropdown - FIXED -->
                                  <div class="mb-3">
                                      <label for="supplierId" class="form-label">Nhà cung cấp</label>
                                      <select class="form-select" id="supplierId" name="supplierId">
                                          <option value="">-- Chọn nhà cung cấp --</option>
                                          <c:choose>
                                              <c:when test="${not empty suppliers}">
                                                  <c:forEach var="supplier" items="${suppliers}">
                                                      <option value="${supplier.supplierID}" 
                                                              ${product.supplierId == supplier.supplierID ? 'selected' : ''}>
                                                          ${supplier.name}
                                                      </option>
                                                  </c:forEach>
                                              </c:when>
                                              <c:otherwise>
                                                  <option value="">Không có nhà cung cấp nào</option>
                                              </c:otherwise>
                                          </c:choose>
                                      </select>
                                  </div><!-- Storage Location Dropdown -->
                                  <div class="mb-3">
                                      <label for="storageLocation" class="form-label">Vị trí lưu trữ</label>
                                      <select class="form-select" id="storageLocation" name="storageLocation">
                                          <option value="">-- Chọn vị trí --</option>
                                          <c:forEach var="location" items="${storageLocations}">
                                              <option value="${location}" 
                                                      ${product.storageLocation == location ? 'selected' : ''}>
                                                  ${location}
                                              </option>
                                          </c:forEach>
                                      </select>
                                  </div>
                              </div>
                          </div>                          <!-- Description -->
                          <div class="row">
                              <div class="col-12">
                                  <h6 class="text-primary mb-3"><i class="fas fa-file-text me-2"></i>Thông tin bổ sung</h6>
                                  
                                  <div class="mb-3">
                                      <label for="description" class="form-label">Mô tả sản phẩm</label>
                                      <textarea class="form-control" id="description" name="description" rows="3">${product.description}</textarea>
                                  </div>
                                  
                                  <div class="row">
                                      <div class="col-md-6">
                                          <div class="mb-3">
                                              <label for="expirationDate" class="form-label">Ngày hết hạn</label>
                                              <input type="date" class="form-control" id="expirationDate" name="expirationDate" 
                                                     value="${product.expirationDate}">
                                          </div>
                                      </div>
                                      <div class="col-md-6">
                                          <div class="mb-3">
                                              <label for="imageUrl" class="form-label">URL hình ảnh</label>
                                              <input type="url" class="form-control" id="imageUrl" name="imageUrl" 
                                                     value="${product.imageUrl}" placeholder="https://example.com/image.jpg">
                                          </div>
                                      </div>
                                  </div>
                                  
                                  <div class="mb-3">
                                      <label for="additionalNotes" class="form-label">Ghi chú bổ sung</label>
                                      <textarea class="form-control" id="additionalNotes" name="additionalNotes" rows="2">${product.additionalNotes}</textarea>
                                  </div>
                              </div>
                          </div>
                              <div class="col-12">
                                  <h6 class="text-primary mb-3"><i class="fas fa-file-alt me-2"></i>Mô tả chi tiết</h6>
                                  <div class="mb-3">
                                      <label for="description" class="form-label">Mô tả sản phẩm</label>
                                      <textarea class="form-control" id="description" name="description" 
                                                rows="4" placeholder="Nhập mô tả chi tiết về sản phẩm...">${product.description}</textarea>
                                  </div>
                              </div>
                          </div>

                          <!-- Action Buttons -->
                          <div class="row mt-4">
                              <div class="col-12 text-center">
                                  <button type="submit" class="btn btn-primary btn-lg me-3">
                                      <i class="fas fa-save me-2"></i>Cập Nhật Sản Phẩm
                                  </button>
                                  <a href="product-list" class="btn btn-secondary btn-lg">
                                      <i class="fas fa-times me-2"></i>Hủy
                                  </a>
                              </div>
                          </div>
                      </form>
                  </div>
              </div>
          </c:when>
          <c:otherwise>
              <!-- No Product Found -->
              <div class="card">
                  <div class="card-body text-center">
                      <i class="fas fa-exclamation-triangle text-warning" style="font-size: 4rem;"></i>
                      <h4 class="mt-3">Không tìm thấy sản phẩm</h4>
                      <p class="text-muted">Sản phẩm có thể đã bị xóa hoặc không tồn tại trong hệ thống.</p>
                      <a href="product-list" class="btn btn-primary">
                          <i class="fas fa-arrow-left me-2"></i>Quay lại Danh sách
                      </a>
                  </div>
              </div>
          </c:otherwise>
      </c:choose>
  </div>

  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
  
  <!-- Form Validation -->
  <script>
      // Bootstrap form validation
      (function() {
          'use strict';
          window.addEventListener('load', function() {
              var forms = document.getElementsByClassName('needs-validation');
              var validation = Array.prototype.filter.call(forms, function(form) {
                  form.addEventListener('submit', function(event) {
                      if (form.checkValidity() === false) {
                          event.preventDefault();
                          event.stopPropagation();
                      }
                      form.classList.add('was-validated');
                  }, false);
              });
          }, false);
      })();

      // Auto-dismiss alerts
      setTimeout(function() {
          var alerts = document.querySelectorAll('.alert');
          alerts.forEach(function(alert) {
              var bsAlert = new bootstrap.Alert(alert);
              bsAlert.close();
          });
      }, 5000);
  </script>
</body>
</html>