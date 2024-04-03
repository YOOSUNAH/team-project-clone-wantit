package io.dcns.wantitauction.domain.scheduler;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.bid.entity.Bid;
import io.dcns.wantitauction.domain.bid.repository.BidRepository;
import io.dcns.wantitauction.domain.event.WinningBidEvent;
import java.util.List;
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

    // cron = "초, 분, 시, 일, 월, 주" 순서
    // cron official document : https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html
//    @Scheduled(cron = "*/10 * * * * *") // 테스트용, 현재 매 10초 마다 동작
    @Transactional
    public void decideBidWinner() throws InterruptedException {
        log.info("경매 낙찰 로직 시작");
        List<AuctionItem> auctionItemList = auctionItemQueryRepository.findAllEndAuctionItems();
        for (AuctionItem auctionItem : auctionItemList) {
            Bid winBid = bidRepository.findTopByAuctionItemOrderByBidPriceDesc(auctionItem);
            auctionItem.finishAuction(winBid.getUserId(), winBid.getBidPrice());
            eventPublisher.publishEvent(
                new WinningBidEvent(
                    auctionItem.getAuctionItemId(), winBid.getUserId(), winBid.getBidPrice()
                ));
        }
        log.info("경매 낙찰 로직 종료");
    }
}
