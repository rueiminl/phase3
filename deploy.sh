#!/bin/bash
sudo iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT --to-port 8080

export CLASSPATH=/usr/share/tomcat7/lib/*
javac src/q?.java -d web/WEB-INF/classes

sudo cp -r web /usr/share/tomcat7/webapps/ROOT
sudo service tomcat7 restart
