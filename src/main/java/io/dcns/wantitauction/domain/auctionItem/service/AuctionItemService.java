package io.dcns.wantitauction.domain.auctionItem.service;

import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;

    public AuctionItemResponseDto getProduct(Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 경매상품 입니다.")
        );

        return new AuctionItemResponseDto(auctionItem);
    }
}
