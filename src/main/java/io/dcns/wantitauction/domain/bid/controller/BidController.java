package io.dcns.wantitauction.domain.bid.controller;

import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemResponseDto;
import io.dcns.wantitauction.domain.bid.dto.BidRequestDto;
import io.dcns.wantitauction.domain.bid.dto.BidResponseDto;
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
        @RequestBody BidRequestDto bidRequestDto
    ) {
        BidResponseDto bidResponseDto = bidService
            .createBid(userDetails.getUser(), auctionItemId, bidRequestDto);

        return ResponseDto.of(HttpStatus.OK, bidResponseDto);
    }

    @GetMapping("/bids")
    public ResponseEntity<ResponseDto<List<BidResponseDto>>> getAllBids(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<BidResponseDto> bidResponseDtoList = bidService
            .getAllBids(userDetails.getUser());
        return ResponseDto.of(HttpStatus.OK, bidResponseDtoList);
    }

    @GetMapping("/bids/top3")
    public ResponseEntity<ResponseDto<List<InProgressItemResponseDto>>> getTop3Bids(
    ) {
        List<InProgressItemResponseDto> TopBidResponseDtoList = bidService
            .getTop3Bids();
        return ResponseDto.of(HttpStatus.OK, TopBidResponseDtoList);
    }
}
