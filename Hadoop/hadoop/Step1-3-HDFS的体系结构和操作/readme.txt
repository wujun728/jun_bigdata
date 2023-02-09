1.对hdfs操作的命令格式是hadoop fs 
	1.1 -ls		<path>	表示对hdfs下一级目录的查看
	1.2 -lsr	<path>	表示对hdfs目录的递归查看
	1.3	-mkdir	<path>	创建目录
	1.4 -put	<src>	<des>	从linux上传文件到hdfs
	1.5 -get	<src>	<des>	从hdfs下载文件到linux
	1.6 -text	<path>	查看文件内容
	1.7 -rm		<path>	表示删除文件
	1.7 -rmr	<path>	表示递归删除文件
2.hdfs在对数据存储进行block划分时，如果文件大小超过block，那么按照block大小进行划分；不如block size的，划分为一个块，是实际数据大小。

*****PermissionDenyException  权限不足**********