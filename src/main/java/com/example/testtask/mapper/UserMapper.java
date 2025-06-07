package com.example.testtask.mapper;

import com.example.testtask.dto.request.UserUpdateRequest;
import com.example.testtask.dto.response.UserResponse;
import com.example.testtask.entity.EmailData;
import com.example.testtask.entity.PhoneData;
import com.example.testtask.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;



import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "emails", expression = "java(mapEmails(user.getEmails()))")
    @Mapping(target = "phones", expression = "java(mapPhones(user.getPhones()))")
    @Mapping(target = "balance", expression = "java(user.getAccount() != null ? user.getAccount().getBalance().toPlainString() : null)")
    UserResponse toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateRequest request, @MappingTarget User user);

    default List<String> mapEmails(List<EmailData> emailDataList) {
        return emailDataList.stream().map(EmailData::getEmail).collect(Collectors.toList());
    }

    default List<String> mapPhones(List<PhoneData> phoneDataList) {
        return phoneDataList.stream().map(PhoneData::getPhone).collect(Collectors.toList());
    }
}
