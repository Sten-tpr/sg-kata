package com.sg.kata.service;

import com.sg.kata.dto.TransactionDto;

import java.util.List;

public interface BankAccountService {
    TransactionDto deposit(int amount);
    TransactionDto withdraw(int amount);
    List<TransactionDto> getStatement();
}
