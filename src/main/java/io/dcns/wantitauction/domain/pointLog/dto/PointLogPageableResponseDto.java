package io.dcns.wantitauction.domain.pointLog.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class PointLogPageableResponseDto {
    List<PointLogResponseDto> pointLogResponseDtoList;
    private final int pageSize;
    private final int currentPage;
    private final int totalPage;

    public PointLogPageableResponseDto(
            List<PointLogResponseDto> pointLogResponseDtoList,
            int size,
            int page,
            int totalPage) {
        this.pointLogResponseDtoList = pointLogResponseDtoList;
        this.pageSize = size;
        this.currentPage = page;
        this.totalPage = totalPage;
    }
}
