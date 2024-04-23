package io.dcns.wantitauction.domain.user.dto;

import lombok.Builder;

@Builder
public record UserUpdateResponseDto(String nickname, String phoneNumber, String address) {

}

