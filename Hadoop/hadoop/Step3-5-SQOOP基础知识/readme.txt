SQOOP是用于对数据进行导入导出的。
    (1)把MySQL、Oracle等数据库中的数据导入到HDFS、Hive、HBase中
	(2)把HDFS、Hive、HBase中的数据导出到MySQL、Oracle等数据库中

1.把数据从mysql导入到hdfs(默认是/user/<username>)中
  sqoop import --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table TBLS --fields-terminated-by '\t'  --null-string '**'  -m 1 --append  --hive-import
  sqoop import --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table TBLS --fields-terminated-by '\t'  --null-string '**'  -m 1 --append  --hive-import  --check-column 'TBL_ID' --incremental append --last-value 6
  
2.把数据从hdfs导出到mysql中  
  sqoop export --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table ids --fields-terminated-by '\t' --export-dir '/ids'
  
3.设置为作业，运行作业
  sqoop job --create myjob -- import --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table TBLS --fields-terminated-by '\t'  --null-string '**'  -m 1 --append  --hive-import  
  
4. 导入导出的事务是以Mapper任务为单位。