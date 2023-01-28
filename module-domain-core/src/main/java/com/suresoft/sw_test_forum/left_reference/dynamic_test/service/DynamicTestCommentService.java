package com.suresoft.sw_test_forum.left_reference.dynamic_test.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestComment;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestCommentDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper.DynamicTestCommentMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper.DynamicTestMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestCommentRepository;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DynamicTestCommentService {
    private final DynamicTestCommentRepository dynamicTestCommentRepository;
    private final DynamicTestCommentRepositoryImpl dynamicTestCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public DynamicTestCommentService(DynamicTestCommentRepository dynamicTestCommentRepository,
                                     DynamicTestCommentRepositoryImpl dynamicTestCommentRepositoryImpl,
                                     UserRepositoryImpl userRepositoryImpl) {
        this.dynamicTestCommentRepository = dynamicTestCommentRepository;
        this.dynamicTestCommentRepositoryImpl = dynamicTestCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param dynamicTestDto
     * @return
     */
    public DynamicTestDto findAllByDynamicTestIdxOrderByIdxDesc(DynamicTestDto dynamicTestDto) {
        List<DynamicTestCommentDto> dynamicTestCommentDtoList = DynamicTestCommentMapper.INSTANCE.toDto(dynamicTestCommentRepository.findAllByDynamicTestIdxOrderByIdxDesc(dynamicTestDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (DynamicTestCommentDto dynamicTestCommentDto : dynamicTestCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(dynamicTestCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            dynamicTestCommentDto.setNewIcon(NewIconCheck.isNew(dynamicTestCommentDto.getCreatedDate()));
            dynamicTestCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            dynamicTestCommentDto.setCreatedByUser(createdByUser);
        }

        dynamicTestDto = DynamicTestMapper.INSTANCE.toDtoByCommentList(dynamicTestDto, dynamicTestCommentDtoList);

        return dynamicTestDto;
    }

    /**
     * 등록
     *
     * @param DynamicTestCommentDto
     * @return
     */
    public long insertDynamicTestComment(DynamicTestCommentDto DynamicTestCommentDto) {
        return dynamicTestCommentRepository.save(DynamicTestCommentMapper.INSTANCE.toEntity(DynamicTestCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param DynamicTestCommentDto
     * @return
     */
    @Transactional
    public long updateDynamicTestComment(long idx, DynamicTestCommentDto DynamicTestCommentDto) {
        DynamicTestComment persistDynamicTestComment = dynamicTestCommentRepository.getReferenceById(idx);
        DynamicTestComment DynamicTestComment = DynamicTestCommentMapper.INSTANCE.toEntity(DynamicTestCommentDto);

        persistDynamicTestComment.update(DynamicTestComment);

        return dynamicTestCommentRepository.save(persistDynamicTestComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteDynamicTestCommentByIdx(long idx) {
        dynamicTestCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByDynamicTestIdx(long idx) {
        dynamicTestCommentRepositoryImpl.deleteAllByDynamicTestIdx(idx);
    }
}