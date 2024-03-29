package io.dcns.wantitauction.domain.auctionItem.repository;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {

}
