package com.suresoft.sw_test_forum.cwe.cwe.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.cwe.cwe.domain.Cwe;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweAttachedFile;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CweMapper extends EntityMapper<CweDto, Cwe> {
    CweMapper INSTANCE = Mappers.getMapper(CweMapper.class);

    default CweDto toDtoByExample(CweDto cweDto, List<CweExampleDto> cweExampleDtoList) {
        for (CweExampleDto cweExampleDto : cweExampleDtoList) {
            cweDto.getCweExampleDtoList().add(cweExampleDto);
        }

        return cweDto;
    }

    default CweDto toDtoByGuideline(CweDto cweDto, List<CweGuidelineDto> cweGuidelineDtoList) {
        for (CweGuidelineDto cweGuidelineDto : cweGuidelineDtoList) {
            cweDto.getCweGuidelineDtoList().add(cweGuidelineDto);
        }

        return cweDto;
    }

    default CweDto toDtoByAttachedFileList(CweDto cweDto, List<CweAttachedFile> attachedFileList) {
        for (CweAttachedFile attachedFile : attachedFileList) {
            cweDto.getAttachedFileList().add(attachedFile);
        }

        return cweDto;
    }

    default CweDto toDtoByCommentList(CweDto cweDto, List<CweCommentDto> commentDtoList) {
        for (CweCommentDto cweCommentDto : commentDtoList) {
            cweDto.getCommentDtoList().add(cweCommentDto);
        }

        return cweDto;
    }
}