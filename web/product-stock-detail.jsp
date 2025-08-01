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
    <title>Chi Tiết Thông Tin Tồn Kho</title>
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
            color: white;
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
            color: white;
        }

        .header p {
            font-size: 1.1rem;
            opacity: 0.9;
            color: white;
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
            background: #667eea;
            color: white;
        }

        .btn-primary:hover {
            background: #5a67d8;
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

        .btn-warning {
            background: #ffc107;
            color: #212529;
        }

        .btn-warning:hover {
            background: #e0a800;
            text-decoration: none;
            color: #212529;
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn-danger:hover {
            background: #c82333;
        }

        .detail-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .error-message {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }

        .section {
            padding: 30px;
            border-bottom: 1px solid #e9ecef;
        }

        .section:last-child {
            border-bottom: none;
        }

        .section-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #495057;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }

        .section-title .material-icons {
            margin-right: 10px;
            color: #667eea;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }

        .info-item {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }

        .info-label {
            font-weight: 600;
            color: #495057;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 8px;
        }

        .info-value {
            color: #333;
            font-size: 1.1rem;
            font-weight: 500;
        }

        .info-value.large {
            font-size: 2rem;
            font-weight: bold;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .status-active {
            background: #d4edda;
            color: #155724;
        }

        .status-inactive {
            background: #fff3cd;
            color: #856404;
        }

        .status-locked {
            background: #f8d7da;
            color: #721c24;
        }

        .stock-status-section {
            background: linear-gradient(135deg, #e3f2fd 0%, #f3e5f5 100%);
            padding: 30px;
            border-left: 4px solid #2196f3;
        }

        .stock-metrics {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .metric-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .metric-value {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .metric-label {
            font-size: 14px;
            color: #666;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .metric-current {
            color: #2196f3;
        }

        .metric-threshold {
            color: #ff9800;
        }

        .metric-status {
            color: #4caf50;
        }

        .alert-section {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            padding: 20px;
            border-radius: 8px;
            margin-top: 20px;
        }

        .alert-icon {
            color: #856404;
            vertical-align: middle;
            margin-right: 10px;
        }

        .alert-text {
            color: #856404;
            font-weight: 500;
        }

        .critical-alert {
            background: #f8d7da;
            border: 1px solid #f5c6cb;
        }

        .critical-alert .alert-icon,
        .critical-alert .alert-text {
            color: #721c24;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
            flex-wrap: wrap;
        }

        .product-info-section {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }

        @media (max-width: 768px) {
            .info-grid {
                grid-template-columns: 1fr;
            }
            
            .nav-buttons {
                flex-direction: column;
            }
            
            .main-content {
                padding: 10px;
            }
            
            .action-buttons {
                flex-direction: column;
            }

            .stock-metrics {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="layout-container">
        <jsp:include page="/include/sidebar.jsp" />
        <div class="main-content">
            <div class="header">
                <h1>Chi Tiết Thông Tin Tồn Kho</h1>
                <p>Xem thông tin chi tiết về tồn kho sản phẩm</p>
            </div>

            <!-- Navigation Buttons -->
            <div class="nav-buttons">
                <a href="${pageContext.request.contextPath}/product-stock/list" class="btn btn-secondary">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">arrow_back</span>
                    Quay lại danh sách
                </a>
                <a href="${pageContext.request.contextPath}/product-stock/edit?productId=${product.id}" class="btn btn-warning">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">edit</span>
                    Chỉnh sửa
                </a>
                <a href="${pageContext.request.contextPath}/categoriesforward.jsp" class="btn btn-secondary">
                    <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">home</span>
                    Trang chủ
                </a>
            </div>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="error-message">
                    <strong>Lỗi!</strong> ${error}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty product}">
                    <div class="detail-container">
                        <!-- Product Information Section -->
                        <div class="section product-info-section">
                            <h2 class="section-title">
                                <span class="material-icons">inventory_2</span>
                                Thông Tin Sản Phẩm
                            </h2>
                            <div class="info-grid">
                                <div class="info-item">
                                    <div class="info-label">Mã sản phẩm</div>
                                    <div class="info-value">${product.code}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">Tên sản phẩm</div>
                                    <div class="info-value">${product.name}</div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">Trạng thái sản phẩm</div>
                                    <div class="info-value">
                                        <span class="status-badge ${product.status == 'active' ? 'status-active' : (product.status == 'inactive' ? 'status-inactive' : 'status-locked')}">
                                            ${product.status}
                                        </span>
                                    </div>
                                </div>
                                <div class="info-item">
                                    <div class="info-label">Đơn vị tính</div>
                                    <div class="info-value">
                                        <c:choose>
                                            <c:when test="${not empty product.unitSymbol}">
                                                ${product.unitSymbol}
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: #6c757d;">Chưa thiết lập</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Stock Information Section -->
                        <div class="section stock-status-section">
                            <h2 class="section-title">
                                <span class="material-icons">analytics</span>
                                Thông Tin Tồn Kho
                            </h2>
                            
                            <c:choose>
                                <c:when test="${not empty stock}">
                                    <div class="stock-metrics">
                                        <div class="metric-card">
                                            <div class="metric-value metric-current">
                                                <fmt:formatNumber value="${stock.qty}" type="number" maxFractionDigits="0"/>
                                            </div>
                                            <div class="metric-label">Số lượng hiện tại</div>
                                        </div>
                                        
                                        <div class="metric-card">
                                            <div class="metric-value metric-threshold">
                                                <c:choose>
                                                    <c:when test="${stock.minStockThreshold != null and stock.minStockThreshold > 0}">
                                                        <fmt:formatNumber value="${stock.minStockThreshold}" type="number" maxFractionDigits="0"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="font-size: 1.2rem; color: #6c757d;">Chưa thiết lập</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="metric-label">Ngưỡng cảnh báo</div>
                                        </div>

                                        <div class="metric-card">
                                            <div class="metric-value metric-status">
                                                <span class="status-badge ${stock.status == 'active' ? 'status-active' : (stock.status == 'inactive' ? 'status-inactive' : 'status-locked')}">
                                                    ${stock.status}
                                                </span>
                                            </div>
                                            <div class="metric-label">Trạng thái tồn kho</div>
                                        </div>
                                    </div>

                                    <!-- Stock Alert Section -->
                                    <c:if test="${stock.minStockThreshold != null and stock.minStockThreshold > 0}">
                                        <c:choose>
                                            <c:when test="${stock.qty <= 0}">
                                                <div class="alert-section critical-alert">
                                                    <span class="material-icons alert-icon">error</span>
                                                    <span class="alert-text">
                                                        <strong>HẾT HÀNG!</strong> Sản phẩm đã hết trong kho. Cần nhập hàng ngay lập tức.
                                                    </span>
                                                </div>
                                            </c:when>
                                            <c:when test="${stock.qty <= stock.minStockThreshold}">
                                                <div class="alert-section">
                                                    <span class="material-icons alert-icon">warning</span>
                                                    <span class="alert-text">
                                                        <strong>CẢNH BÁO TỒN KHO THẤP!</strong> 
                                                        Số lượng hiện tại (<fmt:formatNumber value="${stock.qty}" type="number" maxFractionDigits="0"/>) 
                                                        đã đạt hoặc thấp hơn ngưỡng cảnh báo 
                                                        (<fmt:formatNumber value="${stock.minStockThreshold}" type="number" maxFractionDigits="0"/>). 
                                                        Nên nhập thêm hàng.
                                                    </span>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert-section">
                                        <span class="material-icons alert-icon">info</span>
                                        <span class="alert-text">
                                            Sản phẩm này chưa có thông tin tồn kho. 
                                            <a href="${pageContext.request.contextPath}/product-stock/add" style="color: #007bff; text-decoration: underline;">
                                                Nhấn vào đây để thêm thông tin tồn kho
                                            </a>.
                                        </span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="action-buttons">
                        <c:if test="${not empty stock}">
                            <a href="${pageContext.request.contextPath}/product-stock/edit?productId=${product.id}" class="btn btn-warning">
                                <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">edit</span>
                                Chỉnh sửa tồn kho
                            </a>
                            <a href="${pageContext.request.contextPath}/product-stock/delete?productId=${product.id}" 
                               class="btn btn-danger" 
                               onclick="return confirm('Bạn có chắc chắn muốn xóa thông tin tồn kho này?\n\nSản phẩm: ${product.name}\nSố lượng hiện tại: ${stock.qty}\n\nLưu ý: Hành động này không thể hoàn tác!')">
                                <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">delete</span>
                                Xóa tồn kho
                            </a>
                        </c:if>
                        <c:if test="${empty stock}">
                            <a href="${pageContext.request.contextPath}/product-stock/add" class="btn btn-primary">
                                <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">add</span>
                                Thêm thông tin tồn kho
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/product-stock/list" class="btn btn-secondary">
                            <span class="material-icons" style="vertical-align: middle; margin-right: 5px;">list</span>
                            Quay lại danh sách
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="error-message">
                        <strong>Không tìm thấy sản phẩm!</strong> 
                        Sản phẩm bạn đang tìm không tồn tại hoặc đã bị xóa.
                        <br><br>
                        <a href="${pageContext.request.contextPath}/product-stock/list" class="btn btn-primary" style="margin-top: 10px;">
                            Quay lại danh sách sản phẩm
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
