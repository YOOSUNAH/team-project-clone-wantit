package io.dcns.wantitauction.global.config;

import io.dcns.wantitauction.global.filter.AuthorizationFilter;
import io.dcns.wantitauction.global.impl.UserDetailsServiceImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthorizationFilter jwtAuthorizationFilter() {
        return new AuthorizationFilter(jwtUtil, refreshTokenRepository, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll() // resources 접근 허용 설정
                .requestMatchers("/v1/users/signup").permitAll()
                .requestMatchers("/v1/users/login").permitAll()
                //AuctionItem
                .requestMatchers("/v1/auction-items/{auctionItemId}").permitAll()
                .requestMatchers("/v1/auction-items").permitAll()
                .requestMatchers("/v1/auction-items/finished").permitAll()
                .requestMatchers("/v1/auction-items/{auctionItemId}/finished").permitAll()
                .requestMatchers("/v1/auction-items/bids/top3").permitAll()
                .requestMatchers("/v1/auction-items/{auctionItemId}/bids/top").permitAll()
                // 카카오 로그인
                .requestMatchers("/").permitAll()
                .requestMatchers("/v1/users/kakao/callback").permitAll()
                .requestMatchers("/v1/users/login-page").permitAll()
                .requestMatchers("/v1/users/signup-page").permitAll()
                // 이메일 인증
                .requestMatchers("v1/users/emails/authcode").permitAll()
                .requestMatchers("v1/users/emails/authcode/verify").permitAll()
                .requestMatchers("/v1/live-bids/auction-items/{auctionItemId}").permitAll()
                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
