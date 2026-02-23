package com.ecommerce.backoffice.global.security;


import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
        // 토큰의 Subject에 저장했던 adminId로 DB 조회
        Admin admin = adminRepository.findById(Long.parseLong(adminId))
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Admin ID: " + adminId));

        // 시큐리티가 이해할 수 있는 형태(UserDetailsImpl)로 변환해서 반환
        return new UserDetailsImpl(admin);
    }
}