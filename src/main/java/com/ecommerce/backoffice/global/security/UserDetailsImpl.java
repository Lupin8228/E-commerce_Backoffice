package com.ecommerce.backoffice.global.security;


import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final Admin admin;

    public UserDetailsImpl(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        AdminRole role = admin.getRole();

        // 접두산 확인안함 - @PreAuthorize("hasAuthority('SUPER_ADMIN')"
        String authority = role.name();

        // 접두사 'ROLE_' 확인 - @PreAuthorize("hasRole('SUPER_ADMIN')"
//        String authority = "ROLE_" + role.name(); // 스프링 시큐리티에서 내부적으로 권한 목록을 뒤질때 "ROLE_" 접두사를 확인

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() { return admin.getPassword(); }

    @Override
    public String getUsername() { return admin.getEmail(); }

    // 실제 운영 시 상태값에 따라 조절
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}