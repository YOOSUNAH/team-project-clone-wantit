package io.dcns.wantitauction.global.scheduler;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.bid.entity.Bid;
import io.dcns.wantitauction.domain.bid.repository.BidRepository;
import io.dcns.wantitauction.global.event.StartAuctionEvent;
import io.dcns.wantitauction.global.event.WinningBidEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Scheduled(cron = "5 0 9 * * *")
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

    @Scheduled(cron = "5 0 19 * * * ")
    @Transactional
    public void decideBidWinner() throws InterruptedException {
        log.info("경매 낙찰 로직 시작");
        List<AuctionItem> auctionItemList = auctionItemQueryRepository.findAllEndAuctionItems();
        for (AuctionItem auctionItem : auctionItemList) {
            Bid winBid = bidRepository.findTopByAuctionItemOrderByBidPriceDesc(auctionItem);
            if (winBid == null) {
                auctionItem.failAuction();
                continue;
            }
            auctionItem.finishAuction(winBid.getUserId(), winBid.getBidPrice());
            eventPublisher.publishEvent(
                new WinningBidEvent(
                    auctionItem.getAuctionItemId(), auctionItem.getItemName(),
                    winBid.getUserId(), winBid.getBidPrice()
                ));
        }
        log.info("경매 낙찰 로직 종료");
    }
}
