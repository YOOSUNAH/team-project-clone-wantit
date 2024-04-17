package io.dcns.wantitauction.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserRequestDto {

    private String nickname;
    private String phoneNumber;
    private String address;

}
