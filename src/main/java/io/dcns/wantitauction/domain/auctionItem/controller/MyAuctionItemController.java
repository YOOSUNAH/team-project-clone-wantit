package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyAuctionItemController {

    MyAuctionItemService myAuctionItemService;

    @PostMapping("/v1/auction-items")
    public ResponseEntity<ResponseDto<Void>> createProduct(
        @Valid @RequestBody CreateProductRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        myAuctionItemService.createProduct(request, userDetails.getUser());
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping("/v1/users/auction-items")
    public ResponseEntity<ResponseDto<List<MyAuctionItemsResponseDto>>> getAuctionItems(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<MyAuctionItemsResponseDto> auctionItems = myAuctionItemService.getAuctionItems(
            userDetails.getUser().getUserId());
        return ResponseDto.of(HttpStatus.OK, auctionItems);
    }
}
