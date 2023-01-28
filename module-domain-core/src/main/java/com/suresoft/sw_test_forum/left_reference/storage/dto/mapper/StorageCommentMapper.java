package com.suresoft.sw_test_forum.left_reference.storage.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageComment;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StorageCommentMapper extends EntityMapper<StorageCommentDto, StorageComment> {
    StorageCommentMapper INSTANCE = Mappers.getMapper(StorageCommentMapper.class);
}