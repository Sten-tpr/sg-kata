package com.sg.kata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.kata.config.TestSecurityConfig;
import com.sg.kata.dto.TransactionDto;
import com.sg.kata.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(BankAccountController.class)
@Import(TestSecurityConfig.class)
class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountService service;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public BankAccountService bankAccountService() {
            return Mockito.mock(BankAccountService.class);
        }
    }

    @Test
    @WithMockUser
    void deposit_shouldReturnCreatedTransaction() throws Exception {
        TransactionDto dto = new TransactionDto(1L, LocalDate.now(), BigDecimal.valueOf(100), BigDecimal.valueOf(100));
        when(service.deposit(any(BigDecimal.class))).thenReturn(dto);

        mockMvc.perform(post("/api/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 100))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}