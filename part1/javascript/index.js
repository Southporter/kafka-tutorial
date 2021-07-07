const { Kafka } = require('kafkajs');

const kafka = new Kafka({
    clientId: 'kafka-tutorial-js',
    brokers: ['localhost:29092', 'localhost:39092'],
});

const producer = kafka.producer();
const consumer = kafka.consumer({ groupId: 'kafka-tutorial-js-consumer-group'});

(async () => {
    // Produce
    await producer.connect();
    await producer.send({
        topic: 'pinball.scores',
        messages: [
            { key: 10, value: "10" },
        ],
    });

    await producer.disconnect();

    // And consume
    await consumer.connect();
    await consumer.subscribe({ topic: "pinball.scores" });
    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            console.log(`Key: ${message.key} and value: ${message.value} was sent to partition ${partition} of ${topic}`)
        }
    });
})().catch(e => console.error("Error in producer", e))