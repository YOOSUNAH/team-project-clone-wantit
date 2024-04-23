package io.dcns.wantitauction.domain.user.dto;

public record UserResponseDto(
    Long userId,
    String email,
    String password,
    String username,
    String nickname,
    String phoneNumber,
    String address,
    Long kakaoId
){}
