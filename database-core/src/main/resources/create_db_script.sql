DROP DATABASE STORAGE;

CREATE DATABASE STORAGE;

USE STORAGE;

CREATE TABLE PRODUCT (
	PRODUCT_CODE VARCHAR(3) NOT NULL,
	PRODUCT_TITLE VARCHAR(40) NOT NULL,
	PRIMARY KEY (PRODUCT_CODE)
) ENGINE=INNODB;

CREATE TABLE PRODUCT_OFFER (
	OFFER_NUMBER BIGINT AUTO_INCREMENT NOT NULL,
	PRODUCT_CODE VARCHAR(3),
	QUANTITY INT,
	COST NUMERIC(10, 2),
	PRIMARY KEY (OFFER_NUMBER),
	CONSTRAINT FK_PRODUCT_OFFER_PRODUCT FOREIGN KEY (PRODUCT_CODE) REFERENCES PRODUCT (PRODUCT_CODE)
) ENGINE=INNODB;

INSERT INTO PRODUCT (PRODUCT_CODE, PRODUCT_TITLE) VALUES ('1aa', 'Карандаши цветные 12 шт');
INSERT INTO PRODUCT (PRODUCT_CODE, PRODUCT_TITLE) VALUES ('1ab', 'Карандаши цветные 20 шт');
INSERT INTO PRODUCT (PRODUCT_CODE, PRODUCT_TITLE) VALUES ('1ac', 'Ручка автоматическая');
INSERT INTO PRODUCT (PRODUCT_CODE, PRODUCT_TITLE) VALUES ('1ad', 'Набор для черчения');
INSERT INTO PRODUCT (PRODUCT_CODE, PRODUCT_TITLE) VALUES ('1ae', 'Тетрадь в клеточку 12 л');
INSERT INTO PRODUCT (PRODUCT_CODE, PRODUCT_TITLE) VALUES ('1af', 'Тетрадь в линейку 12 л');

INSERT INTO PRODUCT_OFFER (OFFER_NUMBER, PRODUCT_CODE, QUANTITY, COST) VALUES (1, '1aa', 15, '25000.00');
INSERT INTO PRODUCT_OFFER (OFFER_NUMBER, PRODUCT_CODE, QUANTITY, COST) VALUES (2, '1ab', 20, '18000.00');
INSERT INTO PRODUCT_OFFER (OFFER_NUMBER, PRODUCT_CODE, QUANTITY, COST) VALUES (3, '1ac', 230, '8000.00');
INSERT INTO PRODUCT_OFFER (OFFER_NUMBER, PRODUCT_CODE, QUANTITY, COST) VALUES (4, '1ad', 25, '64000.00');
INSERT INTO PRODUCT_OFFER (OFFER_NUMBER, PRODUCT_CODE, QUANTITY, COST) VALUES (5, '1ae', 150, '1000.00');
INSERT INTO PRODUCT_OFFER (OFFER_NUMBER, PRODUCT_CODE, QUANTITY, COST) VALUES (6, '1af', 100, '1000.00');