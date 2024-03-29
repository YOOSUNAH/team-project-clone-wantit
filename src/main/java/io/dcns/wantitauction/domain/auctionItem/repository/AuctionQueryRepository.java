package io.dcns.wantitauction.domain.auctionItem.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
