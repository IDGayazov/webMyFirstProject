insert into users(id, login, email, password, active)
    values(1, 'admin', 'gayazov02@inbox.ru', '123', true);

insert into user_role(user_id, roles)
    values(1, 'ADMIN');