package io.dcns.wantitauction.domain.admin.dto;

public record AdminResponseDto (
    Long userId,
    String email,
    String password,
    String username,
    String nickname,
    String phoneNumber,
    String address,
    Long kakaoId
){}


