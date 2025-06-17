<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Xử lý Nhập Kho | Hệ thống Quản lý Kho</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            body {
                background-color: #f1f5f9;
                font-family: 'Inter', sans-serif;
                margin: 0;
                padding: 0;
            }

            .container {
                max-width: 1200px;
                margin: 2rem auto;
                padding: 1.5rem;
                background: white;
                border-radius: 0.375rem;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            }

            h2 {
                font-size: 1.5rem;
                font-weight: 600;
                margin-bottom: 1.5rem;
                color: #1e293b;
            }

            .section {
                margin-bottom: 2rem;
            }

            .section h3 {
                font-size: 1.25rem;
                font-weight: 500;
                color: #1e40af;
                margin-bottom: 1rem;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background-color: white;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }

            th, td {
                padding: 0.75rem 1rem;
                border: 1px solid #e2e8f0;
                text-align: left;
                font-size: 0.875rem;
            }

            th {
                background-color: #f8fafc;
                font-weight: 600;
                color: #334155;
                text-transform: uppercase;
                font-size: 0.75rem;
            }

            input[type="text"],
            input[type="number"],
            input[type="date"],
            select {
                width: 100%;
                padding: 0.5rem;
                font-size: 0.875rem;
                border: 1px solid #cbd5e1;
                border-radius: 0.375rem;
            }

            .form-footer {
                margin-top: 2rem;
                display: flex;
                gap: 1rem;
            }

            .btn {
                padding: 0.75rem 1.5rem;
                font-size: 0.875rem;
                font-weight: 500;
                border: none;
                border-radius: 0.375rem;
                cursor: pointer;
                transition: background-color 0.2s;
            }

            .btn-primary {
                background-color: #2563eb;
                color: white;
            }

            .btn-primary:hover {
                background-color: #1d4ed8;
            }

            .btn-secondary {
                background-color: #94a3b8;
                color: white;
            }

            .btn-secondary:hover {
                background-color: #64748b;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Xử lý Nhập Kho</h2>
            <form action="import-confirm" method="post">
                <div class="section">
                    <h3>Thông tin đơn nhập kho</h3>
                    <table>
                        <tr><th>Mã đơn nhập</th><td><input type="text" name="id" value="${p.id}" readonly></td></tr>
                        <tr><th>Ngày tạo đơn</th><td><input type="text" name="dayRequest" value="${p.day_request}" readonly></td></tr>
                        <tr><th>Người tạo đơn</th><td><input type="text" name="userId" value="${p.user_id}" readonly></td></tr>
                        <tr><th>Trạng thái</th><td><input type="text" name="status" value="${p.status}" readonly></td></tr>
                        <tr><th>Lý do nhập kho</th><td><input type="text" name="reason" value="${p.reason}" readonly></td></tr>
                    </table>
                </div>

                <div class="section">
                    <h3>Thông tin nhà cung cấp</h3>
                    <table>
                        <tr><th>Tên nhà cung cấp</th><td><input type="text" name="supplier" value="${p.supplier}" readonly></td></tr>
                        <tr><th>Địa chỉ</th><td><input type="text" name="address" value="${p.address}" readonly></td></tr>
                        <tr><th>Điện thoại</th><td><input type="text" name="phone" value="${p.phone}" readonly></td></tr>
                        <tr><th>Email</th><td><input type="text" name="email" value="${p.email}" readonly></td></tr>
                    </table>
                </div>

                <div class="section">
                    <h3>Danh sách vật tư nhập kho</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Mã VT</th>
                                <th>Tên VT</th>
                                <th>Đơn vị</th>
                                <th>Số lượng YC</th>
                                <th>Đã nhập</th>
                                <th>Nhập lần này</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${itemList}">
                                <tr>
                                    <td><input type="text" name="code_${item.productCode}" value="${item.productCode}" readonly></td>
                                    <td><input type="text" name="name_${item.productCode}" value="${item.productName}" readonly></td>
                                    <td><input type="text" name="unit_${item.productCode}" value="${item.unit}" readonly></td>
                                    <td><input type="text" value="${item.quantity}" readonly></td>
                                    <td><input type="text" value="0" readonly></td>
                                    <td><input type="number" name="importQty_${item.productCode}" min="0" max="${item.quantity}" required></td>
                                    <td><input type="text" name="note_${item.productCode}"></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="section">
                    <h3>Thông tin nhập kho</h3>
                    <table>
                        <tr><th>Kho nhập hàng</th>
                            <td>
                                <select name="warehouse" required>
                                    <option value="">--Chọn kho--</option>
                                    <option value="kho1">Kho chính</option>
                                    <option value="kho2">Kho chi nhánh</option>
                                </select>
                            </td>
                        </tr>
                        <tr><th>Ngày nhập kho thực tế</th><td><input type="date" name="importDate" required></td></tr>
                        <tr><th>Người nhập kho</th><td><input type="text" name="receiver" required></td></tr>
                    </table>
                </div>

                <div class="form-footer">
                    <button type="submit" name="action" value="confirm" class="btn btn-primary">Xác nhận nhập kho</button>
                    <button type="submit" name="action" value="reject" class="btn btn-danger">Từ chối nhập kho</button>
                    <a href="import" class="btn btn-secondary">Quay lại danh sách</a>
                </div>
            </form>
        </div>
    </body>
</html>
