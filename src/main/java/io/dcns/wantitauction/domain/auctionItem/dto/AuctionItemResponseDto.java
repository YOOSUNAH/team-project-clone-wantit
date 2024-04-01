package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AuctionItemResponseDto {

    private final Long auctionItemId;
    private final Long userId;
    private final String itemName;
    private final String productDescription;
    private final Long minPrice;
    private final Long maxPrice;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final AuctionItemEnum status;

    public AuctionItemResponseDto(AuctionItem auctionItem) {
        this.auctionItemId = auctionItem.getAuctionItemId();
        this.userId = auctionItem.getUserId();
        this.itemName = auctionItem.getItemName();
        this.productDescription = auctionItem.getProductDescription();
        this.minPrice = auctionItem.getMinPrice();
        this.maxPrice = auctionItem.getMaxPrice();
        this.startDate = auctionItem.getStartDate();
        this.endDate = auctionItem.getEndDate();
        this.status = auctionItem.getStatus();
    }
}
