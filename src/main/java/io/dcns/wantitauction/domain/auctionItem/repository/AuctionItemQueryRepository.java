package io.dcns.wantitauction.domain.auctionItem.repository;

import static io.dcns.wantitauction.domain.auctionItem.entity.QAuctionItem.auctionItem;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.FinishedItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.auctionItem.dto.ReadyItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.auctionItem.entity.CategoryEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionItemQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<FinishedItemResponseDto> findWinningAuctionItems(Long userId, Pageable pageable) {
        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(auctionItem)
            .where(auctionItem.winnerId.eq(userId).and(
                auctionItem.status.eq(AuctionItemEnum.FINISHED)
            ))
            .fetch()
            .get(0);

        List<FinishedItemResponseDto> finishedItems = jpaQueryFactory
            .select(Projections.fields(FinishedItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate))
            .from(auctionItem)
            .where(auctionItem.winnerId.eq(userId).and(
                auctionItem.status.eq(AuctionItemEnum.FINISHED)
            ))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auctionItem.endDate.desc())
            .fetch();

        return PageableExecutionUtils.getPage(finishedItems, pageable, () -> totalSize);
    }

    public FinishedItemResponseDto findWinningAuctionItem(Long auctionItemId, Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(FinishedItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
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

    public Page<MyAuctionItemsResponseDto> findAllMyAuctionItems(Long userId, Pageable pageable) {

        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(auctionItem)
            .where(auctionItem.userId.eq(userId))
            .fetch()
            .get(0);

        List<MyAuctionItemsResponseDto> myAuctionItems = jpaQueryFactory
            .select(Projections.fields(MyAuctionItemsResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status))
            .from(auctionItem)
            .where(auctionItem.userId.eq(userId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auctionItem.createdAt.desc())
            .fetch();

        return PageableExecutionUtils.getPage(myAuctionItems, pageable, () -> totalSize);
    }

    public Page<FinishedItemResponseDto> findAllByFinished(Pageable pageable) {
        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED))
            .fetch()
            .get(0);

        List<FinishedItemResponseDto> finishedItems = jpaQueryFactory
            .select(Projections.fields(FinishedItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.imageUrl))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.FINISHED))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auctionItem.endDate.desc())
            .fetch();

        return PageableExecutionUtils.getPage(finishedItems, pageable, () -> totalSize);
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
                    auctionItem.category,
                    auctionItem.minPrice,
                    auctionItem.winPrice,
                    auctionItem.startDate,
                    auctionItem.endDate,
                    auctionItem.imageUrl))
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
                auctionItem.category,
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

    public Page<AuctionItemResponseDto> findAll(Pageable pageable) {
        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(auctionItem)
            .fetch()
            .get(0);

        List<AuctionItemResponseDto> auctionItems = jpaQueryFactory
            .select(Projections.fields(AuctionItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.status,
                auctionItem.imageUrl))
            .from(auctionItem)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auctionItem.createdAt.desc())
            .fetch();

        return PageableExecutionUtils.getPage(auctionItems, pageable, () -> totalSize);
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

    public Page<ReadyItemResponseDto> findAllByReady(Pageable pageable) {
        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.READY))
            .fetch()
            .get(0);

        List<ReadyItemResponseDto> readyItems = jpaQueryFactory
            .select(Projections.fields(ReadyItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.imageUrl))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.READY))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auctionItem.startDate.asc())
            .fetch();

        return PageableExecutionUtils.getPage(readyItems, pageable, () -> totalSize);
    }

    public Page<InProgressItemResponseDto> findAllByInProgress(Pageable pageable, String category) {
        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.IN_PROGRESS),
                categoryEq(category))
            .fetch()
            .get(0);

        List<InProgressItemResponseDto> readyItems = jpaQueryFactory
            .select(Projections.fields(InProgressItemResponseDto.class,
                auctionItem.auctionItemId,
                auctionItem.userId,
                auctionItem.winnerId,
                auctionItem.itemName,
                auctionItem.itemDescription,
                auctionItem.category,
                auctionItem.minPrice,
                auctionItem.winPrice,
                auctionItem.startDate,
                auctionItem.endDate,
                auctionItem.imageUrl))
            .from(auctionItem)
            .where(auctionItem.status.eq(AuctionItemEnum.IN_PROGRESS),
                categoryEq(category))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(auctionItem.startDate.desc())
            .fetch();

        return PageableExecutionUtils.getPage(readyItems, pageable, () -> totalSize);
    }

    private BooleanExpression categoryEq(String status) {
        if (status != null) {
            CategoryEnum categoryEnum = CategoryEnum.valueOf(status);
            return auctionItem.category.eq(categoryEnum);
        }
        return null;
    }
}
