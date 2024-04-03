package io.dcns.wantitauction.domain.bid.dto;

import io.dcns.wantitauction.domain.bid.entity.Bid;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BidResponseDto {

    private final Long bidId;

    private final Long auctionItemId;

    private final Long userId;

    private final Long bidPrice;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public BidResponseDto(Bid bid) {
        this.bidId = bid.getBidId();
        this.auctionItemId = bid.getAuctionItem().getAuctionItemId();
        this.userId = bid.getUserId();
        this.bidPrice = bid.getBidPrice();
        this.createdAt = bid.getCreatedAt();
        this.updatedAt = bid.getUpdatedAt();
    }
}
