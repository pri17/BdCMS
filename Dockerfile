FROM tomcat:jre8
MAINTAINER xuejike "xuejike2004@126.com"
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
COPY /target/BdCMS-1.0-SNAPSHOT /usr/local/tomcat/webapps/ROOT/
RUN ls /usr/local/tomcat/webapps/ROOT/
EXPOSE 8080
CMD ["catalina.sh", "run"]