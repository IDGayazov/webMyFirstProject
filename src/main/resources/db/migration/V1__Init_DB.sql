create table card
(
    id int8 not null,
    image varchar(255) not null,
    is_global boolean not null,
    rus varchar(255) not null,
    tag varchar(255) not null,
    tat varchar(255) not null,
    primary key (id)
);

create table users
(
    id int8 not null,
    active boolean not null,
    email varchar(255) not null,
    login varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);

create table user_card
(
    id int8 not null,
    user_id int8 not null,
    card_id int8 not null,
    add_date date,
    is_learn boolean not null,
    constraint card_id_fk foreign key (card_id) references card
    ON DELETE CASCADE,
    constraint user_id_fk foreign key (user_id) references users
    ON DELETE CASCADE,
    primary key(id)
);
create table user_role
(
    user_id int8 not null,
    roles varchar(255)
);

alter table if exists user_role
    add constraint user_role_fk foreign key (user_id) references users;