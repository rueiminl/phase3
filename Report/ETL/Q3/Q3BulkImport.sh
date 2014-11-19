#!/bin/bash
mkdir q3bulkimport_classes
hdfs dfs -rm -r /mnt/q3
javac -cp `hbase classpath` -d q3bulkimport_classes Q3BulkImport.java
jar -cvf q3bulkimport.jar -C q3bulkimport_classes/ .
hadoop jar q3bulkimport.jar Q3BulkImport
hbase org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles /mnt/q3 q3

