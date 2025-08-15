package com.sg.kata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Représente une transaction bancaire avec détails")
public record TransactionDto(
        @Schema(description = "Identifiant unique de la transaction", example = "12345")
        @NotNull
        Long id,

        @Schema(description = "Date de la transaction", example = "2025-07-25")
        LocalDate date,

        @Schema(description = "Montant de la transaction", example = "1500")
        @PositiveOrZero(message = "Le montant doit être strictement positif.")
        BigDecimal amount,

        @Schema(description = "Solde après la transaction", example = "5000")
        @PositiveOrZero(message = "Le solde ne peut pas être négatif.")
        BigDecimal balance
) {}
