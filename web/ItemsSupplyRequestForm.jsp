<%-- 
    Document   : ItemsSupplyRequestForm
    Created on : May 22, 2025, 5:47:12 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Items Supply Request Form</title>
        <style>
            body{
                font-family: Time New Roman;
                overflow-x: hidden;
            }
            .container{
                width: 1400px;
                margin: 30px auto 20px auto;
                border: 2px solid darkblue;
            }
            .container h1{
                text-align: center;

            }
            .container h3{
                text-align: center;
                font-style: italic;
                color: red;
                margin-top: 30px;
            }
            .container input[type="text"]{
                border: 1.7px solid gray;
            }
            #defaultInformations{
                border: 1.7px solid darkblue;
            }
            .informationUser{
                text-align: center;
                margin-left: 1cm;
                ;
            }
            .informationUser input[type="text"]{
                margin: 0px 2cm 0px 5px;
                width: 100px;
            }
            .informationItems{
                text-align: center;
            }
            .informationItems input[type="text"]{
                margin: 0px 2cm 0px 5px;
                width: 200px;
            }
            .itemsTable{
                margin-left: 40px;
                margin-top: 10px;
            }
            .itemsTable table{
                margin-top: 5px;
                border-collapse: collapse;
                margin-bottom: 15px;
            }
            .itemsTable table,
            .itemsTable table th,
            .itemsTable table td{
                border: 1.7px solid gray;
            }
            .itemsTable table th:nth-child(1), table td:nth-child(1){
                width: 2cm;
            }
            .itemsTable table th:nth-child(2), table td:nth-child(2){
                width: 15cm;
            }
            .itemsTable table th:nth-child(3), table td:nth-child(3){
                width: 2.5cm;
            }
            .itemsTable table th:nth-child(4), table td:nth-child(4){
                width: 2.5cm;
            }
            .itemsTable table th:nth-child(5), table td:nth-child(5){
                width: 2.5cm;
            }
            .itemsTable table th:nth-child(6), table td:nth-child(6){
                width: 9cm;
            }
            .itemsTable textarea{
                border: none;
            }
            .itemsTable #addRowTable{
                margin-top: 15px;
            }
            .container input[type="submit"]{
                margin: 20px auto 10px auto ;
                text-align: center;
                display: block;
                background-color: blue;
            }
            #button{
                background: linear-gradient(to bottom, #ffffff, #cccccc);
                border: 3px solid #3261B7;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>ĐƠN YÊU CẦU NHẬP KHO</h1>
            <form id="myForm">
                <h3>Thông tin</h3>
                <div  class="informationUser">

                    Người dùng <input id="defaultInformations" type="text" value="QuocMDB" readonly>
                    Tuổi <input id="defaultInformations" type="text" value="21" readonly>
                    Ngày tháng năm sinh <input id="defaultInformations" type="text" value="20/09/2004" readonly>
                    Vai trò <input id="defaultInformations" type="text" value="Nhân viên" readonly>
                    Ngày yêu cầu <input type="text" required>
                </div>
                <h3>Chi tiết</h3>            
                <div class="informationItems">
                    ID <input type="text" required>
                    Mục đích
                    <select name="purpose" style="border: 1.7px solid gray">
                        <option>Chọn</option>
                        <option>Nhập hàng mới</option>
                        <option>Bổ sung tồn kho</option>
                        <option>Nhập hàng khuyến mãi / hàng tặng</option>
                        <option>Nhập hàng hoàn trả từ khách hàng</option>
                        <option>Nhập hàng sau kiểm kê</option>
                        <option>Nhập hàng do sai sót phiếu xuất</option>
                        <option>Nhập hàng dự phòng / chiến lược</option>
                        <option>Khác</option>
                    </select>
                </div>
                <br/>
                <div class="itemsTable">
                    Vui lòng nhập chi tiết mặt hàng ở bên dưới
                    <br/>
                    <button id="addRowTable" type="button">Thêm hàng</button>
                    <table>
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Tên mặt hàng</th>
                                <th>ID</th>
                                <th>Đơn vị</th>
                                <th>Số lượng</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody id="itemsTableBody">
                            <tr>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                                <td>
                                    <textarea
                                        rows="1"
                                        style="width: 100%; box-sizing: border-box; overflow:hidden; resize:none;"
                                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'"></textarea>
                                </td>
                            </tr>
                        </tbody>


                    </table>
                    Lý do chi tiết
                    <br/>

                    <textarea
                        rows="3"
                        style="width: 1285px;border: 1.7px solid gray; box-sizing: border-box; overflow:hidden; resize:none;"
                        oninput="this.style.height = 'auto'; this.style.height = this.scrollHeight + 'px'" 
                        required></textarea>
                </div>
                <input id="button" type="submit" value="Gửi yêu cầu">
            </form>
        </div>
        <script>
            document.getElementById('addRowTable').addEventListener('click', function () {
                // Lấy tbody
                const tbody = document.getElementById('itemsTableBody');

                // Lấy dòng mẫu (dòng đầu tiên hiện có)
                const firstRow = tbody.querySelector('tr');

                // Clone dòng mẫu
                const newRow = firstRow.cloneNode(true);

                // Xóa hết nội dung textarea trong dòng mới
                newRow.querySelectorAll('textarea').forEach(t => {
                    t.value = '';
                    t.style.height = 'auto'; // reset height cho auto resize
                });

                // Thêm dòng mới vào cuối bảng
                tbody.appendChild(newRow);
            });
            document.getElementById('myForm').addEventListener('submit', function (e) {
                e.preventDefault();
                alert('Gửi yêu cầu thành công!');
            });


        </script>
    </body>
</html>
