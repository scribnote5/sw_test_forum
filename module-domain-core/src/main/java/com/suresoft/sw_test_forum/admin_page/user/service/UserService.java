package com.suresoft.sw_test_forum.admin_page.user.service;

import com.suresoft.sw_test_forum.admin_page.login_history.repository.LoginHistoryRepository;
import com.suresoft.sw_test_forum.admin_page.login_history.service.GeoLocationService;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserDto;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserPrincipal;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserSearchDto;
import com.suresoft.sw_test_forum.admin_page.user.dto.mapper.UserMapper;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepository;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.exception.InactiveUserException;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;
    //    private final LoginHistoryRepository loginHistoryRepository;
    //    private final GeoLocationService geoLocationService;
    @Value("${module.name}")
    private String moduleName;

    public UserService(UserRepository userRepository,
                       UserRepositoryImpl userRepositoryImpl,
                       LoginHistoryRepository loginHistoryRepository,
                       GeoLocationService geoLocationService) {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
        //        this.loginHistoryRepository = loginHistoryRepository;
        //        this.geoLocationService = geoLocationService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param userSearchDto
     * @return
     */
    public Page<UserDto> findUserList(Pageable pageable, UserSearchDto userSearchDto) {
        Page<UserDto> userDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        userDtoList = userRepositoryImpl.findAll(pageable, userSearchDto);

        // NewIcon 판별, createdBy 설정
        for (UserDto userDto : userDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = findUserByIdxAndSetUserWhenEmpty(userDto.getCreatedByIdx());

            userDto.setNewIcon(NewIconCheck.isNew(userDto.getCreatedDate()));
            userDto.setCreatedByUser(createdByUser);
        }

        return userDtoList;
    }


    /**
     * 권한에 따른, 리스트 조회
     *
     * @param authorityType
     * @return
     */
    public List<UserDto> findUserByAuthorityType(AuthorityType authorityType) {
        return userRepositoryImpl.findByAuthorityType(authorityType);
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public UserDto findUserAutoComplete(UserDto userDto) {
        // department 설정
        for (String department : userRepositoryImpl.findDistinctDepartment()) {
            userDto.getAutoCompleteDepartment().add(department);
        }

        return userDto;
    }

    /**
     * 등록(회원 가입)
     *
     * @return long
     */
    @Transactional
    public long joinUser(UserDto userDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(UserMapper.INSTANCE.toEntity(userDto)).getIdx();
    }

    /**
     * 사용자 정의 로그인
     *
     * @param userData
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserPrincipal loadUserByUsername(String userData) throws UsernameNotFoundException {
        User user;
        UserPrincipal userPrincipal;

        String[] array = userData.split("[|]");
        String username = array[0];
        String moduleName = array[1];

        if ("module-app-web".equals(moduleName)) {
            user = userRepository.findByUsername(username);
        } else {
            AuthorityType[] authorityType = {AuthorityType.ROOT, AuthorityType.MANAGER};

            user = userRepository.findByUsernameAndAuthorityTypeIn(username, authorityType);
        }

        // 사용자가 정보가 없는 경우, 비활성화 사용자인 경우 예외 발생
        if (EmptyUtil.isEmpty(user)) {
            log.info("잘못된 사용자 정보 입니다.");
//            insertFailureLoginHistory(username, "로그인 실패: 잘못된 사용자 정보");
            throw new InternalAuthenticationServiceException(null);
        } else if (user.getActiveStatus() == ActiveStatus.INACTIVE) {
            log.info("비활성화된 사용자 정보 입니다.");
//            insertFailureLoginHistory(username, "로그인 실패: 비활성화된 사용자");
            throw new InactiveUserException();
        } else {
            log.info("로그인");
            userPrincipal = new UserPrincipal(user);
        }

        return userPrincipal;
    }

    /**
     * idx 조회
     *
     * @param username
     * @return
     */
    public User findUserIdxByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 중복 조회
     *
     * @param username
     * @return
     */
    public boolean isDuplicationUserByUsername(String username) {
        String regex = "^[a-z0-9_.]{4,20}$";

        return userRepository.countByUsername(username) > 0 && username.length() >= 4 && username.length() <= 20 && Pattern.matches(regex, username) ? true : false;
    }

    /**
     * NON_MEMBER 권한 조회
     *
     * @param authorityType
     * @return
     */
    public boolean isNonUserAuthorityType(AuthorityType authorityType) {
        boolean result = false;

        if (AuthorityType.NON_USER == authorityType) {
            result = true;
        }

        return result;
    }

    /**
     * 사용자 계정이 존재하는지, 조회
     *
     * @param createdByIdx
     * @return
     */
    public User findUserByIdxAndSetUserWhenEmpty(long createdByIdx) {
        User createdByUser = userRepositoryImpl.findByIdx(createdByIdx);

        if (EmptyUtil.isEmpty(createdByUser)) {
            createdByUser = User.builder()
                    .username("withdrawal member")
                    .name("탈퇴 회원")
                    .position(Position.K_ETC)
                    .authorityType(AuthorityType.GENERAL)
                    .activeStatus(ActiveStatus.ACTIVE)
                    .build();
        }

        return createdByUser;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public UserDto findUserByIdx(long idx) {
        UserDto userDto = new UserDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            userDto.setAccess(AuthorityUtil.isAccessInRootAndManager());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            userDto = UserMapper.INSTANCE.toDto(userRepository.findById(idx).orElse(new User()));

            User createdByUser = findUserByIdxAndSetUserWhenEmpty(userDto.getCreatedByIdx());
            User lastModifiedByUser = findUserByIdxAndSetUserWhenEmpty(userDto.getLastModifiedByIdx());

            userDto.setAccess(AuthorityUtil.isAccessInUserPage(createdByUser.getUsername(), userDto.getAuthorityType().getAuthorityType(), userDto.getUsername()));
            userDto.setCreatedByUser(createdByUser);
            userDto.setLastModifiedByUser(lastModifiedByUser);

            userRepositoryImpl.updateViewsByIdx(idx);
            userDto.setViews(userDto.getViews() + 1);
        }

        // 현재 로그인한 사용자의 권한을 조회
        userDto.setAccessorAuthorityType(AuthorityType.valueOf(AuthorityUtil.getAuthorityType()));

        return userDto;
    }

    public long countUser() {
        return userRepository.count();
    }

    /**
     * 수정
     *
     * @param idx
     * @param userDto
     * @return
     */
    @Transactional
    public long updateUser(long idx, UserDto userDto) {
        User persistUser = userRepository.getReferenceById(idx);
        User user = UserMapper.INSTANCE.toEntity(userDto);
        persistUser.update(user, persistUser.getPassword());

        return userRepository.save(persistUser).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteUserByIdx(long idx) {
        userRepository.deleteById(idx);
    }
}