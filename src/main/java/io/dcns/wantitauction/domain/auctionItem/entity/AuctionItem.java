package io.dcns.wantitauction.domain.auctionItem.entity;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import io.dcns.wantitauction.domain.auctionItem.dto.UpdateMyItemRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "auction_items")
@SQLDelete(sql = "update auction_items set deleted_at = NOW() where auction_item_id = ?")
@SQLRestriction(value = "deleted_at is NULL")
@NoArgsConstructor
public class AuctionItem extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionItemId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Long winnerId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String itemDescription;

    @Column(nullable = false)
    private Long minPrice;

    @Column
    private Long winPrice;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionItemEnum status = AuctionItemEnum.READY;

    @Column
    private LocalDateTime deletedAt;

    public AuctionItem(CreateProductRequestDto request, User user) {
        this.userId = user.getUserId();
        this.itemName = request.getItemName();
        this.itemDescription = request.getItemDescription();
        this.minPrice = request.getMinPrice();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }

    public void update(UpdateMyItemRequestDto request) {
        this.itemName = request.getItemName();
        this.itemDescription = request.getItemDescription();
        this.minPrice = request.getMinPrice();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
    }

    public void finishAuction(Long winnerId, Long winPrice) {
        this.winPrice = winPrice;
        this.status = AuctionItemEnum.FINISHED;
        this.winnerId = winnerId;
    }
}
