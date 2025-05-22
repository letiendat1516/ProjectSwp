<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Danh mục</title>
    <style>
        * {
            box-sizing: border-box;
            font-family: Arial, Helvetica, sans-serif;
        }
        
        body {
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        
        .container {
            width: 100%;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .header {
            margin-bottom: 20px;
        }
        
        h1, h2, h3, h4 {
            color: #333;
            margin-top: 0;
        }
        
        .flex-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        
        .list-section {
            flex: 2;
            min-width: 600px;
        }
        
        .form-section {
            flex: 1;
            min-width: 300px;
        }
        
        .card {
            background-color: white;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }
        
        .card-header {
            padding: 15px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .card-body {
            padding: 15px;
        }
        
        .alert {
            padding: 10px 15px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        
        .alert-success {
            background-color: #dff0d8;
            color: #3c763d;
            border: 1px solid #d6e9c6;
        }
        
        .alert-danger {
            background-color: #f2dede;
            color: #a94442;
            border: 1px solid #ebccd1;
        }
        
        .search-form {
            margin-bottom: 15px;
            display: flex;
        }
        
        .search-form input[type="text"] {
            flex: 1;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px 0 0 4px;
        }
        
        .search-form button {
            padding: 8px 15px;
            background-color: #337ab7;
            color: white;
            border: 1px solid #2e6da4;
            border-radius: 0 4px 4px 0;
            cursor: pointer;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 15px;
        }
        
        table th, table td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
        
        table th {
            background-color: #f8f8f8;
            font-weight: bold;
        }
        
        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        
        .badge {
            display: inline-block;
            padding: 3px 7px;
            border-radius: 3px;
            font-size: 12px;
            color: white;
        }
        
        .badge-success {
            background-color: #5cb85c;
        }
        
        .badge-danger {
            background-color: #d9534f;
        }
        
        .btn {
            display: inline-block;
            padding: 6px 12px;
            margin-bottom: 0;
            font-size: 14px;
            font-weight: 400;
            text-align: center;
            white-space: nowrap;
            vertical-align: middle;
            cursor: pointer;
            border: 1px solid transparent;
            border-radius: 4px;
        }
        
        .btn-primary {
            color: #fff;
            background-color: #337ab7;
            border-color: #2e6da4;
        }
        
        .btn-success {
            color: #fff;
            background-color: #5cb85c;
            border-color: #4cae4c;
        }
        
        .btn-warning {
            color: #fff;
            background-color: #f0ad4e;
            border-color: #eea236;
        }
        
        .btn-danger {
            color: #fff;
            background-color: #d9534f;
            border-color: #d43f3a;
        }
        
        .btn-sm {
            padding: 5px 10px;
            font-size: 12px;
        }
        
        .action-buttons {
            white-space: nowrap;
        }
        
        .action-buttons a, .action-buttons button {
            margin-right: 5px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        
        .form-control {
            display: block;
            width: 100%;
            padding: 8px;
            font-size: 14px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .text-right {
            text-align: right;
        }
        
        .hidden {
            display: none;
        }
        
        .pagination {
            display: flex;
            justify-content: center;
            list-style: none;
            padding: 0;
            margin: 20px 0;
        }
        
        .pagination li {
            margin: 0 2px;
        }
        
        .pagination a {
            display: inline-block;
            padding: 5px 10px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #337ab7;
            border-radius: 3px;
        }
        
        .pagination .active a {
            background-color: #337ab7;
            color: white;
            border-color: #337ab7;
        }
        
        .pagination .disabled a {
            color: #777;
            cursor: not-allowed;
            background-color: #fff;
        }
        
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }
        
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 500px;
            border-radius: 5px;
        }
        
        .modal-header {
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
            margin-bottom: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .modal-footer {
            padding-top: 15px;
            border-top: 1px solid #eee;
            margin-top: 15px;
            text-align: right;
        }
        
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        
        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
        
        .radio-group {
            margin-top: 5px;
        }
        
        .radio-group label {
            font-weight: normal;
            margin-right: 15px;
        }
        
        .text-muted {
            color: #777;
        }
        
        .text-danger {
            color: #d9534f;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Quản lý Danh mục</h1>
        </div>
        
        <div class="flex-container">
            <!-- Phần hiển thị danh sách -->
            <div class="list-section">
                <div class="card">
                    <div class="card-header">
                        <h2>Danh sách danh mục</h2>
                        <button type="button" class="btn btn-primary" onclick="showAddForm()">
                            Thêm mới
                        </button>
                    </div>
                    <div class="card-body">
                        <!-- Thông báo -->
                        <c:if test="${not empty message}">
                            <div class="alert alert-success">
                                ${message}
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>
                        
                        <!-- Tìm kiếm -->
                        <form action="${pageContext.request.contextPath}/category" method="get" class="search-form">
                            <input type="text" name="search" placeholder="Tìm kiếm danh mục..." value="${searchKeyword}">
                            <button type="submit">Tìm kiếm</button>
                        </form>
                        
                        <!-- Bảng danh sách -->
                        <table>
                            <thead>
                                <tr>
                                    <th width="5%">ID</th>
                                    <th width="30%">Tên danh mục</th>
                                    <th width="25%">Danh mục cha</th>
                                    <th width="15%">Trạng thái</th>
                                    <th width="25%">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${categories}" var="category">
                                    <tr>
                                        <td>${category.id}</td>
                                        <td>${category.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty category.parentName}">
                                                    <span class="text-muted">Không có</span>
                                                </c:when>
                                                <c:otherwise>${category.parentName}</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${category.activeFlag}">
                                                    <span class="badge badge-success">Hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">Vô hiệu hóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="action-buttons">
                                            <!-- Nút thay đổi trạng thái -->
                                            <c:choose>
                                                <c:when test="${category.activeFlag}">
                                                    <a href="${pageContext.request.contextPath}/category?action=toggle&id=${category.id}&status=0" 
                                                       class="btn btn-sm btn-warning" title="Vô hiệu hóa">
                                                        Tắt
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/category?action=toggle&id=${category.id}&status=1" 
                                                       class="btn btn-sm btn-success" title="Kích hoạt">
                                                        Bật
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                            
                                            <!-- Nút sửa -->
                                            <button type="button" class="btn btn-sm btn-primary" 
                                                    onclick="showEditForm(${category.id}, '${category.name}', ${category.parentId}, ${category.activeFlag})" 
                                                    title="Sửa">
                                                Sửa
                                            </button>
                                            
                                            <!-- Nút xóa -->
                                            <button type="button" class="btn btn-sm btn-danger" 
                                                    onclick="confirmDelete(${category.id})" 
                                                    title="Xóa">
                                                Xóa
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                
                                <c:if test="${empty categories}">
                                    <tr>
                                        <td colspan="5" style="text-align: center;">Không có danh mục nào</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                        
                        <!-- Phân trang -->
                        <c:if test="${totalPages > 1}">
                            <ul class="pagination">
                                <!-- Nút Previous -->
                                <li class="${currentPage == 1 ? 'disabled' : ''}">
                                    <a href="${pageContext.request.contextPath}/category?page=${currentPage - 1}&search=${searchKeyword}" aria-label="Previous">
                                        &laquo;
                                    </a>
                                </li>
                                
                                <!-- Các trang -->
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="${currentPage == i ? 'active' : ''}">
                                        <a href="${pageContext.request.contextPath}/category?page=${i}&search=${searchKeyword}">${i}</a>
                                    </li>
                                </c:forEach>
                                
                                <!-- Nút Next -->
                                <li class="${currentPage == totalPages ? 'disabled' : ''}">
                                    <a href="${pageContext.request.contextPath}/category?page=${currentPage + 1}&search=${searchKeyword}" aria-label="Next">
                                        &raquo;
                                    </a>
                                </li>
                            </ul>
                        </c:if>
                    </div>
                </div>
            </div>
            
            <!-- Phần form thêm/sửa -->
            <div class="form-section">
                <!-- Form thêm mới -->
                <div id="addForm" class="card ${showAddForm ? '' : 'hidden'}">
                    <div class="card-header">
                        <h3>Thêm danh mục mới</h3>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/category" method="post">
                            <input type="hidden" name="action" value="add">
                            
                            <div class="form-group">
                                <label for="name">Tên danh mục <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="name" name="name" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="parentId">Danh mục cha</label>
                                <select class="form-control" id="parentId" name="parentId">
                                    <option value="">-- Không có danh mục cha --</option>
                                    <c:forEach items="${parentCategories}" var="parent">
                                        <option value="${parent.id}">${parent.name}</option>
                                    </c:forEach>
                                </select>
                                <small class="text-muted">Chọn danh mục cha nếu đây là danh mục con</small>
                            </div>
                            
                            <div class="form-group">
                                <label>Trạng thái</label>
                                <div class="radio-group">
                                    <label>
                                        <input type="radio" name="activeFlag" id="activeYes" value="1" checked> Hoạt động
                                    </label>
                                    <label>
                                        <input type="radio" name="activeFlag" id="activeNo" value="0"> Vô hiệu hóa
                                    </label>
                                </div>
                            </div>
                            
                            <div class="form-group text-right">
                                <button type="button" class="btn btn-danger" onclick="hideAddForm()">
                                    Hủy
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    Lưu
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Form chỉnh sửa -->
                <div id="editForm" class="card ${showEditForm ? '' : 'hidden'}">
                    <div class="card-header">
                        <h3>Chỉnh sửa danh mục</h3>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/category" method="post">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="id" id="edit-id">
                            
                            <div class="form-group">
                                <label for="edit-name">Tên danh mục <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="edit-name" name="name" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="edit-parentId">Danh mục cha</label>
                                <select class="form-control" id="edit-parentId" name="parentId">
                                    <option value="">-- Không có danh mục cha --</option>
                                    <c:forEach items="${parentCategories}" var="parent">
                                        <option value="${parent.id}">${parent.name}</option>
                                    </c:forEach>
                                </select>
                                <small class="text-muted">Chọn danh mục cha nếu đây là danh mục con</small>
                            </div>
                            
                            <div class="form-group">
                                <label>Trạng thái</label>
                                <div class="radio-group">
                                    <label>
                                        <input type="radio" name="activeFlag" id="edit-activeYes" value="1"> Hoạt động
                                    </label>
                                    <label>
                                        <input type="radio" name="activeFlag" id="edit-activeNo" value="0"> Vô hiệu hóa
                                    </label>
                                </div>
                            </div>
                            
                            <div class="form-group text-right">
                                <button type="button" class="btn btn-danger" onclick="hideEditForm()">
                                    Hủy
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    Cập nhật
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Modal xác nhận xóa -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4>Xác nhận xóa</h4>
                <span class="close" onclick="closeDeleteModal()">&times;</span>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc chắn muốn xóa danh mục này không?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" onclick="closeDeleteModal()">Hủy</button>
                <a id="deleteLink" href="#" class="btn btn-primary">Xóa</a>
            </div>
        </div>
    </div>
    
    <script>
        // Xác nhận xóa
        function confirmDelete(id) {
            document.getElementById('deleteLink').href = '${pageContext.request.contextPath}/category?action=delete&id=' + id;
            document.getElementById('deleteModal').style.display = 'block';
        }
        
        // Đóng modal xóa
        function closeDeleteModal() {
            document.getElementById('deleteModal').style.display = 'none';
        }
        
        // Hiển thị form thêm mới
        function showAddForm() {
            document.getElementById('addForm').classList.remove('hidden');
            document.getElementById('editForm').classList.add('hidden');
        }
        
        // Ẩn form thêm mới
        function hideAddForm() {
            document.getElementById('addForm').classList.add('hidden');
        }
        
        // Hiển thị form chỉnh sửa
        function showEditForm(id, name, parentId, activeFlag) {
            document.getElementById('edit-id').value = id;
            document.getElementById('edit-name').value = name;
            
            // Xử lý parentId
            const parentSelect = document.getElementById('edit-parentId');
            if (parentId === null || parentId === 'null') {
                parentSelect.value = '';
            } else {
                parentSelect.value = parentId;
            }
            
            // Xử lý activeFlag
            if (activeFlag === true || activeFlag === 'true' || activeFlag === 1) {
                document.getElementById('edit-activeYes').checked = true;
            } else {
                document.getElementById('edit-activeNo').checked = true;
            }
            
            document.getElementById('editForm').classList.remove('hidden');
            document.getElementById('addForm').classList.add('hidden');
        }
        
        // Ẩn form chỉnh sửa
        function hideEditForm() {
            document.getElementById('editForm').classList.add('hidden');
        }
        
        // Khi nhấn ESC thì đóng modal
        window.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') {
                closeDeleteModal();
            }
        });
        
        // Khi nhấn bên ngoài modal thì đóng modal
        window.onclick = function(event) {
            if (event.target == document.getElementById('deleteModal')) {
                closeDeleteModal();
            }
        };
        
        // Hiển thị form thêm mới hoặc chỉnh sửa nếu có lỗi
        window.onload = function() {
            <c:if test="${showAddForm}">
                showAddForm();
            </c:if>
            
            <c:if test="${showEditForm}">
                showEditForm(
                    ${category.id}, 
                    '${category.name}', 
                    ${category.parentId != null ? category.parentId : 'null'}, 
                    ${category.activeFlag}
                );
            </c:if>
        };
    </script>
</body>
</html>
