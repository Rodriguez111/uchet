CREATE TABLE IF NOT EXISTS positions
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    position    character varying UNIQUE NOT NULL,
    position_description character varying
);