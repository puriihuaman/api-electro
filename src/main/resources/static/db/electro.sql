DROP DATABASE IF EXISTS electro;

CREATE DATABASE IF NOT EXISTS electro DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;

USE electro;

-- --------------------------------
-- TABLES
-- --------------------------------


-- CATEGORIES

CREATE TABLE IF NOT EXISTS Categories (
    category_id VARCHAR (40) NOT NULL,
    name_category VARCHAR(30) NOT NULL,
    CONSTRAINT PK_Categories PRIMARY KEY (category_id)
);

ALTER TABLE Categories CHANGE COLUMN name_category category_name VARCHAR(30) NOT NULL UNIQUE;

-- PRODUCTS

-- The data dictionary for the 'new_product' field is:
-- 0 --> New productEntity
-- 1 –-> productEntity existing

CREATE TABLE IF NOT EXISTS Products (
    product_id VARCHAR(40) NOT NULL,
    product_name VARCHAR(60) NOT NULL,
    price DECIMAL(15,2) DEFAULT (0),
    old_price DECIMAL(15,2) DEFAULT 0,
    new_product INT DEFAULT 0,
    photo VARCHAR(50) NULL,
    category_id VARCHAR(40) NOT NULL,
    CONSTRAINT PK_Products PRIMARY KEY (product_id),
    CONSTRAINT FK_CategoriesProducts FOREIGN KEY (category_id) REFERENCES Categories (category_id)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);


-- ROLES

CREATE TABLE IF NOT EXISTS Roles (
    role_id VARCHAR(40) NOT NULL,
    role_name VARCHAR(20) NOT NULL UNIQUE,
    CONSTRAINT PK_Roles PRIMARY KEY (role_id)
);


-- USERS

CREATE TABLE IF NOT EXISTS Users (
    user_id VARCHAR(40) NOT NULL,
    first_name VARCHAR(45) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    email VARCHAR(50) NOT NULL,
    username VARCHAR(15) NULL,
    password VARCHAR(100) NOT NULL,
    CONSTRAINT PK_Users PRIMARY KEY (user_id)
);

ALTER TABLE Users CHANGE COLUMN username username VARCHAR(15) NOT NULL UNIQUE;
ALTER TABLE Users CHANGE COLUMN email email VARCHAR(50) NOT NULL UNIQUE;



-- USERS_ROLES

CREATE TABLE IF NOT EXISTS Users_Roles (
    user_id VARCHAR(40) NOT NULL,
    role_id VARCHAR(40) NOT NULL,
    CONSTRAINT PK_Users_Roles PRIMARY KEY (user_id, role_id)
);


-- --------------------------------
-- STORED PROCEDURE
-- --------------------------------

-- FILTER USERS
DROP PROCEDURE IF EXISTS get_users_by_filters;

DELIMITER $$
CREATE PROCEDURE get_users_by_filters (
    IN _first_name VARCHAR(45),
    IN _last_name VARCHAR(60),
    IN _email VARCHAR(50),
    IN _username VARCHAR(15),
    IN  _offset INT,
    IN _limit INT
)
BEGIN
    SELECT US.user_id, US.first_name, US.last_name, US.email, US.username, US.password, US.role_id 
    FROM Users AS US
    WHERE (_first_name IS NULL OR LOWER(US.first_name) LIKE CONCAT('%', LOWER(_first_name), '%'))
        AND (_last_name IS NULL OR  LOWER(US.last_name) LIKE CONCAT('%', LOWER(_last_name), '%'))
        AND (_email IS NULL OR  LOWER(US.email) LIKE CONCAT('%', LOWER(_email), '%'))
        AND (_username IS NULL OR LOWER(US.username) LIKE CONCAT('%', LOWER(_username), '%'))
    LIMIT _offset, _limit;
END$$
DELIMITER ;

-- READ PRODUCTS

DROP PROCEDURE IF EXISTS read_products;

DELIMITER $$
CREATE PROCEDURE read_products ()
BEGIN
    SELECT product_id, product_name, price, old_price, new_product, photo, category_id FROM Products;
END$$
DELIMITER ;

CALL read_products();


-- GET PRODUCT BY ID

DROP PROCEDURE IF EXISTS get_product_by_id;

DELIMITER $$
CREATE PROCEDURE get_product_by_id ( IN _product_id VARCHAR(40))
BEGIN
    SELECT product_id, product_name, price, old_price, new_product, photo, category_id 
    FROM Products AS prod
    WHERE prod.product_id = _product_id;
END$$
DELIMITER ;


-- DELETE PRODUCT

DROP PROCEDURE IF EXISTS delete_product;

DELIMITER $$
CREATE PROCEDURE delete_product (IN _product_id VARCHAR(40) )
BEGIN
    DELETE FROM Products WHERE product_id = _product_id;
END$$
DELIMITER ;


-- SEARCH PRODUCT

DROP PROCEDURE IF EXISTS search_product_by_name;

DELIMITER $$
CREATE PROCEDURE search_product_by_name (IN _product_name VARCHAR(60))
BEGIN
    SELECT product_id, product_name, price, old_price, new_product, photo, category_id 
    FROM Products 
    WHERE LOWER(product_name) LIKE CONCAT('%', LOWER(_product_name) , '%');
END $$
DELIMITER ;

CALL search_product_by_name('no');


-- FILTER PRODUCTS

DROP PROCEDURE IF EXISTS get_products_by_filters;

DELIMITER $$
CREATE PROCEDURE get_products_by_filters (
    IN _product_name VARCHAR(40),
    IN _min_price DECIMAL(15,2),
    IN _max_price DECIMAL(15,2),
    IN _new_product INT,
    IN _category_name VARCHAR(30),
    IN  _offset INT,
    IN _limit INT
)
BEGIN
    SELECT PROD.product_id, PROD.product_name, PROD.price, PROD.old_price, PROD.new_product, PROD.photo, CAT.category_id 
    FROM Products AS PROD
    INNER JOIN Categories AS CAT ON PROD.category_id = CAT.category_id
    WHERE (_product_name IS NULL OR LOWER(PROD.product_name) LIKE CONCAT('%', LOWER(_product_name), '%'))
        AND (_min_price IS NULL OR PROD.price >= _min_price)
        AND (_max_price IS NULL OR PROD.price <= _max_price)
        AND (_new_product IS NULL OR PROD.new_product = _new_product)
        AND (_category_name IS NULL OR LOWER(CAT.category_name) LIKE CONCAT('%', LOWER(_category_name), '%'))
    LIMIT _offset, _limit;
END$$
DELIMITER ;

CALL get_products_by_filters(NULL, NULL, NULL, NULL, 'e', 0, 5);


-- --------------------------------
-- TRIGGERS
-- --------------------------------

-- CREATE USERNAME

DELIMITER $$
CREATE TRIGGER create_username
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
    SET NEW.username = UPPER(CONCAT(SUBSTRING(NEW.last_name, 1, 4), YEAR( NOW())));
END $$
DELIMITER ;


-- --------------------------------
-- DATA INSERTION
-- --------------------------------

-- CATEGORIES

INSERT INTO Categories (category_id, category_name)
VALUES ('63d7ae7c-3145-4226-b9cc-baf12ebfe2fd', 'Portatil'),
    ('76812a61-3ddb-4162-944a-57aaae6fe3b2', 'Tableta'),
    ('bbde9eb5-b29b-4b3d-b22a-d831c89d0d8f', 'Audifonossss'),
    ('2cf65d25-fc48-4e49-957c-6a840242a23c', 'Celular'),
    ('bc1578c7-68fb-4b5e-87f5-de9e01a4588e', 'Cámara'),
    ('15e12827-e699-40c7-bbe4-43e546a1a126', 'test');


SELECT * FROM Categories;

UPDATE Categories AS C SET category_name = 'Audifonos' WHERE C.category_id = 'bbde9eb5-b29b-4b3d-b22a-d831c89d0d8f';

-- DELETE FROM Categories AS C WHERE C.category_id = '15e12827-e699-40c7-bbe4-43e546a1a126';



-- PRODUCTS

INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('831285b2-269c-4633-b719-0115be631f5f', 'Laptop HP 766i8', '1523.36', '1600', '0', './img/product03.png', '63d7ae7c-3145-4226-b9cc-baf12ebfe2fd');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('872dce86-f427-455c-af16-0cc171a91759', 'Laptop Lenovo 15u776', '1852', '1925.36', '0', './img/product01.png', '63d7ae7c-3145-4226-b9cc-baf12ebfe2fd');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('a20b026d-d031-433a-baf4-43c639d6716f', 'Laptop Acer 5468445', '8500', '8500', '0', './img/product03.png', '63d7ae7c-3145-4226-b9cc-baf12ebfe2fd');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('8ec01253-01eb-48f4-8b1d-130ffe9a8730', 'Laptop HP 876876','2560', '2600', '0', './img/product01.png', '63d7ae7c-3145-4226-b9cc-baf12ebfe2fd');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('26c8103a-63c7-4731-80ee-8bbb42867284', 'Tablet HP 8676788', '1200', '1250', '0', './img/product04.png', '76812a61-3ddb-4162-944a-57aaae6fe3b2');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('a3d46f57-36ac-4ef4-ae19-8cf6adc89d16', 'Tablet Samsung rrw4545', '1350', '1450', '0', './img/product04.png', '76812a61-3ddb-4162-944a-57aaae6fe3b2');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('0866896b-3e76-4430-910b-53e2dc98d620', 'Tablet LG OO88', '1800', '1820', '0', './img/product04.png', '76812a61-3ddb-4162-944a-57aaae6fe3b2');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('b307756d-0911-48d4-b30b-7bf4776de4fe', 'Tablet Lenovo hh5454','1500', '1550', '0', './img/product04.png', '76812a61-3ddb-4162-944a-57aaae6fe3b2');

INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('96c8444c-d0c7-4656-94e0-eb93f22d1577', 'Audifono HHY 76767', '500', '536.36', '0', './img/product05.png', 'bbde9eb5-b29b-4b3d-b22a-d831c89d0d8f');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('03c86fd3-e132-4459-b312-4915cf50d208', 'Audifonos DJ 8787', '7500', '7863', '0', './img/product05.png', 'bbde9eb5-b29b-4b3d-b22a-d831c89d0d8f');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('ea2b62cc-0f52-40be-b254-a95fcb96e295', 'Audifonos UJY 65', '700', '750', '0', './img/product05.png', 'bbde9eb5-b29b-4b3d-b22a-d831c89d0d8f');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('b011e07f-0902-410c-9df8-a088bad7f93c', 'Audifonos Skullcandy 888', '1250', '1350', '0', './img/product05.png', 'bbde9eb5-b29b-4b3d-b22a-d831c89d0d8f');


INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('38a3bc57-ac8b-4cfa-9441-450828bae5c2', 'Celular LG 76767', '850', '855', '0', './img/product07.png', '2cf65d25-fc48-4e49-957c-6a840242a23c');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('c564eafb-bf24-4f4b-90ad-55b6904e7550', 'Celular PO 8978', '900', '900', '0', './img/product07.png', '2cf65d25-fc48-4e49-957c-6a840242a23c');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('38bfa884-cd00-4c5a-be75-67885efe12dd', 'Celular Huawey 9955', '1800', '1853', '0', './img/product07.png', '2cf65d25-fc48-4e49-957c-6a840242a23c');
INSERT INTO Products (product_id, product_name, price, old_price, new_product, photo, category_id) 
VALUES ('ea2993de-8804-4766-ae66-82394c88f7ca', 'Celular iPhone 9', '3520', '3600', '0', './img/product07.png', '2cf65d25-fc48-4e49-957c-6a840242a23c');


SELECT * FROM Products;

-- Roles

INSERT INTO Roles (role_id, role_name)
VALUES 
    ('27c7ab26-caac-4f9d-94e6-e3d5dc3405d2', 'ADMIN'),
    ('86d78dd7-869d-4b16-99c5-5b8c613ff593', 'USER'),
    ('ab482bf4-0efb-42da-9e69-605b21060cbb', 'INVITED');

SELECT * FROM Roles;

-- Users

-- DELETE FROM Users WHERE user_id = '3971d79b-508a-4d87-8191-d3841e2c6001';

SELECT * FROM Users;
SELECT * FROM Users_Roles;















