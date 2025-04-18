package io.dcns.wantitauction.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TopBidChangeEvent {
    private final Long auctionItemId;
    private final Long bidPrice;
}
