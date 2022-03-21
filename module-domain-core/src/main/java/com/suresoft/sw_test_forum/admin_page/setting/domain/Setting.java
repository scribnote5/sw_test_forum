package com.suresoft.sw_test_forum.admin_page.setting.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    private String developerEmail;

    @Builder
    public Setting(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                   long totalMisraCRuleNumber,
                   long totalMisraCppRuleNumber,
                   long totalCweRuleNumber,
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
        this.developerEmail = developerEmail;
    }

    public void update(Setting setting) {
        setActiveStatus(setting.getActiveStatus());
        this.totalMisraCRuleNumber = setting.getTotalMisraCRuleNumber();
        this.totalMisraCppRuleNumber = setting.getTotalMisraCppRuleNumber();
        this.totalCweRuleNumber = setting.getTotalCweRuleNumber();
        this.developerEmail = setting.getDeveloperEmail();
    }
}