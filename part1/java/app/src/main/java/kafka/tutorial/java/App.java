package kafka.tutorial.java;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Future;

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

    private static void sendSync(Producer producer, File file) throws Exception {
        var reader = new BufferedReader(new FileReader(file));
        var line = reader.readLine();
        while (line != null) {
            var parts = line.split(":");
            assert(parts.length == 2);
            var response = producer.send(new ProducerRecord<>("pinball.scores",  parts[0], parts[1]));
            // In this case order matters, so we block waiting for each send to complete.
            response.get();

            line = reader.readLine();
        }
    }

    private static void sendAsync(Producer producer, File file) throws Exception {
        var reader = new BufferedReader(new FileReader(file));
        var line = reader.readLine();
        var responses = new ArrayList<Future<RecordMetadata>>();
        while (line != null) {
            var parts = line.split(":");
            assert(parts.length == 2);
            // In this case, order doesn't matter, so we can send them asynchronously
            responses.add(producer.send(new ProducerRecord<>("pinball.users", parts[0], parts[1])));

            line = reader.readLine();
        }
        // Make sure they all made it
        for (var response : responses) {
            response.get();
        }

    }

    public static void main(String[] args) throws Exception {
        var scoresFile = new File("../../scores.txt");
        var producer = new KafkaProducer<String, String>(getProducerConnectionProperties());

        sendSync(producer, scoresFile);

        var idsFile = new File("../../ids.txt");
        sendAsync(producer, idsFile);
    }
}
