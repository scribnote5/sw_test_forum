package com.suresoft.sw_test_forum;

import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ModuleApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(NoticeRepository noticeRepository) {
        return (args) -> {
            /* Notice 모두 삭제 */
//            noticeRepository.deleteAll();

            /* Notice 등록 */
//            IntStream.rangeClosed(1, 200).forEach(index ->
//                    noticeRepository.save(Notice.builder()
//                            .title("게시글" + index)
//                            .content("컨텐츠" + index)
//                            .activeStatus(ActiveStatus.ACTIVE)
//                            .createdByIdx(1)
//                            .priority(6)
//                            .build()));
        };
    }
}