FROM tomcat:10.1-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*
COPY build/libs/app.war /usr/local/tomcat/webapps/ROOT.war