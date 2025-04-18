package io.dcns.wantitauction.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.admin.dto.AdminResponseDto;
import io.dcns.wantitauction.domain.user.entity.QUser;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<AdminResponseDto> findAll(Pageable pageable) {
        Long totalSize = jpaQueryFactory
                .select(Wildcard.count)
                .from(QUser.user)
                .fetch()
                .get(0);

        List<AdminResponseDto> users = jpaQueryFactory
                .select(Projections.fields(AdminResponseDto.class,
                        QUser.user.userId,
                        QUser.user.email,
                        QUser.user.password,
                        QUser.user.username,
                        QUser.user.nickname,
                        QUser.user.phoneNumber,
                        QUser.user.address,
                        QUser.user.kakaoId))
                .from(QUser.user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(users, pageable, () -> totalSize);
    }
}
