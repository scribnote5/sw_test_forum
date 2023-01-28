package com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraC;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCAttachedFile;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MisraCMapper extends EntityMapper<MisraCDto, MisraC> {
    MisraCMapper INSTANCE = Mappers.getMapper(MisraCMapper.class);

    default MisraCDto toDtoByExample(MisraCDto misraCDto, List<MisraCExampleDto> misraCExampleDtoList) {
        for (MisraCExampleDto misraCExampleDto : misraCExampleDtoList) {
            misraCDto.getMisraCExampleDtoList().add(misraCExampleDto);
        }

        return misraCDto;
    }

    default MisraCDto toDtoByGuideline(MisraCDto misraCDto, List<MisraCGuidelineDto> misraCGuidelineDtoList) {
        for (MisraCGuidelineDto misraCGuidelineDto : misraCGuidelineDtoList) {
            misraCDto.getMisraCGuidelineDtoList().add(misraCGuidelineDto);
        }

        return misraCDto;
    }

    default MisraCDto toDtoByAttachedFileList(MisraCDto misraCDto, List<MisraCAttachedFile> attachedFileList) {
        for (MisraCAttachedFile attachedFile : attachedFileList) {
            misraCDto.getAttachedFileList().add(attachedFile);
        }

        return misraCDto;
    }

    default MisraCDto toDtoByCommentList(MisraCDto misraCDto, List<MisraCCommentDto> commentDtoList) {
        for (MisraCCommentDto misraCCommentDto : commentDtoList) {
            misraCDto.getCommentDtoList().add(misraCCommentDto);
        }

        return misraCDto;
    }
}