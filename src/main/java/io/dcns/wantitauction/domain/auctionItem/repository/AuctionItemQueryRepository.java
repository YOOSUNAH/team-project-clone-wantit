package io.dcns.wantitauction.domain.auctionItem.repository;

import static io.dcns.wantitauction.domain.auctionItem.entity.QAuctionItem.auctionItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.util.List;
import java.util.Optional;
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

    public List<MyAuctionItemsResponseDto> findAllMine(Long userId) {
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

    public List<FinishedItemResponseDto> findAllByFinished() {
        return jpaQueryFactory
            .select(Projections.fields(FinishedItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.productDescription,
                auctionItem.minPrice,
                auctionItem.maxPrice,
                auctionItem.startDate,
                auctionItem.endDate))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED))
            .fetch();
    }

    public Optional<FinishedItemResponseDto> findByIdAndFinished(Long auctionItemId) {
        return Optional.ofNullable(
            jpaQueryFactory
                .select(Projections.fields(FinishedItemResponseDto.class,
                    auctionItem.auctionItemId,
                    auctionItem.userId,
                    auctionItem.winnerId,
                    auctionItem.itemName,
                    auctionItem.productDescription,
                    auctionItem.minPrice,
                    auctionItem.maxPrice,
                    auctionItem.startDate,
                    auctionItem.endDate))
                .from(auctionItem)
                .where(auctionItem.auctionItemId.eq(auctionItemId)
                    .and(auctionItem.status.eq(AuctionItemEnum.FINISHED)))
                .fetchFirst()
        );
    }

    public List<AuctionItemResponseDto> findAll() {
        return jpaQueryFactory
            .select(Projections.fields(AuctionItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.itemName,
                auctionItem.productDescription,
                auctionItem.minPrice,
                auctionItem.maxPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .fetch();
    }
}
