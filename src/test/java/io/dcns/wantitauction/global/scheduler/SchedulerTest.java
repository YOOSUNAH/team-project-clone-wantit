package io.dcns.wantitauction.global.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.bid.entity.Bid;
import io.dcns.wantitauction.domain.bid.repository.BidRepository;
import io.dcns.wantitauction.domain.notification.service.NotificationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class SchedulerTest {

    @Mock
    ApplicationEventPublisher eventPublisher;
    @Mock
    AuctionItemQueryRepository auctionItemQueryRepository;
    @Mock
    BidRepository bidRepository;
    @Mock
    NotificationService notificationService;

    @InjectMocks
    Scheduler scheduler;

    @Nested
    @DisplayName("낙찰 로직 테스트")
    class DecideBidWinner {

        @Test
        @DisplayName("낙찰 성공")
        void decideBidWinner() {
            //given
            AuctionItem testItem = AuctionItem.builder()
                .auctionItemId(1L)
                .userId(1L)
                .status(AuctionItemEnum.IN_PROGRESS)
                .build();
            Bid winBid = new Bid(1L, 2L, 100000L, testItem);
            given(auctionItemQueryRepository.findAllEndAuctionItems()).willReturn(
                List.of(testItem));
            given(bidRepository.findTopByAuctionItemOrderByBidPriceDesc(testItem)).willReturn(
                winBid);
            //when
            try {
                scheduler.decideBidWinner();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //then
            assertThat(testItem.getWinnerId()).isEqualTo(winBid.getUserId());
            assertThat(testItem.getWinPrice()).isEqualTo(winBid.getBidPrice());
        }

        @Test
        @DisplayName("실패 - 입찰자 부재")
        void decideBidWinnerFailCase1() {
            //given
            AuctionItem testItem = AuctionItem.builder()
                .auctionItemId(1L)
                .userId(1L)
                .status(AuctionItemEnum.IN_PROGRESS)
                .build();
            given(auctionItemQueryRepository.findAllEndAuctionItems()).willReturn(
                List.of(testItem));
            given(bidRepository.findTopByAuctionItemOrderByBidPriceDesc(testItem)).willReturn(
                null);
            //when
            try {
                scheduler.decideBidWinner();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //then
            assertThat(testItem.getStatus()).isEqualTo(AuctionItemEnum.FAILED);
        }
    }


    @Nested
    @DisplayName("경매 시작 테스트")
    class StartAuction {

        @Test
        @DisplayName("성공")
        void startAuction() {
            //given
            AuctionItem testItem = AuctionItem.builder()
                .auctionItemId(1L)
                .userId(1L)
                .status(AuctionItemEnum.READY)
                .build();
            given(auctionItemQueryRepository.findAllOpenAuctionItems())
                .willReturn(List.of(testItem));
            //when
            try {
                scheduler.startAuction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //then
            assertThat(testItem.getStatus()).isEqualTo(AuctionItemEnum.IN_PROGRESS);
        }
    }

}
