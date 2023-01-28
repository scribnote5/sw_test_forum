package com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MisraCCommentMapper extends EntityMapper<MisraCCommentDto, MisraCComment> {
    MisraCCommentMapper INSTANCE = Mappers.getMapper(MisraCCommentMapper.class);
}