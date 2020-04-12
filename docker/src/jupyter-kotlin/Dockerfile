FROM ubuntu

LABEL maintainer="Lamberto Basti@basti.lamberto@gmail.com"

ARG DEBIAN_FRONTEND=noninteractive
ARG TZ=Europe/Berlin

RUN apt update > /dev/null
RUN apt upgrade -y > /dev/null
RUN apt install openjdk-8-jdk -y > /dev/null
RUN apt install -yq --no-install-recommends build-essential emacs git inkscape jed libsm6 libxext-dev \
    libxrender1 lmodern netcat pandoc python-dev texlive-fonts-extra texlive-fonts-recommended \
    texlive-generic-recommended texlive-latex-base texlive-latex-extra texlive-xetex \
    unzip nano wget bzip2 ca-certificates locales libblas-dev liblapack-dev gfortran wget > /dev/null
RUN apt install python3 python3-pip -y > /dev/null
RUN apt install -y python3-setuptools > /dev/null
RUN apt install -y python3-dev > /dev/null
RUN apt clean > /dev/null

RUN pip3 install wheel > /dev/null
RUN pip3 install jupyter > /dev/null
RUN pip3 install kotlin-jupyter-kernel > /dev/null

CMD ["jupyter", "notebook", "--ip=0.0.0.0", "--no-browser", "--allow-root"]
