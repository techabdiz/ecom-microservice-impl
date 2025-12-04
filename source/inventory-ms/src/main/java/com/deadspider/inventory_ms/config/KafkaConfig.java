package com.deadspider.inventory_ms.config;

import java.util.HashMap;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.deadspider.inventory_ms.events.listeners.ProductEventsListener;
import com.deadspider.inventory_ms.exceptions.NotRetryableException;
import com.deadspider.inventory_ms.exceptions.RetryableException;


@Configuration
public class KafkaConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductEventsListener.class);

    @Bean
    ConsumerFactory<String, Object> consumerFactory() { 
        LOGGER.info("we are initialized");
        HashMap<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            List.of("localhost:9092", "localhost:9093", "localhost:9094"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-ms");
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.deadspider.events");
        return new DefaultKafkaConsumerFactory<String,Object>(config) ;
    }

    @Bean(name = "custom-kafka-container")
    ConcurrentKafkaListenerContainerFactory<String, Object>
        listenerContainer(ConsumerFactory<String, Object> confact,
            KafkaTemplate<String, Object> template
        ) { 
        var factory =  new ConcurrentKafkaListenerContainerFactory<String, Object>();
        var handler = new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template),
                new FixedBackOff(500, 10));
        handler.addNotRetryableExceptions(NotRetryableException.class);
        handler.addRetryableExceptions(RetryableException.class);
        factory.setConsumerFactory(confact);
        factory.setCommonErrorHandler(handler);
        return factory;
    }
}
