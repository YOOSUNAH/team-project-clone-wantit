package io.dcns.wantitauction.domain.bid.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
