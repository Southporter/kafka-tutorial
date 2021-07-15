# Part 2 - Connectors

Kafka has another piece called Connect. Connect is an externally running
program that can produce and consume Kafka topics. The Connect process
by itself doesn't do anything. You need Connectors to tell Connect how
to produce data or consume it.

Connectors build on Consumers and Producers and help provide prebuilt,
tested integrations with external resources. They come in 2 flavors:
sources and sinks. These can be correlated as such:

- Sources = Producers
- Sinks = Consumers

## Sources
Sources bring data from external resources and dump it into topics. This
is a great way to take an existing data store and turning it into kafka
message streams.

For example, you can take Change Data Capture from a database and turn
those into messages in a topic. Or you could listen to an AWS Kinesis
stream and dump those messages into a topic. No matter what source you
are handling, the result is always messages in a Kafka topic.

## Sinks
Sinks take data out of a topic and dump it into an external destination.
This is a great way to turn Kafka data into something a more traditional
application can handle (i.e. database queries).

For example, you can put every message into a database table, or use it
to trigger a Lambda function. Anything that can't read a topic directly
could benefit from a Connect Sink.

## Single Message Transforms (SMT)
Usually the data for both Sinks and Sources need to be transformed to some
degree before it can be useful. Kafka has a number of prebuilt
[Single Message Transforms](https://docs.confluent.io/platform/current/connect/transforms/overview.html)
that you can use to manipulate the individual messages before they go into
Kafka or are sent to your destination. These transforms can be chained
to do things like select a key, pull a nested object out as the main value,
route message to topics based on a regex of the message, etc.

## Activity

For part 2, we will be deploying a Source and a Sink, as well as letting you
create a connector of your own. You will find a few utility scripts that 
make interacting with the Connect container easier.

### PUTting a connector
The Connect container exposes a REST API that allows you to add a connector,
check its status, and delete connectors. The main api is a PUT request
that uses a json payload to configure a connector.

You will see the [put-connectors.sh](put-connector.sh) file is reading the 
json, getting the connector name, and then curling the contents of that file
to the connect container. [get-connector-status.sh](get-connector-status.sh) is
similar except it is a GET request to get the status. This is useful for debuggin
or finding out why your connector is not working.

### Step 1
Let's start by putting the source connector. The Connect container has a file in
`/etc/kafka-connect` folder that contains a list of usernames. Run the following
to put the usernames into the `pinball.users` topic:

```shell
./put-connector.sh users-source.json
```

This connector will read each line in the file, change the string into a JSON
structure, and put it into the topic. To see the output, run the following:
```shell
# Make sure you are in the infra folder
cd ../infra
docker-compose exec kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic pinball.users --from-beginning
```

### Step 2
Next we will deploy a JDBC sink connector to dump these usernames into a
database. This connector assumes the table has already been created, but
if you have a structure that already has a primary key, you can have the 
JDBC connector create and manage the table for you.

```shell
./put-connector.sh users-sink.json
```

Pull up your favorite mysql database explorer and connect to `127.0.0.1:3306`
with the username of `kafka` and password of `tutorial`. You should see a 
user table and the results of the `pinball.users` topic should be a row
in the database table.


### Assignement
We need a connector to dump game scores into the database. In the next parts
of the tutorial, we will be creating a Kafka stream app that will aggregate
the scores and place the result in the `pinball.highscores` topic in the
following shape:

```json
{
  "userId": "3",
  "score": "150",
  "gameId": "15"
}
```

Your assignment is to set up another JDBC connector that puts that object
into a `score` table with the following columns:

| column | type | content |
| ------ | ---- | ------- |
| id | integer | an auto-incrementing unique identifier for each score |
| userId | integer | the id of a user. This is a foreign key into the users table |
| score | BigInt | The score for that game |

You will need at least 2 transform for this: Dropping the gameId key, and converting
the strings to numbers.

Good luck!

#### Testing
To test the connector, there are 2 values that we can send into the `pinball.highscores`
topic to verify that it is working.

```shell
# Need to be in the infra directory
cd ../infra

docker-compose exec kafka-1 /bin/sh

kafka-console-producer --bootstrap-server localhost:9092 \
   --topic pinball.highscores \
   --property value.serializers=io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer \
   --property value.serializers.schema.registry.url=http://schema-registry:8081 \
   < /etc/tutorial/example-scores.txt
```