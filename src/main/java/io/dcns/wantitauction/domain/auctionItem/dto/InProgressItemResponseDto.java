package io.dcns.wantitauction.domain.auctionItem.dto;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.auctionItem.entity.CategoryEnum;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InProgressItemResponseDto {
    private Long auctionItemId;
    private Long userId;
    private Long winnerId;
    private String itemName;
    private String itemDescription;
    private CategoryEnum category;
    private Long minPrice;
    private Long winPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AuctionItemEnum status = AuctionItemEnum.IN_PROGRESS;
    private String imageUrl;
}
