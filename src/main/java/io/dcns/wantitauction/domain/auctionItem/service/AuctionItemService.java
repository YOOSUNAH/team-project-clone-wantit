package io.dcns.wantitauction.domain.auctionItem.service;

import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.ReadyItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.ReadyItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;
    private final AuctionItemQueryRepository auctionItemQueryRepository;

    public AuctionItemResponseDto getAuctionItem(Long auctionItemId) {
        AuctionItem auctionItem = auctionItemRepository.findById(auctionItemId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 경매상품 입니다.")
        );

        return new AuctionItemResponseDto(auctionItem);
    }

    public AuctionItemPageableResponseDto getAuctionItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuctionItemResponseDto> responseDtoPage =
            auctionItemQueryRepository.findAll(pageable);
        int totalPage = responseDtoPage.getTotalPages();

        return new AuctionItemPageableResponseDto(
            responseDtoPage.getContent(), size, page + 1, totalPage
        );
    }

    public FinishedItemPageableResponseDto getFinishedAuctionItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FinishedItemResponseDto> responseDtoPage =
            auctionItemQueryRepository.findAllByFinished(pageable);

        int totalPage = responseDtoPage.getTotalPages();

        return new FinishedItemPageableResponseDto(
            responseDtoPage.getContent(), size, page + 1, totalPage
        );
    }

    public FinishedItemResponseDto getFinishedAuctionItem(Long auctionItemId) {
        return auctionItemQueryRepository.findByIdAndFinished(auctionItemId)
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품 ID 입니다.")
            );
    }

    public AuctionItem findById(Long auctionItemId) {
        return auctionItemRepository.findById(auctionItemId).orElseThrow(
            () -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        );
    }

    public ReadyItemPageableResponseDto getReadyAuctionItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReadyItemResponseDto> responseDtoPage =
            auctionItemQueryRepository.findAllByReady(pageable);

        int totalPage = responseDtoPage.getTotalPages();

        return new ReadyItemPageableResponseDto(
            responseDtoPage.getContent(), size, page + 1, totalPage
        );
    }

    public InProgressItemPageableResponseDto getInProgressAuctionItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InProgressItemResponseDto> responseDtoPage =
            auctionItemQueryRepository.findAllByInProgress(pageable);

        int totalPage = responseDtoPage.getTotalPages();

        return new InProgressItemPageableResponseDto(
            responseDtoPage.getContent(), size, page + 1, totalPage
        );
    }
}
