drop table if exists `test`;
create table `test` (
    `id`      bigint not null comment 'id',
    `name`     varchar(50) comment '名称',
    `password` varchar(50) comment '密码',
    primary key (`id`)
);
insert into `test` values (1, '测试', 'password')

drop table if exists `demo`;
create table `demo` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    primary key (`id`)
);

insert into `demo` values (1, '测试')