<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thành công - Quản lí kho hàng</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }

        .success-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
            padding: 40px;
            text-align: center;
            max-width: 500px;
            width: 100%;
            position: relative;
            overflow: hidden;
        }

        .success-container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, #4CAF50, #45a049);
        }

        .success-icon {
            font-size: 64px;
            color: #4CAF50;
            margin-bottom: 20px;
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }

        .success-title {
            color: #2c3e50;
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .success-message {
            color: #34495e;
            font-size: 16px;
            line-height: 1.6;
            margin-bottom: 25px;
        }

        .product-details {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
            border-left: 4px solid #4CAF50;
        }

        .product-detail-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding: 5px 0;
        }

        .product-detail-label {
            font-weight: 600;
            color: #495057;
        }

        .product-detail-value {
            color: #6c757d;
            font-weight: 500;
        }

        .button-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
            margin-top: 30px;
        }

        .btn {
            padding: 12px 25px;
            border: none;
            border-radius: 25px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-primary {
            background: linear-gradient(45deg, #667eea, #764ba2);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #f8f9fa;
            color: #495057;
            border: 2px solid #dee2e6;
        }

        .btn-secondary:hover {
            background: #e9ecef;
            transform: translateY(-2px);
        }

        .btn-success {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
        }

        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(76, 175, 80, 0.4);
        }

        @media (max-width: 600px) {
            .success-container {
                padding: 30px 20px;
            }
            
            .success-title {
                font-size: 24px;
            }
            
            .button-group {
                flex-direction: column;
            }
            
            .btn {
                width: 100%;
                justify-content: center;
            }
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">â</div>
        <h1 class="success-title">ThÃ nh CÃ´ng!</h1>
        <p class="success-message">
            <% if(request.getAttribute("successMessage") != null) { %>
                <%= request.getAttribute("successMessage") %>
            <% } else { %>
                Thao tÃ¡c cá»§a báº¡n ÄÃ£ ÄÆ°á»£c thá»±c hiá»n thÃ nh cÃ´ng!
            <% } %>
        </p>
        
        <% if(request.getAttribute("productName") != null && request.getAttribute("productCode") != null) { %>
        <div class="product-details">
            <h3 style="color: #2c3e50; margin-bottom: 15px; font-size: 18px;">Chi tiáº¿t sáº£n pháº©m</h3>
            <div class="product-detail-item">
                <span class="product-detail-label">TÃªn sáº£n pháº©m:</span>
                <span class="product-detail-value"><%= request.getAttribute("productName") %></span>
            </div>
            <div class="product-detail-item">
                <span class="product-detail-label">MÃ£ sáº£n pháº©m:</span>
                <span class="product-detail-value"><%= request.getAttribute("productCode") %></span>
            </div>
        </div>
        <% } %>
        
        <div class="button-group">
            <a href="add-product" class="btn btn-primary">
                â ThÃªm sáº£n pháº©m khÃ¡c
            </a>
            <a href="product-list" class="btn btn-success">
                ð Xem danh sÃ¡ch sáº£n pháº©m
            </a>
            <a href="Admin.jsp" class="btn btn-secondary">
                ð  Vá» trang chá»§
            </a>
        </div>
    </div>
</body>
</html>
