package com.suresoft.sw_test_forum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Value("${localhost.url}")
    private String localhostUrl;
    @Value("${module-app-web.url}")
    private String moduleAppWebUrl;
    @Value("${module-app-admin.url}")
    private String moduleAppAdminUrl;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin(localhostUrl);
        config.addAllowedOrigin(moduleAppWebUrl);
        config.addAllowedOrigin(moduleAppAdminUrl);
        config.addExposedHeader("*");
        config.addAllowedOriginPattern("*");
        config.addAllowedOriginPattern(CorsConfiguration.ALL);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}


