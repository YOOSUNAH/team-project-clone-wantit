package io.dcns.wantitauction.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WinningBidEvent {

    private final Long auctionItemId;
    private final Long winnerId;
    private final Long winPrice;
}
