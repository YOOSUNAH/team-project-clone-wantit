package io.dcns.wantitauction.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsServiceImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.TokenState;
import io.dcns.wantitauction.global.jwt.entity.RefreshTokenEntity;
import io.dcns.wantitauction.global.jwt.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 인가")
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getAccessTokenFromRequest(httpServletRequest);
        if (!StringUtils.hasText(tokenValue)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        TokenState state = jwtUtil.isValidateToken(tokenValue);

        // 토큰이 불일치인 경우
        if (state.equals(TokenState.INVALID)) {
            log.error("Token Error");
            return;
        }

        // 토큰이 만료된 경우
        if (state.equals(TokenState.EXPIRED)) {
            refreshTokenAndHandleException(tokenValue, httpServletResponse);
            return;
        }

        Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    // 인증 처리
    public void setAuthentication(String userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.getUser(Long.parseLong(userId));
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }

    // 토큰을 재발급 및 예외처리하는 메서드
    private void refreshTokenAndHandleException(String tokenValue, HttpServletResponse response) {
        try {
            Claims info = jwtUtil.getUserInfoFromExpiredToken(tokenValue);
            Long userId = Long.parseLong(info.getSubject());
            RefreshTokenEntity refreshToken = tokenRepository.findByUserId(userId);
            TokenState refreshState = jwtUtil.isValidateToken(refreshToken.getToken());

            if (refreshState.equals(TokenState.VALID)) {
                refreshAccessToken(response, refreshToken);
            } else {
                handleExpiredToken(response, refreshToken);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handleExpiredToken(HttpServletResponse response, RefreshTokenEntity refreshToken)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String jsonResponse = objectMapper.writeValueAsString(
            ResponseDto.of(HttpStatus.UNAUTHORIZED, "모든 토큰이 만료되었습니다."));
        tokenRepository.deleteToken(refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    private void refreshAccessToken(HttpServletResponse response, RefreshTokenEntity refreshToken)
        throws IOException {
        String newToken = refreshToken.getToken();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newToken);
        response.setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = objectMapper.writeValueAsString(
            ResponseDto.of(HttpStatus.OK, "성공적으로 토큰을 발급하였습니다."));
        tokenRepository.deleteToken(refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}

