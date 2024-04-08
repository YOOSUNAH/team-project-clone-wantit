package io.dcns.wantitauction.domain.auctionItem.repository;

import static io.dcns.wantitauction.domain.auctionItem.entity.QAuctionItem.auctionItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionItemQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<FinishedItemResponseDto> findWinningAuctionItems(Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(FinishedItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED),
                auctionItem.winnerId.eq(userId))
            .fetch();
    }

    public FinishedItemResponseDto findWinningAuctionItem(Long auctionItemId, Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(FinishedItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED),
                auctionItem.auctionItemId.eq(auctionItemId),
                auctionItem.winnerId.eq(userId))
            .fetchOne();
    }

    public List<MyAuctionItemsResponseDto> findAllMyAuctionItems(Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(MyAuctionItemsResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.minPrice,
                auctionItem.winPrice,
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
                auctionItem.itemDescription,
                auctionItem.minPrice,
                auctionItem.winPrice,
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
                    auctionItem.itemDescription,
                    auctionItem.minPrice,
                    auctionItem.winPrice,
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
                auctionItem.itemDescription,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .fetch();
    }

    public List<AuctionItem> findAllEndAuctionItems() {
        return jpaQueryFactory
            .selectFrom(auctionItem)
            .where(
                auctionItem.status.eq(AuctionItemEnum.IN_PROGRESS)
                    .and(auctionItem.endDate.lt(LocalDateTime.now())))
            .fetch();
    }

    public List<AuctionItem> findAllOpenAuctionItems() {
        return jpaQueryFactory
            .selectFrom(auctionItem)
            .where(
                auctionItem.status.eq(AuctionItemEnum.READY)
                    .and(auctionItem.startDate.lt(LocalDateTime.now())))
            .fetch();
    }

    public List<AuctionItem> findAllTodayWinningAuctionItems() {
        return jpaQueryFactory
            .selectFrom(auctionItem)
            .where(
                auctionItem.status.eq(AuctionItemEnum.FINISHED)
                    .and(auctionItem.endDate.between(
                        LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
                        LocalDateTime.now().withHour(23).withMinute(59).withSecond(59))))
            .fetch();
    }
}
