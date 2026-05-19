SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS cnpm
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cnpm;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS tblReturnedDamage;
DROP TABLE IF EXISTS tblReturnedCollateral;
DROP TABLE IF EXISTS tblReturnedCostume;
DROP TABLE IF EXISTS tblReturnBill;
DROP TABLE IF EXISTS tblRentalCollateral;
DROP TABLE IF EXISTS tblRentedDamage;
DROP TABLE IF EXISTS tblRentedCostume;
DROP TABLE IF EXISTS tblRentalBill;
DROP TABLE IF EXISTS tblCollateral;
DROP TABLE IF EXISTS tblDamage;
DROP TABLE IF EXISTS tblCostume;
DROP TABLE IF EXISTS tblClient;
DROP TABLE IF EXISTS tblUser;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE tblUser (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(25) NOT NULL,
    password VARCHAR(25) NOT NULL,
    fullname VARCHAR(50) NOT NULL,
    role VARCHAR(25) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tblClient (
    id INT NOT NULL AUTO_INCREMENT,
    fullname VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    tel VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    note VARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tblCostume (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(255) NULL,
    price DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tblDamage (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    fee DECIMAL(12, 2) NOT NULL,
    description VARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tblCollateral (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type INT NOT NULL,
    note VARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tblRentalBill (
    id INT NOT NULL AUTO_INCREMENT,
    createdAt DATETIME NOT NULL,
    saleoff DECIMAL(12, 2) NULL DEFAULT 0,
    note VARCHAR(255) NULL,
    userId INT NOT NULL,
    clientId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblRentalBill_user
        FOREIGN KEY (userId) REFERENCES tblUser (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblRentalBill_client
        FOREIGN KEY (clientId) REFERENCES tblClient (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblRentedCostume (
    id INT NOT NULL AUTO_INCREMENT,
    rentalPrice DECIMAL(12, 2) NOT NULL,
    rentalQuantity INT NOT NULL,
    rentedAt DATETIME NOT NULL,
    dateToReturn DATETIME NOT NULL,
    costumeId INT NOT NULL,
    rentalBillId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblRentedCostume_costume
        FOREIGN KEY (costumeId) REFERENCES tblCostume (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblRentedCostume_rentalBill
        FOREIGN KEY (rentalBillId) REFERENCES tblRentalBill (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblRentedDamage (
    id INT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    note VARCHAR(255) NULL,
    damageId INT NOT NULL,
    rentedCostumeId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblRentedDamage_damage
        FOREIGN KEY (damageId) REFERENCES tblDamage (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblRentedDamage_rentedCostume
        FOREIGN KEY (rentedCostumeId) REFERENCES tblRentedCostume (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblRentalCollateral (
    id INT NOT NULL AUTO_INCREMENT,
    value DECIMAL(12, 2) NOT NULL,
    note VARCHAR(255) NULL,
    collateralId INT NOT NULL,
    rentalBillId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblRentalCollateral_collateral
        FOREIGN KEY (collateralId) REFERENCES tblCollateral (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblRentalCollateral_rentalBill
        FOREIGN KEY (rentalBillId) REFERENCES tblRentalBill (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblReturnBill (
    id INT NOT NULL AUTO_INCREMENT,
    returnedAt DATETIME NOT NULL,
    note VARCHAR(255) NULL,
    userId INT NOT NULL,
    clientId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblReturnBill_user
        FOREIGN KEY (userId) REFERENCES tblUser (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblReturnBill_client
        FOREIGN KEY (clientId) REFERENCES tblClient (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblReturnedCostume (
    id INT NOT NULL AUTO_INCREMENT,
    returnedQuantity INT NOT NULL,
    rentedCostumeId INT NOT NULL,
    returnBillId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblReturnedCostume_rentedCostume
        FOREIGN KEY (rentedCostumeId) REFERENCES tblRentedCostume (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblReturnedCostume_returnBill
        FOREIGN KEY (returnBillId) REFERENCES tblReturnBill (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblReturnedDamage (
    id INT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL,
    fee DECIMAL(12, 2) NOT NULL,
    note VARCHAR(255) NULL,
    damageId INT NOT NULL,
    returnedCostumeId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblReturnedDamage_damage
        FOREIGN KEY (damageId) REFERENCES tblDamage (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblReturnedDamage_returnedCostume
        FOREIGN KEY (returnedCostumeId) REFERENCES tblReturnedCostume (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tblReturnedCollateral (
    id INT NOT NULL AUTO_INCREMENT,
    returnedValue DECIMAL(12, 2) NOT NULL,
    rentalCollateralId INT NOT NULL,
    returnBillId INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_tblReturnedCollateral_rentalCollateral
        FOREIGN KEY (rentalCollateralId) REFERENCES tblRentalCollateral (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_tblReturnedCollateral_returnBill
        FOREIGN KEY (returnBillId) REFERENCES tblReturnBill (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
