FROM ubuntu

LABEL maintainer="Lamberto Basti@basti.lamberto@gmail.com"

RUN apt update
RUN apt upgrade -y > /dev/null

# fix for https://bugs.debian.org/cgi-bin/bugreport.cgi?bug=863199#23
RUN mkdir -p /usr/share/man/man1

RUN apt install openjdk-8-jdk -y
RUN apt install -yq --no-install-recommends build-essential wget > /dev/null
RUN apt install python3 python3-pip -y > /dev/null
RUN apt install -y python3-setuptools > /dev/null
RUN apt install -y python3-dev > /dev/null
RUN apt clean

RUN pip3 install wheel > /dev/null
RUN pip3 install jupyter > /dev/null
RUN pip3 install kotlin-jupyter-kernel > /dev/null

CMD ["jupyter", "notebook", "--ip=0.0.0.0", "--no-browser", "--allow-root"]
