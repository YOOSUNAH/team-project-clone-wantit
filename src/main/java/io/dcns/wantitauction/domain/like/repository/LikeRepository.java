package io.dcns.wantitauction.domain.like.repository;

import io.dcns.wantitauction.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByAuctionItemIdAndUserId(Long auctionItemId, Long userId);
}
