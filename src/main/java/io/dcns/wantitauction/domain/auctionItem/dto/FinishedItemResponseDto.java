package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class FinishedItemResponseDto {

    private final Long auctionItemId;
    private final Long userId;
    private final Long winnerId;
    private final String itemName;
    private final String productDescription;
    private final Long minPrice;
    private final Long winPrice;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final AuctionItemEnum status = AuctionItemEnum.FINISHED;

    public FinishedItemResponseDto(AuctionItem auctionItem) {
        this.auctionItemId = auctionItem.getAuctionItemId();
        this.userId = auctionItem.getUserId();
        this.winnerId = auctionItem.getWinnerId();
        this.itemName = auctionItem.getItemName();
        this.productDescription = auctionItem.getProductDescription();
        this.minPrice = auctionItem.getMinPrice();
        this.winPrice = auctionItem.getWinPrice();
        this.startDate = auctionItem.getStartDate();
        this.endDate = auctionItem.getEndDate();
    }
}
