package io.dcns.wantitauction.domain.auctionItem.service;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyAuctionItemService {

    private final AuctionItemRepository auctionItemRepository;
    private final AuctionItemQueryRepository auctionItemQueryRepository;

    @Transactional
    public void createAuctionItem(CreateProductRequestDto request, User user) {
        auctionItemRepository.save(new AuctionItem(request, user));
    }

    public List<MyAuctionItemsResponseDto> getAuctionItems(Long userId) {
        return auctionItemQueryRepository.findAllMyAuctionItems(userId);
    }

    public MyAuctionItemsResponseDto getAuctionItem(Long auctionItemId, Long userId) {

        AuctionItem auctionItem = getItem(auctionItemId, userId);
        return new MyAuctionItemsResponseDto(auctionItem);
    }

    @Transactional
    public MyAuctionItemsResponseDto updateAuctionItem(
        UpdateMyItemRequestDto request, Long auctionItemId, Long userId
    ) {
        AuctionItem auctionItem = getItem(auctionItemId, userId);
        auctionItem.update(request);
        return new MyAuctionItemsResponseDto(auctionItem);
    }

    public void deleteAuctionItem(Long auctionItemId, Long userId) {

        AuctionItem auctionItem = getItem(auctionItemId, userId);
        auctionItemRepository.deleteById(auctionItem.getAuctionItemId());
    }

    public List<FinishedItemResponseDto> getWinningAuctionItems(Long userId) {

        return auctionItemQueryRepository.findWinningAuctionItems(userId);
    }

    public FinishedItemResponseDto getWinningAuctionItem(Long auctionItemId, Long userId) {

        validateItem(auctionItemId, userId);
        return auctionItemQueryRepository.findWinningAuctionItem(auctionItemId, userId);
    }

    private void validateItem(Long auctionItemId, Long userId) {
        if (!auctionItemRepository.existsByAuctionItemIdAndUserId(auctionItemId, userId)) {
            throw new EntityNotFoundException("해당 경매 상품이 존재하지 않습니다.");
        }
    }

    private AuctionItem getItem(Long auctionItemId, Long userId) {
        return auctionItemRepository.findByAuctionItemIdAndUserId(auctionItemId, userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 경매 상품이 존재하지 않습니다."));
    }
}
