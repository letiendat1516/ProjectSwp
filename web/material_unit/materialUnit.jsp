<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đơn vị vật tư</title>
         <style>
        .container {
            width: 80%;
            margin: 0 auto;
            padding: 20px;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input[type="text"],
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .form-buttons {
            margin-top: 20px;
        }

        .btn-primary {
            display: inline-block;
            background-color: #4CAF50;
            color: white;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
        }

        .btn-secondary {
            display: inline-block;
            background-color: #f44336;
            color: white;
            padding: 8px 16px;
            text-decoration: none;
            border-radius: 4px;
            margin-left: 10px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #ddd;
        }

        th, td {
            padding: 12px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .btn-edit {
            display: inline-block;
            background-color: #2196F3;
            color: white;
            padding: 5px 10px;
            text-decoration: none;
            border-radius: 4px;
            margin-right: 5px;
        }

        .status-button {
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 5px;
        }

        .active-button {
            background-color: #4CAF50;
            color: white;
        }

        .inactive-button {
            background-color: #f44336;
            color: white;
        }

        .disabled-button {
            opacity: 0.5;
            cursor: not-allowed;
        }

        /* Styles cho thanh tìm kiếm */
        .search-container {
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 10px;
            flex-wrap: nowrap;
        }

        #searchInput {
            padding: 8px;
            width: 300px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-right: 10px;
        }

        #statusFilter {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            width: 150px;
        }

        /* Add this new style for the form */
        .search-form {
            display: flex;
            align-items: center;
        }

        .no-data {
            text-align: center;
            padding: 20px;
            font-style: italic;
            color: #757575;
        }

        .view-options {
            margin: 20px 0;
            text-align: center;
        }

        .view-options .btn {
            padding: 8px 16px;
            margin: 0 5px;
            text-decoration: none;
            border: 1px solid #ddd;
            color: #333;
            border-radius: 4px;
        }

        .view-options .btn.active {
            background-color: #4CAF50;
            color: white;
            border-color: #4CAF50;
        }

        .view-options .btn:hover:not(.active) {
            background-color: #ddd;
        }
        .pagination {
            text-align: center;
            margin-top: 20px;
        }
        .pagination a, .pagination span.page-number {
            display: inline-block;
            padding: 8px 16px;
            margin: 0 5px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
        .pagination a:hover {
            background-color: #0056b3;
        }
        .pagination span.page-number {
            background-color: #6c757d;
            cursor: default;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Quản lí đơn vị vật tư</h1>
      
        
        <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        
        <!-- Hiển thị thông báo thành công nếu có -->
        <c:if test="${not empty successMessage}">
            <div class="success-message" style="color: green; margin-bottom: 10px;">${successMessage}</div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <!-- Thanh tìm kiếm và nút tạo mới -->
        <div class="search-container">
            <form method="get" action="materialUnit" class="search-form">
                <input type="hidden" name="action" value="search" />
                <input type="text" id="searchInput" name="searchTerm" placeholder="Tên, kí hiệu,..." value="${searchTerm}" />
                <button type="submit" class="btn-primary">Tìm kiếm</button>
            </form>
            <select id="typeFilter" onchange="filterByType(this.value)" style="padding: 8px; width: 300px; border: 1px solid #ddd; border-radius: 4px; margin-right: 10px;">
                <option value="all">Tất cả loại</option>
                <option value="Khối lượng">Khối lượng</option>
                <option value="Độ dài">Độ dài</option>
                <option value="Số lượng">Số lượng</option>
            </select>
            <a href="createMaterialUnit" class="btn-primary">Thêm đơn vị mới</a>
        </div>

        <script>
        function filterByType(type) {
            var table = document.getElementById("materialUnitTable");
            var tr = table.getElementsByTagName("tr");
            for (var i = 1; i < tr.length; i++) {
                var typeCell = tr[i].getElementsByTagName("td")[4];
                if (typeCell) {
                    var typeText = typeCell.textContent || typeCell.innerText;
                    if (type === 'all' || typeText === type) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }
        function sortTable(n) {
            var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
            table = document.getElementById("materialUnitTable");
            switching = true;
            dir = "asc";
            while (switching) {
                switching = false;
                rows = table.rows;
                for (i = 1; i < (rows.length - 1); i++) {
                    shouldSwitch = false;
                    x = rows[i].getElementsByTagName("td")[n];
                    y = rows[i + 1].getElementsByTagName("td")[n];
                    if (dir == "asc") {
                        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                            shouldSwitch = true;
                            break;
                        }
                    } else if (dir == "desc") {
                        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                            shouldSwitch = true;
                            break;
                        }
                    }
                }
                if (shouldSwitch) {
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                    switchcount++;
                } else {
                    if (switchcount == 0 && dir == "asc") {
                        dir = "desc";
                        switching = true;
                    }
                }
            }
            var headers = table.getElementsByTagName("th");
            for (i = 0; i < headers.length; i++) {
                headers[i].className = headers[i].className.replace(" asc desc", "");
            }
            headers[n].className += " " + dir;
        } 
        </script>

        <!-- Bảng hiển thị dữ liệu -->
        <table id="materialUnitTable">
            <thead>
                <tr>
                    <th onclick="sortTable(0)" style="cursor: pointer;">ID ↕</th>
                    <th onclick="sortTable(1)" style="cursor: pointer;">Tên ↕</th>
                    <th onclick="sortTable(2)" style="cursor: pointer;">Kí hiệu ↕</th>
                    <th onclick="sortTable(3)" style="cursor: pointer;">Mô tả ↕</th>
                    <th onclick="sortTable(4)" style="cursor: pointer;">Loại đơn vị ↕</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty materialUnits}">
                        <tr>
                            <td colspan="6" class="no-data">Không tìm thấy đơn vị nào.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="unit" items="${materialUnits}">
                            <tr>
                                <td>${unit.id}</td>
                                <td>${unit.name}</td>
                                <td>${unit.symbol}</td>
                                <td>${unit.description}</td>
                                <td>${unit.type}</td>
                                <td>
                                    <a href="editMaterialUnit?id=${unit.id}" class="btn-edit">Thay đổi</a> 
                                    <button onclick="if(confirm('Bạn có muốn xóa đơn vị này không?')) window.location.href='deleteMaterialUnit?id=${unit.id}'" class="btn-edit btn-delete" style="background-color: #ff4444; border-color: #ff4444;">Xóa</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
   
<!-- Pagination at the very bottom -->


<c:if test="${requestScope.totalPages > 1}">
<div class="pagination">
    <c:choose>
        <c:when test="${requestScope.currentPage gt 1}">
            <a href="materialUnit?page=${requestScope.currentPage - 1}&action=${param.action}&searchTerm=${searchTerm}">Trước</a>
        </c:when>
    </c:choose>

    <span class="page-number">
        Trang ${requestScope.currentPage} / ${requestScope.totalPages}
    </span>

    <c:choose>
        <c:when test="${requestScope.currentPage lt requestScope.totalPages}">
            <a href="materialUnit?page=${requestScope.currentPage + 1}&action=${param.action}&searchTerm=${searchTerm}">Sau</a>
        </c:when>
    </c:choose>
</div>
</c:if>
<div style="text-align: center; margin-top: 30px;">
    <a href="../Admin.jsp" class="btn-primary" style="background-color: #6c757d;">Quay lại trang Admin</a>
</div>
    </div>
</body>
</html>