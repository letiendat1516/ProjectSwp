<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Create Material Unit Page: Form to add a new material unit -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm đơn vị mới</title>
    <style>
        body {
            background: #f6f8fa;
            font-family: 'Segoe UI', Arial, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 500px;
            background: #fff;
            margin: 40px auto 0 auto;
            padding: 32px 28px 28px 28px;
            border-radius: 12px;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
        }
        h1 {
            color: #222;
            margin-bottom: 28px;
            font-size: 1.7rem;
            letter-spacing: 1px;
            text-align: center;
        }
        .form-group {
            margin-bottom: 18px;
        }
        .form-group label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            color: #444;
        }
        .form-group input[type="text"],
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #cfd8dc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 1rem;
            background: #f9fafb;
            transition: border 0.2s;
        }
        .form-group input[type="text"]:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            border: 1.5px solid #4CAF50;
            outline: none;
            background: #fff;
        }
        .form-buttons {
            margin-top: 22px;
            display: flex;
            gap: 12px;
            justify-content: center;
        }
        .btn-primary {
            display: inline-block;
            background: linear-gradient(90deg, #4CAF50 80%, #388e3c 100%);
            color: #fff;
            padding: 9px 22px;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-weight: 600;
            font-size: 1rem;
            box-shadow: 0 2px 8px rgba(76,175,80,0.08);
            transition: background 0.2s, box-shadow 0.2s;
        }
        .btn-primary:hover {
            background: linear-gradient(90deg, #388e3c 80%, #4CAF50 100%);
            box-shadow: 0 4px 16px rgba(76,175,80,0.13);
        }
        .btn-secondary {
            display: inline-block;
            background: #f44336;
            color: #fff;
            padding: 9px 22px;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            font-weight: 600;
            font-size: 1rem;
            transition: background 0.2s;
        }
        .btn-secondary:hover {
            background: #d32f2f;
        }
        .error-message {
            color: #d32f2f;
            background: #ffebee;
            border: 1px solid #ffcdd2;
            padding: 10px 16px;
            border-radius: 5px;
            margin-bottom: 18px;
            text-align: center;
        }
        @media (max-width: 600px) {
            .container {
                padding: 10px 2vw;
            }
            h1 {
                font-size: 1.2rem;
            }
            .form-buttons {
                flex-direction: column;
                gap: 8px;
            }
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
    </style>
</head>
<body>
    <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <div class="main-content">
        <h1>Thêm đơn vị mới</h1>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        <form action="createMaterialUnit" method="post">
            <div class="form-group">
                <label for="name">Tên:</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="symbol">Kí hiệu:</label>
                <input type="text" id="symbol" name="symbol" required>
            </div>
            <div class="form-group">
                <label for="description">Mô tả:</label>
                <textarea id="description" name="description" rows="4"></textarea>
            </div>
            <div class="form-group">
                <label for="type">Loại đơn vị</label>
                <select id="type" name="type" required>
                    <option value="Khối lượng">Khối lượng</option>
                    <option value="Độ dài">Độ dài</option>
                    <option value="Số lượng">Số lượng</option>
                </select>
            </div>
            <div class="form-buttons">
                <button type="submit" class="btn-primary">Lưu</button>
                <a href="materialUnit" class="btn-secondary">Hủy bỏ</a>
            </div>
        </form>
    </div>
            </div>
</body>
</html>
