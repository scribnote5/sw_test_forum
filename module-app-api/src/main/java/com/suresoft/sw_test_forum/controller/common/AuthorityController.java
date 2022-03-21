package com.suresoft.sw_test_forum.controller.common;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.dto.LoginDto;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auths")
public class AuthorityController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    public AuthorityController(TokenProvider tokenProvider,
                               AuthenticationManagerBuilder authenticationManagerBuilder,
                               UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
    }

    /**
     * JWT를 Spring boot 쿠키에 저장하는 방식 - sameSite 적용
     *
     * @param loginDto
     * @param response
     * @return
     */
    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        // authenticationToken 생성, username + moduleName
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername() + "|" + loginDto.getModuleName(), loginDto.getPassword());

        // authenticate 메소드가 실행될 때 loadUserByUsername 메소드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT token 생성, token subject는 userIdx와 username으로 구성
        User user = userService.findUserIdxByUsername(loginDto.getUsername());
        String jwt = tokenProvider.createToken(user.getIdx(), authentication);

        ResponseCookie resCookie = ResponseCookie.from("accessToken", jwt)
                .httpOnly(true)
//                .domain("") //  해당 도메인에서만 유효한 쿠키
                .sameSite("Strict") // None, Strict, Lax
//                .secure(true) // HTTPS가 적용된 요청에만 전송되는 쿠키
                .path("/")
                .maxAge(Math.toIntExact(1 * 24 * 60 * 60))
                .build();
        response.addHeader("Set-Cookie", resCookie.toString());

        return new ResponseEntity(user, HttpStatus.OK);
    }


    /**
     * JWT를 Spring boot 쿠키에 저장하는 방식 - sameSite 미적용
     *
     * @param loginDto
     * @param response
     * @return
     */
//    @PostMapping("/authenticate")
//    public ResponseEntity<TokenDto> authenticate(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
//        // authenticationToken 생성, username + moduleName
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername() + "|" + loginDto.getModuleName(), loginDto.getPassword());
//
//        // authenticate 메소드가 실행될 때 loadUserByUsername 메소드 호출
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // JWT token 생성, token subject는 userIdx와 username으로 구성
//        long userIdx = userService.findUserIdxByUsername(loginDto.getUsername());
//        String jwt = tokenProvider.createToken(userIdx, authentication);
//
//        // Cookie 생성, cookie에 JWT token이 저장됨
//        Cookie cookie = new Cookie("accessToken", jwt);
//
//        // 1 day
//        cookie.setMaxAge(1 * 24 * 60 * 60);
//
//        // 설정
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//
//        // response에 cookie 추가
//        response.addCookie(cookie);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    /**
     * JWT를 쿠키에 저장하지 않는 방식
     * @param loginDto
     * @param response
     * @return
     */
//    @PostMapping("/authenticate")
//    public ResponseEntity<TokenDto> authenticate(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
//        // authenticationToken 생성, username + moduleName
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername() + "|" + loginDto.getModuleName(), loginDto.getPassword());
//
//        // authenticate 메소드가 실행될 때 loadUserByUsername 메소드 호출
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // JWT token 생성, token subject는 userIdx와 username으로 구성
//        long userIdx = userService.findUserIdxByUsername(loginDto.getUsername());
//        String jwt = tokenProvider.createToken(userIdx, authentication);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
//    }
}