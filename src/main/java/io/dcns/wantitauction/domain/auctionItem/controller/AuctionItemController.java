package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.service.AuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auctionItems")
public class AuctionItemController {

    private final AuctionItemService auctionItemService;

    @GetMapping("/{auctionItemId}")
    public ResponseEntity<ResponseDto<AuctionItemResponseDto>> getProduct(
        @PathVariable Long auctionItemId
    ) {
        AuctionItemResponseDto responseDto = auctionItemService.getProduct(auctionItemId);
        return ResponseDto.of(HttpStatus.OK, responseDto);
    }
}
