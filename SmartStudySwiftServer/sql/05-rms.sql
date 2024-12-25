create table rms_points
(
    id                   bigint not null auto_increment,
    user_id              bigint  comment '用户id',
    points               decimal(10,2)  comment '积分',
    primary key (id)
);

create table rms_award
(
    id                   bigint not null auto_increment,
    name                 varchar(64) not null comment '奖品名称',
    pic                  varchar(255) comment '图片',
    points               decimal(10,2) comment '所需积分',
    description          varchar(1024) comment '奖品描述',
    inventory            int comment '库存',
    publish_status       int(1) comment '上架状态：0->下架；1->上架' default 1,
    verify_status        int(1) comment '审核状态：0->未审核；1->审核通过' default 0,
    sort                 int comment '排序' default 100, -- 100之前的手动控制排序
    create_time          datetime comment '创建时间' default current_timestamp,
    update_time          datetime comment '修改时间' on update current_timestamp,
    status               int(1) default 1 comment '启用状态：0->禁用；1->启用',
    primary key (id),
    key idx_name (name)
);

create table rms_order
(
    id                   bigint not null auto_increment,
    user_id              bigint  comment '用户id',
    award_id             bigint comment '奖品id',
    state                bigint comment '订单状态 0 -> 下单, 1 -> 已支付, 2 -> 已发货, 3 -> 已签收, -1 -> 退单, -2 -> 已取消',
    create_time          datetime comment '创建时间' default current_timestamp,
    update_time          datetime comment '修改时间' on update current_timestamp,
    status               int(1) default 1 comment '启用状态：0->禁用；1->启用',
    primary key (id)
);