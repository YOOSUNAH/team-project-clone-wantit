package io.dcns.wantitauction.global.jwt;

import static io.dcns.wantitauction.global.jwt.TokenState.EXPIRED;
import static io.dcns.wantitauction.global.jwt.TokenState.INVALID;
import static io.dcns.wantitauction.global.jwt.TokenState.VALID;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Slf4j(topic = "JWT 토큰")
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final Long REFRESH_TOKEN_VALID_TIME = (60 * 1000L) * 60 * 24;
    private static final Long ACCESS_TOKEN_VALID_TIME = (60 * 1000L) * 30;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Integer BEARER_PREFIX_LENGTH = 7;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessAndRefreshToken(final Long userId,
                                                final UserRoleEnum role) {
        String accessToken = generateAccessToken(userId, role.getAuthority());
        generateRefreshToken(userId, role.getAuthority());
        log.info("새로운 Access Token이 발급되었습니다.");
        return accessToken;
    }

    public String generateAccessToken(final Long userId,
                                      final String role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId.toString())
                        .claim(AUTHORIZATION_KEY, role)  // 사용자 권한
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_VALID_TIME)) // 만료시간
                        .setIssuedAt(date)  // 발급일
                        .signWith(key, SIGNATURE_ALGORITHM) // 암호화 알고리즘
                        .compact();
    }

    @Transactional
    public void generateRefreshToken(final Long userId,
                                     final String role) {
        Date date = new Date();
        String refreshToken = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userId.toString())
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_VALID_TIME))
                        .setIssuedAt(date)
                        .signWith(key, SIGNATURE_ALGORITHM)
                        .compact();

        refreshTokenRepository.save(Long.valueOf(userId), refreshToken, REFRESH_TOKEN_VALID_TIME);
    }

    public String getJwtFromHeader(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX_LENGTH);
        }
        return null;
    }

    public TokenState validateToken(final String token) {
        if (!StringUtils.hasText(token)) {
            return INVALID;
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return VALID;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
            return INVALID;
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT token 입니다.");
            return EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
            return INVALID;
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다.");
            return INVALID;
        }
    }

    // 만료된 토큰으로부터 정보를 가져오기
    public Claims getUserInfoFromExpiredToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return null;
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Claims getUserInfoFromToken(final String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String regenerateAccessToken(final Long userId,
                                        final UserRoleEnum role) {
        ;
        return generateAccessToken(userId, role.getAuthority());

    }
}
