package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCpp;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppAttachedFile;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MisraCppMapper extends EntityMapper<MisraCppDto, MisraCpp> {
    MisraCppMapper INSTANCE = Mappers.getMapper(MisraCppMapper.class);

    default MisraCppDto toDtoByExample(MisraCppDto misraCppDto, List<MisraCppExampleDto> misraCppExampleDtoList) {
        for (MisraCppExampleDto misraCppExampleDto : misraCppExampleDtoList) {
            misraCppDto.getMisraCppExampleDtoList().add(misraCppExampleDto);
        }

        return misraCppDto;
    }

    default MisraCppDto toDtoByGuideline(MisraCppDto misraCppDto, List<MisraCppGuidelineDto> misraCppGuidelineDtoList) {
        for (MisraCppGuidelineDto misraCppGuidelineDto : misraCppGuidelineDtoList) {
            misraCppDto.getMisraCppGuidelineDtoList().add(misraCppGuidelineDto);
        }

        return misraCppDto;
    }

    default MisraCppDto toDtoByAttachedFileList(MisraCppDto misraCppDto, List<MisraCppAttachedFile> attachedFileList) {
        for (MisraCppAttachedFile attachedFile : attachedFileList) {
            misraCppDto.getAttachedFileList().add(attachedFile);
        }

        return misraCppDto;
    }

    default MisraCppDto toDtoByCommentList(MisraCppDto misraCppDto, List<MisraCppCommentDto> commentDtoList) {
        for (MisraCppCommentDto misraCppCommentDto : commentDtoList) {
            misraCppDto.getCommentDtoList().add(misraCppCommentDto);
        }

        return misraCppDto;
    }
}