# Добавление ролей и 2-х пользователей


USE java_exam;

INSERT INTO user (id, username, password, enabled) VALUES ('1', 'Java', '$2a$10$URxgKPFWC11azdpLieuPIuL2CjOMpjz77uXoetOdaCvdiqG/PH0xe', b'1');
INSERT INTO user (id, username, password, enabled) VALUES ('2', 'user', '$2a$10$64VU3wLT203dP1EWT51eku0Xx4GZbLSOi.vTRKAQFKkZRM6gVSPEK', b'1');

INSERT INTO role (role) VALUES ('ROLE_SUPERADMIN');
INSERT INTO role (role) VALUES ('ROLE_ADMIN');
INSERT INTO role (role) VALUES ('ROLE_USER');
INSERT INTO role (role) VALUES ('ROLE_ANONYMOUS');

INSERT INTO user_roles (user_id, authority) VALUES ('1', 'ROLE_SUPERADMIN');
INSERT INTO user_roles (user_id, authority) VALUES ('2', 'ROLE_USER');
