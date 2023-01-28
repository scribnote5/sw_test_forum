package com.suresoft.sw_test_forum.admin_page.user.dto.mapper;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.UserAttachedFile;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserDto;
import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default UserDto toDtoByAttachedFileList(UserDto userDto, List<UserAttachedFile> attachedFileList) {
        for (UserAttachedFile attachedFile : attachedFileList) {
            userDto.getAttachedFileList().add(attachedFile);
        }

        return userDto;
    }
}