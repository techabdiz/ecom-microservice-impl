package com.deadspider.inventory_ms.events.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.deadspider.events.ProductPostedEvent;
import com.deadspider.inventory_ms.exceptions.NotRetryableException;
import com.deadspider.inventory_ms.exceptions.RetryableException;


@Component
@KafkaListener(topics="com.deadspider.products.topic",containerFactory = "custom-kafka-container")
public class ProductEventsListener {

    private final Logger LOGGER = LoggerFactory.getLogger(ProductEventsListener.class);

    @KafkaHandler
    public void handlePosted(ProductPostedEvent event) { 

        if(event.getProductPrice() == null) { 
            throw new NotRetryableException("product price is null");
        }

        if (event.getProductName() == null) { 
            throw new RetryableException("why the name null ?");
        }

        LOGGER.info("recieved product posted event: " + event);
    }
}
