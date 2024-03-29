package io.dcns.wantitauction.global.jwt;

import static io.dcns.wantitauction.global.jwt.TokenState.EXPIRED;
import static io.dcns.wantitauction.global.jwt.TokenState.INVALID;
import static io.dcns.wantitauction.global.jwt.TokenState.VALID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JWT 토큰")
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Integer BEARER_PREFIX_LENGTH = 7;
    private static final Long ACCESS_TOKEN_VALID_TIME = (60 * 1000L) * 20;
    private static final Long REFRESH_TOKEN_VALID_TIME = (60 * 1000L) * 60 * 24;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    // 삭제할지 말지
//    private final Set<String> tokenBlackList = new HashSet<>();

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(final Long userId, final String role){
        return generateToken(String.valueOf(userId), role, ACCESS_TOKEN_VALID_TIME);
    }

    @Transactional
    public String generateRefreshToken(final Long userId, final String role){
        return generateRefreshToken(String.valueOf(userId), role, REFRESH_TOKEN_VALID_TIME);
    }

    public String generateToken(final String id, final String role, Long time) {
        Date date = new Date();
        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(id)
                .claim(AUTHORIZATION_KEY, "user")
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();
    }

    public String generateRefreshToken(final String id, final String role, Long time){
        Date date = new Date();
        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(id)
                .claim(AUTHORIZATION_KEY, "user")
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, SIGNATURE_ALGORITHM)
                .compact();
    }

    public String substringToken(final String tokenValue){
        if(!StringUtils.hasText(tokenValue) || !tokenValue.startsWith(BEARER_PREFIX)){
            throw new IllegalArgumentException("해당 토큰에 접근할 수 없습니다."); // Todo: CustionException 하기
        }
        return tokenValue.substring(BEARER_PREFIX_LENGTH);
    }

    public String getAccessTokenFromRequest(HttpServletRequest httpServletRequest) {
            return getTokenFromRequest(httpServletRequest);
    }

    public TokenState isValidateToken(String token) {
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

    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    // 만료된 토큰으로부터 정보를 가져오기
    public Claims getUserInfoFromExpiredToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    private String getTokenFromRequest(final HttpServletRequest httpServletRequest){
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(BEARER_PREFIX_LENGTH);
        }
        return null;
    }

}
