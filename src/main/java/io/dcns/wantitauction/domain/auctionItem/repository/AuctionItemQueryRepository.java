package io.dcns.wantitauction.domain.auctionItem.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dcns.wantitauction.domain.auctionItem.dto.MyAuctionItemsResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionItemQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MyAuctionItemsResponseDto> findFinishedAuctionItems(User user) {
        // TODO : 내가 낙찰받은 물품 조회
        return null;
    }
}
