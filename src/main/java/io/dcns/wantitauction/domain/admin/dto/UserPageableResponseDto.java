package io.dcns.wantitauction.domain.admin.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPageableResponseDto {


    private final List<UsersResponseDto> responseDtoList;
    private final int pageSize;
    private final int currentPage;
    private final int totalPage;

}
