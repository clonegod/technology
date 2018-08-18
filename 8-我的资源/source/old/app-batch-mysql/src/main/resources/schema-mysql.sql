drop table  if exists person;

create table person (
	id int primary key auto_increment,	
	first_name varchar(100),
	family_name varchar(100),
	year int
);