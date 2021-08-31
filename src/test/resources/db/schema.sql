
drop table if exists certificate_tag;
drop table if exists certificate;
drop table if exists tag;
Set ignorecase = TRUE;

create table certificate
(
    id               bigint primary key auto_increment,
    name             varchar(255),
    description      varchar(255),
    price            decimal(10, 2),
    duration         int,
    create_date      datetime default current_timestamp,
    last_update_date datetime default current_timestamp
);

create table if not exists tag
(
    id   bigint primary key auto_increment,
    name varchar(255)
);

create table certificate_tag
(
    certificate_id BIGINT,
    foreign key (certificate_id) references certificate (id) on delete cascade,
    tag_id         BIGINT,
    foreign key (tag_id) references tag (id) on delete cascade,
    primary key (certificate_id, tag_id)
);

insert into tag(name)
values ('test'),
       ('test2');

insert into certificate(name, description, price, duration, create_date, last_update_date)
values ('TEST-first', 'first', 10.10, 30, '2021-08-26 10:10:10', '2021-08-26 10:10:10'),
       ('TEST-second', 'second', 20.20, 15, '2021-08-25 10:10:10', '2021-08-25 10:10:10');

insert into certificate_tag(certificate_id, tag_id)
values (1, 2),
       (2, 1);