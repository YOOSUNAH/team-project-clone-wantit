package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import io.dcns.wantitauction.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PointChangedResponseDto {

    private Long pointId;

    private Long pointLogId;

    private Long userId;

    private Long changedPoint;

    private LocalDateTime changedDate;

    private String details;

    public PointChangedResponseDto(User user, Point point, PointLog pointLog) {
        this.pointId = point.getPointId();
        this.userId = user.getUserId();
        this.pointLogId = pointLog.getPointLogId();
        this.changedPoint = pointLog.getChangedPoint();
        this.changedDate = pointLog.getChangedDate();
        this.details = pointLog.getDetails();
    }
}
