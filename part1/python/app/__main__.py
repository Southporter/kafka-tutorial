from confluent_kafka import Producer, Consumer


def run():
    producer = Producer({
        'bootstrap.servers': 'localhost:29092,localhost:39092',
        'client.id': 'kafka-tutorial-python'
    })
    producer.produce(topic='pinball.scores', key='10', value='50')
    producer.poll(1.0)
    producer.flush()

    consumer = Consumer({
        'bootstrap.servers': 'localhost:29092,localhost:39092',
        'group.id': 'kafka-tutorial-python-consumer-group',
        'auto.offset.reset': 'earliest'
    })
    consumer.subscribe(['pinball.scores'])
    record = consumer.poll(1.0)
    if record is None:
        print("No record to consume")
    else:
        print(f"Key: {record.key()} and value: {record.value()} was sent to partition {record.partition()} of topic {record.topic()}")


if __name__ == "__main__":
    run()
