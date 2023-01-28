package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuideline;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineAttachedFile;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MisraCppGuidelineMapper extends EntityMapper<MisraCppGuidelineDto, MisraCppGuideline> {
    MisraCppGuidelineMapper INSTANCE = Mappers.getMapper(MisraCppGuidelineMapper.class);

    default MisraCppGuidelineDto toDtoByAttachedFileList(MisraCppGuidelineDto misraCppGuidelineDto, List<MisraCppGuidelineAttachedFile> attachedFileList) {
        for (MisraCppGuidelineAttachedFile attachedFile : attachedFileList) {
            misraCppGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return misraCppGuidelineDto;
    }

    default MisraCppGuidelineDto toDtoByCommentList(MisraCppGuidelineDto misraCppGuidelineDto, List<MisraCppGuidelineCommentDto> commentDtoList) {
        for (MisraCppGuidelineCommentDto misraCppGuidelineCommentDto : commentDtoList) {
            misraCppGuidelineDto.getCommentDtoList().add(misraCppGuidelineCommentDto);
        }

        return misraCppGuidelineDto;
    }
}