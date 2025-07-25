package com.sg.kata.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Représente une transaction bancaire avec détails")
public record TransactionDto(
        @Schema(description = "Identifiant unique de la transaction", example = "12345")
        Long id,

        @Schema(description = "Date de la transaction", example = "2025-07-25")
        LocalDate date,

        @Schema(description = "Montant de la transaction", example = "1500")
        int amount,

        @Schema(description = "Solde après la transaction", example = "5000")
        int balance
) {}
