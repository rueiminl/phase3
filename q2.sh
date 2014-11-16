#!/bin/bash
export CLASSPATH=/usr/share/tomcat7/lib/*:`hbase classpath`
javac src/q2.java -d web/WEB-INF/classes

sudo cp web/WEB-INF/classes/mypackage/q2.class /usr/share/tomcat7/webapps/ROOT/WEB-INF/classes/mypackage/q2.class
sudo cp web/WEB-INF/web.xml /usr/share/tomcat7/webapps/ROOT/WEB-INF/web.xml
