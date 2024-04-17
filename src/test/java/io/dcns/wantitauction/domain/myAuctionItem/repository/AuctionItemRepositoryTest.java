package io.dcns.wantitauction.domain.myAuctionItem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AuctionItemRepositoryTest {

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Test
    @DisplayName("[성공] 경매 상품 추가")
    void addAuctionItem() {
        // given
        AuctionItem auctionItem = AuctionItem.builder()
            .itemName("TEST_ITEM")
            .itemDescription("TEST_DESCRIPTION")
            .minPrice(100000L)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(1))
            .build();

        // when
        AuctionItem savedAuctionItem = auctionItemRepository.save(auctionItem);

        // then
        assertThat(savedAuctionItem.getAuctionItemId()).isEqualTo(auctionItem.getAuctionItemId());
    }
}
