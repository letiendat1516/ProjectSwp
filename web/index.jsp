<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Warehouse Manager</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: #ffffff;
            color: #2c3e50;
            line-height: 1.6;
            overflow-x: hidden;
        }

        /* Navigation */
        nav {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 80px;
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.05);
            z-index: 1000;
            animation: slideDown 0.5s ease-out;
        }

        @keyframes slideDown {
            from {
                transform: translateY(-100%);
            }
            to {
                transform: translateY(0);
            }
        }

        .logo {
            font-size: 28px;
            font-weight: 700;
            background: linear-gradient(135deg, #00d4aa, #00b894);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-decoration: none;
            transition: transform 0.3s ease;
        }

        .logo:hover {
            transform: scale(1.05);
        }

        .nav-links {
            display: flex;
            list-style: none;
            gap: 50px;
            align-items: center;
        }

        .nav-links a {
            text-decoration: none;
            color: #718096;
            font-size: 16px;
            font-weight: 500;
            transition: all 0.3s ease;
            position: relative;
        }

        .nav-links a::after {
            content: '';
            position: absolute;
            bottom: -5px;
            left: 0;
            width: 0;
            height: 3px;
            background: linear-gradient(90deg, #00d4aa, #00b894);
            transition: width 0.3s ease;
            border-radius: 2px;
        }

        .nav-links a:hover {
            color: #00d4aa;
        }

        .nav-links a:hover::after {
            width: 100%;
        }

        .nav-icons {
            display: flex;
            gap: 25px;
            align-items: center;
        }

        .nav-icon {
            width: 24px;
            height: 24px;
            cursor: pointer;
            transition: all 0.3s ease;
            color: #718096;
        }

        .nav-icon:hover {
            transform: translateY(-2px);
            color: #00d4aa;
        }

        /* Hero Section */
        .hero {
            min-height: 100vh;
            display: grid;
            grid-template-columns: 1fr 1fr;
            align-items: center;
            padding: 100px 80px 50px;
            background: linear-gradient(135deg, #f5f7fa 0%, #ffffff 100%);
            position: relative;
            overflow: hidden;
        }

        /* Background decoration */
        .hero::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -20%;
            width: 800px;
            height: 800px;
            background: radial-gradient(circle, rgba(0, 212, 170, 0.1) 0%, transparent 70%);
            border-radius: 50%;
            animation: pulse 4s ease-in-out infinite;
        }

        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.05);
            }
        }

        .hero-content {
            z-index: 2;
            animation: fadeInLeft 1s ease-out;
        }

        @keyframes fadeInLeft {
            from {
                opacity: 0;
                transform: translateX(-50px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        .hero h1 {
            font-size: 72px;
            font-weight: 800;
            line-height: 1.1;
            margin-bottom: 30px;
            letter-spacing: -2px;
        }

        .hero h1 .highlight {
            background: linear-gradient(135deg, #00d4aa, #00b894);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            display: inline-block;
            animation: gradientShift 3s ease infinite;
        }

        @keyframes gradientShift {
            0%, 100% {
                filter: hue-rotate(0deg);
            }
            50% {
                filter: hue-rotate(20deg);
            }
        }

        .hero h1 .block {
            display: block;
            color: #2c3e50;
        }

        .hero-description {
            color: #718096;
            font-size: 20px;
            margin-bottom: 50px;
            line-height: 1.8;
            max-width: 600px;
        }

        .cta-container {
            display: flex;
            gap: 20px;
            align-items: center;
        }

        .cta-button {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            background: linear-gradient(135deg, #00d4aa, #00b894);
            color: white;
            padding: 18px 45px;
            border-radius: 50px;
            text-decoration: none;
            font-weight: 600;
            font-size: 18px;
            transition: all 0.3s ease;
            box-shadow: 0 10px 30px rgba(0, 212, 170, 0.3);
            position: relative;
            overflow: hidden;
        }

        .cta-button::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.2);
            transition: left 0.5s ease;
        }

        .cta-button:hover::before {
            left: 100%;
        }

        .cta-button:hover {
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(0, 212, 170, 0.4);
        }

        .secondary-link {
            color: #718096;
            text-decoration: none;
            font-size: 16px;
            transition: color 0.3s ease;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .secondary-link:hover {
            color: #00d4aa;
        }

        /* Hero Illustration */
        .hero-illustration {
            position: relative;
            z-index: 2;
            animation: fadeInRight 1s ease-out;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        @keyframes fadeInRight {
            from {
                opacity: 0;
                transform: translateX(50px);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        /* Floating elements */
        .float-element {
            position: absolute;
            animation: float 4s ease-in-out infinite;
        }

        @keyframes float {
            0%, 100% {
                transform: translateY(0) rotate(0deg);
            }
            50% {
                transform: translateY(-30px) rotate(10deg);
            }
        }

        .float-1 {
            top: 10%;
            left: 10%;
            animation-delay: 0s;
        }

        .float-2 {
            bottom: 20%;
            right: 15%;
            animation-delay: 1s;
        }

        .float-3 {
            top: 40%;
            right: 10%;
            animation-delay: 2s;
        }

        /* Main illustration SVG */
        .main-illustration {
            width: 600px;
            height: auto;
            filter: drop-shadow(0 20px 40px rgba(0, 0, 0, 0.1));
        }

        /* Features Section */
        .features {
            padding: 120px 80px;
            background: #f8fafc;
            position: relative;
        }

        .features::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 100px;
            background: linear-gradient(to bottom, #ffffff, transparent);
            pointer-events: none;
        }

        .section-header {
            text-align: center;
            margin-bottom: 80px;
        }

        .section-header h2 {
            font-size: 48px;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 20px;
            position: relative;
            display: inline-block;
        }

        .section-header h2::after {
            content: '';
            position: absolute;
            bottom: -15px;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 5px;
            background: linear-gradient(90deg, #00d4aa, #00b894);
            border-radius: 3px;
        }

        .section-header p {
            font-size: 20px;
            color: #718096;
            max-width: 600px;
            margin: 0 auto;
        }

        .features-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
            gap: 50px;
            margin-top: 60px;
        }

        .feature-card {
            background: white;
            padding: 50px 40px;
            border-radius: 24px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.05);
            transition: all 0.4s ease;
            position: relative;
            overflow: hidden;
        }

        .feature-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 6px;
            background: linear-gradient(90deg, #00d4aa, #00b894);
            transform: scaleX(0);
            transform-origin: left;
            transition: transform 0.4s ease;
        }

        .feature-card:hover::before {
            transform: scaleX(1);
        }

        .feature-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
        }

        .feature-icon {
            width: 100px;
            height: 100px;
            background: linear-gradient(135deg, #e6fffa, #b2f5ea);
            border-radius: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 30px;
            font-size: 48px;
            transition: all 0.4s ease;
        }

        .feature-card:hover .feature-icon {
            background: linear-gradient(135deg, #00d4aa, #00b894);
            transform: scale(1.1) rotate(5deg);
            box-shadow: 0 10px 30px rgba(0, 212, 170, 0.3);
        }

        .feature-card:hover .feature-icon {
            filter: brightness(2);
        }

        .feature-card h3 {
            font-size: 26px;
            color: #2c3e50;
            margin-bottom: 20px;
            font-weight: 600;
        }

        .feature-card p {
            color: #718096;
            line-height: 1.8;
            font-size: 17px;
        }

        /* Stats Section */
        .stats {
            padding: 100px 80px;
            background: linear-gradient(135deg, #00d4aa, #00b894);
            position: relative;
            overflow: hidden;
        }

        .stats::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -20%;
            width: 600px;
            height: 600px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            animation: rotate 20s linear infinite;
        }

        @keyframes rotate {
            from {
                transform: rotate(0deg);
            }
            to {
                transform: rotate(360deg);
            }
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 50px;
            position: relative;
            z-index: 2;
        }

        .stat-card {
            text-align: center;
            color: white;
        }

        .stat-number {
            font-size: 64px;
            font-weight: 800;
            margin-bottom: 10px;
            display: block;
            animation: countUp 2s ease-out;
        }

        @keyframes countUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .stat-label {
            font-size: 20px;
            opacity: 0.9;
        }

        /* CTA Section */
        .cta-section {
            padding: 120px 80px;
            background: #ffffff;
            text-align: center;
        }

        .cta-section h2 {
            font-size: 48px;
            color: #2c3e50;
            margin-bottom: 30px;
        }

        .cta-section p {
            font-size: 20px;
            color: #718096;
            margin-bottom: 50px;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        /* Footer */
        footer {
            background: #1a202c;
            color: white;
            padding: 60px 80px 30px;
        }

        .footer-content {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr 1fr;
            gap: 50px;
            margin-bottom: 50px;
        }

        .footer-brand h3 {
            font-size: 28px;
            margin-bottom: 20px;
            background: linear-gradient(135deg, #00d4aa, #00b894);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .footer-brand p {
            color: #a0aec0;
            line-height: 1.8;
        }

        .footer-column h4 {
            font-size: 18px;
            margin-bottom: 20px;
            color: #ffffff;
        }

        .footer-column ul {
            list-style: none;
        }

        .footer-column ul li {
            margin-bottom: 12px;
        }

        .footer-column ul li a {
            color: #a0aec0;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .footer-column ul li a:hover {
            color: #00d4aa;
        }

        .footer-bottom {
            border-top: 1px solid #2d3748;
            padding-top: 30px;
            text-align: center;
            color: #a0aec0;
        }

        /* Responsive */
        @media (max-width: 1024px) {
            nav {
                padding: 20px 40px;
            }

            .nav-links {
                display: none;
            }

            .hero {
                grid-template-columns: 1fr;
                padding: 100px 40px 50px;
                text-align: center;
            }

            .hero h1 {
                font-size: 48px;
            }

            .hero-description {
                margin-left: auto;
                margin-right: auto;
            }

            .cta-container {
                justify-content: center;
            }

            .main-illustration {
                width: 100%;
                max-width: 500px;
                margin-top: 50px;
            }

            .features {
                padding: 80px 40px;
            }

            .footer-content {
                grid-template-columns: 1fr 1fr;
            }
        }

        @media (max-width: 768px) {
            .hero h1 {
                font-size: 36px;
            }

            .hero-description {
                font-size: 18px;
            }

            .features-grid {
                grid-template-columns: 1fr;
            }

            .stats-grid {
                grid-template-columns: 1fr 1fr;
            }

            .footer-content {
                grid-template-columns: 1fr;
                text-align: center;
            }
        }
    </style>
</head>
<body>

    <!-- Navigation -->
    <nav>
        <a href="#" class="logo">WAREHOUSE</a>
        <ul class="nav-links">
            <li><a href="#">Trang chủ</a></li>
            <li><a href="#">Tính năng</a></li>
            <li><a href="#">Giải pháp</a></li>
            <li><a href="#">Giá cả</a></li>
            <li><a href="#">Về chúng tôi</a></li>
            <li><a href="#">Liên hệ</a></li>
        </ul>
        <div class="nav-icons">
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
            </svg>
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
            </svg>
            <svg class="nav-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"></path>
            </svg>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero">
        <div class="hero-content">
            <h1>
                <span class="highlight">QUẢN LÝ KHO</span>
                <span class="block">THÔNG MINH & HIỆN ĐẠI</span>
            </h1>
            <p class="hero-description">
                Giải pháp toàn diện giúp doanh nghiệp tối ưu hóa quy trình quản lý kho hàng, 
                tiết kiệm chi phí và tăng hiệu quả hoạt động với công nghệ tiên tiến nhất.
            </p>
            <div class="cta-container">
                <a href="login.jsp" class="cta-button">
                    Đăng nhập
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                    </svg>
                </a>
            </div>
        </div>

        <div class="hero-illustration">
            <!-- Floating decorative elements -->
            <div class="float-element float-1">
                <svg width="60" height="60" viewBox="0 0 60 60" fill="none">
                    <circle cx="30" cy="30" r="30" fill="#00d4aa" opacity="0.1"/>
                    <circle cx="30" cy="30" r="20" fill="#00d4aa" opacity="0.2"/>
                    <circle cx="30" cy="30" r="10" fill="#00d4aa" opacity="0.3"/>
                </svg>
            </div>
            <div class="float-element float-2">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                    <rect width="40" height="40" rx="8" fill="#FFB800" opacity="0.2"/>
                    <rect x="10" y="10" width="20" height="20" rx="4" fill="#FFB800" opacity="0.3"/>
                </svg>
            </div>
            <div class="float-element float-3">
                <svg width="50" height="50" viewBox="0 0 50 50" fill="none">
                    <path d="M25 0L30.6066 19.3934L50 25L30.6066 30.6066L25 50L19.3934 30.6066L0 25L19.3934 19.3934L25 0Z" fill="#4A90E2" opacity="0.15"/>
                </svg>
            </div>

            <!-- Main SVG Illustration -->
            <svg class="main-illustration" viewBox="0 0 600 500" fill="none">
                <!-- Background elements -->
                <circle cx="300" cy="250" r="220" fill="#E6F7FF" opacity="0.3"/>
                <circle cx="300" cy="250" r="180" fill="#E6F7FF" opacity="0.5"/>
                
                <!-- Warehouse building -->
                <rect x="150" y="200" width="300" height="200" rx="10" fill="#E8F5FF" stroke="#4A90E2" stroke-width="2"/>
                <rect x="150" y="170" width="300" height="30" fill="#4A90E2"/>
                <text x="300" y="190" text-anchor="middle" fill="white" font-size="16" font-weight="bold">WAREHOUSE</text>
                
                <!-- Shelves -->
                <rect x="180" y="250" width="60" height="120" fill="#FFB800" opacity="0.8"/>
                <rect x="180" y="250" width="60" height="5" fill="#FF8C00"/>
                <rect x="180" y="290" width="60" height="5" fill="#FF8C00"/>
                <rect x="180" y="330" width="60" height="5" fill="#FF8C00"/>
                
                <rect x="270" y="250" width="60" height="120" fill="#FFB800" opacity="0.8"/>
                <rect x="270" y="250" width="60" height="5" fill="#FF8C00"/>
                <rect x="270" y="290" width="60" height="5" fill="#FF8C00"/>
                <rect x="270" y="330" width="60" height="5" fill="#FF8C00"/>
                
                <rect x="360" y="250" width="60" height="120" fill="#FFB800" opacity="0.8"/>
                <rect x="360" y="250" width="60" height="5" fill="#FF8C00"/>
                <rect x="360" y="290" width="60" height="5" fill="#FF8C00"/>
                <rect x="360" y="330" width="60" height="5" fill="#FF8C00"/>
                
                <!-- Boxes on shelves -->
                <rect x="185" y="260" width="20" height="20" rx="2" fill="#4A90E2"/>
                <rect x="215" y="260" width="20" height="20" rx="2" fill="#00D4AA"/>
                <rect x="185" y="300" width="50" height="25" rx="2" fill="#FF6B6B"/>
                <rect x="185" y="340" width="30" height="25" rx="2" fill="#4ECDC4"/>
                
                <!-- Person with clipboard -->
                <circle cx="480" cy="280" r="20" fill="#FFE0B2"/>
                <rect x="460" y="300" width="40" height="60" rx="5" fill="#4A90E2"/>
                <rect x="450" y="310" width="12" height="35" rx="6" fill="#4A90E2"/>
                <rect x="498" y="310" width="12" height="35" rx="6" fill="#4A90E2"/>
                <rect x="455" y="355" width="12" height="35" rx="6" fill="#333"/>
                <rect x="493" y="355" width="12" height="35" rx="6" fill="#333"/>
                
                <!-- Clipboard -->
                <rect x="510" y="290" width="60" height="80" rx="5" fill="white" stroke="#00D4AA" stroke-width="2"/>
                <rect x="525" y="285" width="30" height="10" rx="5" fill="#00D4AA"/>
                <line x1="520" y1="310" x2="560" y2="310" stroke="#E0E0E0" stroke-width="2"/>
                <line x1="520" y1="325" x2="560" y2="325" stroke="#E0E0E0" stroke-width="2"/>
                <line x1="520" y1="340" x2="560" y2="340" stroke="#E0E0E0" stroke-width="2"/>
                <line x1="520" y1="355" x2="560" y2="355" stroke="#E0E0E0" stroke-width="2"/>
                
                <!-- Forklift -->
                <rect x="50" y="340" width="80" height="40" rx="5" fill="#FFC107"/>
                <rect x="40" y="380" width="100" height="10" fill="#333"/>
                <circle cx="60" cy="390" r="12" fill="#666"/>
                <circle cx="120" cy="390" r="12" fill="#666"/>
                <rect x="35" y="320" width="40" height="5" fill="#666"/>
                <rect x="35" y="310" width="5" height="30" fill="#666"/>
                
                <!-- Decorative plants -->
                <path d="M500 400 Q510 380 520 400 Q530 380 540 400" stroke="#00D4AA" stroke-width="3" fill="none"/>
                <path d="M60 400 Q70 380 80 400 Q90 380 100 400" stroke="#00D4AA" stroke-width="3" fill="none"/>
            </svg>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features">
        <div class="section-header">
            <h2>Tính năng vượt trội</h2>
            <p>Hệ thống quản lý kho hàng với đầy đủ tính năng giúp tối ưu hóa quy trình làm việc</p>
        </div>

        <div class="features-grid">
            <div class="feature-card">
                <div class="feature-icon">📊</div>
                <h3>Báo cáo thời gian thực</h3>
                <p>Theo dõi tình trạng kho hàng, xuất nhập tồn với báo cáo chi tiết và biểu đồ trực quan, cập nhật liên tục.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🚀</div>
                <h3>Tự động hóa quy trình</h3>
                <p>Tự động cảnh báo hết hàng, đề xuất nhập kho thông minh và tối ưu hóa không gian lưu trữ.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🔒</div>
                <h3>Bảo mật đa lớp</h3>
                <p>Mã hóa dữ liệu AES-256, xác thực 2 yếu tố và sao lưu tự động đảm bảo an toàn tuyệt đối.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">📱</div>
                <h3>Đa nền tảng</h3>
                <p>Sử dụng mọi lúc mọi nơi trên máy tính, điện thoại, máy tính bảng với giao diện responsive.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🔄</div>
                <h3>Đồng bộ tức thì</h3>
                <p>Dữ liệu được đồng bộ realtime giữa các chi nhánh, kho hàng và phòng ban khác nhau.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">💡</div>
                <h3>AI dự báo thông minh</h3>
                <p>Sử dụng trí tuệ nhân tạo để dự báo nhu cầu, phân tích xu hướng và đưa ra gợi ý tối ưu.</p>
            </div>
        </div>
    </section>

    <!-- Stats Section -->
    <section class="stats">
        <div class="stats-grid">
            <div class="stat-card">
                <span class="stat-number">10K+</span>
                <span class="stat-label">Doanh nghiệp tin dùng</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">50M+</span>
                <span class="stat-label">Sản phẩm được quản lý</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">99.9%</span>
                <span class="stat-label">Uptime hệ thống</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">24/7</span>
                <span class="stat-label">Hỗ trợ khách hàng</span>
            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
        <h2>Sẵn sàng nâng cấp quản lý kho hàng?</h2>
        <p>Trải nghiệm miễn phí 30 ngày với đầy đủ tính năng. Không cần thẻ tín dụng.</p>
        <a href="login.jsp" class="cta-button">Đăng ký ngay</a>
    </section>

    <!-- Footer -->
    <footer>
        <div class="footer-content">
            <div class="footer-brand">
                <h3>WAREHOUSE MANAGER</h3>
                <p>Giải pháp quản lý kho hàng thông minh hàng đầu Việt Nam, được tin dùng bởi hàng nghìn doanh nghiệp.</p>
            </div>
            <div class="footer-column">
                <h4>Sản phẩm</h4>
                <ul>
                    <li><a href="#">Tính năng</a></li>
                    <li><a href="#">Bảng giá</a></li>
                    <li><a href="#">Tích hợp</a></li>
                    <li><a href="#">API</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h4>Công ty</h4>
                <ul>
                    <li><a href="#">Về chúng tôi</a></li>
                    <li><a href="#">Blog</a></li>
                    <li><a href="#">Tuyển dụng</a></li>
                    <li><a href="#">Liên hệ</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h4>Hỗ trợ</h4>
                <ul>
                    <li><a href="#">Tài liệu</a></li>
                    <li><a href="#">Hướng dẫn</a></li>
                    <li><a href="#">FAQ</a></li>
                    <li><a href="#">Cộng đồng</a></li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <p>&copy; 2025 Warehouse Manager. Được phát triển bởi Lê Tiến Đạt. All rights reserved.</p>
        </div>
    </footer>

</body>
</html>