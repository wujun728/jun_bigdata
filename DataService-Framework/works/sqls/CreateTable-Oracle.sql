drop table app_t_param_config_list cascade constraints;

drop table app_t_topic_table_list cascade constraints;

drop table app_t_table_column_list cascade constraints;

drop table app_t_topic_code_mapping cascade constraints;

/*==============================================================*/
/* Table: app_t_param_config_list                               */
/*==============================================================*/
create table app_t_param_config_list
(
   param_name           VARCHAR2(128)        not null,
   param_value          VARCHAR2(128),
   constraint PK_app_t_PARAM_CONFIG_LIST primary key (param_name)
);

comment on table app_t_param_config_list is
'参数配置表';

comment on column app_t_param_config_list.param_name is
'参数名称';

comment on column app_t_param_config_list.param_value is
'参数值';

/*==============================================================*/
/* Table: app_t_topic_table_list                                */
/*==============================================================*/
create table app_t_topic_table_list
(
   topic_name           VARCHAR2(128)        not null,
   topic_desc           VARCHAR2(128),
   table_name           VARCHAR2(32),
   field_split          VARCHAR2(32),
   constraint PK_app_t_TOPIC_TABLE_LIST primary key (topic_name)
);

comment on table app_t_topic_table_list is
'主题配置表';

comment on column app_t_topic_table_list.topic_name is
'主题名称';

comment on column app_t_topic_table_list.topic_desc is
'主题描述';

comment on column app_t_topic_table_list.table_name is
'目标表名称';

comment on column app_t_topic_table_list.field_split is
'字段分隔符';

/*==============================================================*/
/* Table: app_t_table_column_list                               */
/*==============================================================*/
create table app_t_table_column_list
(
   table_name           VARCHAR2(32)         not null,
   table_desc           VARCHAR2(128),
   column_name          VARCHAR2(32)         not null,
   column_desc          VARCHAR2(128),
   column_type          VARCHAR2(32),
   column_index         INTEGER,
   column_data          VARCHAR2(128),
   constraint PK_app_t_TABLE_COLUMN_LIST primary key (table_name, column_name)
);

comment on table app_t_table_column_list is
'表结构配置表';

comment on column app_t_table_column_list.table_name is
'目标表名称';

comment on column app_t_table_column_list.table_desc is
'表描述';

comment on column app_t_table_column_list.column_name is
'字段名称';

comment on column app_t_table_column_list.column_desc is
'字段描述';

comment on column app_t_table_column_list.column_type is
'字段类型';

comment on column app_t_table_column_list.column_index is
'字段顺序';

comment on column app_t_table_column_list.column_data is
'字段值';

/*==============================================================*/
/* Table: app_t_topic_code_mapping                              */
/*==============================================================*/
create table app_t_topic_code_mapping
(
   topic_name           VARCHAR2(128)        not null,
   column_name          VARCHAR2(32)         not null,
   source_code          VARCHAR2(32)         not null,
   source_code_desc     VARCHAR2(128),
   target_code          VARCHAR2(32),
   target_code_desc     VARCHAR2(128),
   constraint PK_app_t_TOPIC_CODE_MAPPING primary key (topic_name, column_name, source_code)
);

comment on table app_t_topic_code_mapping is
'码值转换配置表';

comment on column app_t_topic_code_mapping.topic_name is
'主题名称';

comment on column app_t_topic_code_mapping.column_name is
'字段名称';

comment on column app_t_topic_code_mapping.source_code is
'源代码';

comment on column app_t_topic_code_mapping.source_code_desc is
'源代码描述';

comment on column app_t_topic_code_mapping.target_code is
'目标代码';

comment on column app_t_topic_code_mapping.target_code_desc is
'目标代码描述';
