package com.suresoft.sw_test_forum.common.config;

import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl(); // AuditorAware 의 구현체 객체 생성
    }
}

class AuditorAwareImpl implements AuditorAware<Long> {
    public Optional<Long> getCurrentAuditor() {
        UserDetails userDetails = AuthorityUtil.getCurrentUserDetails();
        String result;

        if (EmptyUtil.isEmpty(userDetails)) {
            result = "1";
        } else {
            result = (userDetails.getUsername()).split("[|]")[0];
        }

        // userIdx 반환
        return Optional.of(Long.parseLong(result));
    }
}