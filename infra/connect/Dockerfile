FROM confluentinc/cp-kafka-connect:6.2.0

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:10.2.0

RUN curl -L https://downloads.mariadb.com/Connectors/java/connector-java-2.7.3/mariadb-java-client-2.7.3.jar > /usr/share/confluent-hub-components/confluentinc-kafka-connect-jdbc/lib/mariadb-java-client-2.7.3.jar

COPY ./users.txt /etc/kafka-connect/users.txt