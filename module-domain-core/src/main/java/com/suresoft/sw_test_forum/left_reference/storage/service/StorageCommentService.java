package com.suresoft.sw_test_forum.left_reference.storage.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.storage.repository.StorageCommentRepositoryImpl;
import com.suresoft.sw_test_forum.left_reference.storage.domain.StorageComment;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageCommentDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.mapper.StorageCommentMapper;
import com.suresoft.sw_test_forum.left_reference.storage.dto.mapper.StorageMapper;
import com.suresoft.sw_test_forum.left_reference.storage.repository.StorageCommentRepository;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StorageCommentService {
    private final com.suresoft.sw_test_forum.left_reference.storage.repository.StorageCommentRepository StorageCommentRepository;
    private final com.suresoft.sw_test_forum.left_reference.storage.repository.StorageCommentRepositoryImpl StorageCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public StorageCommentService(StorageCommentRepository StorageCommentRepository, StorageCommentRepositoryImpl StorageCommentRepositoryImpl, UserRepositoryImpl userRepositoryImpl) {
        this.StorageCommentRepository = StorageCommentRepository;
        this.StorageCommentRepositoryImpl = StorageCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param storageDto
     * @return
     */
    public StorageDto findAllByStorageIdxOrderByIdxDesc(StorageDto storageDto) {
        List<StorageCommentDto> storageCommentDtoList = StorageCommentMapper.INSTANCE.toDto(StorageCommentRepository.findAllByStorageIdxOrderByIdxDesc(storageDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (StorageCommentDto storageCommentDto : storageCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(storageCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            storageCommentDto.setNewIcon(NewIconCheck.isNew(storageCommentDto.getCreatedDate()));
            storageCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            storageCommentDto.setCreatedByUser(createdByUser);
        }

        storageDto = StorageMapper.INSTANCE.toDtoByCommentList(storageDto, storageCommentDtoList);

        return storageDto;
    }

    /**
     * 등록
     *
     * @param StorageCommentDto
     * @return
     */
    public long insertStorageComment(StorageCommentDto StorageCommentDto) {
        return StorageCommentRepository.save(StorageCommentMapper.INSTANCE.toEntity(StorageCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param StorageCommentDto
     * @return
     */
    @Transactional
    public long updateStorageComment(long idx, StorageCommentDto StorageCommentDto) {
        StorageComment persistStorageComment = StorageCommentRepository.getReferenceById(idx);
        StorageComment StorageComment = StorageCommentMapper.INSTANCE.toEntity(StorageCommentDto);

        persistStorageComment.update(StorageComment);

        return StorageCommentRepository.save(persistStorageComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStorageCommentByIdx(long idx) {
        StorageCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param storageIdx
     */
    public void deleteAllByStorageIdx(long storageIdx) {
        StorageCommentRepositoryImpl.deleteAllByStorageIdx(storageIdx);
    }
}