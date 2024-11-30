-- 科目(subject)
-- 年级/阶段 (stage)
-- 题目(question)
-- 试卷(test paper) - 科目
-- 题型(选择，填空) - 每题对应知识点范围

-- 阶段体系(按这个体系分有哪些阶段，一年级、二年级.... | 入门、熟练、大师...)
-- 科目知识点结构 大知识点 - 小知识点(题型)
-- 教学体系 - 组织具体哪个知识点分到哪个等级以及先后顺序
-- 题目 - 对应知识点
-- 筛选时，选择教学体系 - 阶段 - 知识点/小知识点 - 题目

-- 试卷生成模板 - 对应一个科目,一个教学体系 - 不同题目类型数量(选择，填空) - 具体每道题的知识点范围 - 随机抽题
-- 试卷生成记录 - 题型 - 题号

-- 用户解题记录/统计知识点/错误率/解题总数/时间线绘图


-- 年级/阶段
create table qms_stage (
    id int auto_increment primary key,
    name varchar(128)
) comment '年级/阶段';

insert into qms_stage(name)
values ('小学一年级（上）'),
       ('小学一年级（下）'),
       ('小学二年级（上）'),
       ('小学二年级（下）'),
       ('小学三年级（上）'),
       ('小学三年级（下）'),
       ('入门'), -- 7
       ('中等'), -- 8
       ('困难') -- 9
;

-- 阶段体系
create table qms_stage_sys (
    id int auto_increment primary key ,
    name varchar(128)
) comment '阶段体系';

insert into qms_stage_sys(name) values ('普通学生'), ('普通-编程学习路线');

-- 阶段体系 - 阶段
create table qms_stage_sys_stage_link (
    id int auto_increment primary key ,
    stage_sys_id int not null,
    stage_id int not null,
    stage_sort int not null default 0 comment '每个阶段体系里阶段都有先后顺序'
);

insert into qms_stage_sys_stage_link (stage_sys_id, stage_id, stage_sort)
values (1, 1, 1), (1, 2, 2), (1, 3, 3), (1, 4, 4), (1, 5, 5), (1, 6, 6), -- 普通学生阶段体系
    (2, 7, 1), (2, 8, 2), (2, 9, 10) -- 编程学习路线阶段体系
;

-- 科目
create table qms_subject (
    id int auto_increment primary key ,
    name varchar(128)
) comment '科目';

insert into qms_subject(name)
values ('语文'),
       ('数学'),
       ('Java')
;

-- 知识点
create table qms_knowledge_point (
    id int auto_increment primary key ,
    name varchar(256)
) comment '知识点';

insert into qms_knowledge_point(name)
values ('记叙文'), ('说明文'), ('古诗'), ('文言文'), ('作文'),
       ('集合'), ('几何'), ('函数'), ('四则运算'),
       ('Java语法'), ('数据结构'), ('设计模式')
;


-- 知识结构
create table qms_knowledge_struct (
    id int auto_increment primary key ,
    name varchar(256),
    stage_sys_id int not null comment '使用哪种阶段教育体系'
) comment '知识点结构';

insert into qms_knowledge_struct(name, stage_sys_id)
values ('人教版', 1), ('大众编程', 2)
;

-- 知识结构对应知识点
create table qms_knowledge_point_struct_link(
    id int auto_increment primary key ,
    knowledge_struct_id int not null comment '哪种知识体系',
    subject_id int not null comment '哪个学科',
    stage_id int not null comment '哪个阶段',
    knowledge_point_id int not null comment '哪个知识点',
    sort int default 0 not null comment '排序',
    parent_id int default 0 comment '父知识点结构，0-没有'
) ;

-- 题型
create table qms_question_type(
    id int auto_increment primary key,
    name varchar(128)
) comment '题型';

insert into qms_question_type(name)
values ('填空题'), ('简答题'), ('计算题'), ('编程题')
;

-- 题目
create table qms_question (
    id int auto_increment primary key,
    title text,
    answer text comment '参考答案',
    type_id int not null comment '题型',
    award_val int default 0 comment '积分值'
) comment '题目';

insert into qms_question(title, answer, type_id, award_val)
values ('1+1', '2', 1, 0), ('输出hello world', 'System.out.println("hello world");', 4, 10)
;

-- 题目知识点，只要具备一行就能做出来
create table qms_question_knowledge_point(
    id int auto_increment primary key ,
    question_id int not null ,
    knowledge_point_range varchar(256) not null comment '需要具备的知识点范围 id1|id2|id3'
);

insert into qms_question_knowledge_point(question_id, knowledge_point_range)
values (1, '9'),
       (2, '10')
;

-- 试卷模板
create table qms_test_template(
  id int auto_increment primary key ,
  subject_id int not null comment '科目',
  knowledge_struct_id int not null comment '教学体系'
);

-- 试卷结构，每种题型顺序
create table qms_test_template_struct(
    id int auto_increment primary key ,
    qms_test_template_id int not null ,
    question_type_id int not null,
    sort int not null default 0
);

-- 试卷具体每道题
create table qms_test_template_question(
    id int auto_increment primary key ,
    test_template_struct_id int not null ,
    knowledge_point_range varchar(256) comment '知识点范围, id1|id2|id3'
);


