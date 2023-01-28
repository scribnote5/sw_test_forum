package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuideline;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineAttachedFile;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MisraCGuidelineMapper extends EntityMapper<MisraCGuidelineDto, MisraCGuideline> {
    MisraCGuidelineMapper INSTANCE = Mappers.getMapper(MisraCGuidelineMapper.class);

    default MisraCGuidelineDto toDtoByAttachedFileList(MisraCGuidelineDto misraCGuidelineDto, List<MisraCGuidelineAttachedFile> attachedFileList) {
        for (MisraCGuidelineAttachedFile attachedFile : attachedFileList) {
            misraCGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return misraCGuidelineDto;
    }

    default MisraCGuidelineDto toDtoByCommentList(MisraCGuidelineDto misraCGuidelineDto, List<MisraCGuidelineCommentDto> commentDtoList) {
        for (MisraCGuidelineCommentDto misraCGuidelineCommentDto : commentDtoList) {
            misraCGuidelineDto.getCommentDtoList().add(misraCGuidelineCommentDto);
        }

        return misraCGuidelineDto;
    }
}