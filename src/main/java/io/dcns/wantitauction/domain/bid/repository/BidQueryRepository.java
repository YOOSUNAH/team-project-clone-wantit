package io.dcns.wantitauction.domain.bid.repository;

import static io.dcns.wantitauction.domain.bid.entity.QBid.bid;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.bid.dto.BidResponseDto;
import io.dcns.wantitauction.domain.bid.dto.TopAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<BidResponseDto> findAllBidsPageable(User user, Pageable pageable) {

        Long totalSize = jpaQueryFactory
            .select(Wildcard.count)
            .from(bid)
            .where(bid.userId.eq(user.getUserId()))
            .fetch()
            .get(0);

        List<BidResponseDto> allBids = jpaQueryFactory
            .select(Projections.fields(BidResponseDto.class,
                bid.bidId,
                bid.auctionItem.auctionItemId,
                bid.auctionItem.itemName,
                bid.userId,
                bid.bidPrice,
                bid.createdAt,
                bid.updatedAt
            ))
            .from(bid)
            .where(bid.userId.eq(user.getUserId()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(bid.createdAt.desc())
            .fetch();

        return PageableExecutionUtils.getPage(allBids, pageable, () -> totalSize);
    }

    public List<TopAuctionItemsResponseDto> findTop3AuctionItemsByBid() {
        return jpaQueryFactory
            .select(Projections.fields(TopAuctionItemsResponseDto.class,
                bid.auctionItem.auctionItemId,
                bid.auctionItem.itemName,
                bid.auctionItem.itemDescription,
                bid.auctionItem.imageUrl,
                bid.auctionItem.startDate,
                bid.auctionItem.endDate
            ))
            .from(bid)
            .where(bid.auctionItem.status.eq(AuctionItemEnum.IN_PROGRESS))
            .groupBy(bid.auctionItem.auctionItemId)
            .orderBy(bid.bidId.count().desc())
            .limit(3)
            .fetch();
    }
}
