package io.dcns.wantitauction.domain.pointLog.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointLogQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
