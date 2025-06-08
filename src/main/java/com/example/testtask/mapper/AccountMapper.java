package com.example.testtask.mapper;

import com.example.testtask.dto.response.AccountResponse;
import com.example.testtask.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toDto(Account account);
}
