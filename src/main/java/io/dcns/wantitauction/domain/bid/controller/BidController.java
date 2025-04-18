package io.dcns.wantitauction.domain.bid.controller;

import io.dcns.wantitauction.domain.bid.dto.BidPageableResponseDto;
import io.dcns.wantitauction.domain.bid.dto.BidRequestDto;
import io.dcns.wantitauction.domain.bid.dto.BidResponseDto;
import io.dcns.wantitauction.domain.bid.dto.TopAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.bid.dto.TopBidResponseDto;
import io.dcns.wantitauction.domain.bid.service.BidService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auction-items")
public class BidController {
    final private BidService bidService;

    @PostMapping("/{auctionItemId}/bids")
    public ResponseEntity<ResponseDto<BidResponseDto>> createBid(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long auctionItemId,
            @RequestBody BidRequestDto bidRequestDto) {
        BidResponseDto bidResponseDto = bidService
                .createBid(userDetails.getUser(), auctionItemId, bidRequestDto);

        return ResponseDto.of(HttpStatus.OK, bidResponseDto);
    }

    @GetMapping("/bids")
    public ResponseEntity<ResponseDto<BidPageableResponseDto>> getAllBids(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        BidPageableResponseDto bidPageableResponseDto = bidService
                .getAllBids(userDetails.getUser(), page - 1, size);
        return ResponseDto.of(HttpStatus.OK, bidPageableResponseDto);
    }

    @GetMapping("/top3")
    public ResponseEntity<ResponseDto<List<TopAuctionItemsResponseDto>>> getTop3AuctionItems() {
        List<TopAuctionItemsResponseDto> TopBidResponseDtoList = bidService
                .getTop3AuctionItemsByBid();
        return ResponseDto.of(HttpStatus.OK, TopBidResponseDtoList);
    }

    @GetMapping("/{auctionItemId}/bids/top")
    public ResponseEntity<ResponseDto<TopBidResponseDto>> findTopBid(@PathVariable Long auctionItemId) {
        TopBidResponseDto bidResponseDto = bidService.findTopBid(auctionItemId);
        return ResponseDto.of(HttpStatus.OK, bidResponseDto);
    }
}
