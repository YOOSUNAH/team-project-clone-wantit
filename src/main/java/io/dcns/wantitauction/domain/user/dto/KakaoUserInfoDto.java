package io.dcns.wantitauction.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long kakaoId;
    private String nickname;
    private String email;

    public KakaoUserInfoDto(
            Long kakaoId,
            String nickname,
            String email) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
    }
}
