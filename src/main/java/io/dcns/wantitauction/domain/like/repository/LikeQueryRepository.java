package io.dcns.wantitauction.domain.like.repository;

import static io.dcns.wantitauction.domain.like.entity.QLike.like;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.like.dto.LikeResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<LikeResponseDto> findAllByUserId(Long userId) {
        return jpaQueryFactory
            .select(Projections.fields(LikeResponseDto.class,
                like.userId,
                like.auctionItemId,
                like.liked))
            .from(like)
            .where(like.userId.eq(userId)
                .and(like.liked.isTrue()))
            .fetch();
    }
}


