package io.dcns.wantitauction.domain.bid.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.CategoryEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopAuctionItemsResponseDto {

    private Long auctionItemId;
    private String itemName;
    private String itemDescription;
    private CategoryEnum category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TopAuctionItemsResponseDto(AuctionItem auctionItem) {
        this.auctionItemId = auctionItem.getAuctionItemId();
        this.itemName = auctionItem.getItemName();
        this.itemDescription = auctionItem.getItemDescription();
        this.category = auctionItem.getCategory();
        this.startDate = auctionItem.getStartDate();
        this.endDate = auctionItem.getEndDate();
    }
}
