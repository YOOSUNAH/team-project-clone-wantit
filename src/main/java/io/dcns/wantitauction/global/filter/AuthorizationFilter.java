package io.dcns.wantitauction.global.filter;

import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.global.impl.UserDetailsServiceImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.RefreshTokenRepository;
import io.dcns.wantitauction.global.jwt.TokenState;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 인가")
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(request);

        if (tokenValue == null) { // null 또는 empty인 경우
            filterChain.doFilter(request, response);
            return;
        }

        handleTokenValidation(tokenValue, response);
        filterChain.doFilter(request, response);
    }

    private void handleTokenValidation(String tokenValue, HttpServletResponse response) {
        TokenState state = jwtUtil.validateToken(tokenValue);

        // Access 토큰이 불일치인 경우
        if (state.equals(TokenState.INVALID)) {
            log.error("Token Error");
        }
        // Access 토큰이 만료된 경우
        else if (state.equals(TokenState.EXPIRED)) {
            handleExpiredToken(tokenValue, response);
        }
        // Access 토큰이 유효한 경우
        else {
            handleValidToken(tokenValue);
        }
    }

    private void handleExpiredToken(String tokenValue, HttpServletResponse response) {
        try {
            // 만료된 AccessToken으로 부터 정보 가지고 오기
            Claims info = jwtUtil.getUserInfoFromExpiredToken(tokenValue);

            /*
            repository(redis)에 같은 userId를 가진 RefresthToken이 있다면 = 유효하다면 , AccessToken을 재발급한다.
            (만료기간이 끝나면 redis에서 사라지기 떄문에 존재하면 만료기간이 끝나지 않았다는 뜻)
            */
            if (refreshTokenRepository.existsByUserId(info.get("userId", Long.class))) {
                Long userId = Long.parseLong(info.getSubject());
                UserRoleEnum role = info.get(JwtUtil.AUTHORIZATION_KEY)
                    .equals("ROLE_ADMIN") ? UserRoleEnum.ADMIN : UserRoleEnum.USER;

                String newToken = jwtUtil.regenerateAccessToken(userId, role);
                response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newToken);
                response.setStatus(HttpServletResponse.SC_OK);
                log.info("새로운 Acces Token이 발급되었습니다.");

            } else {
                /*
                repository(redis)에 같은 userId를 가진 RefresthToken이 없다면 = 유효하지 않다면
                (만료기간이 끝나면 redis에서 사라지기 떄문에 존재하지 않다면 만료기간이 끝났다는 뜻)
                 */
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 토큰의 상태를 바꿔준다.
                log.info("Acces Token, Refresh Token 모두 만료되었습니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handleValidToken(String tokenValue) {
        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

        Long userId = Long.parseLong(info.getSubject());
        UserRoleEnum role = info.get(JwtUtil.AUTHORIZATION_KEY)
            .equals("ROLE_ADMIN") ? UserRoleEnum.ADMIN : UserRoleEnum.USER;

        setAuthentication(userId, role);
    }

    // 인증 처리
    public void setAuthentication(Long userId, UserRoleEnum role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId, role);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(Long userId, UserRoleEnum role) {
        UserDetails userDetails = userDetailsService.getUserDetails(userId, role);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}

