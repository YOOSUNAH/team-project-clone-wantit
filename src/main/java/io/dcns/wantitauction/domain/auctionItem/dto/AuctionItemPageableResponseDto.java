package io.dcns.wantitauction.domain.auctionItem.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class AuctionItemPageableResponseDto {

    private final List<AuctionItemResponseDto> responseDtoList;
    private final int pageSize;
    private final int currentPage;
    private final int totalPage;
}
