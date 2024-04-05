package io.dcns.wantitauction.domain.pointLog.entity;

import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "point_logs", indexes = {
    @Index(columnList = "status")
})
@EntityListeners(AuditingEntityListener.class)
public class PointLog extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointLogId;

    @Column(nullable = false)
    private Long changedPoint;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointLogStatus status;

    @Column(nullable = false)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @Column
    private Long auctionItemId;

    public PointLog(Point point, PointRequestDto pointRequestDto, PointLogStatus pointLogStatus) {
        this.changedPoint = pointRequestDto.getChangedPoint();
        this.details = pointRequestDto.getDetails();
        this.status = pointLogStatus;
        this.point = point;
    }

    public PointLog(
        Long changedPoint, PointLogStatus status, String details, Point point, Long auctionItemId
    ) {
        this.changedPoint = changedPoint;
        this.status = status;
        this.details = details;
        this.point = point;
        this.auctionItemId = auctionItemId;
    }
}
