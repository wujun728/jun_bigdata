1.flume�Ƿֲ�ʽ����־�ռ�ϵͳ�����ռ��������ݴ��͵�Ŀ�ĵ�ȥ��
2.flume�����и����ĸ������agent��agent��һ��java���̣���������־�ռ��ڵ㡣
3.agent�������3�����������source��channel��sink��
3.1 source�����ר�����ռ���־�ģ����Դ���������͸��ָ�ʽ����־����,����avro��thrift��exec��jms��spooling directory��netcat��sequence generator��syslog��http��legacy���Զ��塣
    source����������ռ����Ժ���ʱ�����channel�С�
3.2 channel�������agent��ר������ʱ�洢���ݵģ����Դ����memory��jdbc��file���Զ��塣
    channel�е�����ֻ����sink���ͳɹ�֮��Żᱻɾ����
3.3 sink��������ڰ����ݷ��͵�Ŀ�ĵص������Ŀ�ĵذ���hdfs��logger��avro��thrift��ipc��file��null��hbase��solr���Զ��塣
4.���������ݴ�������У���������event������֤����event����
5.flume����֧�ֶ༶flume��agent��֧������(fan-in)���ȳ�(fan-out)��




6.��д�����ļ�example

#agent1��ʾ��������
agent1.sources=source1
agent1.sinks=sink1
agent1.channels=channel1


#Spooling Directory�Ǽ��ָ���ļ��������ļ��ı仯��һ�����ļ����֣��ͽ������ļ����ݣ�Ȼ��д�뵽channle��д����ɺ󣬱�Ǹ��ļ�����ɻ���ɾ�����ļ���
#����source1
agent1.sources.source1.type=spooldir
agent1.sources.source1.spoolDir=/root/hmbbs
agent1.sources.source1.channels=channel1
agent1.sources.source1.fileHeader = false
agent1.sources.source1.interceptors = i1
agent1.sources.source1.interceptors.i1.type = timestamp

#����sink1
agent1.sinks.sink1.type=hdfs
agent1.sinks.sink1.hdfs.path=hdfs://hadoop0:9000/hmbbs
agent1.sinks.sink1.hdfs.fileType=DataStream
agent1.sinks.sink1.hdfs.writeFormat=TEXT
agent1.sinks.sink1.hdfs.rollInterval=1
agent1.sinks.sink1.channel=channel1
agent1.sinks.sink1.hdfs.filePrefix=%Y-%m-%d

#����channel1
agent1.channels.channel1.type=file
agent1.channels.channel1.checkpointDir=/root/hmbbs_tmp/123
agent1.channels.channel1.dataDirs=/root/hmbbs_tmp/


7.ִ������bin/flume-ng agent -n agent1 -c conf -f conf/example -Dflume.root.logger=DEBUG,console




