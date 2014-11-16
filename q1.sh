#!/bin/bash
export CLASSPATH=/usr/share/tomcat7/lib/*
javac src/q1.java -d web/WEB-INF/classes

sudo cp web/WEB-INF/classes/mypackage/q1.class /usr/share/tomcat7/webapps/ROOT/WEB-INF/classes/mypackage/q1.class
sudo cp web/WEB-INF/web.xml /usr/share/tomcat7/webapps/ROOT/WEB-INF/web.xml
