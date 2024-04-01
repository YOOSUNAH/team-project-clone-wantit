package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.service.AuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auction-items")
public class AuctionItemController {

    private final AuctionItemService auctionItemService;

    @GetMapping("/{auctionItemId}")
    public ResponseEntity<ResponseDto<AuctionItemResponseDto>> getProduct(
        @PathVariable Long auctionItemId
    ) {
        AuctionItemResponseDto responseDto = auctionItemService.getProduct(auctionItemId);
        return ResponseDto.of(HttpStatus.OK, responseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<AuctionItemResponseDto>>> getProducts() {
        List<AuctionItemResponseDto> responseDtoList = auctionItemService.getProducts();
        return ResponseDto.of(HttpStatus.OK, responseDtoList);
    }

    @GetMapping("/finished")
    public ResponseEntity<ResponseDto<List<FinishedItemResponseDto>>> getFinishedProducts() {
        List<FinishedItemResponseDto> responseDtoList = auctionItemService.getFinishedProducts();
        return ResponseDto.of(HttpStatus.OK, responseDtoList);
    }

    @GetMapping("/{auctionItemId}/finished")
    public ResponseEntity<ResponseDto<FinishedItemResponseDto>> getFinishedProduct(
        @PathVariable Long auctionItemId
    ) {
        FinishedItemResponseDto responseDto = auctionItemService.getFinishedProduct(auctionItemId);
        return ResponseDto.of(HttpStatus.OK, responseDto);
    }
}
