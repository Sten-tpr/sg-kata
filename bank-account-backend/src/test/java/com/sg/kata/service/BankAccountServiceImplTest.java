package com.sg.kata.service;

import com.sg.kata.dto.TransactionDto;
import com.sg.kata.mapper.TransactionMapper;
import com.sg.kata.model.Transaction;
import com.sg.kata.repository.TransactionRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
        BigDecimal amount = BigDecimal.valueOf(100);
        Transaction transaction = new Transaction(LocalDate.now(), amount, amount);
        transaction.setId(1L);
        TransactionDto dto = new TransactionDto(1L, LocalDate.now(), amount, amount);

        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(any(Transaction.class))).thenReturn(transaction);
        when(mapper.toDto(transaction)).thenReturn(dto);

        TransactionDto result = service.deposit(amount);

        assertNotNull(result);
        assertEquals(0, result.amount().compareTo(amount));
        assertEquals(0, result.balance().compareTo(amount));
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void deposit_shouldFailValidation_whenAmountIsNegative() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            TransactionDto dto = new TransactionDto(
                    1L,
                    LocalDate.now(),
                    BigDecimal.valueOf(-10),
                    BigDecimal.valueOf(100)
            );

            Set<ConstraintViolation<TransactionDto>> violations = validator.validate(dto);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Le montant doit être strictement positif."))
            );
        }
    }

    @Test
    void balance_shouldFailValidation_whenNegative() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            TransactionDto dto = new TransactionDto(
                    1L,
                    LocalDate.now(),
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(-50)
            );

            Set<ConstraintViolation<TransactionDto>> violations = validator.validate(dto);

            assertFalse(violations.isEmpty(), "Une violation était attendue pour un solde négatif");
            assertTrue(violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Le solde ne peut pas être négatif."))
            );
        }
    }

    @Test
    void withdraw_shouldSaveTransactionAndReturnDto() {
        BigDecimal initialBalance = BigDecimal.valueOf(200);
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);
        BigDecimal newBalance = initialBalance.subtract(withdrawAmount);

        Transaction existingTransaction = new Transaction(LocalDate.now(), initialBalance, initialBalance);
        existingTransaction.setId(1L);

        Transaction withdrawalTransaction = new Transaction(LocalDate.now(), withdrawAmount.negate(), newBalance);
        withdrawalTransaction.setId(2L);

        TransactionDto withdrawalDto = new TransactionDto(2L, LocalDate.now(), withdrawAmount.negate(), newBalance);

        when(repository.findAll()).thenReturn(List.of(existingTransaction));
        when(repository.save(any(Transaction.class))).thenReturn(withdrawalTransaction);
        when(mapper.toDto(withdrawalTransaction)).thenReturn(withdrawalDto);

        TransactionDto result = service.withdraw(withdrawAmount);

        assertNotNull(result);
        assertEquals(0, result.amount().compareTo(withdrawAmount.negate()));
        assertEquals(0, result.balance().compareTo(newBalance));
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void getStatement_shouldReturnAllTransactionsAsDto() {
        Transaction t1 = new Transaction(LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100));
        t1.setId(1L);
        Transaction t2 = new Transaction(LocalDate.now(), BigDecimal.valueOf(-50), BigDecimal.valueOf(50));
        t2.setId(2L);

        TransactionDto dto1 = new TransactionDto(1L, LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100));
        TransactionDto dto2 = new TransactionDto(2L, LocalDate.now(), BigDecimal.valueOf(-50), BigDecimal.valueOf(50));

        when(repository.findAll()).thenReturn(List.of(t1, t2));
        when(mapper.toDtos(anyList())).thenReturn(List.of(dto1, dto2));

        List<TransactionDto> result = service.getStatement();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}