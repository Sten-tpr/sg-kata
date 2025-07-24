package com.sg.kata.controller;

import com.sg.kata.dto.TransactionDto;
import com.sg.kata.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> deposit(@RequestBody Map<String, Integer> body) {
        TransactionDto transaction = service.deposit(body.get("amount"));
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@RequestBody Map<String, Integer> body) {
        TransactionDto transaction = service.withdraw(body.get("amount"));
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/statement")
    public ResponseEntity<List<TransactionDto>> statement() {
        System.out.println("statement called");
        List<TransactionDto> list = service.getStatement();
        return ResponseEntity.ok(list);
    }
}