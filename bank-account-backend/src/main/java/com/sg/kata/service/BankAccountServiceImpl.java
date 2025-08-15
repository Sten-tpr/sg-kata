package com.sg.kata.service;

import com.sg.kata.dto.TransactionDto;
import com.sg.kata.mapper.TransactionMapper;
import com.sg.kata.model.Transaction;
import com.sg.kata.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public BankAccountServiceImpl(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TransactionDto deposit(BigDecimal amount) {
        BigDecimal balance = getCurrentBalance();
        return saveTransaction(amount, balance.add(amount));
    }

    @Override
    public TransactionDto withdraw(BigDecimal amount) {
        BigDecimal balance = getCurrentBalance();
        return saveTransaction(amount.negate(), balance.subtract(amount));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getStatement() {
        return mapper.toDtos(repository.findAll());
    }

    @Transactional
    private TransactionDto saveTransaction(BigDecimal amount, BigDecimal balance) {
        Transaction transaction = new Transaction(LocalDate.now(), amount, balance);
        Transaction saved = repository.save(transaction);
        return mapper.toDto(saved);
    }

    private BigDecimal getCurrentBalance() {
        return repository.findAll().stream()
                .map(Transaction::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
