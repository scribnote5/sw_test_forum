package com.suresoft.sw_test_forum.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class AuthorityUtil {
    public static UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails;

        // 비인증 사용자, 인증이 안된 경우, authentication 객체가 null인 경우
        // -> 접근 불가
        if (EmptyUtil.isEmpty(authentication) || "anonymousUser".equals(authentication.getPrincipal())) {
//            log.error("사용자 인증 절차에 문제가 발생하였습니다.");
//            throw new InternalAuthenticationServiceException(null);
            userDetails = null;
        } else {
            userDetails = (UserDetails) authentication.getPrincipal();
        }

        return userDetails;
    }

    /**
     * [Register 상황에서 사용자 권한에 따른 접근 가능 여부]
     * <p>
     * 비인증 사용자인 경우 접근 불가
     * root, manager, general: 접근 허용
     *
     * @return
     */
    public static Boolean isAccessInRegister() {
        UserDetails userDetails = getCurrentUserDetails();
        boolean result = false;

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "최고 관리자":
                case "관리자":
                case "일반 회원":
                    result = true;
                    break;
                default:
                    result = false;
                    break;
            }
        }

        return result;
    }

    /**
     * [관리자만 접근 가능 여부]
     * <p>
     * root: 접근 허용
     *
     * @return
     */
    public static Boolean isAccessInRootAndManager() {
        UserDetails userDetails = getCurrentUserDetails();
        boolean result = false;

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "최고 관리자":
                case "관리자":
                    result = true;
                    break;
                case "일반 회원":
                default:
                    result = false;
                    break;
            }
        }

        return result;
    }

    /**
     * [사용자 권한 반환]
     *
     * @return
     */
    public static String getAuthorityType() {
        UserDetails userDetails = getCurrentUserDetails();
        String result = "비회원";

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            switch (grantedAuthority.getAuthority()) {
                case "최고 관리자":
                    result = "ROOT";
                    break;
                case "관리자":
                    result = "MANAGER";
                    break;
                case "일반 회원":
                    result = "GENERAL";
                    break;
                case "읽기 회원":
                    result = "READER";
                    break;
                case "비회원":
                default:
                    result = "NON_USER";
                    break;
            }
        }

        return result;
    }

    /**
     * [일반적인 상황에서 사용자 권한에 따른 접근 가능 여부]
     * <p>
     * 비인증 사용자인 경우 접근 불가
     * root: 모든 권한에 대한 접근 허용
     * manager: 작성자가 root인 경우 접근 불가, 로그인한 사용자의 username과 작성자가 같은 경우 접근 허용
     * general: 로그인한 사용자의 username과 작성자가 같은 경우 접근 허용
     *
     * @param username, authorityType
     * @return
     */
    public static Boolean isAccessInGeneral(String username, String authorityType) {
        UserDetails userDetails = getCurrentUserDetails();
        String authenticationUsername = userDetails.getUsername().split("[|]")[1];
        boolean result = false;

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            log.info("grantedAuthority.getAuthority(): " + grantedAuthority.getAuthority());
            log.info("authorityType: " + authorityType);
            log.info("username: " + username);
            log.info("authenticationUsername: " + authenticationUsername);

            switch (grantedAuthority.getAuthority()) {
                // 로그인한 사용자의 권한: 최고 관리자
                // -> 모두 접근 가능
                case "최고 관리자":
                    result = true;
                    break;
                case "관리자":
                    // createdBy: root
                    // -> 접근 불가
                    if ("root".equals(username)) {
                        result = false;
                    }
                    // username authority: 관리자
                    // 로그인한 사용자의 username과 게시글 작성자의 username 다름
                    // -> 접근 불가
                    else if ("관리자".equals(authorityType) && !authenticationUsername.equals(username)) {
                        result = false;
                    }
                    // 나머지 조건
                    // -> 접근 가능
                    else {
                        result = true;
                    }
                    break;
                default:
                    // 로그인한 사용자의 username과 게시글 작성자의 username 같음
                    // -> 접근 가능
                    if (authenticationUsername.equals(username)) {
                        result = true;
                    }
                    // 로그인한 사용자의 username과 게시글 작성자의 username 다름
                    // -> 접근 불가
                    else {
                        result = false;
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * [사용자 페이지 접근에서 사용자 권한에 따른 접근 가능 여부]
     * <p>
     * 비인증 사용자인 경우 접근 불가
     * root: 모든 권한에 대한 접근 허용
     * manager: root 사용자 접근 불가, 작성자가 root인 경우 접근 허용, 로그인한 사용자의 username과 작성자가 같은 경우 접근 허용
     * general: 로그인한 사용자의 username과 작성자가 같은 경우 접근 허용
     *
     * @param username, authorityType
     * @return
     */
    public static Boolean isAccessInUserPage(String createdByUsername, String authorityTypeInUserDto, String usernameInUserDto) {
        UserDetails userDetails = getCurrentUserDetails();
        String authenticationUsername = userDetails.getUsername().split("[|]")[1];
        boolean result = false;

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            log.info("grantedAuthority.getAuthority(): " + grantedAuthority.getAuthority());
            log.info("authorityTypeInUserDto: " + authorityTypeInUserDto);
            log.info("createdByUsername: " + createdByUsername);
            log.info("authenticationUsername: " + authenticationUsername);
            log.info("usernameInUserDto: " + usernameInUserDto);

            switch (grantedAuthority.getAuthority()) {
                // 로그인한 사용자의 권한: 최고 관리자
                // -> 모두 접근 가능
                case "최고 관리자":
                    result = true;
                    break;
                case "관리자":
                    // 사용자 정보에서의 username: root
                    // -> 접근 불가
                    if ("root".equals(usernameInUserDto)) {
                        result = false;
                    }
                    // createdBy: root
                    // -> 접근 허용
                    else if ("root".equals(createdByUsername) && !"관리자".equals(authorityTypeInUserDto)) {
                        result = true;
                    }
                    // 로그인한 사용자의 username과 접근하려는 사용자의 username 다름
                    // -> 접근 불가
                    else if (!authenticationUsername.equals(usernameInUserDto)) {
                        result = false;
                    }
                    // 나머지 조건
                    // -> 접근 가능
                    else {
                        result = true;
                    }
                    break;
                default:
                    // 사용자 정보에서의 username과 로그인한 사용자의 username이 같음
                    // -> 접근 가능
                    if (usernameInUserDto.equals(authenticationUsername)) {
                        result = true;
                    }
                    // 로그인한 사용자의 username과 게시글 작성자의 username 다름
                    // -> 접근 불가
                    else {
                        result = false;
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * [현재 사용자 username 반환]
     *
     * @return
     */
    public static String getCurrentUsername() {
        UserDetails userDetails = getCurrentUserDetails();
        String authenticationUsername = userDetails.getUsername().split("[|]")[1];

        return authenticationUsername;
    }

    /**
     * [현재 사용자 idx 반환]
     *
     * @return
     */
    public static long getCurrentUserIdx() {
        UserDetails userDetails = getCurrentUserDetails();
        String idx = userDetails.getUsername().split("[|]")[0];

        return Long.parseLong(idx);
    }
}