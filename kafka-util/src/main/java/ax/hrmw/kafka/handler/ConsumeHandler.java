package ax.hrmw.kafka.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * 消费处理抽象类，由实现类具体处理
 *
 * @author: ly
 * @date: 2020/3/18 10:48
 */
public interface ConsumeHandler {
    void beforeConsume();

    void consumeSingle(ConsumerRecord<String, String> record);

    void consumerMultiple(ConsumerRecords<String, String> records);
}