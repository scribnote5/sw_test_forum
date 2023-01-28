package com.suresoft.sw_test_forum.admin_page.user.domain;

import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.UserStatus;
import com.suresoft.sw_test_forum.admin_page.user.listener.UserListener;
import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user")
@EntityListeners(UserListener.class)
public class User extends CommonAudit {
    /* 필수 정보 */
    private String username;

    private String password;

    private String name;

    private String department;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorityType;

    /* 연락처 */
    private String contact;

    private String email;

    private String privateEmail;

    /* 자기 소개 */
    private String introduction;

    /* Additional Information */
    private long contribution = 0;

    @Builder
    public User(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                String username,
                String password,
                String name,
                String department,
                Position position,
                UserStatus userStatus,
                AuthorityType authorityType,
                String contact,
                String email,
                String privateEmail,
                String introduction,
                long contribution) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.username = username;
        this.password = password;
        this.name = name;
        this.department = department;
        this.position = position;
        this.userStatus = userStatus;
        this.authorityType = authorityType;
        this.contact = contact;
        this.email = email;
        this.privateEmail = privateEmail;
        this.introduction = introduction;
        this.contribution = contribution;
    }

    public void update(User user, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 비밀번호가 변경된 경우 -> 비밀번호 변경
        if (!passwordEncoder.matches(user.getPassword(), password) && !"".equals(user.getPassword())) {
            this.password = passwordEncoder.encode(user.getPassword());
        }

        setActiveStatus(user.getActiveStatus());
        this.username = user.getUsername();
        this.name = user.getName();
        this.department = user.getDepartment();
        this.position = user.getPosition();
        this.userStatus = user.getUserStatus();
        this.authorityType = user.getAuthorityType();
        this.contact = user.getContact();
        this.email = user.getEmail();
        this.privateEmail = user.getPrivateEmail();
        this.introduction = user.getIntroduction();
        this.contribution = user.getContribution();
    }
}