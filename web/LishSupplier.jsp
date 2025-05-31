<%-- 
    Document   : LishSupplier
    Created on : 28 thg 5, 2025, 14:13:26
    Author     : Fpt06
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lish Supplier</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, sans-serif;
                background: linear-gradient(to right, #cfd9df, #e2ebf0);
                margin: 0;
                padding: 30px;
                animation: fadeIn 1s ease-in-out;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .LishHead {
                background: linear-gradient(to right, #ffffff, #f0f8ff);
                padding: 30px;
                margin-bottom: 30px;
                border-radius: 10px;
                box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
                text-align: center;
                animation: slideDown 0.8s ease;
            }

            @keyframes slideDown {
                from {
                    transform: translateY(-20px);
                    opacity: 0;
                }
                to {
                    transform: translateY(0);
                    opacity: 1;
                }
            }

            .LishHead h1 {
                margin-bottom: 20px;
                color: #0d6efd;
            }

            .LishHead input[type="text"] {
                padding: 10px;
                width: 280px;
                border: 1px solid #ccc;
                border-radius: 6px;
                margin-right: 10px;
                transition: box-shadow 0.3s;
            }
            .LishHead input[type="text"]:focus {
                box-shadow: 0 0 5px #007bff;
                outline: none;
            }

            .LishHead input[type="submit"] {
                padding: 10px 20px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                transition: background-color 0.3s, transform 0.2s;
            }
            .LishHead input[type="submit"]:hover {
                background-color: #0056b3;
                transform: scale(1.05);
            }

            .LishBody table {
                width: 100%;
                border-collapse: collapse;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
                animation: fadeIn 1s ease;
                background: #ffffff;
            }

            .LishBody th {
                background-color: #007bff;
                color: white;
                font-weight: bold;
                text-align: center;
            }

            .LishBody td {
                padding: 12px 14px;
                text-align: left;
                border-bottom: 1px solid #eee;
                background-color: #f8f9fa;
            }

            .LishBody tr:nth-child(even) td {
                background-color: #e9f7ff;
            }

            .LishBody tr:hover td {
                background-color: #d4edff;
            }

            .LishBody a {
                display: inline-block;
                padding: 6px 12px;
                margin: 2px;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                font-weight: 500;
                transition: all 0.3s ease;
            }

            .LishBody a:nth-child(1) {
                background-color: #ffc107;
                border: 1px solid #e0a800;
                color: black;
            }
            .LishBody a:nth-child(1):hover {
                background-color: #e0a800;
                transform: scale(1.05);
            }

            .LishBody a:nth-child(2) {
                background-color: #dc3545;
                border: 1px solid #bd2130;
            }
            .LishBody a:nth-child(2):hover {
                background-color: #bd2130;
                transform: scale(1.05);
            }

            .LishHead a[href="AddNewSupplier.jsp"],
            .but {
                display: inline-block;
                margin-top: 20px;
                color: #007bff;
                border: 1px solid #007bff;
                padding: 8px 16px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 500;
                transition: all 0.3s ease;
            }
            .LishHead a[href="AddNewSupplier.jsp"]:hover,
            .but:hover {
                background-color: #007bff;
                color: white;
                transform: translateY(-2px);
            }

        </style>

    </head>
    <body>
        <c:set var="listSupplier" value="${sessionScope.listSupplier}" />
        <div class="LishHead">
            <h1>List Supplier</h1>
            <form action="SearchListSupplier">
                <input type="text"placeholder="Nhập Tên nhà cung cấp"  name="name">
                <input type="submit" value="Search" name="name">
            </form>
            <a href="AddNewSupplier.jsp">+ Thêm Nhà Cung Cấp Mới</a>
        </div>
        <div class="LishBody">
            <c:if test="${not empty listSupplier}">
                <table border="1px solid">
                    <tr>
                        <td>ID</td>
                        <td>Name</td>
                        <td>Phone</td>
                        <td>Email</td>
                        <td>Address</td>
                        <td>Note</td>
                        <td colspan="3">Option</td>

                    </tr>
                    <c:forEach var="listItem" items="${listSupplier}">
                        <tr>
                            <td>${listItem.supplierID}</td>
                            <td>${listItem.name}</td>
                            <td>${listItem.phone}</td>
                            <td>${listItem.email}</td>
                            <td>${listItem.address}</td>
                            <td>${listItem.note}</td>
                            <td>
                                <a href="UpdateSupplier.jsp?id=${listItem.supplierID}&name=${listItem.name}&phone=${listItem.phone}&email=${listItem.email}&address=${listItem.address}&note=${listItem.note}">Sửa</a>
                                <a href="DeleteSupplier?id=${listItem.supplierID}" 
                                   onclick="return confirm('Bạn có chắc chắn muốn xoá nhà cung cấp này không?')">Xoá</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
        <a class="but" href="url">Back</a>
    </body>
</html>
