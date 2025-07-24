package com.sg.kata.service;

import com.sg.kata.dto.TransactionDto;
import com.sg.kata.mapper.TransactionMapper;
import com.sg.kata.model.Transaction;
import com.sg.kata.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public BankAccountServiceImpl(TransactionRepository repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public TransactionDto deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        int balance = getCurrentBalance();
        Transaction transaction = new Transaction(LocalDate.now(), amount, balance + amount);
        Transaction saved = repository.save(transaction);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public TransactionDto withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        int balance = getCurrentBalance();
        if (balance < amount) {
            throw new IllegalStateException("Insufficient funds.");
        }

        Transaction transaction = new Transaction(LocalDate.now(), -amount, balance - amount);
        Transaction saved = repository.save(transaction);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getStatement() {
        return mapper.toDtos(repository.findAll());
    }

    private int getCurrentBalance() {
        return repository.findAll().stream()
                .mapToInt(Transaction::getBalance)
                .reduce(0, (acc, current) -> current); // prend le dernier solde
    }
}
