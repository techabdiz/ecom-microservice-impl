package com.deadspider.orders.everything;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("orders")
public class OrderController {

    private OrderService service;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAll() {
        return ResponseEntity.ok(service.listOrders());
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> byId(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
