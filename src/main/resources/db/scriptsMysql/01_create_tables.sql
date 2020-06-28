--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS permissions
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    permission             VARCHAR(255) UNIQUE NOT NULL,
    permission_description VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS positions
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    position             VARCHAR(60) UNIQUE  NOT NULL,
    position_description VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS positions_permissions
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    position_id   INT NOT NULL,
    permission_id INT NOT NULL,
    FOREIGN KEY (position_id) REFERENCES positions (id) ON DELETE RESTRICT,
    FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_login       VARCHAR(20) UNIQUE                  NOT NULL,
    user_password    VARCHAR(255)                        NOT NULL,
    user_name        VARCHAR(25)                         NOT NULL,
    user_surname     VARCHAR(25)                         NOT NULL,
    user_position_id INT                                 NOT NULL,
    user_is_active   BOOLEAN                             NOT NULL,
    user_create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_update_date TIMESTAMP,
    FOREIGN KEY (user_position_id) REFERENCES positions (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS contractors
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    contractor_code VARCHAR(6) UNIQUE                   NOT NULL,
    contractor_name VARCHAR(255) UNIQUE                 NOT NULL,
    is_supplier BOOLEAN,
    is_customer BOOLEAN,
    create_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_date   TIMESTAMP
);

CREATE TABLE IF NOT EXISTS units
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    unit        VARCHAR(20) UNIQUE NOT NULL,
    description VARCHAR(80) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS sku
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    sku_code       VARCHAR(6) UNIQUE   NOT NULL,
    sku_name       VARCHAR(255) UNIQUE NOT NULL,
    sku_unit_id    INT                 NOT NULL,
    contractor_id    INT                 NOT NULL,
    qty_in_sec_pkg INT,
    sku_is_active  BOOLEAN             NOT NULL,
    FOREIGN KEY (sku_unit_id) references units (id) ON DELETE RESTRICT,
    FOREIGN KEY (contractor_id) references contractors (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS in_doc_types
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    doc_type  VARCHAR(60) UNIQUE                  NOT NULL
);

CREATE TABLE IF NOT EXISTS out_doc_types
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    doc_type  VARCHAR(60) UNIQUE                  NOT NULL
);


CREATE TABLE IF NOT EXISTS in_docs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    doc_number  VARCHAR(10) UNIQUE                  NOT NULL,
    doc_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    doc_type_id INT                                 NOT NULL,
    contractor_id INT                                 NOT NULL,
    owner_id    INT                                 NOT NULL,
    FOREIGN KEY (doc_type_id) references in_doc_types (id) ON DELETE RESTRICT,
    FOREIGN KEY (contractor_id) references contractors (id) ON DELETE RESTRICT,
    FOREIGN KEY (owner_id) references users (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS in_doc_details
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    in_doc_id BIGINT NOT NULL,
    sku_id int NOT NULL,
    serial varchar(60),
    expire_date DATE,
    qty DOUBLE NOT NULL,
    price DECIMAL(10,2),
    vat_price DECIMAL(10,2),
    FOREIGN KEY (in_doc_id) references in_docs (id) ON DELETE RESTRICT,
    FOREIGN KEY (sku_id) references sku (id) ON DELETE RESTRICT
);




