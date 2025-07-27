package com.deadspider.services;


import com.deadspider.models.Account;
import com.deadspider.models.Transaction;
import com.deadspider.models.TransactionType;
import com.deadspider.models.dtos.AccountCreateRequest;
import com.deadspider.models.dtos.TransactionRequest;
import com.deadspider.repos.AccountRepo;
import com.deadspider.repos.TransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepo repo;
    private TransactionRepo trepo;

    public Account create(AccountCreateRequest req) {
        Account acc = Account.builder()
                .accno(UUID.randomUUID().toString())
                .currentBalance(0.0)
                .creationDate(Instant.now())
                .build();
        BeanUtils.copyProperties(req, acc);
        repo.save(acc);
        return acc;
    }


    public List<Account> listAllAccounts() {
        return repo.findAll();
    }

    public Optional<Account> accountByNumber(String accno) {
        return repo.findByAccno(accno);
    }

    public String transact(TransactionRequest req) {
        Account account = repo.findByAccno(req.getAccno())
                .orElseThrow(() -> new IllegalArgumentException("invalid account number"));

        if(req.getType() == null ) {
            throw new IllegalArgumentException("invalid transaction type");
        }

        if(req.getType() == TransactionType.DEBIT && account.getCurrentBalance() < req.getAmount()) {
            throw new IllegalArgumentException("insufficient balance");
        }

        Transaction trans = Transaction.builder()
                .account(account)
                .refnum(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .build();
        BeanUtils.copyProperties(req, trans);

        trepo.save(trans);

        // this need to be atomic...
        // what if something messes up if service goes down before rest of it executes
        account.setCurrentBalance(
                (req.getType() == TransactionType.DEBIT) ?
                        account.getCurrentBalance() - req.getAmount() :
                        account.getCurrentBalance() + req.getAmount()
        );
        repo.save(account);

        return trans.getRefnum();
    }
}
