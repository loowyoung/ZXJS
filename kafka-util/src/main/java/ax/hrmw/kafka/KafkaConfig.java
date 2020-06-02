package ax.hrmw.kafka;

import lombok.Data;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfig {
    /**
     * 序列化配置
     */
    private static final String SERIALIZER_CLASS_KEY = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String SERIALIZER_CLASS_VALUE = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * 反序列化配置
     */
    private static final String DESERIALIZER_CLASS_KEY = "org.apache.kafka.common.serialization.StringDeserializer";
    private static final String DESERIALIZER_CLASS_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";

    private String servers;
    private Integer maxPollRecords = 50;
    private Integer pollTimeoutSeconds = 1;
    private List<Topic> topics = new ArrayList<>();

    public Properties getProducerConfig() {
        Properties result = getCommonConfig();

        result.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, SERIALIZER_CLASS_KEY);
        result.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SERIALIZER_CLASS_VALUE);
        result.put(ProducerConfig.RETRIES_CONFIG, 3);
        result.put(ProducerConfig.ACKS_CONFIG, "1");

        return result;
    }

    public Properties getConsumerConfig() {
        Properties result = getCommonConfig();

        result.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, DESERIALIZER_CLASS_KEY);
        result.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DESERIALIZER_CLASS_VALUE);
        result.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        result.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);

        return result;
    }

    private Properties getCommonConfig() {
        Properties result = new Properties();

        result.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, this.servers);

        return result;
    }

    @Data
    public static class Topic {
        private String desc;
        private String topic;
    }
}