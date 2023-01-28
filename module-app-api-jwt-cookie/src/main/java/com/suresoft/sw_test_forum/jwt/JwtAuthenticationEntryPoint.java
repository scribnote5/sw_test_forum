package com.suresoft.sw_test_forum.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 유효한 자격증명을 제공하지 않고 접근하려 할때 401
     *
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("401(Unauthorized) error");

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효한 토큰이 없는 상태에서 접근하여 오류가 발생하였습니다.");
    }
}