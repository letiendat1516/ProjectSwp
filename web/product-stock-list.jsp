<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Users" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || (!"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName()))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Tồn Kho Sản Phẩm</title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #f5f5f5;
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: black;
            padding: 30px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        .header h1 {
            font-size: 2.5rem;
            margin-bottom: 10px;
            font-weight: 300;
            color: black;
        }

        .header p {
            font-size: 1.1rem;
            opacity: 0.9;
            color: black;
        }

        .header-user {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 20px;
            margin-top: 15px;
        }

        .label {
            color: white;
            font-weight: 500;
        }

        .logout-btn {
            background: #dc3545;
            color: white;
            padding: 8px 16px;
            border-radius: 4px;
            text-decoration: none;
            transition: background 0.3s ease;
        }

        .logout-btn:hover {
            background: #c82333;
            text-decoration: none;
            color: black;
        }

        .nav-buttons {
            display: flex;
            gap: 15px;
            margin-bottom: 25px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: #28a745;
            color: white;
        }

        .btn-primary:hover {
            background: #218838;
            text-decoration: none;
            color: white;
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
            text-decoration: none;
            color: white;
        }

        .btn-info {
            background: #17a2b8;
            color: white;
        }

        .btn-info:hover {
            background: #138496;
            text-decoration: none;
            color: white;
        }

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
            border: 1px solid #f5c6cb;
        }

        .controls {
            background: white;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 25px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .controls-row {
            display: flex;
            gap: 20px;
            align-items: center;
            flex-wrap: wrap;
        }

        .search-box {
            flex: 1;
            min-width: 250px;
        }

        .search-box input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e5e9;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }

        .search-box input:focus {
            outline: none;
            border-color: #667eea;
        }

        .table-container {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .table {
            width: 100%;
            border-collapse: collapse;
        }

        .table th {
            background: #f8f9fa;
            padding: 15px 12px;
            text-align: left;
            font-weight: 600;
            color: #495057;
            border-bottom: 2px solid #dee2e6;
        }

        .table td {
            padding: 15px 12px;
            border-bottom: 1px solid #dee2e6;
            vertical-align: middle;
        }

        .table tbody tr:hover {
            background: #f8f9fa;
        }

        .badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 500;
            text-transform: uppercase;
        }

        .badge-success {
            background: #d4edda;
            color: #155724;
        }

        .badge-warning {
            background: #fff3cd;
            color: #856404;
        }

        .badge-danger {
            background: #f8d7da;
            color: #721c24;
        }

        .badge-secondary {
            background: #e2e3e5;
            color: #383d41;
        }

        .stock-info {
            display: flex;
            flex-direction: column;
            gap: 2px;
        }

        .stock-quantity {
            font-weight: 600;
            font-size: 1.1rem;
        }

        .stock-threshold {
            font-size: 0.85rem;
            color: #6c757d;
        }

        .stock-warning {
            color: #dc3545;
            font-weight: 600;
        }

        .action-buttons {
            display: flex;
            gap: 5px;
            align-items: center;
        }

        .btn-sm {
            padding: 6px 12px;
            font-size: 12px;
        }

        .btn-edit {
            background: #ffc107;
            color: #212529;
        }

        .btn-edit:hover {
            background: #e0a800;
            text-decoration: none;
            color: #212529;
        }

        .btn-detail {
            background: #17a2b8;
            color: white;
        }

        .btn-detail:hover {
            background: #138496;
            text-decoration: none;
            color: white;
        }

        .btn-delete {
            background: #dc3545;
            color: white;
        }

        .btn-delete:hover {
            background: #c82333;
            text-decoration: none;
            color: white;
        }

        .no-data {
            text-align: center;
            padding: 50px;
            color: #666;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 25px;
            gap: 10px;
        }

        .pagination-btn {
            padding: 8px 12px;
            border: 1px solid #dee2e6;
            background: white;
            color: #495057;
            text-decoration: none;
            border-radius: 4px;
            transition: all 0.3s ease;
        }

        .pagination-btn:hover {
            background: #e9ecef;
            text-decoration: none;
            color: #495057;
        }

        .pagination-btn.active {
            background: #667eea;
            color: white;
            border-color: #667eea;
        }

        .pagination-btn.disabled {
            background: #f8f9fa;
            color: #adb5bd;
            cursor: not-allowed;
        }

        .pagination-info {
            margin: 0 15px;
            color: #666;
            font-size: 14px;
        }

        .stats-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 25px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }

        .stat-item {
            text-align: center;
        }

        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            margin-bottom: 5px;
        }

        .stat-label {
            color: #666;
            font-size: 0.9rem;
        }

        @media (max-width: 768px) {
            .main-content {
                padding: 10px;
            }

            .controls-row {
                flex-direction: column;
                align-items: stretch;
            }

            .table-container {
                overflow-x: auto;
            }

            .table {
                min-width: 800px;
            }

            .action-buttons {
                flex-direction: column;
                gap: 3px;
            }

            .nav-buttons {
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
                <h1>Quản Lý Số Lượng Sản Phẩm</h1>
                <div class="header-user">
                    <label class="label">Xin chào, <%= user.getFullname()%></label>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </div>
            </div>

            <!-- Navigation Buttons -->
            <div class="nav-buttons">
                <a href="${pageContext.request.contextPath}/categoriesforward.jsp" class="btn btn-secondary">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">arrow_back</span>
                    Quay lại
                </a>
                <a href="${pageContext.request.contextPath}/product-list" class="btn btn-info">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">inventory_2</span>
                    Quản Lý Sản Phẩm
                </a>
            </div>

            <!-- Success/Error Messages -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    <strong>Thành công!</strong> ${successMessage}
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <strong>Lỗi!</strong> ${errorMessage}
                </div>
            </c:if>

            <!-- Statistics -->
            <div class="stats-card">
                <div class="stats-grid">
                    <div class="stat-item">
                        <div class="stat-number" style="color: #667eea;">${totalProducts}</div>
                        <div class="stat-label">Tổng Sản Phẩm</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number" style="color: #28a745;">
                            <c:set var="inStockCount" value="0"/>
                            <c:forEach var="product" items="${products}">
                                <c:if test="${product.stockQuantity != null and product.stockQuantity > 0}">
                                    <c:set var="inStockCount" value="${inStockCount + 1}"/>
                                </c:if>
                            </c:forEach>
                            ${inStockCount}
                        </div>
                        <div class="stat-label">Có Tồn Kho</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number" style="color: #dc3545;">
                            <c:set var="lowStockCount" value="0"/>
                            <c:forEach var="product" items="${products}">
                                <c:if test="${product.stockQuantity != null and product.minStockThreshold != null and product.stockQuantity <= product.minStockThreshold}">
                                    <c:set var="lowStockCount" value="${lowStockCount + 1}"/>
                                </c:if>
                            </c:forEach>
                            ${lowStockCount}
                        </div>
                        <div class="stat-label">Sắp Hết Hàng</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number" style="color: #6c757d;">
                            <c:set var="noStockCount" value="0"/>
                            <c:forEach var="product" items="${products}">
                                <c:if test="${product.stockQuantity == null}">
                                    <c:set var="noStockCount" value="${noStockCount + 1}"/>
                                </c:if>
                            </c:forEach>
                            ${noStockCount}
                        </div>
                        <div class="stat-label">Chưa Có Tồn Kho</div>
                    </div>
                </div>
            </div>

            <!-- Search and Controls -->
            <div class="controls">
                <form method="get" action="${pageContext.request.contextPath}/product-stock/list">
                    <div class="controls-row">
                        <div class="search-box">
                            <input type="text" name="search" value="${search}" 
                                   placeholder="Tìm kiếm theo tên sản phẩm, mã sản phẩm hoặc danh mục...">
                        </div>
                        <button type="submit" class="btn btn-primary">
                            <span class="material-icons" style="vertical-align: middle;">search</span>
                            Tìm kiếm
                        </button>
                        <c:if test="${not empty search}">
                            <a href="${pageContext.request.contextPath}/product-stock/list" class="btn btn-secondary">
                                <span class="material-icons" style="vertical-align: middle;">clear</span>
                                Xóa bộ lọc
                            </a>
                        </c:if>
                    </div>
                </form>
            </div>

            <!-- Product Stock Table -->
            <div class="table-container">
                <c:choose>
                    <c:when test="${not empty products}">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Mã Sản Phẩm</th>
                                    <th>Tên Sản Phẩm</th>
                                    <th>Danh Mục</th>
                                    <th>Đơn Vị</th>
                                    <th>Tồn Kho</th>
                                    <th>Ngưỡng Cảnh Báo</th>
                                    <th>Trạng Thái</th>
                                    <th>Thao Tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <td><strong>${product.code}</strong></td>
                                        <td>${product.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty categoryMap[product.cateId]}">
                                                    ${categoryMap[product.cateId]}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa phân loại</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty product.unitSymbol}">
                                                    ${product.unitSymbol}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="stock-info">
                                                <c:choose>
                                                    <c:when test="${product.stockQuantity != null}">
                                                        <c:set var="isLowStock" value="${product.minStockThreshold != null and product.stockQuantity <= product.minStockThreshold}"/>
                                                        <span class="stock-quantity ${isLowStock ? 'stock-warning' : ''}">
                                                            <fmt:formatNumber value="${product.stockQuantity}" type="number" maxFractionDigits="2"/>
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-secondary">Chưa có</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.minStockThreshold != null}">
                                                    <span class="stock-threshold">
                                                        <fmt:formatNumber value="${product.minStockThreshold}" type="number" maxFractionDigits="2"/>
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Chưa thiết lập</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${product.stockQuantity == null}">
                                                    <span class="badge badge-secondary">Chưa có tồn kho</span>
                                                </c:when>
                                                <c:when test="${product.stockQuantity == 0}">
                                                    <span class="badge badge-danger">Hết hàng</span>
                                                </c:when>
                                                <c:when test="${product.minStockThreshold != null and product.stockQuantity <= product.minStockThreshold}">
                                                    <span class="badge badge-warning">Sắp hết</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-success">Sẵn có</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="action-buttons">
                                                <a href="${pageContext.request.contextPath}/product-stock/detail?productId=${product.id}" 
                                                   class="btn btn-detail btn-sm" title="Xem chi tiết">
                                                    <span class="material-icons" style="font-size: 16px;">visibility</span>
                                                </a>
                                                <c:choose>
                                                    <c:when test="${product.stockQuantity != null}">
                                                        <a href="${pageContext.request.contextPath}/product-stock/edit?productId=${product.id}" 
                                                           class="btn btn-edit btn-sm" title="Sửa tồn kho">
                                                            <span class="material-icons" style="font-size: 16px;">edit</span>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/product-stock/delete?productId=${product.id}" 
                                                           class="btn btn-delete btn-sm" title="Xóa tồn kho"
                                                           onclick="return confirm('Bạn có chắc chắn muốn xóa thông tin tồn kho này?')">
                                                            <span class="material-icons" style="font-size: 16px;">delete</span>
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted" style="font-size: 12px; font-style: italic;">
                                                            Chưa có tồn kho
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-data">
                            <span class="material-icons" style="font-size: 48px; color: #dee2e6; margin-bottom: 15px;">inventory</span>
                            <h4>Không tìm thấy sản phẩm nào</h4>
                            <p>Hãy thử tìm kiếm với từ khóa khác hoặc thêm sản phẩm mới.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 0}">
                        <a href="?page=${currentPage - 1}&search=${search}" class="pagination-btn">
                            <span class="material-icons" style="font-size: 16px;">chevron_left</span>
                        </a>
                    </c:if>

                    <c:choose>
                        <c:when test="${totalPages <= 7}">
                            <c:forEach begin="0" end="${totalPages - 1}" var="i">
                                <a href="?page=${i}&search=${search}" 
                                   class="pagination-btn ${i == currentPage ? 'active' : ''}">${i + 1}</a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${currentPage < 3}">
                                    <c:forEach begin="0" end="4" var="i">
                                        <a href="?page=${i}&search=${search}" 
                                           class="pagination-btn ${i == currentPage ? 'active' : ''}">${i + 1}</a>
                                    </c:forEach>
                                    <span class="pagination-info">...</span>
                                    <a href="?page=${totalPages - 1}&search=${search}" 
                                       class="pagination-btn">${totalPages}</a>
                                </c:when>
                                <c:when test="${currentPage > totalPages - 4}">
                                    <a href="?page=0&search=${search}" class="pagination-btn">1</a>
                                    <span class="pagination-info">...</span>
                                    <c:forEach begin="${totalPages - 5}" end="${totalPages - 1}" var="i">
                                        <a href="?page=${i}&search=${search}" 
                                           class="pagination-btn ${i == currentPage ? 'active' : ''}">${i + 1}</a>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <a href="?page=0&search=${search}" class="pagination-btn">1</a>
                                    <span class="pagination-info">...</span>
                                    <c:forEach begin="${currentPage - 1}" end="${currentPage + 1}" var="i">
                                        <a href="?page=${i}&search=${search}" 
                                           class="pagination-btn ${i == currentPage ? 'active' : ''}">${i + 1}</a>
                                    </c:forEach>
                                    <span class="pagination-info">...</span>
                                    <a href="?page=${totalPages - 1}&search=${search}" 
                                       class="pagination-btn">${totalPages}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${currentPage < totalPages - 1}">
                        <a href="?page=${currentPage + 1}&search=${search}" class="pagination-btn">
                            <span class="material-icons" style="font-size: 16px;">chevron_right</span>
                        </a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>
