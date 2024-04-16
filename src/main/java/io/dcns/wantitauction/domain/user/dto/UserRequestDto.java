package io.dcns.wantitauction.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRequestDto {

    private String nickname;
    private String phoneNumber;
    private String address;

}
