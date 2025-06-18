<%-- 
    Document   : Dashboard
    Created on : May 19, 2025, 9:38:23 PM
    Author     : phucn
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.Users" session="true" %> 
<!DOCTYPE html>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>

<%@page import="model.Users"%>
<%
    Users user = (Users) session.getAttribute("user");
    if (user == null || !"Admin".equalsIgnoreCase(user.getRoleName())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Warehouse Manager</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: Arial, sans-serif;
                background: #f5f5f5;
                color: #333;
                line-height: 1.6;
            }

            .container {
                display: flex;
                min-height: 100vh;
            }

            /* Sidebar Styles - Simple */
            .sidebar {
                width: 280px;
                background: white;
                border-right: 1px solid #ddd;
                box-shadow: 2px 0 5px rgba(0,0,0,0.1);
                position: relative;
            }

            .sidebar-header {
                padding: 20px;
                text-align: center;
                background: #f8f9fa;
                border-bottom: 1px solid #ddd;
            }

            .sidebar-header h2 {
                font-size: 1.5rem;
                font-weight: bold;
                margin-bottom: 5px;
                color: #333;
            }

            .sidebar-header .subtitle {
                font-size: 0.9rem;
                color: #666;
            }

            .sidebar-nav {
                padding: 20px 0;
            }

            .nav-section {
                margin-bottom: 25px;
            }

            .nav-section-title {
                padding: 0 20px 10px;
                font-size: 0.8rem;
                color: #666;
                text-transform: uppercase;
                letter-spacing: 1px;
                font-weight: bold;
                border-bottom: 1px solid #eee;
                margin-bottom: 10px;
            }

            .nav-item {
                display: block;
                padding: 12px 20px;
                color: #333;
                text-decoration: none;
                font-size: 14px;
                border-left: 3px solid transparent;
                transition: all 0.2s ease;
                position: relative;
            }

            .nav-item:hover {
                background: #f8f9fa;
                border-left-color: #007bff;
                padding-left: 25px;
            }

            .nav-item.active {
                background: #e7f3ff;
                border-left-color: #007bff;
                font-weight: bold;
                color: #007bff;
            }

            .nav-item .icon {
                margin-right: 10px;
                font-size: 16px;
                width: 18px;
                display: inline-block;
                text-align: center;
            }

            /* Main Content */
            .main-content {
                flex: 1;
                background: #f5f5f5;
                display: flex;
                flex-direction: column;
            }

            /* Header */
            .header {
                background: white;
                padding: 20px 30px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                display: flex;
                justify-content: space-between;
                align-items: center;
                border-bottom: 1px solid #ddd;
            }

            .header-title {
                font-size: 1.8rem;
                font-weight: bold;
                color: #333;
                margin: 0;
            }

            .header-user {
                display: flex;
                align-items: center;
                gap: 15px;
            }

            .user-info {
                display: flex;
                align-items: center;
                gap: 10px;
                padding: 8px 15px;
                background: #f8f9fa;
                border-radius: 20px;
                border: 1px solid #ddd;
            }

            .user-avatar {
                width: 32px;
                height: 32px;
                background: #007bff;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                font-weight: bold;
                font-size: 14px;
            }

            .user-name {
                font-weight: bold;
                color: #333;
            }

            .logout-btn {
                background: #dc3545;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                text-decoration: none;
                font-size: 14px;
                font-weight: 500;
            }

            .logout-btn:hover {
                background: #c82333;
            }

            /* Dashboard Content */
            .dashboard-content {
                flex: 1;
                padding: 30px;
                max-width: 1200px;
                margin: 0 auto;
                width: 100%;
            }

            .page-header {
                text-align: center;
                margin-bottom: 30px;
                padding-bottom: 20px;
                border-bottom: 2px solid #eee;
            }

            .page-title {
                font-size: 2rem;
                color: #333;
                margin-bottom: 10px;
            }

            .page-subtitle {
                color: #666;
                font-size: 1rem;
            }

            /* System Info */
            .system-info {
                background: #fff3cd;
                border: 1px solid #ffeaa7;
                border-radius: 5px;
                padding: 15px;
                margin-bottom: 20px;
            }

            .system-info-title {
                font-weight: bold;
                color: #856404;
                margin-bottom: 10px;
            }

            .system-info-text {
                color: #856404;
                font-size: 0.9rem;
            }

            /* Welcome Section */
            .welcome-section {
                background: white;
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 25px;
                margin-bottom: 30px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .welcome-title {
                font-size: 1.5rem;
                color: #333;
                margin-bottom: 15px;
            }

            .welcome-text {
                color: #666;
                line-height: 1.6;
            }

            /* Stats Grid */
            .stats-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }

            .stat-card {
                background: white;
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 20px;
                text-align: center;
                transition: transform 0.2s ease;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .stat-card:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0,0,0,0.15);
            }

            .stat-card.users {
                border-left: 4px solid #007bff;
            }

            .stat-card.materials {
                border-left: 4px solid #28a745;
            }

            .stat-card.transactions {
                border-left: 4px solid #ffc107;
            }

            .stat-card.roles {
                border-left: 4px solid #6f42c1;
            }

            .stat-icon {
                font-size: 2rem;
                margin-bottom: 10px;
                opacity: 0.7;
            }

            .stat-title {
                font-size: 0.9rem;
                color: #666;
                text-transform: uppercase;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .stat-value {
                font-size: 2rem;
                font-weight: bold;
                color: #333;
                margin-bottom: 5px;
            }

            .stat-change {
                font-size: 0.8rem;
                color: #28a745;
            }

            /* Quick Actions */
            .quick-actions {
                background: white;
                border: 1px solid #ddd;
                border-radius: 5px;
                overflow: hidden;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .quick-actions-header {
                background: #f8f9fa;
                padding: 15px 20px;
                border-bottom: 1px solid #ddd;
            }

            .quick-actions-title {
                font-size: 1.2rem;
                color: #333;
                margin: 0;
                font-weight: bold;
            }

            .actions-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 0;
            }

            .action-item {
                display: flex;
                align-items: center;
                padding: 20px;
                text-decoration: none;
                color: #333;
                border-bottom: 1px solid #eee;
                border-right: 1px solid #eee;
                transition: background-color 0.2s ease;
            }

            .action-item:hover {
                background: #f8f9fa;
            }

            .action-item:last-child {
                border-bottom: none;
            }

            .action-icon {
                margin-right: 15px;
                font-size: 1.5rem;
                color: #007bff;
            }

            .action-text {
                font-weight: 500;
            }

            .action-description {
                font-size: 0.9rem;
                color: #666;
                margin-top: 5px;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .container {
                    flex-direction: column;
                }
                
                .sidebar {
                    width: 100%;
                    order: 2;
                }
                
                .main-content {
                    order: 1;
                }
                
                .header {
                    padding: 15px 20px;
                }
                
                .header-title {
                    font-size: 1.5rem;
                }
                
                .dashboard-content {
                    padding: 20px;
                }
                
                .stats-grid {
                    grid-template-columns: 1fr;
                }
                
                .actions-grid {
                    grid-template-columns: 1fr;
                }

                .action-item {
                    border-right: none;
                }

                .sidebar-nav {
                    display: flex;
                    overflow-x: auto;
                    padding: 10px;
                }

                .nav-section {
                    margin-bottom: 0;
                    margin-right: 20px;
                    min-width: 200px;
                }
            }

            /* Utilities */
            .text-center {
                text-align: center;
            }

            .mb-3 {
                margin-bottom: 1rem;
            }

            .mb-4 {
                margin-bottom: 1.5rem;
            }

            .text-muted {
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <!-- Sidebar -->
            <div class="sidebar">
                <div class="sidebar-header">
                    <h2>🏭 Warehouse</h2>
                    <div class="subtitle">Management System</div>
                </div>
                
                <nav class="sidebar-nav">
                    <div class="nav-section">
                        <div class="nav-section-title">Quản lý chính</div>
                        <a href="admin" class="nav-item">
                            <span class="icon">👥</span>
                            User Manager
                        </a>
                        <a href="roleAssignment" class="nav-item">
                            <span class="icon">🔐</span>
                            Role Assignment
                        </a>
                        <a href="categoriesforward.jsp" class="nav-item">
                            <span class="icon">📦</span>
                            Material Information
                        </a>
                    </div>
                    
                    <div class="nav-section">
                        <div class="nav-section-title">Phê duyệt</div>
                        <a href="ApproveListForward.jsp" class="nav-item">
                            <span class="icon">✅</span>
                            Approve
                        </a>
                    </div>
                    
                    <div class="nav-section">
                        <div class="nav-section-title">Giao dịch & Thống kê</div>
                        <a href="RequestForward.jsp" class="nav-item">
                            <span class="icon">💼</span>
                            Transaction
                        </a>
                        <a href="#" class="nav-item">
                            <span class="icon">📊</span>
                            Statistic
                        </a>
                    </div>
                </nav>
            </div>

            <!-- Main Content -->
            <div class="main-content">
                <!-- Header -->
                <div class="header">
                    <h1 class="header-title">📊 Admin Dashboard</h1>
                    <div class="header-user">
                        <div class="user-info">
                            <div class="user-avatar">A</div>
                            <span class="user-name">Admin</span>
                        </div>
                        <a href="logout" class="logout-btn">🚪 Log out</a>
                    </div>
                </div>

                <!-- Dashboard Content -->
                <div class="dashboard-content">
                    <div class="page-header">
                        <h1 class="page-title">Admin Dashboard</h1>
                        <p class="page-subtitle">Tổng quan hệ thống quản lý kho hàng</p>
                    </div>

                    <!-- Welcome Section -->
                    <div class="welcome-section">
                        <h2 class="welcome-title">🎉 Chào mừng đến với Warehouse Manager!</h2>
                        <p class="welcome-text">
                            Hệ thống quản lý kho hàng toàn diện giúp bạn theo dõi và quản lý hiệu quả tất cả các hoạt động trong kho. 
                            Sử dụng menu bên trái để truy cập các chức năng quản lý khác nhau.
                        </p>
                    </div>

                </div>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                // Update current time
                function updateTime() {
                    const now = new Date();
                    const timeString = now.toLocaleTimeString('vi-VN');
                    const dateString = now.toLocaleDateString('vi-VN');
                    
                    // Update time display if element exists
                    const timeElement = document.getElementById('current-time');
                    if (timeElement) {
                        timeElement.textContent = `${dateString} ${timeString}`;
                    }
                }

                updateTime();
                setInterval(updateTime, 1000);

                // Add active state to current nav item
                const currentPath = window.location.pathname;
                const navItems = document.querySelectorAll('.nav-item');
                
                navItems.forEach(item => {
                    const href = item.getAttribute('href');
                    if (href && currentPath.includes(href)) {
                        item.classList.add('active');
                    }
                });

                // Add click animation to cards
                const cards = document.querySelectorAll('.stat-card, .action-item');
                cards.forEach(card => {
                    card.addEventListener('click', function() {
                        this.style.transform = 'scale(0.98)';
                        setTimeout(() => {
                            this.style.transform = '';
                        }, 150);
                    });
                });

                // Auto-refresh stats (if needed)
                function refreshStats() {
                    // Add AJAX call to refresh statistics if needed
                    console.log('Stats refreshed at:', new Date().toLocaleTimeString());
                }

                // Refresh stats every 5 minutes
                setInterval(refreshStats, 300000);
            });

            // Smooth scrolling for internal links
            document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                anchor.addEventListener('click', function (e) {
                    e.preventDefault();
                    const target = document.querySelector(this.getAttribute('href'));
                    if (target) {
                        target.scrollIntoView({
                            behavior: 'smooth'
                        });
                    }
                });
            });
        </script>
    </body>
</html>
