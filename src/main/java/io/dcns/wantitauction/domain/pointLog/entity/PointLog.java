package io.dcns.wantitauction.domain.pointLog.entity;

import io.dcns.wantitauction.domain.point.dto.PointRequestDto;
import io.dcns.wantitauction.domain.point.entity.Point;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "point_logs")
@EntityListeners(AuditingEntityListener.class)
public class PointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointLogId;

    @Column(nullable = false)
    private Long changedPoint;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime changedDate;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;

    public PointLog(Point point, PointRequestDto pointRequestDto) {
        this.changedPoint = pointRequestDto.getChangedPoint();
        this.details = pointRequestDto.getDetails();
        this.point = point;
    }
}
