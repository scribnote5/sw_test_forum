package com.suresoft.sw_test_forum.admin_page.setting.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SettingDto extends CommonDto {
    @Min(value = 0, message = "'MISRA C 규칙 개수'는 0보다 커야 합니다.")
    private long totalMisraCRuleNumber;

    @Min(value = 0, message = "'MISRA C++ 규칙 개수'는 0보다 커야 합니다.")
    private long totalMisraCppRuleNumber;

    @Min(value = 0, message = "'CWE 규칙 개수'는 0보다 커야 합니다.")
    private long totalCweRuleNumber;

    @Min(value = 0, message = "'MISRA C 규칙 개수'는 0보다 커야 합니다.")
    private long totalJavaCodeConventionsRuleNumber;

    @Min(value = 0, message = "'MISRA C++ 규칙 개수'는 0보다 커야 합니다.")
    private long totalCweJavaRuleNumber;

    @Min(value = 0, message = "'CWE 규칙 개수'는 0보다 커야 합니다.")
    private long totalStyleCopRuleNumber;

    @Min(value = 0, message = "'CWE 규칙 개수'는 0보다 커야 합니다.")
    private long totalFxCopRuleNumber;

    private LocalDate initialReleaseDate;

    private LocalDate lastReleaseDate;

    @NotBlank(message = "'담당자 메일'은 공란이 될 수 없습니다.")
    @Size(max = 255, message = "'담당자 메일'의 길이는 255 보다 작아야 합니다.")
    private String developerEmail;
}