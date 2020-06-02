package ax.hrmw.kafka.model;

import lombok.Data;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.SaslConfigs;

import java.util.Properties;

/**
 * 生产者配置类
 *
 * @author: ly
 * @date: 2020/3/18 10:47
 */
@Data
public class KafkaProducerConfig {
    //kafka消息序列化
    private static final String SERIALIZER_STRING = "org.apache.kafka.common.serialization.StringSerializer";

    private String bootstrapServers;

    private String topicName;

    private String acksConfig = "1";

    private Integer retriesConfig = 3;

    private String keySerializerClassName = SERIALIZER_STRING;

    private String valueSerializerClassName = SERIALIZER_STRING;

    private String securityProtocol;

    private String saslMechanism;

    private String saslKerberosServiceName;

    public Properties toProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", this.getBootstrapServers());
        props.put("value.serializer", this.getValueSerializerClassName());
        props.put("key.serializer", this.getKeySerializerClassName());
        props.put("retries", this.getRetriesConfig());
        props.put("acks", this.getAcksConfig());
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
