FROM ubuntu

RUN apt update
RUN apt upgrade -y
RUN apt install openjdk-8-jdk -y

COPY build/sql-analytics-server.tar sql-analytics-server.tar
RUN tar -xf sql-analytics-server.tar
RUN rm sql-analytics-server.tar

ENV BIN_PATH="/sql-analytics-server/bin"
ENV PATH="${PATH}:$BIN_PATH"
