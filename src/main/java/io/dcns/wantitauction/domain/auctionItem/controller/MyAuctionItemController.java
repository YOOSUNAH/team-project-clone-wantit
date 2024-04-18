package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/my/auction-items")
public class MyAuctionItemController {

    private final MyAuctionItemService myAuctionItemService;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createAuctionItem(
        @Valid @RequestBody CreateProductRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        myAuctionItemService.createAuctionItem(request, userDetails.getUser());
        return ResponseDto.of(HttpStatus.CREATED, null);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<MyAuctionItemPageableResponseDto>> getAuctionItems(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    ) {
        MyAuctionItemPageableResponseDto pageableResponseDto = myAuctionItemService.getAuctionItems(
            userDetails.getUser().getUserId(), page - 1, size);
        return ResponseDto.of(HttpStatus.OK, pageableResponseDto);
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
    public ResponseEntity<ResponseDto<FinishedItemPageableResponseDto>> getWinningAuctionItems(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam("page") int page,
        @RequestParam("size") int size
    ) {
        FinishedItemPageableResponseDto pageableResponseDto = myAuctionItemService
            .getWinningAuctionItems(
                userDetails.getUser().getUserId(), page - 1, size
            );

        return ResponseDto.of(HttpStatus.OK, pageableResponseDto);
    }

    @GetMapping("/{auctionItemId}/finished")
    public ResponseEntity<ResponseDto<FinishedItemResponseDto>> getWinningAuctionItem(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long auctionItemId
    ) {
        FinishedItemResponseDto finishedAuction = myAuctionItemService
            .getWinningAuctionItem(auctionItemId, userDetails.getUser().getUserId());
        return ResponseDto.of(HttpStatus.OK, finishedAuction);
    }
}
