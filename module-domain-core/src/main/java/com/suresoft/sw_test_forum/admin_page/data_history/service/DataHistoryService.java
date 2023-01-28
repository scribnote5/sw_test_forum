package com.suresoft.sw_test_forum.admin_page.data_history.service;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistoryDto;
import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistorySearchDto;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepository;
import com.suresoft.sw_test_forum.admin_page.data_history.repository.DataHistoryRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataHistoryService {
    private final DataHistoryRepository dataHistoryRepository;
    private final DataHistoryRepositoryImpl dataHistoryRepositoryImpl;
    private final UserService userService;

    public DataHistoryService(DataHistoryRepository dataHistoryRepository,
                              DataHistoryRepositoryImpl dataHistoryRepositoryImpl,
                              UserService userService) {
        this.dataHistoryRepository = dataHistoryRepository;
        this.dataHistoryRepositoryImpl = dataHistoryRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param dataHistorySearchDto
     * @return
     */
    public Page<DataHistoryDto> findDataHistoryList(Pageable pageable, DataHistorySearchDto dataHistorySearchDto) {
        Page<DataHistoryDto> dataHistoryDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        dataHistoryDtoList = dataHistoryRepositoryImpl.findAll(pageable, dataHistorySearchDto);

        // NewIcon 판별, createdBy 설정
        for (DataHistoryDto dataHistoryDto : dataHistoryDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(dataHistoryDto.getCreatedByIdx());

            dataHistoryDto.setNewIcon(NewIconCheck.isNew(dataHistoryDto.getCreatedDate()));
            dataHistoryDto.setCreatedByUser(createdByUser);
        }

        return dataHistoryDtoList;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<DataHistoryDto> findTop10() {
        return dataHistoryRepositoryImpl.findTop10();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public DataHistoryDto findDataHistoryByIdx(long idx) {
        DataHistoryDto dataHistoryDto = dataHistoryRepositoryImpl.findByIdx(idx);

        User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(dataHistoryDto.getCreatedByIdx());
        User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(dataHistoryDto.getLastModifiedByIdx());

        dataHistoryDto.setCreatedByUser(createdByUser);
        dataHistoryDto.setLastModifiedByUser(lastModifiedByUser);

        dataHistoryRepositoryImpl.updateViewsByIdx(idx);
        dataHistoryDto.setViews(dataHistoryDto.getViews() + 1);

        return dataHistoryDto;
    }

    /**
     * AuditType에 따른 리스트 개수 조회
     *
     * @param auditType
     * @return
     */
    public long countDataHistoryByAuditType(AuditType auditType) {
        return dataHistoryRepository.countAllByAuditType(auditType);
    }

    /**
     * 기간 내의 리스트 개수 조회
     *
     * @param days
     * @return
     */
    public long countDataHistoryByDays(long days) {
        return dataHistoryRepository.countAllByCreatedDateBetween(LocalDateTime.now().minusDays(days), LocalDateTime.now());
    }
}