package com.ecommerce.backoffice.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * CSRF(Cross-Site Request Forgery) 비활성화 (테스트 시 편리함)
         Stateless API: 보통 현대의 백엔드는 세션을 유지하지 않는 Stateless(무상태) 방식(예: JWT)을 많이 씁니다. CSRF 공격은 "브라우저가 쿠키를 자동으로 보낸다"는 점을 이용하는데, 쿠키를 아예 안 쓰거나 LocalStorage에 토큰을 저장하면 이 공격 자체가 성립하기 어렵습니다.
         Non-Browser Client: API 서버는 브라우저뿐만 아니라 앱(iOS/Android), 다른 서버 등 다양한 클라이언트가 호출합니다. 이들은 브라우저처럼 쿠키를 관리하지 않으므로 CSRF 방어가 불필요한 경우가 많습니다.
         Postman 테스트 불편함: CSRF가 켜져 있으면 모든 POST, PUT, DELETE 요청 시 서버가 발행한 CSRF 토큰을 헤더에 담아 보내야 합니다. 테스트 단계에서는 매우 번거로운 작업이죠.
         */

        // HTTP CSRF, 기본 인증, 폼 로그인을 모두 OFF
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 세션 사용하지않음 (SessionCreationPolicy.Stateless)
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청 권한 설정 (접두사 필요? hasRole(), hasAnyRole() : hasAuthority(), hasAnyAuthority() )
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 정적 리소스 허용
                .requestMatchers("/api/signup", "/api/login","/error").permitAll() // 가입, 로그인 허용
                // 1. 관리자 관리 API (슈퍼 관리자 전용)
                // /api/admin/admins, /api/admin/{adminId} 등 관리자 관련 모든 경로는 SUPER_ADMIN만
                .requestMatchers("/api/admins/**", "/api/admins/{adminId}/**").hasAuthority("SUPER_ADMIN")

                // 2. 상품 관리 API (슈퍼 관리자, 운영 관리자)
                // 예: /api/products 경로는 두 역할 모두 가능
                .requestMatchers("/api/products/**").hasAnyAuthority("SUPER_ADMIN", "OPERATION_ADMIN")

                // 3. 주문 관리 API (모든 관리자 가능)
                .requestMatchers("/api/orders/**").hasAnyAuthority("SUPER_ADMIN", "OPERATION_ADMIN", "CS_ADMIN")
                .anyRequest().authenticated() // 그 외는 모두 인증 필요
        );

//        http.exceptionHandling(exception -> exception
//                .accessDeniedHandler(accessDeniedHandler)
//        );

        // JWT 필터 배치 (가장 중요!)
        // UsernamePasswordAuthenticationFilter(기본 로그인 필터) 전에 우리 문지기를 세웁니다.
        http.addFilterBefore(
                new JwtAuthorizationFilter(jwtProvider, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}