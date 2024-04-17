package io.dcns.wantitauction.domain.bid.entity;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.bid.dto.BidRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bids")
public class Bid extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;


    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long bidPrice;

    @ManyToOne
    @JoinColumn(name = "auction_item_id")
    private AuctionItem auctionItem;

    public Bid(User user, BidRequestDto bidRequestDto, AuctionItem auctionItem) {
        this.userId = user.getUserId();
        this.bidPrice = bidRequestDto.getBidPrice();
        this.auctionItem = auctionItem;
    }
}
