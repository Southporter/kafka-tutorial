# Part 1

Welcome! Let's get started.

## Pick your poison
Kafka has libraries for most languages. Pick a language you would like
to use, and If you cannot find your favorite
language in this folder, feel free to open an Issue or PR.

## Getting going
Depending on your language this will be a bit different. First install
packages. Then look into the main file.

### Producing Basics
In your main file, you will find some code for initializing the Kafka
Producer. There are many more configuration options, which I won't go
into here other than a few key properties:

| key | alternate |  value |
| --- | ----- | ---- |
| client.id | clientId | Identifies the producer from others. |
| bootstrap.servers | brokers | A list of urls for connecting to kafka. It is good practice to either have multiple urls in this list or to have it be a loadbalanced url that can connect to the brokers |
| acks | - | The level at which you want acknowledgement for receipt of a message. -1 means that all brokers need to acknowledge, 1 means only the leader acknowledge, and 0 is fire-and-forget |

#### Producing Values
After you have connected, you can now start to produce messages to Kafka.
Messages must have at least the topic specified. Messages may or may not have
a key and a value, but neither are required. However a message without either
a key or a value doesn't have much meaning, so you should always make sure to
send a value.

### Consuming Basics
In your main file, you will also find code for consuming from a Kafka topic.
Again, there are a lot of config options, but look at the documentation
for more information. Here are a few key properties:

| key | alternate | value |
| group.id | groupId | Identifies the group to which the consumer belongs. This group id is used to coordinate between consumers so that all partitions are covered. It will rebalance partition assignments if a new consumer joins or one drops out |
| bootstrap.servers | brokers | same as for producers |