package com.example.demo.checkout.config;

import com.example.demo.checkout.pojo.MaxwellKey;
import com.example.demo.checkout.pojo.MaxwellValue;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.List;

@Configuration
public class KafkaConfig {

    @Bean
    public ReceiverOptions<MaxwellKey, MaxwellValue>
    receiverOptions(@Value(value = "${kafka.topics}") List<String> topics,
                    @Value(value = "${kafka.bootstrap-servers}") List<String> bootstrapServers) {

        final var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "checkout");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "checkout");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, "com.example.demo.checkout.pojo.MaxwellKey");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.demo.checkout.pojo.MaxwellValue");
        return ReceiverOptions.<MaxwellKey, MaxwellValue>create(props).subscription(topics);
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<MaxwellKey, MaxwellValue>
    reactiveKafkaConsumerTemplate(ReceiverOptions<MaxwellKey, MaxwellValue> receiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }
}
