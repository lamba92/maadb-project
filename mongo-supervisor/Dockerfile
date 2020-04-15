FROM openjdk

COPY mongo-supervisor.tar mongo-supervisor.tar
RUN tar -xf mongo-supervisor.tar
RUN rm mongo-supervisor.tar

ENV BIN_PATH="/mongo-supervisor/bin"
ENV PATH="${PATH}:$BIN_PATH"

CMD mongo-supervisor
