<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa danh mục loại sản phẩm</title>
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
            border-bottom: 2px solid #eee;
        }

        .card-header-warning {
            background: #ffc107;
            color: #212529;
        }

        .card-header-info {
            background: #17a2b8;
            color: white;
        }

        .card-body {
            padding: 30px;
        }

        /* Alert Styles */
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
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

        .form-control, .form-select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            background: white;
        }

        .form-control:focus, .form-select:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 0 2px rgba(0,123,255,0.25);
        }

        .text-muted {
            color: #6c757d;
            font-size: 12px;
            margin-top: 5px;
        }

        .text-danger {
            color: #dc3545;
        }

        .text-success {
            color: #28a745;
        }

        /* Radio Group */
        .radio-group {
            margin-top: 10px;
        }

        .radio-item {
            display: flex;
            align-items: center;
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #eee;
            border-radius: 4px;
            cursor: pointer;
        }

        .radio-item:hover {
            background: #f8f9fa;
        }

        .radio-input {
            margin-right: 10px;
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

        .btn-warning {
            background: #ffc107;
            color: #212529;
        }

        .btn:hover {
            opacity: 0.9;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
            gap: 15px;
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

        /* Info Section */
        .info-item {
            margin-bottom: 15px;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }

        .info-item:last-child {
            border-bottom: none;
        }

        .info-label {
            font-weight: bold;
            margin-bottom: 5px;
        }

        .note {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            border-left: 4px solid #17a2b8;
            margin-top: 20px;
        }

        /* Navigation Buttons */
        .nav-buttons {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
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
        <!-- Navigation Buttons -->
        <div class="nav-buttons">
            <a href="${pageContext.request.contextPath}/category/list" class="btn btn-secondary">← Quay lại danh sách</a>
        </div>

        <!-- Form chỉnh sửa -->
        <div class="card">
            <div class="card-header card-header-warning">
                Chỉnh sửa danh mục loại sản phẩm
            </div>
            <div class="card-body">
                <c:if test="${error != null}">
                    <div class="alert alert-danger">
                        ${error}
                        <button type="button" class="alert-close" onclick="this.parentElement.style.display='none'">&times;</button>
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/category/edit" method="post">
                    <input type="hidden" name="id" value="${category.id}">
                    
                    <div class="form-group">
                        <label for="name" class="form-label">Tên danh mục loại sản phẩm <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" value="${category.name}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="parentId" class="form-label">Danh mục </label>
                        <select class="form-select" id="parentId" name="parentId">
                            <option value="0">-- Không có danh mục  --</option>
                            <c:forEach items="${parentCategories}" var="parent">
                                <option value="${parent.id}" ${category.parentId == parent.id ? 'selected' : ''}>${parent.name}</option>
                            </c:forEach>
                        </select>
                        <div class="text-muted">Chọn danh mục  từ danh sách có sẵn</div>
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Trạng thái</label>
                        <div class="radio-group">
                            <div class="radio-item">
                                <input class="radio-input" type="radio" name="activeFlag" id="activeTrue" value="1" ${category.activeFlag ? 'checked' : ''}>
                                <label for="activeTrue">
                                    <span class="text-success">✓ Hoạt động</span>
                                </label>
                            </div>
                            <div class="radio-item">
                                <input class="radio-input" type="radio" name="activeFlag" id="activeFalse" value="0" ${!category.activeFlag ? 'checked' : ''}>
                                <label for="activeFalse">
                                    <span class="text-danger">✕ Không hoạt động</span>
                                </label>
                            </div>
                        </div>
                    </div>
                    
                    <div class="button-group">
                        <a href="${pageContext.request.contextPath}/category/list" class="btn btn-secondary">
                            ← Hủy
                        </a>
                        <button type="submit" class="btn btn-warning">
                            💾 Cập nhật
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Thông tin chi tiết -->
        <div class="card">
            <div class="card-header card-header-info">
                Thông tin chi tiết
            </div>
            <div class="card-body">
                <div class="info-item">
                    <div class="info-label">ID danh mục loại sản phẩm:</div>
                    <div>#${category.id}</div>
                </div>
                
                <div class="info-item">
                    <div class="info-label">Trạng thái hiện tại:</div>
                    <div>
                        <c:choose>
                            <c:when test="${category.activeFlag}">
                                <span class="badge badge-success">Hoạt động</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-secondary">Không hoạt động</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <div class="info-item">
                    <div class="info-label">Danh mục hiện tại:</div>
                    <div>
                        <c:choose>
                            <c:when test="${category.parentName != null}">
                                <strong>${category.parentName}</strong>
                                <span class="text-muted">(ID: ${category.parentId})</span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">Không có danh mục</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
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
    </script>
</body>
</html>