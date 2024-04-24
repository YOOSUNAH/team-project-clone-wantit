package io.dcns.wantitauction.domain.like.controller;

import io.dcns.wantitauction.domain.like.dto.LikeResponseDto;
import io.dcns.wantitauction.domain.like.dto.LikeAuctionItemResponseDto;
import io.dcns.wantitauction.domain.like.service.LikeService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auction-item")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{auctionItemId}/likes")
    public ResponseEntity<ResponseDto<LikeResponseDto>> likeAuctionItem(
        @PathVariable Long auctionItemId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        LikeResponseDto likeResponseDto = likeService.likeAuctionItem(
            auctionItemId, userDetails.getUser()
        );
        log.info("좋아요(찜하기) 등록 또는 취소 API 성공");
        return ResponseDto.of(HttpStatus.OK, likeResponseDto);
    }

    @GetMapping("/likes")
    public ResponseEntity<ResponseDto<List<LikeAuctionItemResponseDto>>> getLikeAuctionItem(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LikeAuctionItemResponseDto> likeResponseDtolist = likeService.getLikedAuctionItem(
            userDetails.getUser().getUserId());
        log.info("내가 찜한 경매 상품 조회 API 성공");
        return ResponseDto.of(HttpStatus.OK, likeResponseDtolist);
    }

    @GetMapping("/dislikes")
    public ResponseEntity<ResponseDto<List<LikeAuctionItemResponseDto>>> getDislikeAuctionItem(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LikeAuctionItemResponseDto> likeResponseDtolist = likeService.getDislikedAuctionItem(
            userDetails.getUser().getUserId());
        log.info("내가 취소한 찜한 경매 상품 조회 API 성공");
        return ResponseDto.of(HttpStatus.OK, likeResponseDtolist);
    }
}
