CREATE TABLE IF NOT EXISTS positions_users
(
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    position_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (position_id) REFERENCES positions (id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);

-- select user_login, position FROM users
--                                      JOIN positions_users ON
--         users.id = positions_users.user_id
--                                      JOIN positions ON
--         positions_users.position_id = positions.id
-- WHERE user_login = 'dir'