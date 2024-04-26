package io.dcns.wantitauction.domain.bid.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class BidPageableResponseDto {

    List<BidResponseDto> bidResponseDtoList;
    private final int pageSize;
    private final int currentPage;
    private final int totalPage;

    public BidPageableResponseDto(
        List<BidResponseDto> bidResponseDtoList,
        int pageSize,
        int currentPage,
        int totalPage
    ) {
        this.bidResponseDtoList = bidResponseDtoList;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }
}
