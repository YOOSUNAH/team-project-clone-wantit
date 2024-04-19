package io.dcns.wantitauction.global.sse;

import io.dcns.wantitauction.global.event.TopBidChangeEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class LiveBidService {

    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> auctionItemUserMap = new ConcurrentHashMap<>();
    private static final Long TIMEOUT = 60 * 60 * 1000L;

    public SseEmitter subscribeLiveBid(Long userId, Long auctionItemId) {
        sseEmitterMap.remove(userId);

        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);
        setSseEmitter(sseEmitter, userId);
        sseEmitterMap.put(userId, sseEmitter);
        addAuctionItemUser(auctionItemId, userId);
        sendToClient(sseEmitter, userId, "연결 성공");
        return sseEmitter;
    }

    private void addAuctionItemUser(Long auctionItemId, Long userId) {
        Set<Long> userIdSet = auctionItemUserMap.getOrDefault(auctionItemId,
            new HashSet<>());
        userIdSet.add(userId);
        auctionItemUserMap.put(auctionItemId, userIdSet);
    }

    private void setSseEmitter(SseEmitter sseEmitter, Long userId) {
        sseEmitter.onTimeout(() -> removeSseEmitterAndUser(userId));
        sseEmitter.onCompletion(() -> removeSseEmitterAndUser(userId));
        sseEmitter.onError((e) -> removeSseEmitterAndUser(userId));
    }

    private void removeSseEmitterAndUser(Long userId) {
        sseEmitterMap.remove(userId);
        for (Collection<Long> collection : auctionItemUserMap.values()) {
            collection.remove(userId);
        }
    }

    private void sendToClient(SseEmitter sseEmitter, Long userId, Object data) {
        try {
            sseEmitter.send(
                SseEmitter.event()
                    .id(userId.toString())
                    .name("SSE 연결")
                    .data(data)
            );
        } catch (Exception e) {
            sseEmitterMap.remove(userId);
        }
    }

    @TransactionalEventListener
    public void topBidEventListener(TopBidChangeEvent topBidChangeEvent) {
        Long auctionItemId = topBidChangeEvent.getAuctionItemId();
        Set<Long> userIdSet = auctionItemUserMap.get(auctionItemId);

        userIdSet.parallelStream().forEach(userId -> sendToClient(userId, topBidChangeEvent));
    }

    public void sendToClient(Long userId, TopBidChangeEvent bidChangeEvent) {
        try {
            sseEmitterMap.get(userId)
                .send(
                    SseEmitter.event()
                        .name("LiveBid")
                        .data(bidChangeEvent)
                );
        } catch (IOException e) {
            log.error("유저 ID: [ " + userId + " ] 에게 메세지 전송에 실패했습니다.");
        }
    }
}
