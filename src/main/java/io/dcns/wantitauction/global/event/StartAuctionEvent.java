package io.dcns.wantitauction.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StartAuctionEvent {

    private final Long auctionItemId;
}
