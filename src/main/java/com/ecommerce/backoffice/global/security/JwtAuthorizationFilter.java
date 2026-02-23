package com.ecommerce.backoffice.global.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (StringUtils.hasText(token)) {
            if (!jwtProvider.validateToken(token)) {
                log.error("Token Error: 토큰 검증 실패");
                filterChain.doFilter(request, response);
                return;
            }

            // 3. 토큰에서 유저 정보(Claims) 추출
            Claims info = jwtProvider.getUserInfoFromToken(token);
            log.info("JWT 인증 성공: {}", info.getSubject());

            // TODO: 여기서 Spring Security의 Authentication 객체를 생성해 Context에 저장하는 로직이 추가될 예정입니다.
            // ★ 여기가 핵심입니다! 직원을 불러서 명찰을 만들고 시큐리티 보관함에 넣습니다.

            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(info.getSubject());
            } catch (UsernameNotFoundException e) {
                throw new RuntimeException(e);
            }

            // 시큐리티 전용 인증 토큰(명찰) 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            // 보관함(SecurityContext)에 명찰 저장 -> 이제 로그인된 상태로 인정됨!
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}