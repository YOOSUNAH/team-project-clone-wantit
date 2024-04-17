package io.dcns.wantitauction.domain.user.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(String nickname, String phoneNumber, String address) {

}

