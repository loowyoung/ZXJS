package ax.hrmw.kafka.util;

import ax.hrmw.kafka.handler.ConsumeHandler;
import ax.hrmw.kafka.model.KafkaConsumerConfig;
import ax.hrmw.kafka.model.KafkaProducerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.Utils;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: ly
 * @date: 2020/3/18 10:49
 */
@Slf4j
public class KafkaUtils {

    /**
     * kafka消息发送
     *
     * @param config  kafka配置
     * @param key
     * @param message
     * @return
     */
    public void produce(KafkaProducerConfig config, String key, String message) {
        String topic = config.getTopicName();
        if (StringUtils.isBlank(topic) || StringUtils.isBlank(key) || StringUtils.isBlank(message)) {
            log.error("队列消息不完整：Topic：{}，Key：{}，Msg：{}", topic, key, message);
            return;
        }
        log.debug("kafka生产者，Topic：{},Key：{},Msg：{}", topic, key, message);
        // 创建生产者
        Producer<String, String> producer = new KafkaProducer<>(config.toProperties());
        ProducerRecord<String, String> data = new ProducerRecord<>(topic, key, message);
        //发送消息
        producer.send(data);
        //关闭生产者
        producer.close();
    }

    /**
     * kafka消息发送
     *
     * @param config     kafka配置
     * @param messageMap 待发送消息集合 Map<key,message>
     * @return
     */
    public void produce(KafkaProducerConfig config, Map<String, String> messageMap) {
        String topic = config.getTopicName();
        if (StringUtils.isBlank(topic) || messageMap.size() == 0) {
            log.error("队列消息不完整：Topic：{}，Map<key,message>：{}", topic, messageMap.toString());
            return;
        }
        // 创建生产者
        Producer<String, String> producer = new KafkaProducer<>(config.toProperties());
        messageMap.forEach((k, v) -> {
            if (StringUtils.isBlank(k) || StringUtils.isBlank(v)) {
                return;
            }
            log.debug("kafka生产者，Topic：{},Key：{},Msg：{}", topic, k, v);
            ProducerRecord<String, String> data = new ProducerRecord<>(topic, k, v);
            //发送消息
            producer.send(data);
        });
        //关闭生产者
        producer.close();
    }

    /**
     * kafka消息消费
     *
     * @param config  kafka配置
     * @param handler 消费者处理类
     */
    public void consume(KafkaConsumerConfig config, ConsumeHandler handler) {
        String topicName = config.getTopicName();

        //创建消费者，并配置
        KafkaConsumer<String, String> consumer;
        //获取所有分区
        int[] partitionIds = getTopicPartitionIds(config, topicName);
        if (partitionIds == null) {
            log.info("kafka消费者，topic：{}", topicName);
            consumer = new KafkaConsumer<>(config.toProperties());
            consumer.subscribe(Collections.singletonList(topicName));
            startConsume(config, handler, consumer);
        } else {
            for (int partitionId : partitionIds) {
                log.info("kafka消费者，topic：{}，partitionId：{}", topicName, partitionId);
                consumer = new KafkaConsumer<>(config.toProperties());
                TopicPartition topicPartition = new TopicPartition(topicName, partitionId);
                consumer.assign(Collections.singletonList(topicPartition));
                startConsume(config, handler, consumer);
            }
        }
    }

    /**
     * 开始消费消息
     *
     * @param config
     * @param handler
     * @param consumer
     */
    private void startConsume(KafkaConsumerConfig config, ConsumeHandler handler, KafkaConsumer<String, String> consumer) {
        //开线程，监听消费
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    //拉取消息
                    ConsumerRecords<String, String> records = consumer.poll(config.getPollTimeoutMills());
                    if (records.isEmpty()) {//如果本次拉取数据为空，则停顿一下，继续下次拉取
                        Utils.sleep(config.getMaxPollIntervalMills());
                        continue;
                    }
                    //处理消息
                    handler.consumerMultiple(records);
                    //提交消费的offset
                    consumer.commitSync();
                } catch (Exception e) {
                    log.error("消费者发生错误；{}", e);
                    if (consumer != null) {
                        try {
                            consumer.unsubscribe();
                            consumer.close();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }
            }
        });
        //启动线程
        t.start();
    }

    /**
     * 获取所有分区
     *
     * @param consumerConfig
     * @param topicName
     * @return
     */
    public int[] getTopicPartitionIds(KafkaConsumerConfig consumerConfig, String topicName) {
        KafkaProducerConfig producerConfig = new KafkaProducerConfig();
        BeanUtils.copyProperties(consumerConfig, producerConfig);
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerConfig.toProperties())) {
            List<PartitionInfo> partitions = producer.partitionsFor(topicName);
            if (partitions.size() == 0) {
                return null;
            }
            return partitions.stream().mapToInt(p -> p.partition()).toArray();
        }
    }
}
