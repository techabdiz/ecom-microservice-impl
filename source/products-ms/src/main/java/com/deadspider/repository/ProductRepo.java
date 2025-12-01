package com.deadspider.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.deadspider.models.Product;

public interface ProductRepo extends MongoRepository<Product, String>{

}
