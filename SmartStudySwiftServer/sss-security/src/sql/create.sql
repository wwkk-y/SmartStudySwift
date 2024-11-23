create table ums_user
(
    id                   bigint not null auto_increment,
    username             varchar(64) comment '用户名 不可修改',
    password             varchar(64) comment '密码',
    icon                 varchar(500) comment '头像',
    email                varchar(100) comment '邮箱',
    nick_name            varchar(200) comment '昵称',
    note                 varchar(500) comment '备注信息',
    login_time           datetime comment '最后登录时间',
    create_time          datetime comment '创建时间' default current_timestamp,
    update_time          datetime comment '修改时间' on update current_timestamp,
    status               int(1) default 1 comment '帐号启用状态：0->禁用；1->启用',
    primary key (id),
    key `idx_username` (username),
    key `idx_email` (email(32))
);

create table ums_role
(
    id                   bigint not null auto_increment,
    name                 varchar(100) comment '名称',
    description          varchar(500) comment '描述',
    sort                 int default 0,
    create_time          datetime comment '创建时间' default current_timestamp,
    update_time          datetime comment '修改时间' on update current_timestamp,
    status               int(1) default 1 comment '启用状态：0->禁用；1->启用',
    primary key (id)
);

create table ums_role_of_user
(
    id                   bigint not null auto_increment,
    user_id              bigint,
    role_id              bigint,
    primary key (id)
);

create table ums_permission
(
    id                   bigint not null auto_increment,
    name                 varchar(100) comment '名称',
    description          varchar(500) comment '描述',
    sort                 int default 0,
    create_time          datetime comment '创建时间' default current_timestamp,
    update_time          datetime comment '修改时间' on update current_timestamp,
    status               int(1) default 1 comment '启用状态：0->禁用；1->启用',
    primary key (id)
);

create table ums_permission_of_role
(
    id                   bigint not null auto_increment,
    permission_id        bigint,
    role_id              bigint,
    primary key (id)
);

create table ums_login_log
(
    id                   bigint not null auto_increment,
    username             varchar(64) comment '用户名',
    token                varchar(256) comment 'token',
    logout               boolean default 0 comment '是否下线 1-已下线, 0-未下线',
    login_time           datetime comment '创建时间' default current_timestamp,
    PRIMARY KEY (id),
    KEY `idx_username` (username)
);