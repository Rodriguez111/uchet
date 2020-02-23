CREATE TABLE IF NOT EXISTS users
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    user_login       character varying(20) UNIQUE                    NOT NULL,
    user_password    character varying,
    user_name        character varying(25)                           NOT NULL,
    user_surname     character varying(25)                           NOT NULL,
    user_is_active   BOOLEAN                                         NOT NULL,
    user_create_date DATETIME DEFAULT (DATETIME('now', 'localtime')) NOT NULL,
    user_update_date DATETIME
);