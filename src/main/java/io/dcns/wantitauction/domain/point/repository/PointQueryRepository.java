package io.dcns.wantitauction.domain.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
