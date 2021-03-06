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
     * ????????? ??????
     *
     * @param pageable
     * @param userSearchDto
     * @return
     */
    public Page<UserDto> findUserList(Pageable pageable, UserSearchDto userSearchDto) {
        Page<UserDto> userDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        userDtoList = userRepositoryImpl.findAll(pageable, userSearchDto);

        // NewIcon ??????, createdBy ??????
        for (UserDto userDto : userDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = findUserByIdxAndSetUserWhenEmpty(userDto.getCreatedByIdx());

            userDto.setNewIcon(NewIconCheck.isNew(userDto.getCreatedDate()));
            userDto.setCreatedByUser(createdByUser);
        }

        return userDtoList;
    }


    /**
     * ????????? ??????, ????????? ??????
     *
     * @param authorityType
     * @return
     */
    public List<UserDto> findUserByAuthorityType(AuthorityType authorityType) {
        return userRepositoryImpl.findByAuthorityType(authorityType);
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public UserDto findUserAutoComplete(UserDto userDto) {
        // department ??????
        for (String department : userRepositoryImpl.findDistinctDepartment()) {
            userDto.getAutoCompleteDepartment().add(department);
        }


        return userDto;
    }

    /**
     * ??????(?????? ??????)
     *
     * @return long
     */
    @Transactional
    public long joinUser(UserDto userDto) {
        // ???????????? ?????????
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(UserMapper.INSTANCE.toEntity(userDto)).getIdx();
    }

    /**
     * ????????? ?????? ?????????
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

        // ???????????? ????????? ?????? ??????, ???????????? ???????????? ?????? ?????? ??????
        if (EmptyUtil.isEmpty(user)) {
            log.info("????????? ????????? ?????? ?????????.");
//            insertFailureLoginHistory(username, "????????? ??????: ????????? ????????? ??????");
            throw new InternalAuthenticationServiceException(null);
        } else if (user.getActiveStatus() == ActiveStatus.INACTIVE) {
            log.info("??????????????? ????????? ?????? ?????????.");
//            insertFailureLoginHistory(username, "????????? ??????: ??????????????? ?????????");
            throw new InactiveUserException();
        } else {
            log.info("?????????");
            userPrincipal = new UserPrincipal(user);
        }

        return userPrincipal;
    }

    /**
     * idx ??????
     *
     * @param username
     * @return
     */
    public User findUserIdxByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * ?????? ??????
     *
     * @param username
     * @return
     */
    public boolean isDuplicationUserByUsername(String username) {
        String regex = "^[a-z0-9]{4,16}$";

        return userRepository.countByUsername(username) > 0 && username.length() >= 4 && username.length() <= 16 && Pattern.matches(regex, username) ? true : false;
    }

    /**
     * NON_MEMBER ?????? ??????
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
     * ????????? ????????? ???????????????, ??????
     *
     * @param createdByIdx
     * @return
     */
    public User findUserByIdxAndSetUserWhenEmpty(long createdByIdx) {
        User createdByUser = userRepositoryImpl.findByIdx(createdByIdx);

        if (EmptyUtil.isEmpty(createdByUser)) {
            createdByUser = User.builder()
                    .username("withdrawal member")
                    .name("?????? ??????")
                    .position(Position.K_ETC)
                    .authorityType(AuthorityType.GENERAL)
                    .activeStatus(ActiveStatus.ACTIVE)
                    .build();
        }

        return createdByUser;
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public UserDto findUserByIdx(long idx) {
        UserDto userDto = new UserDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            userDto.setAccess(AuthorityUtil.isAccessInRootAndManager());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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

        // ?????? ???????????? ???????????? ????????? ??????
        userDto.setAccessorAuthorityType(AuthorityType.valueOf(AuthorityUtil.getAuthorityType()));

        return userDto;
    }

    public long countUser() {
        return userRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param userDto
     * @return
     */
    @Transactional
    public long updateUser(long idx, UserDto userDto) {
        User persistUser = userRepository.getById(idx);
        User user = UserMapper.INSTANCE.toEntity(userDto);
        persistUser.update(user, persistUser.getPassword());

        return userRepository.save(persistUser).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteUserByIdx(Long idx) {
        userRepository.deleteById(idx);
    }
}