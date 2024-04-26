package io.dcns.wantitauction.domain.bid.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.service.AuctionItemService;
import io.dcns.wantitauction.domain.bid.dto.BidPageableResponseDto;
import io.dcns.wantitauction.domain.bid.dto.BidRequestDto;
import io.dcns.wantitauction.domain.bid.dto.BidResponseDto;
import io.dcns.wantitauction.domain.bid.entity.Bid;
import io.dcns.wantitauction.domain.bid.repository.BidQueryRepository;
import io.dcns.wantitauction.domain.bid.repository.BidRepository;
import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.point.service.PointService;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("입찰 서비스 유닛 테스트")
@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @InjectMocks
    BidService bidService;

    @Mock
    PointService pointService;

    @Mock
    AuctionItemService auctionItemService;

    @Mock
    BidRepository bidRepository;

    @Mock
    BidQueryRepository bidQueryRepository;

    private User user;

    @BeforeEach
    void setTest() {
        user = User.builder()
            .userId(1L)
            .build();
    }

    @DisplayName("입찰 테스트_성공")
    @Test
    void createBid() {
        // Given
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(2L)
            .build();
        Point point = new Point(user);
        point.changePoint(100_000L);
        BidRequestDto bidRequestDto = new BidRequestDto(20_000L);

        // When
        when(pointService.findPoint(any(Long.class))).thenReturn(point);
        when(auctionItemService.findById(any(Long.class))).thenReturn(auctionItem);
        when(bidRepository.findTopByAuctionItemOrderByBidPriceDesc(
            any(AuctionItem.class))).thenReturn(null);
        when(bidRepository.save(any(Bid.class))).thenAnswer(i -> i.getArguments()[0]);
        BidResponseDto bidResponseDto = bidService.createBid(user, 1L, bidRequestDto);

        // Then
        assertEquals(user.getUserId(), bidResponseDto.getUserId());
        assertEquals(auctionItem.getAuctionItemId(), bidResponseDto.getAuctionItemId());
        assertEquals(bidRequestDto.getBidPrice(), bidResponseDto.getBidPrice());
    }

    @DisplayName("입찰 테스트_성공_기존 입찰 존재")
    @Test
    void createBid_ExistingBid() {
        // Given
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(2L)
            .build();
        Point point = new Point(user);
        point.changePoint(100_000L);
        Bid recentBid = Bid.builder()
            .bidPrice(20_000L)
            .userId(3L)
            .build();
        BidRequestDto bidRequestDto = new BidRequestDto(30_000L);

        // When
        when(pointService.findPoint(any(Long.class))).thenReturn(point);
        when(auctionItemService.findById(any(Long.class))).thenReturn(auctionItem);
        when(bidRepository.findTopByAuctionItemOrderByBidPriceDesc(
            any(AuctionItem.class))).thenReturn(recentBid);
        when(bidRepository.save(any(Bid.class))).thenAnswer(i -> i.getArguments()[0]);
        BidResponseDto bidResponseDto = bidService.createBid(user, 1L, bidRequestDto);

        // Then
        assertEquals(user.getUserId(), bidResponseDto.getUserId());
        assertEquals(auctionItem.getAuctionItemId(), bidResponseDto.getAuctionItemId());
        assertEquals(bidRequestDto.getBidPrice(), bidResponseDto.getBidPrice());
    }

    @DisplayName("입찰 테스트_실패_본인이 올린 상품")
    @Test
    void createBid_Fail_SelfAuctionItem() {
        // Given
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(1L)
            .build();
        BidRequestDto bidRequestDto = new BidRequestDto(20_000L);

        // When
        when(auctionItemService.findById(any(Long.class))).thenReturn(auctionItem);

        // Then
        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> bidService.createBid(user, 1L, bidRequestDto));
        assertEquals("본인이 올린 상품입니다.", exception.getMessage());
    }

    @DisplayName("입찰 테스트_실패_포인트 부족")
    @Test
    void createBid_Fail_NotEnoughPoint() {
        // Given
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(2L)
            .build();
        Point point = new Point(user);
        point.changePoint(10_000L);
        BidRequestDto bidRequestDto = new BidRequestDto(20_000L);

        // When
        when(pointService.findPoint(any(Long.class))).thenReturn(point);
        when(auctionItemService.findById(any(Long.class))).thenReturn(auctionItem);

        // Then
        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> bidService.createBid(user, 1L, bidRequestDto));
        assertEquals("포인트가 부족합니다.", exception.getMessage());
    }

    @DisplayName("입찰 테스트_실패_최고 입찰가")
    @Test
    void createBid_Fail_BidPriceLowerThanMaxPrice() {
        // Given
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(2L)
            .build();
        Point point = new Point(user);
        point.changePoint(100_000L);
        Bid recentBid = Bid.builder()
            .bidPrice(20_000L)
            .userId(3L)
            .build();
        BidRequestDto bidRequestDto = new BidRequestDto(15_000L);

        // When
        when(pointService.findPoint(any(Long.class))).thenReturn(point);
        when(auctionItemService.findById(any(Long.class))).thenReturn(auctionItem);
        when(bidRepository.findTopByAuctionItemOrderByBidPriceDesc(
            any(AuctionItem.class))).thenReturn(recentBid);

        // Then
        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> bidService.createBid(user, 1L, bidRequestDto));
        assertEquals("최고 입찰가 보다 높아야 합니다.", exception.getMessage());
    }

    @DisplayName("입찰 테스트_실패_호가 단위")
    @Test
    void createBid_Fail_NotAskingPoint() {
        // Given
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(2L)
            .build();
        Point point = new Point(user);
        point.changePoint(100_000L);
        BidRequestDto bidRequestDto = new BidRequestDto(25_000L);

        // When
        when(pointService.findPoint(any(Long.class))).thenReturn(point);
        when(auctionItemService.findById(any(Long.class))).thenReturn(auctionItem);

        // Then
        IllegalArgumentException exception = Assertions.assertThrows(
            IllegalArgumentException.class, () -> bidService.createBid(user, 1L, bidRequestDto));
        assertEquals("호가 단위를 맞춰주세요.", exception.getMessage());
    }


    @DisplayName("입찰 조회 테스트_성공")
    @Test
    void getAllBidsTest() {
        //given
        User user = User.builder().userId(1L).build();
        int page = 0;
        int size = 5;
        AuctionItem auctionItem = AuctionItem.builder()
            .auctionItemId(1L)
            .minPrice(10_000L)
            .userId(2L)
            .build();
        BidRequestDto bidRequestDto = new BidRequestDto(20_000L);
        Bid bid = new Bid(user, bidRequestDto, auctionItem);
        Pageable pageable = PageRequest.of(page, size);
        List<BidResponseDto> bidResponseDtoList = List.of(new BidResponseDto(bid));
        Page<BidResponseDto> bidResponseDtoPage = new PageImpl<>(bidResponseDtoList, pageable,
            bidResponseDtoList.size());

        //when
        when(bidQueryRepository.findAllBidsPageable(any(User.class),
            any(Pageable.class))).thenReturn(bidResponseDtoPage);
        BidPageableResponseDto actual = bidService.getAllBids(user, page, size);

        //then
        assertEquals(bidResponseDtoList.size(), actual.getBidResponseDtoList().size());
        assertEquals(page + 1, actual.getCurrentPage());
        assertEquals(size, actual.getPageSize());
        assertEquals(1, actual.getTotalPage());
    }
}
