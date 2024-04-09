package io.dcns.wantitauction.global.scheduler;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.bid.entity.Bid;
import io.dcns.wantitauction.domain.bid.repository.BidRepository;
import io.dcns.wantitauction.domain.notification.dto.NotificationRequestDto;
import io.dcns.wantitauction.domain.notification.service.NotificationService;
import io.dcns.wantitauction.global.event.StartAuctionEvent;
import io.dcns.wantitauction.global.event.WinningBidEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ApplicationEventPublisher eventPublisher;
    private final AuctionItemQueryRepository auctionItemQueryRepository;
    private final BidRepository bidRepository;
    private final NotificationService notificationService;

    // cron = "초, 분, 시, 일, 월, 주" 순서
    // cron official document : https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html
    // @Scheduled(cron = "*/10 * * * * *") // 테스트용, 현재 매 10초 마다 동작, Todo : 경매 마감 시간 정해야함
    @Transactional
    public void decideBidWinner() throws InterruptedException {
        log.info("경매 낙찰 로직 시작");
        List<AuctionItem> auctionItemList = auctionItemQueryRepository.findAllEndAuctionItems();
        for (AuctionItem auctionItem : auctionItemList) {
            Bid winBid = bidRepository.findTopByAuctionItemOrderByBidPriceDesc(auctionItem);
            auctionItem.finishAuction(winBid.getUserId(), winBid.getBidPrice());
            eventPublisher.publishEvent(
                new WinningBidEvent(
                    auctionItem.getAuctionItemId(), auctionItem.getItemName(),
                    winBid.getUserId(), winBid.getBidPrice()
                ));
        }
        log.info("경매 낙찰 로직 종료");
    }

    // @Scheduled(cron = "*/10 * * * * *") // Todo : 경매 오픈 시간 정해야함
    @Transactional
    public void startAuction() throws InterruptedException {
        log.info("경매 오픈 로직 시작");
        List<AuctionItem> auctionItemList = auctionItemQueryRepository.findAllOpenAuctionItems();
        for (AuctionItem auctionItem : auctionItemList) {
            auctionItem.startAuction();
            eventPublisher.publishEvent(new StartAuctionEvent(auctionItem.getAuctionItemId()));
        }
        log.info("경매 오픈 로직 종료");
    }

    // @Scheduled(cron = "0 5 19 * * ?")
    public void sendNotification() throws ExecutionException, InterruptedException {
        if (auctionItemQueryRepository.findAllTodayWinningAuctionItems().isEmpty()) {
            log.info("오늘 낙찰된 경매가 없습니다.");
            return;
        }

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
            .title("경매 낙찰 알림")
            .token(notificationService.getNotificationToken())
            .message("낙찰된 경매가 존재합니다. 확인해주세요!")
            .build();
        notificationService.sendNotification(notificationRequestDto);
        log.info("알림 전송 완료");
    }
}
