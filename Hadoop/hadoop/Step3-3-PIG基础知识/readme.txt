1.Pig是基于hadoop的一个数据处理的框架。
  MapReduce是使用java进行开发的，Pig有一套自己的数据处理语言，Pig的数据处理过程要转化为MR来运行。
2.Pig的数据处理语言是数据流方式的，类似于初中做的数学题。
3.Pig基本数据类型：int、long、float、double、chararry、bytearray
     复合数据类型：Map、Tuple、Bag  
	 Bag的类型如{('age',31),('name','张三')}
4.如何安装Pig
4.1 把pig-0.11.1.tar.gz复制到/usr/local下
4.2 使用命令tar -zxvf  pig-0.11.1.tar.gz解压缩
4.3 使用命令mv pig-0.11.1  pig 进行重命名
4.4 编辑文件vi /etc/profile 设置环境变量
    export $PIG_HOME=/usr/local/bin
	export PATH =......$PIG_HOME/bin....
	保存，然后执行source  /etc/profile
4.5 编辑文件$PIG_HOME/conf/pig.properties，增加两行如下内容
    fs.default.name=hdfs://hadoop0:9000
	mapred.job.tracker=hadoop0:9001
5.对wlan数据如何使用pig进行分析处理
5.1 把待处理的数据上传到HDFS中
5.2 把HDFS中的数据转换为pig可以处理的模式
    A = LOAD '/wlan' AS (t0:long, msisdn:chararray, t2:chararray, t3:chararray, t4:chararray, t5:chararray, t6:long, t7:long, t8:long, t9:long, t10:chararray);
5.3 把里面的有用的字段抽取出来
    B = FOREACH A GENERATE msisdn, t6, t7, t8, t9;	
5.4 分组数据
    C = GROUP B BY msisdn;	
5.5 流量汇总
    D = FOREACH C GENERATE 	group, SUM(B.t6), SUM(B.t7), SUM(B.t8), SUM(B.t9);
5.6 存储到HDFS中
    STORE D INTO '/wlan_result';	
