create table student(
	id int PRIMARY KEY auto_increment,
	name varchar(255) not null,
	age int not null DEFAULT 0,
	gender varchar(255),
	clazz varchar(255),
	sum_score int
);

-- 对name做索引
create index stu_name_index on student(name);