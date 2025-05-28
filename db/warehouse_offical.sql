-- USERS
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    fullname VARCHAR(100),
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
    active_flag TINYINT(1) DEFAULT 1,
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
    price DECIMAL(15,2),
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
    qty DECIMAL(10,2),
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
    qty DECIMAL(10,2),
    price DECIMAL(15,2),
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
    quantity DECIMAL(10,2),
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
