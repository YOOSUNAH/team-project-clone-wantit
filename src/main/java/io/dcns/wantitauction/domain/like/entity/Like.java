package io.dcns.wantitauction.domain.like.entity;

import io.dcns.wantitauction.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long auctionItemId;

    @Column(nullable = false)
    private boolean liked;

    public Like(
            User user,
            Long auctionItemId) {
        this.userId = user.getUserId();
        this.auctionItemId = auctionItemId;
        this.liked = true;  // 처음에는 true (좋아요)로 저장한다.
    }

    public void updateLikedStatus() {
        this.liked = !this.liked;  //update시 좋아요 상태를 변경해준다.
    }
}
