package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExampleComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper.MisraCppExampleCommentMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper.MisraCppExampleMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleCommentRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCppExampleCommentService {
    private final MisraCppExampleCommentRepository misraCppExampleCommentRepository;
    private final MisraCppExampleCommentRepositoryImpl misraCppExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCppExampleCommentService(MisraCppExampleCommentRepository misraCppExampleCommentRepository,
                                       MisraCppExampleCommentRepositoryImpl misraCppExampleCommentRepositoryImpl,
                                       UserRepositoryImpl userRepositoryImpl) {
        this.misraCppExampleCommentRepository = misraCppExampleCommentRepository;
        this.misraCppExampleCommentRepositoryImpl = misraCppExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * ????????? ??????
     *
     * @param misraCppExampleDto
     * @return
     */
    public MisraCppExampleDto findAllByMisraCppExampleIdxOrderByIdxDesc(MisraCppExampleDto misraCppExampleDto) {
        List<MisraCppExampleCommentDto> misraCppExampleCommentDtoList = MisraCppExampleCommentMapper.INSTANCE.toDto(misraCppExampleCommentRepository.findAllByMisraCppExampleIdxOrderByIdxDesc(misraCppExampleDto.getIdx()));

        // NewIcon ??????, createdBy ??????
        for (MisraCppExampleCommentDto misraCppExampleCommentDto : misraCppExampleCommentDtoList) {
            // ?????? ??????
            // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userRepositoryImpl.findByIdx(misraCppExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("?????? ??????")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCppExampleCommentDto.setNewIcon(NewIconCheck.isNew(misraCppExampleCommentDto.getCreatedDate()));
            misraCppExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppExampleCommentDto.setCreatedByUser(createdByUser);
        }

        misraCppExampleDto = MisraCppExampleMapper.INSTANCE.toDtoByCommentList(misraCppExampleDto, misraCppExampleCommentDtoList);

        return misraCppExampleDto;
    }

    /**
     * ??????
     *
     * @param MisraCppExampleCommentDto
     * @return
     */
    public long insertMisraCppExampleComment(MisraCppExampleCommentDto MisraCppExampleCommentDto) {
        return misraCppExampleCommentRepository.save(MisraCppExampleCommentMapper.INSTANCE.toEntity(MisraCppExampleCommentDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @param MisraCppExampleCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCppExampleComment(long idx, MisraCppExampleCommentDto MisraCppExampleCommentDto) {
        MisraCppExampleComment persistMisraCppExampleComment = misraCppExampleCommentRepository.getById(idx);
        MisraCppExampleComment MisraCppExampleComment = MisraCppExampleCommentMapper.INSTANCE.toEntity(MisraCppExampleCommentDto);

        persistMisraCppExampleComment.update(MisraCppExampleComment);

        return misraCppExampleCommentRepository.save(persistMisraCppExampleComment).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteMisraCppExampleCommentByIdx(long idx) {
        misraCppExampleCommentRepository.deleteById(idx);
    }

    /**
     * ?????? ??????
     *
     * @param misraCppExampleIdx
     */
    public void deleteAllByMisraCppExampleIdx(long misraCppExampleIdx) {
        misraCppExampleCommentRepositoryImpl.deleteAllByMisraCppExampleIdx(misraCppExampleIdx);
    }
}