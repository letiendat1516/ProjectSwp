<%-- 
    Document   : StatisticSupplierEvaluation
    Created on : 17 thg 6, 2025, 10:35:16
    Author     : Fpt06
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Statistic Supplier</title>
        <style>
            /* Reset và base styles */
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
                color: #333;
            }

            /* Container chính */
            .container {
                max-width: 1200px;
                margin: 0 auto;
                background: white;
                border-radius: 15px;
                box-shadow: 0 20px 40px rgba(0,0,0,0.1);
                padding: 30px;
            }

            /* Header */
            h2 {
                text-align: center;
                color: #2c3e50;
                margin-bottom: 30px;
                font-size: 2.5em;
                font-weight: 300;
                position: relative;
            }

            h2::after {
                content: '';
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
                width: 80px;
                height: 3px;
                background: linear-gradient(45deg, #667eea, #764ba2);
                border-radius: 2px;
            }

            /* Form styling */
            form {
                background: #f8f9fa;
                padding: 25px;
                border-radius: 10px;
                margin-bottom: 30px;
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
                justify-content: center;
                box-shadow: 0 5px 15px rgba(0,0,0,0.05);
            }

            /* Select styling */
            select {
                padding: 12px 15px;
                border: 2px solid #e9ecef;
                border-radius: 8px;
                font-size: 14px;
                background: white;
                color: #495057;
                cursor: pointer;
                transition: all 0.3s ease;
                min-width: 200px;
            }

            select:hover {
                border-color: #667eea;
            }

            select:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            /* Submit button */
            input[type="submit"] {
                background: linear-gradient(45deg, #667eea, #764ba2);
                color: white;
                border: none;
                padding: 12px 25px;
                border-radius: 8px;
                font-size: 14px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            input[type="submit"]:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
            }

            input[type="submit"]:active {
                transform: translateY(0);
            }

            /* Table styling */
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
                background: white;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            }

            table tr:first-child {
                background: linear-gradient(45deg, #667eea, #764ba2);
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
                transition: background-color 0.3s ease;
            }

            table tr:not(:first-child):hover {
                background-color: #f8f9fa;
            }

            table tr:not(:first-child):nth-child(even) {
                background-color: #fdfdfd;
            }

            table tr:not(:first-child):nth-child(even):hover {
                background-color: #f0f0f0;
            }

            /* Back link */
            a {
                display: inline-block;
                margin-top: 20px;
                padding: 10px 20px;
                background: #6c757d;
                color: white;
                text-decoration: none;
                border-radius: 5px;
                transition: all 0.3s ease;
                font-weight: 500;
            }

            a:hover {
                background: #5a6268;
                transform: translateY(-1px);
            }

            /* Responsive design */
            @media (max-width: 768px) {
                body {
                    padding: 10px;
                }

                .container {
                    padding: 20px;
                }

                h2 {
                    font-size: 2em;
                }

                form {
                    flex-direction: column;
                    align-items: stretch;
                }

                select {
                    min-width: auto;
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

                h2 {
                    font-size: 1.5em;
                }
            }

            /* Animation cho table load */
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

            table {
                animation: fadeInUp 0.6s ease-out;
            }

            /* Hover effect cho rows */
            table tr:not(:first-child) {
                transition: all 0.3s ease;
            }

            table tr:not(:first-child):hover {
                transform: translateX(5px);
                box-shadow: 5px 0 15px rgba(0,0,0,0.1);
            }
        </style>
    </head>
    <body>
        <c:set var="list" value="${requestScope.list}"/>
        <c:set var="fl" value="${requestScope.fl}"/>
        <c:set var="st" value="${requestScope.st}"/>
        <c:set var="mess" value="${requestScope.mess}"/>
        <c:set var="sta" value="${requestScope.sta}"/>
        <h2 style="font-weight: bold;color: #c3e6cb" >Statistic Evaluation</h2>
        <form action="StatisticSupplierEvaluation">
            <select name="top">
                <option ${fl=="avg"?'selected':''} value="avg">Top-rated Supplier</option>
                <option ${fl=="expexted"?'selected':''} value="expexted">Top-rated expected delivery </option>
                <option ${fl=="market"?'selected':''} value="market">Top-rated market price comparison</option>
            </select>
            <select name="sort">
                <option ${st=="desc" || st==null?'selected':''} value="desc">Sort descending</option>
                <option ${st=="asc"?'selected':''} value="asc">Sort ascending</option>
            </select>
            <select name="status">
                <option ${sta=="ATPX"||sta==null?'selected':''} value="ATPX">All Supplier</option>
                <option ${sta=="active"?'selected':''} value="active">Active Supplier</option>
                <option ${sta=="inactive"?'selected':''} value="inactive">Inactive Supplier</option>
            </select>
            <input type="submit" value="Statistic" name="name">
        </form>
        <c:if test="${not empty list}">
            <h2>${mess}</h2>
            <table border="1px solid">
                <tr>
                    <td>ID</td>
                    <td>Name</td>
                    <td>Phone</td>
                    <td>Email</td>
                    <td>Address</td>
                    <td>Note</td>
                </tr>
                <c:forEach var="listItem" items="${list}">
                    <tr>
                        <td>${listItem.supplierID}</td>
                        <td>${listItem.name}</td>
                        <td>${listItem.phone}</td>
                        <td>${listItem.email}</td>
                        <td>${listItem.address}</td>
                        <td>${listItem.note}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <a href="#">back</a>
    </body>
</html>
