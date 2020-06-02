package ax.hrmw.kafka.model;

import lombok.Data;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.Properties;

/**
 * 消费者配置类
 *
 * @author: ly
 * @date: 2020/3/18 10:47
 */
@Data
public class KafkaConsumerConfig {
    //kafka消息反序列化
    private static final String DESERIALIZER_STRING = "org.apache.kafka.common.serialization.StringDeserializer";
    //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
    private static final String AUTO_OFFSET_RESET_EARLIEST = "earliest";
    //当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
    private static final String AUTO_OFFSET_RESET_LATEST = "latest";
    //当各分区下有已提交的offset时，从提交的offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
    private static final String AUTO_OFFSET_RESET_NONE = "none";

    private String bootstrapServers;

    private String groupName;

    private String topicName;

    //设置是否为自动提交
    private Boolean autoCommit = false;

    //偏移量
    private String autoOffsetReset = AUTO_OFFSET_RESET_LATEST;

    //最大拉取条数
    private Integer maxPollRecords = 20;

    //两次拉取间隔时间
    private Integer maxPollIntervalMills = 5000;

    //拉取超时时间
    private Integer pollTimeoutMills = 2000;

    private String keyDeserializerClassName = DESERIALIZER_STRING;

    private String valueDeserializerClassName = DESERIALIZER_STRING;

    private String securityProtocol;

    private String saslMechanism;

    private String saslKerberosServiceName;

    public Properties toProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupName);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.autoCommit);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializerClassName);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializerClassName);
        if (null != this.securityProtocol) {
            props.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, this.securityProtocol);
        }
        if (null != this.saslMechanism) {
            props.put(SaslConfigs.SASL_MECHANISM, this.saslMechanism);
        }
        if (null != this.saslKerberosServiceName) {
            props.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, this.saslKerberosServiceName);
        }
        return props;
    }

}
