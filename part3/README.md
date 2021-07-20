# Kafka Streams

Kafka Streams is a set of APIs that sit on top of Producers and
Consumers and abstracts away a lot of the complexity and configuration.
Kafka Streams also provides ways to deal with messages one at a time,
allowing you to do things like transformations, aggregations, etc.

Kafka Streams is currently only available to JVM languages.

## Assignement

You will find a couple folders containing a Stream app in a few
different JVM languages. Pick one and dive in!

The goal of this part is to take the events you produced in [Part 1](../part1)
and transform them into the format you expected in your connector in
[Part 2](../part2).

The input will be in the `pinball.scores` topic with roughly the following JSON:

```json
{
  "gameId": "12",
  "points": "150"
}
```

The output will go to the `pinball.highscores` topic with roughly the following
JSON:

```json
{
  "gameId": "12",
  "score": "1545",
  "userId": "10"
}
```

If you are unsure of where to start, the following section should help:

### Breaking it down

Here is a high level break down of some of the steps you might code up.

#### Option 1

1. Create a [KTable](https://javadoc.io/doc/org.apache.kafka/kafka-streams/latest/org/apache/kafka/streams/kstream/KTable.html)
the `pinball.users` topic. We will be using this later.
1. Create a [KGroupedStream](https://javadoc.io/doc/org.apache.kafka/kafka-streams/latest/org/apache/kafka/streams/kstream/KGroupedStream.html)
 from the `pinball.scores` topic. Group on the gameId.
1. [Aggregate](https://javadoc.io/doc/org.apache.kafka/kafka-streams/latest/org/apache/kafka/streams/kstream/KGroupedStream.html)
or [Reduce](https://javadoc.io/doc/org.apache.kafka/kafka-streams/latest/org/apache/kafka/streams/kstream/KGroupedStream.html)
the GroupedKStream app to only emit a value when you get
a `DONE` or `GAME OVER` message. This should then emit the gameId and total score.
1. [Join](https://javadoc.io/static/org.apache.kafka/kafka-streams/2.8.0/org/apache/kafka/streams/kstream/KStream.html#join-org.apache.kafka.streams.kstream.KTable-org.apache.kafka.streams.kstream.ValueJoiner-org.apache.kafka.streams.kstream.Joined-)
the two KStreams on gameId, so that you can get the userId.
1. Send the values to the `pinball.highscores` topic.

#### Option 2

1. Add a [Global State Store](https://javadoc.io/static/org.apache.kafka/kafka-streams/2.8.0/org/apache/kafka/streams/StreamsBuilder.html#addGlobalStore-org.apache.kafka.streams.state.StoreBuilder-java.lang.String-org.apache.kafka.streams.kstream.Consumed-org.apache.kafka.streams.processor.ProcessorSupplier-)
from the `pinball.users` topic to look up the user id from the game id.
1. Create a [KStream](https://javadoc.io/static/org.apache.kafka/kafka-streams/2.8.0/org/apache/kafka/streams/StreamsBuilder.html#stream-java.util.Collection-org.apache.kafka.streams.kstream.Consumed-)
from the `pinball.scores` topic.
1. Statefully [transform](://javadoc.io/static/org.apache.kafka/kafka-streams/2.8.0/org/apache/kafka/streams/kstream/KStream.html#transformValues-org.apache.kafka.streams.kstream.ValueTransformerSupplier-java.lang.String...-)
the KStream to create the new value.
1. Send the values to the `pinball.highscores` topic.
