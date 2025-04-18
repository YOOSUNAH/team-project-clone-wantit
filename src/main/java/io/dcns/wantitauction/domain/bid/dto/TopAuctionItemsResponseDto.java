package io.dcns.wantitauction.domain.bid.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.CategoryEnum;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopAuctionItemsResponseDto {
    private Long auctionItemId;
    private String itemName;
    private String itemDescription;
    private String imageUrl;
    private CategoryEnum category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
