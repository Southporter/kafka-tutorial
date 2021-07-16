package kafka.tutorial.scala

import domain.ScoreRecord

import io.confluent.kafka.serializers.{KafkaJsonDeserializer, KafkaJsonSerializer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import java.util.{Collections, Properties}

object Stream extends App {
  import Serdes._

  val inputTopic = "pinball.scores"
  val outputTopic = "pinball.highscores"

  implicit val recordSerde: Serde[ScoreRecord] = getScoreRecordSerde()

  val builder: StreamsBuilder = new StreamsBuilder

  val inputs: KStream[String, String] = builder.stream[String, String](inputTopic)(Consumed.`with`[String, String])
  val mappedInputs: KStream[String, ScoreRecord] = inputs.mapValues((v) => ScoreRecord(score = v.toLong))
  mappedInputs.to(outputTopic)(Produced.`with`[String, ScoreRecord])

  val stream = new KafkaStreams(builder.build, getStreamProps())

  private def getStreamProps(): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // Always read from beginning, usually want to only read from provided offset


    props.put(ProducerConfig.ACKS_CONFIG, "-1")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka-stream-tutorial-java")

    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-tutorial-java")
    props
  }

  private def getScoreRecordSerde(): Serde[ScoreRecord] = {
    val serdeProps: java.util.Map[String, Object] = Collections.singletonMap("json.value.type", classOf[ScoreRecord])
    val serializer: Serializer[ScoreRecord] = new KafkaJsonSerializer[ScoreRecord]()
    serializer.configure(serdeProps, false)
    val deserializer: Deserializer[ScoreRecord] = new KafkaJsonDeserializer[ScoreRecord]()
    deserializer.configure(serdeProps, false)
    Serdes.fromFn(
      (topic, data) => serializer.serialize(topic, data),
      (topic, bytes) => Option(deserializer.deserialize(topic, bytes))
    )
  }
}
