#!/bin/bash
mkdir q2bulkimport_classes
hdfs dfs -rm -r /mnt/q2
hdfs dfs -mkdir /mnt/lib
hdfs dfs -put json-simple-1.1.1.jar /mnt/lib
hdfs dfs -mkdir /mnt/cache
hdfs dfs -put AFINN.txt /mnt/cache
hdfs dfs -put banned.txt /mnt/cache
javac -cp `hbase classpath`:json-simple-1.1.1.jar -d q2bulkimport_classes Q2BulkImport.java
jar -cvf q2bulkimport.jar -C q2bulkimport_classes/ .
export HADOOP_CLASSPATH=`hbase classpath`:json-simple-1.1.1.jar
hadoop jar q2bulkimport.jar Q2BulkImport
hbase org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles /mnt/q2 q2

