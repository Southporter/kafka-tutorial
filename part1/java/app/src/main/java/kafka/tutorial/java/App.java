package kafka.tutorial.java;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class App {

    private static Properties getProducerConnectionProperties() {
        var properties = new Properties();
        properties.put("bootstrap.servers", "localhost:29092,localhost:39092");
        properties.put("client.id", "kafka-tutorial-java");
        properties.put("acks", "-1");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    private static Properties getConsumerConnectionProperties() {
        var properties = new Properties();
        properties.put("bootstrap.servers", "localhost:29092,localhost:39092");
        properties.put("client.id", "kafka-tutorial-java");
        properties.put("group.id", "kafka-tutorial-java-consumer-group");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.offset.reset", "earliest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(getProducerConnectionProperties());
        var response = producer.send(new ProducerRecord<>("pinball.scores",  "10", "150"));
        response.get();

        var consumer = new KafkaConsumer<String, String>(getConsumerConnectionProperties());
        consumer.subscribe(List.of("pinball.scores"));

        var records = consumer.poll(Duration.ofSeconds(5));
        System.out.println("Found " + records.count() + " records");
        for (var record : records) {
            System.out.printf("Key: %s and value: %s was sent to partition %d of topic %s\n",
                record.key(),
                record.value(),
                record.partition(),
                record.topic());
        }
    }
}
