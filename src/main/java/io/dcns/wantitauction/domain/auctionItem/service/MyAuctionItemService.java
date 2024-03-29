package io.dcns.wantitauction.domain.auctionItem.service;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyAuctionItemService {

    AuctionItemRepository auctionItemRepository;

    public CreateProductResponseDto createProduct(CreateProductRequestDto request, User user) {
        AuctionItem auctionItem = auctionItemRepository.save(new AuctionItem(request, user));
        return new CreateProductResponseDto(auctionItem);
    }
}
