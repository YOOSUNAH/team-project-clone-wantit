package io.dcns.wantitauction.domain.like.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeAuctionItemResponseDto {

    private Long auctionItemId;
    private String itemName;
    private String itemDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String imageUrl;

    public LikeAuctionItemResponseDto(
        Long auctionItemId, String itemName, String itemDescription, LocalDateTime startDate, LocalDateTime endDate, String imageUrl) {
       this.auctionItemId = auctionItemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrl = imageUrl;
    }
}
