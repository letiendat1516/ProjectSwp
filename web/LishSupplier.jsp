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
                background-color: purple;
                transform: scale(1.05);
            }

            .LishBody a:nth-child(2) {
                background-color: #dc3545;
                border: 1px solid #bd2130;
            }
            .LishBody a:nth-child(2):hover {
                background-color: purple;
                transform: scale(1.05);
            }

            .LishBody a:nth-child(3) {
                background-color: green;
                border: 1px solid #bd2130;
                color: yellow;
            }
            .LishBody a:nth-child(3):hover {
                background-color: purple;
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
            .pagination-container {
                text-align: center;
                margin-top: 20px;
            }

            .pagination-link {
                display: inline-block;
                margin: 0 4px;
                padding: 8px 14px;
                background-color: #f0f0f0;
                border-radius: 6px;
                border: 1px solid #ccc;
                color: #333;
                text-decoration: none;
                transition: all 0.3s ease;
            }

            .pagination-link:hover {
                background-color: #007bff;
                color: white;
            }

            .pagination-link.active-page {
                background-color: #007bff;
                color: white;
                font-weight: bold;
                border-color: #0056b3;
            }

            .filter{
                height: 35px;
                width: 100px;
            }


        </style>

    </head>
    <body>

        <c:set var="currentPage" value="${requestScope.currentPage}"/>
        <c:set var="listSupplier" value="${listSupplier}" />
        <c:set var="option" value="${requestScope.filter}"/>
        <c:set var="status" value="${requestScope.status}"/>
        <c:set var="name" value="${requestScope.name}"/>
        <c:set var="line" value="${requestScope.line}"/>
        <div class="LishHead">
            <h1>List Supplier</h1>
            <form action="SearchListSupplier">
                <select class="filter" name="status">
                    <option value="all" ${status == 'all' ? 'selected="selected"' : ''}>All Status</option>
                    <option value="1" ${status == '1' ? 'selected="selected"' : ''}>Active</option>
                    <option value="0" ${status == '0' ? 'selected="selected"' : ''}>Inactive</option>
                </select>
                <select class="filter" name="line">
                    <option value="7" ${line == '7'?'selected=selected':''}>7 Line</option>
                    <option value="10" ${line == '10'?'selected=selected':''}>10 Line</option>
                    <option value="15" ${line == '15'?'selected=selected':''}>15 Line</option>
                </select>
                <input type="text"placeholder="Nhập Tên nhà cung cấp" value="${name}"  name="name">
                <input type="submit" value="Search" name="name">
            </form>
            <a href="AddNewSupplier.jsp">+ Add new supplier</a>
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
                        <td>Status</td>
                        <td>Date Create</td>
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
                            <c:choose>
                                <c:when test="${listItem.activeFlag == 1}">
                                    <td style="color: red;font-weight: bold">Active</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="font-weight: bold">Inactive</td>
                                </c:otherwise>
                            </c:choose>
                            <td>${listItem.createDate}</td>      
                            <td>
                                <a href="UpdateSupplier.jsp?id=${listItem.supplierID}&name=${listItem.name}&phone=${listItem.phone}&email=${listItem.email}&address=${listItem.address}&note=${listItem.note}">Edit</a>
                                <c:choose>
                                    <c:when test="${listItem.activeFlag == 1}">
                                        <a href="DeleteSupplier?id=${listItem.supplierID}&filter=${option}&status=${status}&name=${name}&line=${line}&currentPage=${currentPage}" 
                                           onclick="return confirm('Bạn có chắc chắn muốn xoá nhà cung cấp này không?')">Delete</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a style="background-color: green;border: 1px solid #bd2130; color: yellow;"
                                           href="ActiveSupplier?id=${listItem.supplierID}&filter=${option}&status=${status}&name=${name}&line=${line}"
                                           onclick="return confirm('Bạn có chắc chắn muốn active nhà cung cấp này không?')"
                                           >Active</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a style="background-color: aquamarine;border: 1px solid #bd2130; color: #0d6efd;"
                                   href="TableSupplierEvaluation?id=${listItem.supplierID}" 
                                   >Evaluation</a>
                                <a style="background-color: #ccc;border: 1px solid #bd2130; color: #0d6efd;"
                                   href="ViewSupplierEvaluation?supplierID=${listItem.supplierID}" 
                                   >View evaluation</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
        <c:if test="${empty option }">
            <c:if test="${totalPages > 1}">
                <div class="pagination-container">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a class="pagination-link ${i == currentPage ? 'active-page' : ''}" 
                           href="LishSupplier?page=${i}">${i}</a>
                    </c:forEach>
                </div>
            </c:if>
        </c:if>
        <c:if test="${not empty option}">
            <c:if test="${totalPages > 1}">
                <div class="pagination-container">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a class="pagination-link ${i == currentPage ? 'active-page' : ''}" 
                           href="SearchListSupplier?status=${status}&name=${name}&line=${line}&page=${i}">${i}</a>
                    </c:forEach>
                </div>
            </c:if>
        </c:if>
        <a class="but" href="categoriesforward.jsp">Back</a>
    </body>
</html>
