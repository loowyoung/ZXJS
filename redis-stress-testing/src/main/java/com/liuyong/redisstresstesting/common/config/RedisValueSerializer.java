package com.liuyong.redisstresstesting.common.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 自定义redis-value序列化功能类
 *
 * @author xw
 */
public class RedisValueSerializer implements RedisSerializer<Object> {
    private static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 将各类型value序列化为byte[]
     *
     * @param obj
     * @return
     * @throws SerializationException
     */
    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        if (obj == null) {
            return EMPTY_ARRAY;
        }

        String value = null;

        Class<?> valueClass = obj.getClass();

        if (valueClass.equals(String.class)) {
            value = obj.toString();
        } else if (valueClass.equals(LocalDateTime.class)) {
            long epochMilli = ((LocalDateTime) obj).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            value = String.valueOf(epochMilli);
        } else if (valueClass.equals(LocalDate.class)) {
            long epochMilli = ((LocalDate) obj).atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
            value = String.valueOf(epochMilli);
        } else {
            value = obj.toString();
        }

        return value.getBytes(StandardCharsets.UTF_8);

    }

    /**
     * 将byte[]反序列化为String
     *
     * @param bytes
     * @return
     * @throws SerializationException
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes.length == 0) {
            return null;
        }

        String data = new String(bytes, StandardCharsets.UTF_8);
        return data;
    }

}
