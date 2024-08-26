package com.example.demo.checkout.config;

import com.example.demo.checkout.pojo.StockAndStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisOperations<String, StockAndStatus>
    redisOps(final ReactiveRedisConnectionFactory connectionFactory) {
        final var serializer = new Jackson2JsonRedisSerializer<>(StockAndStatus.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, StockAndStatus> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        final var context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
