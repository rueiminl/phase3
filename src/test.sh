#!/bin/bash
javac -cp `hbase classpath` TestHBase.java
java -cp .:`hbase classpath` TestHBase
