package io.dcns.wantitauction.global.sse;

import io.dcns.wantitauction.global.event.TopBidChangeEvent;

import java.io.IOException;
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
    private Map<Long, Set<SseEmitter>> auctionEmitterMap = new ConcurrentHashMap<>();
    private static final Long TIMEOUT = 60 * 60 * 1000L;

    public SseEmitter subscribeLiveBid(Long auctionItemId) {
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);
        setSseEmitter(sseEmitter, auctionItemId);
        addAuctionEmitter(auctionItemId, sseEmitter);
        sendToClient(sseEmitter, "초기 연결", "연결 성공");
        return sseEmitter;
    }

    private void addAuctionEmitter(Long auctionItemId,
                                   SseEmitter sseEmitter) {
        Set<SseEmitter> emitterSet = auctionEmitterMap
                .getOrDefault(auctionItemId, new HashSet<>());
        emitterSet.add(sseEmitter);
        auctionEmitterMap.put(auctionItemId, emitterSet);
    }

    private void setSseEmitter(SseEmitter sseEmitter,
                               Long auctionItemId) {
        sseEmitter.onTimeout(() -> removeSseEmitter(auctionItemId, sseEmitter));
        sseEmitter.onCompletion(() -> removeSseEmitter(auctionItemId, sseEmitter));
        sseEmitter.onError((e) -> removeSseEmitter(auctionItemId, sseEmitter));
    }

    private void removeSseEmitter(Long auctionItemId,
                                  SseEmitter sseEmitter) {
        Set<SseEmitter> emitterSet = auctionEmitterMap.get(auctionItemId);
        if (emitterSet != null) {
            emitterSet.remove(sseEmitter);
            log.info("SseEmitter 삭제");
            if (emitterSet.isEmpty()) {
                auctionEmitterMap.remove(auctionItemId);
                log.info("SseEmitter Set 삭제");
            }
        }
    }

    private void sendToClient(SseEmitter sseEmitter,
                              String eventName,
                              Object data) {
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .name(eventName)
                            .data(data)
            );
        } catch (IOException e) {
            log.error("메세지 전송에 실패했습니다.");
        }
    }

    @TransactionalEventListener
    public void topBidEventListener(TopBidChangeEvent topBidChangeEvent) {
        Long auctionItemId = topBidChangeEvent.getAuctionItemId();
        Set<SseEmitter> emitterSet = auctionEmitterMap.get(auctionItemId);
        String eventName = "bidUpdate";

        if (emitterSet != null) {
            emitterSet.parallelStream().forEach(
                    sseEmitter -> sendToClient(
                            sseEmitter, eventName, topBidChangeEvent
                    )
            );
        }
    }
}
