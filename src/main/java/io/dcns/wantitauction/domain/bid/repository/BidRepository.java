package io.dcns.wantitauction.domain.bid.repository;

import io.dcns.wantitauction.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {

}
