SQOOP�����ڶ����ݽ��е��뵼���ġ�
    (1)��MySQL��Oracle�����ݿ��е����ݵ��뵽HDFS��Hive��HBase��
	(2)��HDFS��Hive��HBase�е����ݵ�����MySQL��Oracle�����ݿ���

1.�����ݴ�mysql���뵽hdfs(Ĭ����/user/<username>)��
  sqoop import --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table TBLS --fields-terminated-by '\t'  --null-string '**'  -m 1 --append  --hive-import
  sqoop import --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table TBLS --fields-terminated-by '\t'  --null-string '**'  -m 1 --append  --hive-import  --check-column 'TBL_ID' --incremental append --last-value 6
  
2.�����ݴ�hdfs������mysql��  
  sqoop export --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table ids --fields-terminated-by '\t' --export-dir '/ids'
  
3.����Ϊ��ҵ��������ҵ
  sqoop job --create myjob -- import --connect jdbc:mysql://hadoop0:3306/hive  --username root --password admin --table TBLS --fields-terminated-by '\t'  --null-string '**'  -m 1 --append  --hive-import  
  
4. ���뵼������������Mapper����Ϊ��λ��