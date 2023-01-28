package com.suresoft.sw_test_forum.admin_page.user.dto;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.UserStatus;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserPrincipal implements UserDetails {
    /* CommonDto */
    private long idx;

    private ActiveStatus activeStatus;


    /* 필수 정보 */
    @Getter(value = AccessLevel.NONE)
    private String username;

    @Getter(value = AccessLevel.NONE)
    private String password;

    private String name;

    private String department;

    private Position position;

    private UserStatus userStatus;


    /* 기타 정보 */
    private AuthorityType authorityType;

    private long contribution;

    public UserPrincipal(User user) {
        setIdx(user.getIdx());
        setActiveStatus(user.getActiveStatus());
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setName(user.getName());
        setDepartment(user.getDepartment());
        setPosition(user.getPosition());
        setUserStatus(user.getUserStatus());
        setAuthorityType(user.getAuthorityType());
        setContribution(user.getContribution());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(this.getAuthorityType().getAuthorityType()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getAuthorityType() != AuthorityType.NON_USER;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getActiveStatus() == ActiveStatus.ACTIVE;
    }
}