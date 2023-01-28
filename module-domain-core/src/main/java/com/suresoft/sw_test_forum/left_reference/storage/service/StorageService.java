package com.suresoft.sw_test_forum.left_reference.storage.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.left_reference.storage.repository.StorageCommentRepositoryImpl;
import com.suresoft.sw_test_forum.left_reference.storage.domain.Storage;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageSearchDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.mapper.StorageMapper;
import com.suresoft.sw_test_forum.left_reference.storage.repository.StorageRepository;
import com.suresoft.sw_test_forum.left_reference.storage.repository.StorageRepositoryImpl;
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
public class StorageService {
    private final StorageRepository storageRepository;
    private final StorageRepositoryImpl storageRepositoryImpl;
    private final StorageCommentRepositoryImpl storageCommentRepositoryImpl;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public StorageService(StorageRepository storageRepository, StorageRepositoryImpl storageRepositoryImpl, StorageCommentRepositoryImpl storageCommentRepositoryImpl, UserService userService) {
        this.storageRepository = storageRepository;
        this.storageRepositoryImpl = storageRepositoryImpl;
        this.storageCommentRepositoryImpl = storageCommentRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<StorageDto> findAllByHighPriorityAsc() {
        List<StorageDto> storageDtoList = storageRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (StorageDto storageDto : storageDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(storageDto.getCreatedByIdx());

            storageDto.setNewIcon(NewIconCheck.isNew(storageDto.getCreatedDate()));
            storageDto.setCreatedByUser(createdByUser);
            storageDto.setCommentDtoCount(storageCommentRepositoryImpl.countAllByStorageIdx(storageDto.getIdx()));
        }

        return storageDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param storageSearchDto
     * @return
     */
    public Page<StorageDto> findStorageList(Pageable pageable, StorageSearchDto storageSearchDto) {
        Page<StorageDto> storageDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        storageDtoList = storageRepositoryImpl.findAll(pageable, storageSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (StorageDto storageDto : storageDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(storageDto.getCreatedByIdx());

            storageDto.setNewIcon(NewIconCheck.isNew(storageDto.getCreatedDate()));
            storageDto.setCreatedByUser(createdByUser);
            storageDto.setCommentDtoCount(storageCommentRepositoryImpl.countAllByStorageIdx(storageDto.getIdx()));
        }

        return storageDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite() {
        List<StorageDto> highPriorityList = storageRepositoryImpl.findAllByHighPriorityAsc();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (StorageDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public StorageDto findStorageAutoComplete(StorageDto StorageDto) {
        // title 설정
        for (String title : storageRepositoryImpl.findDistinctTitle()) {
            StorageDto.getAutoCompleteTitle().add(title);
        }

        return StorageDto;
    }

    /**
     * 등록
     *
     * @param storageDto
     * @return
     */
    public long insertStorage(StorageDto storageDto) {
        return storageRepository.save(StorageMapper.INSTANCE.toEntity(storageDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StorageDto findStorageByIdx(long idx) {
        StorageDto storageDto = StorageMapper.INSTANCE.toDto(storageRepository.findById(idx).orElse(new Storage()));

        // 권한 설정
        // Register: 로그인한 사용자 Register 접근 가능
        if (idx == 0) {
            storageDto.setAccess(true);
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(storageDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(storageDto.getLastModifiedByIdx());

            storageDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            storageDto.setCreatedByUser(createdByUser);
            storageDto.setLastModifiedByUser(lastModifiedByUser);
        }

        storageRepositoryImpl.updateViewsByIdx(idx);
        storageDto.setViews(storageDto.getViews() + 1);

        return storageDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx) {
        List<StorageDto> highPriorityList = storageRepositoryImpl.findAllByHighPriorityAsc();
        Storage storagePriority = storageRepositoryImpl.findAllByHighPriorityAscCheckPriority(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (StorageDto highPriority : highPriorityList) {
            if (storagePriority.getPriority() == highPriority.getPriority()) {
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
     * @param storageDto
     * @return
     */
    @Transactional
    public long updateStorage(long idx, StorageDto storageDto) {
        Storage persistStorage = storageRepository.getReferenceById(idx);
        Storage storage = StorageMapper.INSTANCE.toEntity(storageDto);

        persistStorage.update(storage);

        return storageRepository.save(persistStorage).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStorageByIdx(long idx) {
        storageRepository.deleteById(idx);
    }
}