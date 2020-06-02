package ax.hrmw.kafka.util;

import ax.hrmw.kafka.model.KafkaProducerConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 消息生产者
 *
 * @author ly
 * @date 2020年 05月13日 22:44:53
 */
@Slf4j
@Data
public class QueueProducer {
    private KafkaProducerConfig config;//消息生产者配置

    private Producer<String, String> producer;//消息生产者

    /**
     * 构造方法，传入生产者配置
     *
     * @param config
     */
    public QueueProducer(KafkaProducerConfig config) {
        this.config = config;
        if (null == producer) {
            producer = new KafkaProducer<>(config.toProperties());//创建生产者
            log.info("创建kafka生产者，topic：{}", config.getTopicName());
        }
    }

    /**
     * kafka消息发送
     *
     * @param key
     * @param message
     * @return
     */
    public void produce(String key, String message) {
        String topic = config.getTopicName();
        produce(topic, key, message);
    }

    /**
     * kafka消息发送
     *
     * @param key
     * @param message
     * @return
     */
    public void produce(String topic, String key, String message) {
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(key) || StringUtils.isBlank(message)) {
            log.error("队列消息不完整：Topic：{}，Key：{}，Msg：{}", topic, key, message);
            return;
        }
        log.debug("kafka生产者消息体，Topic：{},Key：{},Msg：{}", topic, key, message);
        ProducerRecord<String, String> data = new ProducerRecord<>(topic, key, message);
        //发送消息
        producer.send(data);
    }
}
