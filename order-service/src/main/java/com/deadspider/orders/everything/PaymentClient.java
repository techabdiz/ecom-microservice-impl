package com.deadspider.orders.everything;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "payment-service", path = "payments")
public interface PaymentClient {

    @GetMapping("{id}")
    String intiatePayment(@RequestParam(required = true) String action, @PathVariable Long id);

}
