package com.deadspider.orders.everything;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository repo;
    private PaymentClient payservice;

    private static final String ordersTopic = "com.deadspider.orders";

    public Order create (Order order) {
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedDate(Instant.now());
        Order created = repo.save(order);
        payservice.intiatePayment("INIT", order.getId());
        return order;
    }

    public List<Order> listOrders() {
        return repo.findAll();
    }

    public Order getById(Long id) {
        return repo.findById(id).orElseThrow(()->new IllegalArgumentException("invalid order id"));
    }

    @KafkaListener(topics = ordersTopic, groupId = "my-consumer-group")
    public void updateOrderEvents(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                                  @Payload String value) {
        System.out.printf("received event with id %s <--> %s\n", key, value);
        try {
            PaymentRequest req = new ObjectMapper().readValue(value, PaymentRequest.class);
            Order order = repo.findById(req.getOrderId())
                        .orElseThrow(()->new IllegalArgumentException("order not found with id --> " + req.getOrderId()));
            switch (req.getStatus()) {
                case PaymentStatus.PENDING -> order.setStatus(OrderStatus.PAYMENT_PENDING);
                case PaymentStatus.COMPLETED -> order.setStatus(OrderStatus.SUCCESS);
                case PaymentStatus.FAILED -> order.setStatus(OrderStatus.FAILED);
            }
            order.setTransId(key);
            repo.save(order);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
