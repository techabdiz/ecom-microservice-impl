package com.deadspider.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.deadspider.events.ProductPostedEvent;

@Configuration
public class KafkaConfig {

    Map<String,Object> producerConfig() { 
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            List.of("192.168.1.5:9092", "192.168.1.5:9093", "192.168.1.5:9094"));
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);

        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        //configs.put(ProducerConfig.RETRIES_CONFIG, 1);
        return configs;
    }

    @Bean
    ProducerFactory<String, ProductPostedEvent> pcEvent() { 
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    KafkaTemplate<String, ProductPostedEvent> pcTemplate(ProducerFactory<String, ProductPostedEvent> factory) { 
        return new KafkaTemplate<>(factory);
    }

    @Bean
    NewTopic productsTopic() { 
        return TopicBuilder
                .name("com.deadspider.products.topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
            .build();
    }

}
