package com.sg.kata.service;

import com.sg.kata.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountService {
    TransactionDto deposit(BigDecimal amount);
    TransactionDto withdraw(BigDecimal amount);
    List<TransactionDto> getStatement();
}
