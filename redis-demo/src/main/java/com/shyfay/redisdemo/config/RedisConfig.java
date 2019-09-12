package com.shyfay.redisdemo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

/**
 * Redis配置类
 * RedisTemplate可以采用几种序列化方式
 * JdkSerializationRedisSerializer序列化时间：8ms,序列化后的长度：1325
 * JdkSerializationRedisSerializer反序列化时间：4
 * StringRedisSerializer
 * GenericJackson2JsonRedisSerializer序列化时间：52ms,序列化后的长度：17425
 * GenericJackson2JsonRedisSerializer反序列化时间：60
 * Jackson2JsonRedisSerializer序列化时间：4ms,序列化后的长度：9801
 * Jackson2JsonRedisSerializer反序列化时间：4
 * RedisTemplate默认采用的是JdkSerializationRedisSerializer序列化方式
 * StringRedisTemplate默认采用的是StringRedisSerializer序列化方式
 * 一般都推荐分开设置，例如类型是字符串键值对，就让key和value都使用StringRedisSerializer
 * 如果是哈希键，那key就是用StringRedisSerializer序列化而value采用Jackson2JsonRedisSerializer序列化
 * @author mx
 * @since 2019/9/11
 */

@Configuration
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

//        template.setKeySerializer(stringSerializer);
//        template.setValueSerializer(stringSerializer);
//        template.setHashKeySerializer(stringSerializer);
//        template.setHashValueSerializer(stringSerializer);
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
