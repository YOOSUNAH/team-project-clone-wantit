package io.dcns.wantitauction.domain.auctionItem.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuctionItemPageableResponseDto {

    private final List<AuctionItemResponseDto> responseDtoList;
    private final int pageSize;
    private final int currentPage;
    private final int totalPage;
}
