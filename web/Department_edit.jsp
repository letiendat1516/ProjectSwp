<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa phòng ban</title>
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

        .header {
            text-align: center;
            margin-bottom: 30px;
        }

        .header-user {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 20px;
        }

        .label {
            color: #888;
        }

        .logout-btn {
            background: red;
            color: #fff;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .logout-btn:hover {
            background: orange;
        }

        .page-title {
            color: #3f51b5;
            font-size: 2rem;
            margin-bottom: 10px;
        }

        .container {
            max-width: 900px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .form-header {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #eee;
        }

        .form-header h2 {
            color: #333;
            font-size: 1.8rem;
        }

        .department-info {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 4px solid #2196f3;
        }

        .nav-buttons {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
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
        }

        .btn-info {
            background: #17a2b8;
            color: white;
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
            transform: translateY(-1px);
        }

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

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .required {
            color: red;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            transition: border-color 0.3s;
        }

        .form-control:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0,123,255,0.3);
        }

        .form-control.error {
            border-color: #dc3545;
        }

        .form-text {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .form-section {
            margin-bottom: 30px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 5px;
            border-left: 4px solid #007bff;
        }

        .form-section h3 {
            margin-bottom: 15px;
            color: #333;
            font-size: 1.2rem;
        }

        .status-toggle {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 34px;
        }

        .switch input {
            opacity: 0;
            width: 0;
            height: 0;
        }

        .slider {
            position: absolute;
            cursor: pointer;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: #ccc;
            transition: .4s;
            border-radius: 34px;
        }

        .slider:before {
            position: absolute;
            content: "";
            height: 26px;
            width: 26px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            transition: .4s;
            border-radius: 50%;
        }

        input:checked + .slider {
            background-color: #2196F3;
        }

        input:checked + .slider:before {
            transform: translateX(26px);
        }

        .audit-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            font-size: 12px;
            color: #666;
        }

        @media (max-width: 768px) {
            .container {
                margin: 10px;
                padding: 20px;
            }

            .form-row {
                grid-template-columns: 1fr;
            }

            .nav-buttons {
                flex-direction: column;
            }

            .form-actions {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="layout-container">
        <jsp:include page="/include/sidebar.jsp" />
        <div class="main-content">
            <div class="header">
                <h1 class="page-title">Chỉnh sửa phòng ban</h1>
                <div class="header-user">
                    <label class="label">Xin chào, <%= user.getFullname()%></label>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="logout-btn">Đăng xuất</a>
                </div>
            </div>

                <div class="form-header">
                    <h2>✏️ Chỉnh sửa phòng ban: ${department.deptName}</h2>
                </div>

                <!-- Navigation Buttons -->
                <div class="nav-buttons">
                    <a href="${pageContext.request.contextPath}/department/list" class="btn btn-info">← Quay lại danh sách</a>
                    <a href="${pageContext.request.contextPath}/department/detail?id=${department.id}" class="btn btn-warning">👁️ Xem chi tiết</a>
                </div>

                <!-- Department Info -->
                <div class="department-info">
                    <strong>Thông tin hiện tại:</strong> 
                    ID: #${department.id} | 
                    Mã: ${department.deptCode} | 
                    Trạng thái: ${department.activeFlag ? 'Hoạt động' : 'Không hoạt động'} |
                    Số nhân viên: ${department.employeeCount}
                </div>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        <strong>Lỗi:</strong> ${error}
                    </div>
                </c:if>

                <!-- Edit Form -->
                <form method="post" action="${pageContext.request.contextPath}/department/edit" id="editForm">
                    <input type="hidden" name="id" value="${department.id}">
                    
                    <!-- Basic Information -->
                    <div class="form-section">
                        <h3>📋 Thông tin cơ bản</h3>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="deptCode">Mã phòng ban <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="deptCode" 
                                       name="deptCode" 
                                       value="${department.deptCode}" 
                                       maxlength="20" 
                                       required>
                                <div class="form-text">Mã phòng ban phải là duy nhất (tối đa 20 ký tự)</div>
                            </div>
                            
                            <div class="form-group">
                                <label for="deptName">Tên phòng ban <span class="required">*</span></label>
                                <input type="text" 
                                       class="form-control" 
                                       id="deptName" 
                                       name="deptName" 
                                       value="${department.deptName}" 
                                       maxlength="100" 
                                       required>
                                <div class="form-text">Tên đầy đủ của phòng ban (tối đa 100 ký tự)</div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="description">Mô tả</label>
                            <textarea class="form-control" 
                                      id="description" 
                                      name="description" 
                                      rows="3" 
                                      maxlength="500" 
                                      placeholder="Mô tả về chức năng và nhiệm vụ của phòng ban...">${department.description}</textarea>
                            <div class="form-text">Mô tả chi tiết về phòng ban (tối đa 500 ký tự)</div>
                        </div>
                    </div>

                    <!-- Contact Information -->
                    <div class="form-section">
                        <h3>📞 Thông tin liên hệ</h3>
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="phone">Số điện thoại</label>
                                <input type="tel" 
                                       class="form-control" 
                                       id="phone" 
                                       name="phone" 
                                       value="${department.phone}" 
                                       maxlength="20" 
                                       pattern="[0-9+\-\s()]+"
                                       placeholder="0123456789">
                                <div class="form-text">Số điện thoại liên hệ của phòng ban</div>
                            </div>
                            
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" 
                                       class="form-control" 
                                       id="email" 
                                       name="email" 
                                       value="${department.email}" 
                                       maxlength="100"
                                       placeholder="department@company.com">
                                <div class="form-text">Địa chỉ email của phòng ban</div>
                            </div>
                        </div>
                    </div>

                    <!-- Manager Assignment -->
                    <div class="form-section">
                        <h3>👤 Trưởng phòng</h3>
                        
                        <div class="form-group">
                            <label for="managerId">Chọn trưởng phòng</label>
                            <select class="form-control" id="managerId" name="managerId">
                                <option value="">-- Không gán trưởng phòng --</option>
                                <c:forEach var="manager" items="${availableManagers}">
                                    <option value="${manager.id}" ${department.managerId == manager.id ? 'selected' : ''}>
                                        ${manager.fullname} (${manager.username})
                                        <c:if test="${not empty manager.email}"> - ${manager.email}</c:if>
                                    </option>
                                </c:forEach>
                            </select>
                            <div class="form-text">
                                <c:if test="${not empty department.managerName}">
                                    Trưởng phòng hiện tại: <strong>${department.managerName}</strong>
                                </c:if>
                                <c:if test="${empty department.managerName}">
                                    Chưa có trưởng phòng
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <!-- Status Control -->
                    <div class="form-section">
                        <h3>⚙️ Trạng thái</h3>
                        
                        <div class="form-group">
                            <div class="status-toggle">
                                <label class="switch">
                                    <input type="checkbox" 
                                           id="activeFlag" 
                                           name="activeFlag" 
                                           value="1" 
                                           ${department.activeFlag ? 'checked' : ''}>
                                    <span class="slider"></span>
                                </label>
                                <label for="activeFlag">Kích hoạt phòng ban</label>
                            </div>
                            <div class="form-text">
                                ${department.activeFlag ? 'Phòng ban đang hoạt động' : 'Phòng ban đang bị vô hiệu hóa'}
                                <c:if test="${department.employeeCount > 0}">
                                    <br><strong>Lưu ý:</strong> Phòng ban này có ${department.employeeCount} nhân viên
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <!-- Form Actions -->
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            💾 Cập nhật phòng ban
                        </button>
                        <a href="${pageContext.request.contextPath}/department/list" class="btn btn-secondary">
                            ❌ Hủy bỏ
                        </a>
                    </div>
                </form>

                <!-- Audit Information -->
                <div class="audit-info">
                    <strong>Thông tin kiểm toán:</strong><br>
                    Tạo bởi: ${department.createdByName} vào 
                    <fmt:formatDate value="${department.createDate}" pattern="dd/MM/yyyy HH:mm:ss"/><br>
                    <c:if test="${not empty department.updatedByName}">
                        Cập nhật lần cuối bởi: ${department.updatedByName} vào 
                        <fmt:formatDate value="${department.updateDate}" pattern="dd/MM/yyyy HH:mm:ss"/>
                    </c:if>
                </div>
            </div>
        </div>

    <script>
        // Form validation
        document.getElementById('editForm').addEventListener('submit', function(e) {
            const deptCode = document.getElementById('deptCode').value.trim();
            const deptName = document.getElementById('deptName').value.trim();
            
            // Reset error states
            document.querySelectorAll('.form-control').forEach(input => {
                input.classList.remove('error');
            });
            
            let hasError = false;
            
            // Validate department code
            if (!deptCode) {
                document.getElementById('deptCode').classList.add('error');
                hasError = true;
            } else if (deptCode.length > 20) {
                document.getElementById('deptCode').classList.add('error');
                alert('Mã phòng ban không được vượt quá 20 ký tự');
                hasError = true;
            }
            
            // Validate department name
            if (!deptName) {
                document.getElementById('deptName').classList.add('error');
                hasError = true;
            } else if (deptName.length > 100) {
                document.getElementById('deptName').classList.add('error');
                alert('Tên phòng ban không được vượt quá 100 ký tự');
                hasError = true;
            }
            
            // Validate phone if provided
            const phone = document.getElementById('phone').value.trim();
            if (phone && !/^[0-9+\-\s()]+$/.test(phone)) {
                document.getElementById('phone').classList.add('error');
                alert('Số điện thoại chỉ được chứa số và các ký tự +, -, (), khoảng trắng');
                hasError = true;
            }
            
            // Validate email if provided
            const email = document.getElementById('email').value.trim();
            if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                document.getElementById('email').classList.add('error');
                alert('Định dạng email không hợp lệ');
                hasError = true;
            }
            
            if (hasError) {
                e.preventDefault();
                return false;
            }
            
            // Confirm deactivation if has employees
            const activeFlag = document.getElementById('activeFlag').checked;
            const employeeCount = ${department.employeeCount};
            
            if (!activeFlag && employeeCount > 0) {
                if (!confirm(`Phòng ban này có ${employeeCount} nhân viên. Bạn có chắc chắn muốn vô hiệu hóa phòng ban không?`)) {
                    e.preventDefault();
                    return false;
                }
            }
            
            // Show loading state
            const submitBtn = this.querySelector('button[type="submit"]');
            submitBtn.innerHTML = '⏳ Đang cập nhật...';
            submitBtn.disabled = true;
        });

        // Character counter for description
        document.getElementById('description').addEventListener('input', function() {
            const current = this.value.length;
            const max = 500;
            const counter = this.parentNode.querySelector('.form-text');
            counter.textContent = `Mô tả chi tiết về phòng ban (${current}/${max} ký tự)`;
            
            if (current > max * 0.9) {
                counter.style.color = '#dc3545';
            } else {
                counter.style.color = '#666';
            }
        });

        // Real-time validation
        document.getElementById('deptCode').addEventListener('blur', function() {
            const value = this.value.trim();
            if (value && value.length > 20) {
                this.classList.add('error');
            } else {
                this.classList.remove('error');
            }
        });

        document.getElementById('deptName').addEventListener('blur', function() {
            const value = this.value.trim();
            if (value && value.length > 100) {
                this.classList.add('error');
            } else {
                this.classList.remove('error');
            }
        });

        // Toggle switch animation
        document.getElementById('activeFlag').addEventListener('change', function() {
            const statusText = this.parentNode.parentNode.querySelector('.form-text');
            if (this.checked) {
                statusText.innerHTML = 'Phòng ban sẽ được kích hoạt<c:if test="${department.employeeCount > 0}"><br><strong>Lưu ý:</strong> Phòng ban này có ${department.employeeCount} nhân viên</c:if>';
            } else {
                statusText.innerHTML = 'Phòng ban sẽ bị vô hiệu hóa<c:if test="${department.employeeCount > 0}"><br><strong>Lưu ý:</strong> Phòng ban này có ${department.employeeCount} nhân viên</c:if>';
            }
        });
    </script>
</body>
</html>