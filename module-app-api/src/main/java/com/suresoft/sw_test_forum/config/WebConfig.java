package com.suresoft.sw_test_forum.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ObjectMapper objectMapper;

    public WebConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Vue에서는 v-html 태그를 사용하지 않는한 XSS 공격을 방어할 수 있으므로, Spring boot에서 XSS 공격 대비를 위한 HTML escape 빈을 등록하지 않는다.
     * @return
     */
//    @Bean
//    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
//        ObjectMapper copy = objectMapper.copy();
//        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
//
//        return new MappingJackson2HttpMessageConverter(copy);
//    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

    }
}
