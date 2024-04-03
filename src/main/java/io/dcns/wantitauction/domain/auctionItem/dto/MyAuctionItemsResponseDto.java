package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MyAuctionItemsResponseDto {

    private final Long auctionItemId;
    private final Long userId;
    private final String itemName;
    private final String productDescription;
    private final Long minPrice;
    private final Long winPrice;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final AuctionItemEnum status;

    public MyAuctionItemsResponseDto(AuctionItem auctionItems) {
        this.auctionItemId = auctionItems.getAuctionItemId();
        this.userId = auctionItems.getUserId();
        this.itemName = auctionItems.getItemName();
        this.productDescription = auctionItems.getProductDescription();
        this.minPrice = auctionItems.getMinPrice();
        this.winPrice = auctionItems.getWinPrice();
        this.startDate = auctionItems.getStartDate();
        this.endDate = auctionItems.getEndDate();
        this.status = auctionItems.getStatus();
    }
}
