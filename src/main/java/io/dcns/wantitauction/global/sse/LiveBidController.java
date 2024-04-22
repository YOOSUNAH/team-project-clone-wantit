package io.dcns.wantitauction.global.sse;

import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/live-bids")
public class LiveBidController {

    private final LiveBidService liveBidService;

    @GetMapping(value = "/auction-items/{auctionItemId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeLiveBid(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long auctionItemId
    ) {
        return ResponseEntity.ok(
            liveBidService.subscribeLiveBid(userDetails.getUser().getUserId(), auctionItemId));
    }
}
