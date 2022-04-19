package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuideline;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineAttachedFile;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FxCopGuidelineMapper extends EntityMapper<FxCopGuidelineDto, FxCopGuideline> {
    FxCopGuidelineMapper INSTANCE = Mappers.getMapper(FxCopGuidelineMapper.class);

    default FxCopGuidelineDto toDtoByAttachedFileList(FxCopGuidelineDto fxCopGuidelineDto, List<FxCopGuidelineAttachedFile> attachedFileList) {
        for (FxCopGuidelineAttachedFile attachedFile : attachedFileList) {
            fxCopGuidelineDto.getAttachedFileList().add(attachedFile);
        }

        return fxCopGuidelineDto;
    }

    default FxCopGuidelineDto toDtoByCommentList(FxCopGuidelineDto fxCopGuidelineDto, List<FxCopGuidelineCommentDto> commentDtoList) {
        for (FxCopGuidelineCommentDto fxCopGuidelineCommentDto : commentDtoList) {
            fxCopGuidelineDto.getCommentDtoList().add(fxCopGuidelineCommentDto);
        }

        return fxCopGuidelineDto;
    }
}