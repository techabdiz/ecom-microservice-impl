package com.deadspider.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deadspider.models.Product;
import com.deadspider.services.ProductsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/product")
@AllArgsConstructor
public class ProductsController {

    private ProductsService service;


    @PostMapping
    public ResponseEntity<Map<String, String>> create(
        @RequestBody Product product) { 
        service.createProduct(product);
        return ResponseEntity
            .created(URI.create(product.getId())).body(Map.of("message","product has been registered"));
            
    }

    @GetMapping
    public ResponseEntity<List<Product>> get() { 
        return ResponseEntity.ok(service.getAllProducts());
    }

}
