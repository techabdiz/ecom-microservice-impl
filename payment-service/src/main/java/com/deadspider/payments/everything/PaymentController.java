package com.deadspider.payments.everything;

import brave.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("payments")
public class PaymentController {

    private PaymentsService service;


    @PostMapping
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody  PaymentRequest req) throws JsonProcessingException {
        return ResponseEntity.ok(service.pay(req));
    }

    @GetMapping("{id}")
    public String intiatePayment(@RequestParam(required = true) String action, @PathVariable Long id, @RequestHeader Map<String, String> headers) throws JsonProcessingException {
        System.out.println("headers : " + headers);
        service.intiPayment(id);
        return "SUCCESS";
    }
}
