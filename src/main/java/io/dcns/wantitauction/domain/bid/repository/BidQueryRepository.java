package io.dcns.wantitauction.domain.bid.repository;

import static io.dcns.wantitauction.domain.bid.entity.QBid.bid;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.dto.InProgressItemResponseDto;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import io.dcns.wantitauction.domain.bid.dto.BidResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<BidResponseDto> findAllBids(User user) {
        return jpaQueryFactory
            .select(Projections.fields(BidResponseDto.class,
                bid.bidId,
                bid.auctionItem.auctionItemId,
                bid.userId,
                bid.bidPrice,
                bid.createdAt,
                bid.updatedAt
            ))
            .from(bid)
            .where(bid.userId.eq(user.getUserId()))
            .orderBy(bid.createdAt.desc())
            .fetch();
    }

    public List<InProgressItemResponseDto> findTop3Bids() {
        return jpaQueryFactory
            .select(Projections.fields(InProgressItemResponseDto.class,
                bid.auctionItem.auctionItemId,
                bid.auctionItem.userId,
                bid.auctionItem.winnerId,
                bid.auctionItem.itemName,
                bid.auctionItem.itemDescription,
                bid.auctionItem.minPrice,
                bid.auctionItem.winPrice,
                bid.auctionItem.startDate,
                bid.auctionItem.endDate,
                bid.auctionItem.status
            ))
            .from(bid)
            .where(bid.auctionItem.status.eq(AuctionItemEnum.IN_PROGRESS))
            .groupBy(bid.auctionItem.auctionItemId)
            .orderBy(bid.bidId.count().desc())
            .limit(3)
            .fetch();
    }
}
