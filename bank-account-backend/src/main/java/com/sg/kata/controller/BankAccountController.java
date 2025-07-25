package com.sg.kata.controller;

import com.sg.kata.dto.AmountDto;
import com.sg.kata.dto.TransactionDto;
import com.sg.kata.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/account")
public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @Operation(summary = "Déposer de l'argent sur le compte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dépôt effectué",
                    content = @Content(schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "Montant invalide", content = @Content)
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> deposit(@RequestBody AmountDto body) {
        TransactionDto transaction = service.deposit(body.amount());
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Retirer de l'argent du compte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrait effectué",
                    content = @Content(schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "400", description = "Montant invalide ou solde insuffisant", content = @Content)
    })
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@RequestBody AmountDto body) {
        TransactionDto transaction = service.withdraw(body.amount());
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Afficher l'historique des opérations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des transactions",
                    content = @Content(schema = @Schema(implementation = TransactionDto.class)))
    })
    @GetMapping("/statement")
    public ResponseEntity<List<TransactionDto>> statement() {
        System.out.println("statement called");
        List<TransactionDto> list = service.getStatement();
        return ResponseEntity.ok(list);
    }
}