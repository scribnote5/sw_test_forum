package com.suresoft.sw_test_forum.admin_page.notice.service;

import com.suresoft.sw_test_forum.admin_page.notice.domain.Notice;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeSearchDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.mapper.NoticeMapper;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeCommentRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeRepository;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeRepositoryImpl noticeRepositoryImpl;
    private final NoticeCommentRepositoryImpl noticeCommentRepositoryImpl;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public NoticeService(NoticeRepository noticeRepository, NoticeRepositoryImpl noticeRepositoryImpl, NoticeCommentRepositoryImpl noticeCommentRepositoryImpl, UserService userService) {
        this.noticeRepository = noticeRepository;
        this.noticeRepositoryImpl = noticeRepositoryImpl;
        this.noticeCommentRepositoryImpl = noticeCommentRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<NoticeDto> findAllByHighPriorityAsc() {
        List<NoticeDto> noticeDtoList = noticeRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (NoticeDto noticeDto : noticeDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(noticeDto.getCreatedByIdx());

            noticeDto.setNewIcon(NewIconCheck.isNew(noticeDto.getCreatedDate()));
            noticeDto.setCreatedByUser(createdByUser);
            noticeDto.setCommentDtoCount(noticeCommentRepositoryImpl.countAllByNoticeIdx(noticeDto.getIdx()));
        }

        return noticeDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param noticeSearchDto
     * @return
     */
    public Page<NoticeDto> findNoticeList(Pageable pageable, NoticeSearchDto noticeSearchDto) {
        Page<NoticeDto> noticeDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        noticeDtoList = noticeRepositoryImpl.findAll(pageable, noticeSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (NoticeDto noticeDto : noticeDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(noticeDto.getCreatedByIdx());

            noticeDto.setNewIcon(NewIconCheck.isNew(noticeDto.getCreatedDate()));
            noticeDto.setCreatedByUser(createdByUser);
            noticeDto.setCommentDtoCount(noticeCommentRepositoryImpl.countAllByNoticeIdx(noticeDto.getIdx()));
        }

        return noticeDtoList;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<NoticeDto> findTop10() {
        return noticeRepositoryImpl.findTop10();
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite() {
        List<NoticeDto> highPriorityList = noticeRepositoryImpl.findAllByHighPriorityAsc();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (NoticeDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public NoticeDto findNoticeAutoComplete(NoticeDto NoticeDto) {
        // title 설정
        for (String title : noticeRepositoryImpl.findDistinctTitle()) {
            NoticeDto.getAutoCompleteTitle().add(title);
        }

        return NoticeDto;
    }

    /**
     * 등록
     *
     * @param noticeDto
     * @return
     */
    public long insertNotice(NoticeDto noticeDto) {
        return noticeRepository.save(NoticeMapper.INSTANCE.toEntity(noticeDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public NoticeDto findNoticeByIdx(long idx) {
        NoticeDto noticeDto = NoticeMapper.INSTANCE.toDto(noticeRepository.findById(idx).orElse(new Notice()));

        // 권한 설정
        // Register: 로그인한 사용자 Register 접근 가능
        if (idx == 0) {
            noticeDto.setAccess(true);
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(noticeDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(noticeDto.getLastModifiedByIdx());

            noticeDto.setAccess(AuthorityUtil.isAccessInRootAndManager());
            noticeDto.setCreatedByUser(createdByUser);
            noticeDto.setLastModifiedByUser(lastModifiedByUser);
        }

        noticeRepositoryImpl.updateViewsByIdx(idx);
        noticeDto.setViews(noticeDto.getViews() + 1);

        return noticeDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx) {
        List<NoticeDto> highPriorityList = noticeRepositoryImpl.findAllByHighPriorityAsc();
        Notice noticePriority = noticeRepositoryImpl.findAllByHighPriorityAscCheckPriority(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (NoticeDto highPriority : highPriorityList) {
            if (noticePriority.getPriority() == highPriority.getPriority()) {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(false, "지금 설정된 우선순위 입니다.");
            } else {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
            }
        }

        return priorityDtoArray;
    }

    /**
     * 수정
     *
     * @param idx
     * @param noticeDto
     * @return
     */
    @Transactional
    public long updateNotice(long idx, NoticeDto noticeDto) {
        Notice persistNotice = noticeRepository.getReferenceById(idx);
        Notice notice = NoticeMapper.INSTANCE.toEntity(noticeDto);

        persistNotice.update(notice);

        return noticeRepository.save(persistNotice).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteNoticeByIdx(long idx) {
        noticeRepository.deleteById(idx);
    }
}