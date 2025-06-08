package com.example.testtask.mapper;

import com.example.testtask.dto.response.PhoneResponse;
import com.example.testtask.entity.PhoneData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper {
    PhoneResponse toDto(PhoneData phoneData);
}
