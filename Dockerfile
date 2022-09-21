FROM ubuntu:20.04

RUN apt update -y

RUN apt install openjdk-8-jdk -y

RUN apt install wget -y
RUN wget https://downloads.lightbend.com/scala/2.12.0/scala-2.12.0.deb
RUN dpkg -i scala-2.12.0.deb

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list
RUN apt install curl -y
RUN apt-get install gnupg -y
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update -y 
RUN apt-get install sbt -y
 
RUN apt install nodejs -y
RUN apt install npm -y

EXPOSE 3000 9000

RUN mkdir dataExchange
VOLUME dataExchange