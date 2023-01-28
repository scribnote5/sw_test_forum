package com.suresoft.sw_test_forum.admin_page.notice.dto.mapper;

import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeComment;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeCommentDto;
import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NoticeCommentMapper extends EntityMapper<NoticeCommentDto, NoticeComment> {
    NoticeCommentMapper INSTANCE = Mappers.getMapper(NoticeCommentMapper.class);
}