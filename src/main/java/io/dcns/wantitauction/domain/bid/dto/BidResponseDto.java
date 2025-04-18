package io.dcns.wantitauction.domain.bid.dto;

import io.dcns.wantitauction.domain.bid.entity.Bid;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidResponseDto {
    private Long bidId;
    private Long auctionItemId;
    private String itemName;
    private String imageUrl;
    private Long userId;
    private Long bidPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BidResponseDto(Bid bid) {
        this.bidId = bid.getBidId();
        this.auctionItemId = bid.getAuctionItem().getAuctionItemId();
        this.itemName = bid.getAuctionItem().getItemName();
        this.imageUrl = bid.getAuctionItem().getImageUrl();
        this.userId = bid.getUserId();
        this.bidPrice = bid.getBidPrice();
        this.createdAt = bid.getCreatedAt();
        this.updatedAt = bid.getUpdatedAt();
    }
}
