FROM ubuntu

# fix for https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=863199#23
RUN mkdir -p /usr/share/man/man1

RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install openjdk-8-jdk humanity-icon-theme- -y

COPY sql-analytics-server.tar sql-analytics-server.tar
RUN tar -xf sql-analytics-server.tar
RUN rm sql-analytics-server.tar

ENV BIN_PATH="/sql-analytics-server/bin"
ENV PATH="${PATH}:$BIN_PATH"
