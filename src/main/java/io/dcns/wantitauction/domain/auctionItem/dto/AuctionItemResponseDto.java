package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuctionItemResponseDto {

    private Long auctionItemId;
    private Long userId;
    private Long winnerId;
    private String itemName;
    private String itemDescription;
    private Long minPrice;
    private Long winPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AuctionItemEnum status;

    public AuctionItemResponseDto(AuctionItem auctionItem) {
        this.auctionItemId = auctionItem.getAuctionItemId();
        this.userId = auctionItem.getUserId();
        this.winnerId = auctionItem.getWinnerId();
        this.itemName = auctionItem.getItemName();
        this.itemDescription = auctionItem.getItemDescription();
        this.minPrice = auctionItem.getMinPrice();
        this.winPrice = auctionItem.getWinPrice();
        this.startDate = auctionItem.getStartDate();
        this.endDate = auctionItem.getEndDate();
        this.status = auctionItem.getStatus();
    }
}
