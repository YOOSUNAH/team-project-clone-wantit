package io.dcns.wantitauction.domain.auctionItem.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductResponseDto;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyAuctionItemController {

    MyAuctionItemService myAuctionItemService;

    @PostMapping("/v1/auction-items")
    public ResponseEntity<ResponseDto<CreateProductResponseDto>> createProduct(
        @Valid @RequestBody CreateProductRequestDto request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CreateProductResponseDto responseDto = myAuctionItemService.createProduct(
            request,
            userDetails.getUser()
        );
        return ResponseDto.of(HttpStatus.OK, responseDto);
    }

}
