package com.deadspider.payments.client;

import com.deadspider.payments.everything.TransferRequest;
import com.deadspider.payments.models.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shmuck-bank" , url = "http://localhost:9000/api/shmuck")
public interface ShmuckClient {

    @PostMapping("transact")
    ApiResponse makePayment(@RequestBody TransferRequest req);
}
