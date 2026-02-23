package com.ecommerce.backoffice.global.security;


import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.global.error.CommonError;
import com.ecommerce.backoffice.global.error.CommonException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    // JWT 서명에 사용할 비밀키
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    // 만료 시간 설정 (단위 : sec)
    private final long TOKEN_TIME = 24 * 60 * 60 * 1000L;

    @PostConstruct
    public void init() {
        // 비밀키를 Base64 디코딩하여 HMAC-SHA 알고리즘에 적합한 Key 객체로 변환
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * [JWT 토큰 생성]
     * Payload 구성: ID(Subject), Email, Role, 발급시간, 만료시간
     */
    public String createToken(Long adminId, String email, AdminRole role) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(adminId))
                .claim("email", email)
                .claim("role", role.name())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * [JWT 토큰 검증]
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature");
            throw new CommonException(CommonError.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw new CommonException(CommonError.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw new CommonException(CommonError.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty");
            throw new CommonException(CommonError.EMPTY_TOKEN);
        }

    }

    /**
     * [JWT 토큰에서 정보 추출]
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}