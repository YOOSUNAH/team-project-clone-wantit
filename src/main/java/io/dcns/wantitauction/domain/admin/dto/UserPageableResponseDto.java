package io.dcns.wantitauction.domain.admin.dto;


import java.util.List;


public record UserPageableResponseDto (
    List<UsersResponseDto> responseDtoList,
    int pageSize,
        int currentPage,
        int totalPage
){}
