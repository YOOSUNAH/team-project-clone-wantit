package io.dcns.wantitauction.domain.myAuctionItem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MyAuctionItemServiceTest {

    @Mock
    private AuctionItemRepository auctionItemRepository;

    @Mock
    private AuctionItemQueryRepository auctionItemQueryRepository;

    @InjectMocks
    private MyAuctionItemService myAuctionItemService;

    @Test
    @DisplayName("[성공] 신규 경매 등록")
    void addAuctionItem() {
        // given
        CreateProductRequestDto request = CreateProductRequestDto.builder()
            .itemName("TEST_ITEM")
            .itemDescription("TEST_DESCRIPTION")
            .minPrice(100000L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        User user = User.builder()
            .userId(1L)
            .build();

        // when
        myAuctionItemService.createAuctionItem(request, user);

        // then
        ArgumentCaptor<AuctionItem> argumentCaptor = ArgumentCaptor.forClass(AuctionItem.class);
        verify(auctionItemRepository).save(argumentCaptor.capture());
        AuctionItem capturedAuctionItem = argumentCaptor.getValue();

        assertThat(capturedAuctionItem.getItemName()).isEqualTo(request.getItemName());
        assertThat(capturedAuctionItem.getItemDescription()).isEqualTo(request.getItemDescription());
        assertThat(capturedAuctionItem.getMinPrice()).isEqualTo(request.getMinPrice());
        assertThat(capturedAuctionItem.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(capturedAuctionItem.getEndDate()).isEqualTo(request.getEndDate());
    }

    @Test
    @DisplayName("[성공] 내 경매 리스트 조회")
    void getMyAuctionItems() {
        // given
        User user = User.builder()
            .userId(1L)
            .build();

        List<MyAuctionItemsResponseDto> expectedAuctionItems = new ArrayList<>();
        when(auctionItemQueryRepository.findAllMyAuctionItems(user.getUserId())).thenReturn(expectedAuctionItems);

        // when
        List<MyAuctionItemsResponseDto> actualAuctionItems = myAuctionItemService.getAuctionItems(user.getUserId());

        // then
        verify(auctionItemQueryRepository).findAllMyAuctionItems(user.getUserId());
        assertThat(actualAuctionItems).isEqualTo(expectedAuctionItems);
    }
}
