-- USERS
Use swp;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    fullname VARCHAR(100),
    phone VARCHAR(20),
    dob DATE,
    active_flag TINYINT(1) DEFAULT 1,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- ROLE
CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);
-- USER_ROLE
CREATE TABLE user_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
-- CATEGORY
CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id INT,
    active_flag TINYINT(1) DEFAULT 1,
    FOREIGN KEY (parent_id) REFERENCES category(id)
);
-- UNIT
CREATE TABLE unit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    symbol VARCHAR(10) NOT NULL UNIQUE,
    description TEXT,
    type ENUM('Khối lượng', 'Độ dài', 'Số lượng') NOT NULL,
    UNIQUE KEY uk_symbol (symbol),
    UNIQUE KEY uk_name (name)
);
-- PRODUCT INFO
CREATE TABLE product_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    cate_id INT,
    unit_id INT,
    price DECIMAL(15, 2),
    active_flag TINYINT(1) DEFAULT 1,
    status VARCHAR(20),
    description TEXT,
    FOREIGN KEY (cate_id) REFERENCES category(id),
    FOREIGN KEY (unit_id) REFERENCES unit(id)
);
-- PRODUCT STOCK
CREATE TABLE product_in_stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    qty DECIMAL(10, 2),
    status VARCHAR(20),
    FOREIGN KEY (product_id) REFERENCES product_info(id)
);
-- INVOICE (IMPORT/EXPORT)
CREATE TABLE invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    type ENUM('import', 'export') NOT NULL,
    user_id INT,
    status ENUM('pending', 'approved', 'completed'),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- INVOICE DETAIL
CREATE TABLE invoice_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT,
    product_id INT,
    qty DECIMAL(10, 2),
    price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (product_id) REFERENCES product_info(id)
);
-- REQUEST
CREATE TABLE request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    user_id INT,
    type ENUM('export', 'purchase', 'repair'),
    status ENUM('pending', 'approved', 'rejected'),
    reason TEXT,
    approve_by INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (approve_by) REFERENCES users(id)
);
-- REQUEST ITEMS
CREATE TABLE request_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT,
    product_id INT,
    quantity DECIMAL(10, 2),
    FOREIGN KEY (request_id) REFERENCES request(id),
    FOREIGN KEY (product_id) REFERENCES product_info(id)
);
-- SUPPLIER
CREATE TABLE supplier (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    note TEXT,
    active_flag TINYINT(1) DEFAULT 1,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE request
ADD COLUMN approve_by VARCHAR(100) DEFAULT NULL,
    ADD COLUMN warehouse VARCHAR(100) DEFAULT NULL;
ALTER TABLE request_items
ADD COLUMN imported_qty DECIMAL(10, 2) DEFAULT 0;
ALTER TABLE request
MODIFY status ENUM('pending', 'approved', 'rejected', 'completed');
-- Enhanced PRODUCT INFO table with additional fields for comprehensive product management
ALTER TABLE product_info
ADD COLUMN supplier_id INT,
    ADD COLUMN expiration_date DATE,
    ADD COLUMN image_url VARCHAR(255),
    ADD COLUMN additional_notes TEXT,
    ADD COLUMN created_by INT,
    ADD COLUMN created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_by INT,
    ADD COLUMN updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ADD FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    ADD FOREIGN KEY (created_by) REFERENCES users(id),
    ADD FOREIGN KEY (updated_by) REFERENCES users(id);
-- Add some storage locations for dropdown selection
CREATE TABLE storage_location (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active_flag TINYINT(1) DEFAULT 1
);
I -- INSERT SAMPLE DATA
-- Insert roles
INSERT INTO role (role_name)
VALUES ('Admin'),
    ('Nhân viên kho'),
    ('Nhân viên công ty');
-- Insert sample users
INSERT INTO users (
        username,
        password,
        email,
        fullname,
        phone,
        dob,
        active_flag
    )
VALUES (
        'admin',
        '123',
        'admin@warehouse.com',
        'System Administrator',
        '0123456789',
        '1985-01-15',
        1
    ),
    (
        'warehouse_staff1',
        'password123',
        'warehouse1@warehouse.com',
        'Nguyễn Văn Kho',
        '0987654321',
        '1990-05-20',
        1
    ),
    (
        'company_staff1',
        'password123',
        'company1@warehouse.com',
        'Trần Thị Công Ty',
        '0912345678',
        '1988-12-10',
        1
    ),
    (
        'warehouse_staff2',
        'password123',
        'warehouse2@warehouse.com',
        'Lê Văn Quản',
        '0909876543',
        '1992-08-03',
        1
    ),
    (
        'company_staff2',
        'password123',
        'company2@warehouse.com',
        'Phạm Thị Doanh',
        '0898765432',
        '1995-03-25',
        1
    );
-- Assign roles to users
INSERT INTO user_role (user_id, role_id)
VALUES (1, 1),
    -- admin -> Admin
    (2, 2),
    -- warehouse_staff1 -> Nhân viên kho
    (3, 3),
    -- company_staff1 -> Nhân viên công ty
    (4, 2),
    -- warehouse_staff2 -> Nhân viên kho
    (5, 3);
-- company_staff2 -> Nhân viên công ty
-- Add sample categories and units for testing
INSERT INTO category (name, parent_id, active_flag)
VALUES ('Điện tử', NULL, 1),
    ('Thiết bị văn phòng', NULL, 1),
    ('Vật liệu xây dựng', NULL, 1),
    ('Thiết bị y tế', NULL, 1);
INSERT INTO unit (name, symbol, description, type)
VALUES ('Cái', 'cái', 'Đơn vị đếm', 'Số lượng'),
    (
        'Kilogram',
        'kg',
        'Đơn vị khối lượng',
        'Khối lượng'
    ),
    ('Mét', 'm', 'Đơn vị độ dài', 'Độ dài'),
    ('Hộp', 'hộp', 'Đơn vị đóng gói', 'Số lượng'),
    ('Lít', 'l', 'Đơn vị thể tích', 'Khối lượng');
-- Add sample products
INSERT INTO product_info (
        name,
        code,
        cate_id,
        unit_id,
        price,
        active_flag,
        status,
        description
    )
VALUES (
        'Laptop Dell Inspiron 15',
        'LAPTOP001',
        1,
        1,
        15000000,
        1,
        'active',
        'Laptop Dell Inspiron 15 inch, RAM 8GB, SSD 256GB'
    ),
    (
        'Máy in HP LaserJet',
        'PRINTER001',
        2,
        1,
        3500000,
        1,
        'active',
        'Máy in laser đen trắng HP LaserJet Pro'
    ),
    (
        'Thép xây dựng',
        'STEEL001',
        3,
        2,
        25000,
        1,
        'active',
        'Thép xây dựng D10, chất lượng cao'
    ),
    (
        'Máy đo huyết áp',
        'MEDICAL001',
        4,
        1,
        1200000,
        1,
        'active',
        'Máy đo huyết áp tự động, chính xác cao'
    ),
    (
        'Bàn phím không dây',
        'KEYBOARD001',
        1,
        1,
        500000,
        1,
        'active',
        'Bàn phím không dây Bluetooth'
    ),
    (
        'Giấy A4',
        'PAPER001',
        2,
        4,
        80000,
        1,
        'active',
        'Giấy A4 80gsm, 500 tờ/ream'
    ),
    (
        'Xi măng Portland',
        'CEMENT001',
        3,
        2,
        120000,
        1,
        'active',
        'Xi măng Portland PC40, bao 50kg'
    ),
    (
        'Khẩu trang y tế',
        'MASK001',
        4,
        4,
        150000,
        1,
        'active',
        'Khẩu trang y tế 3 lớp, hộp 50 cái'
    ),
    (
        'Ổ cứng SSD 1TB',
        'SSD001',
        1,
        1,
        2800000,
        1,
        'active',
        'Ổ cứng SSD Samsung 1TB'
    ),
    (
        'Bút bi xanh',
        'PEN001',
        2,
        4,
        25000,
        1,
        'active',
        'Bút bi xanh Thiên Long, hộp 20 cái'
    );
-- Add sample stock data
INSERT INTO product_in_stock (product_id, qty, status)
VALUES (1, 5, 'available'),
    -- Low stock - laptop
    (2, 15, 'available'),
    -- Normal stock - printer  
    (3, 500, 'available'),
    -- High stock - steel
    (4, 8, 'available'),
    -- Low stock - blood pressure monitor
    (5, 25, 'available'),
    -- Normal stock - keyboard
    (6, 100, 'available'),
    -- High stock - paper
    (7, 200, 'available'),
    -- High stock - cement
    (8, 3, 'available'),
    -- Very low stock - masks
    (9, 12, 'available'),
    -- Normal stock - SSD
    (10, 50, 'available');
-- Normal stock - pens