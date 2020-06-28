--liquibase formatted sql
--changeset myname:create-multiple-tables splitStatements:true endDelimiter:;

INSERT INTO permissions
    (permission, permission_description)
VALUES ('accounting_page', 'Учет'),
       ('remains_page', 'Остатки товаров'),
       ('incoming_docs_page', 'Приходные документы'),
       ('sku_in_page', 'Приход товаров'),
       ('outgoing_docs_page', 'Расходные документы'),
       ('sku_out_page', 'Расход товаров'),
       ('sku_page', 'Товары'),
       ('contractors_page', 'Контрагенты'),
       ('admin_page', 'Администрирование'),
       ('users_page', 'Пользователи'),
       ('positions_page', 'Должности');

INSERT INTO positions
    (position, position_description)
SELECT 'admin', 'Администратор'
WHERE NOT EXISTS(SELECT position FROM positions WHERE position = 'admin')
UNION ALL
SELECT 'director', 'Директор'
WHERE NOT EXISTS(SELECT position FROM positions WHERE position = 'director')
UNION ALL
SELECT 'buh', 'Бухгалтер'
WHERE NOT EXISTS(SELECT position FROM positions WHERE position = 'buh');

INSERT INTO positions_permissions
    (position_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (2, 7),
       (2, 8),
       (2, 9),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6);



INSERT INTO users
(user_login, user_password, user_name, user_surname, user_position_id, user_is_active, user_create_date)
VALUES ('admin', '$2a$10$RnWwhcoFP6WYBiYFtZAVZOTYHuZ51/CNQqNw44oQP1SoxYJbpOOF.',
        'ИмяАдминистратора', 'ФамилияАдминистратора', 1, true, now());



INSERT INTO units
    (unit, description)
VALUES ('шт.', 'штука'),
       ('гр.', 'грамм'),
       ('кг.', 'килограмм'),
       ('м.', 'метр'),
       ('м2.', 'метр квадратный'),
       ('м3.', 'метр кубический'),
       ('л.', 'литр');


INSERT INTO contractors
    (contractor_code, contractor_name, is_supplier, is_customer)
VALUES ('000001', 'Поставщик молока', true, false),
       ('000002', 'Поставщик хлеба', true, false),
       ('000003', 'Поставщик мяса', true, true),
       ('000004', 'Молочный магазин', false, true);

INSERT INTO sku
(sku_code, sku_name, sku_unit_id, contractor_id, qty_in_sec_pkg, sku_is_active)
VALUES ('000001', 'Молоко', 7, 1, 1, true),
       ('000002', 'Хлеб', 1, 2, 1, true),
       ('000003', 'Булочка1', 7, 2, 1, true),
       ('000004', 'Булочка2', 7, 2, 1, true),
       ('000005', 'Булочка3', 7, 2, 1, true),
       ('000006', 'Булочка4', 7, 2, 1, true),
       ('000007', 'Булочка5', 7, 2, 1, true),
       ('000008', 'Булочка6', 7, 2, 1, true),
       ('000009', 'Булочка7', 7, 2, 1, true),
       ('000010', 'Булочка8', 7, 2, 1, true),
       ('000011', 'Булочка9', 7, 2, 1, true),
       ('000012', 'Булочка10', 7, 2, 1, true);

INSERT INTO in_doc_types
    (doc_type)
VALUES ('Приход от поставщика'),
       ('Возврат от клиента');

INSERT INTO out_doc_types
    (doc_type)
VALUES ('Продажа клиенту'),
       ('Возврат поставщику');

INSERT INTO in_docs
    (doc_number, doc_date, contractor_id, owner_id, doc_type_id)
VALUES ('0000000001', '2020-04-15 16:02:30', 1, 1, 1),
       ('0000000002', '2020-04-28 11:08:16', 2, 1, 1);

INSERT INTO in_doc_details
    (in_doc_id, sku_id, serial, expire_date, qty, price, vat_price)
VALUES (1, 1, 'seria01', '2020-08-01', 10, 20, 24),
       (1, 2, 'seria02', '2020-09-01', 15, 25, 30),
       (2, 1, 'seria03', '2020-10-01', 100, 22, 26.4),
       (2, 2, 'seria04', '2020-11-01', 110, 23, 27.6),
       (2, 2, 'seria05', '2020-12-01', 120, 28, 33.6);