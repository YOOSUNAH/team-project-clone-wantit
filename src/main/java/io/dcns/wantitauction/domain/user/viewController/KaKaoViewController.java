package io.dcns.wantitauction.domain.user.viewController;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.service.KakaoService;
import io.dcns.wantitauction.domain.user.service.UserService;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@AllArgsConstructor
public class KaKaoViewController {

    private final UserService userService;
    private final KakaoService kaoKaoService;

    @GetMapping("/v1/users/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/v1/users/signup-page")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/v1/users/signup-page")
    public String signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                log.error(error.getDefaultMessage());
            });
        }
        userService.signup(requestDto);
        return "redirect:/v1/users/login-page";
    }

    @GetMapping("/v1/users/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response)
        throws JsonProcessingException {
        log.info("controller code : " + code);
        String token = kaoKaoService.kakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

