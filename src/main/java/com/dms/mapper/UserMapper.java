package com.dms.mapper;

import com.dms.dto.UserDTO;
import com.dms.entity.mysql.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
    User toEntity(UserDTO dto);
}
