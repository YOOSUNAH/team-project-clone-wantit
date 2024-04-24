package io.dcns.wantitauction.domain.bid.service;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.service.AuctionItemService;
import io.dcns.wantitauction.domain.bid.dto.BidRequestDto;
import io.dcns.wantitauction.domain.bid.dto.BidResponseDto;
import io.dcns.wantitauction.domain.bid.dto.TopAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.bid.entity.Bid;
import io.dcns.wantitauction.domain.bid.repository.BidQueryRepository;
import io.dcns.wantitauction.domain.bid.repository.BidRepository;
import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.point.service.PointService;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.global.aop.Lock.Lockable;
import io.dcns.wantitauction.global.event.TopBidChangeEvent;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final PointService pointService;
    private final AuctionItemService auctionItemService;
    private final BidRepository bidRepository;
    private final BidQueryRepository bidQueryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Lockable(value = "createBid", waitTime = 3000, leaseTime = 1000)
    public BidResponseDto createBid(User user, Long auctionItemId, BidRequestDto bidRequestDto) {

        Point point = pointService.findPoint(user.getUserId());
        AuctionItem auctionItem = auctionItemService.findById(auctionItemId);
        checkUser(auctionItem, user);
        checkAvailablePoint(point, bidRequestDto);
        Bid recentBid = bidRepository.findTopByAuctionItemOrderByBidPriceDesc(auctionItem);

        if (recentBid == null) { // 최초 입찰
            checkMaxPrice(bidRequestDto, auctionItem.getMinPrice());
            checkAskingPoint(bidRequestDto, auctionItem.getMinPrice());
            Bid newBid = new Bid(user, bidRequestDto, auctionItem);
            pointService.subtractPoint(user.getUserId(), bidRequestDto.getBidPrice());
            bidRepository.save(newBid);
            eventPublisher.publishEvent(new TopBidChangeEvent(
                auctionItem.getAuctionItemId(), newBid.getBidPrice()
            ));
            return new BidResponseDto(newBid);
        } else {
            checkMaxPrice(bidRequestDto, recentBid.getBidPrice());
            checkAskingPoint(bidRequestDto, recentBid.getBidPrice());
            pointService.returnBidPoint(recentBid.getUserId(), recentBid.getBidPrice());
            Bid newBid = new Bid(user, bidRequestDto, auctionItem);
            pointService.subtractPoint(user.getUserId(), bidRequestDto.getBidPrice());
            bidRepository.save(newBid);
            eventPublisher.publishEvent(new TopBidChangeEvent(
                auctionItem.getAuctionItemId(), newBid.getBidPrice()
            ));
            return new BidResponseDto(newBid);
        }
    }

    public List<BidResponseDto> getAllBids(User user) {
        return bidQueryRepository.findAllBids(user);
    }

    public List<TopAuctionItemsResponseDto> getTop3AuctionItemsByBid() {
        return bidQueryRepository.findTop3AuctionItemsByBid();
    }

    private void checkUser(AuctionItem auctionItem, User user) {
        if (auctionItem.getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("본인이 올린 상품입니다.");
        }
    }

    private void checkAvailablePoint(Point point, BidRequestDto bidRequestDto) {
        if (point.getAvailablePoint() < bidRequestDto.getBidPrice()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
    }

    private void checkMaxPrice(BidRequestDto bidRequestDto, Long maxPrice) {
        if (maxPrice >= bidRequestDto.getBidPrice()) {
            throw new IllegalArgumentException("최고 입찰가 보다 높아야 합니다.");
        }
    }

    private void checkAskingPoint(BidRequestDto bidRequestDto, Long currentPrice) {
        Long askingPoint = askingPoint(currentPrice);
        if (!(Math.abs(bidRequestDto.getBidPrice() - currentPrice) % askingPoint == 0)) {
            throw new IllegalArgumentException("호가 단위를 맞춰주세요.");
        }
    }

    private Long askingPoint(Long currentPrice) {
        List<Entry<Long, Long>> priceRangeList = new ArrayList<>();

        priceRangeList.add(new AbstractMap.SimpleEntry<>(500_000L, 10_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(1_000_000L, 50_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(3_000_000L, 100_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(5_000_000L, 200_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(10_000_000L, 300_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(30_000_000L, 500_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(50_000_000L, 1_000_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(100_000_000L, 2_000_000L));
        priceRangeList.add(new AbstractMap.SimpleEntry<>(200_000_000L, 3_000_000L));

        Optional<Long> askingPoint = priceRangeList.stream()
            .filter(entry -> currentPrice < entry.getKey())
            .map(Map.Entry::getValue)
            .findFirst();

        if (askingPoint.isPresent()) {
            return askingPoint.get();
        } else if (currentPrice >= 200000000) {
            return 5_000_000L;
        } else {
            throw new IllegalArgumentException("정상적인 가격이 아닙니다.");
        }
    }
}
