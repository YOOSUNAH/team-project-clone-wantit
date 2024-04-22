package io.dcns.wantitauction.domain.auctionItem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.ReadyItemPageableResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.ReadyItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemQueryRepository;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@ExtendWith(MockitoExtension.class)
class AuctionItemServiceTest {

    @Mock
    AuctionItemRepository auctionItemRepository;
    @Mock
    AuctionItemQueryRepository auctionItemQueryRepository;
    @InjectMocks
    AuctionItemService auctionItemService;

    AuctionItem testItem;

    @Nested
    @DisplayName("특정 경매 상품 조회")
    class GetTest {

        @Test
        @DisplayName("조회 성공")
        void getAuctionItem() {
            //given
            AuctionItem testItem = new AuctionItem(
                1L, 1L, null, "Test Item",
                "itemDescription", 100000L, 110000L,
                LocalDateTime.now(), LocalDateTime.now(), AuctionItemEnum.READY, null
            );
            given(auctionItemRepository.findById(anyLong())).willReturn(Optional.of(testItem));
            //when
            AuctionItemResponseDto responseDto = auctionItemService.getAuctionItem(anyLong());
            //then
            assertThat(responseDto.getAuctionItemId()).isEqualTo(
                new AuctionItemResponseDto(testItem).getAuctionItemId());
        }

        @Test
        @DisplayName("조회 실패")
        void getAuctionItemFail() {
            //given
            given(auctionItemRepository.findById(anyLong())).willThrow(
                new IllegalArgumentException("존재하지 않는 경매상품 입니다."));
            //when
            //then
            assertThatThrownBy(
                () -> auctionItemService.getAuctionItem(2L)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("경매 상품 목록 조회")
    class GetListTest {

        @Test
        @DisplayName("조회 성공")
        void getAuctionItemList() {
            //given
            int page = 0;
            int size = 3;
            Long totalSize = 1L;
            Pageable pageable = PageRequest.of(page, size);
            testItem = new AuctionItem(
                1L, 1L, null, "Test Item",
                "itemDescription", 100000L, 110000L,
                LocalDateTime.now(), LocalDateTime.now(), AuctionItemEnum.READY, null
            );
            List<AuctionItemResponseDto> auctionItems = List.of(
                new AuctionItemResponseDto(testItem),
                new AuctionItemResponseDto(testItem)
            );
            given(auctionItemQueryRepository.findAll(any(Pageable.class)))
                .willReturn(
                    PageableExecutionUtils.getPage(auctionItems, pageable, () -> totalSize)
                );
            //when
            AuctionItemPageableResponseDto responseDtoPage = auctionItemService.getAuctionItems(
                page, size);

            //then
            assertThat(responseDtoPage.getResponseDtoList().get(0).getAuctionItemId())
                .isEqualTo(testItem.getAuctionItemId());
        }
    }

    @Nested
    @DisplayName("낙찰된 경매 상품 조회")
    class GetFinishedTest {

        @Test
        @DisplayName("조회 성공")
        void getFinishedAuctionItem() {
            //given
            AuctionItem testItem = new AuctionItem(
                1L, 1L, null, "Test Item",
                "itemDescription", 100000L, 110000L,
                LocalDateTime.now(), LocalDateTime.now(), AuctionItemEnum.FINISHED, null
            );
            given(auctionItemQueryRepository.findByIdAndFinished(anyLong()))
                .willReturn(Optional.of(new FinishedItemResponseDto(testItem)));
            //when
            FinishedItemResponseDto responseDto = auctionItemService.getFinishedAuctionItem(1L);
            //then
            assertThat(responseDto.getAuctionItemId()).isEqualTo(
                new FinishedItemResponseDto(testItem).getAuctionItemId());
        }

        @Test
        @DisplayName("조회 실패")
        void getFinishedAuctionItemFail() {
            //given
            given(auctionItemQueryRepository.findByIdAndFinished(anyLong()))
                .willThrow(new IllegalArgumentException("존재하지 않는 상품 ID 입니다."));
            //when
            //then
            assertThatThrownBy(
                () -> auctionItemService.getFinishedAuctionItem(2L)
            ).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("종료 경매 상품 목록 조회")
    class GetFinishedListTest {

        @Test
        @DisplayName("조회 성공")
        void getFinishedAuctionItemList() {
            //given
            int page = 0;
            int size = 3;
            Long totalSize = 1L;
            Pageable pageable = PageRequest.of(page, size);
            testItem = new AuctionItem(
                1L, 1L, null, "Test Item",
                "itemDescription", 100000L, 110000L,
                LocalDateTime.now(), LocalDateTime.now(), AuctionItemEnum.FINISHED, null
            );
            List<FinishedItemResponseDto> auctionItems = List.of(
                new FinishedItemResponseDto(testItem),
                new FinishedItemResponseDto(testItem)
            );
            given(auctionItemQueryRepository.findAllByFinished(any(Pageable.class)))
                .willReturn(
                    PageableExecutionUtils.getPage(auctionItems, pageable, () -> totalSize)
                );
            //when
            FinishedItemPageableResponseDto responseDtoPage = auctionItemService.getFinishedAuctionItems(
                page, size);

            //then
            assertThat(responseDtoPage.getResponseDtoList().get(1).getAuctionItemId())
                .isEqualTo(testItem.getAuctionItemId());
        }
    }

    @Nested
    @DisplayName("예정 경매 상품 목록 조회")
    class GetReadyListTest {

        @Test
        @DisplayName("조회 성공")
        void getFinishedAuctionItemList() {
            //given
            int page = 0;
            int size = 3;
            Long totalSize = 1L;
            Pageable pageable = PageRequest.of(page, size);
            testItem = new AuctionItem(
                1L, 1L, null, "Test Item",
                "itemDescription", 100000L, 110000L,
                LocalDateTime.now(), LocalDateTime.now(), AuctionItemEnum.READY, null
            );
            List<ReadyItemResponseDto> auctionItems = List.of(
                new ReadyItemResponseDto(testItem),
                new ReadyItemResponseDto(testItem),
                new ReadyItemResponseDto(testItem)
            );
            given(auctionItemQueryRepository.findAllByReady(any(Pageable.class)))
                .willReturn(
                    PageableExecutionUtils.getPage(auctionItems, pageable, () -> totalSize)
                );
            //when
            ReadyItemPageableResponseDto responseDtoPage = auctionItemService.getReadyAuctionItems(
                page, size);

            //then
            assertThat(responseDtoPage.getResponseDtoList().get(1).getAuctionItemId())
                .isEqualTo(testItem.getAuctionItemId());
        }
    }

    @Nested
    @DisplayName("진행중인 경매 상품 목록 조회")
    class GetInProgressListTest {

        @Test
        @DisplayName("조회 성공")
        void getFinishedAuctionItemList() {
            //given
            int page = 0;
            int size = 3;
            Long totalSize = 1L;
            Pageable pageable = PageRequest.of(page, size);
            testItem = new AuctionItem(
                1L, 1L, null, "Test Item",
                "itemDescription", 100000L, 110000L,
                LocalDateTime.now(), LocalDateTime.now(), AuctionItemEnum.IN_PROGRESS, null
            );
            List<InProgressItemResponseDto> auctionItems = List.of(
                new InProgressItemResponseDto(testItem),
                new InProgressItemResponseDto(testItem),
                new InProgressItemResponseDto(testItem)
            );
            given(auctionItemQueryRepository.findAllByInProgress(any(Pageable.class)))
                .willReturn(
                    PageableExecutionUtils.getPage(auctionItems, pageable, () -> totalSize)
                );
            //when
            InProgressItemPageableResponseDto responseDtoPage = auctionItemService.getInProgressAuctionItems(
                page, size);

            //then
            assertThat(responseDtoPage.getResponseDtoList().get(1).getAuctionItemId())
                .isEqualTo(testItem.getAuctionItemId());
        }
    }

}
