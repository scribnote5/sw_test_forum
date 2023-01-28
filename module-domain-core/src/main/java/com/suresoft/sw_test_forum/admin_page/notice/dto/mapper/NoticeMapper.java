package com.suresoft.sw_test_forum.admin_page.notice.dto.mapper;

import com.suresoft.sw_test_forum.admin_page.notice.domain.Notice;
import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeAttachedFile;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeCommentDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeMapper extends EntityMapper<NoticeDto, Notice> {
    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    default NoticeDto toDtoByAttachedFileList(NoticeDto noticeDto, List<NoticeAttachedFile> attachedFileList) {
        for (NoticeAttachedFile attachedFile : attachedFileList) {
            noticeDto.getAttachedFileList().add(attachedFile);
        }

        return noticeDto;
    }

    default NoticeDto toDtoByCommentList(NoticeDto noticeDto, List<NoticeCommentDto> commentDtoList) {
        for (NoticeCommentDto noticeCommentDto : commentDtoList) {
            noticeDto.getCommentDtoList().add(noticeCommentDto);
        }

        return noticeDto;
    }
}