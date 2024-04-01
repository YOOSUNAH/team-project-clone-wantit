package io.dcns.wantitauction.domain.auctionItem.service;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MyAuctionItemService {

    private final AuctionItemRepository auctionItemRepository;

    public void createProduct(CreateProductRequestDto request, User user) {
        auctionItemRepository.save(new AuctionItem(request, user));
    }

    @Transactional(readOnly = true)
    public List<MyAuctionItemsResponseDto> getAuctionItems(Long userId) {

        List<AuctionItem> auctionItems = auctionItemRepository.findAllByUserId(userId);
        return auctionItems.stream()
            .map(MyAuctionItemsResponseDto::new)
            .toList();
    }

    @Transactional(readOnly = true)
    public MyAuctionItemsResponseDto getAuctionItem(Long auctionItemId, Long userId) {

        AuctionItem auctionItem = getItem(auctionItemId, userId);
        return new MyAuctionItemsResponseDto(auctionItem);
    }

    public MyAuctionItemsResponseDto updateAuctionItem(
        UpdateMyItemRequestDto request, Long auctionItemId, Long userId
    ) {
        AuctionItem auctionItem = getItem(auctionItemId, userId);
        auctionItem.update(request);
        return new MyAuctionItemsResponseDto(auctionItem);
    }

    private AuctionItem getItem(Long auctionItemId, Long userId) {
        return auctionItemRepository.findByAuctionItemIdAndUserId(auctionItemId, userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 경매 상품이 존재하지 않습니다."));
    }
}
