package io.dcns.wantitauction.domain.bid.dto;

import io.dcns.wantitauction.domain.bid.entity.Bid;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopBidResponseDto {

    private Long bidId;
    private Long auctionItemId;
    private Long bidPrice;

    public TopBidResponseDto(Long bidId, Long auctionItemId, Long bidPrice) {
        this.bidId = bidId;
        this.auctionItemId = auctionItemId;
        this.bidPrice = bidPrice;
    }

    public TopBidResponseDto(Bid bid) {
        this.bidId = bid.getBidId();
        this.auctionItemId = bid.getAuctionItem().getAuctionItemId();
        this.bidPrice = bid.getBidPrice();
    }
}
