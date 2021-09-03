create database  gift_certificates_system;
use gift_certificates_system;

create table certificate (
id bigint primary key auto_increment,
name  varchar(255),
description varchar(255),
price decimal(10,2),
duration int,
create_date datetime default current_timestamp,
last_update_date datetime default current_timestamp,
FULLTEXT (name, description));

create table tag(
id bigint primary key auto_increment,
name varchar(255),
FULLTEXT (name));

create table certificate_tag (
certificate_id BIGINT,
constraint foreign key(certificate_id) references certificate(id) on delete cascade,
tag_id BIGINT,
constraint foreign key(tag_id) references tag(id) on delete cascade,
primary key(certificate_id, tag_id));

insert into certificate(name, description, price, duration, create_date, last_update_date) values
('Sky-jumping Lipki', 'Parachute jump in tandem with an instructor from a height of 2500 meters', 10.10, 30, default, default),
('Woman spa-program', 'Complex of wellness services for women', 20.20, 15, default, default),
('Horror escape room ', 'Dress more conveniently, you will have to move a little!', 30.30, 20, default, default);

insert into tag(name) values
('rest'),('extreme'),('sport'),('activities'),('health'),('woman'),('game'),('escaperoom');

insert into certificate_tag(certificate_id, tag_id) values
(1, 1),(1, 2),(1, 3),(1, 4),
(2, 1),(2, 5),(2, 6),
(3, 4),(3, 7),(3, 8);





