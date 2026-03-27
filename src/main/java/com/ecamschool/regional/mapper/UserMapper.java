package com.ecamschool.regional.mapper;

import com.ecamschool.regional.dto.UserDTO;
import com.ecamschool.regional.dto.UserRequestDTO;
import com.ecamschool.regional.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface UserMapper {

    @Named("cleanString")
    static String cleanString(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.replaceAll("^\"+|\"+$", "");

        cleaned = cleaned.replace("\\\"", "\"");
        return cleaned;
    }

    @Named("stringToRole")
    static UserDTO.RoleEnum stringToRole(String role) {
        if (role == null) {
            return null;
        }
        String cleanRole = cleanString(role);
        for (UserDTO.RoleEnum r : UserDTO.RoleEnum.values()) {
            if (r.getValue().equals(cleanRole)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "role", target = "role", qualifiedByName = "stringToRole")
    @Mapping(source = "username", target = "username", qualifiedByName = "cleanString")
    @Mapping(source = "email", target = "email", qualifiedByName = "cleanString")
    @Mapping(source = "fullName", target = "fullName", qualifiedByName = "cleanString")
    @Mapping(source = "delegationRegion", target = "delegationRegion", qualifiedByName = "cleanString")
    @Mapping(source = "department", target = "department", qualifiedByName = "cleanString")
    UserDTO toDto(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "dataMatrices", ignore = true)
    @Mapping(target = "role", source = "role")
    User toEntity(UserRequestDTO request);
}