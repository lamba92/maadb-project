FROM ubuntu

# fix for https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=863199#23
RUN mkdir -p /usr/share/man/man1

RUN apt-get update
RUN apt-get upgrade -y
RUN apt-get install openjdk-8-jdk humanity-icon-theme- -y

COPY mongo-supervisor.tar mongo-supervisor.tar
RUN tar -xf mongo-supervisor.tar
RUN rm mongo-supervisor.tar

ENV BIN_PATH="/mongo-supervisor/bin"
ENV PATH="${PATH}:$BIN_PATH"

CMD mongo-supervisor
