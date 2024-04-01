package io.dcns.wantitauction.domain.like.repository;

import io.dcns.wantitauction.domain.like.entity.Like;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByUserId(Long userId);

    Optional<Like> findByAuctionItemIdAndUserId(Long auctionItemId, Long userId);
}
