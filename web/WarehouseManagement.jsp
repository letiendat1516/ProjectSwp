<%-- 
    Document   : WarehouseManagement
    Created on : May 24, 2025, 4:47:55 PM
    Author     : Dell
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nhập Kho - Xử lý đơn nhập</title>
    </head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h2 {
            color: #2E8B57;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        input[type="number"] {
            width: 80px;
        }
        .section {
            margin-bottom: 30px;
        }
        .btn {
            padding: 10px 20px;
            background-color: #2E8B57;
            color: white;
            border: none;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #246b45;
        }
    </style>
    <body>

        <h2>Nhập Kho - Xử lý đơn nhập</h2>
        <form action="warehouse" method="get">
            <div class="section">
                <h3>Thông tin đơn nhập kho</h3>
                <table>
                    <tr><th>Mã đơn nhập</th><td>PNK12345</td></tr>
                    <tr><th>Ngày tạo đơn</th><td>2025-05-23</td></tr>
                    <tr><th>Người tạo đơn</th><td>Nguyễn Văn A</td></tr>
                    <tr><th>Trạng thái</th><td>Đã duyệt</td></tr>
                    <tr><th>Lý do nhập kho</th><td>Nhập hàng hoa tươi mùa hè</td></tr>
                </table>
            </div>

            <div class="section">
                <h3>Thông tin nhà cung cấp</h3>
                <table>
                    <tr><th>Tên nhà cung cấp</th><td>Hoa Sài Gòn</td></tr>
                    <tr><th>Địa chỉ</th><td>123 Đường Lê Lợi, TP.HCM</td></tr>
                    <tr><th>Điện thoại</th><td>0909 123 456</td></tr>
                    <tr><th>Email</th><td>hoasaigon@example.com</td></tr>
                </table>
            </div>

            <div class="section">
                <h3>Danh sách vật tư nhập kho</h3>
                <form action="processImport" method="post">
                    <table>
                        <thead>
                            <tr>
                                <th>Mã vật tư</th>
                                <th>Tên vật tư</th>
                                <th>Đơn vị tính</th>
                                <th>Số lượng yêu cầu</th>
                                <th>Số lượng đã nhập</th>
                                <th>Số lượng nhập lần này</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>VT001</td>
                                <td>Hoa hồng đỏ</td>
                                <td>Chục</td>
                                <td>50</td>
                                <td>20</td>
                                <td><input type="number" name="qty_VT001" min="0" max="30" required></td>
                                <td><input type="text" name="note_VT001"></td>
                            </tr>
                            <tr>
                                <td>VT002</td>
                                <td>Hoa lan tím</td>
                                <td>Chục</td>
                                <td>30</td>
                                <td>10</td>
                                <td><input type="number" name="qty_VT002" min="0" max="20" required></td>
                                <td><input type="text" name="note_VT002"></td>
                            </tr>
                            <!-- Thêm dòng vật tư khác nếu cần -->
                        </tbody>
                    </table>

                    <div style="margin-top: 20px;">
                        <label><b>Kho nhập hàng:</b></label>
                        <select name="warehouse" required>
                            <option value="">--Chọn kho--</option>
                            <option value="kho1">Kho chính</option>
                            <option value="kho2">Kho chi nhánh</option>
                        </select>
                    </div>

                    <div style="margin-top: 20px;">
                        <label><b>Ngày nhập kho thực tế:</b></label>
                        <input type="date" name="importDate" required>
                    </div>

                    <div style="margin-top: 20px;">
                        <label><b>Người nhập kho:</b></label>
                        <input type="text" name="receiver" required>
                    </div>

                    <div style="margin-top: 30px;">
                        <button type="submit" class="btn">Xác nhận nhập kho</button>
                        <button type="reset" class="btn" style="background-color: #999;">Hủy</button>
                    </div>
            </div>
        </form>
    </body>
</html>
