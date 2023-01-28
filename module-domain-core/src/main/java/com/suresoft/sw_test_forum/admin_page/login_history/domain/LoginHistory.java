package com.suresoft.sw_test_forum.admin_page.login_history.domain;

import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table
public class LoginHistory extends CommonAudit {
    private String loginUsername;

    private String ip;

    private String location;

    private String message;

    private String detailedMessage;

    @Enumerated(EnumType.STRING)
    private LoginResultType loginResultType;

    @Builder
    public LoginHistory(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                        String loginUsername,
                        String ip,
                        String location,
                        String message,
                        String detailedMessage,
                        LoginResultType loginResultType) {

        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.loginUsername = loginUsername;
        this.ip = ip;
        this.location = location;
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.loginResultType = loginResultType;
    }

    public void update(LoginHistory loginHistory) {
        setActiveStatus(loginHistory.getActiveStatus());
        this.loginUsername = loginHistory.getLoginUsername();
        this.ip = loginHistory.getIp();
        this.location = loginHistory.getLocation();
        this.message = loginHistory.getMessage();
        this.detailedMessage = loginHistory.getDetailedMessage();
        this.loginResultType = loginHistory.getLoginResultType();
    }
}