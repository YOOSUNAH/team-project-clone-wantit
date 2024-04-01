package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateProductRequestDto {

    private Long userId;
    private String itemName;
    private String productDescription;
    private Long minPrice;
    private Long maxPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final AuctionItemEnum status = AuctionItemEnum.READY;

}
