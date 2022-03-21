package com.suresoft.sw_test_forum.controller.admin_page;

import com.suresoft.sw_test_forum.admin_page.setting.dto.SettingDto;
import com.suresoft.sw_test_forum.admin_page.setting.service.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/settings")
public class SettingController {
    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @return
     */
    @GetMapping("/form")
    public ResponseEntity getSettingWhenForm() {
        long idx = 1;
        SettingDto settingDto = settingService.findSetting(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (settingDto.isAccess()) {
            responseEntity = new ResponseEntity(settingDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }


    /**
     * 수정 페이지에서, 수정
     *
     * @param settingDto
     * @return
     */
    @PutMapping
    public ResponseEntity<?> putSetting(@RequestBody @Valid SettingDto settingDto) {
        long idx = 1;

        settingService.updateSetting(idx, settingDto);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}