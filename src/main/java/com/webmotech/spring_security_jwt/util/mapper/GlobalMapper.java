package com.webmotech.spring_security_jwt.util.mapper;

import com.webmotech.spring_security_jwt.dto.UserDTO;
import com.webmotech.spring_security_jwt.model.UserEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface GlobalMapper {

    UserDTO toUserDTO(UserEntity user);
    UserEntity toUser(UserDTO userDTO);

}
