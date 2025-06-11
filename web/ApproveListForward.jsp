<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách phê duyệt</title>
    <!-- Material Icons -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!-- Google Fonts: Poppins -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            background: linear-gradient(135deg, #e0e7ff, #f5f7fa);
            padding: 40px 20px;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .container {
            max-width: 1200px;
            width: 100%;
            margin: 0 auto;
            padding: 0 20px;
        }

        .header {
            text-align: center;
            margin-bottom: 40px;
        }

        .page-title {
            color: #1e3a8a;
            font-size: 2.5rem;
            font-weight: 600;
            margin-bottom: 10px;
            letter-spacing: 0.5px;
        }

        .lead {
            font-size: 1.1rem;
            color: #4b5563;
            font-weight: 300;
            margin-bottom: 20px;
            line-height: 1.6;
        }

        .card-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
            justify-items: center;
        }

        .card {
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            width: 100%;
            max-width: 360px;
            overflow: hidden;
        }

        .card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
        }

        .card-header {
            background: linear-gradient(90deg, #3b82f6, #1e3a8a);
            color: #ffffff;
            border-radius: 16px 16px 0 0;
            padding: 20px;
            text-align: center;
        }

        .card-header h3 {
            margin: 0;
            font-size: 1.4rem;
            font-weight: 500;
        }

        .card-body {
            padding: 30px;
            text-align: center;
        }

        .icon-container {
            background: #eff6ff;
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            transition: background 0.3s ease;
        }

        .card:hover .icon-container {
            background: #dbeafe;
        }

        .material-icons {
            font-size: 40px;
            color: #1e40af;
        }

        .card-title {
            font-size: 1.25rem;
            font-weight: 500;
            color: #1f2937;
            margin-bottom: 12px;
        }

        .card-text {
            font-size: 0.95rem;
            color: #6b7280;
            margin-bottom: 25px;
            line-height: 1.6;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            padding: 12px 24px;
            border-radius: 8px;
            text-decoration: none;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: linear-gradient(90deg, #3b82f6, #1e40af);
            color: #ffffff;
            border: none;
        }

        .btn-primary:hover {
            background: linear-gradient(90deg, #2563eb, #1e3a8a);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .btn-secondary {
            background: #ffffff;
            color: #4b5563;
            border: 1px solid #d1d5db;
        }

        .btn-secondary:hover {
            background: #f3f4f6;
            color: #1f2937;
            border-color: #9ca3af;
            transform: translateY(-2px);
        }

        .btn-icon {
            font-size: 18px;
            vertical-align: middle;
            margin-left: 8px;
        }

        .back-btn-icon {
            margin-right: 8px;
        }

        .footer {
            text-align: center;
            margin-top: 50px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1 class="page-title">Yêu Cầu Cần Phê Duyệt</h1>
            <p class="lead">Chọn danh mục cần phê duyệt để xem chi tiết và xử lý các yêu cầu.</p>
        </div>

        <div class="card-container">
            <!-- Purchase Request -->
            <div class="card">
                <div class="card-header">
                    <h3>Yêu Cầu Mua Hàng</h3>
                </div>
                <div class="card-body">
                    <div class="icon-container">
                        <span class="material-icons">attach_money</span>
                    </div>
                    <h4 class="card-title">Phê Duyệt Yêu Cầu Mua Hàng</h4>
                    <p class="card-text">Xem và xử lý các yêu cầu mua hàng từ nhân viên, đảm bảo quy trình mua sắm hiệu quả.</p>
                    <a href="approvepurchaserequest" class="btn btn-primary">
                        Truy Cập <span class="material-icons btn-icon">arrow_forward</span>
                    </a>
                </div>
            </div>

            <!-- Purchase Order -->
            <div class="card">
                <div class="card-header">
                    <h3>Đơn Báo Giá</h3>
                </div>
                <div class="card-body">
                    <div class="icon-container">
                        <span class="material-icons">request_quote</span>
                    </div>
                    <h4 class="card-title">Phê Duyệt Đơn Báo Giá</h4>
                    <p class="card-text">Kiểm tra và phê duyệt các báo giá sản phẩm được đề xuất từ yêu cầu mua hàng.</p>
                    <a href="#" class="btn btn-primary">
                        Truy Cập <span class="material-icons btn-icon">arrow_forward</span>
                    </a>
                </div>
            </div>
        </div>

        <div class="footer">
            <a href="admin" class="btn btn-secondary">
                <span class="material-icons back-btn-icon">arrow_back</span> Quay Lại Trang Chủ
            </a>
        </div>
    </div>
</body>
</html>