FROM adoptopenjdk:8u252-b09-jdk-hotspot-bionic

ARG APP_NAME
ARG TAR_NAME

COPY $TAR_NAME.tar $TAR_NAME.tar
RUN tar -xf $TAR_NAME.tar
RUN rm $TAR_NAME.tar

ENV BIN_PATH="/$TAR_NAME/bin"
ENV PATH="$PATH:$BIN_PATH"

# workaround for https://github.com/moby/moby/issues/34772
ENV RUN_COMMAND=$APP_NAME
CMD $RUN_COMMAND
