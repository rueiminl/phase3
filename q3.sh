#!/bin/bash
export CLASSPATH=/usr/share/tomcat7/lib/*:`hbase classpath`
javac src/q3.java -d web/WEB-INF/classes

sudo cp web/WEB-INF/classes/mypackage/q3.class /usr/share/tomcat7/webapps/ROOT/WEB-INF/classes/mypackage/q3.class
