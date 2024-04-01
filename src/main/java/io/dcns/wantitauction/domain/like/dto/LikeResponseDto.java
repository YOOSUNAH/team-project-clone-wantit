package io.dcns.wantitauction.domain.like.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private Long userId;
    private Long auctionItemId;
    private boolean liked;

    public LikeResponseDto(Long userId, Long auctionItemId, boolean liked) {
        this.userId = userId;
        this.auctionItemId = auctionItemId;
        this.liked = liked;
    }
}
