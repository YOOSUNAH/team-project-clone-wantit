package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.ReadyItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.service.AuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auction-items")
public class AuctionItemController {
    private final AuctionItemService auctionItemService;

    @GetMapping("/{auctionItemId}")
    public ResponseEntity<ResponseDto<AuctionItemResponseDto>> getAuctionItem(@PathVariable Long auctionItemId) {
        AuctionItemResponseDto responseDto = auctionItemService.getAuctionItem(auctionItemId);
        return ResponseDto.of(HttpStatus.OK, responseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<AuctionItemPageableResponseDto>> getAuctionItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        AuctionItemPageableResponseDto pageableResponseDto = auctionItemService
                .getAuctionItems(page - 1, size);
        return ResponseDto.of(HttpStatus.OK, pageableResponseDto);
    }

    @GetMapping("/finished")
    public ResponseEntity<ResponseDto<FinishedItemPageableResponseDto>> getFinishedAuctionItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        FinishedItemPageableResponseDto pageableResponseDto = auctionItemService
                .getFinishedAuctionItems(page - 1, size);
        return ResponseDto.of(HttpStatus.OK, pageableResponseDto);
    }

    @GetMapping("/{auctionItemId}/finished")
    public ResponseEntity<ResponseDto<FinishedItemResponseDto>> getFinishedAuctionItem(
            @PathVariable Long auctionItemId) {
        FinishedItemResponseDto responseDto = auctionItemService.getFinishedAuctionItem(auctionItemId);
        return ResponseDto.of(HttpStatus.OK, responseDto);
    }

    @GetMapping("/ready")
    public ResponseEntity<ResponseDto<ReadyItemPageableResponseDto>> getReadyAuctionItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        ReadyItemPageableResponseDto pageableResponseDto = auctionItemService
                .getReadyAuctionItems(page - 1, size);
        return ResponseDto.of(HttpStatus.OK, pageableResponseDto);
    }

    @GetMapping("/in-progress")
    public ResponseEntity<ResponseDto<InProgressItemPageableResponseDto>> getInProgressAuctionItems(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "category", required = false) String category) {
        InProgressItemPageableResponseDto pageableResponseDto = auctionItemService
                .getInProgressAuctionItems(page - 1, size, category);
        return ResponseDto.of(HttpStatus.OK, pageableResponseDto);
    }
}
