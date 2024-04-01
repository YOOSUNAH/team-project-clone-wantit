package io.dcns.wantitauction.domain.auctionItem.repository;

import static io.dcns.wantitauction.domain.auctionItem.entity.QAuctionItem.auctionItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionItemQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MyAuctionItemsResponseDto> findWinningAuctionItems(Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(MyAuctionItemsResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.productDescription,
                auctionItem.minPrice,
                auctionItem.maxPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED),
                auctionItem.winnerId.eq(userId))
            .fetch();
    }

    public MyAuctionItemsResponseDto findWinningAuctionItem(Long auctionItemId, Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(MyAuctionItemsResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.itemName,
                auctionItem.productDescription,
                auctionItem.minPrice,
                auctionItem.maxPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED),
                auctionItem.auctionItemId.eq(auctionItemId),
                auctionItem.winnerId.eq(userId))
            .fetchOne();
    }

    public List<MyAuctionItemsResponseDto> findAll(Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(MyAuctionItemsResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.itemName,
                auctionItem.productDescription,
                auctionItem.minPrice,
                auctionItem.maxPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .where(auctionItem.userId.eq(userId))
            .fetch();
    }
}
