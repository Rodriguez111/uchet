CREATE TABLE IF NOT EXISTS paths
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    path    character varying UNIQUE NOT NULL,
    path_description character varying UNIQUE NOT NULL
);