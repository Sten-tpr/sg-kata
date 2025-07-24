package com.sg.kata.service;

import com.sg.kata.dto.TransactionDto;
import com.sg.kata.mapper.TransactionMapper;
import com.sg.kata.model.Transaction;
import com.sg.kata.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BankAccountServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private BankAccountServiceImpl service;

    private AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void deposit_shouldSaveTransactionAndReturnDto() {
        int amount = 100;
        Transaction transaction = new Transaction(LocalDate.now(), amount, amount);
        transaction.setId(1L);
        TransactionDto dto = new TransactionDto(1L, LocalDate.now(), amount, amount);

        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(any(Transaction.class))).thenReturn(transaction);
        when(mapper.toDto(transaction)).thenReturn(dto);

        TransactionDto result = service.deposit(amount);

        assertNotNull(result);
        assertEquals(amount, result.amount());
        assertEquals(amount, result.balance());
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void deposit_shouldThrowException_whenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.deposit(-10));
    }

    @Test
    void withdraw_shouldSaveTransactionAndReturnDto() {
        int initialBalance = 200;
        int withdrawAmount = 50;
        int newBalance = initialBalance - withdrawAmount;

        Transaction existingTransaction = new Transaction(LocalDate.now(), 200, initialBalance);
        existingTransaction.setId(1L);

        Transaction withdrawalTransaction = new Transaction(LocalDate.now(), -withdrawAmount, newBalance);
        withdrawalTransaction.setId(2L);

        TransactionDto withdrawalDto = new TransactionDto(2L, LocalDate.now(), -withdrawAmount, newBalance);

        when(repository.findAll()).thenReturn(List.of(existingTransaction));
        when(repository.save(any(Transaction.class))).thenReturn(withdrawalTransaction);
        when(mapper.toDto(withdrawalTransaction)).thenReturn(withdrawalDto);

        TransactionDto result = service.withdraw(withdrawAmount);

        assertNotNull(result);
        assertEquals(-withdrawAmount, result.amount());
        assertEquals(newBalance, result.balance());
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void withdraw_shouldThrowException_whenInsufficientFunds() {
        when(repository.findAll()).thenReturn(List.of()); // solde = 0

        assertThrows(IllegalStateException.class, () -> service.withdraw(50));
    }

    @Test
    void getStatement_shouldReturnAllTransactionsAsDto() {
        Transaction t1 = new Transaction(LocalDate.now(), 100, 100);
        t1.setId(1L);
        Transaction t2 = new Transaction(LocalDate.now(), -50, 50);
        t2.setId(2L);

        TransactionDto dto1 = new TransactionDto(1L, LocalDate.now(), 100, 100);
        TransactionDto dto2 = new TransactionDto(2L, LocalDate.now(), -50, 50);

        when(repository.findAll()).thenReturn(List.of(t1, t2));
        when(mapper.toDtos(anyList())).thenReturn(List.of(dto1, dto2));

        List<TransactionDto> result = service.getStatement();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}