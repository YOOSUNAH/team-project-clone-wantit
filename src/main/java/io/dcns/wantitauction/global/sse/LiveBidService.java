package io.dcns.wantitauction.global.sse;

import io.dcns.wantitauction.global.event.TopBidChangeEvent;
import io.dcns.wantitauction.global.exception.LiveBidException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class LiveBidService {

    private final Map<Long, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    private static final Long TIMEOUT = 60 * 60 * 1000L; // 60분 동안 유지

    public SseEmitter subscribeLiveBid(Long userId) {
        sseEmitterMap.remove(userId); // 해당 유저의 SseEmitter가 이미 존재하는 경우 지움

        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);
        setSseEmitter(sseEmitter, userId);
        sseEmitterMap.put(userId, sseEmitter);
        sendToClient(sseEmitter, userId, "연결 성공");
        return sseEmitter;
    }

    private void setSseEmitter(SseEmitter sseEmitter, Long userId) {
        sseEmitter.onTimeout(() -> sseEmitterMap.remove(userId));
        sseEmitter.onCompletion(() -> sseEmitterMap.remove(userId));
        sseEmitter.onError((e) -> sseEmitterMap.remove(userId));
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

    @EventListener(classes = {TopBidChangeEvent.class})
    public void sendToClient(TopBidChangeEvent bidChangeEvent) {
        for (SseEmitter emitter : sseEmitterMap.values()) {
            try {
                emitter.send(
                    SseEmitter.event()
                        .name("LiveBid")
                        .data(bidChangeEvent)
                );
            } catch (IOException e) {
                throw new LiveBidException("LiveBid 데이터를 전송하는 중 오류가 발생했습니다.");
            }
        }
    }
}
