package pinball.stream

import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.Stores
import pinball.stream.aggregators.ScoreAggregator
import pinball.stream.domain.ScoreAggregate
import pinball.stream.domain.ScoreRecord
import pinball.stream.store.processors.UserStoreProcessor
import pinball.stream.store.processors.UserStoreProcessorSupplier
import java.util.*

fun getStreamProps(): Properties {
    var props = Properties()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092,localhost:39092";
    props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"; // Always read from beginning, usually want to only read from provided offset

    props[ProducerConfig.ACKS_CONFIG] = "-1";
    props[ProducerConfig.CLIENT_ID_CONFIG] = "kafka-stream-tutorial-java";

    props[StreamsConfig.APPLICATION_ID_CONFIG] = "kafka-streams-tutorial-java";
    return props
}

fun getScoreRecordSerde(): Serde<ScoreRecord> {
    return KafkaJsonSchemaSerde()
}

const val UserStoreName = "USER_ID_STORE"

fun addStore(builder: StreamsBuilder) {
    val storeBuilder = Stores.keyValueStoreBuilder(
        Stores.inMemoryKeyValueStore(UserStoreName),
        Serdes.String(),
        Serdes.String(),
    )
    builder.addGlobalStore(
        storeBuilder,
        inputTopic,
        Consumed.with(Serdes.String(), Serdes.String()),
        UserStoreProcessorSupplier()
    )
}

const val inputTopic = "pinball.scores"
const val outputTopic = "pinball.highscores"

fun main() {
    val props = getStreamProps()

    var builder = StreamsBuilder()

    addStore(builder)

    builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()))
        .groupBy { k, _ -> k }
        .aggregate({ -> ScoreAggregate() }, ScoreAggregator())
        .toStream()
        .filter { _, value ->  value.isDone() }
        .mapValues { value -> value.getScore() }
        .to(outputTopic, Produced.with(Serdes.String(), getScoreRecordSerde()))

    val stream = KafkaStreams(builder.build(), props)
    stream.start()

    Runtime.getRuntime().addShutdownHook(Thread(stream::close))
}
