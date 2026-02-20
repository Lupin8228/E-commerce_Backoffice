package com.ecommerce.backoffice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * CSRF(Cross-Site Request Forgery) 비활성화 (테스트 시 편리함)
         Stateless API: 보통 현대의 백엔드는 세션을 유지하지 않는 Stateless(무상태) 방식(예: JWT)을 많이 씁니다. CSRF 공격은 "브라우저가 쿠키를 자동으로 보낸다"는 점을 이용하는데, 쿠키를 아예 안 쓰거나 LocalStorage에 토큰을 저장하면 이 공격 자체가 성립하기 어렵습니다.
         Non-Browser Client: API 서버는 브라우저뿐만 아니라 앱(iOS/Android), 다른 서버 등 다양한 클라이언트가 호출합니다. 이들은 브라우저처럼 쿠키를 관리하지 않으므로 CSRF 방어가 불필요한 경우가 많습니다.
         Postman 테스트 불편함: CSRF가 켜져 있으면 모든 POST, PUT, DELETE 요청 시 서버가 발행한 CSRF 토큰을 헤더에 담아 보내야 합니다. 테스트 단계에서는 매우 번거로운 작업이죠.
         */

        http
                // csrf 활성화
                .csrf(csrf -> csrf.disable())

                // HTTP 기본 인증과 폼 로그인을 모두 끕니다.
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // 경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                );

        return http.build();
    }
}