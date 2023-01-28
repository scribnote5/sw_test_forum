package com.suresoft.sw_test_forum.admin_page.setting.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
public class Setting extends CommonAudit {
    @Column(name = "total_misra_c_rule_number")
    private long totalMisraCRuleNumber;

    @Column(name = "total_misra_cpp_rule_number")
    private long totalMisraCppRuleNumber;

    @Column(name = "total_cwe_rule_number")
    private long totalCweRuleNumber;

    @Column(name = "total_java_code_conventions_rule_number")
    private long totalJavaCodeConventionsRuleNumber;

    @Column(name = "total_cwe_java_rule_number")
    private long totalCweJavaRuleNumber;

    @Column(name = "total_style_cop_number")
    private long totalStyleCopRuleNumber;

    @Column(name = "total_fx_cop_rule_number")
    private long totalFxCopRuleNumber;

    @Column(name = "initial_release_date")
    private LocalDate initialReleaseDate;

    @Column(name = "last_release_date")
    private LocalDate lastReleaseDate;

    @Column(name = "developer_email")
    private String developerEmail;

    @Builder
    public Setting(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                   long totalMisraCRuleNumber,
                   long totalMisraCppRuleNumber,
                   long totalCweRuleNumber,
                   long totalJavaCodeConventionsRuleNumber,
                   long totalCweJavaRuleNumber,
                   long totalStyleCopRuleNumber,
                   long totalFxCopRuleNumber,
                   LocalDate initialReleaseDate,
                   LocalDate lastReleaseDate,
                   String developerEmail) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.totalMisraCRuleNumber = totalMisraCRuleNumber;
        this.totalMisraCppRuleNumber = totalMisraCppRuleNumber;
        this.totalCweRuleNumber = totalCweRuleNumber;
        this.totalJavaCodeConventionsRuleNumber = totalJavaCodeConventionsRuleNumber;
        this.totalCweJavaRuleNumber = totalCweJavaRuleNumber;
        this.totalStyleCopRuleNumber = totalStyleCopRuleNumber;
        this.totalFxCopRuleNumber = totalFxCopRuleNumber;
        this.initialReleaseDate = initialReleaseDate;
        this.lastReleaseDate = lastReleaseDate;
        this.developerEmail = developerEmail;
    }

    public void update(Setting setting) {
        setActiveStatus(setting.getActiveStatus());
        this.totalMisraCRuleNumber = setting.getTotalMisraCRuleNumber();
        this.totalMisraCppRuleNumber = setting.getTotalMisraCppRuleNumber();
        this.totalCweRuleNumber = setting.getTotalCweRuleNumber();
        this.totalJavaCodeConventionsRuleNumber = setting.getTotalJavaCodeConventionsRuleNumber();
        this.totalCweJavaRuleNumber = setting.getTotalCweJavaRuleNumber();
        this.totalStyleCopRuleNumber = setting.getTotalStyleCopRuleNumber();
        this.totalFxCopRuleNumber = setting.getTotalFxCopRuleNumber();
        this.initialReleaseDate = setting.getInitialReleaseDate();
        this.lastReleaseDate = setting.getLastReleaseDate();
        this.developerEmail = setting.getDeveloperEmail();
    }
}