package io.dcns.wantitauction.domain.like.controller;

import io.dcns.wantitauction.domain.like.dto.LikeResponseDto;
import io.dcns.wantitauction.domain.like.service.LikeService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auctions")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{auctionItemId}/likes")
    public ResponseEntity<ResponseDto<LikeResponseDto>> likeAuctionItem(
        @PathVariable Long auctionItemId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        LikeResponseDto likeResponseDto = likeService.likeAuctionItem(
            auctionItemId, userDetails.getUser()
        );

        return ResponseDto.of(HttpStatus.OK, likeResponseDto);
    }

    @GetMapping("/likes")
    public ResponseEntity<ResponseDto<List<LikeResponseDto>>> getLikeAuctionItem(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LikeResponseDto> likeResponseDtolist = likeService.getLikedAuctionItem(
            userDetails.getUser().getUserId());

        return ResponseDto.of(HttpStatus.OK, likeResponseDtolist);
    }
}
