create table roles (id  bigserial not null, name varchar(255), primary key (id));
insert into roles (id, name) values (1, 'USER');
insert into roles (id, name) values (2, 'ADMIN');

create table users (id  bigserial not null, e_mail varchar(255) not null, password varchar(255) not null, reg_time timestamp not null, username varchar(255) not null, role_id int8, primary key (id), FOREIGN KEY (role_id) REFERENCES roles(id));
insert into users (e_mail, username, password, reg_time, role_id) values ('dexter_aljp1@gmail.com', 'Alex', '$2a$12$p8c88WRUSCLVhpUKdWefEu4FzVUFN5tCr/TaP08Y7iMevtfyppFmu', current_timestamp, 2);
insert into users (e_mail, username, password, reg_time, role_id) values ('dexteraljp1@gmail.com', 'Kate', '$2a$12$p8c88WRUSCLVhpUKdWefEu4FzVUFN5tCr/TaP08Y7iMevtfyppFmu', current_timestamp, 1);
