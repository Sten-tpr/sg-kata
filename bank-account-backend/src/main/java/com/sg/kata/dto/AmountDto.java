package com.sg.kata.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AmountDto(
        @Schema(description = "Montant de la transaction", example = "1000")
        @PositiveOrZero(message = "Le montant doit être strictement positif.")
        BigDecimal amount
) {}
