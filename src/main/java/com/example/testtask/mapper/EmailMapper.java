package com.example.testtask.mapper;

import com.example.testtask.dto.response.EmailResponse;
import com.example.testtask.entity.EmailData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    EmailResponse toDto(EmailData emailData);
}
