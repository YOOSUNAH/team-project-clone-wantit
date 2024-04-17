package io.dcns.wantitauction.domain.admin.dto;


import java.util.List;
import lombok.Builder;

@Builder
public record UserPageableResponseDto (
    List<UsersResponseDto> responseDtoList,
    int pageSize,
        int currentPage,
        int totalPage
){}
