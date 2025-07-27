package com.deadspider.payments.client;

import com.deadspider.payments.models.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service" , url = "http://localhost:9001/orders")
public interface OrderClient {


    @GetMapping("{id}")
    OrderResponse byId(@PathVariable Long id);

}
