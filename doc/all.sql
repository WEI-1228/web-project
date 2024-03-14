drop table if exists `ebook`;
create table `ebook` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    `category1_id` bigint comment '分类1',
    `category2_id` bigint comment '分类2',
    `description` varchar(200) comment '描述',
    `cover` varchar(200) comment '封面',
    `doc_count` int comment '文档数',
    `view_count` int comment '阅读数',
    `vote_count` int comment '点赞数',
    primary key (`id`)
) comment '电子书';

insert into `ebook` (id, name, description) values (1, 'Spring Boot入门教程', '零基础入门Java开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (2, 'Vue 入门教程', '零基础入门Vue开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (3, 'Python入门教程', '零基础入门Python开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (4, 'Mysql入门教程', '零基础入门Mysql开发，企业级应用开发最佳首选框架');
insert into `ebook` (id, name, description) values (5, 'Oracle入门教程', '零基础入门Oracle开发，企业级应用开发最佳首选框架');


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

drop table if exists `category`;
create table `category`
(
    `id`     bigint      not null primary key comment 'id',
    `parent` bigint      not null default 0 comment '父id',
    `name`   varchar(50) not null comment '名称',
    `sort`   int comment '顺序'
) comment '分类';

insert into `category` value (100, 000, '前端开发', 100);
insert into `category` value (101, 100, 'Vue', 101);
insert into `category` value (102, 100, 'HTML & CSS', 102);
insert into `category` value (200, 000, 'Java', 200);
insert into `category` value (201, 200, '基础应用', 201);
insert into `category` value (202, 200, '框架应用', 202);
insert into `category` value (300, 000, 'Python', 300);
insert into `category` value (301, 300, '基础应用', 301);
insert into `category` value (302, 300, '进阶方向应用', 302);
insert into `category` value (400, 000, '数据库', 400);
insert into `category` value (401, 400, 'MySQL', 401);
insert into `category` value (500, 000, '其他', 500);
insert into `category` value (501, 500, '服务器', 501);
insert into `category` value (502, 500, '开发工具', 502);
insert into `category` value (503, 500, '热门服务端语言', 503);

drop table if exists `doc`;
create table `doc` (
    `id` bigint not null primary key comment 'id',
    `ebook_id` bigint not null default 0 comment '电子书id',
    `parent` bigint not null default 0 comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int comment '顺序',
    `view_count` int default 0 comment '阅读数',
    `vote_count` int default 0 comment '点赞数'
) comment '文档';