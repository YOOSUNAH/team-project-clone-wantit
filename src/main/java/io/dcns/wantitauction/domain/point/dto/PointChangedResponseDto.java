package io.dcns.wantitauction.domain.point.dto;

import io.dcns.wantitauction.domain.point.entity.Point;
import io.dcns.wantitauction.domain.pointLog.entity.PointLog;
import io.dcns.wantitauction.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PointChangedResponseDto {

    private final Long pointId;

    private final Long pointLogId;

    private final Long userId;

    private final Long changedPoint;

    private final LocalDateTime changedDate;

    private final String details;

    public PointChangedResponseDto(User user, Point point, PointLog pointLog) {
        this.pointId = point.getPointId();
        this.userId = user.getUserId();
        this.pointLogId = pointLog.getPointLogId();
        this.changedPoint = pointLog.getChangedPoint();
        this.changedDate = pointLog.getChangedDate();
        this.details = pointLog.getDetails();
    }
}
