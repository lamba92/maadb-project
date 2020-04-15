FROM openjdk

COPY sql-analytics-server.tar sql-analytics-server.tar
RUN tar -xf sql-analytics-server.tar
RUN rm sql-analytics-server.tar

ENV BIN_PATH="/sql-analytics-server/bin"
ENV PATH="${PATH}:$BIN_PATH"

CMD sql-analytics-server
