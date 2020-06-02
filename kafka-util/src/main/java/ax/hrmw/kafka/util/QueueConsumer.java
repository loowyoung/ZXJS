package ax.hrmw.kafka.util;

import ax.hrmw.kafka.handler.ConsumeHandler;
import ax.hrmw.kafka.model.KafkaConsumerConfig;
import ax.hrmw.kafka.model.KafkaProducerConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 消息消费者
 *
 * @author ly
 * @date 2020年 05月14日 15:16:15
 */
@Slf4j
@Data
public class QueueConsumer {
    private KafkaConsumerConfig config;//消息消费者配置

    /**
     * 构造方法，传入消费者配置
     *
     * @param config
     */
    public QueueConsumer(KafkaConsumerConfig config) {
        this.config = config;
    }

    /**
     * kafka消息消费
     *
     * @param handler 消费者处理类
     */
    public void consume(ConsumeHandler handler) {
        String topicName = config.getTopicName();
        //获取所有分区
        List<Integer> partitionIds = getTopicPartitionIds(topicName);
        partitionIds.forEach(partitionId -> {
            //开线程，监听消费
            Thread t = new Thread(() -> {
                doConsume(handler, partitionId);
                return;
            });
            t.start();
        });

    }

    private void doConsume(ConsumeHandler handler, Integer partitionId) {
        Consumer consumer = createConsumer(partitionId);
        //开线程，监听消费
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
                log.error("消费者消费时发生错误:", e);
                if (consumer != null) {
                    try {
                        consumer.unsubscribe();
                        consumer.close();
                        consumer = null;
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            } finally {
                if (null == consumer) {
                    log.info("consumer 开始初始化");
                    consumer = createConsumer(partitionId);
                    log.info("consumer 初始化完成");
                }
            }
        }
    }

    /**
     * 初始化消费者
     *
     * @return
     */
    private Consumer<String, String> createConsumer(Integer partitionId) {
        String topicName = config.getTopicName();
        //消息消费者
        Consumer<String, String> consumer = null;
        if (partitionId == null) {
            log.info("创建kafka消费者，topic：{}", topicName);
            consumer = new KafkaConsumer<>(config.toProperties());
            consumer.subscribe(Collections.singletonList(topicName));
        } else {
            log.info("创建kafka消费者，topic：{}，partitionId：{}", topicName, partitionId);
            consumer = new KafkaConsumer<>(config.toProperties());
            TopicPartition topicPartition = new TopicPartition(topicName, partitionId);
            consumer.assign(Collections.singletonList(topicPartition));
        }
        return consumer;
    }

    /**
     * 获取所有分区
     *
     * @param topicName
     * @return
     */
    public List<Integer> getTopicPartitionIds(String topicName) {
        List<Integer> result = new ArrayList<>();

        KafkaProducerConfig producerConfig = new KafkaProducerConfig();
        producerConfig.setBootstrapServers(config.getBootstrapServers());
        producerConfig.setTopicName(topicName);
        if (null != config.getSecurityProtocol()) {
            producerConfig.setSecurityProtocol(config.getSecurityProtocol());
        }
        if (null != config.getSaslMechanism()) {
            producerConfig.setSaslMechanism(config.getSaslMechanism());
        }
        if (null != config.getSaslKerberosServiceName()) {
            producerConfig.setSaslKerberosServiceName(config.getSaslKerberosServiceName());
        }
        log.debug("kafka消费者获取分区数：{}", topicName);
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerConfig.toProperties())) {
            List<PartitionInfo> partitions = producer.partitionsFor(topicName);
            if (partitions.size() == 0) {
                result.add(null);
            } else {
                partitions.forEach(partitionInfo -> {
                    result.add(partitionInfo.partition());
                });
            }
            return result;
        }
    }

}
