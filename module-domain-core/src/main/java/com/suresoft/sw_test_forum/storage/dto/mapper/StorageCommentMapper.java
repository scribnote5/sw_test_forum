package com.suresoft.sw_test_forum.storage.dto.mapper;

import com.suresoft.sw_test_forum.storage.domain.StorageComment;
import com.suresoft.sw_test_forum.storage.dto.StorageCommentDto;
import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StorageCommentMapper extends EntityMapper<StorageCommentDto, StorageComment> {
    StorageCommentMapper INSTANCE = Mappers.getMapper(StorageCommentMapper.class);
}