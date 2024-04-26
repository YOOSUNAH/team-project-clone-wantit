package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.auctionItem.entity.CategoryEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyAuctionItemsResponseDto {

    private Long auctionItemId;
    private Long userId;
    private String itemName;
    private String itemDescription;
    private CategoryEnum category;
    private Long minPrice;
    private Long winPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AuctionItemEnum status;
    private String imageUrl;

    public MyAuctionItemsResponseDto(AuctionItem auctionItems) {
        this.auctionItemId = auctionItems.getAuctionItemId();
        this.userId = auctionItems.getUserId();
        this.itemName = auctionItems.getItemName();
        this.itemDescription = auctionItems.getItemDescription();
        this.category = auctionItems.getCategory();
        this.minPrice = auctionItems.getMinPrice();
        this.winPrice = auctionItems.getWinPrice();
        this.startDate = auctionItems.getStartDate();
        this.endDate = auctionItems.getEndDate();
        this.status = auctionItems.getStatus();
        this.imageUrl = auctionItems.getImageUrl();
    }
}
