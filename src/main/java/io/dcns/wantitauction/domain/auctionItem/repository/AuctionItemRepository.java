package io.dcns.wantitauction.domain.auctionItem.repository;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {

    List<AuctionItem> findAllByUserId(Long userId);

    Optional<AuctionItem> findByAuctionItemIdAndUserId(Long auctionItemId, Long userId);

    boolean existsByAuctionItemIdAndUserId(Long auctionItemId, Long userId);

}
