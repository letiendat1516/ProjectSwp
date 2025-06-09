<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Users" session="true" %> 
<!DOCTYPE html>
<%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equals(user.getRoleName()) && !"Nhân viên kho".equals(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Thông tin Vật liệu</title>
        <!-- Material Icons -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: Arial, Helvetica, sans-serif;
            }

            body {
                background-color: #f5f5f5;
                padding: 20px;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 15px;
            }

            .header {
                text-align: center;
                margin-bottom: 30px;
            }

            .label {
                color: #888;
                width: 120px;
            }
            .value {
                color: #222;
                font-weight: bold;
            }

            .page-title {
                color: #3f51b5;
                font-size: 2rem;
                margin-bottom: 10px;
            }

            .lead {
                font-size: 1.2rem;
                color: #666;
                margin-bottom: 20px;
            }

            .card-container {
                display: flex;
                flex-wrap: wrap;
                justify-content: space-between;
                gap: 20px;
            }

            .card {
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s, box-shadow 0.3s;
                width: calc(33.333% - 20px);
                margin-bottom: 20px;
            }

            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            }

            .card-header {
                background-color: #3f51b5;
                color: white;
                border-radius: 10px 10px 0 0;
                padding: 15px;
            }

            .card-header h3 {
                margin: 0;
                font-size: 1.2rem;
            }

            .card-body {
                padding: 25px;
                text-align: center;
            }

            .icon-container {
                background-color: #eaecf4;
                width: 70px;
                height: 70px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px;
            }

            .material-icons {
                font-size: 36px;
                color: #3f51b5;
            }

            .card-title {
                font-size: 1.1rem;
                margin-bottom: 10px;
                color: #333;
            }

            .card-text {
                color: #666;
                margin-bottom: 20px;
                line-height: 1.5;
            }

            .btn {
                display: inline-block;
                padding: 10px 20px;
                border-radius: 5px;
                text-decoration: none;
                font-weight: bold;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            .btn-primary {
                background-color: #3f51b5;
                color: white;
            }

            .btn-primary:hover {
                background-color: #303f9f;
            }

            .btn-secondary {
                background-color: transparent;
                color: blue;
                border: 1px solid #ccc;
            }
            .btn-secondaries {
                background-color: transparent;
                color: red;
                border: 1px solid #ccc;
            }
            .btn-secondary:hover {
                background-color: #f0f0f0;
            }

            .footer {
                text-align: center;
                margin-top: 30px;
            }

            .btn-icon {
                font-size: 16px;
                vertical-align: text-bottom;
                margin-left: 5px;
            }

            .back-btn-icon {
                margin-right: 5px;
            }

            /* Responsive styles */
            @media (max-width: 900px) {
                .card {
                    width: calc(50% - 15px);
                }
            }

            @media (max-width: 600px) {
                .card {
                    width: 100%;
                }
            }
        </style>

    </head>
    <body>
        <div class="container">
            <div class="profile-info">
                <label class="label">Employee Name:</label>
                <span class="value"><%= user.getFullname() %></span>
            </div>
            <div class="profile-info">
                <span class="value">
                    <a href="Profile.jsp">Personal Information</a>
                </span>
            </div>
            <div class="header">
                <h1 class="page-title">Quản lý Thông tin Vật liệu</h1>
                <p class="lead">Chọn danh mục bạn muốn quản lý</p>
            </div>

            <div class="card-container">
                <!-- Categories Product -->
                <div class="card">
                    <div class="card-header">
                        <h3>Danh mục Sản phẩm</h3>
                    </div>
                    <div class="card-body">
                        <div class="icon-container">
                            <span class="material-icons">category</span>
                        </div>
                        <h4 class="card-title">Quản lý Danh mục Sản phẩm</h4>
                        <p class="card-text">Thêm, sửa, xóa và xem các danh mục sản phẩm trong hệ thống.</p>
                        <a href="category/list" class="btn btn-primary">
                            Truy cập <span class="material-icons btn-icon">arrow_forward</span>
                        </a>
                    </div>
                </div>

                <!-- Categories Unit -->
                <div class="card">
                    <div class="card-header">
                        <h3>Danh mục Đơn vị</h3>
                    </div>
                    <div class="card-body">
                        <div class="icon-container">
                            <span class="material-icons">straighten</span>
                        </div>
                        <h4 class="card-title">Quản lý Đơn vị Tính</h4>
                        <p class="card-text">Quản lý các đơn vị đo lường và tính toán cho vật liệu và sản phẩm.</p>
                        <a href="materialUnit.jsp" class="btn btn-primary">
                            Truy cập <span class="material-icons btn-icon">arrow_forward</span>
                        </a>
                    </div>
                </div>

                <!-- Categories Supplier -->
                <div class="card">
                    <div class="card-header">
                        <h3>Danh mục Nhà cung cấp</h3>
                    </div>
                    <div class="card-body">
                        <div class="icon-container">
                            <span class="material-icons">business</span>
                        </div>
                        <h4 class="card-title">Quản lý Nhà cung cấp</h4>
                        <p class="card-text">Thêm, sửa, xóa và xem thông tin về các nhà cung cấp vật liệu.</p>
                        <a href="LishSupplier" class="btn btn-primary">
                            Truy cập <span class="material-icons btn-icon">arrow_forward</span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="footer">
                <a href="admin" class="btn btn-secondary">
                    Quay lại Trang Admin (Admin only)
                </a>
            </div>
            <div class="footer">
                <a href="logout" class="btn btn-secondaries">
                    Logout
                </a>
            </div>
        </div>
    </body>
</html>