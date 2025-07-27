package com.deadspider.controllers;

import com.deadspider.models.Account;
import com.deadspider.models.dtos.AccountCreateRequest;
import com.deadspider.models.dtos.ApiResponse;
import com.deadspider.models.dtos.TransactionRequest;
import com.deadspider.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/shmuck")
public class ShmuckController {

    private AccountService service;

    @PostMapping("account/create")
    public ResponseEntity<ApiResponse> create(@RequestBody  AccountCreateRequest req) {
        Account acc = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .message(acc.getAccno())
                        .code(HttpStatus.CREATED.value())
                        .build());
    }

    @GetMapping("account")
    public ResponseEntity<List<Account>>  getAccounts() {
        return ResponseEntity
                .ok(service.listAllAccounts());
    }

    @GetMapping("account/{accno}")
    public ResponseEntity<Account>  getAccounts(@PathVariable String accno) {
        return service.accountByNumber(accno)
                    .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("transact")
    public ResponseEntity<ApiResponse> transact(@RequestBody TransactionRequest req) {
        String ref =  service.transact(req);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message(ref)
                        .code(HttpStatus.OK.value())
                        .build()
        );
    }


}
