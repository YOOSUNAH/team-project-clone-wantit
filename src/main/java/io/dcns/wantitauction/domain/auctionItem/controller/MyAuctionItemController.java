package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/my/auction-items")
public class MyAuctionItemController {

    private final MyAuctionItemService myAuctionItemService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createProduct(
        @Valid @RequestBody CreateProductRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        myAuctionItemService.createProduct(request, userDetails.getUser());
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<MyAuctionItemsResponseDto>>> getAuctionItems(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<MyAuctionItemsResponseDto> auctionItems = myAuctionItemService.getAuctionItems(
            userDetails.getUser().getUserId());
        return ResponseDto.of(HttpStatus.CREATED, auctionItems);
    }

    @GetMapping("/{auctionItemId}")
    public ResponseEntity<ResponseDto<MyAuctionItemsResponseDto>> getAuctionItem(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long auctionItemId
    ) {
        MyAuctionItemsResponseDto auctionItem = myAuctionItemService.getAuctionItem(
            auctionItemId, userDetails.getUser().getUserId());
        return ResponseDto.of(HttpStatus.OK, auctionItem);
    }

    @PutMapping("/{auctionItemId}")
    public ResponseEntity<ResponseDto<MyAuctionItemsResponseDto>> updateAuctionItem(
        @Valid @RequestBody UpdateMyItemRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long auctionItemId
    ) {
        MyAuctionItemsResponseDto updateItem = myAuctionItemService.updateAuctionItem(
            request, auctionItemId, userDetails.getUser().getUserId());
        return ResponseDto.of(HttpStatus.OK, updateItem);
    }

    @DeleteMapping("/{auctionItemId}")
    public ResponseEntity<ResponseDto<Void>> deleteAuctionItem(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long auctionItemId
    ) {
        myAuctionItemService.deleteAuctionItem(auctionItemId, userDetails.getUser().getUserId());
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/finished")
    public ResponseEntity<ResponseDto<List<MyAuctionItemsResponseDto>>> getFinishedAuctionItems(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<MyAuctionItemsResponseDto> finishedAuction = myAuctionItemService.getFinishedAuctionItems(
            userDetails.getUser());
        return ResponseDto.of(HttpStatus.CREATED, finishedAuction);
    }
}
