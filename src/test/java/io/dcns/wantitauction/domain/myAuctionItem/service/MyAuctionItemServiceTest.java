package io.dcns.wantitauction.domain.myAuctionItem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.auctionItem.service.MyAuctionItemService;
import io.dcns.wantitauction.domain.user.entity.User;
import java.time.LocalDateTime;
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

    @InjectMocks
    private MyAuctionItemService myAuctionItemService;

    private User createUser() {
        return User.builder()
            .userId(1000L)
            .build();
    }

    private CreateProductRequestDto createRequest() {
        String itemName = "TEST_ITEM";
        String itemDescription = "TEST_DESCRIPTION";
        Long minPrice = 100000L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        return new CreateProductRequestDto(itemName, itemDescription, minPrice, startDate, endDate);
    }

    private UpdateMyItemRequestDto updateRequest() {
        String itemName = "TEST_ITEM";
        String itemDescription = "TEST_DESCRIPTION";
        Long minPrice = 100000L;
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        return new UpdateMyItemRequestDto(itemName, itemDescription, minPrice, startDate, endDate);
    }

    @Test
    @DisplayName("[성공] 경매 상품 생성")
    void createAuctionItem() {
        // given
        User user = createUser();
        CreateProductRequestDto request = createRequest();

        // when
        when(auctionItemRepository.save(any(AuctionItem.class))).thenReturn(any(AuctionItem.class));
        myAuctionItemService.createAuctionItem(request, user);

        // then
        ArgumentCaptor<AuctionItem> argumentCaptor = ArgumentCaptor.forClass(AuctionItem.class);
        verify(auctionItemRepository, times(1)).save(argumentCaptor.capture());
        AuctionItem capturedAuctionItem = argumentCaptor.getValue();

        assertEquals(request.getItemName(), capturedAuctionItem.getItemName());
        assertEquals(request.getItemDescription(), capturedAuctionItem.getItemDescription());
        assertEquals(request.getMinPrice(), capturedAuctionItem.getMinPrice());
        assertEquals(request.getStartDate(), capturedAuctionItem.getStartDate());
        assertEquals(request.getEndDate(), capturedAuctionItem.getEndDate());
    }

}
