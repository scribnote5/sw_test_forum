package com.suresoft.sw_test_forum.controller;

import com.suresoft.sw_test_forum.jwt.JwtFilter;
import com.suresoft.sw_test_forum.jwt.TokenProvider;
import com.suresoft.sw_test_forum.admin_page.user.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.user.dto.LoginDto;
import com.suresoft.sw_test_forum.admin_page.user.user.dto.TokenDto;
import com.suresoft.sw_test_forum.admin_page.user.user.service.UserService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auths")
public class AuthorityRestController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    public AuthorityRestController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    /*
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        // authenticationToken 생성, username + moduleName
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername() + "|" + loginDto.getModuleName(), loginDto.getPassword());

        // authenticate 메소드가 실행될 때 loadUserByUsername 메소드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT token 생성, token subject는 userIdx와 username으로 구성
        long userIdx = userService.findUserIdxByUsername(loginDto.getUsername());
        String jwt = tokenProvider.createToken(userIdx, authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
    */

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        // authenticationToken 생성, username + moduleName
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername() + "|" + loginDto.getModuleName(), loginDto.getPassword());

        // authenticate 메소드가 실행될 때 loadUserByUsername 메소드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT token 생성, token subject는 userIdx와 username으로 구성
        long userIdx = userService.findUserIdxByUsername(loginDto.getUsername());
        String jwt = tokenProvider.createToken(userIdx, authentication);

        // create a cookie
        Cookie cookie = new Cookie("accessToken",jwt);

        // expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);

        // optional properties
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

        return new ResponseEntity<>(new TokenDto(jwt), HttpStatus.OK);
    }

    @PostMapping("/getAllUserInfo")
    public ResponseEntity<UserDetails> getAllUserInfo() {
        log.info("getAllUserInfo");
        log.info(AuthorityUtil.getCurrentUserDetails());

        return ResponseEntity.ok(AuthorityUtil.getCurrentUserDetails());
    }

    @PostMapping("/getRootUserInfo")
    @PreAuthorize("hasAuthority('최고 관리자')")
    public ResponseEntity<UserDetails> getRootUserInfo() {
        log.info("getRootUserInfo");
        log.info(AuthorityUtil.getCurrentUserDetails());

        return ResponseEntity.ok(AuthorityUtil.getCurrentUserDetails());
    }

    @PostMapping("/getManagerUserInfo")
    @PreAuthorize("hasAuthority('관리자')")
    public ResponseEntity<UserDetails> getManagerUserInfo() {
        log.info("getManagerUserInfo");
        log.info(AuthorityUtil.getCurrentUserDetails());

        return ResponseEntity.ok(AuthorityUtil.getCurrentUserDetails());
    }

    @PostMapping("/getManagerTest/{username}")
    @PreAuthorize("(hasAuthority('관리자') and #username == authentication.principal.username) or (hasAuthority('최고 관리자'))")
    public ResponseEntity<User> getManagerTest(@PathVariable String username) {
        log.info("getManagerTest");
        log.info(AuthorityUtil.getCurrentUserDetails());

        return ResponseEntity.ok(userService.findOneWithAuthorityTypeByUsername(username));
    }

    @PostMapping("/getManagerTest4")
    @PreAuthorize("hasAuthority('관리자')")
    public ResponseEntity<String> getManagerTest4() {
        log.info("getManagerTest");

        return ResponseEntity.ok("OK");
    }
}