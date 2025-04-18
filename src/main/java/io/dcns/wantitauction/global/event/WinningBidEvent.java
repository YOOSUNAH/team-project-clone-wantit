package io.dcns.wantitauction.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WinningBidEvent {
    private final Long auctionItemId;
    private final String itemName;
    private final Long winnerId;
    private final Long winPrice;
}
