package com.suresoft.sw_test_forum.left_reference.storage.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.left_reference.storage.domain.Storage;
import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageAttachedFile;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageCommentDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StorageMapper extends EntityMapper<StorageDto, Storage> {
    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    default StorageDto toDtoByAttachedFileList(StorageDto storageDto, List<StorageAttachedFile> attachedFileList) {
        for (StorageAttachedFile attachedFile : attachedFileList) {
            storageDto.getAttachedFileList().add(attachedFile);
        }

        return storageDto;
    }

    default StorageDto toDtoByCommentList(StorageDto storageDto, List<StorageCommentDto> commentDtoList) {
        for (StorageCommentDto storageCommentDto : commentDtoList) {
            storageDto.getCommentDtoList().add(storageCommentDto);
        }

        return storageDto;
    }
}