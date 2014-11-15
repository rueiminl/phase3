#!/bin/bash
sudo iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT --to-port 8080

sudo cp -r web /usr/share/tomcat7/webapps/ROOT
sudo service tomcat7 start

export CLASSPATH=/usr/share/tomcat7/lib/*
javac src/q1.java -d web/WEB-INF/classes

