package com.suresoft.sw_test_forum.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 필요한 권한이 없이 접근하려 할때 403
     *
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("403(Access Denied) error");

        response.sendError(HttpServletResponse.SC_FORBIDDEN, "필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.");
    }
}