package com.sg.kata.dto;

import java.time.LocalDate;

public record TransactionDto(
        Long id,
        LocalDate date,
        int amount,
        int balance
) {}
