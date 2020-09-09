/***** 公共配置 *****/
/* Kafka Broker地址列表 */
insert into app_t_param_config_list values ('kafka.bootstrap.servers','20.0.0.29:21005,20.0.0.30:21005,20.0.0.31:21005,20.0.0.32:21005,20.0.0.33:21005');





/***** kafka-streams 功能相关的配置 *****/
/* 应用程序名称 */
insert into app_t_param_config_list values ('kafka.application.id','KafkaStreamsTopicFilter');

/* 下面的三条配置数据为一组，根据实际情况配置，根据实际情况增加、删除组，并且需要修改group1为与其他组不同的名称 */
/* Kafka Streams 程序的源Topic */
insert into app_t_param_config_list values ('kafka.group1.source.topic','');
/* Kafka Streams 程序的源Topic的数据放到目标Topic需要满足的条件的正则表达式 */
insert into app_t_param_config_list values ('kafka.group1.regex.string','');
/* Kafka Streams 程序的目标Topic */
insert into app_t_param_config_list values ('kafka.group1.target.topic','');





/***** spark-kafka 功能相关的配置 *****/
/* 应用程序名称 */
insert into app_t_param_config_list values ('spark.streaming.application.name','SparkStreamingKafkaToDatabase');
/* SparkStreaming 程序批量时间间隔，单位：秒 */
insert into app_t_param_config_list values ('spark.streaming.batch.duration','5');
/* SparkStreaming 程序的checkpoint的目录，nocp 表示不设置检查点 */
insert into app_t_param_config_list values ('spark.streaming.checkpoint.dir','nocp');

/* SparkStreaming 程序订阅的Kafka的Topic列表，逗号分隔 */
insert into app_t_param_config_list values ('kafka.consumer.topics','demo_topic_1,demo_topic_2');
/* 以下配置数据需要根据订阅的Topic列表的数量进行对应的配置，订阅了几个Topic下面的配置就是几条数据，并且需要修改demo_topic_1、demo_topic_2为实际订阅的Topic名称 */
/* demo_topic_1消息对应的处理类，根据实际情况配置 */
insert into app_t_param_config_list values ('spark.demo_topic_1.process.class','com.service.data.spark.streaming.process.DefaultTextProcess');
/* demo_topic_2消息对应的处理类，根据实际情况配置 */
insert into app_t_param_config_list values ('spark.demo_topic_2.process.class','com.service.data.spark.streaming.process.DefaultJsonProcess');
