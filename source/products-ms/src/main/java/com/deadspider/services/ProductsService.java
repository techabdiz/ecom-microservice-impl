package com.deadspider.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.deadspider.events.ProductPostedEvent;
import com.deadspider.models.Product;
import com.deadspider.repository.ProductRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsService.class);
    
    private KafkaTemplate<String,ProductPostedEvent> template;
    private ProductRepo repo;

    public void createProduct(Product p) { 
        p = repo.save(p);

        ProductPostedEvent event =  ProductPostedEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .productId(p.getId())
            .productName(p.getName())
            .productPrice(p.getPrice())
        .build();

        CompletableFuture<SendResult<String, ProductPostedEvent>> kafkaResponse =
          template.send("com.deadspider.products.topic", event.getEventId(), event);
        
        kafkaResponse.whenComplete((res, e)->{
            if (e != null) {
                LOGGER.error("error publishing event to kafka", e); 
                return;
            }

            LOGGER.info("message sent successfully {}", res);
        });
    }


    @KafkaListener(topics = "com.deadspider.products.topic", groupId = "product-service")
    public void processProductEvents(ProductPostedEvent event) { 
        LOGGER.info("recieved product event: " + event);
    }


    public List<Product> getAllProducts() {
        return repo.findAll();
    }
}
