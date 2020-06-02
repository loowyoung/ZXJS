package ax.hrmw.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class KafkaFactory {
    private KafkaConfig config;

    public KafkaFactory(KafkaConfig config) {
        this.config = config;
    }

    public Integer getPollTimeoutSeconds() {
        Integer result = config.getPollTimeoutSeconds();

        return result == null ? 1 : result;
    }

    public int[] getTopicPartitionIds(String topicName) {
        Properties config = this.config.getProducerConfig();

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(config)) {
            List<PartitionInfo> partitions = producer.partitionsFor(topicName);
            if (partitions.size() == 0) {
                return null;
            }

            return partitions.stream().mapToInt(p -> p.partition()).toArray();
        }
    }

    public KafkaConsumer<String, String> createConsumer(String groupName, String topicName, Integer partition) {
        return createConsumer(groupName, topicName, partition, false);
    }

    public KafkaConsumer<String, String> createConsumer(String groupName, String topicName, Integer partition,
                                                        boolean waitingForSuccess) {
        boolean created = false;
        KafkaConsumer<String, String> result = null;

        Properties config = this.config.getConsumerConfig();
        config.put("group.id", groupName);
        config.put("client.id", String.format("Consumer-%s-%s", topicName, partition));

        while (!created) {
            try {
                result = new KafkaConsumer<>(config);

                if (partition == null) {
                    result.subscribe(Collections.singletonList(topicName));
                } else {
                    TopicPartition topicPartition = new TopicPartition(topicName, partition);
                    result.assign(Collections.singletonList(topicPartition));
                }

                created = true;
            } catch (Exception e) {
                if (!waitingForSuccess) {
                    throw e;
                }

                log.error("Kafka消费者创建失败，正在重建...", e);

                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException ex) {
                }
            }
        }

        return result;
    }
}