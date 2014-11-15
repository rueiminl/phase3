#!/bin/bash
sudo iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT --to-port 8080

sudo cp other/catalina.properties /etc/tomcat7/catalina.properties

export CLASSPATH=/usr/share/tomcat7/lib/*:`hbase classpath`
javac src/q?.java -d web/WEB-INF/classes
sudo mkdir /usr/share/tomcat7/webapps/ROOT/
sudo cp -r web/WEB-INF /usr/share/tomcat7/webapps/ROOT
sudo service tomcat7 start
