package com.sg.kata.mapper;

import com.sg.kata.dto.TransactionDto;
import com.sg.kata.model.Transaction;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDto toDto(Transaction transaction);
    List<TransactionDto> toDtos(List<Transaction> transactions);
}
