package io.dcns.wantitauction.domain.myAuctionItem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@ExtendWith(MockitoExtension.class)
class MyAuctionItemServiceTest {

    @Mock
    private AuctionItemRepository auctionItemRepository;

    @Mock
    private AuctionItemQueryRepository auctionItemQueryRepository;

    @InjectMocks
    private MyAuctionItemService myAuctionItemService;

    private static User createUser() {
        return User.builder()
            .userId(1L)
            .build();
    }

    @Test
    @DisplayName("[성공] 신규 경매 등록")
    void addAuctionItem() {
        // given
        User user = createUser();

        CreateProductRequestDto request = CreateProductRequestDto.builder()
            .itemName("TEST_ITEM")
            .itemDescription("TEST_DESCRIPTION")
            .minPrice(100000L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        // when
        myAuctionItemService.createAuctionItem(request, user);

        // then
        ArgumentCaptor<AuctionItem> argumentCaptor = ArgumentCaptor.forClass(AuctionItem.class);
        verify(auctionItemRepository).save(argumentCaptor.capture());
        AuctionItem capturedAuctionItem = argumentCaptor.getValue();

        assertThat(capturedAuctionItem.getItemName()).isEqualTo(request.getItemName());
        assertThat(capturedAuctionItem.getItemDescription()).isEqualTo(
            request.getItemDescription());
        assertThat(capturedAuctionItem.getMinPrice()).isEqualTo(request.getMinPrice());
        assertThat(capturedAuctionItem.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(capturedAuctionItem.getEndDate()).isEqualTo(request.getEndDate());
    }

    @Test
    @DisplayName("[성공] 내 경매 리스트 조회")
    void getMyAuctionItems() {
        // given
        User user = createUser();
        AuctionItem testItem = AuctionItem.builder().auctionItemId(1L).build();
        Pageable pageable = PageRequest.of(0, 3);
        int totalSize = 10;
        List<MyAuctionItemsResponseDto> expectedAuctionItems = List.of(
            new MyAuctionItemsResponseDto(testItem),
            new MyAuctionItemsResponseDto(testItem),
            new MyAuctionItemsResponseDto(testItem)
        );

        when(auctionItemQueryRepository.findAllMyAuctionItems(anyLong(),
            any(Pageable.class))).thenReturn(
            PageableExecutionUtils.getPage(expectedAuctionItems, pageable, () -> totalSize));

        // when
        MyAuctionItemPageableResponseDto pageableResponseDto = myAuctionItemService.getAuctionItems(
            user.getUserId(), pageable.getPageNumber(), pageable.getPageSize());

        // then
        verify(auctionItemQueryRepository).findAllMyAuctionItems(user.getUserId(), pageable);
        assertThat(pageableResponseDto.getResponseDtoList()).isEqualTo(expectedAuctionItems);
    }

    @Test
    @DisplayName("[성공] 내 경매 단건 조회")
    void getAuctionItem() {
        //given
        User user = createUser();

        AuctionItem auctionItem = AuctionItem.builder()
            .itemName("TEST_ITEM")
            .itemDescription("TEST_DESCRIPTION")
            .minPrice(100000L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        when(auctionItemRepository.findByAuctionItemIdAndUserId(1L, user.getUserId()))
            .thenReturn(Optional.of(auctionItem));

        // when
        MyAuctionItemsResponseDto actualAuctionItem = myAuctionItemService.getAuctionItem(
            1L,
            user.getUserId());

        // then
        assertThat(actualAuctionItem.getItemName()).isEqualTo(auctionItem.getItemName());
    }

    @Test
    @DisplayName("[성공] 내 경매 수정")
    void updateAuctionItem() {
        // given
        User user = createUser();

        AuctionItem auctionItem = AuctionItem.builder()
            .itemName("TEST_ITEM")
            .itemDescription("TEST_DESCRIPTION")
            .minPrice(100000L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        UpdateMyItemRequestDto request = UpdateMyItemRequestDto.builder()
            .itemName("UPDATED_ITEM")
            .itemDescription("UPDATED_DESCRIPTION")
            .minPrice(200000L)
            .startDate(LocalDateTime.now().plusDays(1))
            .endDate(LocalDateTime.now().plusDays(2))
            .build();

        when(auctionItemRepository.findByAuctionItemIdAndUserId(1L, user.getUserId()))
            .thenReturn(Optional.of(auctionItem));

        // when
        MyAuctionItemsResponseDto actualAuctionItem = myAuctionItemService.updateAuctionItem(
            request,
            1L,
            user.getUserId());

        // then
        assertThat(actualAuctionItem.getItemName()).isEqualTo(request.getItemName());
        assertThat(actualAuctionItem.getItemDescription()).isEqualTo(request.getItemDescription());
        assertThat(actualAuctionItem.getMinPrice()).isEqualTo(request.getMinPrice());
        assertThat(actualAuctionItem.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(actualAuctionItem.getEndDate()).isEqualTo(request.getEndDate());
    }

    @Test
    @DisplayName("[성공] 내 경매 삭제")
    void deleteAuctionItem() {
        // given
        User user = createUser();

        AuctionItem auctionItem = AuctionItem.builder()
            .itemName("TEST_ITEM")
            .itemDescription("TEST_DESCRIPTION")
            .minPrice(100000L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        when(auctionItemRepository.findByAuctionItemIdAndUserId(1L, user.getUserId()))
            .thenReturn(Optional.of(auctionItem));

        // when
        myAuctionItemService.deleteAuctionItem(1L, user.getUserId());

        // then
        verify(auctionItemRepository).deleteById(auctionItem.getAuctionItemId());
    }
}
