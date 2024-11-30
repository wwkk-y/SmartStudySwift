-- 会话表
create table cms_conversation(
    id int auto_increment primary key ,
    last_msg_id int comment '最后一条消息id',
    user_id1 int not null comment '成员1',
    user_id2 int not null comment '成员2',
    create_time datetime default CURRENT_TIMESTAMP,
    update_time datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
);

-- 消息表
create table cms_message(
    id int auto_increment primary key ,
    sender_id int not null comment '发送者',
    conversation_id int not null comment '所属会话',
    type varchar(128) comment '消息类型, TEXT, IMAGE, FILE',
    content varchar(512) not null comment '消息内容, 不同type的消息内容结构不一样',
    create_time datetime DEFAULT CURRENT_TIMESTAMP
);

-- 未读消息表
create table cms_unread_message(
    id int auto_increment primary key ,
    message_id int not null comment '消息',
    user_id int not null comment '用户'
);
