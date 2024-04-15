package io.dcns.wantitauction.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.user.entity.QUser;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public List<User> getUserListWithPage(long offset, int pageSize) {

        QUser users = QUser.user;

        return jpaQueryFactory.selectFrom(users)
            .offset(offset)
            .limit(pageSize)
            .fetch();
    }



}
