package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductResponseDto {

    private Long auctionId;
    private Long userId;
    private String itemName;
    private String productDescription;
    private Long minPrice;
    private Long maxPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final AuctionItemEnum status = AuctionItemEnum.READY;

    public CreateProductResponseDto(AuctionItem auctionItem) {
        this.auctionId = auctionItem.getAuctionId();
        this.userId = auctionItem.getUserId();
        this.itemName = auctionItem.getItemName();
        this.productDescription = auctionItem.getProductDescription();
        this.minPrice = auctionItem.getMinPrice();
        this.maxPrice = auctionItem.getMaxPrice();
        this.startDate = auctionItem.getStartDate();
        this.endDate = auctionItem.getEndDate();
    }
}
