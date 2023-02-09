1.Hive
1.1��hadoop��̬Ȧ���������ݲֿ�Ľ�ɫ�����ܹ�����hadoop�е����ݣ�ͬʱ���Բ�ѯhadoop�е����ݡ�
  �����Ͻ���hive��һ��SQL�������档Hive���԰�SQL��ѯת��ΪMapReduce�е�job�����С�
  hive��һ��ӳ�乤�ߣ����԰�SQLת��ΪMapReduce�е�job�����԰�SQL�еı��ֶ�ת��ΪHDFS�е��ļ�(��)�Լ��ļ��е��С�
  ����ӳ�乤�߳�֮Ϊmetastore��һ������derby��mysql�С�
1.2 hive��hdfs�е�Ĭ��λ����/user/hive/warehouse�����������ļ�hive-conf.xml������hive.metastore.warehouse.dir�����ġ�
2.hive�İ�װ
  (1)��ѹ���������������û�������
  (2)��Ŀ¼$HIVE_HOME/conf/�£�ִ������mv hive-default.xml.template  hive-site.xml������
     ��Ŀ¼$HIVE_HOME/conf/�£�ִ������mv hive-env.sh.template  hive-env.sh������
  (3)�޸�hadoop�������ļ�hadoop-env.sh���޸��������£�
     export HADOOP_CLASSPATH=.:$CLASSPATH:$HADOOP_CLASSPATH:$HADOOP_HOME/bin
  (4)��Ŀ¼$HIVE_HOME/bin���棬�޸��ļ�hive-config.sh�������������ݣ�
     export JAVA_HOME=/usr/local/jdk
     export HIVE_HOME=/usr/local/hive
     export HADOOP_HOME=/usr/local/hadoop
3.��װmysql
  (1)ɾ��linux���Ѿ���װ��mysql��ؿ���Ϣ��rpm  -e  xxxxxxx   --nodeps
     ִ������rpm -qa |grep mysql ����Ƿ�ɾ���ɾ�
  (2)ִ������ rpm -i   mysql-server-********  ��װmysql�����	 
  (3)����mysql ����ˣ�ִ������  mysqld_safe &
  (4)ִ������ rpm -i   mysql-client-********  ��װmysql�ͻ���
  (5)ִ������mysql_secure_installation����root�û�����
4. ʹ��mysql��Ϊhive��metastore
  (1)��mysql��jdbc�������õ�hive��libĿ¼��
  (2)�޸�hive-site.xml�ļ����޸��������£�  
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
5. �ڲ���
   CREATE TABLE t1(id int);  
   LOAD DATA LOCAL INPATH '/root/id' INTO TABLE t1;
   
   CREATE TABLE t2(id int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';
6. ������
   CREATE TABLE t3(id int) PARTITIONED BY (day int);  
   LOAD DATA LOCAL INPATH '/root/id' INTO TABLE t1 PARTITION (day=22);   
7. Ͱ��
   create table t4(id int) clustered by(id) into 4 buckets; 
   set hive.enforce.bucketing = true;
   insert into table t4 select id from t3;
8. �ⲿ��
   create external table t5(id int) location '/external';   
   
   
��ϰ�⣺��ԭ�����ֻ�����ͳ��ʹ��hive����   
	 
  