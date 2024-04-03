package io.dcns.wantitauction.domain.point.entity;

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
@NoArgsConstructor
@Getter
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    private Long availablePoint;

    public Point(User user) {
        this.userId = user.getUserId();
        this.point = 0L;
        this.availablePoint = 0L;
    }

    public void changePoint(Long changedPoint) {
        this.point = this.point + changedPoint;
        this.availablePoint = this.availablePoint + changedPoint;
    }

    public void returnBidPoint(Long maxPrice) {
        this.availablePoint = this.availablePoint + maxPrice;
    }

    public void subtractPoint(Long availablePoint) {
        this.availablePoint = this.availablePoint - availablePoint;
    }
}
