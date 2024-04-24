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
        int size,
        int page,
        int totalPage
    ) {
        this.bidResponseDtoList = bidResponseDtoList;
        this.pageSize = size;
        this.currentPage = page;
        this.totalPage = totalPage;
    }
}
