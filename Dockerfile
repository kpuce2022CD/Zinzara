FROM python:3.7
  
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

RUN apt-get update
RUN apt-get install -y python3-dev musl-dev
RUN apt-get install -y python3-pip libgl1-mesa-glx
RUN apt-get install -y python3-h5py

RUN pip install --upgrade pip
RUN pip install --upgrade setuptools

RUN mkdir /config

ADD /config/requirements.txt /config/
RUN pip3 install -r /config/requirements.txt

RUN mkdir /zinzaraServer;

WORKDIR /zinzaraServer