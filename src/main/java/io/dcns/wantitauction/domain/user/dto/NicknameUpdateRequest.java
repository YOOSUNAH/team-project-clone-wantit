package io.dcns.wantitauction.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class NicknameUpdateRequest {

    @NotBlank
    private String newNickname;
}

