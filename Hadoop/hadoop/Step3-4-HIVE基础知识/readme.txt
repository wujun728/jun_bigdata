1.Hive
1.1在hadoop生态圈中属于数据仓库的角色。他能够管理hadoop中的数据，同时可以查询hadoop中的数据。
  本质上讲，hive是一个SQL解析引擎。Hive可以把SQL查询转换为MapReduce中的job来运行。
  hive有一套映射工具，可以把SQL转换为MapReduce中的job，可以把SQL中的表、字段转换为HDFS中的文件(夹)以及文件中的列。
  这套映射工具称之为metastore，一般存放在derby、mysql中。
1.2 hive在hdfs中的默认位置是/user/hive/warehouse，是由配置文件hive-conf.xml中属性hive.metastore.warehouse.dir决定的。
2.hive的安装
  (1)解压缩、重命名、设置环境变量
  (2)在目录$HIVE_HOME/conf/下，执行命令mv hive-default.xml.template  hive-site.xml重命名
     在目录$HIVE_HOME/conf/下，执行命令mv hive-env.sh.template  hive-env.sh重命名
  (3)修改hadoop的配置文件hadoop-env.sh，修改内容如下：
     export HADOOP_CLASSPATH=.:$CLASSPATH:$HADOOP_CLASSPATH:$HADOOP_HOME/bin
  (4)在目录$HIVE_HOME/bin下面，修改文件hive-config.sh，增加以下内容：
     export JAVA_HOME=/usr/local/jdk
     export HIVE_HOME=/usr/local/hive
     export HADOOP_HOME=/usr/local/hadoop
3.安装mysql
  (1)删除linux上已经安装的mysql相关库信息。rpm  -e  xxxxxxx   --nodeps
     执行命令rpm -qa |grep mysql 检查是否删除干净
  (2)执行命令 rpm -i   mysql-server-********  安装mysql服务端	 
  (3)启动mysql 服务端，执行命令  mysqld_safe &
  (4)执行命令 rpm -i   mysql-client-********  安装mysql客户端
  (5)执行命令mysql_secure_installation设置root用户密码
4. 使用mysql作为hive的metastore
  (1)把mysql的jdbc驱动放置到hive的lib目录下
  (2)修改hive-site.xml文件，修改内容如下：  
	<property>
		<name>javax.jdo.option.ConnectionURL</name>
		<value>jdbc:mysql://hadoop0:3306/hive?createDatabaseIfNotExist=true</value>
	</property>
	<property>
		<name>javax.jdo.option.ConnectionDriverName</name>
		<value>com.mysql.jdbc.Driver</value>
	</property>
	<property>
		<name>javax.jdo.option.ConnectionUserName</name>
		<value>root</value>
	</property>
	<property>
		<name>javax.jdo.option.ConnectionPassword</name>
		<value>admin</value>
	</property>
5. 内部表
   CREATE TABLE t1(id int);  
   LOAD DATA LOCAL INPATH '/root/id' INTO TABLE t1;
   
   CREATE TABLE t2(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
6. 分区表
   CREATE TABLE t3(id int) PARTITIONED BY (day int);  
   LOAD DATA LOCAL INPATH '/root/id' INTO TABLE t1 PARTITION (day=22);   
7. 桶表
   create table t4(id int) clustered by(id) into 4 buckets; 
   set hive.enforce.bucketing = true;
   insert into table t4 select id from t3;
8. 外部表
   create external table t5(id int) location '/external';   
   
   
练习题：把原来的手机流量统计使用hive处理   
	 
  