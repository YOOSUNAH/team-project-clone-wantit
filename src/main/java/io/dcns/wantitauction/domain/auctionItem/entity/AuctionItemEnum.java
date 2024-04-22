package io.dcns.wantitauction.domain.auctionItem.entity;

public enum AuctionItemEnum {
    READY,
    IN_PROGRESS,
    FINISHED,
    FAILED,
    CANCELED, // User 취소 시
    BANNED //관리자 권한 차단 시
}
