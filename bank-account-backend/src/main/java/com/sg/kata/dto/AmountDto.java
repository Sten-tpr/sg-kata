package com.sg.kata.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AmountDto(
        @Schema(description = "Montant de la transaction", example = "1000")
        int amount
) {}
