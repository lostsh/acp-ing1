#Alpine base image
FROM alpine


#Install R
RUN apk add R

#Install texlive -> latexmk
RUN apk add texlive-full

#Install make
RUN apk add make

#Install pdfunite
RUN apk add poppler-utils

#Create project directory
RUN mkdir -p /opt/tp
COPY . /opt/tp
WORKDIR "/opt/tp"
