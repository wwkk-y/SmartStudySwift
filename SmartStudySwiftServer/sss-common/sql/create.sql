create table global_set
(
    id                   bigint not null auto_increment primary key,
    value                varchar(256) unique key
);

create table mq_log
(
    id                   bigint auto_increment primary key,
    messageId            varchar(256) comment 'MQ内部消息ID',
    bid                  varchar(256) comment '业务id确保唯一',
    destination                varchar(256) comment '主题 + 标签',
    message              varchar(1024) comment '消息内容',
    state                int comment '0->生产者发送成功；1->生产者发送失败；2->消费者处理中；3->消费者处理成功；4->消费者处理异常；5->WAL消费时不能获取日志行',
    error_msg            text comment '异常信息',
    reconsume_times      int default 0 comment '消费重试次数',
    create_time          datetime comment '创建时间' default current_timestamp,
    update_time          datetime comment '修改时间' on update current_timestamp,
    key `idx_messageId` (messageId)
);