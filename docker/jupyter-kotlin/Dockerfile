FROM ubuntu

LABEL maintainer="Lamberto Basti@basti.lamberto@gmail.com"

ARG DEBIAN_FRONTEND=noninteractive
ARG TZ=Europe/Berlin

RUN apt update
RUN apt upgrade -y
RUN apt install openjdk-8-jdk -y
RUN apt install -yq --no-install-recommends build-essential emacs git inkscape jed libsm6 libxext-dev \
    libxrender1 lmodern netcat pandoc python-dev texlive-fonts-extra texlive-fonts-recommended \
    texlive-generic-recommended texlive-latex-base texlive-latex-extra texlive-xetex \
    unzip nano wget bzip2 ca-certificates locales libblas-dev liblapack-dev gfortran wget
RUN apt install python3 python3-pip -y
RUN apt install -y python3-setuptools
RUN apt install -y python3-dev
RUN apt clean

RUN pip3 install wheel
RUN pip3 install jupyter
RUN pip3 install kotlin-jupyter-kernel

CMD ["jupyter", "notebook", "--ip=0.0.0.0", "--no-browser", "--allow-root"]
