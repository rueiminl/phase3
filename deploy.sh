#!/bin/bash
sudo iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT --to-port 8080

sudo cp other/catalina.properties /etc/tomcat7/catalina.properties

sudo mkdir /usr/share/tomcat7/webapps/ROOT/

# note: need s3cmd --configure if no role
wget http://softlayer-dal.dl.sourceforge.net/project/s3tools/s3cmd/1.5.0-rc1/s3cmd-1.5.0-rc1.tar.gz
tar zxvf s3cmd-1.5.0-rc1.tar.gz
cd s3cmd-1.5.0-rc1
sudo python setup.py install
sudo s3cmd get s3://raylin/q6.txt /usr/share/tomcat7/webapps/ROOT
cd ..

export CLASSPATH=/usr/share/tomcat7/lib/*:`hbase classpath`
javac src/q?.java -d web/WEB-INF/classes
sudo cp -r web/WEB-INF /usr/share/tomcat7/webapps/ROOT
sudo service tomcat7 start
