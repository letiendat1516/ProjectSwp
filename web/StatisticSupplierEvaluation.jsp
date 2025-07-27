<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page import="model.Users" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
Users user = (Users) session.getAttribute("user");
if (user == null || (!"Admin".equalsIgnoreCase(user.getRoleName()))) {
    response.sendRedirect("login.jsp");
    return;
}
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Statistic Supplier</title>
        <style>
            :root {
                --main-bg: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                --btn-gradient: linear-gradient(45deg, #667eea, #764ba2);
                --btn-view-gradient: linear-gradient(45deg, #ffb347, #ffcc33);
                --btn-view-hover: linear-gradient(45deg, #ffa500, #ffbf00);
                --border-radius: 10px;
                --shadow-light: 0 5px 15px rgba(0, 0, 0, 0.05);
                --shadow-medium: 0 10px 30px rgba(0, 0, 0, 0.1);
                --transition-fast: all 0.3s ease;
            }

            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: var(--main-bg);
                min-height: 100vh;
                padding: 20px;
                color: #333;
            }

            .layout-container {
                display: flex;
                min-height: 100vh;
            }

            .main-content {
                flex: 1;
                padding: 30px;
            }

            .form-wrapper {
                background: #fff;
                padding: 30px;
                border-radius: var(--border-radius);
                box-shadow: var(--shadow-medium);
                max-width: 1100px;
                margin: 0 auto;
            }

            .title, h2.section-header {
                text-align: center;
                color: #1567c1;
                font-size: 2.2em;
                font-weight: bold;
                margin-bottom: 25px;
                position: relative;
            }

            .title::after, h2.section-header::after {
                content: '';
                position: absolute;
                bottom: -8px;
                left: 50%;
                transform: translateX(-50%);
                width: 80px;
                height: 3px;
                background: var(--main-bg);
                border-radius: 2px;
            }

            /* Form */
            form {
                background: #f8f9fa;
                padding: 20px;
                border-radius: var(--border-radius);
                margin-bottom: 30px;
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
                justify-content: center;
                box-shadow: var(--shadow-light);
            }

            select,
            input[type="number"] {
                padding: 12px 15px;
                border: 2px solid #e9ecef;
                border-radius: var(--border-radius);
                font-size: 14px;
                background: white;
                color: #495057;
                cursor: pointer;
                transition: var(--transition-fast);
                min-width: 200px;
            }

            select:focus,
            input[type="number"]:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            input[type="submit"] {
                background: var(--btn-gradient);
                color: white;
                border: none;
                padding: 12px 25px;
                border-radius: var(--border-radius);
                font-size: 14px;
                font-weight: 600;
                cursor: pointer;
                transition: var(--transition-fast);
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            input[type="submit"]:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
            }

            /* Table */
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
                background: white;
                border-radius: var(--border-radius);
                overflow: hidden;
                box-shadow: var(--shadow-medium);
                animation: fadeInUp 0.6s ease-out;
            }

            table tr:first-child {
                background: var(--btn-gradient);
                color: white;
            }

            table tr:first-child td {
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 1px;
                font-size: 13px;
            }

            table td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #e9ecef;
                transition: var(--transition-fast);
            }

            table tr:not(:first-child):hover {
                background-color: #f8f9fa;
                transform: translateX(5px);
                box-shadow: 5px 0 15px rgba(0, 0, 0, 0.1);
            }

            table tr:nth-child(even):not(:first-child) {
                background-color: #fdfdfd;
            }

            table tr:nth-child(even):not(:first-child):hover {
                background-color: #f0f0f0;
            }

            /* Button back */
            .btn-back {
                display: inline-block;
                margin-top: 25px;
                padding: 10px 20px;
                background: #6c757d;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                transition: var(--transition-fast);
                font-weight: 500;
            }

            .btn-back:hover {
                background: #5a6268;
                transform: translateY(-1px);
            }

            /* Pagination */
            .pagination {
                display: flex;
                justify-content: center;
                gap: 10px;
                margin-top: 20px;
            }

            .pagination a {
                padding: 8px 12px;
                background: #667eea;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                transition: var(--transition-fast);
            }

            .pagination a:hover {
                background: #764ba2;
                transform: translateY(-2px);
            }

            .pagination a.active {
                background: #764ba2;
                font-weight: bold;
            }

            /* View Button */
            .btn-view {
                display: inline-block;
                padding: 8px 16px;
                background: var(--btn-view-gradient);
                color: white;
                font-size: 13px;
                font-weight: 600;
                text-decoration: none;
                border-radius: 6px;
                transition: var(--transition-fast);
                box-shadow: 0 5px 10px rgba(255, 195, 0, 0.3);
                text-align: center;
            }

            .btn-view:hover {
                background: var(--btn-view-hover);
                transform: scale(1.05);
                box-shadow: 0 8px 20px rgba(255, 195, 0, 0.5);
            }

            /* Responsive */
            @media (max-width: 768px) {
                form {
                    flex-direction: column;
                    align-items: stretch;
                }

                select, input[type="number"] {
                    width: 100%;
                }

                table {
                    font-size: 12px;
                }

                table td {
                    padding: 10px 8px;
                }
            }

            @media (max-width: 480px) {
                table {
                    font-size: 10px;
                }

                table td {
                    padding: 8px 5px;
                }

                .title {
                    font-size: 1.5em;
                }
            }

            /* Animations */
            @keyframes fadeInUp {
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            /* FORM THỐNG KÊ */
            .statistics-form {
                background: #f1f3f5;
                padding: 20px 25px;
                margin-top: 20px;
                border-radius: 8px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
                justify-content: center;
            }

            .statistics-form label {
                font-weight: bold;
                font-size: 14px;
                margin-right: 8px;
                align-self: center;
            }

            .statistics-form input[type="number"],
            .statistics-form select {
                padding: 10px 15px;
                font-size: 14px;
                border: 2px solid #dee2e6;
                border-radius: 6px;
                background: white;
                transition: border-color 0.3s ease;
            }

            .statistics-form input[type="number"]:focus,
            .statistics-form select:focus {
                border-color: #667eea;
                outline: none;
                box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.15);
            }

            .statistics-form input[type="submit"] {
                background: linear-gradient(45deg, #667eea, #764ba2);
                color: white;
                font-weight: 600;
                padding: 10px 25px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                transition: all 0.3s ease;
                text-transform: uppercase;
            }

            .statistics-form input[type="submit"]:hover {
                background: linear-gradient(45deg, #5a67d8, #6b46c1);
                transform: translateY(-2px);
                box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
            }

            /* Responsive cho form */
            @media (max-width: 768px) {
                .statistics-form {
                    flex-direction: column;
                    align-items: stretch;
                }

                .statistics-form label {
                    margin-bottom: 5px;
                }

                .statistics-form input[type="number"],
                .statistics-form select,
                .statistics-form input[type="submit"] {
                    width: 100%;
                }
            }
            .statistics-form {
                font-family: Arial, sans-serif;
                padding: 20px;
                background-color: #f9f9f9;
            }

            .form-wrapper {
                margin-bottom: 20px;
            }

            .form-filter {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                align-items: center;
            }

            .date-input {
                padding: 6px 10px;
                border-radius: 4px;
                border: 1px solid #ccc;
            }

            .summary-container {
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                gap: 16px;
                margin-bottom: 20px;
            }

            .summary-item {
                background: #fff;
                border-radius: 10px;
                padding: 16px;
                box-shadow: 0 0 6px rgba(0, 0, 0, 0.05);
                flex: 1;
                min-width: 200px;
                text-align: center;
            }

            .summary-value {
                font-size: 24px;
                font-weight: bold;
                margin-bottom: 6px;
            }

            .summary-label {
                font-size: 14px;
                color: #666;
            }

            .table-wrapper {
                overflow-x: auto;
            }






        </style>
    </head>
    <body>
        <div class="layout-container">
            <jsp:include page="/include/sidebar.jsp" />
            <c:set var="choice" value="${requestScope.choice}"/>
            <c:if test="${empty choice}">
                <c:set var="choice" value="${param.luaChonValue}" />
            </c:if>
            <div class="main-content">
                <div class="form-wrapper">
                    <h2>Chọn thống kê</h2>
                    <select id="luaChon">
                        <option value="first" ${choice == null ||choice eq 'first' ? 'selected="selected"' : ''}>Import statistic</option>
                        <option value="third" ${choice eq 'third' ? 'selected="selected"' : ''}>Đánh giá nhà cung cấp</option>
                    </select>

                    <div id="content-first" class="content-box" class="statistics-form">
                        <c:set var="fromdate" value="${requestScope.fromdate}"/>
                        <c:set var="todate" value="${requestScope.todate}"/>
                        <c:set var="list2" value="${requestScope.list2}"/>
                        <c:set var="totalPurchase" value="${requestScope.totalPurchase}"/>
                        <c:set var="totalProduct" value="${requestScope.totalProduct}"/>
                        <c:set var="totalPrice" value="${requestScope.totalPrice}"/>
                        <c:set var="error" value="${requestScope.error}"/>
                        <p>${error}</p>
                        <div class="form-wrapper">
                            <form action="StatisticSupplierEvaluation" class="form-filter">
                                <label>Từ ngày:</label>
                                <input value="${fromdate}" required="" type="date" name="fromDay" class="date-input">
                                <label>Đến ngày:</label>
                                <input value="${todate}" required="" type="date" name="toDay" class="date-input">
                                <input type="hidden" name="luaChonValue" id="luaChonValue" value="first">
                                <input type="submit" value="Statistic" name="name">
                            </form>
                        </div> 
                        <c:if test="${not empty list2}">
                            <c:set var="topExport" value="${requestScope.topExport}" />
                            <c:set var="topImport" value="${requestScope.topImport}" />
                            <c:set var="indexImport" value="0" />
                            <c:set var="indexExport" value="0" />
                            <c:set var="list2" value="${requestScope.list2}" />
                            <c:set var="list21" value="${requestScope.list21}" />
                            <c:set var="totalPurchase" value="${requestScope.totalPurchase}" />
                            <c:set var="totalProduct" value="${requestScope.totalProduct}" />
                            <c:set var="totalPrice" value="${requestScope.totalPrice}" />
                            <c:set var="totalPage1" value="${requestScope.totalPage1}"/>
                            <c:set var="totalExport" value="${requestScope.totalExport}"/>
                            <c:if test="${not empty list2}">
                                <div class="summary-container">

                                    <div class="summary-item">
                                        <label class="summary-value">Tổng phiếu xuất</label>
                                        <p class="summary-value">${totalExport}</p>
                                    </div>
                                    <div class="summary-item">
                                        <label class="summary-value">Tổng phiếu nhập</label>
                                        <p class="summary-value">${totalPurchase}</p>
                                    </div>
                                    <div class="summary-item">
                                        <label class="summary-value">Tổng giá trị nhập kho</label>
                                        <label class="summary-value">
                                            <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" maxFractionDigits="2" />
                                            $
                                        </label>
                                    </div>
                                </div>

                                <div class="table-wrapper">
                                    <h3 style="color: blue" >Chi tiết nhập kho</h3>
                                    <table class="styled-table">
                                        <tr>
                                            <th>ID</th>
                                            <th>Tên sản phẩm</th>
                                            <th>Ngày nhập</th>
                                            <th>Nhà cung cấp</th>
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                        </tr>
                                        <c:forEach var="i" items="${list2}">
                                            <tr>
                                                <td>${i.id}</td>
                                                <td>${i.productName}</td>
                                                <td>${i.createAt}</td>
                                                <td>${i.supplierName}</td>
                                                <td><fmt:formatNumber value="${i.pricePerUnit}" type="number" groupingUsed="true" maxFractionDigits="2" /> $</td>
                                                <td>${i.quantity}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </c:if>
                            <c:if test="${totalPage1 > 1}">
                                <div class="pagination">
                                    <c:forEach var="i" begin="1" end="${totalPage1}">
                                        <a href="StatisticSupplierEvaluation?fromDay=${fromdate}&toDay=${todate}&luaChonValue=${choice}&index1=${i}" 
                                           class="${i == currentIndex ? 'active' : ''}">${i}</a>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <div></div>       
                            <div class="table-wrapper">
                                <h3 style="color: blue" >Chi tiết xuất kho</h3>
                                <table class="styled-table">
                                    <tr>
                                        <th>ID</th>
                                        <th>Tên sản phẩm</th>
                                        <th>Ngày xuất</th>
                                        <th>Số lượng</th>
                                    </tr>
                                    <c:forEach var="i3" items="${list21}">
                                        <tr>
                                            <td>${i3.id}</td>
                                            <td>${i3.product_name}</td>
                                            <td>${i3.export_at}</td>
                                            <td>${i3.quantity}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                            <div>
                                <div>
                                    <h3>Top sản phẩm xuất kho nhiều</h3>
                                    <table>
                                        <tr>
                                            <td>Product Name</td>
                                            <td>Quantity</td>
                                        </tr>
                                        <c:forEach var="ie" items="${topExport}">
                                            <tr>
                                                <td>${ie.product_name}</td>
                                                <td>${ie.quantity}</td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                                <div>
                                    <div>
                                        <h3>Top sản phẩm nhập kho nhiều</h3>
                                        <table>
                                            <tr>
                                                <td>Product Name</td>
                                                <td>Quantity</td>
                                            </tr>
                                            <c:forEach var="ii" items="${topImport}">
                                                <tr>
                                                    <td>${ii.product_name}</td>
                                                    <td>${ii.quantity}</td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    <!-- ^ xuất nhập tồn -->
                    <div id="content-second" class="content-box" style="display: none;" class="statistics-form">
                    </div>
                    <div id="content-third" class="content-box" style="display: none;">
                        <c:set var="list" value="${requestScope.list}"/>
                        <c:set var="st" value="${requestScope.st}"/>
                        <c:set var="mess" value="${requestScope.mess}"/>
                        <c:set var="sta" value="${requestScope.sta}"/>
                        <c:set var="totalPage" value="${requestScope.totalPage}"/>
                        <c:set var="currentIndex" value="${param.index}"/>
                        <c:set var="listStar" value="${requestScope.listStar}"/>
                        <c:set var="listComment" value="${requestScope.listComment}"/>
                        <h2 class="title">Thống kê đánh giá nhà cung cấp</h2>
                        <form action="StatisticSupplierEvaluation">
                            <select name="sort">
                                <option ${st=="desc" || st==null?'selected':''} value="desc">Tăng dần theo sao</option>
                                <option ${st=="asc"?'selected':''} value="asc">Giảm dần theo sao</option>
                            </select>
                            <select name="status">
                                <option ${sta=="ATPX"||sta==null?'selected':''} value="ATPX">Toàn bộ nhà cung cấp</option>
                                <option ${sta=="active"?'selected':''} value="active">Nhà cung cấp vẫn hoạt động</option>
                                <option ${sta=="inactive"?'selected':''} value="inactive">Nhà cung cấp ngừng hoạt động</option>
                            </select>
                            <input type="hidden" name="luaChonValue" id="luaChonValue" value="third">
                            <input type="submit" value="Statistic" name="name">
                        </form>

                        <c:if test="${not empty list}">
                            <h2 class="title">Danh sách đánh giá</h2>
                            <table>
                                <tr>
                                    <td>ID</td>
                                    <td>Name</td>
                                    <td>Phone</td>
                                    <td>Email</td>
                                    <td>Address</td>
                                    <td>Note</td>
                                    <td>Star</td>
                                    <td>Number Evaluation</td>
                                    <td>Option</td>
                                </tr>
                                <c:set var="index" value="0"/>
                                <c:forEach var="listItem" items="${list}">
                                    <tr>
                                        <td>${listItem.supplierID}</td>
                                        <td>${listItem.name}</td>
                                        <td>${listItem.phone}</td>
                                        <td>${listItem.email}</td>
                                        <td>${listItem.address}</td>
                                        <td>${listItem.note}</td>
                                        <td style="color: orange" >${listStar[index]}★</td>
                                        <td>${listComment[index]}</td>
                                        <c:set var="index" value="${index+1}"/>
                                        <td><a class="btn-view" href="ViewSupplierEvaluation?supplierID=${listItem.supplierID}&page=statistic">Xem chi tiết</a></td>
                                    </tr>
                                </c:forEach>                           
                            </table>
                        </c:if>

                        <c:if test="${totalPage > 1}">
                            <div class="pagination">
                                <c:forEach var="i" begin="1" end="${totalPage}">
                                    <a href="StatisticSupplierEvaluation?&sort=${st}&status=${sta}&luaChonValue=${choice}&index=${i}" 
                                       class="${i == currentIndex ? 'active' : ''}">${i}</a>
                                </c:forEach>
                            </div>
                        </c:if>
                        <br>
                        <c:set var="list11" value="${requestScope.list11}"/>
                        <c:set var="error" value="${requestScope.error}"/>
                        <c:set var="list22" value="${requestScope.list22}"/>
                        <c:set var="listStar11" value="${requestScope.listStar11}"/>
                        <c:set var="listStar22" value="${requestScope.listStar22}"/>
                        <c:set var="list22" value="${requestScope.list22}"/>
                        <c:set var="index11" value="0"/>
                        <c:set var="index22" value="0"/>
                        ${error}
                        <c:if test="${not empty list11}">
                            <div>
                                <h2>Nhà cung cấp được đánh giá là giao hàng nhanh</h2>
                                <table>
                                    <tr>
                                        <td>ID</td>
                                        <td>Name</td>
                                        <td>Phone</td>
                                        <td>Email</td>
                                        <td>Address</td>
                                        <td>Star</td>
                                    </tr>
                                    <c:forEach items="${list11}" var="i1">
                                        <tr>
                                            <td>${i1.supplierID}</td>
                                            <td>${i1.name}</td>
                                            <td>${i1.phone}</td>
                                            <td>${i1.email}</td>
                                            <td>${i1.address}</td>
                                            <td style="color: orange" >${listStar11[index11]}★</td>
                                            <c:set var="index11" value="${index11+1}"/>

                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                            <br>
                            <div>
                                <h2>Nhà cung cấp được đánh giá có giá rẻ</h2>
                                <table>
                                    <tr>
                                        <td>ID</td>
                                        <td>Name</td>
                                        <td>Phone</td>
                                        <td>Email</td>
                                        <td>Address</td>
                                        <td>Star</td>
                                    </tr>
                                    <c:forEach items="${list22}" var="i2">
                                        <tr>
                                            <td>${i2.supplierID}</td>
                                            <td>${i2.name}</td>
                                            <td>${i2.phone}</td>
                                            <td>${i2.email}</td>
                                            <td>${i2.address}</td>
                                            <td style="color: orange" >${listStar22[index22]}★</td>
                                            <c:set var="index22" value="${index2+1}"/>

                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </c:if>
                    </div>

                    <a href="Admin.jsp" class="btn-back">Back</a>
                </div>
            </div>
        </div>

        <script>
            const selectedChoice = '${choice}';

            document.addEventListener("DOMContentLoaded", function () {
                const selectBox = document.getElementById("luaChon");
                const hiddenInput = document.getElementById("luaChonValue");
                const selectedChoice = '${choice}';

                if (selectedChoice) {
                    selectBox.value = selectedChoice;
                    if (hiddenInput)
                        hiddenInput.value = selectedChoice;
                } else {
                    // Nếu chưa chọn, mặc định là 'first'
                    selectBox.value = 'first';
                    if (hiddenInput)
                        hiddenInput.value = 'first';
                }

                // Ẩn tất cả
                document.getElementById("content-first").style.display = "none";
                document.getElementById("content-second").style.display = "none";
                document.getElementById("content-third").style.display = "none";

                // Hiển thị theo lựa chọn
                const value = selectBox.value;
                if (value === "first") {
                    document.getElementById("content-first").style.display = "block";
                } else if (value === "second") {
                    document.getElementById("content-second").style.display = "block";
                } else if (value === "third") {
                    document.getElementById("content-third").style.display = "block";
                }
            });

            document.getElementById("luaChon").addEventListener("change", function () {
                const value = this.value;
                const hiddenInput = document.getElementById("luaChonValue");
                if (hiddenInput) {
                    hiddenInput.value = value;
                }

                document.getElementById("content-first").style.display = "none";
                document.getElementById("content-second").style.display = "none";
                document.getElementById("content-third").style.display = "none";

                if (value === "first") {
                    document.getElementById("content-first").style.display = "block";
                } else if (value === "second") {
                    document.getElementById("content-second").style.display = "block";
                } else if (value === "third") {
                    document.getElementById("content-third").style.display = "block";
                }
            });
        </script>

    </body>
</html>
