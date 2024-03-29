package io.dcns.wantitauction.domain.like.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
