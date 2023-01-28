package com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCop;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopAttachedFile;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FxCopMapper extends EntityMapper<FxCopDto, FxCop> {
    FxCopMapper INSTANCE = Mappers.getMapper(FxCopMapper.class);

    default FxCopDto toDtoByExample(FxCopDto fxCopDto, List<FxCopExampleDto> fxCopExampleDtoList) {
        for (FxCopExampleDto fxCopExampleDto : fxCopExampleDtoList) {
            fxCopDto.getFxCopExampleDtoList().add(fxCopExampleDto);
        }

        return fxCopDto;
    }

    default FxCopDto toDtoByGuideline(FxCopDto fxCopDto, List<FxCopGuidelineDto> fxCopGuidelineDtoList) {
        for (FxCopGuidelineDto fxCopGuidelineDto : fxCopGuidelineDtoList) {
            fxCopDto.getFxCopGuidelineDtoList().add(fxCopGuidelineDto);
        }

        return fxCopDto;
    }

    default FxCopDto toDtoByAttachedFileList(FxCopDto fxCopDto, List<FxCopAttachedFile> attachedFileList) {
        for (FxCopAttachedFile attachedFile : attachedFileList) {
            fxCopDto.getAttachedFileList().add(attachedFile);
        }

        return fxCopDto;
    }

    default FxCopDto toDtoByCommentList(FxCopDto fxCopDto, List<FxCopCommentDto> commentDtoList) {
        for (FxCopCommentDto fxCopCommentDto : commentDtoList) {
            fxCopDto.getCommentDtoList().add(fxCopCommentDto);
        }

        return fxCopDto;
    }
}