package io.dcns.wantitauction.domain.like.repository;

import static io.dcns.wantitauction.domain.like.entity.QLike.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.like.entity.Like;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Like> findAllByUserId(Long userId) {
        return jpaQueryFactory
            .selectFrom(like)
            .where(like.userId.eq(userId))
            .fetch();
    }
}


